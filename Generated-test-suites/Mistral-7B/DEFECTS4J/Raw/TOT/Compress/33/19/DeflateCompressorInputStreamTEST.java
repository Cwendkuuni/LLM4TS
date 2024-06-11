import org.apache.commons.compress.compressors.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.DeflateParameters;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DeflateCompressorInputStreamTest {

    private InputStream inputStream;
    private DeflateCompressorInputStream decompressor;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() throws IOException {
        byte[] testData = "Test data for DeflateCompressorInputStreamTest".getBytes();
        inputStream = new ByteArrayInputStream(testData);
        outputStream = new ByteArrayOutputStream();
        DeflateParameters parameters = new DeflateParameters();
        decompressor = new DeflateCompressorInputStream(inputStream, parameters);
    }

    @After
    public void tearDown() throws IOException {
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(outputStream);
        IOUtils.closeQuietly(decompressor);
    }

    @Test
    public void testReadByte() throws IOException {
        byte[] expectedData = new byte[testData.length];
        int readByte;
        for (int i = 0; i < testData.length; i++) {
            readByte = decompressor.read();
            expectedData[i] = (byte) readByte;
        }
        Assert.assertArrayEquals("Unexpected decompressed data", expectedData, decompressor.readAllBytes());
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] expectedData = new byte[testData.length];
        byte[] buffer = new byte[testData.length];
        int read = decompressor.read(buffer);
        Assert.assertEquals("Unexpected number of bytes read", testData.length, read);
        Assert.assertArrayEquals("Unexpected decompressed data", expectedData, buffer);
    }

    @Test(expected = IOException.class)
    public void testReadByteFailsWithEOF() throws IOException {
        byte[] buffer = new byte[testData.length];
        decompressor.read(buffer);
        decompressor.read();
        decompressor.read();
    }

    @Test
    public void testSkip() throws IOException {
        long skipped = decompressor.skip(testData.length);
        Assert.assertEquals("Unexpected number of bytes skipped", testData.length, skipped);
    }

    @Test
    public void testAvailable() throws IOException {
        Assert.assertEquals("Unexpected number of bytes available", testData.length, decompressor.available());
    }

    @Test
    public void testClose() throws IOException {
        byte[] expectedData = new byte[testData.length];
        int readByte;
        for (int i = 0; i < testData.length; i++) {
            readByte = decompressor.read();
            expectedData[i] = (byte) readByte;
        }
        decompressor.close();
        Assert.assertArrayEquals("Unexpected decompressed data", expectedData, decompressor.readAllBytes());
    }

    @Test
    public void testZlibHeader() throws IOException {
        byte[] zlibHeader = new byte[]{0x78, 0x5E, 0x01, 0x00, 0xDA, 0x00};
        ByteArrayInputStream zlibHeaderInputStream = new ByteArrayInputStream(zlibHeader);
        DeflateCompressorInputStream zlibDecompressor = new DeflateCompressorInputStream(zlibHeaderInputStream, new DeflateParameters());
        byte[] testData = "Test data for DeflateCompressorInputStreamTest".getBytes();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DeflateParameters parameters = new DeflateParameters();
        DeflateCompressorInputStream decompressor = new DeflateCompressorInputStream(new ByteArrayInputStream(new byte[zlibHeader.length + testData.length]), parameters);

        int read;
        byte[] expectedData = new byte[testData.length];
        for (int i = 0; i < testData.length; i++) {
            read = decompressor.read();
            expectedData[i] = (byte) read;
        }

        decompressor.read(outputStream.getBuffer(), 0, testData.length);
        decompressor.close();

        Assert.assertArrayEquals("Unexpected decompressed data", expectedData, outputStream.toByteArray());
    }

    @Test(expected = IOException.class)
    public void testIncorrectZlibHeader() throws IOException {
        byte[] incorrectZlibHeader = new byte[]{0x78, 0x5E, 0x01, 0x01, 0xDA, 0x00};
        ByteArrayInputStream incorrectZlibHeaderInputStream = new ByteArrayInputStream(incorrectZlibHeader);
        DeflateCompressorInputStream incorrectZlibDecompressor = new DeflateCompressorInputStream(incorrectZlibHeaderInputStream, new DeflateParameters());
    }
}