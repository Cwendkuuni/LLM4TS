import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveConstants;
import org.apache.commons.compress.archivers.dump.DumpArchiveEntry;
import org.apache.commons.compress.archivers.dump.DumpArchiveInputStream;
import org.apache.commons.compress.archivers.dump.DumpArchiveSummary;
import org.apache.commons.compress.archivers.dump.DumpArchiveUtil;
import org.apache.commons.compress.archivers.zip.ZipEncoding;
import org.apache.commons.compress.archivers.zip.ZipEncodingHelper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Stack;

public class DumpArchiveInputStreamTest {

    private static final String TEST_FILE = "testData.dump";
    private static final String ENCODING = "UTF-8";
    private static final int BUFFER_SIZE = 1024;

    private DumpArchiveInputStream inputStream;
    private DumpArchiveSummary summary;

    @Before
    public void setUp() throws Exception {
        byte[] testData = getTestData();
        InputStream testInputStream = new ByteArrayInputStream(testData);
        inputStream = new DumpArchiveInputStream(testInputStream, ENCODING);
        summary = inputStream.getSummary();
    }

    @Test
    public void testConstructorWithDefaultEncoding() throws IOException {
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(
                new ByteArrayInputStream(getTestData()), null);
        DumpArchiveSummary summary = inputStream.getSummary();
        Assert.assertEquals(summary.getMagicNumber(), DumpArchiveConstants.MAGIC_NUMBER);
    }

    @Test
    public void testConstructorWithSpecifiedEncoding() throws IOException {
        ZipEncoding encoding = ZipEncodingHelper.getZipEncoding(ENCODING);
        DumpArchiveInputStream inputStream = new DumpArchiveInputStream(
                new ByteArrayInputStream(getTestData()), ENCODING);
        DumpArchiveSummary summary = inputStream.getSummary();
        Assert.assertEquals(summary.getMagicNumber(), DumpArchiveConstants.MAGIC_NUMBER);
        Assert.assertEquals(inputStream.getEncoding(), encoding);
    }

    @Test
    public void testGetSummary() throws IOException {
        Assert.assertEquals(summary.getVolumeLabel(), "dumptest");
        Assert.assertEquals(summary.getFileFormatVersion(), 0);
        Assert.assertEquals(summary.getBlockSize(), 512);
        Assert.assertEquals(summary.getRecordSize(), DumpArchiveConstants.TP_SIZE);
        Assert.assertEquals(summary.getFileCount(), 6);
        Assert.assertEquals(summary.getBlockCount(), 20);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        DumpArchiveEntry entry = inputStream.getNextEntry();
        Assert.assertEquals(entry.getHeaderType(), DumpArchiveConstants.SEGMENT_TYPE.ADDR);
        Assert.assertEquals(entry.getHeaderCount(), 1);
        Assert.assertEquals(entry.getName(), "testDir/");
        Assert.assertEquals(entry.isDirectory(), true);

        entry = inputStream.getNextEntry();
        Assert.assertEquals(entry.getHeaderType(), DumpArchiveConstants.SEGMENT_TYPE.INODE);
        Assert.assertEquals(entry.getHeaderCount(), 1);
        Assert.assertEquals(entry.getName(), "testFile1.txt");
        Assert.assertEquals(entry.isDirectory(), false);
        Assert.assertEquals(entry.getIno(), 12345);

        entry = inputStream.getNextEntry();
        Assert.assertEquals(entry.getHeaderType(), DumpArchiveConstants.SEGMENT_TYPE.INODE);
        Assert.assertEquals(entry.getHeaderCount(), 0);
        Assert.assertEquals(entry.getName(), "testFile2.txt");
        Assert.assertEquals(entry.isDirectory(), false);
        Assert.assertEquals(entry.getIno(), 67890);

        entry = inputStream.getNextEntry();
        Assert.assertEquals(entry, null);
    }

    @Test
    public void testRead() throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;

        // Read first file
        bytesRead = inputStream.read(buffer, 0, BUFFER_SIZE);
        Assert.assertGreaterThan(bytesRead, 0);
        Assert.assertEquals(new String(buffer, 0, bytesRead),
                "This is the first file.");

        // Read second file
        bytesRead = inputStream.read(buffer, 0, BUFFER_SIZE);
        Assert.assertGreaterThan(bytesRead, 0);
        Assert.assertEquals(new String(buffer, 0, bytesRead),
                "This is the second file.");

        // End of stream
        bytesRead = inputStream.read(buffer, 0, BUFFER_SIZE);
        Assert.assertEquals(bytesRead, -1);
    }

    @Test
    public void testClose() throws IOException {
        inputStream.close();
        Assert.assertTrue(inputStream.isClosed());
    }

    @Test(expected = IOException.class)
    public void testGetNextEntryAfterClose() throws IOException {
        inputStream.close();
        inputStream.getNextEntry();
    }

    private byte[] getTestData() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DumpArchiveOutputStream archiveOutputStream = new DumpArchiveOutputStream(
                new ByteArrayOutputStream(512 * 21), null);

        archiveOutputStream.putNextEntry(new DumpFileEntry("testDir/", null, null, DumpFileMode.DIR));
        archiveOutputStream.putNextEntry(new DumpFileEntry("testFile1.txt", null, null, DumpFileMode.FILE));
        archiveOutputStream.write("This is the first file.".getBytes());
        archiveOutputStream.putNextEntry(new DumpFileEntry("testFile2.txt", null, null, DumpFileMode.FILE));
        archiveOutputStream.write("This is the second file.".getBytes());
        archiveOutputStream.finish();
        outputStream.writeTo(archiveOutputStream.getOutputStream());
        archiveOutputStream.close();

        return outputStream.toByteArray();
    }

    private static class DumpFileEntry extends DumpArchiveEntry {
        private final String name;

        public DumpFileEntry(String name, byte[] nameInBytes, DumpArchiveEntry parent, DumpFileMode fileMode) {
            super(parent, DumpArchiveConstants.SEGMENT_TYPE.INODE, 0, fileMode, new byte[0]);
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public String getOriginalName() {
            return name;
        }
    }

    private static enum DumpFileMode {
        FILE, DIRECTORY
    }
}