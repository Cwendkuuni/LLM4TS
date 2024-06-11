
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.codec.binary.Base64InputStream;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws Exception {
        // Test basic functionality
        byte[] data = {97, 98, 99}; // 'a', 'b', 'c'
        byte[] encoded = {77, 81, 61, 90, 87, 61, 61}; // 'T', 'U', '=', 'Z', 'W', '=='
        Base64InputStream stream = new Base64InputStream(new ByteArrayInputStream(encoded));
        byte[] result = new byte[3];
        assertEquals(3, stream.read(result));
        assertArrayEquals(data, result);
        assertEquals(-1, stream.read());
        stream.close();
        
        // Test reading 0 bytes
        stream = new Base64InputStream(new ByteArrayInputStream(encoded));
        assertEquals(0, stream.read(new byte[3], 0, 0));
        assertEquals(-1, stream.read());
        stream.close();
    }

    @Test(expected = NullPointerException.class)
    public void testReadNullDestination() throws Exception {
        Base64InputStream stream = new Base64InputStream(new ByteArrayInputStream(new byte[0]));
        stream.read(null, 0, 1);
        stream.close();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidOffset() throws Exception {
        Base64InputStream stream = new Base64InputStream(new ByteArrayInputStream(new byte[0]));
        stream.read(new byte[3], -1, 1);
        stream.close();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidLength() throws Exception {
        Base64InputStream stream = new Base64InputStream(new ByteArrayInputStream(new byte[0]));
        stream.read(new byte[3], 0, -1);
        stream.close();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidBufferSize() throws Exception {
        Base64InputStream stream = new Base64InputStream(new ByteArrayInputStream(new byte[0]));
        stream.read(new byte[5], 0, 3);
        stream.close();
    }

    @Test
    public void testMarkSupported() {
        Base64InputStream stream = new Base64InputStream(new ByteArrayInputStream(new byte[0]));
        assertFalse(stream.markSupported());
    }

}
