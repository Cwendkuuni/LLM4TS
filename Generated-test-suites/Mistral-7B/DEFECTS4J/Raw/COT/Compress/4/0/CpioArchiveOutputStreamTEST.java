import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntry() throws IOException {
        OutputStream outputStream = new FileOutputStream("test.cpio");
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioArchiveOutputStream.FORMAT_NEW);

        String testFileContents = "Test Contents";
        String testFileName = "testfile.txt";
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setFileSize(testFileContents.length());
        entry.setMode(CpioConstants.C_ISREG); // regular file
        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.write(testFileContents.getBytes());
        cpioOutputStream.closeArchiveEntry();

        byte[] expectedHeader = new byte[1024];
        ArchiveUtils.copyStreamToByteArray(cpioOutputStream.getOutputStream(), expectedHeader, 0, 1024);

        byte[] expectedFileContent = testFileContents.getBytes();
        byte[] actualFileContent = new byte[expectedFileContent.length];
        cpioOutputStream.read(actualFileContent, 0, expectedFileContent.length);

        Assert.assertArrayEquals("Unexpected header in output file", expectedHeader, cpioOutputStream.getOutputStream().readAllBytes());
        Assert.assertArrayEquals("Unexpected file content in output file", expectedFileContent, actualFileContent);

        cpioOutputStream.close();
        outputStream.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryWithInvalidFormat() throws IOException {
        OutputStream outputStream = new FileOutputStream("test.cpio");
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, (short) 6);
        cpioOutputStream.putArchiveEntry(new CpioArchiveEntry());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithDuplicateEntry() throws IOException {
        OutputStream outputStream = new FileOutputStream("test.cpio");
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioArchiveOutputStream.FORMAT_NEW);
        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("testfile.txt");
        cpioOutputStream.putArchiveEntry(entry1);
        cpioOutputStream.write("Test Contents".getBytes());
        cpioOutputStream.closeArchiveEntry();

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("testfile.txt");
        cpioOutputStream.putArchiveEntry(entry2);
    }

    @Test
    public void testWriteByteArray() throws IOException {
        OutputStream outputStream = new FileOutputStream("test.cpio");
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioArchiveOutputStream.FORMAT_NEW);

        byte[] testData = {0x01, 0x02, 0x03};
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile.bin");
        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.write(testData, 0, testData.length);
        cpioOutputStream.closeArchiveEntry();

        byte[] expectedData = {0x01, 0x02, 0x03};
        byte[] actualData = new byte[expectedData.length];
        cpioOutputStream.read(actualData, 0, expectedData.length);

        Assert.assertArrayEquals("Unexpected file content in output file", expectedData, actualData);

        cpioOutputStream.close();
        outputStream.close();
    }

    @Test
    public void testPutNextEntryWithNullEntry() throws IOException {
        OutputStream outputStream = new FileOutputStream("test.cpio");
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioArchiveOutputStream.FORMAT_NEW);

        cpioOutputStream.putNextEntry(null);
    }

    @Test(expected = ClassCastException.class)
    public void testPutNextEntryWithInvalidEntry() throws IOException {
        OutputStream outputStream = new FileOutputStream("test.cpio");
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioArchiveOutputStream.FORMAT_NEW);

        Object invalidEntry = new Object();
        cpioOutputStream.putNextEntry(invalidEntry);
    }

    @Test(expected = IOException.class)
    public void testPutNextEntryWithOpenEntry() throws IOException {
        OutputStream outputStream = new FileOutputStream("test.cpio");
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioArchiveOutputStream.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.write("Test Contents".getBytes());
        cpioOutputStream.closeArchiveEntry();

        cpioOutputStream.putNextEntry(entry);
    }

    @Test
    public void testFinish() throws IOException {
        OutputStream outputStream = new FileOutputStream("test.cpio");
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioArchiveOutputStream.FORMAT_NEW);

        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("testfile1.txt");
        cpioOutputStream.putArchiveEntry(entry1);
        cpioOutputStream.write("Test Contents 1".getBytes());
        cpioOutputStream.closeArchiveEntry();

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("testfile2.txt");
        cpioOutputStream.putArchiveEntry(entry2);
        cpioOutputStream.write("Test Contents 2".getBytes());
        cpioOutputStream.closeArchiveEntry();

        cpioOutputStream.finish();

        byte[] expectedHeader = new byte[1024];
        ArchiveUtils.copyStreamToByteArray(cpioOutputStream.getOutputStream(), expectedHeader, 0, 1024);

        byte[] testFile1Content = "Test Contents 1".getBytes();
        byte[] testFile2Content = "Test Contents 2".getBytes();

        byte[] actualFileContent1 = new byte[testFile1Content.length];
        cpioOutputStream.read(actualFileContent1, 0, testFile1Content.length);

        byte[] actualFileContent2 = new byte[testFile2Content.length];
        cpioOutputStream.read(actualFileContent2, 0, testFile2Content.length);

        Assert.assertArrayEquals("Unexpected header in output file", expectedHeader, cpioOutputStream.getOutputStream().readAllBytes());
        Assert.assertArrayEquals("Unexpected file content for testfile1.txt in output file", testFile1Content, actualFileContent1);
        Assert.assertArrayEquals("Unexpected file content for testfile2.txt in output file", testFile2Content, actualFileContent2);

        cpioOutputStream.close();
        outputStream.close();
    }

    @Test
    public void testClose() throws IOException {
        OutputStream outputStream = new FileOutputStream("test.cpio");
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioArchiveOutputStream.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile.txt");
        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.write("Test Contents".getBytes());
        cpioOutputStream.closeArchiveEntry();

        cpioOutputStream.close();
        outputStream.close();
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File testFile = new File("test.txt");
        OutputStream outputStream = new FileOutputStream(testFile);
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioArchiveOutputStream.FORMAT_NEW);

        ArchiveEntry archiveEntry = cpioOutputStream.createArchiveEntry(testFile, "testfile.txt");
        cpioOutputStream.putArchiveEntry(archiveEntry);
        cpioOutputStream.write("Test Contents".getBytes());
        cpioOutputStream.closeArchiveEntry();

        byte[] expectedFileContent = "Test Contents".getBytes();
        byte[] actualFileContent = new byte[expectedFileContent.length];
        FileInputStream testFileInputStream = new FileInputStream(testFile);
        testFileInputStream.read(actualFileContent, 0, expectedFileContent.length);

        Assert.assertArrayEquals("Unexpected file content in test file", expectedFileContent, actualFileContent);

        testFile.delete();
        cpioOutputStream.close();
        outputStream.close();
    }
}