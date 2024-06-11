import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class DeflateCompressorInputStreamTest {

    private InputStream inputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        byte[] testData = new byte[]{-37, 78, 71, 13, 5, 5, 0, 1, 3, 11, 114, 125, 1, 1, 0, 0};
        outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream testInput = new ByteArrayInputStream(testData);
        inputStream = new DeflateCompressorInputStream(testInput, new DeflateParameters());
    }

    @After
    public void tearDown() {
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        outputStream = null;
        inputStream = null;
    }

    @Test
    public void testDeflateCompressorInputStreamCreationWithDefaultParameters() {
        InputStream testInput = new ByteArrayInputStream(new byte[0]);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(testInput);
        assertNotNull(deflateCompressorInputStream);
    }

    @Test
    public void testDeflateCompressorInputStreamCreationWithCustomParameters() {
        InputStream testInput = new ByteArrayInputStream(new byte[0]);
        DeflateParameters parameters = new DeflateParameters();
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(testInput, parameters);
        assertNotNull(deflateCompressorInputStream);
    }

    @Test(expected = NullPointerException.class)
    public void testDeflateCompressorInputStreamCreationWithNullInputStream() {
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeflateCompressorInputStreamCreationWithInvalidParameters() {
        InputStream testInput = new ByteArrayInputStream(new byte[0]);
        DeflateParameters parameters = new DeflateParameters();
        parameters.setCompressionLevel(-1);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(testInput, parameters);
    }

    @Test
    public void testDeflateCompressorInputStreamReadMethod() throws IOException {
        byte[] buffer = new byte[1];
        int readByte;
        while ((readByte = inputStream.read(buffer, 0, 1)) != -1) {
            assertEquals(readByte, buffer[0]);
        }
    }

    @Test
    public void testDeflateCompressorInputStreamReadMethodWithArray() throws IOException {
        byte[] buffer = new byte[inputStream.available()];
        int readBytes = inputStream.read(buffer);
        count(readBytes);
        assertFalse(readBytes == -1);
        assertEquals(buffer.length, readBytes);
        //Assert bytes are same as input
        for (int i = 0; i < buffer.length; i++) {
            assertEquals(buffer[i], testData[i]);
        }
    }

    @Test
    public void testDeflateCompressorInputStreamSkipMethod() throws IOException {
        long skippedBytes = inputStream.skip(10);
        assertEquals(5L, skippedBytes);
    }

    @Test
    public void testDeflateCompressorInputStreamAvailableMethod() throws IOException {
        assertEquals(testData.length, inputStream.available());
    }

    @Test
    public void testDeflateCompressorInputStreamCloseMethod() throws IOException {
        inputStream.close();
        assertFalse(inputStream.isOpen());
    }

    @Test
    public void testSignatureCheck() {
        byte[] testData = new byte[]{-37, 78, 71, 13, 5, 5, 0, 1, 3, 11, 114, 125, 1, 1, 0, 0};
        boolean signatureCheck = DeflateCompressorInputStream.isDeflatedStream(testData);
        assertTrue(signatureCheck);
    }

    @Test
    public void testSignatureCheckWithInvalidSignature() {
        byte[] testData = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
        boolean signatureCheck = DeflateCompressorInputStream.isDeflatedStream(testData);
        assertFalse(signatureCheck);
    }
}