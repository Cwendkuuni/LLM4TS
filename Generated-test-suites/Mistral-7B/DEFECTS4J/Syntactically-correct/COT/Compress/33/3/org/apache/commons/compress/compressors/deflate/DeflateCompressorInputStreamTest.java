import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testDeflateCompressorInputStreamConstructorWithInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x78, (byte) 0x49, (byte) 0x50, (byte) 0x5B, (byte) 0x08, (byte) 0x00, (byte) 0x14, (byte) 0x00, (byte) 0x00, (byte) 0x0F, (byte) 0xCB}); // zlib header
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        Assert.assertNotNull(deflateCompressorInputStream);
    }

    @Test
    public void testDeflateCompressorInputStreamConstructorWithInputStreamAndParameters() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x78, (byte) 0x49, (byte) 0x50, (byte) 0x5B, (byte) 0x08, (byte) 0x00, (byte) 0x14, (byte) 0x00, (byte) 0x00, (byte) 0x0F, (byte) 0xCB}); // zlib header
        DeflateParameters deflateParameters = new DeflateParameters();
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream, deflateParameters);
        Assert.assertNotNull(deflateCompressorInputStream);
    }

    @Test
    public void testReadMethod() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03};
        byteArrayOutputStream.write(data);
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        byte readByte = (byte) deflateCompressorInputStream.read();
        Assert.assertEquals(data[0], readByte);
    }

    @Test
    public void testReadMethodReturnsMinusOneWhenEndReached() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03};
        byteArrayOutputStream.write(data);
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        byte readByte = (byte) deflateCompressorInputStream.read();
        Assert.assertEquals(data[0], readByte);
        readByte = (byte) deflateCompressorInputStream.read();
        Assert.assertEquals(data[1], readByte);
        readByte = (byte) deflateCompressorInputStream.read();
        Assert.assertEquals(data[2], readByte);
        readByte = (byte) deflateCompressorInputStream.read(); // should return -1
        Assert.assertEquals(-1, readByte);
    }

    @Test
    public void testReadMethodWithEmptyInputStream() throws IOException {
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(new byte[]{}));
        int read = deflateCompressorInputStream.read();
        Assert.assertEquals(-1, read);
    }

    @Test(expected = IOException.class)
    public void testReadMethodThrowsIOExceptionWhenIOExceptionOccurs() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03};
        byteArrayOutputStream.write(data);
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        InputStream inputStreamThrown = new ByteArrayInputStream(new byte[]{});
        deflateCompressorInputStream.wrapInput(inputStreamThrown);
        int read = deflateCompressorInputStream.read();
    }

    @Test
    public void testReadMethodWithNullByteArray() {
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(new byte[]{}));
        byte[] nullByteArray = null;
        int read = deflateCompressorInputStream.read(nullByteArray, 0, 1);
        Assert.assertNull(nullByteArray);
        Assert.assertEquals(-1, read);
    }

    @Test
    public void testReadMethodWithNegativeOffset() {
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(new byte[]{(byte) 0x01}));
        byte[] byteArray = new byte[1];
        int read = deflateCompressorInputStream.read(byteArray, -1, 1);
        Assert.assertEquals(-1, read);
    }

    @Test
    public void testReadMethodWithNegativeLength() {
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(new byte[]{(byte) 0x01}));
        byte[] byteArray = new byte[1];
        int read = deflateCompressorInputStream.read(byteArray, 0, -1);
        Assert.assertEquals(-1, read);
    }

    @Test
    public void testReadMethodWithZeroLength() {
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(new byte[]{(byte) 0x01}));
        byte[] byteArray = new byte[0];
        int read = deflateCompressorInputStream.read(byteArray, 0, 0);
        Assert.assertEquals(0, read);
    }

    @Test
    public void testReadMethodWithNonZeroLength() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03};
        byteArrayOutputStream.write(data);
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] byteArray = new byte[3];
        int read = deflateCompressorInputStream.read(byteArray, 0, 3);
        Assert.assertArrayEquals(data, byteArray);
    }

    @Test
    public void testSkipMethod() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03};
        byteArrayOutputStream.write(data);
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        long skippedBytes = deflateCompressorInputStream.skip(5);
        Assert.assertEquals(5, skippedBytes);
    }

    @Test
    public void testSkipMethodWithNegativeNumber() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03};
        byteArrayOutputStream.write(data);
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        long skippedBytes = deflateCompressorInputStream.skip(-1);
        Assert.assertEquals(-1, skippedBytes);
    }

    @Test
    public void testAvailableMethod() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03};
        byteArrayOutputStream.write(data);
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        long availableBytes = deflateCompressorInputStream.available();
        Assert.assertEquals(3, availableBytes);
    }

    @Test
    public void testAvailableMethodWithClosedStream() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03};
        byteArrayOutputStream.write(data);
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        deflateCompressorInputStream.close();
        long availableBytes = deflateCompressorInputStream.available();
        Assert.assertEquals(0, availableBytes);
    }

    @Test
    public void testCloseMethod() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03};
        byteArrayOutputStream.write(data);
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        deflateCompressorInputStream.close();
        InputStream inputStreamClosed = deflateCompressorInputStream.getWrappedInputStream();
        Assert.assertFalse(inputStreamClosed.isOpen());
    }

    @Test
    public void testCountMethod() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03};
        byteArrayOutputStream.write(data);
        InputStream inputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        deflateCompressorInputStream.read(new byte[10]);
        int count = deflateCompressorInputStream.getBytesRead();
        Assert.assertEquals(10, count);
    }
}