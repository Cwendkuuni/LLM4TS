import static org.junit.Assert.*;
import org.junit.Test;

public class TarArchiveOutputStreamTest {

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tar = new TarArchiveOutputStream(null);
        tar.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tar.longFileMode);
    }

    @Test
    public void testFinish() {
        TarArchiveOutputStream tar = new TarArchiveOutputStream(null);
        try {
            tar.finish();
            // TODO: assert something
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testClose() {
        TarArchiveOutputStream tar = new TarArchiveOutputStream(null);
        try {
            tar.close();
            // TODO: assert something
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testGetRecordSize() {
        TarArchiveOutputStream tar = new TarArchiveOutputStream(null);
        int recordSize = tar.getRecordSize();
        assertEquals(512, recordSize);
    }

    @Test
    public void testPutArchiveEntry() {
        TarArchiveOutputStream tar = new TarArchiveOutputStream(null);
        try {
            tar.putArchiveEntry(new TarArchiveEntry("test.txt"));
            // TODO: assert something
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testCloseArchiveEntry() {
        TarArchiveOutputStream tar = new TarArchiveOutputStream(null);
        try {
            tar.closeArchiveEntry();
            // TODO: assert something
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testWrite() {
        TarArchiveOutputStream tar = new TarArchiveOutputStream(null);
        byte[] data = new byte[1024];
        try {
            tar.write(data, 0, data.length);
            // TODO: assert something
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }

    @Test
    public void testCreateArchiveEntry() {
        TarArchiveOutputStream tar = new TarArchiveOutputStream(null);
        File file = new File("test.txt");
        try {
            assertEquals(TarArchiveEntry.class, tar.createArchiveEntry(file, "test.txt").getClass());
        } catch (Exception e) {
            fail("Should not throw exception");
        }
    }
}