import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Base64InputStreamTest {

    @Test
    public void testRead() {
        byte[] data = new byte[] { 0x61, 0x62, 0x63 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        try {
            assertEquals(97, base64InputStream.read());
            assertEquals(98, base64InputStream.read());
            assertEquals(99, base64InputStream.read());
            assertEquals(-1, base64InputStream.read());
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testReadWithBuffer() {
        byte[] data = new byte[] { 0x61, 0x62, 0x63, 0x64, 0x65, 0x66, 0x67, 0x68, 0x69, 0x6A };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[5];
        try {
            int bytesRead = base64InputStream.read(buffer, 0, 5);
            assertEquals(5, bytesRead);
            assertArrayEquals(new byte[] { 97, 98, 99, 100, 101 }, buffer);

            bytesRead = base64InputStream.read(buffer, 0, 5);
            assertEquals(5, bytesRead);
            assertArrayEquals(new byte[] { 102, 103, 104, 105, 106 }, buffer);

            bytesRead = base64InputStream.read(buffer, 0, 5);
            assertEquals(0, bytesRead);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testReadWithInvalidOffsetAndLength() {
        byte[] data = new byte[] { 0x61, 0x62, 0x63 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[5];
        try {
            int bytesRead = base64InputStream.read(buffer, -1, 5);
            fail("IndexOutOfBoundsException should be thrown");

        } catch (IndexOutOfBoundsException e) {
            // Pass
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }

        try {
            int bytesRead = base64InputStream.read(buffer, 0, -1);
            fail("IndexOutOfBoundsException should be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Pass
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }

        try {
            int bytesRead = base64InputStream.read(buffer, 0, 10);
            fail("IndexOutOfBoundsException should be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Pass
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testReadWithNullBuffer() {
        byte[] data = new byte[] { 0x61, 0x62, 0x63 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        try {
            int bytesRead = base64InputStream.read(null, 0, 5);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
            // Pass
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testMarkSupported() {
        byte[] data = new byte[] { 0x61, 0x62, 0x63 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        assertFalse(base64InputStream.markSupported());
    }
}