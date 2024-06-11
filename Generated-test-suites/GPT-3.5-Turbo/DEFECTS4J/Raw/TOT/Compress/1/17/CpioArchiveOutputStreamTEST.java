import org.junit.Test;
import static org.junit.Assert.*;

public class CpioArchiveOutputStreamTest {
    
    @Test
    public void testPutNextEntry() {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(new OutputStream());
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        try {
            out.putNextEntry(entry);
            out.closeArchiveEntry();
        } catch (IOException e) {
            fail("Failed to put next entry");
        }
        assertEquals("testfile", out.getNextEntry().getName());
    }
    
    @Test
    public void testCloseArchiveEntry() {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(new OutputStream());
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        try {
            out.putNextEntry(entry);
            out.closeArchiveEntry();
        } catch (IOException e) {
            fail("Failed to put next entry");
        }
        assertNull(out.getNextEntry());
    }
    
    @Test
    public void testWrite() {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(new OutputStream());
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        try {
            out.putNextEntry(entry);
            out.write("12345".getBytes());
            out.closeArchiveEntry();
        } catch (IOException e) {
            fail("Failed to put next entry");
        }
        assertEquals("12345", out.getNextEntry().getContent());
    }
    
    @Test
    public void testFinish() {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(new OutputStream());
        try {
            out.finish();
        } catch (IOException e) {
            fail("Failed to finish output stream");
        }
        assertNotNull(out.getNextEntry());
        assertEquals("TRAILER!!!", out.getNextEntry().getName());
    }
    
    @Test
    public void testClose() {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(new OutputStream());
        try {
            out.close();
        } catch (IOException e) {
            fail("Failed to close output stream");
        }
        assertTrue(out.isClosed());
    }
}