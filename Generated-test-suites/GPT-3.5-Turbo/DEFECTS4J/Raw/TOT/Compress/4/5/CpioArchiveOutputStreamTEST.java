import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;

public class CpioArchiveOutputStreamTest {
    
    private File testFile;
    
    @Before
    public void setUp() {
        testFile = new File("test.cpio");
    }
    
    @Test
    public void testPutArchiveEntry() throws IOException {
        // Create a new CpioArchiveOutputStream
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(new FileOutputStream(testFile));
        
        // Create a CpioArchiveEntry and put it into the stream
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        entry.setMode(CpioArchiveOutputStream.C_ISREG); // regular file
        out.putArchiveEntry(entry);
        
        // Verify that the entry is correctly set
        assertEquals("testfile", entry.getName());
        assertEquals(5, entry.getFileSize());
        assertEquals(CpioArchiveOutputStream.C_ISREG, entry.getMode());
        
        out.close();
    }
    
    @Test
    public void testWrite() throws IOException {
        // Create a new CpioArchiveOutputStream
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(new FileOutputStream(testFile));
        
        // Create a CpioArchiveEntry and put it into the stream
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        entry.setMode(CpioArchiveOutputStream.C_ISREG); // regular file
        out.putArchiveEntry(entry);
        
        // Write data to the stream
        byte[] data = "12345".getBytes();
        out.write(data);
        
        // Close the entry
        out.closeArchiveEntry();
        out.close();
        
        // Verify that the data is correctly written to the file
        byte[] readData = new byte[data.length];
        FileInputStream in = new FileInputStream(testFile);
        in.read(readData);
        in.close();
        assertArrayEquals(data, readData);
    }
    
    @Test
    public void testCloseArchiveEntry() throws IOException {
        // Create a new CpioArchiveOutputStream
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(new FileOutputStream(testFile));
        
        // Create a CpioArchiveEntry and put it into the stream
        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName("testfile");
        entry.setFileSize(5);
        entry.setMode(CpioArchiveOutputStream.C_ISREG); // regular file
        out.putArchiveEntry(entry);
        
        // Write data to the stream
        byte[] data = "12345".getBytes();
        out.write(data);
        
        // Close the entry
        out.closeArchiveEntry();
        
        // Verify that the entry is closed
        assertNull(out.getEntry());
        
        out.close();
    }
    
    @Test
    public void testFinish() throws IOException {
        // Create a new CpioArchiveOutputStream
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(new FileOutputStream(testFile));
        
        // Close the stream without finishing
        out.close();
        
        // Verify that the finish method throws an IOException
        try {
            out.finish();
            fail("Expected IOException not thrown");
        } catch (IOException e) {
            assertEquals("This archives contains unclosed entries.", e.getMessage());
        }
    }
    
    @Test
    public void testClose() throws IOException {
        // Create a new CpioArchiveOutputStream
        CpioArchiveOutputStream out = new CpioArchiveOutputStream(new FileOutputStream(testFile));
        
        // Close the stream
        out.close();
        
        // Verify that the stream is closed
        assertTrue(out.isClosed());
    }
}