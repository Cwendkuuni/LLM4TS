import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class ArArchiveInputStreamTest {

    @Test
    public void testGetNextArEntry() {
        byte[] arData = {
            0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, // AR header
            // AR entry data
            'f', 'i', 'l', 'e', '1', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
            '1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
            0x60, 0x3E, 0x0A // AR entry trailer
        };

        ArArchiveInputStream arStream = new ArArchiveInputStream(new ByteArrayInputStream(arData));
        try {
            assertNotNull(arStream.getNextArEntry());
        } catch (IOException e) {
            fail("IOException not expected");
        }
    }

    @Test
    public void testGetNextEntry() {
        byte[] arData = {
            0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a, // AR header
            // AR entry data
            'f', 'i', 'l', 'e', '1', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ',
            '1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
            0x60, 0x3E, 0x0A // AR entry trailer
        };

        ArArchiveInputStream arStream = new ArArchiveInputStream(new ByteArrayInputStream(arData));
        try {
            assertNotNull(arStream.getNextEntry());
        } catch (IOException e) {
            fail("IOException not expected");
        }
    }

    @Test
    public void testRead() {
        byte[] testData = { 0x41, 0x42, 0x43 }; // ABC

        ArArchiveInputStream arStream = new ArArchiveInputStream(new ByteArrayInputStream(testData));
        try {
            assertEquals(0x41, arStream.read());
            assertEquals(0x42, arStream.read());
            assertEquals(0x43, arStream.read());
        } catch (IOException e) {
            fail("IOException not expected");
        }
    }

    @Test
    public void testReadByteArray() {
        byte[] testData = { 0x41, 0x42, 0x43 }; // ABC

        byte[] buffer = new byte[3];
        ArArchiveInputStream arStream = new ArArchiveInputStream(new ByteArrayInputStream(testData));
        try {
            assertEquals(3, arStream.read(buffer));
            assertArrayEquals(testData, buffer);
        } catch (IOException e) {
            fail("IOException not expected");
        }
    }

    @Test
    public void testReadByteArrayOffLen() {
        byte[] testData = { 0x41, 0x42, 0x43 }; // ABC

        byte[] buffer = new byte[3];
        ArArchiveInputStream arStream = new ArArchiveInputStream(new ByteArrayInputStream(testData));
        try {
            assertEquals(2, arStream.read(buffer, 0, 2));
            assertEquals(0x41, buffer[0]);
            assertEquals(0x42, buffer[1]);
        } catch (IOException e) {
            fail("IOException not expected");
        }
    }

    @Test
    public void testMatches() {
        byte[] signature = { 0x21, 0x3c, 0x61, 0x72, 0x63, 0x68, 0x3e, 0x0a };
        assertTrue(ArArchiveInputStream.matches(signature, signature.length));
    }

    @Test
    public void testClose() {
        byte[] testData = { 0x41, 0x42, 0x43 }; // ABC

        ArArchiveInputStream arStream = new ArArchiveInputStream(new ByteArrayInputStream(testData));
        try {
            arStream.close();
            arStream.read(); // Should throw exception after closing
            fail("IOException expected after closing stream");
        } catch (IOException e) {
            // Expected behavior
        }
    }
}