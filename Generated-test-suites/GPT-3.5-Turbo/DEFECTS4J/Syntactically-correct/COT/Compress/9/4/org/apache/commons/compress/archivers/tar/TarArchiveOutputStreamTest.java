
import org.junit.Test;
import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor1() {
        TarArchiveOutputStream t = new TarArchiveOutputStream(new OutputStream());
        assertNotNull(t);
    }

    @Test
    public void testConstructor2() {
        TarArchiveOutputStream t = new TarArchiveOutputStream(new OutputStream(), 512);
        assertNotNull(t);
    }

    @Test
    public void testConstructor3() {
        TarArchiveOutputStream t = new TarArchiveOutputStream(new OutputStream(), 512, 1024);
        assertNotNull(t);
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream t = new TarArchiveOutputStream(new OutputStream());
        t.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, t.getLongFileMode());
    }

    @Test
    public void testFinish() {
        TarArchiveOutputStream t = new TarArchiveOutputStream(new OutputStream());
        t.finish();
        assertTrue(t.isFinished());
    }

    @Test(expected = IOException.class)
    public void testFinishAlreadyFinished() throws IOException {
        TarArchiveOutputStream t = new TarArchiveOutputStream(new OutputStream());
        t.finish();
        t.finish();
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        TarArchiveOutputStream t = new TarArchiveOutputStream(new OutputStream());
        t.putArchiveEntry(new TarArchiveEntry("test.txt"));
        t.finish();
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveOutputStream t = new TarArchiveOutputStream(new OutputStream());
        t.close();
        assertTrue(t.isClosed());
    }

    @Test
    public void testCloseAlreadyClosed() throws IOException {
        TarArchiveOutputStream t = new TarArchiveOutputStream(new OutputStream());
        t.close();
        t.close();
        assertTrue(t.isClosed());
    }

    @Test
    public void testGetRecordSize() {
        TarArchiveOutputStream t = new TarArchiveOutputStream(new OutputStream(), 512);
        assertEquals(512, t.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveOutputStream t = new TarArchiveOutputStream(new OutputStream());
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        t.putArchiveEntry(entry);
        assertTrue(t.hasUnclosedEntry());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryFinished() throws IOException {
        TarArchiveOutputStream t = new TarArchiveOutputStream(new OutputStream());
        t.finish();
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        t.putArchiveEntry(entry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveOutputStream t = new TarArchiveOutputStream(new OutputStream());
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        t.putArchiveEntry(entry);
        t.closeArchiveEntry();
        assertFalse(t.hasUnclosedEntry());
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryNoCurrentEntry() throws IOException {
        TarArchiveOutputStream t = new TarArchiveOutputStream(new OutputStream());
        t.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryFinished() throws IOException {
        TarArchiveOutputStream t = new TarArchiveOutputStream(new OutputStream());
        t.finish();
        t.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryIncompleteWrite() throws IOException {
        TarArchiveOutputStream t = new TarArchiveOutputStream(new OutputStream());
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(5);
        t.putArchiveEntry(entry);
        t.write(new byte[]{1, 2, 3}, 0, 3);
        t.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveOutputStream t = new TarArchiveOutputStream(new OutputStream());
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(3);
        t.putArchiveEntry(entry);
        t.write(new byte[]{1, 2, 3}, 0, 3);
        t.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testWriteExceedsSize() throws IOException {
        TarArchiveOutputStream t = new TarArchiveOutputStream(new OutputStream());
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(3);
        t.putArchiveEntry(entry);
        t.write(new byte[]{1, 2, 3, 4}, 0, 4);
        t.closeArchiveEntry();
    }

    @Test
    public void testFlush() throws IOException {
        TarArchiveOutputStream t = new TarArchiveOutputStream(new OutputStream());
        t.flush();
        // No way to verify if flush is successful without mocking OutputStream
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        TarArchiveOutputStream t = new TarArchiveOutputStream(new OutputStream());
        ArchiveEntry entry = t.createArchiveEntry(new File("test.txt"), "test.txt");
        assertNotNull(entry);
        assertTrue(entry instanceof TarArchiveEntry);
    }

}
