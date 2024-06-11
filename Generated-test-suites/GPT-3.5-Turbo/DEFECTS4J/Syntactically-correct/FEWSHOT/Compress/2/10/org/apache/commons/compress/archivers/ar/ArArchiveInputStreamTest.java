import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() throws IOException {
        byte[] header = new byte[8];
        header[0] = 0x21;
        header[1] = 0x3c;
        header[2] = 0x61;
        header[3] = 0x72;
        header[4] = 0x63;
        header[5] = 0x68;
        header[6] = 0x3e;
        header[7] = 0x0a;

        byte[] name = "testFile".getBytes();
        byte[] length = "100".getBytes();
        byte[] trailer = "trailer".getBytes();

        byte[] testData = new byte[header.length + name.length + length.length + trailer.length];
        System.arraycopy(header, 0, testData, 0, header.length);
        System.arraycopy(name, 0, testData, header.length, name.length);
        System.arraycopy(length, 0, testData, header.length + name.length, length.length);
        System.arraycopy(trailer, 0, testData, header.length + name.length + length.length, trailer.length);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);

        ArArchiveEntry entry = arInputStream.getNextArEntry();

        assertEquals("testFile", entry.getName());
        assertEquals(100, entry.getLength());

        entry = arInputStream.getNextArEntry();
        assertNull(entry);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        byte[] header = new byte[8];
        header[0] = 0x21;
        header[1] = 0x3c;
        header[2] = 0x61;
        header[3] = 0x72;
        header[4] = 0x63;
        header[5] = 0x68;
        header[6] = 0x3e;
        header[7] = 0x0a;

        byte[] name = "testFile".getBytes();
        byte[] length = "100".getBytes();
        byte[] trailer = "trailer".getBytes();

        byte[] testData = new byte[header.length + name.length + length.length + trailer.length];
        System.arraycopy(header, 0, testData, 0, header.length);
        System.arraycopy(name, 0, testData, header.length, name.length);
        System.arraycopy(length, 0, testData, header.length + name.length, length.length);
        System.arraycopy(trailer, 0, testData, header.length + name.length + length.length, trailer.length);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);

        ArchiveEntry entry = arInputStream.getNextEntry();

        assertEquals("testFile", entry.getName());
        assertEquals(100, entry.getSize());

        entry = arInputStream.getNextEntry();
        assertNull(entry);
    }
    
    @Test
    public void testRead() throws IOException {
        byte[] testData = "testData".getBytes();

        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);

        int read = arInputStream.read();
        
        assertEquals('t', read);

        byte[] buffer = new byte[4];
        int bytesRead = arInputStream.read(buffer);

        assertEquals(4, bytesRead);
        assertEquals("estD", new String(buffer));

        bytesRead = arInputStream.read(buffer, 0, 4);

        assertEquals(4, bytesRead);
        assertEquals("ataD", new String(buffer));

        bytesRead = arInputStream.read(buffer, 0, 4);

        assertEquals(-1, bytesRead);
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[8];
        signature[0] = 0x21;
        signature[1] = 0x3c;
        signature[2] = 0x61;
        signature[3] = 0x72;
        signature[4] = 0x63;
        signature[5] = 0x68;
        signature[6] = 0x3e;
        signature[7] = 0x0a;

        boolean matches = ArArchiveInputStream.matches(signature, 8);

        assertEquals(true, matches);

        signature[7] = 0x0b;
        
        matches = ArArchiveInputStream.matches(signature, 8);

        assertEquals(false, matches);
    }

    @Test
    public void testClose() throws IOException {
        byte[] header = new byte[8];
        header[0] = 0x21;
        header[1] = 0x3c;
        header[2] = 0x61;
        header[3] = 0x72;
        header[4] = 0x63;
        header[5] = 0x68;
        header[6] = 0x3e;
        header[7] = 0x0a;

        byte[] name = "testFile".getBytes();
        byte[] length = "100".getBytes();
        byte[] trailer = "trailer".getBytes();

        byte[] testData = new byte[header.length + name.length + length.length + trailer.length];
        System.arraycopy(header, 0, testData, 0, header.length);
        System.arraycopy(name, 0, testData, header.length, name.length);
        System.arraycopy(length, 0, testData, header.length + name.length, length.length);
        System.arraycopy(trailer, 0, testData, header.length + name.length + length.length, trailer.length);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);

        arInputStream.close();

        int read = arInputStream.read();

        assertEquals(-1, read);
    }

}