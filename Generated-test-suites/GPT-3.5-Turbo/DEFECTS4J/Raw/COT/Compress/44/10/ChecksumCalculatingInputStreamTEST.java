import org.junit.Test;
import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.Checksum;

import static org.junit.Assert.assertEquals;

public class ChecksumCalculatingInputStreamTest {

    // Test read() method

    @Test
    public void testRead() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(mockChecksum(), in);

        int result = inputStream.read();

        assertEquals(data[0], (byte) result);
        assertEquals(1L, inputStream.getValue());
    }

    // Test read(byte[] b) method

    @Test
    public void testReadByteArray() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(mockChecksum(), in);

        byte[] buffer = new byte[3];
        int result = inputStream.read(buffer);

        assertEquals(3, result);
        assertEquals(data[0], buffer[0]);
        assertEquals(data[1], buffer[1]);
        assertEquals(data[2], buffer[2]);
        assertEquals(6L, inputStream.getValue());
    }

    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(mockChecksum(), in);

        byte[] buffer = new byte[5];
        int result = inputStream.read(buffer, 1, 3);

        assertEquals(3, result);
        assertEquals(0, buffer[0]);
        assertEquals(data[0], buffer[1]);
        assertEquals(data[1], buffer[2]);
        assertEquals(data[2], buffer[3]);
        assertEquals(6L, inputStream.getValue());
    }

    // Test skip() method

    @Test
    public void testSkip() throws IOException {
        byte[] data = {1, 2, 3, 4, 5};
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ChecksumCalculatingInputStream inputStream = new ChecksumCalculatingInputStream(mockChecksum(), in);

        long result = inputStream.skip(3);

        assertEquals(3L, result);
        assertEquals(6L, inputStream.getValue());
    }

    // Helper method

    private Checksum mockChecksum() {
        return new Checksum() {
            @Override
            public void update(int b) {
                // Do nothing
            }

            @Override
            public void update(byte[] b, int off, int len) {
                // Do nothing
            }

            @Override
            public long getValue() {
                return 0L;
            }

            @Override
            public void reset() {
                // Do nothing
            }
        };
    }

}

