import static org.junit.Assert.*;
import org.apache.commons.compress.utils.Checksum;
import org.apache.commons.compress.utils.Checksums;
import org.junit.Test;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testGetValue() throws Exception {
        byte[] testData = "Test Data".getBytes();
        Checksum checksum = Checksums.md5();
        InputStream input = new ByteArrayInputStream(testData);
        ChecksumCalculatingInputStream testStream = new ChecksumCalculatingInputStream(checksum, input);

        int bytesRead = testStream.read(testData, 0, testData.length);
        long calculatedChecksum = testStream.getValue();

        byte[] expectedChecksum = checksum.getValue();
        assertEquals(expectedChecksum, calculatedChecksum);
    }

    @Test(expected = IOException.class)
    public void testReadPastEndOfStream() throws Exception {
        byte[] testData = "Test Data".getBytes();
        Checksum checksum = Checksums.md5();
        InputStream input = new ByteArrayInputStream(testData);
        ChecksumCalculatingInputStream testStream = new ChecksumCalculatingInputStream(checksum, input);

        testStream.read(); // Read the first byte
        testStream.close(); // Close the stream

        testStream.read(); // This should throw an IOException
    }

    @Test(expected = NullPointerException.class)
    public void testNullByteArray() {
        Checksum checksum = Checksums.md5();
        InputStream input = new ByteArrayInputStream("Test Data".getBytes());
        ChecksumCalculatingInputStream testStream = new ChecksumCalculatingInputStream(checksum, input);

        testStream.read(null); // This should throw a NullPointerException
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testNegativeOffset() throws Exception {
        byte[] testData = "Test Data".getBytes();
        Checksum checksum = Checksums.md5();
        InputStream input = new ByteArrayInputStream(testData);
        ChecksumCalculatingInputStream testStream = new ChecksumCalculatingInputStream(checksum, input);

        testStream.read(new byte[1], -1, 1); // This should throw an IndexOutOfBoundsException
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testNegativeLength() throws Exception {
        byte[] testData = "Test Data".getBytes();
        Checksum checksum = Checksums.md5();
        InputStream input = new ByteArrayInputStream(testData);
        ChecksumCalculatingInputStream testStream = new ChecksumCalculatingInputStream(checksum, input);

        testStream.read(new byte[1], 0, -1); // This should throw an IndexOutOfBoundsException
    }

    @Test
    public void testReadWithLargerLengthThanRemainingData() throws Exception {
        byte[] testData = "Test Data".getBytes();
        Checksum checksum = Checksums.md5();
        InputStream input = new ByteArrayInputStream(testData);
        ChecksumCalculatingInputStream testStream = new ChecksumCalculatingInputStream(checksum, input);

        int bytesRead = testStream.read(new byte[testData.length.IntValue() + 1], 0, testData.length.IntValue());

        // The remaining data in the input stream is exhausted, so the test should pass even if an exception is not thrown
    }
}