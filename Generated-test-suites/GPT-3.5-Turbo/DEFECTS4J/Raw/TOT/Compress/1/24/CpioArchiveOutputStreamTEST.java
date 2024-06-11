import org.junit.Test;
import static org.junit.Assert.*;

public class CpioArchiveOutputStreamTest {

    // Test case for putNextEntry method
    @Test
    public void testPutNextEntry() {
        CpioArchiveOutputStream os = new CpioArchiveOutputStream(System.out);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        
        try {
            os.putNextEntry(entry);
            assertEquals(entry, os.getEntry());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
        
        os.close();
    }

    // Test case for write method
    @Test
    public void testWrite() {
        CpioArchiveOutputStream os = new CpioArchiveOutputStream(System.out);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        
        try {
            os.putNextEntry(entry);
            byte[] data = "Hello".getBytes();
            os.write(data, 0, data.length);
            os.closeArchiveEntry();
            
            assertEquals(data.length, os.getBytesWritten());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
        
        os.close();
    }

    // Test case for closeArchiveEntry method
    @Test
    public void testCloseArchiveEntry() {
        CpioArchiveOutputStream os = new CpioArchiveOutputStream(System.out);
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        
        try {
            os.putNextEntry(entry);
            os.write("Hello".getBytes());
            os.closeArchiveEntry();
            
            assertNull(os.getEntry());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
        
        os.close();
    }

    // Test case for finish method
    @Test
    public void testFinish() {
        CpioArchiveOutputStream os = new CpioArchiveOutputStream(System.out);
        
        try {
            os.finish();
            assertNotNull(os.getEntry());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
        
        os.close();
    }

    // Test case for close method
    @Test
    public void testClose() {
        CpioArchiveOutputStream os = new CpioArchiveOutputStream(System.out);
        
        try {
            os.close();
            assertTrue(os.isClosed());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
}