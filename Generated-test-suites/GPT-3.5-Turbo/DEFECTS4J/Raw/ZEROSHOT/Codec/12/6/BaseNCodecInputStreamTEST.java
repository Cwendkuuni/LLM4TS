import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class BaseNCodecInputStreamTest {

    private BaseNCodec baseNCodec;
    private BaseNCodecInputStream baseNCodecInputStream;

    @Before
    public void setup() {
        baseNCodec = new BaseNCodec(StandardCharsets.UTF_8);
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);
    }

    @Test
    public void readShouldReturnByteInRange0To255() throws Exception {
        int result = baseNCodecInputStream.read();
        Assert.assertTrue(result >= 0 && result <= 255);
    }

    @Test
    public void readShouldReturnNegativeOneWhenEOFReached() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        int result = baseNCodecInputStream.read();
        Assert.assertEquals(-1, result);
    }

    @Test(expected = IOException.class)
    public void readShouldThrowIOExceptionOnIOException() throws Exception {
        InputStream inputStream = new InputStream() {
            @Override
            public int read() throws IOException {
                throw new IOException();
            }
        };
        baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);
        baseNCodecInputStream.read();
    }

    @Test(expected = NullPointerException.class)
    public void readShouldThrowNullPointerExceptionWhenByteArrayParameterIsNull() throws Exception {
        baseNCodecInputStream.read(null, 1, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void readShouldThrowIndexOutOfBoundsExceptionWhenOffsetIsNegative() throws Exception {
        byte[] byteArray = new byte[5];
        baseNCodecInputStream.read(byteArray, -1, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void readShouldThrowIndexOutOfBoundsExceptionWhenOffsetPlusLenIsGreaterThanByteArrayLength() throws Exception {
        byte[] byteArray = new byte[5];
        baseNCodecInputStream.read(byteArray, 0, 6);
    }

    @Test
    public void readShouldReturnZeroWhenLenIsZero() throws Exception {
        byte[] byteArray = new byte[5];
        int result = baseNCodecInputStream.read(byteArray, 0, 0);
        Assert.assertEquals(0, result);
    }

    @Test
    public void readShouldReturnNumberOfBytesRead() throws Exception {
        byte[] byteArray = new byte[5];
        int result = baseNCodecInputStream.read(byteArray, 0, 5);
        Assert.assertEquals(5, result);
    }

    @Test
    public void readShouldReadByteArrayIntoSpecifiedBuffer() throws Exception {
        byte[] byteArray = new byte[5];
        baseNCodecInputStream.read(byteArray, 0, 5);
        Assert.assertArrayEquals(new byte[]{0, 0, 0, 0, 0}, byteArray);
    }

    @Test(expected = NullPointerException.class)
    public void readShouldThrowNullPointerExceptionWhenByteArrayParameterIsNullInBaseNCodec() throws Exception {
        BaseNCodec baseNCodec = new BaseNCodec(StandardCharsets.UTF_8) {
            @Override
            protected boolean hasData() {
                return true;
            }

            @Override
            protected void encode(byte[] b, int off, int len) {
            }

            @Override
            protected void decode(byte[] b, int off, int len) {
                throw new NullPointerException();
            }
        };

        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] byteArray = new byte[5];
        baseNCodecInputStream.read(byteArray, 0, 5);
    }

    @Test
    public void markSupportedShouldReturnFalse() {
        boolean result = baseNCodecInputStream.markSupported();
        Assert.assertFalse(result);
    }

    @Test
    public void availableShouldReturnZeroWhenEOFReached() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        int result = baseNCodecInputStream.available();
        Assert.assertEquals(0, result);
    }

    @Test
    public void availableShouldReturnOneWhenDataAvailable() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[1]);
        baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        int result = baseNCodecInputStream.available();
        Assert.assertEquals(1, result);
    }

    @Test(expected = IOException.class)
    public void availableShouldThrowIOExceptionOnIOException() throws Exception {
        InputStream inputStream = new InputStream() {
            @Override
            public int available() throws IOException {
                throw new IOException();
            }
        };
        baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);
        baseNCodecInputStream.available();
    }

}