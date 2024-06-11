import static org.junit.Assert.*;
import org.junit.Test;

public class CpioArchiveOutputStreamTest {
    
    // Test case for putNextEntry() method
    @Test
    public void testPutNextEntry() {
        CpioArchiveOutputStream output = new CpioArchiveOutputStream();
        
        // Test typical use case
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        output.putNextEntry(entry);
        assertEquals(entry, output.getCurrentEntry());
        
        // Test edge case with a long file name
        entry.setName("testfilewithlongname");
        output.putNextEntry(entry);
        assertEquals(entry, output.getCurrentEntry());
        
        // Test error scenario when duplicate entry is added
        CpioArchiveEntry duplicateEntry = new CpioArchiveEntry();
        duplicateEntry.setName("testfile");
        assertThrows(IOException.class, () -> {
            output.putNextEntry(duplicateEntry);
        });
    }
    
    // Test case for closeArchiveEntry() method
    @Test
    public void testCloseArchiveEntry() {
        CpioArchiveOutputStream output = new CpioArchiveOutputStream();
        CpioArchiveEntry entry = new CpioArchiveEntry();
        
        // Test typical use case
        output.putNextEntry(entry);
        output.closeArchiveEntry();
        assertNull(output.getCurrentEntry());
        
        // Test error scenario when entry size does not match written bytes
        entry.setSize(10);
        output.putNextEntry(entry);
        assertThrows(IOException.class, () -> {
            output.closeArchiveEntry();
        });
    }
    
    // Test case for write() method
    @Test
    public void testWrite() {
        CpioArchiveOutputStream output = new CpioArchiveOutputStream();
        CpioArchiveEntry entry = new CpioArchiveEntry();
        output.putNextEntry(entry);
        
        // Test typical use case
        byte[] data = {1, 2, 3, 4, 5};
        output.write(data, 0, 5);
        assertEquals(5, output.getBytesWritten());
        
        // Test error scenario when attempting to write past end of entry
        assertThrows(IOException.class, () -> {
            output.write(data, 0, 10);
        });
    }
    
    // Test case for finish() method
    @Test
    public void testFinish() {
        CpioArchiveOutputStream output = new CpioArchiveOutputStream();
        CpioArchiveEntry entry = new CpioArchiveEntry();
        output.putNextEntry(entry);
        
        // Test typical use case
        try {
            output.finish();
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
        
        // Test calling finish() twice
        output.finish();
        
        // Test error scenario when checksum does not match
        entry.setFormat(CpioConstants.FORMAT_NEW_CRC);
        entry.setChksum(123);
        assertThrows(IOException.class, () -> {
            output.finish();
        });
    }
    
    // Test case for close() method
    @Test
    public void testClose() {
        CpioArchiveOutputStream output = new CpioArchiveOutputStream();
        
        // Test typical use case
        try {
            output.close();
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
        
        // Test calling close() twice
        try {
            output.close();
        } catch (IOException e) {
            fail("IOException thrown: " + e.getMessage());
        }
    }
    
}