import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Test;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() throws IOException {
        byte[] testData = "This is a test data".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(testData);
        ArArchiveInputStream arInput = new ArArchiveInputStream(input);

        ArArchiveEntry entry = arInput.getNextArEntry();

        assertEquals("This", entry.getFileName());
        assertEquals(testData.length, entry.getSize());
        assertTrue(arInput.available() > 0);

        arInput.close();
    }

    @Test
    public void testGetNextEntry() throws IOException {
        byte[] testData = "This is a test data".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(testData);
        ArArchiveInputStream arInput = new ArArchiveInputStream(input);

        ArchiveEntry entry = arInput.getNextEntry();

        assertTrue(entry instanceof ArArchiveEntry);
        assertEquals("This", entry.getFileName());
        assertEquals(testData.length, entry.getSize());
        assertTrue(arInput.available() > 0);

        arInput.close();
    }

    @Test
    public void testClose() throws IOException {
        byte[] testData = "This is a test data".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(testData);
        ArArchiveInputStream arInput = new ArArchiveInputStream(input);

        arInput.close();

        assertEquals(0, arInput.available());
    }

    @Test
    public void testRead() throws IOException {
        byte[] testData = "This is a test data".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(testData);
        ArArchiveInputStream arInput = new ArArchiveInputStream(input);

        assertEquals('T', arInput.read());
        assertEquals('h', arInput.read());
        assertEquals('i', arInput.read());
        assertEquals('s', arInput.read());
        assertEquals(0, arInput.read());
        assertEquals(-1, arInput.read());

        arInput.close();
    }

    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] testData = "This is a test data".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(testData);
        ArArchiveInputStream arInput = new ArArchiveInputStream(input);

        byte[] buffer = new byte[10];
        int bytesRead = arInput.read(buffer);

        assertEquals(10, bytesRead);
        assertEquals('T', buffer[0]);
        assertEquals('h', buffer[1]);
        assertEquals('i', buffer[2]);
        assertEquals('s', buffer[3]);
        assertEquals(' ', buffer[4]);
        assertEquals('i', buffer[5]);
        assertEquals('s', buffer[6]);
        assertEquals(' ', buffer[7]);
        assertEquals('a', buffer[8]);
        assertEquals(' ', buffer[9]);

        arInput.close();
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] testData = "This is a test data".getBytes();
        ByteArrayInputStream input = new ByteArrayInputStream(testData);
        ArArchiveInputStream arInput = new ArArchiveInputStream(input);

        byte[] buffer = new byte[10];
        int bytesRead = arInput.read(buffer, 2, 6);

        assertEquals(6, bytesRead);
        assertEquals(0, buffer[0]);
        assertEquals(0, buffer[1]);
        assertEquals('T', buffer[2]);
        assertEquals('h', buffer[3]);
        assertEquals('i', buffer[4]);
        assertEquals('s', buffer[5]);
        assertEquals(' ', buffer[6]);
        assertEquals(0, buffer[7]);
        assertEquals(0, buffer[8]);
        assertEquals(0, buffer[9]);

        arInput.close();
    }

    @Test
    public void testMatches() {
        byte[] signature = {(byte) 0x21, (byte) 0x3c, (byte) 0x61, (byte) 0x72,
                (byte)  0x63, (byte) 0x68, (byte) 0x3e, (byte) 0x0a};

        assertTrue(ArArchiveInputStream.matches(signature, signature.length));
    }

}