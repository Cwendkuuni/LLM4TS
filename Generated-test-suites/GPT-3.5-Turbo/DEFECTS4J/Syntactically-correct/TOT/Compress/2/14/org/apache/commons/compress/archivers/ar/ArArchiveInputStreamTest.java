
import static org.junit.Assert.*;
import org.junit.Test;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() {
        // Test case 1: Typical use case, valid header and content
        ArArchiveInputStream arInputStream = new ArArchiveInputStream();
        ArArchiveEntry entry = arInputStream.getNextArEntry();
        assertNotNull(entry);
        assertEquals("SampleEntry", entry.getName());
        assertEquals(100, entry.getLength());

        // Test case 2: Typical use case, empty entry
        entry = arInputStream.getNextArEntry();
        assertNotNull(entry);
        assertEquals("", entry.getName());
        assertEquals(0, entry.getLength());

        // Test case 3: Edge case, entry name exceeds 16 characters
        entry = arInputStream.getNextArEntry();
        assertNotNull(entry);
        assertEquals("LongEntryNameLongEn", entry.getName());
        assertEquals(500, entry.getLength());

        // Test case 4: Error scenario, invalid header
        try {
            entry = arInputStream.getNextArEntry();
            fail("IOException not thrown");
        } catch (IOException e) {
            assertEquals("invalid header", e.getMessage());
        }
    }

    @Test
    public void testGetNextEntry() {
        // Test case 1: Equivalent to testGetNextArEntry, same logic as getNextArEntry
    }

    @Test
    public void testClose() {
        // Test case 1: Typical use case, close input stream
        ArArchiveInputStream arInputStream = new ArArchiveInputStream();
        arInputStream.close();
        assertTrue(arInputStream.isClosed());

        // Test case 2: Repeated close, no exception thrown
        arInputStream.close();
        assertTrue(arInputStream.isClosed());
    }

    @Test
    public void testRead() {
        // Test case 1: Typical use case, read single byte
        ArArchiveInputStream arInputStream = new ArArchiveInputStream();
        int byteRead = arInputStream.read();
        assertEquals(97, byteRead);

        // Test case 2: Typical use case, read bytes into byte array
        byte[] buffer = new byte[10];
        int bytesRead = arInputStream.read(buffer);
        assertEquals(10, bytesRead);

        // Test case 3: Edge case, empty byte array
        buffer = new byte[0];
        bytesRead = arInputStream.read(buffer);
        assertEquals(0, bytesRead);

        // Test case 4: Error scenario, read after stream is closed
        arInputStream.close();
        try {
            byteRead = arInputStream.read();
            fail("IOException not thrown");
        } catch (IOException e) {
            assertEquals("Stream closed", e.getMessage());
        }
    }

    @Test
    public void testMatches() {
        // Test case 1: Typical use case, valid signature
        byte[] signature = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        assertTrue(ArArchiveInputStream.matches(signature, signature.length));

        // Test case 2: Error scenario, invalid signature
        byte[] invalidSignature = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x09};
        assertFalse(ArArchiveInputStream.matches(invalidSignature, invalidSignature.length));
    }

}
