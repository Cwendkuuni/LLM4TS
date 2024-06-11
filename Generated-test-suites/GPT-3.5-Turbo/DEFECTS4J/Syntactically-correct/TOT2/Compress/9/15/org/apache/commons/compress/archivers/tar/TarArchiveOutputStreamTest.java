import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TarArchiveOutputStreamTest {
    private static final int BLOCK_SIZE = 512;
    private static final int RECORD_SIZE = 1024;

    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() {
        tarArchiveOutputStream = new TarArchiveOutputStream(new ByteArrayOutputStream(), BLOCK_SIZE, RECORD_SIZE);
    }

    @After
    public void tearDown() {
        try {
            tarArchiveOutputStream.close();
        } catch (IOException e) {
            fail("Error closing TarArchiveOutputStream");
        }
    }

    @Test
    public void testSetLongFileMode() {
        // Test setting longFileMode to LONGFILE_ERROR
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_ERROR);
        assertEquals(tarArchiveOutputStream.getRecordSize(), TarBuffer.DEFAULT_BLKSIZE);

        // Test setting longFileMode to LONGFILE_TRUNCATE
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(tarArchiveOutputStream.getRecordSize(), TarBuffer.DEFAULT_BLKSIZE);

        // Test setting longFileMode to LONGFILE_GNU
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(tarArchiveOutputStream.getRecordSize(), TarBuffer.DEFAULT_BLKSIZE);
    }

    @Test
    public void testPutArchiveEntryAndCloseArchiveEntry() {
        // Test putArchiveEntry with a new TarArchiveEntry
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        try {
            tarArchiveOutputStream.putArchiveEntry(entry);
            tarArchiveOutputStream.write("Test content".getBytes());
            tarArchiveOutputStream.closeArchiveEntry();
        } catch (IOException e) {
            fail("Error putting or closing archive entry");
        }

        // Test closeArchiveEntry without putArchiveEntry
        try {
            tarArchiveOutputStream.closeArchiveEntry();
            fail("Expected an IOException as no entry was added");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testWrite() {
        // Test writing bytes to current entry
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        byte[] content = "Test content".getBytes();
        try {
            tarArchiveOutputStream.putArchiveEntry(entry);
            tarArchiveOutputStream.write(content, 0, content.length);
            tarArchiveOutputStream.closeArchiveEntry();
        } catch (IOException e) {
            fail("Error writing to current entry");
        }
    }
}