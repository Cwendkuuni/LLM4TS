import static org.junit.Assert.*;
import org.junit.Test;

public class TarArchiveOutputStreamTest {
    
    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream t = new TarArchiveOutputStream(null);
        t.setLongFileMode(0);
        assertEquals(0, t.getLongFileMode());
    }
    
    @Test
    public void testFinishWithUnclosedEntry() {
        try {
            TarArchiveOutputStream t = new TarArchiveOutputStream(null);
            t.putArchiveEntry(new TarArchiveEntry(null, null));
            t.finish();
            fail("Expected IOException");
        } catch(IOException e) {
            assertEquals("This archives contains unclosed entries.", e.getMessage());
        }
    }
    
    @Test
    public void testFinish() throws IOException {
        TarArchiveOutputStream t = new TarArchiveOutputStream(null);
        t.finish();
        // No assertion to check if finish() successfully executed without exceptions
    }
    
    @Test
    public void testClose() throws IOException {
        TarArchiveOutputStream t = new TarArchiveOutputStream(null);
        t.close();
        // No assertion to check if close() successfully executed without exceptions
    }
    
    @Test
    public void testGetRecordSize() {
        TarArchiveOutputStream t = new TarArchiveOutputStream(null);
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, t.getRecordSize());
    }
    
    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveOutputStream t = new TarArchiveOutputStream(null);
        t.putArchiveEntry(new TarArchiveEntry(null, null));
        // No assertion to check if putArchiveEntry() successfully executed without exceptions
    }
    
    @Test
    public void testCloseArchiveEntryWithIncompleteData() {
        try {
            TarArchiveOutputStream t = new TarArchiveOutputStream(null);
            t.putArchiveEntry(new TarArchiveEntry(null, null));
            t.closeArchiveEntry();
            fail("Expected IOException");
        } catch(IOException e) {
            assertEquals("entry 'null' closed at '0' before the '0' bytes specified in the header were written", e.getMessage());
        }
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveOutputStream t = new TarArchiveOutputStream(null);
        t.putArchiveEntry(new TarArchiveEntry(null, null));
        t.closeArchiveEntry();
        // No assertion to check if closeArchiveEntry() successfully executed without exceptions
    }
    
    @Test
    public void testWriteWithExceededSize() {
        try {
            TarArchiveOutputStream t = new TarArchiveOutputStream(null);
            TarArchiveEntry entry = new TarArchiveEntry(null, null);
            entry.setSize(10);
            t.putArchiveEntry(entry);
            byte[] bytes = new byte[15];
            t.write(bytes, 0, bytes.length);
            fail("Expected IOException");
        } catch(IOException e) {
            assertEquals("request to write '15' bytes exceeds size in header of '10' bytes for entry 'null'", e.getMessage());
        }
    }
    
    @Test
    public void testWriteWithPartialBuffer() throws IOException {
        TarArchiveOutputStream t = new TarArchiveOutputStream(null);
        TarArchiveEntry entry = new TarArchiveEntry(null, null);
        entry.setSize(10);
        t.putArchiveEntry(entry);
        byte[] bytes = new byte[5];
        t.write(bytes, 0, bytes.length);
        // No assertion to check if write() successfully executed without exceptions
    }
    
    @Test
    public void testWriteWithCompleteBuffer() throws IOException {
        TarArchiveOutputStream t = new TarArchiveOutputStream(null);
        TarArchiveEntry entry = new TarArchiveEntry(null, null);
        entry.setSize(10);
        t.putArchiveEntry(entry);
        byte[] bytes = new byte[10];
        t.write(bytes, 0, bytes.length);
        // No assertion to check if write() successfully executed without exceptions
    }
    
    @Test
    public void testWriteEOFRecord() throws IOException {
        TarArchiveOutputStream t = new TarArchiveOutputStream(null);
        t.writeEOFRecord();
        // No assertion to check if writeEOFRecord() successfully executed without exceptions
    }
    
    @Test
    public void testFlush() throws IOException {
        TarArchiveOutputStream t = new TarArchiveOutputStream(null);
        t.flush();
        // No assertion to check if flush() successfully executed without exceptions
    }
    
    @Test
    public void testCreateArchiveEntry() throws IOException {
        TarArchiveOutputStream t = new TarArchiveOutputStream(null);
        ArchiveEntry entry = t.createArchiveEntry(null, null);
        assertTrue(entry instanceof TarArchiveEntry);
    }
}