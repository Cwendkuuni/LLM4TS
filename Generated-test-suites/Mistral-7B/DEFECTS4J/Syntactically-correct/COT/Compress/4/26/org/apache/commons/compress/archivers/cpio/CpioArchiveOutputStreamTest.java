import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.CpioConstants;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        String contents = "12345";
        entry.setFileSize(contents.length());
        entry.setMode(CpioConstants.C_ISREG);

        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write(contents.getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();

        Assert.assertEquals(ArchiveUtils.byteArrayToInputStream(outputStream.toByteArray()), cpioArchiveOutputStream.getInputStream());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryInvalidFormat() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, (short) 123);
    }

    @Test
    public void testPutArchiveEntryDuplicateEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName("testfile");
        String contents1 = "12345";
        entry1.setFileSize(contents1.length());
        entry1.setMode(CpioConstants.C_ISREG);

        cpioArchiveOutputStream.putArchiveEntry(entry1);
        cpioArchiveOutputStream.write(contents1.getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName("testfile");
        String contents2 = "67890";
        entry2.setFileSize(contents2.length());
        entry2.setMode(CpioConstants.C_ISREG);

        Assert.assertThrows(IOException.class, () -> cpioArchiveOutputStream.putArchiveEntry(entry2));

        Map<String, CpioArchiveEntry> entries = new HashMap<>();
        entries.put(entry1.getName(), entry1);
        Assert.assertThrows(IOException.class, () -> cpioArchiveOutputStream.putArchiveEntry(entry2));
    }

    @Test
    public void testPutArchiveEntryInvalidEntryName() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("\0");
        String contents = "12345";
        entry.setFileSize(contents.length());
        entry.setMode(CpioConstants.C_ISREG);

        Assert.assertThrows(IOException.class, () -> cpioArchiveOutputStream.putArchiveEntry(entry));
    }

    @Test
    public void testPutArchiveEntryNullEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        Assert.assertThrows(ClassCastException.class, () -> cpioArchiveOutputStream.putArchiveEntry(null));
    }

    @Test
    public void testWrite() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        String contents = "12345";
        entry.setFileSize(contents.length());
        entry.setMode(CpioConstants.C_ISREG);

        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write(contents.getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();

        byte[] outputBytes = outputStream.toByteArray();

        InputStream inputStream = new ByteArrayInputStream(outputBytes);
        int read = 0;
        byte[] buffer = new byte[1];

        while ((read = inputStream.read(buffer)) > 0) {
            Assert.assertEquals(contents.charAt(read - contents.length()), buffer[0]);
        }
    }

    @Test
    public void testWriteEmptyData() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(0);
        entry.setMode(CpioConstants.C_ISREG);

        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write("");
        cpioArchiveOutputStream.closeArchiveEntry();

        byte[] outputBytes = outputStream.toByteArray();

        InputStream inputStream = new ByteArrayInputStream(outputBytes);
        int read = 0;
        byte[] buffer = new byte[1];

        while ((read = inputStream.read(buffer)) > 0) {
            Assert.assertEquals('\0', buffer[0]);
        }
    }

    @Test
    public void testWriteInvalidDataSize() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        String contents = "12345";
        entry.setFileSize(contents.length() + 1);
        entry.setMode(CpioConstants.C_ISREG);

        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write(contents.getBytes());

        Assert.assertThrows(IOException.class, () -> cpioArchiveOutputStream.closeArchiveEntry());
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        String contents = "12345";
        entry.setFileSize(contents.length());
        entry.setMode(CpioConstants.C_ISREG);

        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write(contents.getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();

        Assert.assertFalse(cpioArchiveOutputStream.isOpen());
        Assert.assertNull(cpioArchiveOutputStream.getEntry());
    }

    @Test
    public void testFinish() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        String contents = "12345";
        entry.setFileSize(contents.length());
        entry.setMode(CpioConstants.C_ISREG);

        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write(contents.getBytes());
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.finish();

        byte[] outputBytes = outputStream.toByteArray();
        InputStream inputStream = new ByteArrayInputStream(outputBytes);

        int read = 0;
        byte[] buffer = new byte[1];

        while ((read = inputStream.read(buffer)) > 0) {
            Assert.assertEquals('\0', buffer[0]);
        }

        Assert.assertTrue(cpioArchiveOutputStream.isFinished());
    }

    @Test
    public void testFinishAlreadyFinished() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        cpioArchiveOutputStream.finish();

        Assert.assertTrue(cpioArchiveOutputStream.isFinished());

        cpioArchiveOutputStream.finish();

        Assert.assertTrue(cpioArchiveOutputStream.isFinished());
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        cpioArchiveOutputStream.finish();
        cpioArchiveOutputStream.close();

        Assert.assertTrue(cpioArchiveOutputStream.isClosed());
    }

    @Test
    public void testCloseAlreadyClosed() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        cpioArchiveOutputStream.close();

        Assert.assertTrue(cpioArchiveOutputStream.isClosed());

        cpioArchiveOutputStream.close();

        Assert.assertTrue(cpioArchiveOutputStream.isClosed());
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File testFile = new File("test.txt");
        testFile.createNewFile();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        CpioArchiveEntry entry = cpioArchiveOutputStream.createArchiveEntry(testFile, "testfile");

        Assert.assertEquals("testfile", entry.getName());
        Assert.assertEquals(testFile.length(), entry.getSize());
        Assert.assertEquals(CpioConstants.C_ISREG, entry.getMode());

        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write(testFile.length() > 0 ? new byte[testFile.length()] : new byte[0]);
        cpioArchiveOutputStream.closeArchiveEntry();

        testFile.delete();
    }

    @Test
    public void testCreateArchiveEntryNullFile() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        Assert.assertNull(cpioArchiveOutputStream.createArchiveEntry(null, "testfile"));
    }

    @Test
    public void testCreateArchiveEntryNullFileName() throws IOException {
        File testFile = new File("test.txt");
        testFile.createNewFile();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_NEW);

        Assert.assertNull(cpioArchiveOutputStream.createArchiveEntry(testFile, null));

        testFile.delete();
    }
}