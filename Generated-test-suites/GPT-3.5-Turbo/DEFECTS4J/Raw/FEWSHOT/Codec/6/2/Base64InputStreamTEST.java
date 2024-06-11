import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Base64InputStreamTest {

    private byte[] testData;
    private InputStream inputStream;

    @Before
    public void setUp() {
        // Create test data
        testData = "Test data".getBytes();
        // Create Base64 encoded test data
        byte[] encodedData = Base64.encodeBase64(testData);
        // Create input stream using Base64 encoded test data
        inputStream = new ByteArrayInputStream(encodedData);
    }

    @After
    public void tearDown() throws IOException {
        // Close the input stream
        inputStream.close();
    }

    @Test
    public void testConstructor() {
        // Test constructor with InputStream parameter
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        // Assert that doEncode field is false
        assertFalse(base64InputStream.doEncode);

        // Test constructor with InputStream and doEncode parameters
        base64InputStream = new Base64InputStream(inputStream, true);
        // Assert that doEncode field is true
        assertTrue(base64InputStream.doEncode);

        // Test constructor with InputStream, doEncode, lineLength, and lineSeparator parameters
        byte[] lineSeparator = "\n".getBytes();
        base64InputStream = new Base64InputStream(inputStream, true, 76, lineSeparator);
        // Assert that doEncode field is true
        assertTrue(base64InputStream.doEncode);
        // Assert that lineLength field is 76
        assertEquals(76, base64InputStream.base64.getLineLength());
        // Assert that lineSeparator field is equal to lineSeparator parameter
        assertArrayEquals(lineSeparator, base64InputStream.base64.getLineSeparator());
    }

    @Test
    public void testRead() throws IOException {
        // Create Base64 encoded test data
        byte[] encodedData = Base64.encodeBase64(testData);
        // Create input stream using Base64 encoded test data
        InputStream inputStream = new ByteArrayInputStream(encodedData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // Read first byte
        int result = base64InputStream.read();
        // Assert that result is equal to the first byte of decoded test data
        assertEquals(testData[0], (byte) result);

        // Read remaining bytes
        byte[] decodedData = new byte[testData.length - 1];
        int bytesRead = base64InputStream.read(decodedData);
        // Assert that bytesRead is equal to the number of remaining bytes
        assertEquals(testData.length - 1, bytesRead);
        // Assert that decodedData is equal to the remaining bytes of decoded test data
        assertArrayEquals(testData, 1, decodedData, 0, bytesRead);
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        // Create Base64 encoded test data
        byte[] encodedData = Base64.encodeBase64(testData);
        // Create input stream using Base64 encoded test data
        InputStream inputStream = new ByteArrayInputStream(encodedData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // Read first byte
        int result = base64InputStream.read();
        // Assert that result is equal to the first byte of decoded test data
        assertEquals(testData[0], (byte) result);

        // Read remaining bytes with offset and length
        byte[] decodedData = new byte[testData.length];
        int bytesRead = base64InputStream.read(decodedData, 1, testData.length - 1);
        // Assert that bytesRead is equal to the number of remaining bytes
        assertEquals(testData.length - 1, bytesRead);
        // Assert that the first byte of decodedData is 0
        assertEquals((byte) 0, decodedData[0]);
        // Assert that the remaining bytes of decodedData are equal to the remaining bytes of decoded test data
        assertArrayEquals(testData, 1, decodedData, 1, bytesRead);
    }

    @Test(expected=NullPointerException.class)
    public void testReadWithNullBuffer() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        base64InputStream.read(null, 0, 10);
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[10];
        base64InputStream.read(buffer, -1, 10);
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void testReadWithInvalidLength() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[10];
        base64InputStream.read(buffer, 0, -1);
    }

    @Test(expected=IndexOutOfBoundsException.class)
    public void testReadWithInvalidBufferSize() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[10];
        base64InputStream.read(buffer, 0, 20);
    }

    @Test
    public void testReadWithEmptyBuffer() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[0];
        int bytesRead = base64InputStream.read(buffer, 0, 0);
        // Assert that bytesRead is 0
        assertEquals(0, bytesRead);
    }

    @Test
    public void testMarkSupported() {
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        boolean markSupported = base64InputStream.markSupported();
        // Assert that markSupported is false
        assertFalse(markSupported);
    }

    @Test
    public void testClose() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        base64InputStream.close();
        assertTrue(base64InputStream.base64.hasData());
    }
}