import org.junit.Test;
import static org.junit.Assert.*;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testReadOneByte() {
        // Create a checksum calculating input stream
        Checksum mockChecksum = new MockChecksum();
        byte[] inputData = {0x01};
        InputStream inputStream = new ByteArrayInputStream(inputData);
        ChecksumCalculatingInputStream testStream = new ChecksumCalculatingInputStream(mockChecksum, inputStream);

        // Read one byte from the stream
        int result;
        try {
            result = testStream.read();
        } catch (IOException e) {
            fail("Should not throw an exception");
        }

        // Assert the result and checksum value
        assertEquals(0x01, result);
        assertEquals(0x01, mockChecksum.getValue());
    }

    @Test
    public void testReadByteArray() {
        // Create a checksum calculating input stream
        Checksum mockChecksum = new MockChecksum();
        byte[] inputData = {0x01, 0x02, 0x03};
        InputStream inputStream = new ByteArrayInputStream(inputData);
        ChecksumCalculatingInputStream testStream = new ChecksumCalculatingInputStream(mockChecksum, inputStream);

        // Read a byte array from the stream
        byte[] buffer = new byte[2];
        int result;
        try {
            result = testStream.read(buffer);
        } catch (IOException e) {
            fail("Should not throw an exception");
        }

        // Assert the result, buffer contents, and checksum value
        assertEquals(2, result);
        assertArrayEquals(new byte[]{0x01, 0x02}, buffer);
        assertEquals(0x01 + 0x02, mockChecksum.getValue());
    }

    @Test
    public void testReadByteArrayWithOffsetAndLength() {
        // Create a checksum calculating input stream
        Checksum mockChecksum = new MockChecksum();
        byte[] inputData = {0x01, 0x02, 0x03};
        InputStream inputStream = new ByteArrayInputStream(inputData);
        ChecksumCalculatingInputStream testStream = new ChecksumCalculatingInputStream(mockChecksum, inputStream);

        // Read a byte array with offset and length from the stream
        byte[] buffer = new byte[3];
        int result;
        try {
            result = testStream.read(buffer, 1, 2);
        } catch (IOException e) {
            fail("Should not throw an exception");
        }

        // Assert the result, buffer contents, and checksum value
        assertEquals(2, result);
        assertArrayEquals(new byte[]{0x00, 0x01, 0x02}, buffer);
        assertEquals(0x01 + 0x02, mockChecksum.getValue());
    }

    @Test
    public void testSkip() {
        // Create a checksum calculating input stream
        Checksum mockChecksum = new MockChecksum();
        byte[] inputData = {0x01, 0x02, 0x03};
        InputStream inputStream = new ByteArrayInputStream(inputData);
        ChecksumCalculatingInputStream testStream = new ChecksumCalculatingInputStream(mockChecksum, inputStream);

        // Skip bytes from the stream
        long result;
        try {
            result = testStream.skip(2);
        } catch (IOException e) {
            fail("Should not throw an exception");
        }

        // Assert the result and checksum value
        assertEquals(2, result);
        assertEquals(0x01 + 0x02, mockChecksum.getValue());
    }

    private class MockChecksum implements Checksum {
        private long value = 0;

        @Override
        public void update(int b) {
            value += b;
        }

        @Override
        public void update(byte[] b, int off, int len) {
            for (int i = off; i < off + len; i++) {
                value += b[i];
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
    }
}