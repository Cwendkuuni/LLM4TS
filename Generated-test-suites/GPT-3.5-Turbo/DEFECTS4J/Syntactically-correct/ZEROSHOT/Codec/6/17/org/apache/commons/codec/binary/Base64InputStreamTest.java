import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class Base64InputStreamTest {

    private Base64InputStream base64InputStream;

    @Before
    public void setUp() {
        byte[] testBytes = new byte[]{1, 2, 3, 4, 5};
        base64InputStream = new Base64InputStream(testBytes);
    }

    @Test
    public void testRead() {
        try {
            int result = base64InputStream.read();
            assertEquals(1, result);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testReadWithInvalidOffset() {
        try {
            byte[] result = new byte[5];
            base64InputStream.read(result, -1, 5);
            fail("IndexOutOfBoundsException should be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Passed test
        } catch (Exception e) {
            fail("Wrong exception thrown");
        }
    }

    @Test
    public void testReadWithInvalidLen() {
        try {
            byte[] result = new byte[5];
            base64InputStream.read(result, 0, -1);
            fail("IndexOutOfBoundsException should be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Passed test
        } catch (Exception e) {
            fail("Wrong exception thrown");
        }
    }

    @Test
    public void testReadWithInvalidBufferSize() {
        try {
            byte[] result = new byte[5];
            base64InputStream.read(result, 0, 6);
            fail("IndexOutOfBoundsException should be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Passed test
        } catch (Exception e) {
            fail("Wrong exception thrown");
        }
    }

    @Test
    public void testReadWithZeroLen() {
        try {
            byte[] result = new byte[5];
            int bytesRead = base64InputStream.read(result, 0, 0);
            assertEquals(0, bytesRead);
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testReadWithNullByteArray() {
        try {
            base64InputStream.read(null, 0, 5);
            fail("NullPointerException should be thrown");
        } catch (NullPointerException e) {
            // Passed test
        } catch (Exception e) {
            fail("Wrong exception thrown");
        }
    }

    @Test
    public void testMarkSupported() {
        boolean result = base64InputStream.markSupported();
        assertFalse(result);
    }
}