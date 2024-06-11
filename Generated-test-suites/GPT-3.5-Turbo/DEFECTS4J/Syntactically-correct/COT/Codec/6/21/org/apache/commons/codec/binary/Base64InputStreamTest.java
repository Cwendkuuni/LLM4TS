
import static org.junit.Assert.*;
import org.junit.Test;

public class Base64InputStreamTest {

    @Test
    public void testRead() {
        try {
            Base64InputStream stream = new Base64InputStream(null);
            assertEquals(-1, stream.read());
            stream.close();
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testReadByteArrayIntInt() {
        try {
            Base64InputStream stream = new Base64InputStream(null);
            byte[] buffer = new byte[10];
            assertEquals(0, stream.read(buffer, 0, 10));
            stream.close();
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    public void testMarkSupported() {
        Base64InputStream stream = new Base64InputStream(null);
        assertFalse(stream.markSupported());
    }
}
