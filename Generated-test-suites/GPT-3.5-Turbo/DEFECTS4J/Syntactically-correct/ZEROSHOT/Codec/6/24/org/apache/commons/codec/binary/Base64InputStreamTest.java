import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

    @Test
    public void testRead() {
        InputStream in = new ByteArrayInputStream(new byte[]{65, 66, 67});
        Base64InputStream base64InputStream = new Base64InputStream(in);
        try {
            assertEquals(65, base64InputStream.read());
            assertEquals(66, base64InputStream.read());
            assertEquals(67, base64InputStream.read());
            assertEquals(-1, base64InputStream.read());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testReadWithOffsetAndLength() {
        InputStream in = new ByteArrayInputStream(new byte[]{65, 66, 67});
        byte[] buffer = new byte[3];
        Base64InputStream base64InputStream = new Base64InputStream(in);
        try {
            int bytesRead = base64InputStream.read(buffer, 1, 2);
            assertEquals(2, bytesRead);
            assertEquals(0, buffer[0]);
            assertEquals(65, buffer[1]);
            assertEquals(66, buffer[2]);
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testReadWithOffsetAndLength_InvalidOffset() {
        InputStream in = new ByteArrayInputStream(new byte[]{65, 66, 67});
        byte[] buffer = new byte[3];
        Base64InputStream base64InputStream = new Base64InputStream(in);
        try {
            base64InputStream.read(buffer, -1, 2);
            fail("Should have thrown IndexOutOfBoundsException");
        } catch (IOException e) {
            assertFalse(e instanceof IndexOutOfBoundsException);
        }
    }

    @Test
    public void testReadWithOffsetAndLength_InvalidLength() {
        InputStream in = new ByteArrayInputStream(new byte[]{65, 66, 67});
        byte[] buffer = new byte[3];
        Base64InputStream base64InputStream = new Base64InputStream(in);
        try {
            base64InputStream.read(buffer, 1, -2);
            fail("Should have thrown IndexOutOfBoundsException");
        } catch (IOException e) {
            assertFalse(e instanceof IndexOutOfBoundsException);
        }
    }

    @Test
    public void testReadWithOffsetAndLength_InvalidBuffer() {
        InputStream in = new ByteArrayInputStream(new byte[]{65, 66, 67});
        byte[] buffer = null;
        Base64InputStream base64InputStream = new Base64InputStream(in);
        try {
            base64InputStream.read(buffer, 1, 2);
            fail("Should have thrown NullPointerException");
        } catch (IOException e) {
            assertFalse(e instanceof NullPointerException);
        }
    }

    @Test
    public void testMarkSupported() {
        InputStream in = new ByteArrayInputStream(new byte[]{65, 66, 67});
        Base64InputStream base64InputStream = new Base64InputStream(in);
        assertFalse(base64InputStream.markSupported());
    }
}