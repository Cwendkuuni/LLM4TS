import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.DataFormatException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for DeflateCompressorInputStream.
 */
public class DeflateCompressorInputStreamTest {

    private InputStream inputStream;
    private DeflateCompressorInputStream deflateInputStream;
    private ByteArrayOutputStream outputStream;
    private ByteArrayInputStream testData;

    @Before
    public void setUp() throws IOException {
        byte[] testDataBytes = "Lorem ipsum dolor sit amet, consectetur adipiscing elit".getBytes();
        outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream outputStreamForTestData = new ByteArrayOutputStream();
        IOUtils.write(testDataBytes, outputStreamForTestData);
        testData = new ByteArrayInputStream(outputStreamForTestData.toByteArray());
        inputStream = new DeflateCompressorInputStream(testData, new DeflateParameters());
        deflateInputStream = new DeflateCompressorInputStream(inputStream);
    }

    @After
    public void tearDown() throws IOException {
        if (outputStream != null) {
            outputStream.close();
        }
        if (deflateInputStream != null) {
            deflateInputStream.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
        if (testData != null) {
            testData.close();
        }
    }

    /**
     * Tests if reading a single byte works correctly.
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testReadSingleByte() throws IOException {
        int ret = deflateInputStream.read();
        assertEquals((int) testData.read(), ret);
    }

    /**
     * Tests if reading multiple bytes works correctly.
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testReadMultipleBytes() throws IOException {
        byte[] buffer = new byte[10];
        int len = deflateInputStream.read(buffer, 0, buffer.length);
        assertEquals(testData.available(), len);
        assertEquals(testData.read(buffer), len);
    }

    /**
     * Tests if skipping works correctly.
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testSkip() throws IOException {
        long skipped = deflateInputStream.skip(5);
        assertEquals(testData.skip(5), skipped);
    }

    /**
     * Tests if available works correctly.
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testAvailable() throws IOException {
        assertEquals(testData.available(), deflateInputStream.available());
    }

    /**
     * Tests if closing works correctly.
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testClose() throws IOException {
        deflateInputStream.close();
        assertNull(deflateInputStream.getInputStream());
        assertFalse(deflateInputStream.isReadable());
    }

    /**
     * Tests if reading with incorrect parameters works correctly.
     * @throws IOException if an I/O error occurs.
     * @throws DataFormatException if the input data is not in the expected format.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidParameter() throws IOException, DataFormatException {
        new DeflateCompressorInputStream(null, new DeflateParameters());
    }

    /**
     * Tests if reading from an empty stream works correctly.
     * @throws IOException if an I/O error occurs.
     */
    @Test
    public void testEmptyStream() throws IOException {
        inputStream = new ByteArrayInputStream(new byte[0]);
        deflateInputStream = new DeflateCompressorInputStream(inputStream);
        assertEquals(-1, deflateInputStream.read());
        deflateInputStream.close();
    }
}