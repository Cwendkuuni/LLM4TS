import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.finish();
        tarOutputStream.close();
    }

    @Test
    public void testPutSimpleEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(11);
        tarOutputStream.putArchiveEntry(entry);

        byte[] testData = "Hello World".getBytes();
        tarOutputStream.write(testData, 0, testData.length);
        tarOutputStream.closeArchiveEntry();

        byte[] expected = createExpectedTarFile("test.txt", 11, "Hello World");
        Assert.assertArrayEquals(expected, outputStream.toByteArray());
    }

    @Test
    public void testPutLongFileNameEntry() throws IOException {
        String longFileName = "a/long/path/to/test/file.txt";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        entry.setSize(11);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarOutputStream.putArchiveEntry(entry);

        byte[] testData = "Long file name test".getBytes();
        tarOutputStream.write(testData, 0, testData.length);
        tarOutputStream.closeArchiveEntry();

        byte[] expected = createExpectedTarFileWithLongFileName(longFileName, 11, "Long file name test");
        Assert.assertArrayEquals(expected, outputStream.toByteArray());
    }

    @Test(expected = IOException.class)
    public void testPutLongFileNameEntryWithErrorMode() throws IOException {
        String longFileName = "a/long/path/to/test/file.txt";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        entry.setSize(11);
        tarOutputStream.putArchiveEntry(entry);

        byte[] testData = "Long file name test".getBytes();
        tarOutputStream.write(testData, 0, testData.length);

        // Should throw an exception here
        tarOutputStream.closeArchiveEntry();
    }

    @Test
    public void testPutLongFileNameEntryWithTruncateMode() throws IOException {
        String longFileName = "a/long/path/to/test/file.txt";
        TarArchiveEntry entry = new TarArchiveEntry(longFileName);
        entry.setSize(11);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarOutputStream.putArchiveEntry(entry);

        byte[] testData = "Long file name test".getBytes();
        tarOutputStream.write(testData, 0, testData.length);
        tarOutputStream.closeArchiveEntry();

        byte[] expected = createExpectedTarFileWithLongFileNameTruncated(longFileName, 11, "Long file name test");
        Assert.assertArrayEquals(expected, outputStream.toByteArray());
    }

    @Test(expected = IOException.class)
    public void testPutErrorIfSizeMismatch() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(11);
        tarOutputStream.putArchiveEntry(entry);

        byte[] testData = "Hello World".getBytes();
        tarOutputStream.write(testData, 0, 12); // write more data than the size specified in the header

        tarOutputStream.closeArchiveEntry();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutNullEntry() throws IOException {
        tarOutputStream.putArchiveEntry(null);
    }

    private byte[] createExpectedTarFile(String fileName, long size, String content) throws IOException {
        byte[] header = TarArchiveEntry.createHeader(fileName, TarConstants.TAR_FILE_TYPE_REG, size, 0);
        byte[] contentBytes = content.getBytes();
        byte[] record = new byte[header.length + contentBytes.length];
        System.arraycopy(header, 0, record, 0, header.length);
        System.arraycopy(contentBytes, 0, record, header.length, contentBytes.length);
        return record;
    }

    private byte[] createExpectedTarFileWithLongFileName(String fileName, long size, String content) throws IOException {
        byte[] header = TarArchiveEntry.createHeader(fileName, TarConstants.TAR_FILE_TYPE_REG, size, 0, fileName.length());
        byte[] contentBytes = content.getBytes();
        byte[] record = new byte[header.length + contentBytes.length];
        System.arraycopy(header, 0, record, 0, header.length);
        System.arraycopy(contentBytes, 0, record, header.length, contentBytes.length);
        return record;
    }

    private byte[] createExpectedTarFileWithLongFileNameTruncated(String fileName, long size, String content) throws IOException {
        int nameLength = fileName.length();
        long truncatedSize = size + (TarConstants.NAMELEN + 1 + fileName.length());
        byte[] header = TarArchiveEntry.createHeader(fileName, TarConstants.TAR_FILE_TYPE_REG, truncatedSize, 0, nameLength);
        byte[] contentBytes = content.getBytes();
        byte[] record = new byte[header.length + contentBytes.length];
        System.arraycopy(header, 0, record, 0, header.length);
        System.arraycopy(contentBytes, 0, record, header.length, contentBytes.length);
        return record;
    }
}