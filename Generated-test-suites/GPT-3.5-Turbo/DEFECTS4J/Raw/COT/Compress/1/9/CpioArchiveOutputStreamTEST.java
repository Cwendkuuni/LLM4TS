import org.junit.Test;
import static org.junit.Assert.*;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testConstructor() {
        // Test the constructor with a specified format
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(null, CpioArchiveOutputStream.FORMAT_NEW);
        assertNotNull(out);
        
        // Test the constructor with the default format
        CpioArchiveOutputStream out2 = new CpioArchiveOutputStream(null);
        assertNotNull(out2);
    }
    
    @Test
    public void testPutNextEntry() {
        // Test putting a new entry
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(null, CpioArchiveOutputStream.FORMAT_NEW);
        try {
            out.putNextEntry(entry);
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
        
        // Test closing the current entry
        try {
            out.closeArchiveEntry();
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testWrite() {
        // Test writing bytes to the current entry
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(null, CpioArchiveOutputStream.FORMAT_NEW);
        try {
            out.putNextEntry(entry);
            out.write("12345".getBytes());
            out.closeArchiveEntry();
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testFinish() {
        // Test finishing the output stream
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(null, CpioArchiveOutputStream.FORMAT_NEW);
        try {
            out.finish();
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testClose() {
        // Test closing the output stream
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(null, CpioArchiveOutputStream.FORMAT_NEW);
        try {
            out.close();
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }
}