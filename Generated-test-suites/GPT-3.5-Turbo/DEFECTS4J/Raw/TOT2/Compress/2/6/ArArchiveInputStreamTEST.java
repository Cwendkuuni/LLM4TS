import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry_validEntry() throws IOException {
        byte[] data = "Content of archive entry".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);
        
        ArArchiveEntry entry = arInputStream.getNextArEntry();
        
        assertNotNull(entry);
        assertEquals("Content of archive entry", entry.getName());
        assertEquals(22, entry.getSize());
    }

    @Test
    public void testGetNextArEntry_eofBeforeCompleteEntry() throws IOException {
        byte[] data = "Header not complete".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);
        
        try {
            arInputStream.getNextArEntry();
            fail("IOException expected");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testGetNextArEntry_invalidHeader() throws IOException {
        byte[] data = "Invalid header".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);
        
        try {
            arInputStream.getNextArEntry();
            fail("IOException expected");
        } catch (IOException e) {
            // expected
        }
    }

    @Test
    public void testRead() throws IOException {
        byte[] data = "Test read method".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);
        
        assertEquals('T', arInputStream.read());
        assertEquals('e', arInputStream.read());
    }

    @Test
    public void testReadBytes() throws IOException {
        byte[] data = "Test read bytes method".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);
        
        byte[] buffer = new byte[10];
        int bytesRead = arInputStream.read(buffer);
        
        assertEquals(10, bytesRead);
        assertEquals("Test read ", new String(buffer));
    }

    @Test
    public void testReadBytesWithOffset() throws IOException {
        byte[] data = "Test read bytes with offset method".getBytes();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);
        
        byte[] buffer = new byte[10];
        int bytesRead = arInputStream.read(buffer, 5, 10);
        
        assertEquals(10, bytesRead);
        assertEquals("\u0000\u0000\u0000\u0000\u0000Test rea", new String(buffer));
    }

    @Test
    public void testMatches_validSignature() {
        byte[] signature = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        
        assertTrue(ArArchiveInputStream.matches(signature, 8));
    }

    @Test
    public void testMatches_invalidSignature() {
        byte[] signature = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x00};
        
        assertFalse(ArArchiveInputStream.matches(signature, 8));
    }

    @Test
    public void testMatches_shortSignature() {
        byte[] signature = {0x21, 0x3c, 0x61};
        
        assertFalse(ArArchiveInputStream.matches(signature, 3));
    }
}