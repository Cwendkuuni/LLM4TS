import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BaseNCodecInputStreamTest {

    private BaseNCodecInputStream inputStream;
    private ByteArrayOutputStream outputStream;
    private BaseNCodec baseNCodec;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        baseNCodec = new BaseNCodec();
        inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(outputStream.toByteArray()), baseNCodec, false);
    }

    @Test
    public void testRead() throws IOException {
        int readByte;
        byte expectedByte;

        // Test reading valid bytes
        for (int i = 0; i < 256; i++) {
            expectedByte = (byte) i;
            inputStream.write(expectedByte);
            outputStream.reset();
            readByte = inputStream.read();
            Assert.assertEquals("Expected byte: " + expectedByte + " was not read", expectedByte, readByte);
        }

        // Test reading EOF
        outputStream.writeTo(0, BaseNCodec.EOF.length);
        inputStream.mark(BaseNCodecInputStream.MARK_SIZE_UNLIMITED);
        readByte = inputStream.read();
        inputStream.reset();
        Assert.assertEquals("Expected EOF was not read", BaseNCodec.EOF, readByte);
        readByte = inputStream.read();
        Assert.assertEquals("Expected EOF was not read", BaseNCodec.EOF, readByte);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] expectedArray = new byte[5];
        byte[] actualArray;

        // Test reading valid bytes into byte array
        for (int i = 0; i < 256; i++) {
            expectedArray[i] = (byte) i;
            inputStream.write(expectedArray[i]);
            outputStream.reset();
            actualArray = new byte[expectedArray.length];
            int bytesRead = inputStream.read(actualArray, 0, expectedArray.length);
            Assert.assertEquals("Expected number of bytes read was not as expected: " + bytesRead, expectedArray.length, bytesRead);
            for (int j = 0; j < expectedArray.length; j++) {
                Assert.assertEquals("Expected byte " + expectedArray[j] + " was not read correctly", expectedArray[j], actualArray[j]);
            }
        }

        // Test reading EOF into byte array
        outputStream.writeTo(0, BaseNCodec.EOF.length);
        actualArray = new byte[expectedArray.length];
        bytesRead = inputStream.read(actualArray, 0, expectedArray.length);
        Assert.assertEquals("Expected number of bytes read was not as expected: " + bytesRead, BaseNCodecInputStream.MARK_SIZE_UNLIMITED, bytesRead);
        for (int i = 0; i < BaseNCodecInputStream.MARK_SIZE_UNLIMITED; i++) {
            Assert.assertEquals("Expected EOF was not read correctly", BaseNCodec.EOF, actualArray[i]);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMarkUnsupported() throws IOException {
        inputStream.mark(512);
    }

    @Test
    public void testSkip() throws IOException {
        long expectedPosition = 0;

        // Test skipping valid bytes
        for (int i = 0; i < 1024; i += 512) {
            inputStream.skip(512);
            expectedPosition += 512;
            Assert.assertEquals("Expected position: " + expectedPosition + " was not as expected: " + inputStream.getFilePointer(), expectedPosition, inputStream.getFilePointer());
        }

        // Test skipping to EOF
        int remainingBytes = (int) (BaseNCodecInputStream.MARK_SIZE_UNLIMITED - inputStream.getFilePointer());
        inputStream.skip(remainingBytes);
        Assert.assertEquals("Expected position: " + BaseNCodecInputStream.MARK_SIZE_UNLIMITED + " was not as expected: " + inputStream.getFilePointer(), BaseNCodecInputStream.MARK_SIZE_UNLIMITED, inputStream.getFilePointer());

        // Test skipping negative bytes
        inputStream.skip(-1);
    }

    @Test
    public void testAvailable() {
        int expectedAvailable = 0;

        // Test available bytes when no data is present
        Assert.assertEquals("Expected number of available bytes was not as expected", expectedAvailable, inputStream.available());

        // Test available bytes when valid data is present
        for (int i = 0; i < 256; i++) {
            inputStream.write((byte) i);
            expectedAvailable++;
            outputStream.reset();
            Assert.assertEquals("Expected number of available bytes was not as expected", expectedAvailable, inputStream.available());
        }

        // Test available bytes when EOF is reached
        outputStream.writeTo(0, BaseNCodec.EOF.length);
        inputStream.mark(BaseNCodecInputStream.MARK_SIZE_UNLIMITED);
        inputStream.read();
        inputStream.reset();
        Assert.assertEquals("Expected number of available bytes was not as expected", BaseNCodecInputStream.MARK_SIZE_UNLIMITED, inputStream.available());
    }

    @Test
    public void testClose() throws IOException {
        // Test closing the InputStream
        inputStream.close();
        Assert.assertFalse("Expected InputStream to be closed", inputStream.isOpen());
    }
}