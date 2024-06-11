import org.junit.Test;
import static org.junit.Assert.*;

public class CpioArchiveOutputStreamTest {
    
    @Test
    public void testPutNextEntry() {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(new OutputStreamStub());
        
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        
        try {
            out.putNextEntry(entry);
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        assertEquals("testfile", entry.getName());
        assertEquals(5, entry.getFileSize());
    }
    
    @Test
    public void testCloseArchiveEntry() {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(new OutputStreamStub());
        
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setSize(10);
        
        try {
            out.putNextEntry(entry);
            out.write(new byte[10]);
            out.closeArchiveEntry();
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        assertEquals(10, entry.getSize());
    }
    
    @Test
    public void testFinish() {
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(new OutputStreamStub());
        
        try {
            out.finish();
        } catch (IOException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        assertEquals("TRAILER!!!", out.getTrailer());
    }
    
    class OutputStreamStub extends OutputStream {
        public void write(int b) throws IOException {
        }

        public void write(byte[] b) throws IOException {
        }

        public void write(byte[] b, int off, int len) throws IOException {
        }

        public void flush() throws IOException {
        }

        public void close() throws IOException {
        }
    }
}