import org.junit.Test;

import static org.junit.Assert.*;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testRead() {
        // Test case 1: Read a single byte from the stream
        // Arrange
        InputStream in = new InputStream() {
            @Override
            public int read() throws IOException {
                return 42; // arbitrary byte value
            }
        };
        Checksum checksum = new Checksum() {
            long value = 0;

            @Override
            public void update(int b) {
                value += b;
            }

            @Override
            public void update(byte[] b, int off, int len) {
                for (int i = off; i < off + len; i++) {
                    update(b[i]);
                }
            }

            @Override
            public long getValue() {
                return value;
            }

            @Override
            public void reset() {
                value = 0;
            }
        };
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, in);

        // Act
        int result = 0;
        try {
            result = inputStream.read();
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }

        // Assert
        assertEquals(42, result);
        assertEquals(42, inputStream.getValue());
    }

    @Test
    public void testReadByteArray() {
        // Test case 1: Read a byte array from the stream
        // Arrange
        byte[] expected = {1, 2, 3};
        InputStream in = new InputStream() {
            int count = 0;

            @Override
            public int read() throws IOException {
                return count < expected.length ? expected[count++] : -1;
            }
        };
        Checksum checksum = new Checksum() {
            long value = 0;

            @Override
            public void update(int b) {
                value += b;
            }

            @Override
            public void update(byte[] b, int off, int len) {
                for (int i = off; i < off + len; i++) {
                    update(b[i]);
                }
            }

            @Override
            public long getValue() {
                return value;
            }

            @Override
            public void reset() {
                value = 0;
            }
        };
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, in);

        // Act
        byte[] result = new byte[3];
        try {
            inputStream.read(result);
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }

        // Assert
        assertArrayEquals(expected, result);
        assertEquals(6, inputStream.getValue());
    }

    @Test
    public void testReadByteArrayOffsetLength() {
        // Test case 1: Read a byte array from the stream with offset and length
        // Arrange
        byte[] expected = {1, 2, 3, 4, 5};
        InputStream in = new InputStream() {
            int count = 0;

            @Override
            public int read() throws IOException {
                return count < expected.length ? expected[count++] : -1;
            }
        };
        Checksum checksum = new Checksum() {
            long value = 0;

            @Override
            public void update(int b) {
                value += b;
            }

            @Override
            public void update(byte[] b, int off, int len) {
                for (int i = off; i < off + len; i++) {
                    update(b[i]);
                }
            }

            @Override
            public long getValue() {
                return value;
            }

            @Override
            public void reset() {
                value = 0;
            }
        };
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, in);

        // Act
        byte[] result = new byte[3];
        try {
            inputStream.read(result, 1, 2);
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }

        // Assert
        assertArrayEquals(new byte[]{0, 1, 2}, result);
        assertEquals(3, inputStream.getValue());
    }

    @Test
    public void testSkip() {
        // Test case 1: Skip bytes from the stream
        // Arrange
        byte[] expected = {1, 2, 3};
        InputStream in = new InputStream() {
            int count = 0;

            @Override
            public int read() throws IOException {
                return count < expected.length ? expected[count++] : -1;
            }
        };
        Checksum checksum = new Checksum() {
            long value = 0;

            @Override
            public void update(int b) {
                value += b;
            }

            @Override
            public void update(byte[] b, int off, int len) {
                for (int i = off; i < off + len; i++) {
                    update(b[i]);
                }
            }

            @Override
            public long getValue() {
                return value;
            }

            @Override
            public void reset() {
                value = 0;
            }
        };
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(checksum, in);

        // Act
        long result = 0;
        try {
            result = inputStream.skip(2);
        } catch (IOException e) {
            fail("Exception occurred: " + e.getMessage());
        }

        // Assert
        assertEquals(2, result);
        assertEquals(6, inputStream.getValue());
    }

}