import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

/**
 * Test class for DeflateCompressorInputStream.
 */
public class DeflateCompressorInputStreamTest {

    private InputStream inputStream;
    private DeflateCompressorInputStream deflateInputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        byte[] inputData = "Test data".getBytes();
        this.inputStream = new ByteArrayInputStream(inputData);
        DeflateParameters parameters = new DeflateParameters();
        this.deflateInputStream = new DeflateCompressorInputStream(this.inputStream, parameters);
        this.outputStream = new ByteArrayOutputStream();
    }

    /**
     * Test reading a single byte from the input stream.
     *
     * @throws IOException if an error occurs during the test
     */
    @Test
    public void testRead() throws IOException {
        byte[] expected = new byte[]{'T', 'e', 's', 't', ' ', 'd', 'a', 't', 'a', '\r', '\n'};
        byte[] buffer = new byte[expected.length];
        int bytesRead = 0;

        while (bytesRead < expected.length) {
            int result = this.deflateInputStream.read();
            if (result != -1) {
                buffer[bytesRead++] = (byte) result;
            }
        }

        assertEquals(expected, buffer);
    }

    /**
     * Test reading multiple bytes from the input stream.
     *
     * @throws IOException if an error occurs during the test
     */
    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] expected = "Test data".getBytes();
        byte[] buffer = new byte[expected.length];

        int bytesRead = this.deflateInputStream.read(buffer);
        assertEquals(expected.length, bytesRead);
        assertEquals(expected, buffer);
    }

    /**
     * Test skipping bytes in the input stream.
     *
     * @throws IOException if an error occurs during the test
     */
    @Test
    public void testSkip() throws IOException {
        int length = 5;

        this.deflateInputStream.skip(length);

        byte[] buffer = new byte[5];
        int bytesRead = this.deflateInputStream.read(buffer);

        assertEquals(buffer.length, bytesRead);
    }

    /**
     * Test reading from the input stream when an error occurs.
     *
     * @throws IOException if an error occurs during the test
     */
    @Test
    public void testIOException() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        DeflateParameters parameters = new DeflateParameters();
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(inputStream, parameters);

        assertThrows(IOException.class, () -> deflateInputStream.read());
    }

    /**
     * Test closing the input stream.
     *
     * @throws IOException if an error occurs during the test
     */
    @Test
    public void testClose() throws IOException {
        this.deflateInputStream.close();

        assertThrows(IOException.class, () -> this.deflateInputStream.read());
    }

    /**
     * Test reading compressed data from a ByteArrayInputStream and writing it to an OutputStream.
     *
     * @throws IOException if an error occurs during the test
     */
    @Test
    public void testDecompression() throws IOException {
        byte[] inputData = "Test data".getBytes();
        this.inputStream = new ByteArrayInputStream(inputData);
        DeflateParameters parameters = new DeflateParameters();

        this.outputStream.write(IOUtils.toByteArray(this.deflateInputStream));
        byte[] expected = "Test data".getBytes();

        assertEquals(expected.length, this.outputStream.size());
        assertEquals(expected, IOUtils.toByteArray(this.outputStream));
    }
}