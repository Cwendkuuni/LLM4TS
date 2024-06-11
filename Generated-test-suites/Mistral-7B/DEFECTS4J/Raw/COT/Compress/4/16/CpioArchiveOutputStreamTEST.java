import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntry() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(byteArrayOutputStream, CpioArchiveOutputStream.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);
        entry.setMode(CpioConstants.C_ISREG); // regular file

        cpioArchiveOutputStream.putArchiveEntry(entry);

        Assert.assertEquals(ArchiveUtils.MAGIC_NEW, byteArrayOutputStream.toByteArray(), 0, 8);
        Assert.assertEquals(entry.getName().getBytes().length + 1, byteArrayOutputStream.toByteArray(), 8, 8);
        Assert.assertEquals(entry.getMode(), byteArrayOutputStream.toByteArray(), 16, 8);
        // other assertions for other fields

        cpioArchiveOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithDuplicateName() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(byteArrayOutputStream, CpioArchiveOutputStream.FORMAT_NEW);

        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("testfile");
        cpioArchiveOutputStream.putArchiveEntry(entry1);

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("testfile");
        cpioArchiveOutputStream.putArchiveEntry(entry2);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithInvalidFormat() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(byteArrayOutputStream, (short) 123);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        cpioArchiveOutputStream.putArchiveEntry(entry);
    }

    @Test
    public void testWrite() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(byteArrayOutputStream, CpioArchiveOutputStream.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);
        entry.setMode(CpioConstants.C_ISREG); // regular file

        cpioArchiveOutputStream.putArchiveEntry(entry);
        byte[] testBytes = "test".getBytes();
        cpioArchiveOutputStream.write(testBytes, 0, testBytes.length);

        Assert.assertEquals(testBytes.length + 10, byteArrayOutputStream.toByteArray().length);
        // other assertions for the written data

        cpioArchiveOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testWriteWithoutEntry() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(byteArrayOutputStream, CpioArchiveOutputStream.FORMAT_NEW);

        byte[] testBytes = "test".getBytes();
        cpioArchiveOutputStream.write(testBytes, 0, testBytes.length);
    }

    @Test
    public void testFinish() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(byteArrayOutputStream, CpioArchiveOutputStream.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);
        entry.setMode(CpioConstants.C_ISREG); // regular file

        cpioArchiveOutputStream.putArchiveEntry(entry);
        byte[] testBytes = "test".getBytes();
        cpioArchiveOutputStream.write(testBytes, 0, testBytes.length);

        cpioArchiveOutputStream.finish();

        Assert.assertEquals(ArchiveUtils.MAGIC_TRAILER, byteArrayOutputStream.toByteArray(), 0, 8);

        cpioArchiveOutputStream.close();
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(byteArrayOutputStream, CpioArchiveOutputStream.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);
        entry.setMode(CpioConstants.C_ISREG); // regular file

        cpioArchiveOutputStream.putArchiveEntry(entry);
        byte[] testBytes = "test".getBytes();
        cpioArchiveOutputStream.write(testBytes, 0, testBytes.length);

        cpioArchiveOutputStream.close();

        Assert.assertEquals(ArchiveUtils.MAGIC_TRAILER, byteArrayOutputStream.toByteArray(), 0, 8);
    }

    @Test(expected = IOException.class)
    public void testCloseTwice() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(byteArrayOutputStream, CpioArchiveOutputStream.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(10);
        entry.setMode(CpioConstants.C_ISREG); // regular file

        cpioArchiveOutputStream.putArchiveEntry(entry);
        byte[] testBytes = "test".getBytes();
        cpioArchiveOutputStream.write(testBytes, 0, testBytes.length);

        cpioArchiveOutputStream.close();
        cpioArchiveOutputStream.close();
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File testFile = new File("test.txt");
        testFile.createNewFile();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(byteArrayOutputStream, CpioArchiveOutputStream.FORMAT_NEW);

        ArchiveEntry archiveEntry = cpioArchiveOutputStream.createArchiveEntry(testFile, "testfile");

        Assert.assertEquals("testfile", archiveEntry.getName());
        Assert.assertEquals(testFile.length(), archiveEntry.getSize());

        cpioArchiveOutputStream.close();
        testFile.delete();
    }
}