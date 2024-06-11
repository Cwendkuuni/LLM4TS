import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.archivers.tar.TarOutputStream;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class TarArchiveOutputStreamTest {

    private TarOutputStream tarOutputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
    }

    @Test
    public void testConstructor() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        Assert.assertNotNull(tarOutputStream);
    }

    @Test
    public void testConstructorWithBlockSize() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 1024);
        Assert.assertNotNull(tarOutputStream);
    }

    @Test
    public void testConstructorWithBlockSizeAndRecordSize() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), 1024, 512);
        Assert.assertNotNull(tarOutputStream);
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        Assert.assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.getLongFileMode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetLongFileModeWithInvalidValue() {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarOutputStream.setLongFileMode(4);
    }

    @Test
    public void testFinish() throws IOException {
        tarOutputStream.putNextEntry(new TarArchiveEntry("test.txt"));
        tarOutputStream.write("Hello World!".getBytes(StandardCharsets.UTF_8));
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();

        byte[] expectedEOFRecord = new byte[TarConstants.STANDARD_RECORD_LENGTH];
        Arrays.fill(expectedEOFRecord, (byte) 0);

        byte[] actualBytes = outputStream.toByteArray();
        byte[] headerBytes = new byte[TarConstants.STANDARD_RECORD_LENGTH];
        System.arraycopy(actualBytes, 0, headerBytes, 0, TarConstants.STANDARD_RECORD_LENGTH);

        TarEntryHeader header = new TarEntryHeader(headerBytes);
        Assert.assertEquals("test.txt", header.getName());
        Assert.assertEquals("test.txt\0", header.getFileName());
        Assert.assertEquals("", header.getFileComment());
        Assert.assertEquals(0, header.getFileHardLinkNumber());
        Assert.assertEquals(0, header.getFilePermissions());
        Assert.assertEquals(0, header.getFileUID());
        Assert.assertEquals(0, header.getFileGID());
        Assert.assertEquals(0, header.getFileModTime());
        Assert.assertEquals(0, header.getFileModDate());
        Assert.assertEquals(0, header.getFileSize());
        Assert.assertEquals(0, header.getFileMajorVersion());
        Assert.assertEquals(0, header.getFileMinorVersion());
        Assert.assertEquals(0, header.getFileTypeFlag());
        Assert.assertEquals(0, header.getFileLinkNameOffset());
        Assert.assertEquals(0, header.getFileLinkNameLength());

        Assert.assertArrayEquals(expectedEOFRecord, actualBytes, TarConstants.STANDARD_RECORD_LENGTH + TarConstants.STANDARD_RECORD_LENGTH);
    }

    @Test
    public void testClose() throws IOException {
        tarOutputStream.putNextEntry(new TarArchiveEntry("test.txt"));
        tarOutputStream.write("Hello World!".getBytes(StandardCharsets.UTF_8));
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.close();

        byte[] expectedEOFRecord = new byte[TarConstants.STANDARD_RECORD_LENGTH];
        Arrays.fill(expectedEOFRecord, (byte) 0);

        byte[] actualBytes = outputStream.toByteArray();
        byte[] headerBytes = new byte[TarConstants.STANDARD_RECORD_LENGTH];
        System.arraycopy(actualBytes, 0, headerBytes, 0, TarConstants.STANDARD_RECORD_LENGTH);

        TarEntryHeader header = new TarEntryHeader(headerBytes);
        Assert.assertEquals("test.txt", header.getName());
        Assert.assertEquals("test.txt\0", header.getFileName());
        Assert.assertEquals("", header.getFileComment());
        Assert.assertEquals(0, header.getFileHardLinkNumber());
        Assert.assertEquals(0, header.getFilePermissions());
        Assert.assertEquals(0, header.getFileUID());
        Assert.assertEquals(0, header.getFileGID());
        Assert.assertEquals(0, header.getFileModTime());
        Assert.assertEquals(0, header.getFileModDate());
        Assert.assertEquals(0, header.getFileSize());
        Assert.assertEquals(0, header.getFileMajorVersion());
        Assert.assertEquals(0, header.getFileMinorVersion());
        Assert.assertEquals(0, header.getFileTypeFlag());
        Assert.assertEquals(0, header.getFileLinkNameOffset());
        Assert.assertEquals(0, header.getFileLinkNameLength());

        Assert.assertArrayEquals(expectedEOFRecord, actualBytes, TarConstants.STANDARD_RECORD_LENGTH + TarConstants.STANDARD_RECORD_LENGTH);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullArchiveEntry() throws IOException {
        tarOutputStream.putArchiveEntry(null);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        tarOutputStream.putNextEntry(new TarArchiveEntry("test.txt"));

        TarArchiveEntry entry = tarOutputStream.getNextEntry();
        Assert.assertEquals("test.txt", entry.getName());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithInvalidArchiveEntry() throws IOException {
        tarOutputStream.putNextEntry(new Object());
    }

    @Test
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        String longFileName = "a" + new String(new char[TarConstants.NAMELEN + 1]) + "b";
        tarOutputStream.putNextEntry(new TarArchiveEntry(longFileName));

        byte[] expectedLongFileNameBytes = longFileName.getBytes(StandardCharsets.UTF_8);

        byte[] actualBytes = outputStream.toByteArray();

        byte[] headerBytes = new byte[TarConstants.STANDARD_RECORD_LENGTH];
        System.arraycopy(actualBytes, 0, headerBytes, 0, TarConstants.STANDARD_RECORD_LENGTH);

        TarEntryHeader header = new TarEntryHeader(headerBytes);
        Assert.assertEquals(longFileName.substring(0, TarConstants.NAMELEN), header.getName());
        Assert.assertEquals(longFileName, header.getFileName());
        Assert.assertEquals("", header.getFileComment());
        Assert.assertEquals(0, header.getFileHardLinkNumber());
        Assert.assertEquals(0, header.getFilePermissions());
        Assert.assertEquals(0, header.getFileUID());
        Assert.assertEquals(0, header.getFileGID());
        Assert.assertEquals(0, header.getFileModTime());
        Assert.assertEquals(0, header.getFileModDate());
        Assert.assertEquals(0, header.getFileSize());
        Assert.assertEquals(0, header.getFileMajorVersion());
        Assert.assertEquals(0, header.getFileMinorVersion());
        Assert.assertEquals(0, header.getFileTypeFlag());
        Assert.assertEquals(0, header.getFileLinkNameOffset());
        Assert.assertEquals(0, header.getFileLinkNameLength());

        Assert.assertArrayEquals(expectedLongFileNameBytes, actualBytes, TarConstants.STANDARD_RECORD_LENGTH + TarConstants.STANDARD_RECORD_LENGTH);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        tarOutputStream.putNextEntry(new TarArchiveEntry("test.txt"));
        tarOutputStream.write("Hello World!".getBytes(StandardCharsets.UTF_8));
        tarOutputStream.closeArchiveEntry();

        byte[] expectedEOFRecord = new byte[TarConstants.STANDARD_RECORD_LENGTH];
        Arrays.fill(expectedEOFRecord, (byte) 0);

        byte[] actualBytes = outputStream.toByteArray();
        byte[] headerBytes = new byte[TarConstants.STANDARD_RECORD_LENGTH];
        System.arraycopy(actualBytes, 0, headerBytes, 0, TarConstants.STANDARD_RECORD_LENGTH);

        TarEntryHeader header = new TarEntryHeader(headerBytes);
        Assert.assertEquals("test.txt", header.getName());
        Assert.assertEquals("test.txt\0", header.getFileName());
        Assert.assertEquals("", header.getFileComment());
        Assert.assertEquals(0, header.getFileHardLinkNumber());
        Assert.assertEquals(0, header.getFilePermissions());
        Assert.assertEquals(0, header.getFileUID());
        Assert.assertEquals(0, header.getFileGID());
        Assert.assertEquals(0, header.getFileModTime());
        Assert.assertEquals(0, header.getFileModDate());
        Assert.assertEquals(0, header.getFileSize());
        Assert.assertEquals(0, header.getFileMajorVersion());
        Assert.assertEquals(0, header.getFileMinorVersion());
        Assert.assertEquals(0, header.getFileTypeFlag());
        Assert.assertEquals(0, header.getFileLinkNameOffset());
        Assert.assertEquals(0, header.getFileLinkNameLength());

        Assert.assertArrayEquals(expectedEOFRecord, actualBytes, TarConstants.STANDARD_RECORD_LENGTH + TarConstants.STANDARD_RECORD_LENGTH);
    }

    @Test
    public void testCloseArchiveEntryWithDataToWrite() throws IOException {
        tarOutputStream.putNextEntry(new TarArchiveEntry("test.txt"));
        tarOutputStream.write("Hello World!".getBytes(StandardCharsets.UTF_8));
        tarOutputStream.write("Goodbye World!".getBytes(StandardCharsets.UTF_8));
        tarOutputStream.closeArchiveEntry();

        byte[] expectedEOFRecord = new byte[TarConstants.STANDARD_RECORD_LENGTH];
        Arrays.fill(expectedEOFRecord, (byte) 0);

        byte[] actualBytes = outputStream.toByteArray();
        byte[] headerBytes = new byte[TarConstants.STANDARD_RECORD_LENGTH];
        System.arraycopy(actualBytes, 0, headerBytes, 0, TarConstants.STANDARD_RECORD_LENGTH);

        TarEntryHeader header = new TarEntryHeader(headerBytes);
        Assert.assertEquals("test.txt", header.getName());
        Assert.assertEquals("test.txt\0", header.getFileName());
        Assert.assertEquals("", header.getFileComment());
        Assert.assertEquals(0, header.getFileHardLinkNumber());
        Assert.assertEquals(0, header.getFilePermissions());
        Assert.assertEquals(0, header.getFileUID());
        Assert.assertEquals(0, header.getFileGID());
        Assert.assertEquals(0, header.getFileModTime());
        Assert.assertEquals(0, header.getFileModDate());
        Assert.assertEquals(11, header.getFileSize());
        Assert.assertEquals(0, header.getFileMajorVersion());
        Assert.assertEquals(0, header.getFileMinorVersion());
        Assert.assertEquals(0, header.getFileTypeFlag());
        Assert.assertEquals(0, header.getFileLinkNameOffset());
        Assert.assertEquals(0, header.getFileLinkNameLength());

        Assert.assertArrayEquals(expectedEOFRecord, actualBytes, TarConstants.STANDARD_RECORD_LENGTH + TarConstants.STANDARD_RECORD_LENGTH);

        Assert.assertEquals("Hello World!\nGoodbye World!\n", new String(outputStream.toByteArray(), 0, 11, StandardCharsets.UTF_8));
    }

    @Test(expected = IOException.class)
    public void testWriteWithInvalidOffset() throws IOException {
        tarOutputStream.write(-1, new byte[1]);
    }

    @Test(expected = IOException.class)
    public void testWriteWithInvalidLength() throws IOException {
        tarOutputStream.write(0, new byte[1]);
    }

    @Test(expected = IOException.class)
    public void testWriteWithNullData() throws IOException {
        tarOutputStream.write(0, null);
    }

    @Test
    public void testWrite() throws IOException {
        tarOutputStream.putNextEntry(new TarArchiveEntry("test.txt"));
        tarOutputStream.write("Hello World!".getBytes(StandardCharsets.UTF_8));
        tarOutputStream.closeArchiveEntry();

        byte[] expectedBytes = "Hello World!".getBytes(StandardCharsets.UTF_8);

        byte[] actualBytes = outputStream.toByteArray();

        Assert.assertArrayEquals(expectedBytes, actualBytes, 11);
    }

    @Test
    public void testWriteWithLongData() throws IOException {
        tarOutputStream.putNextEntry(new TarArchiveEntry("test.txt"));
        byte[] longData = new byte[Integer.MAX_VALUE];
        tarOutputStream.write(longData);
        tarOutputStream.closeArchiveEntry();

        byte[] expectedEOFRecord = new byte[TarConstants.STANDARD_RECORD_LENGTH];
        Arrays.fill(expectedEOFRecord, (byte) 0);

        Exception exception = null;
        try {
            byte[] actualBytes = outputStream.toByteArray();
            Assert.fail();
        } catch (OutOfMemoryError e) {
            exception = e;
        }

        Assert.assertNotNull(exception);
        Assert.assertEquals(expectedEOFRecord.length + TarConstants.STANDARD_RECORD_LENGTH, outputStream.size());
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = new File("test.txt");
        inputFile.createNewFile();

        TarArchiveEntry entry = tarOutputStream.createArchiveEntry(inputFile, "test.txt");

        Assert.assertNotNull(entry);
        Assert.assertEquals("test.txt", entry.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateArchiveEntryWithNullInputFile() throws IOException {
        TarArchiveEntry entry = tarOutputStream.createArchiveEntry(null, "test.txt");
    }

    @Test(expected = FileNotFoundException.class)
    public void testCreateArchiveEntryWithNonExistentFile() throws IOException {
        TarArchiveEntry entry = tarOutputStream.createArchiveEntry(new File("non_existent_file.txt"), "test.txt");
    }
}