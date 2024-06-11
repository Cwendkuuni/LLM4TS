import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {

    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws Exception {
        tarArchiveOutputStream = new TarArchiveOutputStream(null); // Set output stream to null for testing purpose
    }

    @After
    public void tearDown() throws Exception {
        tarArchiveOutputStream.close();
    }

    @Test
    public void testPutArchiveEntry() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        assertEquals(0, tarArchiveOutputStream.getRecordSize());
        assertTrue(tarArchiveOutputStream.getOut() instanceof CountingOutputStream);
        assertTrue(tarArchiveOutputStream.isHaveUnclosedEntry());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithFinishedArchive() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.putArchiveEntry(entry); // should throw IOException
    }

    @Test
    public void testCloseArchiveEntry() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);

        tarArchiveOutputStream.closeArchiveEntry();

        assertFalse(tarArchiveOutputStream.isHaveUnclosedEntry());
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithoutEntry() throws Exception {
        tarArchiveOutputStream.closeArchiveEntry(); // should throw IOException
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithFinishedArchive() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.closeArchiveEntry(); // should throw IOException
    }

    @Test
    public void testWrite() throws Exception {
        byte[] wBuf = "test".getBytes();
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        entry.setSize(wBuf.length);

        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(wBuf, 0, wBuf.length);

        assertTrue(tarArchiveOutputStream.getOut() instanceof CountingOutputStream);
        assertEquals(tarArchiveOutputStream.getCurrBytes(), wBuf.length);
    }

    @Test(expected = IOException.class)
    public void testWriteExceedEntrySize() throws Exception {
        byte[] wBuf = "test".getBytes();
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        entry.setSize(wBuf.length - 1);

        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write(wBuf, 0, wBuf.length); // should throw IOException
    }

    @Test
    public void testFinish() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.finish();

        assertTrue(tarArchiveOutputStream.isFinished());
    }

    @Test(expected = IOException.class)
    public void testFinishTwice() throws Exception {
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.finish(); // should throw IOException
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws Exception {
        TarArchiveEntry entry = new TarArchiveEntry("testFile.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.finish(); // should throw IOException
    }

    @Test
    public void testClose() throws Exception {
        tarArchiveOutputStream.close();

        assertTrue(tarArchiveOutputStream.isClosed());
    }

    @Test
    public void testGetRecordSize() throws Exception {
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarArchiveOutputStream.getRecordSize());
    }

    @Test
    public void testSetLongFileMode() throws Exception {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
    }
}