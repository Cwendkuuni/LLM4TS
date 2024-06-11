import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    @Test
    public void testReadSuccess() throws IOException {
        String input = "Hello, World!";
        byte[] expected = new byte[]{72, 101, 108, 108, 111, 44, 32, 87, 111, 114, 108, 100, 33}; // ASCII values for "Hello, World!"
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] actual = new byte[expected.length];
        int bytesRead = base64InputStream.read(actual, 0, expected.length);

        assertEquals(expected.length, bytesRead);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testReadBoundaryCase() throws IOException {
        String input = ""; // empty string
        byte[] expected = new byte[0];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] actual = new byte[expected.length];
        int bytesRead = base64InputStream.read(actual, 0, expected.length);

        assertEquals(expected.length, bytesRead);
        assertArrayEquals(expected, actual);
    }

    @Test(expected = NullPointerException.class)
    public void testReadNullArray() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        base64InputStream.read(null, 0, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidOffset() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        base64InputStream.read(new byte[5], -1, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidLength() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        base64InputStream.read(new byte[5], 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidBufferSize() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        base64InputStream.read(new byte[5], 0, 10);
    }

    @Test
    public void testReadDecodeSuccessful() throws IOException {
        String input = "SGVsbG8sIFdvcmxkIQ=="; // Base64 encoded "Hello, World!"
        byte[] expected = new byte[]{72, 101, 108, 108, 111, 44, 32, 87, 111, 114, 108, 100, 33}; // ASCII values for "Hello, World!"
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        byte[] actual = new byte[expected.length];
        int bytesRead = base64InputStream.read(actual, 0, expected.length);

        assertEquals(expected.length, bytesRead);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testReadEncodeSuccessful() throws IOException {
        String input = "Hello, World!";
        String expected = "SGVsbG8sIFdvcmxkIQ=="; // Base64 encoded "Hello, World!"
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        byte[] actual = new byte[expected.length()];
        int bytesRead = base64InputStream.read(actual, 0, expected.length());

        assertEquals(expected.length(), bytesRead);
        assertArrayEquals(expected.getBytes(), actual);
    }

    @Test
    public void testReadEncodeLargeData() throws IOException {
        StringBuilder inputBuilder = new StringBuilder();
        StringBuilder expectedBuilder = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            inputBuilder.append("Hello, World!");
            expectedBuilder.append("SGVsbG8sIFdvcmxkIQ==");
        }
        String input = inputBuilder.toString();
        String expected = expectedBuilder.toString();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        byte[] actual = new byte[expected.length()];
        int bytesRead = base64InputStream.read(actual, 0, expected.length());

        assertEquals(expected.length(), bytesRead);
        assertArrayEquals(expected.getBytes(), actual);
    }

    @Test
    public void testReadDecodeLargeData() throws IOException {
        StringBuilder expectedBuilder = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            expectedBuilder.append("Hello, World!");
        }
        String expected = expectedBuilder.toString();
        String input = "SGVsbG8sIFdvcmxkIQ==".repeat(1000);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        byte[] actual = new byte[expected.length()];
        int bytesRead = base64InputStream.read(actual, 0, expected.length());

        assertEquals(expected.length(), bytesRead);
        assertArrayEquals(expected.getBytes(), actual);
    }

    @Test
    public void testReadMultipleCalls() throws IOException {
        String input = "Hello, World!";
        byte[] expected = new byte[]{72, 101, 108, 108, 111, 44, 32, 87, 111, 114, 108, 100, 33}; // ASCII values for "Hello, World!"
        ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        
        byte[] actual1 = new byte[10];
        int bytesRead1 = base64InputStream.read(actual1, 0, 10);
        
        byte[] actual2 = new byte[10];
        int bytesRead2 = base64InputStream.read(actual2, 0, 10);

        byte[] actual3 = new byte[10];
        int bytesRead3 = base64InputStream.read(actual3, 0, 10);

        byte[] mergedActual = new byte[bytesRead1 + bytesRead2 + bytesRead3];
        System.arraycopy(actual1, 0, mergedActual, 0, bytesRead1);
        System.arraycopy(actual2, 0, mergedActual, bytesRead1, bytesRead2);
        System.arraycopy(actual3, 0, mergedActual, bytesRead1 + bytesRead2, bytesRead3);

        assertEquals(expected.length, bytesRead1 + bytesRead2 + bytesRead3);
        assertArrayEquals(expected, mergedActual);
    }
}