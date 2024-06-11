import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BaseNCodecInputStreamTest {

    @Test
    public void testRead() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] testData = "A2345678901234567890".getBytes();
        byteArrayOutputStream.write(testData);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] readByte = new byte[1];
        int read;

        // Test normal read
        read = baseNCodecInputStream.read(readByte, 0, 1);
        Assert.assertEquals(1, read);
        Assert.assertEquals(testData[0], readByte[0]);

        // Test read at EOF
        read = baseNCodecInputStream.read(readByte, 0, 1);
        Assert.assertEquals(-1, read);

        // Test read with empty buffer
        read = baseNCodecInputStream.read(readByte, 0, 0);
        Assert.assertEquals(-1, read);
    }

    @Test
    public void testReadByteArray() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] testData = "A2345678901234567890".getBytes();
        byteArrayOutputStream.write(testData);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] expected = testData;
        byte[] readByteArray = new byte[expected.length];
        int read;

        // Test normal read
        read = baseNCodecInputStream.read(readByteArray, 0, expected.length);
        Assert.assertEquals(expected.length, read);
        Assert.assertArrayEquals(expected, readByteArray);

        // Test read at EOF
        read = baseNCodecInputStream.read(readByteArray, 0, 1);
        Assert.assertEquals(-1, read);

        // Test read with negative offset and length
        try {
            baseNCodecInputStream.read(readByteArray, -1, 1);
            Assert.fail();
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        try {
            baseNCodecInputStream.read(readByteArray, 0, -1);
            Assert.fail();
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        // Test read with offset larger than array size
        try {
            baseNCodecInputStream.read(readByteArray, 5, 1);
            Assert.fail();
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }

        // Test read with length larger than array size
        try {
            baseNCodecInputStream.read(readByteArray, 0, 6);
            Assert.fail();
        } catch (IndexOutOfBoundsException e) {
            // Expected
        }
    }

    @Test
    public void testMarkSupported() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] testData = "A2345678901234567890".getBytes();
        byteArrayOutputStream.write(testData);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        // Test markSupported returns false
        Assert.assertFalse(baseNCodecInputStream.markSupported());
    }

    @Test
    public void testSkip() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] testData = "A2345678901234567890".getBytes();
        byteArrayOutputStream.write(testData);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        int skip = 512;

        // Test normal skip
        baseNCodecInputStream.skip(skip);

        // Test skip at EOF
        baseNCodecInputStream.skip(skip);

        // Test skip with negative value
        try {
            baseNCodecInputStream.skip(-1);
            Assert.fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testAvailable() throws IOException {
        BaseNCodec baseNCode = new BaseNCodec();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] testData = "A2345678901234567890".getBytes();
        byteArrayOutputStream.write(testData);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCode, false);

        int available = 511;
        byte[] testDataPart = new byte[available];

        // Test available returns 0 at EOF
        baseNCodecInputStream.read(testDataPart, 0, available);
        Assert.assertEquals(0, baseNCodecInputStream.available());

        // Test normal available
        baseNCodecInputStream.read(testDataPart, 0, available);
        Assert.assertEquals(available, baseNCodecInputStream.available());
    }
}