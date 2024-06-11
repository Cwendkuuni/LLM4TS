import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BaseNCodecInputStreamTest {

    private BaseNCodecInputStream baseNCodecInputStream;
    private byte[] testData = {65, 66, 67, 68, 69}; // ABCDE

    @Before
    public void setUp() {
        InputStream inputStream = new ByteArrayInputStream(testData);
        BaseNCodec baseNCodec = new BaseNCodec();
        baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);
    }

    @Test
    public void testRead() throws IOException {
        int result = baseNCodecInputStream.read();
        Assert.assertEquals(65, result);

        result = baseNCodecInputStream.read();
        Assert.assertEquals(66, result);

        result = baseNCodecInputStream.read();
        Assert.assertEquals(67, result);

        result = baseNCodecInputStream.read();
        Assert.assertEquals(68, result);

        result = baseNCodecInputStream.read();
        Assert.assertEquals(69, result);

        result = baseNCodecInputStream.read();
        Assert.assertEquals(-1, result); // EOF
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[3];
        int bytesRead = baseNCodecInputStream.read(buffer);

        Assert.assertEquals(3, bytesRead);
        Assert.assertArrayEquals(new byte[]{65, 66, 67}, buffer);

        bytesRead = baseNCodecInputStream.read(buffer);

        Assert.assertEquals(2, bytesRead);
        Assert.assertArrayEquals(new byte[]{68, 69, 67}, buffer);

        bytesRead = baseNCodecInputStream.read(buffer);

        Assert.assertEquals(-1, bytesRead); // EOF
    }

    @Test(expected = NullPointerException.class)
    public void testReadByteArrayNullBuffer() throws IOException {
        baseNCodecInputStream.read(null, 0, 3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayNegativeOffset() throws IOException {
        byte[] buffer = new byte[3];
        baseNCodecInputStream.read(buffer, -1, 3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayNegativeLength() throws IOException {
        byte[] buffer = new byte[3];
        baseNCodecInputStream.read(buffer, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayInvalidOffset() throws IOException {
        byte[] buffer = new byte[3];
        baseNCodecInputStream.read(buffer, 4, 3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayInvalidLength() throws IOException {
        byte[] buffer = new byte[3];
        baseNCodecInputStream.read(buffer, 0, 4);
    }

    @Test
    public void testMarkSupported() {
        boolean result = baseNCodecInputStream.markSupported();
        Assert.assertFalse(result);
    }

    @Test
    public void testAvailable() {
        int result = baseNCodecInputStream.available();
        Assert.assertEquals(1, result);
    }

}