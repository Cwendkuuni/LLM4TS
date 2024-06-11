import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DeflateCompressorInputStreamTest {

    private ByteArrayOutputStream outputStream;
    private ByteArrayInputStream inputStream;
    private DeflateCompressorInputStream testStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        DeflateParameters deflateParameters = new DeflateParameters();

        inputStream = new ByteArrayInputStream(IOUtils.toByteArray(this.getClass().getResource("/testData.txt")));
        testStream = new DeflateCompressorInputStream(inputStream, deflateParameters);
    }

    @Test
    public void testReadByte() throws IOException {
        byte[] testByteArray = new byte[10];
        int readByte;

        for (int i = 0; i < 10; i++) {
            readByte = testStream.read();
            testByteArray[i] = (byte) readByte;
        }

        Assert.assertArrayEquals("Test readByte() incorrect result", testByteArray, IOUtils.toByteArray(this.getClass().getResource("/testData.txt")));
    }

    @Test(expected = IOException.class)
    public void testReadByteNegativeInput() throws IOException {
        testStream.read(-1);
    }

    @Test
    public void testReadByteEndOfStream() throws IOException {
        int readByte = testStream.read();

        Assert.assertEquals("Test readByte() at end of stream incorrect result", -1, readByte);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] testByteArray = new byte[10];
        int bytesRead;

        bytesRead = testStream.read(testByteArray, 0, 10);

        Assert.assertEquals("Test read() incorrect number of bytes read", 10, bytesRead);
        Assert.assertArrayEquals("Test readByteArray() incorrect result", IOUtils.toByteArray(this.getClass().getResource("/testData.txt")), testByteArray);
    }

    @Test(expected = IOException.class)
    public void testReadByteArrayNegativeInput() throws IOException {
        byte[] testByteArray = new byte[10];
        int bytesToRead = -10;

        testStream.read(testByteArray, 0, bytesToRead);
    }

    @Test
    public void testSkip() throws IOException {
        long bytesToSkip = 5;

        testStream.skip(bytesToSkip);

        byte[] testByteArray = new byte[10];
        int bytesRead = testStream.read(testByteArray, 0, 10);

        Assert.assertArrayEquals("Test skip() incorrect result", IOUtils.toByteArray(this.getClass().getResource("/testData.txt")).substring(5), testByteArray);
    }

    @Test
    public void testAvailable() throws IOException {
        int bytesAvailable = testStream.available();

        Assert.assertEquals("Test available() incorrect number of bytes available", IOUtils.sizeOf(this.getClass().getResource("/testData.txt")), bytesAvailable);
    }

    @Test
    public void testClose() throws IOException {
        testStream.close();

        Assert.assertThrows(IOException.class, () -> testStream.read());
    }

    @Test
    public void testZlibHeader() throws IOException {
        outputStream.write("PK\u0003\u0004".getBytes());
        outputStream.write(IOUtils.toByteArray(this.getClass().getResource("/testData.txt")));
        inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        DeflateCompressorInputStream zlibTestStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());

        byte[] testByteArray = new byte[4];
        int bytesRead = zlibTestStream.read(testByteArray, 0, 4);

        Assert.assertArrayEquals("Test zlib header incorrect result", new byte[]{"PK", 0x03, 0x04}, testByteArray);
    }

    @After
    public void tearDown() throws Exception {
        IOUtils.closeQuietly(testStream);
        IOUtils.closeQuietly(inputStream);
        IOUtils.closeQuietly(outputStream);
    }
}