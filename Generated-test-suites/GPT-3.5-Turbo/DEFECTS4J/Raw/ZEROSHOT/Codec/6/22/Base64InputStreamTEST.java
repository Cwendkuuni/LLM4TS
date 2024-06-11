import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

    @Test
    public void testRead() {
        // Create a mock InputStream
        InputStream mockInputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                return 65; // Return the ASCII value of 'A'
            }
        };

        Base64InputStream base64InputStream = new Base64InputStream(mockInputStream);

        try {
            int result = base64InputStream.read();
            assertEquals(65, result);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testReadArray() {
        // Create a mock InputStream
        InputStream mockInputStream = new InputStream() {
            private int count = 0;

            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                b[off] = (byte) (65 + count); // Set the ASCII values of 'A', 'B', 'C',...
                count++;
                return 1;
            }
        };

        Base64InputStream base64InputStream = new Base64InputStream(mockInputStream);

        try {
            byte[] result = new byte[3];
            int bytesRead = base64InputStream.read(result, 0, 3);
            assertEquals(3, bytesRead);
            assertArrayEquals(new byte[] {65, 66, 67}, result);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testReadNullArray() {
        // Create a mock InputStream
        InputStream mockInputStream = new InputStream() {
            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                return 0;
            }
        };

        Base64InputStream base64InputStream = new Base64InputStream(mockInputStream);

        try {
            byte[] result = new byte[3];
            int bytesRead = base64InputStream.read(result, 0, 3);
            assertEquals(0, bytesRead);
            assertArrayEquals(new byte[3], result);
        } catch (IOException e) {
            fail("IOException occurred");
        }
    }

    @Test
    public void testMarkSupported() {
        // Create a mock InputStream
        InputStream mockInputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                return -1; // End of stream
            }
        };

        Base64InputStream base64InputStream = new Base64InputStream(mockInputStream);

        boolean result = base64InputStream.markSupported();
        assertFalse(result);
    }
}