import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class DeflateCompressorInputStreamTest {

    private ByteArrayOutputStream outputStream;
    private ByteArrayInputStream inputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
    }

    @After
    public void cleanUp() throws IOException {
        outputStream.close();
    }

    @Test
    public void testReadMethod() throws IOException {
        DeflateCompressorInputStream deflateInput = new DeflateCompressorInputStream(inputStream, new DeflateParameters());
        byte[] testData = "Hello World!".getBytes();
        outputStream.write(testData);
        outputStream.flush();

        byte readByte = (byte) deflateInput.read();
        assertEquals((int) testData[0], (int) readByte);

        deflateInput.close();
    }

    @Test
    public void testReadByteArrayMethod() throws IOException {
        DeflateCompressorInputStream deflateInput = new DeflateCompressorInputStream(inputStream, new DeflateParameters());
        byte[] testData = "Hello World!".getBytes();
        outputStream.write(testData);
        outputStream.flush();

        byte[] readData = new byte[testData.length];
        int len = deflateInput.read(readData, 0, testData.length);
        assertEquals(testData.length, len);

        for (int i = 0; i < testData.length; i++) {
            assertEquals((int) testData[i], (int) readData[i]);
        }

        deflateInput.close();
    }

    @Test(expected = IOException.class)
    public void testReadMethodWithNegativeLength() throws IOException {
        DeflateCompressorInputStream deflateInput = new DeflateCompressorInputStream(inputStream, new DeflateParameters());
        deflateInput.read(-1);
    }

    @Test(expected = IOException.class)
    public void testReadByteArrayMethodWithNegativeLength() throws IOException {
        DeflateCompressorInputStream deflateInput = new DeflateCompressorInputStream(inputStream, new DeflateParameters());
        byte[] readData = new byte[1];
        deflateInput.read(readData, 0, -1);
    }

    @Test
    public void testSkipMethod() throws IOException {
        DeflateCompressorInputStream deflateInput = new DeflateCompressorInputStream(inputStream, new DeflateParameters());
        long testDataLength = 5;
        outputStream.write(new byte[testDataLength]);
        outputStream.flush();

        long skippedBytes = deflateInput.skip(testDataLength);
        assertEquals(testDataLength, skippedBytes);

        deflateInput.close();
    }

    @Test
    public void testAvailableMethod() throws IOException {
        DeflateCompressorInputStream deflateInput = new DeflateCompressorInputStream(inputStream, new DeflateParameters());
        outputStream.write("Test".getBytes());
        outputStream.flush();

        int availableBytes = deflateInput.available();
        assertEquals(4, availableBytes);

        deflateInput.close();
    }

    @Test
    public void testCloseMethod() throws IOException {
        DeflateCompressorInputStream deflateInput = new DeflateCompressorInputStream(inputStream, new DeflateParameters());

        deflateInput.close();

        byte[] readData = new byte[1];
        int len = deflateInput.read(readData, 0, 1);
        assertEquals(-1, len);
    }
}