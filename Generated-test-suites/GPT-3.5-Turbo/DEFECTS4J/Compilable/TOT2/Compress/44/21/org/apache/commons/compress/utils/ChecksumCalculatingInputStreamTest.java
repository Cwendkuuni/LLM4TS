package org.apache.commons.compress.utils;
import org.junit.Test;
import org.junit.Assert;
import java.io.ByteArrayInputStream;
import java.util.zip.Adler32;

public class ChecksumCalculatingInputStreamTest {

    @Test
    public void testReadSingleByte() {
        byte[] testData = {1, 2, 3};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(testData);
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(new Adler32(), byteArrayInputStream);

        try {
            Assert.assertEquals(1, stream.read());
            Assert.assertEquals(2, stream.read());
            Assert.assertEquals(3, stream.read());
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testReadByteArray() {
        byte[] testData = {1, 2, 3};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(testData);
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(new Adler32(), byteArrayInputStream);

        try {
            byte[] data = new byte[3];
            Assert.assertEquals(3, stream.read(data));
            Assert.assertArrayEquals(testData, data);
            Assert.assertEquals(0, stream.read(data));
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testSkip() {
        byte[] testData = {1, 2, 3};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(testData);
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(new Adler32(), byteArrayInputStream);

        try {
            Assert.assertEquals(1, stream.skip(1));
            Assert.assertEquals(2, stream.skip(1));
            Assert.assertEquals(0, stream.skip(1));
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testGetValue() {
        byte[] testData = {1, 2, 3};
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(testData);
        Adler32 adler32 = new Adler32();
        ChecksumCalculatingInputStream stream = new ChecksumCalculatingInputStream(adler32, byteArrayInputStream);

        try {
            while (stream.read() >= 0) {
                // Reading all data
            }

            Assert.assertEquals(adler32.getValue(), stream.getValue());
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }

}