import org.junit.Test;
import static org.junit.Assert.*;

public class CpioArchiveOutputStreamTest {
    
    @Test
    public void testPutNextEntry() {
        CpioArchiveOutputStream output = new CpioArchiveOutputStream();
        CpioArchiveEntry entry = new CpioArchiveEntry();
        
        try {
            output.putNextEntry(entry);
            assertEquals(entry, output.getCpioEntry());
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testWrite() {
        CpioArchiveOutputStream output = new CpioArchiveOutputStream();
        byte[] testData = "test data".getBytes();
        
        try {
            output.putNextEntry(new CpioArchiveEntry());
            output.write(testData, 0, testData.length);
            assertEquals(testData.length, output.getWritten());
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testCloseArchiveEntry() {
        CpioArchiveOutputStream output = new CpioArchiveOutputStream();
        
        try {
            output.putNextEntry(new CpioArchiveEntry());
            output.write("test data".getBytes());
            output.closeArchiveEntry();
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testFinish() {
        CpioArchiveOutputStream output = new CpioArchiveOutputStream();
        
        try {
            output.finish();
            assertTrue(output.isFinished());
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }
    
    @Test
    public void testClose() {
        CpioArchiveOutputStream output = new CpioArchiveOutputStream();
        
        try {
            output.close();
            assertTrue(output.isClosed());
        } catch (IOException e) {
            fail("Exception should not be thrown");
        }
    }
}