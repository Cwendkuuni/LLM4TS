import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class CpioArchiveOutputStreamTest {

    @Test
    public void testPutArchiveEntry() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out);
        
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        entry.setMode(CpioConstants.C_ISREG);
        
        cpioOut.putArchiveEntry(entry);
        cpioOut.closeArchiveEntry();
        
        byte[] result = out.toByteArray();
        
        assertNotNull(result);
        assertEquals(110, result.length); // Expected header size
        
        // Add more assertions for edge cases if needed
    }

    @Test
    public void testWrite() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out);
        
        byte[] data = "12345".getBytes();
        cpioOut.putArchiveEntry(new CpioArchiveEntry());
        cpioOut.write(data, 0, data.length);
        cpioOut.closeArchiveEntry();
        
        byte[] result = out.toByteArray();
        
        assertNotNull(result);
        assertEquals(110 + data.length, result.length); // Expected header size + data size
        
        // Add more assertions for edge cases if needed
    }

    @Test
    public void testPutArchiveEntryFormatException() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out);
        
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        entry.setMode(CpioConstants.C_ISREG);
        entry.setFormat(99); // Invalid format
        
        try {
            cpioOut.putArchiveEntry(entry);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testClose() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        CpioArchiveOutputStream cpioOut = new CpioArchiveOutputStream(out);
        
        cpioOut.close();
        
        assertTrue(cpioOut.isClosed());
    }
}