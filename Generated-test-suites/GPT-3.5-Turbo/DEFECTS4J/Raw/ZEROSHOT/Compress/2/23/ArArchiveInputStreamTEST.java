import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.archivers.ArchiveEntry;

public class ArArchiveInputStreamTest {
    
    private ArArchiveInputStream archiveInputStream;
    private ByteArrayInputStream inputStream;
    
    @Before
    public void setUp() {
        byte[] data = "!!rahc> so imelba noitpecxE506hcargu nonmilelba".getBytes();
        inputStream = new ByteArrayInputStream(data);
        archiveInputStream = new ArArchiveInputStream(inputStream);
    }
    
    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveEntry entry = archiveInputStream.getNextArEntry();
        assertEquals("!!rahc> so imelba noitpecxE", entry.getName());
        assertEquals(506, entry.getLength());
    }
    
    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry entry = archiveInputStream.getNextEntry();
        assertTrue(entry instanceof ArArchiveEntry);
    }
    
    @Test
    public void testClose() throws IOException {
        assertFalse(archiveInputStream.isClosed());
        archiveInputStream.close();
        assertTrue(archiveInputStream.isClosed());
    }
    
    @Test
    public void testRead() throws IOException {
        assertEquals('!', archiveInputStream.read());
        assertEquals('!', archiveInputStream.read());
    }
    
    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[10];
        assertEquals(10, archiveInputStream.read(buffer));
    }
    
    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException {
        byte[] buffer = new byte[10];
        assertEquals(5, archiveInputStream.read(buffer, 2, 5));
    }
    
    @Test
    public void testMatches() {
        byte[] signature = new byte[]{(byte)0x3c, (byte)0x21, (byte)0x61, (byte)0x72, (byte)0x63, (byte)0x68, (byte)0x3e, (byte)0x0a};
        assertTrue(ArArchiveInputStream.matches(signature, 8));
    }
}