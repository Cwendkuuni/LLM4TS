import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;

import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {
    private TarArchiveOutputStream tarOutput;

    @Before
    public void setUp() throws IOException {
        tarOutput = new TarArchiveOutputStream(null);
    }

    @After
    public void tearDown() throws IOException {
        tarOutput.close();
    }

    @Test
    public void testFinish() throws IOException {
        tarOutput.finish();
        assertTrue(tarOutput.isFinished());
    }

    @Test(expected = IOException.class)
    public void testFinishTwice() throws IOException {
        tarOutput.finish();
        tarOutput.finish();
    }

    @Test
    public void testClose() throws IOException {
        tarOutput.close();
        assertTrue(tarOutput.isClosed());
    }

    @Test(expected = IOException.class)
    public void testCloseTwice() throws IOException {
        tarOutput.close();
        tarOutput.close();
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutput.putArchiveEntry(entry);
        assertTrue(tarOutput.hasUnclosedEntry());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryAfterFinish() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutput.finish();
        tarOutput.putArchiveEntry(entry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryAfterClose() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutput.close();
        tarOutput.putArchiveEntry(entry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutput.putArchiveEntry(entry);
        tarOutput.closeArchiveEntry();
        assertFalse(tarOutput.hasUnclosedEntry());
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithoutEntry() throws IOException {
        tarOutput.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryAfterClose() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutput.putArchiveEntry(entry);
        tarOutput.close();
        tarOutput.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        byte[] data = { 0x01, 0x02, 0x03 };
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(data.length);
        tarOutput.putArchiveEntry(entry);
        tarOutput.write(data);
        tarOutput.closeArchiveEntry();
        assertEquals(data.length, tarOutput.getBytesWritten());
    }

    @Test(expected = IOException.class)
    public void testWriteExceedsEntrySize() throws IOException {
        byte[] data = { 0x01, 0x02, 0x03 };
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(data.length - 1);
        tarOutput.putArchiveEntry(entry);
        tarOutput.write(data);
        tarOutput.closeArchiveEntry();
    }

    @Test
    public void testIsFinished() throws IOException {
        assertFalse(tarOutput.isFinished());
        tarOutput.finish();
        assertTrue(tarOutput.isFinished());
    }

    @Test
    public void testIsClosed() throws IOException {
        assertFalse(tarOutput.isClosed());
        tarOutput.close();
        assertTrue(tarOutput.isClosed());
    }

    @Test
    public void testHasUnclosedEntry() throws IOException {
        assertFalse(tarOutput.hasUnclosedEntry());
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutput.putArchiveEntry(entry);
        assertTrue(tarOutput.hasUnclosedEntry());
        tarOutput.closeArchiveEntry();
        assertFalse(tarOutput.hasUnclosedEntry());
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = new File("test.txt");
        TarArchiveEntry entry = (TarArchiveEntry) tarOutput.createArchiveEntry(inputFile, "test.txt");
        assertEquals(inputFile, entry.getFile());
        assertEquals("test.txt", entry.getName());
    }
}