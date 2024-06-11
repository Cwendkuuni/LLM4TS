import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.*;

public class ArArchiveInputStreamTest {

    private ArArchiveInputStream arArchiveInputStream;

    @Before
    public void setup() {
        byte[] data = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        ByteArrayInputStream is = new ByteArrayInputStream(data);
        arArchiveInputStream = new ArArchiveInputStream(is);
    }

    @Test
    public void testGetNextArEntry() throws IOException {
        ArArchiveEntry entry = arArchiveInputStream.getNextArEntry();
        assertNull(entry);

        byte[] header = ArArchiveEntry.HEADER.getBytes();
        byte[] entryData = new byte[header.length];
        for (int i = 0; i < header.length; i++) {
            entryData[i] = arArchiveInputStream.readByte();
        }
        assertArrayEquals(header, entryData);

        entry = arArchiveInputStream.getNextArEntry();
        assertNotNull(entry);
        assertEquals("", entry.getName());
        assertEquals(0, entry.getSize());

        byte[] trailer = ArArchiveEntry.TRAILER.getBytes();
        byte[] trailerData = new byte[trailer.length];
        for (int i = 0; i < trailer.length; i++) {
            trailerData[i] = arArchiveInputStream.readByte();
        }
        assertArrayEquals(trailer, trailerData);
    }

    @Test
    public void testGetNextEntry() throws IOException {
        ArchiveEntry entry = arArchiveInputStream.getNextEntry();
        assertNull(entry);

        byte[] header = ArArchiveEntry.HEADER.getBytes();
        byte[] entryData = new byte[header.length];
        for (int i = 0; i < header.length; i++) {
            entryData[i] = arArchiveInputStream.readByte();
        }
        assertArrayEquals(header, entryData);

        entry = arArchiveInputStream.getNextEntry();
        assertNotNull(entry);
        assertEquals("", entry.getName());
        assertEquals(0, entry.getSize());

        byte[] trailer = ArArchiveEntry.TRAILER.getBytes();
        byte[] trailerData = new byte[trailer.length];
        for (int i = 0; i < trailer.length; i++) {
            trailerData[i] = arArchiveInputStream.readByte();
        }
        assertArrayEquals(trailer, trailerData);
    }

    @Test
    public void testClose() throws IOException {
        assertFalse(arArchiveInputStream.isClosed());
        arArchiveInputStream.close();
        assertTrue(arArchiveInputStream.isClosed());
    }

    @Test
    public void testRead() throws IOException {
        int data = arArchiveInputStream.read();
        assertEquals(0x21, data);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] data = new byte[8];
        int bytesRead = arArchiveInputStream.read(data);
        assertEquals(8, bytesRead);
        assertArrayEquals(new byte[]{0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 0x0}, data);
    }

    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException {
        byte[] data = new byte[8];
        int bytesRead = arArchiveInputStream.read(data, 2, 4);
        assertEquals(4, bytesRead);
        assertArrayEquals(new byte[]{0x0, 0x0, 0x3c, 0x61, 0x0, 0x0, 0x0, 0x0}, data);
    }

    @Test
    public void testMatches() {
        byte[] signature = new byte[]{0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        boolean result = ArArchiveInputStream.matches(signature, 8);
        assertTrue(result);
    }
}