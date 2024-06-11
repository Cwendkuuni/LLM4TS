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
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(outputStream.toByteArray()), baseNCodec, false);

        byte[] result = new byte[1];
        int read = inputStream.read(result, 0, 1);
        Assert.assertEquals(1, read);
        Assert.assertArrayEquals(new byte[]{(byte) 0x01}, result);

        outputStream.write(new byte[]{(byte) 0x01});
        outputStream.flush();

        read = inputStream.read(result, 0, 1);
        Assert.assertEquals(1, read);
        Assert.assertArrayEquals(new byte[]{(byte) 0x01}, result);

        // Test reading past EOF
        read = inputStream.read(result, 0, 1);
        Assert.assertEquals(-1, read);
    }

    @Test
    public void testReadBytes() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(outputStream.toByteArray()), baseNcodec, false);

        byte[] result = new byte[3];
        int read = inputStream.read(result, 0, 3);
        Assert.assertEquals(3, read);
        Assert.assertArrayEquals(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03}, result);

        outputStream.write(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03});
        outputStream.flush();

        read = inputStream.read(result, 0, 3);
        Assert.assertEquals(3, read);
        Assert.assertArrayEquals(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03}, result);

        // Test reading past EOF
        read = inputStream.read(result, 0, 3);
        Assert.assertEquals(-1, read);
    }

    @Test(expected = NullPointerException.class)
    public void testReadNullArray() {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), new BaseNCodec(), false);
        inputStream.read(null, 0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadNegativeOffset() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(outputStream.toByteArray()), baseNCodec, false);
        byte[] result = new byte[1];
        inputStream.read(result, -1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadNegativeLength() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(outputStream.toByteArray()), baseNCodec, false);
        byte[] result = new byte[1];
        inputStream.read(result, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadBeyondAvailableData() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(outputStream.toByteArray()), baseNCodec, false);

        byte[] result = new byte[2];
        int read = inputStream.read(result, 0, 1);
        Assert.assertEquals(1, read);
        Assert.assertArrayEquals(new byte[]{(byte) 0x01}, result);

        outputStream.write(new byte[]{(byte) 0x01});
        outputStream.flush();

        read = inputStream.read(result, 0, 1);
        Assert.assertEquals(1, read);
        Assert.assertArrayEquals(new byte[]{(byte) 0x01}, result);

        read = inputStream.read(result, 0, 2);
        Assert.assertEquals(-1, read);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testMarkSupportedNegative() {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), new BaseNCodec(), false);
        inputStream.markSupported(-1);
    }

    @Test
    public void testMarkSupportedFalse() {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), new BaseNCodec(), false);
        Assert.assertFalse(inputStream.markSupported());
    }
}