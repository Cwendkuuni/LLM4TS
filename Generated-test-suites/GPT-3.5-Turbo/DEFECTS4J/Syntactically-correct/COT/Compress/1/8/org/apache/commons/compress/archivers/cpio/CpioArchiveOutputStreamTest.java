import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CpioArchiveOutputStreamTest {
    
    private CpioArchiveOutputStream outputStream;
    
    @Before
    public void setUp() {
        outputStream = new CpioArchiveOutputStream(null);
    }
    
    @Test
    public void testPutNextEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();
        outputStream.putNextEntry(entry);
        
        assertEquals(entry, outputStream.cpioEntry);
        assertEquals(0, outputStream.written);
    }
    
    @Test
    public void testWrite() throws IOException {
        byte[] data = "test data".getBytes();
        
        outputStream.putNextEntry(new CpioArchiveEntry());
        outputStream.write(data);
        
        assertEquals(data.length, outputStream.written);
    }
    
    @Test(expected = IOException.class)
    public void testWritePastEndOfStoredEntry() throws IOException {
        byte[] data = new byte[10];
        
        outputStream.putNextEntry(new CpioArchiveEntry(20));
        outputStream.write(data);
    }
    
    @Test
    public void testFinish() throws IOException {
        outputStream.putNextEntry(new CpioArchiveEntry());
        outputStream.finish();
        
        assertNotNull(outputStream.cpioEntry);
        assertEquals(0, outputStream.cpioEntry.getMode());
        assertEquals("TRAILER!!!", outputStream.cpioEntry.getName());
        assertEquals(1, outputStream.cpioEntry.getNumberOfLinks());
        assertEquals(45, outputStream.written);
    }
    
    @Test
    public void testClose() throws IOException {
        outputStream.close();
        
        assertTrue(outputStream.closed);
    }
    
    // Additional edge case tests and exception handling tests to be added
}