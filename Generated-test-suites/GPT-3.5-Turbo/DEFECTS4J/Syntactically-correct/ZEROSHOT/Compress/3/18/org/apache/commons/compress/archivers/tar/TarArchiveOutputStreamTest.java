import org.junit.Test;
import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(outputStream);
        assertNotNull(tao);
    }

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(outputStream);
        int longFileMode = TarArchiveOutputStream.LONGFILE_TRUNCATE;
        tao.setLongFileMode(longFileMode);
        assertEquals(longFileMode, tao.getLongFileMode());
    }

    @Test
    public void testGetRecordSize() {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(outputStream);
        int recordSize = tao.getRecordSize();
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, recordSize);
    }

    @Test
    public void testPutArchiveEntry() {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(outputStream);
        ArchiveEntry entry = new TarArchiveEntry("testfile");
        try {
            tao.putArchiveEntry(entry);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testCloseArchiveEntry() {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(outputStream);
        ArchiveEntry entry = new TarArchiveEntry("testfile");
        try {
            tao.putArchiveEntry(entry);
            tao.closeArchiveEntry();
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testWrite() {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(outputStream);
        ArchiveEntry entry = new TarArchiveEntry("testfile");
        byte[] buf = new byte[1024];
        try {
            tao.putArchiveEntry(entry);
            tao.write(buf);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testFinish() {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(outputStream);
        try {
            tao.finish();
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testClose() {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(outputStream);
        try {
            tao.close();
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testCreateArchiveEntry() {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(outputStream);
        File inputFile = new File("testfile");
        String entryName = "testentry";
        ArchiveEntry entry = null;
        try {
            entry = tao.createArchiveEntry(inputFile, entryName);
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
        assertNotNull(entry);
    }

}