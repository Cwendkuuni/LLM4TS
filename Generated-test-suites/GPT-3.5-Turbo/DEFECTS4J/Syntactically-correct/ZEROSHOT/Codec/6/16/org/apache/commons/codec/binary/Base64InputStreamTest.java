import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

    @Test
    public void testRead() {
        // Test case 1: Reading single byte from empty input stream
        Base64InputStream base64InputStream1 = new Base64InputStream(new InputStream() {
            public int read() throws IOException {
                return -1;
            }
        });
        assertEquals(-1, base64InputStream1.read());

        // Test case 2: Reading single byte from input stream with data
        Base64InputStream base64InputStream2 = new Base64InputStream(new InputStream() {
            private int count = 0;

            public int read() throws IOException {
                if (count < 5) {
                    count++;
                    return count;
                } else {
                    return -1;
                }
            }
        });
        assertEquals(1, base64InputStream2.read());
        assertEquals(2, base64InputStream2.read());
        assertEquals(3, base64InputStream2.read());
        assertEquals(4, base64InputStream2.read());
        assertEquals(5, base64InputStream2.read());
        assertEquals(-1, base64InputStream2.read());
    }

    @Test
    public void testReadByteOffsetLen() {
        // Test case 1: Reading zero bytes from empty input stream
        Base64InputStream base64InputStream1 = new Base64InputStream(new InputStream() {
            public int read(byte[] b, int offset, int len) throws IOException {
                return -1;
            }
        });
        byte[] buffer1 = new byte[10];
        assertEquals(0, base64InputStream1.read(buffer1, 0, 0));

        // Test case 2: Reading zero bytes from input stream with data
        Base64InputStream base64InputStream2 = new Base64InputStream(new InputStream() {
            private int count = 0;

            public int read(byte[] b, int offset, int len) throws IOException {
                if (count < 5 && offset + len >= 5) {
                    for (int i = offset; i < offset + len; i++) {
                        b[i] = (byte) (count + 1);
                        count++;
                    }
                    return 5;
                } else {
                    return -1;
                }
            }
        });
        byte[] buffer2 = new byte[10];
        assertEquals(0, base64InputStream2.read(buffer2, 0, 0));

        // Test case 3: Reading non-zero bytes from empty input stream
        Base64InputStream base64InputStream3 = new Base64InputStream(new InputStream() {
            public int read(byte[] b, int offset, int len) throws IOException {
                return -1;
            }
        });
        byte[] buffer3 = new byte[10];
        assertEquals(-1, base64InputStream3.read(buffer3, 0, 5));

        // Test case 4: Reading non-zero bytes from input stream with data
        Base64InputStream base64InputStream4 = new Base64InputStream(new InputStream() {
            private int count = 0;

            public int read(byte[] b, int offset, int len) throws IOException {
                if (count < 5 && offset + len >= 5) {
                    for (int i = offset; i < offset + len; i++) {
                        b[i] = (byte) (count + 1);
                        count++;
                    }
                    return 5;
                } else {
                    return -1;
                }
            }
        });
        byte[] buffer4 = new byte[10];
        assertEquals(5, base64InputStream4.read(buffer4, 0, 5));
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5, 0, 0, 0, 0, 0}, buffer4);

        // Test case 5: Reading more bytes that available in input stream
        Base64InputStream base64InputStream5 = new Base64InputStream(new InputStream() {
            private int count = 0;

            public int read(byte[] b, int offset, int len) throws IOException {
                if (count < 4 && offset + len >= 4) {
                    for (int i = offset; i < offset + len; i++) {
                        b[i] = (byte) (count + 1);
                        count++;
                    }
                    return 4;
                } else {
                    return -1;
                }
            }
        });
        byte[] buffer5 = new byte[10];
        assertEquals(4, base64InputStream5.read(buffer5, 0, 10));
        assertArrayEquals(new byte[]{1, 2, 3, 4, 0, 0, 0, 0, 0, 0}, buffer5);
    }

    @Test
    public void testMarkSupported() {
        Base64InputStream base64InputStream = new Base64InputStream(new InputStream() {
            public int read() throws IOException {
                return -1;
            }
        });
        assertFalse(base64InputStream.markSupported());
    }
}