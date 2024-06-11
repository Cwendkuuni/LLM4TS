import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Test class for CpioArchiveOutputStream
 */
public class CpioArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream target = new FilterOutputStream(outputStream);
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(target, CpioConstants.FORMAT_NEW);

        String testFileName = "testFile";
        String testContent = "testContent";

        File testFile = new File("testFile.txt");
        testFile.createNewFile();
        try (OutputStream fileOutputStream = new FileOutputStream(testFile)) {
            fileOutputStream.write(testContent.getBytes());
        }

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setFileSize(testContent.length());
        entry.setMode(CpioConstants.C_ISREG); // regular file

        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write(testContent.getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();

        Assert.assertEquals(testContent.length(), cpioArchiveOutputStream.getSize());

        byte[] expectedResult = ArchiveUtils.buildTestCpioFile(new File("testFile.cpio"), new byte[0][], new String[]{testFileName});
        Assert.assertArrayEquals(expectedResult, outputStream.toByteArray());

        testFile.delete();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithDuplicateName() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream target = new FilterOutputStream(outputStream);
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(target, CpioConstants.FORMAT_NEW);

        String testFileName = "testFile";

        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName(testFileName);
        entry1.setFileSize(0);
        entry1.setMode(CpioConstants.C_ISREG); // regular file
        cpioArchiveOutputStream.putArchiveEntry(entry1);

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName(testFileName);
        cpioArchiveOutputStream.putArchiveEntry(entry2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryWithInvalidFormat() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream target = new FilterOutputStream(outputStream);
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(target, (short) 123);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream target = new FilterOutputStream(outputStream);
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(target, CpioConstants.FORMAT_NEW);
        cpioArchiveOutputStream.putArchiveEntry(null);
    }

    @Test(expected = ClassCastException.class)
    public void testPutArchiveEntryWithInvalidEntryType() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream target = new FilterOutputStream(outputStream);
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(target, CpioConstants.FORMAT_NEW);
        cpioArchiveOutputStream.putArchiveEntry(new Object());
    }

    @Test
    public void testWriteWithExistingEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream target = new FilterOutputStream(outputStream);
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(target, CpioConstants.FORMAT_NEW);

        String testFileName = "testFile";
        String testContent = "testContent";

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setFileSize(testContent.length());
        entry.setMode(CpioConstants.C_ISREG); // regular file
        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write(testContent.getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();

        byte[] dataToWrite = "newData".getBytes();

        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write(dataToWrite);
        cpioArchiveOutputStream.closeArchiveEntry();

        Assert.assertEquals(testContent.length() + dataToWrite.length, cpioArchiveOutputStream.getSize());
    }

    @Test(expected = IOException.class)
    public void testWriteWithNullData() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream target = new FilterOutputStream(outputStream);
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(target, CpioConstants.FORMAT_NEW);

        String testFileName = "testFile";
        String testContent = "testContent";

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setFileSize(testContent.length());
        entry.setMode(CpioConstants.C_ISREG); // regular file
        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write(testContent.getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.write(null);
    }

    @Test(expected = IOException.class)
    public void testWriteWithNonExistentEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream target = new FilterOutputStream(outputStream);
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(target, CpioConstants.FORMAT_NEW);

        String testFileName = "testFile";
        String testContent = "testContent";

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setFileSize(testContent.length());
        entry.setMode(CpioConstants.C_ISREG); // regular file
        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write(testContent.getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.putArchiveEntry(entry); // already exists
        cpioArchiveOutputStream.write(testContent.getBytes()); // should throw an exception
    }

    @Test
    public void testFinish() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream target = new FilterOutputStream(outputStream);
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(target, CpioConstants.FORMAT_NEW);

        String testFileName = "testFile";
        String testContent = "testContent";

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setFileSize(testContent.length());
        entry.setMode(CpioConstants.C_ISREG); // regular file
        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write(testContent.getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.finish();
        Assert.assertTrue(cpioArchiveOutputStream.isClosed());
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        OutputStream target = new FilterOutputStream(outputStream);
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(target, CpioConstants.FORMAT_NEW);

        String testFileName = "testFile";
        String testContent = "testContent";

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(testFileName);
        entry.setFileSize(testContent.length());
        entry.setMode(CpioConstants.C_ISREG); // regular file
        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write(testContent.getBytes());

        cpioArchiveOutputStream.finish();

        cpioArchiveOutputStream.putArchiveEntry(entry); // should throw an exception
    }
}