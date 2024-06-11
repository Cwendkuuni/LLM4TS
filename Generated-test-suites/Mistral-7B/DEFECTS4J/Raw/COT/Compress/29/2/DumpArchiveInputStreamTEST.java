import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveConstants;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveSummary;
import org.apache.commons.compress.archivers.dump.DumpArchiveUtil;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.apache.commons.compress.utils.TapeInputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Stack;

public class DumpArchiveInputStreamTest {

    private static final String TEST_FILE = "testData.dump";
    private static final int TEST_FILE_SIZE = 1024;
    private static final int TEST_RECORD_SIZE = 16;
    private static final int TEST_HEADER_SIZE = 12;
    private static final int TEST_ENTRY_COUNT = 5;

    private DumpArchiveInputStream testee;
    private byte[] testData;
    private InputStream testInputStream;

    @Before
    public void setUp() throws IOException {
        // Create test data
        testData = new byte[TEST_FILE_SIZE];
        for (int i = 0; i < TEST_FILE_SIZE; i++) {
            testData[i] = (byte) i;
        }

        // Create test input stream
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        for (int i = 0; i < TEST_HEADER_SIZE; i++) {
            outputStream.write(DumpArchiveConstants.NFS_MAGIC);
            outputStream.write(i);
        }
        byte[] header = outputStream.toByteArray();
        outputStream.reset();

        for (int i = 0; i < TEST_ENTRY_COUNT; i++) {
            outputStream.write(DumpArchiveUtil.convert32(header, 24));
            outputStream.write(TEST_RECORD_SIZE);
            outputStream.write(DumpArchiveConstants.SEGMENT_TYPE.HEADER);
            outputStream.write(DumpArchiveConstants.TP_SIZE);
            outputStream.write((byte) i);
            outputStream.write(DumpArchiveConstants.SEGMENT_TYPE.INODE);
            outputStream.write(1);
            outputStream.write(4);
            outputStream.write(DumpArchiveUtil.encode(ZipEncodingHelper.getZipEncoding(null), "test" + i));
            outputStream.write(0);
            outputStream.write(1);
        }

        for (int i = 0; i < TEST_ENTRY_COUNT; i++) {
            int inode = i + 1;
            byte[] dirEntryData = new byte[TEST_RECORD_SIZE];
            DumpArchiveUtil.convert32(header, 24 + (i * DumpArchiveConstants.TP_SIZE))
                    .buildByteArray(dirEntryData, 0);
            dirEntryData[12] = (byte) inode;
            dirEntryData[13] = (byte) inode;
            dirEntryData[14] = (byte) 4;
            System.arraycopy(
                    DumpArchiveUtil.encode(ZipEncodingHelper.getZipEncoding(null), "test" + i).getBytes(),
                    0,
                    dirEntryData,
                    16,
                    DumpArchiveUtil.encode(ZipEncodingHelper.getZipEncoding(null), "test" + i).length);
            for (int j = 17; j < TEST_RECORD_SIZE; j++) {
                dirEntryData[j] = 0;
            }

            byte[] data = new byte[TEST_RECORD_SIZE];
            System.arraycopy(testData, TEST_FILE_SIZE - TEST_RECORD_SIZE, data, 0, TEST_RECORD_SIZE);

            outputStream.write(DumpArchiveConstants.SEGMENT_TYPE.DATA);
            outputStream.write(TEST_RECORD_SIZE);
            outputStream.write(dirEntryData);
            outputStream.write(data);
        }

        byte[] testFileBytes = outputStream.toByteArray();
        testInputStream = new ByteArrayInputStream(testFileBytes);

        // Initialize testee
        testee = new DumpArchiveInputStream(testInputStream, null);
    }

    @Test
    public void testMatches() {
        Assert.assertTrue(DumpArchiveInputStream.matches(testData, TEST_FILE_SIZE));
        Assert.assertTrue(DumpArchiveInputStream.matches(testData, TEST_FILE_SIZE - DumpArchiveConstants.TP_SIZE));
        Assert.assertFalse(DumpArchiveInputStream.matches(testData, TEST_FILE_SIZE - DumpArchiveConstants.TP_SIZE + 1));
    }

    @Test
    public void testGetSummary() {
        DumpArchiveSummary summary = testee.getSummary();
        Assert.assertEquals(TEST_ENTRY_COUNT, summary.getEntryCount());
    }

