import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Test class for Base64InputStream
 */
public class Base64InputStreamTest {

    private static final String TEST_DATA = "This is a test";

    /**
     * Test constructor with default decoding behavior
     */
    @Test
    public void testDefaultDecoder() throws IOException {
        byte[] inputData = TEST_DATA.getBytes();
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(inputData);

        Base64InputStream base64InputStream = new Base64InputStream(byteInputStream);

        byte[] outputData = new byte[TEST_DATA.length()];

        int len = base64InputStream.read(outputData, 0, outputData.length);
        Assert.assertEquals(TEST_DATA.length(), len);
        Assert.assertArrayEquals(TEST_DATA.getBytes(), outputData);
    }

    /**
     * Test constructor with encoding behavior
     */
    @Test
    public void testEncoder() throws IOException {
        Base64OutputStream base64OutputStream = new Base64OutputStream(new ByteArrayOutputStream());
        base64OutputStream.write(TEST_DATA.getBytes());
        base64OutputStream.finish();

        byte[] inputData = base64OutputStream.toByteArray();
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(inputData);

        Base64InputStream base64InputStream = new Base64InputStream(byteInputStream, true);

        byte[] outputData = new byte[TEST_DATA.length()];

        int len = base64InputStream.read(outputData, 0, outputData.length);
        Assert.assertEquals(TEST_DATA.length(), len);
        Assert.assertArrayEquals(TEST_DATA.getBytes(), outputData);
    }

    /**
     * Test edge case with negative length
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testNegativeLength() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA.getBytes()));
        int len = base64InputStream.read(new byte[TEST_DATA.length() - 1], 0, TEST_DATA.length());
    }

    /**
     * Test edge case with null destination array
     */
    @Test(expected = NullPointerException.class)
    public void testNullDestinationArray() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA.getBytes()));
        base64InputStream.read(null, 0, TEST_DATA.length());
    }

    /**
     * Test edge case with offset out of bound
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testOffsetOutOfBound() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA.getBytes()));
        byte[] outputData = new byte[TEST_DATA.length()];
        int offset = TEST_DATA.length();
        int len = base64InputStream.read(outputData, offset, TEST_DATA.length());
    }

    /**
     * Test edge case with destination array length less than expected data size
     */
    @Test
    public void testDestinationArrayLengthLessThanExpectedDataSize() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA.getBytes()));
        byte[] outputData = new byte[TEST_DATA.length() - 1];
        int len = base64InputStream.read(outputData, 0, outputData.length);

        Assert.assertFalse(len == TEST_DATA.length());
    }

    /**
     * Test reading past EOF
     */
    @Test
    public void testReadPastEOF() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(TEST_DATA.getBytes()));

        int len = base64InputStream.read(new byte[1], 0, 1);

        Assert.assertEquals(-1, len);
    }

    /**
     * Test reading from empty stream
     */
    @Test(expected = IOException.class)
    public void testReadFromEmptyStream() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0][]));

        base64InputStream.read(new byte[1], 0, 1);
    }

}