import org.junit.Test;
import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(null);
        tao.setLongFileMode(0);
        assertEquals(0, tao.longFileMode);
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(null);
        tao.finish();
    }

    @Test(expected=IOException.class)
    public void testFinishWithUnclosedEntries() throws IOException {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(null);
        tao.haveUnclosedEntry = true;
        tao.finish();
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(null);
        tao.close();
        assertTrue(tao.closed);
    }

    @Test
    public void testGetRecordSize() {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(null);
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tao.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(null);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tao.putArchiveEntry(entry);
        assertTrue(tao.haveUnclosedEntry);
    }

    @Test(expected=IOException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(null);
        TarArchiveEntry entry = new TarArchiveEntry("verylongfilenameverylongfilenameverylongfilenameverylongfilenameverylongfilenameverylongfilename");
        tao.putArchiveEntry(entry);
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(null);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tao.putArchiveEntry(entry);
        tao.closeArchiveEntry();
        assertFalse(tao.haveUnclosedEntry);
    }

    @Test(expected=IOException.class)
    public void testCloseArchiveEntryWithIncompleteData() throws IOException {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(null);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(10);
        tao.putArchiveEntry(entry);
        tao.write(new byte[5]);
        tao.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(null);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        entry.setSize(10);
        tao.putArchiveEntry(entry);
        tao.write("Hello World".getBytes(), 0, 11);
        tao.closeArchiveEntry();
    }

    @Test
    public void testWriteEOFRecord() throws IOException {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(null);
        tao.writeEOFRecord();
    }

    @Test
    public void testFlush() throws IOException {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(null);
        tao.flush();
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        TarArchiveOutputStream tao = new TarArchiveOutputStream(null);
        assertNotNull(tao.createArchiveEntry(null, ""));
    }
}