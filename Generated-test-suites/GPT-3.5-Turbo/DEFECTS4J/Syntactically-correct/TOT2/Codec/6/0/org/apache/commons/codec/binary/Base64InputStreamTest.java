import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    @Test
    public void testReadSingleByte() throws IOException {
        byte[] testData = "Hello World!".getBytes();
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(bais);

        int result = base64InputStream.read();
        
        assertEquals('H', result);
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] testData = "Hello World!".getBytes();
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(bais);

        byte[] buffer = new byte[5];
        int result = base64InputStream.read(buffer, 0, 5);

        assertArrayEquals("Hello".getBytes(), buffer);
        assertEquals(5, result);
    }

    @Test(expected = NullPointerException.class)
    public void testNullByteArray() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(bais);
        
        base64InputStream.read(null, 0, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testInvalidOffset() throws IOException {
        byte[] testData = "Hello World!".getBytes();
        ByteArrayInputStream bais = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(bais);

        byte[] buffer = new byte[5];
        base64InputStream.read(buffer, -1, 5);
    }

    @Test
    public void testEmptyStream() throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(bais);

        assertEquals(-1, base64InputStream.read());
    }
}