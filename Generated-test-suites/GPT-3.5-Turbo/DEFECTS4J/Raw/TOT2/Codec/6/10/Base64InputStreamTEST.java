import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    @Test
    public void testConstructor1() {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream("test".getBytes());
            Base64InputStream base64InputStream = new Base64InputStream(inputStream);
            assertNotNull(base64InputStream);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testConstructor2() {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream("test".getBytes());
            Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
            assertNotNull(base64InputStream);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testConstructor3() {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream("test".getBytes());
            Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, 76, new byte[]{'\r', '\n'});
            assertNotNull(base64InputStream);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testRead() {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream("test".getBytes());
            Base64InputStream base64InputStream = new Base64InputStream(inputStream);
            int data = base64InputStream.read();
            assertEquals('t', data); // First character from "test"
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testReadByteArray() {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream("test".getBytes());
            Base64InputStream base64InputStream = new Base64InputStream(inputStream);
            byte[] buffer = new byte[10];
            int bytesRead = base64InputStream.read(buffer, 0, 2);
            assertEquals('t', buffer[0]);
            assertEquals('e', buffer[1]);
            assertEquals(2, bytesRead);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayError() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[10];
        base64InputStream.read(buffer, 5, 6); // Should throw IndexOutOfBoundsException
    }

    @Test
    public void testMarkSupported() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        assertFalse(base64InputStream.markSupported());
    }
}