import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.archivers.tar.TarConstants;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

    private static final String TEST_FILE = "testFile.txt";
    private static final String TEST_LONG_FILE = "testLongFile.txt";
    private static final String TEST_DIR = "testDir";

    private TarArchiveOutputStream tarOutputStream;
    private ByteArrayOutputStream outputStream;
    private File testFile;
    private File testLongFile;
    private File testDir;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
        testFile = new File("testFile.txt");
        testLongFile = new File("testLongFile.txt");
        testDir = new File("testDir");
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
        IOUtils.closeQuietly(outputStream);
        if (testFile.exists()) {
            testFile.delete();
        }
        if (testLongFile.exists()) {
            testLongFile.delete();
        }
        if (testDir.exists()) {
            testDir.delete();
        }
    }

    @Test
    public void testConstructor() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertNotNull(tarArchiveOutputStream);
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
    }

    @Test
    public void testPutArchiveEntrySuccess() throws IOException {
        tarOutputStream.putNextEntry(new TarArchiveEntry(testFile, TEST_FILE));
        assertNotNull(tarOutputStream.getNextEntry());
        tarOutputStream.write(TEST_FILE.getBytes(StandardCharsets.UTF_8));
        tarOutputStream.closeArchiveEntry();

        byte[] bytes = outputStream.toByteArray();
        assertFalse(bytes[bytes.length - 1] == 0);
        byte[] header = new byte[512];
        System.arraycopy(bytes, 0, header, 0, 512);
        TarEntryHeader headerInfo = new TarEntryHeader(header);
        assertEquals(TEST_FILE, headerInfo.getName());
        assertEquals(TEST_FILE.length(), headerInfo.getSize());

        byte[] record = new byte[512];
        System.arraycopy(bytes, 512, record, 0, 512);
        assertEquals(TEST_FILE.length(), new TarRecord(record).getSize());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryLongFileNameFailure() throws IOException {
        String longFileName = "a" + new String(new char[TarConstants.NAMELEN + 1]) + "b";
        tarOutputStream.putNextEntry(new TarArchiveEntry(testLongFile, longFileName));
    }

    @Test
    public void testPutArchiveEntryDirectory() throws IOException {
        tarOutputStream.putNextEntry(new TarArchiveEntry(testDir, TEST_DIR));
        assertNull(tarOutputStream.getNextEntry());

        byte[] bytes = outputStream.toByteArray();
        TarDirectoryEntry directoryEntry = new TarDirectoryEntry(bytes, 0);
        assertEquals(TEST_DIR, directoryEntry.getName());

        byte[] record = new byte[512];
        System.arraycopy(bytes, 512, record, 0, 512);
        assertEquals(0, new TarRecord(record).getSize());
    }

    @Test
    public void testWrite() throws IOException {
        tarOutputStream.putNextEntry(new TarArchiveEntry(testFile, TEST_FILE));
        tarOutputStream.write(TEST_FILE.getBytes(StandardCharsets.UTF_8));
        tarOutputStream.closeArchiveEntry();

        byte[] bytes = outputStream.toByteArray();
        assertEquals(TEST_FILE.length(), bytes.length - 512);
    }

    @Test
    public void testClose() throws IOException {
        tarOutputStream.putNextEntry(new TarArchiveEntry(testFile, TEST_FILE));
        tarOutputStream.write(TEST_FILE.getBytes(StandardCharsets.UTF_8));
        tarOutputStream.close();

        byte[] bytes = outputStream.toByteArray();
        assertEquals(TEST_FILE.length(), bytes.length);
        assertEquals(bytes.length, outputStream.size());
    }

    @Test
    public void testFinish() throws IOException {
        tarOutputStream.finish();

        byte[] bytes = outputStream.toByteArray();
        assertEquals(bytes.length, outputStream.size());
        assertEquals(bytes[bytes.length - 1], (byte) 0);
        assertEquals(bytes[bytes.length - 2], (byte) 0);
    }

    @Test
    public void testCreateArchiveEntry() {
        ArchiveEntry archiveEntry = tarOutputStream.createArchiveEntry(testFile, TEST_FILE);
        assertSame(new TarArchiveEntry(testFile, TEST_FILE), archiveEntry);
    }

    @Test(expected = IOException.class)
    public void testCreateArchiveEntryNullFile() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.createArchiveEntry(null, TEST_FILE);
    }

    @Test(expected = IOException.class)
    public void testCreateArchiveEntryNullFileName() throws IOException {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        tarArchiveOutputStream.createArchiveEntry(testFile, null);
    }
}