import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ArArchiveInputStreamTest {

    private static final byte[] SAMPLE_AR_FILE = {
        0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, // header
        // first entry
        'f', 'i', 'l', 'e', '1', '.', 't', 'x', 't', '\0', '\0', '\0', '\0', '\0', '\0', '\0', // name
        '1', '5', '4', '3', '2', '1', '0', '4', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', // last modified
        '0', '0', '0', '0', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', // user id
        '0', '0', '0', '0', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', // group id
        '0', '0', '0', '0', '6', '4', '6', '4', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', // mode
        '1', '4', '4', '3', '2', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', // length
        0x60, 0x0a, // trailer

        // second entry
        'f', 'i', 'l', 'e', '2', '.', 't', 'x', 't', '\0', '\0', '\0', '\0', '\0', '\0', '\0', // name
        '1', '5', '4', '3', '2', '1', '0', '4', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', // last modified
        '0', '0', '0', '0', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', // user id
        '0', '0', '0', '0', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', // group id
        '0', '0', '0', '0', '6', '4', '6', '4', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', // mode
        '1', '4', '4', '3', '2', '0', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', // length
        0x60, 0x0a // trailer
    };

    @Test
    public void testGetNextArEntry() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(SAMPLE_AR_FILE);
        ArArchiveInputStream arStream = new ArArchiveInputStream(input);

        assertNotNull(arStream.getNextArEntry());
        assertNotNull(arStream.getNextArEntry());
        assertNull(arStream.getNextArEntry());
    }

    @Test
    public void testReadByte() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(SAMPLE_AR_FILE);
        ArArchiveInputStream arStream = new ArArchiveInputStream(input);

        assertEquals(33, arStream.read());
        assertEquals(60, arStream.read());
    }

    @Test
    public void testReadByteArray() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(SAMPLE_AR_FILE);
        ArArchiveInputStream arStream = new ArArchiveInputStream(input);

        byte[] buffer = new byte[5];
        arStream.read(buffer);
        assertArrayEquals(new byte[] { 33, 60, 97, 114, 99 }, buffer);
    }

    @Test
    public void testReadByteArrayOffsetLength() throws IOException {
        ByteArrayInputStream input = new ByteArrayInputStream(SAMPLE_AR_FILE);
        ArArchiveInputStream arStream = new ArArchiveInputStream(input);

        byte[] buffer = new byte[5];
        arStream.read(buffer, 1, 3);
        assertArrayEquals(new byte[] { 0, 33, 60, 97, 0 }, buffer);
    }

    @Test
    public void testMatches() {
        byte[] signature = { 33, 60, 97, 114, 99, 104, 62, 10 };
        assertTrue(ArArchiveInputStream.matches(signature, 8));

        byte[] invalidSignature = { 0, 60, 97, 114, 99, 104, 62, 10 };
        assertFalse(ArArchiveInputStream.matches(invalidSignature, 8));
    }
}