import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveOutputStreamUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry("testFile.txt");
        entry.setSize(10);
        cpioOutputStream.putArchiveEntry(entry);

        byte[] testFileContent = "Test file content".getBytes();
        cpioOutputStream.write(testFileContent, 0, testFileContent.length);
        cpioOutputStream.closeArchiveEntry();

        cpioOutputStream.finish();
        cpioOutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveInputStream archiveInputStream = new CpioArchiveInputStream(inputStream);

        Assert.assertEquals("testFile.txt", archiveInputStream.getNextEntry().getName());
        byte[] readData = new byte[10];
        int read = archiveInputStream.read(readData, 0, readData.length);
        Assert.assertArrayEquals(testFileContent, readData);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryWithInvalidFormat() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, (short) 10);
        cpioOutputStream.putArchiveEntry(new CpioArchiveEntry("testFile.txt"));
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithDuplicateEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry1 = new CpioArchiveEntry("testFile.txt");
        cpioOutputStream.putArchiveEntry(entry1);

        CpioArchiveEntry entry2 = new CpioArchiveEntry("testFile.txt");
        cpioOutputStream.putArchiveEntry(entry2);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        cpioOutputStream.putArchiveEntry(null);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullEntryName() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveEntry entry = new CpioArchiveEntry(null);
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        cpioOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithEmptyEntryName() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveEntry entry = new CpioArchiveEntry("");
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        cpioOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithZeroSizeEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveEntry entry = new CpioArchiveEntry("testFile.txt");
        entry.setSize(0);
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        cpioOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithInvalidEntryFormat() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveEntry entry = new CpioArchiveEntry("testFile.txt");
        entry.setFormat((short) 10);
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        cpioOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullOutputStream() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry("testFile.txt");
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(null, CpioConstants.FORMAT_NEW);
        cpioOutputStream.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testWriteWithInvalidOffset() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        byte[] testData = "Test data".getBytes();
        cpioOutputStream.putArchiveEntry(new CpioArchiveEntry("testFile.txt"));
        cpioOutputStream.write(testData, -1, testData.length);
    }

    @Test(expected = IOException.class)
    public void testWriteWithInvalidLength() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        byte[] testData = "Test data".getBytes();
        cpioOutputStream.putArchiveEntry(new CpioArchiveEntry("testFile.txt"));
        cpioOutputStream.write(testData, 0, testData.length + 1);
    }

    @Test(expected = IOException.class)
    public void testWriteWithNullData() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        cpioOutputStream.putArchiveEntry(new CpioArchiveEntry("testFile.txt"));
        cpioOutputStream.write(null, 0, 0);
    }

    @Test(expected = IOException.class)
    public void testWriteWithNullOutputStream() throws IOException {
        byte[] testData = "Test data".getBytes();
        CpioArchiveEntry entry = new CpioArchiveEntry("testFile.txt");
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(null, CpioConstants.FORMAT_NEW);
        cpioOutputStream.putArchiveEntry(entry);
        cpioOutputStream.write(testData, 0, testData.length);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testWriteWithInvalidIndex() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        byte[] testData = "Test data".getBytes();
        cpioOutputStream.putArchiveEntry(new CpioArchiveEntry("testFile.txt"));
        cpioOutputStream.write(testData, -1, testData.length);
    }

    @Test(expected = IOException.class)
    public void testWriteWithWritingBeyondEntrySize() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        byte[] testData = "Test data".getBytes();
        cpioOutputStream.putArchiveEntry(new CpioArchiveEntry("testFile.txt"));
        cpioOutputStream.write(testData, 0, testData.length + 1);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithClosedStream() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        cpioOutputStream.close();
        cpioOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithNullEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        cpioOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testFinishWithClosedStream() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        cpioOutputStream.finish();
        cpioOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testFinishWithNullOutputStream() throws IOException {
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(null, CpioConstants.FORMAT_NEW);
        cpioOutputStream.finish();
    }

    @Test(expected = IOException.class)
    public void testCloseWithClosedStream() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);
        cpioOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testCloseWithNullOutputStream() throws IOException {
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(null, CpioConstants.FORMAT_NEW);
        cpioOutputStream.close();
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File testFile = new File("testFile.txt");
        CpioArchiveEntry entry = cpioOutputStream.createArchiveEntry(testFile, "testFile.txt");
        Assert.assertEquals("testFile.txt", entry.getName());
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateArchiveEntryWithNonExistentFile() throws IOException {
        CpioArchiveEntry entry = cpioOutputStream.createArchiveEntry(new File("nonExistentFile.txt"), "testFile.txt");
    }

    @Test
    public void testPad() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        byte[] testData = "Test data".getBytes();
        cpioOutputStream.putArchiveEntry(new CpioArchiveEntry("testFile.txt"));
        cpioOutputStream.write(testData, 0, testData.length);
        cpioOutputStream.closeArchiveEntry();
        cpioOutputStream.finish();
        cpioOutputStream.close();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveInputStream archiveInputStream = new CpioArchiveInputStream(inputStream);

        byte[] readData = new byte[10];
        int read = archiveInputStream.read(readData, 0, readData.length);
        Assert.assertArrayEquals(testData, readData);

        ArchiveUtils.inputStreamToByteArray(archiveInputStream);
        byte[] archiveData = ArchiveOutputStreamUtils.toByteArray(archiveInputStream);

        int padCount = 0;
        switch (cpioOutputStream.getEntryFormat()) {
            case CpioConstants.FORMAT_NEW:
                padCount = 8;
                break;
            case CpioConstants.FORMAT_NEW_CRC:
                padCount = 12;
                break;
            case CpioConstants.FORMAT_OLD_ASCII:
                padCount = 12;
                break;
            case CpioConstants.FORMAT_OLD_BINARY:
                padCount = 2;
                break;
        }

        byte[] expectedPad = new byte[padCount];
        for (int i = 0; i < padCount; i++) {
            expectedPad[i] = (byte) 0xFF;
        }

        int index = archiveData.length - padCount;
        for (int i = 0; i < padCount; i++) {
            Assert.assertEquals(expectedPad[i], archiveData[index + i]);
        }
    }
}