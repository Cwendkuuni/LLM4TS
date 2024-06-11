import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class BaseNCodecInputStreamTest {

    private BaseNCodecInputStream baseNCodecInputStream;

    @Before
    public void setUp() {
        byte[] data = {97, 98, 99, 100, 101, 102}; // sample data
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        BaseNCodec baseNCodec = new BaseNCodec();
        baseNCodecInputStream = new BaseNCodecInputStream(byteArrayInputStream, baseNCodec, true);
    }

    @Test
    public void testReadSingleByte() throws IOException {
        int result = baseNCodecInputStream.read();
        Assert.assertEquals(97, result);
    }

    @Test
    public void testReadIntoArray() throws IOException {
        byte[] buffer = new byte[3];
        int result = baseNCodecInputStream.read(buffer, 0, 3);
        Assert.assertEquals(3, result);
        Assert.assertEquals(97, buffer[0]);
        Assert.assertEquals(98, buffer[1]);
        Assert.assertEquals(99, buffer[2]);
    }

    @Test(expected = NullPointerException.class)
    public void testReadNullArray() throws IOException {
        baseNCodecInputStream.read(null, 0, 3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidOffset() throws IOException {
        byte[] buffer = new byte[3];
        baseNCodecInputStream.read(buffer, -1, 3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidLength() throws IOException {
        byte[] buffer = new byte[3];
        baseNCodecInputStream.read(buffer, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidArraySize() throws IOException {
        byte[] buffer = new byte[3];
        baseNCodecInputStream.read(buffer, 0, 4);
    }

    @Test
    public void testReadZeroLength() throws IOException {
        byte[] buffer = new byte[3];
        int result = baseNCodecInputStream.read(buffer, 0, 0);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testMarkSupported() {
        boolean result = baseNCodecInputStream.markSupported();
        Assert.assertFalse(result);
    }

    @Test
    public void testReadToEndOfFile() throws IOException {
        byte[] buffer = new byte[6];
        int result = baseNCodecInputStream.read(buffer, 0, 6);
        Assert.assertEquals(6, result);
        Assert.assertEquals(97, buffer[0]);
        Assert.assertEquals(98, buffer[1]);
        Assert.assertEquals(99, buffer[2]);
        Assert.assertEquals(100, buffer[3]);
        Assert.assertEquals(101, buffer[4]);
        Assert.assertEquals(102, buffer[5]);
    }

    @Test
    public void testReadBeyondEndOfFile() throws IOException {
        byte[] buffer = new byte[7];
        int result = baseNCodecInputStream.read(buffer, 0, 7);
        Assert.assertEquals(6, result);
        Assert.assertEquals(97, buffer[0]);
        Assert.assertEquals(98, buffer[1]);
        Assert.assertEquals(99, buffer[2]);
        Assert.assertEquals(100, buffer[3]);
        Assert.assertEquals(101, buffer[4]);
        Assert.assertEquals(102, buffer[5]);
        Assert.assertEquals(0, buffer[6]);
    }
}