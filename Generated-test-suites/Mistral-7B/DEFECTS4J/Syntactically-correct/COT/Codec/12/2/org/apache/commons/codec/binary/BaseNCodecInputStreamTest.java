import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.apache.commons.codec.binary.BaseNCodecOutputStream;
import org.apache.commons.codec.binary.BaseNCodecTest;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BaseNCodecInputStreamTest {

    @Test
    public void testDefaultConstructor() {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(null, null, false);
        Assert.assertNull(inputStream.getBaseNCodec());
        Assert.assertFalse(inputStream.isDoEncode());
    }

    @Test
    public void testConstructorWithInputStreamAndCodec() {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01});
        BaseNCodecInputStream inputStreamWithCodec = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        Assert.assertEquals(baseNCodec, inputStreamWithCodec.getBaseNCodec());
        Assert.assertFalse(inputStreamWithCodec.isDoEncode());
    }

    @Test
    public void testConstructorWithInputStreamAndCodecAndEncode() {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01});
        BaseNCodecInputStream inputStreamWithCodec = new BaseNCodecInputStream(inputStream, baseNCodec, true);
        Assert.assertEquals(baseNCodec, inputStreamWithCodec.getBaseNCodec());
        Assert.assertTrue(inputStreamWithCodec.isDoEncode());
    }

    @Test
    public void testRead() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02});
        BaseNCodecInputStream inputStreamWithCodec = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] buffer = new byte[2];

        int read = inputStreamWithCodec.read(buffer, 0, 2);
        Assert.assertEquals(2, read);
        Assert.assertArrayEquals(new byte[]{0x01, 0x02}, buffer);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeLength() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01});
        BaseNCodecInputStream inputStreamWithCodec = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        inputStreamWithCodec.read(new byte[0], 1, 2);
    }

    @Test
    public void testReadWithZeroLength() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01});
        BaseNCodecInputStream inputStreamWithCodec = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        byte[] buffer = new byte[1];
        int read = inputStreamWithCodec.read(buffer, 0, 0);
        Assert.assertEquals(0, read);
    }

    @Test
    public void testReadWithOffset() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02});
        BaseNCodecInputStream inputStreamWithCodec = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        byte[] buffer = new byte[2];

        int read = inputStreamWithCodec.read(buffer, 1, 1);
        Assert.assertEquals(1, read);
        Assert.assertEquals(0x02, buffer[1]);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testReadWithOffsetOutOfBounds() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02});
        BaseNCodecInputStream inputStreamWithCodec = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        byte[] buffer = new byte[2];

        int read = inputStreamWithCodec.read(buffer, 3, 1);
    }

    @Test
    public void testMarkSupported() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01});
        BaseNCodecInputStream inputStreamWithCodec = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        Assert.assertFalse(inputStreamWithCodec.markSupported());
    }

    @Test
    public void testReadWithMark() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02});
        BaseNCodecInputStream inputStreamWithCodec = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        byte[] buffer = new byte[2];

        inputStreamWithCodec.mark(10);
        int read = inputStreamWithCodec.read(buffer, 0, 2);
        Assert.assertEquals(2, read);
        Assert.assertArrayEquals(new byte[]{0x01, 0x02}, buffer);

        int readAfterMark = inputStreamWithCodec.read(buffer, 0, 2);
        Assert.assertEquals(2, readAfterMark);
        Assert.assertArrayEquals(new byte[]{0x02, 0x01}, buffer);

        inputStreamWithCodec.reset();
        int readAfterReset = inputStreamWithCodec.read(buffer, 0, 2);
        Assert.assertEquals(1, readAfterReset);
        Assert.assertEquals(0x01, buffer[0]);
    }

    @Test(expected = IOException.class)
    public void testReadWithMarkAndNegativeLength() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02});
        BaseNCodecInputStream inputStreamWithCodec = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        byte[] buffer = new byte[2];

        inputStreamWithCodec.mark(10);
        inputStreamWithCodec.read(buffer, -1, 2);
    }

    @Test
    public void testReadWithBufferFull() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
        BaseNCodecInputStream inputStreamWithCodec = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] buffer = new byte[1024];
        int read = inputStreamWithCodec.read(buffer);
        Assert.assertEquals(1024, read);
    }

    @Test
    public void testReadWithBufferPartiallyFilled() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A});
        BaseNCodecInputStream inputStreamWithCodec = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] buffer = new byte[5];
        int read = inputStreamWithCodec.read(buffer);
        Assert.assertEquals(5, read);
        Assert.assertArrayEquals(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05}, buffer);
    }

    @Test(expected = IOException.class)
    public void testReadWithNullBuffer() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02});
        BaseNCodecInputStream inputStreamWithCodec = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        inputStreamWithCodec.read(null, 0, 1);
    }

    @Test
    public void testReadWithNullBufferAndNegativeLength() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02});
        BaseNCodecInputStream inputStreamWithCodec = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        inputStreamWithCodec.read(null, -1, 1);
    }

    @Test
    public void testClose() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02});
        BaseNCodecInputStream inputStreamWithCodec = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        inputStreamWithCodec.close();
        Assert.assertFalse(inputStream.isOpen());
        Assert.assertNull(inputStreamWithCodec.getBaseNCodec());
    }

    @Test(expected = NullPointerException.class)
    public void testDecodeWithNullCodec() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02});
        BaseNCodecInputStream inputStreamWithCodec = new BaseNCodecInputStream(inputStream, null, false);
        byte[] buffer = new byte[2];
        inputStreamWithCodec.read(buffer, 0, 2);
    }

    @Test(expected = NullPointerException.class)
    public void testEncodeWithNullCodec() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02});
        BaseNCodecInputStream inputStreamWithCodec = new BaseNCodecInputStream(inputStream, null, true);
        byte[] buffer = new byte[2];
        inputStreamWithCodec.read(buffer, 0, 2);
    }

    @Test(expected = IOException.class)
    public void testDecodeWithEmptyBuffer() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02});
        BaseNCodecInputStream inputStreamWithCodec = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        byte[] buffer = new byte[0];
        int read = inputStreamWithCodec.read(buffer, 0, 0);
    }

    @Test(expected = IOException.class)
    public void testEncodeWithEmptyBuffer() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02});
        BaseNCodecInputStream inputStreamWithCodec = new BaseNCodecInputStream(inputStream, baseNCodec, true);
        byte[] buffer = new byte[0];
        int read = inputStreamWithCodec.read(buffer, 0, 0);
    }

    @Test(expected = IOException.class)
    public void testDecodeWithNegativeLength() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02});
        BaseNCodecInputStream inputStreamWithCodec = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        byte[] buffer = new byte[2];
        int read = inputStreamWithCodec.read(buffer, -1, 1);
    }

    @Test(expected = IOException.class)
    public void testEncodeWithNegativeLength() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02});
        BaseNCodecInputStream inputStreamWithCodec = new BaseNCodecInputStream(inputStream, baseNCodec, true);
        byte[] buffer = new byte[2];
        int read = inputStreamWithCodec.read(buffer, 2, 1);
    }

    @Test(expected = IOException.class)
    public void testDecodeWithInvalidData() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, (byte) 0x80});
        BaseNCodecInputStream inputStreamWithCodec = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        byte[] buffer = new byte[3];
        int read = inputStreamWithCodec.read(buffer, 0, 3);
    }

    @Test(expected = IOException.class)
    public void testEncodeWithInvalidData() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, (byte) 0x80});
        BaseNCodecInputStream inputStreamWithCodec = new BaseNCodecInputStream(inputStream, baseNCodec, true);
        byte[] buffer = new byte[3];
        int read = inputStreamWithCodec.read(buffer, 0, 3);
    }

    @Test
    public void testDecode() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A});
        BaseNCodecInputStream inputStreamWithCodec = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] buffer = new byte[13];
        int read = inputStreamWithCodec.read(buffer, 0, 13);
        Assert.assertEquals(13, read);
        Assert.assertArrayEquals(BaseNCodecTest.getBase64Value("QJw=/Q==").getBytes(), buffer);
    }

    @Test
    public void testEncode() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A});
        BaseNCodecInputStream inputStreamWithCodec = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        byte[] buffer = new byte[13];
        int read = inputStreamWithCodec.read(buffer, 0, 13);
        Assert.assertEquals(13, read);
        Assert.assertArrayEquals(BaseNCodecTest.getBase64Value("QJw=/Q==").getBytes(), buffer);

        String encodedValue = BaseNCodecTest.getBaseNValue(buffer);
        Assert.assertEquals("CeQ==ZW1haWxfc29tZS5jb20=", encodedValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testResetAfterClose() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x01, 0x02});
        BaseNCodecInputStream inputStreamWithCodec = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        inputStreamWithCodec.close();
        inputStreamWithCodec.reset();
    }
}