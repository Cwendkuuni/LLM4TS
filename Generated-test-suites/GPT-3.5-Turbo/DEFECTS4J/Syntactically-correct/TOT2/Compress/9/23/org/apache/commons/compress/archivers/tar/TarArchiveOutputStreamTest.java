import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class TarArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntry() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream tao = new TarArchiveOutputStream(baos);

        try {
            TarArchiveEntry entry = new TarArchiveEntry("test.txt");
            tao.putArchiveEntry(entry);
            tao.closeArchiveEntry();

            assertEquals("test.txt", entry.getName());
            assertFalse(tao.haveUnclosedEntry);
        } catch (IOException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testWrite() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream tao = new TarArchiveOutputStream(baos);

        try {
            byte[] bytes = "Hello, World!".getBytes();
            tao.write(bytes, 0, bytes.length);
            tao.closeArchiveEntry();

            assertEquals(13, tao.currBytes);
        } catch (IOException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testCloseArchiveEntry() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream tao = new TarArchiveOutputStream(baos);

        try {
            TarArchiveEntry entry = new TarArchiveEntry("test.txt");
            tao.putArchiveEntry(entry);
            tao.closeArchiveEntry();

            assertFalse(tao.haveUnclosedEntry);
        } catch (IOException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testFinish() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream tao = new TarArchiveOutputStream(baos);

        try {
            tao.finish();

            assertTrue(tao.finished);
        } catch (IOException e) {
            fail("Exception not expected");
        }
    }

    @Test
    public void testSetLongFileMode() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream tao = new TarArchiveOutputStream(baos);

        tao.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);

        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tao.longFileMode);
    }

    @Test
    public void testGetRecordSize() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream tao = new TarArchiveOutputStream(baos);

        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tao.getRecordSize());
    }

    @Test
    public void testCreateArchiveEntry() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        TarArchiveOutputStream tao = new TarArchiveOutputStream(baos);

        try {
            TarArchiveEntry entry = (TarArchiveEntry) tao.createArchiveEntry(new File("testFile.txt"), "testFile.txt");

            assertEquals("testFile.txt", entry.getName());
        } catch (IOException e) {
            fail("Exception not expected");
        }
    }

}