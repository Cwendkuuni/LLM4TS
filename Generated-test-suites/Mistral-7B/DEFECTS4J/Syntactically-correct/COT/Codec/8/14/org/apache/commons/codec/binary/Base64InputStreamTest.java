import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    @Test
    public void testDefaultConstructor() throws IOException {
        Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]));
        Assert.assertFalse(inputStream.doEncode);
        Base64 base64 = new Base64(false);
        Assert.assertSame(base64, inputStream.base64);
    }

    @Test
    public void testBooleanConstructorEncodes() throws IOException {
        Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]), true);
        Assert.assertTrue(inputStream.doEncode);
        Base64 base64 = new Base64(true);
        Assert.assertSame(base64, inputStream.base64);
    }

    @Test
    public void testBooleanConstructorDecodes() throws IOException {
        Base64InputStream inputStream = new Base64InputStream(new ByteArrayInputStream(new byte[0]), false);
        Assert.assertFalse(inputStream.doEncode);
        Base64 base64 = new Base64(false);
        Assert.assertSame(base64, inputStream.base64);
    }

    @Test
    public void testLineLengthAndLineSeparatorConstructor() throws IOException {
        Base64OutputStream outputStream = new Base64OutputStream(new ByteArrayOutputStream());
        byte[] data = "test".getBytes();
        outputStream.write(data, 0, data.length);
        outputStream.close();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        Base64InputStream inputStreamWithLineLengthAndLineSeparator = new Base64InputStream(inputStream, true, 76, "\r\n".getBytes());
        Assert.assertTrue(inputStreamWithLineLengthAndLineSeparator.doEncode);
        Base64 base64 = new Base64(76, "\r\n".getBytes());
        Assert.assertSame(base64, inputStreamWithLineLengthAndLineSeparator.base64);

        byte[] result = new byte[76];
        int read = inputStreamWithLineLengthAndLineSeparator.read(result, 0, result.length);
        Assert.assertEquals(76, read);
    }

    @Test(expected = NullPointerException.class)
    public void testReadByteThrowNullPointerExceptionWithNullByteArray() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(null);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        base64InputStream.read();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteThrowIndexOutOfBoundsExceptionWithNegativeOffset() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        base64InputStream.read(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteThrowIndexOutOfBoundsExceptionWithNegativeLength() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        base64InputStream.read(-1, 0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteThrowIndexOutOfBoundsExceptionWithNegativeOffsetLength() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        base64InputStream.read(-1, -1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteThrowIndexOutOfBoundsExceptionWithNegativeBufferSize() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[-1];
        base64InputStream.read(buffer, 0, buffer.length);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteThrowIndexOutOfBoundsExceptionWithBufferSizeBiggerThanAvailableData() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[1]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[2];
        base64InputStream.read(buffer, 0, buffer.length);
    }

    @Test(expected = IOException.class)
    public void testReadByteThrowIOException() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]) {
            @Override
            public int read() throws IOException {
                throw new IOException();
            }
        };
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        base64InputStream.read();
    }

    @Test(expected = IOException.class)
    public void testReadByteArrayThrowIOException() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]) {
            @Override
            public int read(byte[] b, int off, int len) throws IOException {
                throw new IOException();
            }
        };
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[1];
        base64InputStream.read(buffer, 0, buffer.length);
    }

    @Test
    public void testReadByteArrayNormal() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{"A".getBytes()[0], "B".getBytes()[0]});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[2];
        int read = base64InputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(2, read);
        Assert.assertArrayEquals(new byte[]{"A".getBytes()[0], "B".getBytes()[0]}, buffer);
    }

    @Test
    public void testReadByteArrayWithAvailableDataLessThanBufferSize() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{"A".getBytes()[0], "B".getBytes()[0]});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[3];
        int read = base64InputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(2, read);
        Assert.assertArrayEquals(new byte[]{"A".getBytes()[0], "B".getBytes()[0]}, buffer, 0, 2);
    }

    @Test
    public void testReadByteArrayWithAvailableDataBiggerThanBufferSize() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{"A".getBytes()[0], "B".getBytes()[0], "C".getBytes()[0]});
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[2];
        int read = base64InputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(2, read);
        Assert.assertArrayEquals(new byte[]{"A".getBytes()[0], "B".getBytes()[0]}, buffer);
        read = base64InputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(1, read);
        Assert.assertArrayEquals(new byte[]{"C".getBytes()[0]}, buffer);
    }

    @Test(expected = NullPointerException.class)
    public void testReadByteArrayWithNullByteArray() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        base64InputStream.read(null, 0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayWithNegativeOffset() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[1];
        base64InputStream.read(buffer, -1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayWithNegativeLength() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[1];
        base64InputStream.read(buffer, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayWithNegativeBufferSize() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[-1];
        base64InputStream.read(buffer, 0, buffer.length);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayWithNegativeOffsetLength() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[1];
        base64InputStream.read(buffer, -1, -1);
    }
}