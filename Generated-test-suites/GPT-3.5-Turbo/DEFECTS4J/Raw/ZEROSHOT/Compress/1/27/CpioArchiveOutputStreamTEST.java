import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CpioArchiveOutputStreamTest {
    
    private CpioArchiveOutputStream cpioOutput;
    private CpioArchiveEntry cpioEntry;
    private byte[] testData = "test data".getBytes();
    
    @Before
    public void setup() throws Exception {
        cpioOutput = new CpioArchiveOutputStream(new OutputStream() {
            public void write(int b) {
                // do nothing
            }
        });
        
        cpioEntry = new CpioArchiveEntry();
        cpioEntry.setName("testfile");
        cpioEntry.setFileSize(testData.length);
    }
    
    @Test
    public void testPutNextEntry() throws Exception {
        cpioOutput.putNextEntry(cpioEntry);
        assertEquals(cpioEntry, cpioOutput.cpioEntry);
        assertEquals(0, cpioOutput.written);
    }
    
    @Test(expected = IOException.class)
    public void testPutNextEntryWithoutStream() throws Exception {
        CpioArchiveOutputStream cpioOutput = new CpioArchiveOutputStream(null);
        cpioOutput.putNextEntry(cpioEntry);
    }
    
    @Test(expected = IOException.class)
    public void testPutNextEntryWithInvalidEntry() throws Exception {
        cpioEntry.setFormat((short) 42);
        cpioOutput.putNextEntry(cpioEntry);
    }
    
    @Test(expected = IOException.class)
    public void testPutNextEntryWithDuplicateEntry() throws Exception {
        cpioOutput.putNextEntry(cpioEntry);
        cpioOutput.putNextEntry(cpioEntry);
    }
    
    @Test
    public void testWrite() throws Exception {
        cpioOutput.putNextEntry(cpioEntry);
        cpioOutput.write(testData);
        
        assertEquals(testData.length, cpioOutput.written);
        assertEquals(0, cpioOutput.crc);
    }
    
    @Test(expected = IOException.class)
    public void testWriteWithoutEntry() throws Exception {
        cpioOutput.write(testData);
    }
    
    @Test(expected = IOException.class)
    public void testWritePastEndOfEntry() throws Exception {
        cpioEntry.setSize(5);
        cpioOutput.putNextEntry(cpioEntry);
        
        byte[] data = "123456".getBytes();
        cpioOutput.write(data);
    }
    
    @Test
    public void testFinish() throws Exception {
        cpioOutput.putNextEntry(cpioEntry);
        cpioOutput.write(testData);
        cpioOutput.finish();
        
        assertEquals(0, cpioOutput.crc);
    }
    
    @Test
    public void testFinishWithoutEntry() throws Exception {
        cpioOutput.finish();
        
        assertEquals(0, cpioOutput.crc);
    }
    
    @Test(expected = IOException.class)
    public void testClose() throws Exception {
        cpioOutput.close();
        
        cpioOutput.putNextEntry(cpioEntry);
    }
    
}