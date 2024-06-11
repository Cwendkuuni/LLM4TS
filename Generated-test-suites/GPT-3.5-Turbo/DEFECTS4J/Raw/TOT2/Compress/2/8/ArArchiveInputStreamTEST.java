import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() throws IOException {
        byte[] testData = { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, 0x00, 0x00 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);

        ArArchiveEntry entry = arInputStream.getNextArEntry();

        assertNotNull(entry);
        assertEquals("ar", entry.getName());
        assertEquals(0, entry.getLength());
    }

    @Test
    public void testMatches() {
        byte[] signature = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a};
        assertTrue(ArArchiveInputStream.matches(signature, signature.length));

        byte[] invalidSignature = {0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e};
        assertFalse(ArArchiveInputStream.matches(invalidSignature, invalidSignature.length));
    }

    @Test
    public void testRead() throws IOException {
        byte[] testData = { 0x01, 0x02, 0x03 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);

        assertEquals(0x01, arInputStream.read());
        assertEquals(0x02, arInputStream.read());
        assertEquals(0x03, arInputStream.read());

        assertEquals(-1, arInputStream.read());
    }

    @Test
    public void testReadBytes() throws IOException {
        byte[] testData = { 0x01, 0x02, 0x03 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);
        byte[] buffer = new byte[2];

        assertEquals(2, arInputStream.read(buffer));
        assertArrayEquals(new byte[] { 0x01, 0x02 }, buffer);

        assertEquals(1, arInputStream.read(buffer));
        assertArrayEquals(new byte[] { 0x03, 0x02 }, buffer);
    }

    @Test
    public void testClose() throws IOException {
        byte[] testData = { 0x01, 0x02, 0x03 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        ArArchiveInputStream arInputStream = new ArArchiveInputStream(inputStream);

        assertFalse(arInputStream.isClosed());
        arInputStream.close();
        assertTrue(arInputStream.isClosed());
    }

}