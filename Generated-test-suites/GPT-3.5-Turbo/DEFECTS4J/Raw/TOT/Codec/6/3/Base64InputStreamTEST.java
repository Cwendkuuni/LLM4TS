import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.codec.binary.Base64;

public class Base64InputStreamTest {

    // test read method
    @Test
    public void testRead() throws IOException {
        String input = "Hello World";
        byte[] encodedBytes = Base64.encodeBase64(input.getBytes());
        ByteArrayInputStream bais = new ByteArrayInputStream(encodedBytes);
        Base64InputStream b64is = new Base64InputStream(bais);
      
        assertEquals((int) 'H', b64is.read());
        assertEquals((int) 'e', b64is.read());
        int len = 0;
        byte[] buffer = new byte[20];
        while ((len = b64is.read(buffer)) != -1) {
            for (int i = 0; i < len; i++) {
                assertTrue(isBase64(buffer[i]));
            }
        }
        b64is.close();
    }

    // Helper method to check if a given byte is a valid Base64 character
    private boolean isBase64(byte b) {
        return (b >= 0 && b <= 127);
    }

    // test read method with zero length byte array
    @Test
    public void testReadWithZeroLengthArray() throws IOException {
        byte[] input = new byte[0];
        ByteArrayInputStream bais = new ByteArrayInputStream(input);
        Base64InputStream b64is = new Base64InputStream(bais);

        assertEquals(-1, b64is.read());
        assertEquals(-1, b64is.read(new byte[0]));

        b64is.close();
    }

    // test read method with null byte array
    @Test(expected = NullPointerException.class)
    public void testReadWithNullArray() throws IOException {
        byte[] input = new byte[]{};
        ByteArrayInputStream bais = new ByteArrayInputStream(input);
        Base64InputStream b64is = new Base64InputStream(bais);

        b64is.read(null);

        b64is.close();
    }

    // test read method with negative offset
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeOffset() throws IOException {
        byte[] input = new byte[]{0, 1, 2, 3};
        ByteArrayInputStream bais = new ByteArrayInputStream(input);
        Base64InputStream b64is = new Base64InputStream(bais);

        b64is.read(input, -1, 2);

        b64is.close();
    }

    // test read method with negative length
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeLength() throws IOException {
        byte[] input = new byte[]{0, 1, 2, 3};
        ByteArrayInputStream bais = new ByteArrayInputStream(input);
        Base64InputStream b64is = new Base64InputStream(bais);

        b64is.read(input, 1, -2);

        b64is.close();
    }

    // test read method with invalid offset and length combination
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffsetAndLength() throws IOException {
        byte[] input = new byte[]{0, 1, 2, 3};
        ByteArrayInputStream bais = new ByteArrayInputStream(input);
        Base64InputStream b64is = new Base64InputStream(bais);

        b64is.read(input, 2, 3);

        b64is.close();
    }

    // test read method with EOFException
    @Test(expected = EOFException.class)
    public void testReadWithEOFException() throws IOException {
        byte[] input = new byte[]{0, 1, 2, 3};
        ByteArrayInputStream bais = new ByteArrayInputStream(input);
        Base64InputStream b64is = new Base64InputStream(bais);

        b64is.read(new byte[5]);

        b64is.close();
    }

    // test markSupported method
    @Test
    public void testMarkSupported() {
        byte[] input = new byte[]{0, 1, 2, 3};
        ByteArrayInputStream bais = new ByteArrayInputStream(input);
        Base64InputStream b64is = new Base64InputStream(bais);

        assertFalse(b64is.markSupported());

        b64is.close();
    }
}