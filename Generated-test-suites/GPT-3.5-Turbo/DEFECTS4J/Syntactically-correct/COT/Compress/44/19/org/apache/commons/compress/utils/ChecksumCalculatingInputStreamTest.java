
import org.junit.Test;
import static org.junit.Assert.*;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testRead() throws Exception {
        // Initialize test data
        byte[] inputBytes = {1, 2, 3};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream calcStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        // Test the read method
        int result = calcStream.read();

        // Check the result
        assertEquals(1, result);
    }

    @Test
    public void testReadArray() throws Exception {
        // Initialize test data
        byte[] inputBytes = {1, 2, 3};
        byte[] buffer = new byte[2];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream calcStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        // Test the readArray method
        int result = calcStream.read(buffer);

        // Check the result
        assertEquals(2, result);
        assertArrayEquals(new byte[]{1, 2}, buffer);
    }

    @Test
    public void testReadArrayWithOffsetAndLength() throws Exception {
        // Initialize test data
        byte[] inputBytes = {1, 2, 3};
        byte[] buffer = new byte[4];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream calcStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        // Test the readArray method
        int result = calcStream.read(buffer, 1, 2);

        // Check the result
        assertEquals(2, result);
        assertArrayEquals(new byte[]{0, 1, 2, 0}, buffer);
    }

    @Test
    public void testSkip() throws Exception {
        // Initialize test data
        byte[] inputBytes = {1, 2, 3};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream calcStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        // Test the skip method
        long result = calcStream.skip(2);

        // Check the result
        assertEquals(2, result);
    }

    @Test
    public void testGetValue() throws Exception {
        // Initialize test data
        byte[] inputBytes = {1, 2, 3};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        Checksum checksum = new CRC32();
        ChecksumCalculatingInputStream calcStream = new ChecksumCalculatingInputStream(checksum, inputStream);

        // Read the input stream to update checksum
        while (calcStream.read() >= 0);

        // Test the getValue method
        long result = calcStream.getValue();

        // Check the result
        assertEquals(3581100203L, result);
    }
}

