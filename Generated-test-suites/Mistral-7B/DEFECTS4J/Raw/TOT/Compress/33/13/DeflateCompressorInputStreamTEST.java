import org.apache.commons.compress.compressors.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.DeflateParameters;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.bytearrayoutputstream.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Tests for DeflateCompressorInputStream class
 */
public class DeflateCompressorInputStreamTest {

    private InputStream inputStream;
    private DeflateCompressorInputStream testStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        byte[] data = "This is a test string".getBytes();
        outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStreamFromData = new ByteArrayInputStream(data);
        DeflateParameters deflateParameters = new DeflateParameters();

        this.inputStream = new DeflateCompressorInputStream(inputStreamFromData, deflateParameters);
        this.testStream = new DeflateCompressorInputStream(this.inputStream);
    }

    @After
    public void tearDown() throws IOException {
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(outputStream);
        IOUtils.closeQuietly(testStream);
    }

    /**
     * Test reading data from the DeflateCompressorInputStream
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testReadData() throws IOException {
        int dataRead;
        byte[] buffer = new byte[10];

        do {
            dataRead = testStream.read(buffer);
            if (dataRead > 0) {
                System.out.write(buffer, 0, dataRead);
            }
        } while (dataRead > -1);

        Assert.assertEquals("Data not read correctly", "This is a test string".getBytes(), outputStream.toByteArray());
    }

    /**
     * Test reading a single byte from the DeflateCompressorInputStream
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testReadSingleByte() throws IOException {
        byte[] buffer = new byte[1];
        int dataRead = testStream.read(buffer);
        Assert.assertNotEquals(-1, dataRead);
        Assert.assertEquals("Single byte not read correctly", buffer[0], (byte) 'T');
    }

    /**
     * Test reading an empty stream from the DeflateCompressorInputStream
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testReadEmptyStream() throws IOException {
        ByteArrayOutputStream emptyOutput = new ByteArrayOutputStream();
        ByteArrayInputStream emptyInput = new ByteArrayInputStream(new byte[0]);
        DeflateCompressorInputStream emptyTestStream = new DeflateCompressorInputStream(emptyInput);

        int dataRead;
        byte[] buffer = new byte[1];

        do {
            dataRead = emptyTestStream.read(buffer);
            if (dataRead > 0) {
                emptyOutput.write(buffer, 0, dataRead);
            }
        } while (dataRead > -1);

        Assert.assertEquals("Stream should be empty", new byte[0], emptyOutput.toByteArray());
    }

    /**
     * Test reading negative length from the DeflateCompressorInputStream
     *
     * @throws IOException if an I/O error occurs
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReadNegativeLength() throws IOException {
        testStream.read(-1);
    }

    /**
     * Test skipping bytes from the DeflateCompressorInputStream
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testSkipBytes() throws IOException {
        long bytesToSkip = 2;
        long bytesSkipped = testStream.skip(bytesToSkip);
        Assert.assertEquals("Bytes not skipped correctly", bytesToSkip, bytesSkipped);
    }

    /**
     * Test skipping more bytes than available from the DeflateCompressorInputStream
     *
     * @throws IOException if an I/O error occurs
     */
    @Test(expected = IOException.class)
    public void testSkipMoreBytesThanAvailable() throws IOException {
        byte[] data = "This is a test string".getBytes();
        ByteArrayInputStream inputStreamFromData = new ByteArrayInputStream(data);
        DeflateCompressorInputStream testStream = new DeflateCompressorInputStream(inputStreamFromData, new DeflateParameters());

        long bytesToSkip = data.length + 1;
        testStream.skip(bytesToSkip);
    }

    /**
     * Test available bytes from the DeflateCompressorInputStream
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testAvailableBytes() throws IOException {
        byte[] data = "This is a test string".getBytes();
        ByteArrayInputStream inputStreamFromData = new ByteArrayInputStream(data);
        DeflateCompressorInputStream testStream = new DeflateCompressorInputStream(inputStreamFromData, new DeflateParameters());

        int availableBytes = testStream.available();
        Assert.assertEquals("Bytes not available correctly", data.length, availableBytes);
    }

    /**
     * Test closing the DeflateCompressorInputStream
     *
     * @throws IOException if an I/O error occurs
     */
    @Test
    public void testCloseStream() throws IOException {
        testStream.close();
        Assert.assertFalse("Stream should be closed", testStream.isOpen());
    }
}