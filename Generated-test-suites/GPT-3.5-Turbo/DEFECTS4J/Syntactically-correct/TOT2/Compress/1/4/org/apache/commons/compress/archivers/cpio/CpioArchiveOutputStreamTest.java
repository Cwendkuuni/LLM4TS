import static org.junit.Assert.*;
import org.junit.Test;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testConstructorWithFormat() {
        // Test constructor with format parameter
        // Edge case: Valid format specified
        CpioArchiveOutputStream cpio = new CpioArchiveOutputStream(outputStream, CpioArchiveOutputStream.FORMAT_NEW);
        assertNotNull(cpio);
    }

    @Test
    public void testConstructorWithoutFormat() {
        // Test constructor without format parameter (defaults to FORMAT_NEW)
        CpioArchiveOutputStream cpio = new CpioArchiveOutputStream(outputStream);
        assertNotNull(cpio);
    }

    @Test
    public void testPutNextEntry() {
        // Test putNextEntry method
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        
        // Typical use case
        cpio.putNextEntry(entry);
        assertEquals(entry, cpio.cpioEntry);
        
        // Edge case: Entry with no modification time set
        CpioArchiveEntry entryNoTime = new CpioArchiveEntry();
        cpio.putNextEntry(entryNoTime);
        assertNotEquals(-1, entryNoTime.getTime());
        
        // Exception handling: Duplicate entry name
        CpioArchiveEntry duplicateEntry = new CpioArchiveEntry();
        duplicateEntry.setName("testfile");
        assertThrows(IOException.class, () -> cpio.putNextEntry(duplicateEntry));
    }

    @Test
    public void testWrite() {
        // Test write method
        byte[] data = "12345".getBytes();
        
        // Typical use case
        cpio.putNextEntry(new CpioArchiveEntry());
        cpio.write(data, 0, data.length);
        assertEquals(data.length, cpio.written);
        
        // Edge case: Writing past end of entry
        assertThrows(IOException.class, () -> cpio.write(data, 0, data.length + 1));
    
    }

    @Test
    public void testCloseArchiveEntry() {
        // Test closeArchiveEntry method
        cpio.putNextEntry(new CpioArchiveEntry());
        
        // Typical use case
        cpio.write("12345".getBytes(), 0, 5);
        cpio.closeArchiveEntry();

        // Edge case: Mismatch in entry size
        cpio.putNextEntry(new CpioArchiveEntry());
        assertThrows(IOException.class, () -> cpio.closeArchiveEntry());
    }

    @Test
    public void testFinish() {
        // Test finish method
        cpio.putNextEntry(new CpioArchiveEntry());
        cpio.finish();
        assertTrue(cpio.finished);
    }
    
    @Test
    public void testClose() {
        // Test close method
        cpio.close();
        assertTrue(cpio.closed);
    }

}