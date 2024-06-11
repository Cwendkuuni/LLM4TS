import org.junit.Test;
import static org.junit.Assert.*;

public class BaseNCodecInputStreamTest {

    @Test
    public void testReadEmptyInputStream() {
        // Create an empty input stream
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new EmptyInputStream(), new BaseNCodec(), true);

        // Perform read operation
        int result = inputStream.read();

        // Assert the result
        assertEquals(-1, result);
    }

    @Test
    public void testReadSingleByteInputStream() {
        // Create a single byte input stream
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new SingleByteInputStream(), new BaseNCodec(), true);

        // Perform read operation
        int result = inputStream.read();

        // Assert the result
        assertEquals(65, result);
    }

    @Test
    public void testReadInputStreamWithOffsetAndLen() {
        // Create an input stream with offset and len parameters
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new CustomInputStream(), new BaseNCodec(), true);
        byte[] buffer = new byte[10];
        int offset = 2;
        int len = 6;

        // Perform read operation
        int result = inputStream.read(buffer, offset, len);

        // Assert the result and the contents of the buffer
        assertEquals(4, result);
        assertArrayEquals(new byte[]{0, 0, 65, 66, 67, 68, 0, 0, 0, 0}, buffer);
    }

    @Test(expected = NullPointerException.class)
    public void testReadNullPointerException() throws IOException {
        // Create an input stream
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new CustomInputStream(), new BaseNCodec(), true);

        // Set byte array parameter to null
        byte[] buffer = null;

        // Perform read operation
        inputStream.read(buffer, 0, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadIndexOutOfBoundsException() throws IOException {
        // Create an input stream
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new CustomInputStream(), new BaseNCodec(), true);
        byte[] buffer = new byte[10];

        // Perform read operation with invalid offset
        inputStream.read(buffer, -1, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadIndexOutOfBoundsException2() throws IOException {
        // Create an input stream
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new CustomInputStream(), new BaseNCodec(), true);
        byte[] buffer = new byte[10];

        // Perform read operation with offset exceeding array size
        inputStream.read(buffer, 11, 2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadIndexOutOfBoundsException3() throws IOException {
        // Create an input stream
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new CustomInputStream(), new BaseNCodec(), true);
        byte[] buffer = new byte[10];

        // Perform read operation with len exceeding array size
        inputStream.read(buffer, 0, 11);
    }

    @Test(expected = IOException.class)
    public void testReadIOException() throws IOException {
        // Create an input stream
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new CustomInputStream(), new BaseNCodec(), true);

        // Perform read operation with I/O error
        inputStream.read();
    }

    @Test
    public void testMarkSupported() {
        // Create an input stream
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new CustomInputStream(), new BaseNCodec(), true);

        // Assert that mark is not supported
        assertFalse(inputStream.markSupported());
    }

    private static class CustomInputStream extends InputStream {
        @Override
        public int read() throws IOException {
            return 65;
        }

        @Override
        public int read(byte[] b, int offset, int len) throws IOException {
            b[offset] = 65;
            b[offset + 1] = 66;
            b[offset + 2] = 67;
            b[offset + 3] = 68;
            return 4;
        }
    }

    private static class EmptyInputStream extends InputStream {
        @Override
        public int read() throws IOException {
            return -1;
        }
    }

    private static class SingleByteInputStream extends InputStream {
        private boolean readFirst = false;

        @Override
        public int read() throws IOException {
            if (!readFirst) {
                readFirst = true;
                return 65;
            } else {
                return -1;
            }
        }
    }
}