    @Test(expected = ArchiveException.class)
    public void testConstructorWithNullInputStream() {
        new DumpArchiveInputStream(null, null);
    }

    @Test(expected = ArchiveException.class)
    public void testReadCLRIWithoutHeader() throws IOException {
        testee.readCLRI();
    }

    @Test(expected = ArchiveException.class)
    public void testReadBITSWithoutHeader() throws IOException {
        testee.readBITS();
    }

    @Test
    public void testGetNextEntry() throws IOException {
        DumpArchiveEntry entry = testee.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals(1, entry.getIno());
        Assert.assertEquals(4, entry.getType());
        Assert.assertEquals("test0", entry.getName());

        entry = testee.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals(2, entry.getIno());
        Assert.assertEquals(4, entry.getType());
        Assert.assertEquals("test1", entry.getName());

        entry = testee.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals(3, entry.getIno());
        Assert.assertEquals(4, entry.getType());
        Assert.assertEquals("test2", entry.getName());

        entry = testee.getNextEntry();
        Assert.assertNotNull(entry);
        Assert.assertEquals(4, entry.getIno());
        Assert.assertEquals(4, entry.getType());
        Assert.assertEquals("test3", entry.getName());

        entry = testee.getNextEntry();
        Assert.assertNull(entry);
    }

    @Test(expected = IOException.class)
    public void testGetNextEntryWithEOF() throws IOException {
        testee.getNextEntry();
        testee.getNextEntry();
        testee.getNextEntry();
        testee.getNextEntry();
        testee.getNextEntry();

        testee.getNextEntry();
    }

    @Test(expected = IOException.class)
    public void testGetNextEntryWithCorruptedData() throws IOException {
        testee = new DumpArchiveInputStream(new ByteArrayInputStream(Arrays.copyOfRange(testData, 0, TEST_FILE_SIZE - 1)), null);
        testee.getNextEntry();
        testee.getNextEntry();
        testee.getNextEntry();
        testee.getNextEntry();
        testee.getNextEntry();

        testee.getNextEntry();
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[TEST_RECORD_SIZE];

        int totalRead = testee.read(buffer, 0, TEST_RECORD_SIZE);
        Assert.assertEquals(TEST_RECORD_SIZE, totalRead);

        totalRead = testee.read(buffer, 0, TEST_RECORD_SIZE);
        Assert.assertEquals(TEST_RECORD_SIZE, totalRead);

        totalRead = testee.read(buffer, 0, TEST_RECORD_SIZE * 2);
        Assert.assertEquals(TEST_RECORD_SIZE * 2, totalRead);

        totalRead = testee.read(buffer, 0, TEST_RECORD_SIZE * 3);
        Assert.assertEquals(-1, totalRead);
    }

    @Test(expected = IOException.class)
    public void testReadWithEOF() throws IOException {
        testee.read(new byte[TEST_RECORD_SIZE], 0, TEST_RECORD_SIZE);
        testee.read(new byte[TEST_RECORD_SIZE], 0, TEST_RECORD_SIZE);
        testee.read(new byte[TEST_RECORD_SIZE], 0, TEST_RECORD_SIZE);
        testee.read(new byte[TEST_RECORD_SIZE], 0, TEST_RECORD_SIZE);
        testee.read(new byte[TEST_RECORD_SIZE], 0, TEST_RECORD_SIZE);

        testee.read(new byte[TEST_RECORD_SIZE], 0, TEST_RECORD_SIZE);
    }

    @Test(expected = IOException.class)
    public void testReadWithCorruptedData() throws IOException {
        testee = new DumpArchiveInputStream(new ByteArrayInputStream(Arrays.copyOfRange(testData, 0, TEST_FILE_SIZE - 1)), null);
        testee.read(new byte[TEST_RECORD_SIZE], 0, TEST_RECORD_SIZE);
        testee.read(new byte[TEST_RECORD_SIZE], 0, TEST_RECORD_SIZE);
        testee.read(new byte[TEST_RECORD_SIZE], 0, TEST_RECORD_SIZE);
        testee.read(new byte[TEST_RECORD_SIZE], 0, TEST_RECORD_SIZE);

        testee.read(new byte[TEST_RECORD_SIZE], 0, TEST_RECORD_SIZE);
    }

    @Test
    public void testClose() throws IOException {
        testee.close();
    }
}