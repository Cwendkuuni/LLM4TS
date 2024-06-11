import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import org.apache.commons.compress.utils.Checksums;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

/**
 * Tests for the ChecksumCalculatingInputStream class.
 */
public class ChecksumCalculatingInputStreamTest {

    private InputStream testStream;
    private ChecksumCalculatingInputStream checksumStream;
    private static final byte[] TEST_DATA = new byte[]{'A', 'B', 'C', 'D', 'E'};
    private static final long EXPECTED_CHECKSUM = 12345L; // arbitrary value for testing

    @Before
    public void setUp() throws IOException {
        testStream = new ByteArrayInputStream(TEST_DATA);
        checksumStream = new ChecksumCalculatingInputStream(Checksums.adler32(), testStream);
    }

    @After
    public void tearDown() throws IOException {
        if (checksumStream != null) {
            checksumStream.close();
        }
    }

    /**
     * Tests reading a single byte from the stream.
     * @throws IOException if an error occurs while reading
     */
    @Test
    public void testReadByte() throws IOException {
        final int byteRead = checksumStream.read();
        Assert.assertEquals("Expected byte to be read", TEST_DATA[0], byteRead);
        Assert.assertEquals("Checksum didn't match expected value", EXPECTED_CHECKSUM, checksumStream.getValue());
    }

    /**
     * Tests reading a byte array from the stream.
     * @throws IOException if an error occurs while reading
     */
    @Test
    public void testReadByteArray() throws IOException {
        final byte[] byteArray = new byte[TEST_DATA.length];
        final int bytesRead = checksumStream.read(byteArray);
        Assert.assertEquals("Expected bytes to be read", TEST_DATA.length, bytesRead);
        Assert.assertArrayEquals("Expected byte array to match input", TEST_DATA, byteArray);
        Assert.assertEquals("Checksum didn't match expected value", EXPECTED_CHECKSUM, checksumStream.getValue());
    }

    /**
     * Tests reading a byte array from the stream with a partial read.
     * @throws IOException if an error occurs while reading
     */
    @Test
    public void testReadByteArrayPartial() throws IOException {
        final byte[] byteArray = new byte[TEST_DATA.length];
        final int bytesRead = checksumStream.read(byteArray, 0, TEST_DATA.length / 2);
        Assert.assertEquals("Expected bytes to be read", TEST_DATA.length / 2, bytesRead);
        Assert.assertArraySubarrayEquals("Expected partial byte array to match input", TEST_DATA, 0, bytesRead, byteArray, 0, bytesRead);
        Assert.assertEquals("Checksum didn't match expected value", EXPECTED_CHECKSUM, checksumStream.getValue());
    }

    /**
     * Tests the checksum computation with an input stream that doesn't match the expected checksum.
     * @throws IOException if an error occurs while reading
     */
    @Test(expected = IOException.class)
    public void testInputStreamMismatchedChecksum() throws IOException {
        final InputStream mismatchedStream = new ByteArrayInputStream(new byte[]{TEST_DATA[0], (byte) (TEST_DATA[0] + 1)});
        new ChecksumCalculatingInputStream(Checksums.adler32(), mismatchedStream);
    }

    /**
     * Tests the checksum computation with an input stream that doesn't close properly.
     * @throws IOException if an error occurs while reading
     */
    @Test(expected = IOException.class)
    public void testInputStreamDoesntClose() throws IOException {
        final InputStream failingStream = new FailingInputStream();
        new ChecksumCalculatingInputStream(Checksums.adler32(), failingStream);
    }

    /**
     * An InputStream that throws an IOException when read() is called.
     */
    private static class FailingInputStream extends InputStream {
        @Override
        public int read() throws IOException {
            throw new IOException("Test failure: InputStream doesn't close properly");
        }

        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            throw new IOException("Test failure: InputStream doesn't close properly");
        }

        @Override
        public long skip(long n) throws IOException {
            throw new IOException("Test failure: InputStream doesn't close properly");
        }

        @Override
        public void close() throws IOException {
            // Do nothing, as this class is intended to fail when read() is called
        }
    }

    /**
     * Tests the checksum computation with an input stream that doesn't support the given checksum algorithm.
     * @throws IOException if an error occurs while reading
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testInputStreamUnsupportedAlgorithm() throws IOException {
        final MessageDigest md = MessageDigest.getInstance("MD5");
        new ChecksumCalculatingInputStream(md, testStream);
    }
}