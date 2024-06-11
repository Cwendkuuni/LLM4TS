import static org.junit.Assert.*;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructorWithDefaultBlockSize() {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(new OutputStream());
        assertNotNull(tao);
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(new OutputStream());
        tao.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tao.getLongFileMode());
    }

    @Test
    public void testFinish() {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(new OutputStream());
        assertFalse(tao.isFinished());
        tao.finish();
        assertTrue(tao.isFinished());
    }

    @Test
    public void testPutArchiveEntry() {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(new OutputStream());
        TarArchiveEntry entry = new TarArchiveEntry("testEntry");
        tao.putArchiveEntry(entry);
        assertTrue(tao.getHaveUnclosedEntry());
    }

    @Test
    public void testWrite() {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(new OutputStream());
        byte[] bytes = {0, 1, 2, 3, 4};
        tao.write(bytes, 0, bytes.length);
        // Check if bytes were written
    }

    @Test
    public void testCloseArchiveEntry() {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(new OutputStream());
        TarArchiveEntry entry = new TarArchiveEntry("testEntry");
        tao.putArchiveEntry(entry);
        tao.closeArchiveEntry();
        assertFalse(tao.getHaveUnclosedEntry());
    }

    @Test
    public void testCreateArchiveEntry() {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(new OutputStream());
        File file = new File("testFile.txt");
        TarArchiveEntry entry = (TarArchiveEntry) tao.createArchiveEntry(file, "testFile.txt");
        assertNotNull(entry);
        assertEquals("testFile.txt", entry.getName());
    }

    @Test
    public void testClose() {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(new OutputStream());
        tao.close();
        assertTrue(tao.isClosed());
    }

}