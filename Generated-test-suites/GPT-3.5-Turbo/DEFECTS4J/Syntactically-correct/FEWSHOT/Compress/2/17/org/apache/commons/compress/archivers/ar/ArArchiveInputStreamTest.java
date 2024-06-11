import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() throws IOException {
        byte[] data = { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 0x66, 0x6f, 0x6f, 0x2e, 0x74, 0x78, 0x74, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);
        
        ArchiveEntry entry = arInputStream.getNextArEntry();
        assertEquals("foo.txt", entry.getName());
        assertEquals(0L, entry.getSize());
        
        entry = arInputStream.getNextArEntry();
        assertNull(entry);
        
        arInputStream.close();
    }

    @Test
    public void testGetNextEntry() throws IOException {
        byte[] data = { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 0x66, 0x6f, 0x6f, 0x2e, 0x74, 0x78, 0x74, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);
        
        ArchiveEntry entry = arInputStream.getNextEntry();
        assertEquals("foo.txt", entry.getName());
        assertEquals(0L, entry.getSize());
        
        entry = arInputStream.getNextEntry();
        assertNull(entry);
        
        arInputStream.close();
    }

    @Test
    public void testClose() throws IOException {
        byte[] data = { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 0x66, 0x6f, 0x6f, 0x2e, 0x74, 0x78, 0x74, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);
        
        arInputStream.close();
    }

    @Test
    public void testRead() throws IOException {
        byte[] data = { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 0x66, 0x6f, 0x6f, 0x2e, 0x74, 0x78, 0x74, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);
        
        int read = arInputStream.read();
        assertEquals(0x21, read);
        
        arInputStream.close();
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] data = { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 0x66, 0x6f, 0x6f, 0x2e, 0x74, 0x78, 0x74, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);
        
        byte[] buffer = new byte[8];
        int read = arInputStream.read(buffer);
        assertEquals(8, read);
        
        arInputStream.close();
    }

    @Test
    public void testReadByteArrayIntInt() throws IOException {
        byte[] data = { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 0x66, 0x6f, 0x6f, 0x2e, 0x74, 0x78, 0x74, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);
        
        byte[] buffer = new byte[8];
        int read = arInputStream.read(buffer, 0, 8);
        assertEquals(8, read);
        
        arInputStream.close();
    }

    @Test
    public void testMatches() {
        byte[] signature = { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a };
        boolean result = ArArchiveInputStream.matches(signature, 8);
        assertEquals(true, result);
    }

}