import org.apache.commons.compress.archivers.dump.DumpArchiveConstants;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveSummary;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class DumpArchiveInputStreamTest {

    private static final String TEST_DATA = "Test data.";
    private static final int TEST_DATA_LENGTH = 13;
    private static final int TEST_HEADER_LENGTH = 512;
    private static final int TEST_CHECKSUM = 0x12345678;

    private DumpArchiveInputStream inputStream;
    private DumpArchiveSummary summary;

    @Before
    public void setUp() {
        byte[] header = new byte[TEST_HEADER_LENGTH];
        byte[] testData = TEST_DATA.getBytes();

        System.arraycopy(DumpArchiveUtil.intToBytes(DumpArchiveConstants.NFS_MAGIC), 0, header, 0, 4);
        System.arraycopy(DumpArchiveUtil.intToBytes(TEST_HEADER_LENGTH), 0, header, 24, 4);
        System.arraycopy(testData, 0, header, TEST_HEADER_LENGTH - TEST_DATA_LENGTH, TEST_DATA_LENGTH);
        System.arraycopy(DumpArchiveUtil.intToBytes(TEST_CHECKSUM), 0, header, TEST_HEADER_LENGTH - 4, 4);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(header);
        InputStream testInputStream = new ByteArrayInputStream(outputStream.toByteArray());

        summary = new DumpArchiveSummary(TEST_HEADER_LENGTH, ZipEncodingHelper.getDefaultEncoding());
        inputStream = new DumpArchiveInputStream(testInputStream);
    }

    @Test
    public void testGetNextEntry_ValidEntry() throws IOException {
        DumpArchiveEntry actualEntry = inputStream.getNextEntry();
        DumpArchiveEntry expectedEntry = new DumpArchiveEntry(1, 1, 1, "test_entry.txt", 13, 13, 32, "test_entry.txt", 0, 0, null);

        Assert.assertEquals(expectedEntry.getHeaderType(), actualEntry.getHeaderType());
        Assert.assertEquals(expectedEntry.getIno(), actualEntry.getIno());
        Assert.assertEquals(expectedEntry.getType(), actualEntry.getType());
        Assert.assertEquals(expectedEntry.getName(), actualEntry.getName());
        Assert.assertEquals(expectedEntry.getSize(), actualEntry.getSize());
        Assert.assertEquals(expectedEntry.getLinkName(), actualEntry.getLinkName());
        Assert.assertEquals(expectedEntry.getFileType(), actualEntry.getFileType());
        Assert.assertEquals(expectedEntry.getUid(), actualEntry.getUid());
        Assert.assertEquals(expectedEntry.getGid(), actualEntry.getGid());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetNextEntry_NullInputStream() {
        inputStream = null;
        inputStream.getNextEntry();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetNextEntry_NoEntriesAvailable() throws IOException {
        for (int i = 0; i < TEST_HEADER_LENGTH / DumpArchiveConstants.TP_SIZE; i++) {
            inputStream.getNextEntry();
        }
        inputStream.getNextEntry();
    }

    @Test(expected = EOFException.class)
    public void testGetNextEntry_EndOfArchive() throws IOException {
        for (int i = 0; i < TEST_HEADER_LENGTH / DumpArchiveConstants.TP_SIZE; i++) {
            inputStream.getNextEntry();
        }
        inputStream.getNextEntry();
        inputStream.getNextEntry();
    }

    @Test(expected = IOException.class)
    public void testGetNextEntry_InvalidEntry() throws IOException {
        byte[] invalidHeader = Arrays.copyOfRange(header, 0, TEST_HEADER_LENGTH - 4);
        ByteArrayInputStream invalidInputStream = new ByteArrayInputStream(invalidHeader);
        DumpArchiveInputStream invalidInput = new DumpArchiveInputStream(invalidInputStream);

        invalidInput.getNextEntry();
    }

    @Test
    public void testRead_ValidData() throws IOException {
        byte[] buffer = new byte[14];
        int read = inputStream.read(buffer, 0, buffer.length);

        Assert.assertEquals(TEST_DATA_LENGTH, read);
        Assert.assertArrayEquals(TEST_DATA.getBytes(), Arrays.copyOfRange(buffer, 0, TEST_DATA_LENGTH));
    }

    @Test(expected = IOException.class)
    public void testRead_EndOfArchive() throws IOException {
        byte[] buffer = new byte[1];
        inputStream.read(buffer, 0, buffer.length);
        inputStream.getNextEntry();
        inputStream.read(buffer, 0, buffer.length);

        Assert.assertEquals(-1, inputStream.read(buffer, 0, buffer.length));
    }

    @Test
    public void testGetSummary_ValidData() {
        DumpArchiveSummary actualSummary = inputStream.getSummary();

        Assert.assertEquals(summary.getMagic(), actualSummary.getMagic());
        Assert.assertEquals(summary.getBlockSize(), actualSummary.getBlockSize());
        Assert.assertEquals(summary.isCompressed(), actualSummary.isCompressed());
        Assert.assertEquals(summary.getNumFiles(), actualSummary.getNumFiles());
        Assert.assertEquals(summary.getNumDirectories(), actualSummary.getNumDirectories());
    }
}