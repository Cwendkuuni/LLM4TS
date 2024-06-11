
import static org.junit.Assert.*;

import org.junit.Test;

public class Base64InputStreamTest {

    @Test
    public void testRead() {
        // Test case where the input stream returns EOF (-1)
        Base64InputStream inputStream = new Base64InputStream(new InputStream() {
            @Override
            public int read() throws IOException {
                return -1;
            }
        });
        assertEquals(-1, inputStream.read());

        // Test case where the input stream returns a single byte
        inputStream = new Base64InputStream(new InputStream() {
            @Override
            public int read() throws IOException {
                return 'A';
            }
        });
        assertEquals(65, inputStream.read());

        // Test case where the input stream returns multiple bytes
        inputStream = new Base64InputStream(new InputStream() {
            private int count = 0;

            @Override
            public int read() throws IOException {
                if (count < 3) {
                    count++;
                    return 'A';
                } else {
                    return -1;
                }
            }
        });
        assertEquals(65, inputStream.read());
        assertEquals(65, inputStream.read());
        assertEquals(65, inputStream.read());
        assertEquals(-1, inputStream.read());
    }

    @Test
    public void testReadByteArrayIntInt() {
        // Test case where input array, offset, and length are all 0
        Base64InputStream inputStream = new Base64InputStream(new InputStream() {
            @Override
            public int read() throws IOException {
                return 'A';
            }
        });
        byte[] buffer = new byte[0];
        assertEquals(0, inputStream.read(buffer, 0, 0));

        // Test case where length is 0
        inputStream = new Base64InputStream(new InputStream() {
            @Override
            public int read() throws IOException {
                return 'A';
            }
        });
        buffer = new byte[1];
        assertEquals(0, inputStream.read(buffer, 0, 0));

        // Test case where input stream returns fewer bytes than length
        inputStream = new Base64InputStream(new InputStream() {
            private int count = 0;

            @Override
            public int read() throws IOException {
                if (count < 2) {
                    count++;
                    return 'A';
                } else {
                    return -1;
                }
            }
        });
        buffer = new byte[4];
        assertEquals(2, inputStream.read(buffer, 0, 4));
        assertEquals(65, buffer[0]);
        assertEquals(65, buffer[1]);
        assertEquals(0, buffer[2]);
        assertEquals(0, buffer[3]);

        // Test case where input stream returns more bytes than length
        inputStream = new Base64InputStream(new InputStream() {
            private int count = 0;

            @Override
            public int read() throws IOException {
                if (count < 4) {
                    count++;
                    return 'A';
                } else {
                    return -1;
                }
            }
        });
        buffer = new byte[2];
        assertEquals(2, inputStream.read(buffer, 0, 2));
        assertEquals(65, buffer[0]);
        assertEquals(65, buffer[1]);
    }

    @Test
    public void testMarkSupported() {
        // Test case where mark is not supported
        Base64InputStream inputStream = new Base64InputStream(new InputStream() {
            @Override
            public int read() throws IOException {
                return -1;
            }
        });
        assertFalse(inputStream.markSupported());
    }

}
