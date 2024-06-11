import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() throws IOException {
        byte[] testData = "!<header>\n<data>\n<trailer>\n".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(testData);
        ArArchiveInputStream ais = new ArArchiveInputStream(input);
        
        ArArchiveEntry entry = ais.getNextArEntry();
        
        assertNotNull(entry);
        assertEquals("<header>", entry.getName());
        assertEquals(6, entry.getSize());
        
        input.close();
    }

    @Test
    public void testGetNextEntry() throws IOException {
        byte[] testData = "!<header>\n<data>\n<trailer>\n".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(testData);
        ArArchiveInputStream ais = new ArArchiveInputStream(input);
        
        ArchiveEntry entry = ais.getNextEntry();
        
        assertNotNull(entry);
        assertEquals("ArArchiveEntry", entry.getClass().getSimpleName());
        
        input.close();
    }

    @Test
    public void testClose() throws IOException {
        byte[] testData = "!<header>\n<data>\n<trailer>\n".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(testData);
        ArArchiveInputStream ais = new ArArchiveInputStream(input);
        
        ais.close();
        
        assertTrue(ais.isClosed());
    }

    @Test
    public void testRead() throws IOException {
        byte[] testData = "test data".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(testData);
        ArArchiveInputStream ais = new ArArchiveInputStream(input);
        
        assertEquals('t', ais.read());
        
        input.close();
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] testData = "test data".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(testData);
        ArArchiveInputStream ais = new ArArchiveInputStream(input);
        
        byte[] buffer = new byte[4];
        ais.read(buffer);
        
        assertArrayEquals("test".getBytes(), buffer);
        
        input.close();
    }

    @Test
    public void testMatches() {
        byte[] signature = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        
        assertTrue(ArArchiveInputStream.matches(signature, 8));
    }

}