import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Inflater;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testDeflateCompressorInputStreamConstructorWithInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x78, 0xDA, 0x10, 0x00, 0x08, 0x00, 0x00, 0x0B, (byte) 0xFB, 0x08, 0x00, 0x00, 0x00, 0x02, 0x00, 0x02, 0x00, 0x00});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        Assert.assertNotNull(deflateCompressorInputStream);
    }

    @Test
    public void testDeflateCompressorInputStreamConstructorWithInputStreamAndParameters() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x78, 0xDA, 0x10, 0x00, 0x08, 0x00, 0x00, 0x0B, (byte) 0xFB, 0x08, 0x00, 0x00, 0x00, 0x02, 0x00, 0x02, 0x00, 0x00});
        DeflateParameters deflateParameters = new DeflateParameters();
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream, deflateParameters);
        Assert.assertNotNull(deflateCompressorInputStream);
    }

    @Test(expected = NullPointerException.class)
    public void testDeflateCompressorInputStreamConstructorWithNullInputStream() {
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(null);
    }

    @Test
    public void testReadMethod() throws IOException {
        byte[] compressedData = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
        InputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int readByte;
        while ((readByte = deflateCompressorInputStream.read()) != -1) {
            outputStream.write(readByte);
        }
        byte[] expectedData = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
        Assert.assertArrayEquals(expectedData, outputStream.toByteArray());
    }

    @Test
    public void testReadMethodWithEmptyBuffer() throws IOException {
        byte[] compressedData = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
        InputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[0];
        int length = deflateCompressorInputStream.read(buffer);
        Assert.assertEquals(0, length);
    }

    @Test
    public void testReadMethodWithBuffer() throws IOException {
        byte[] compressedData = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
        InputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[5];
        int length = deflateCompressorInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(5, length);
        byte[] expectedData = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05};
        Assert.assertArrayEquals(expectedData, Arrays.copyOfRange(buffer, 0, length));
    }

    @Test
    public void testSkipMethod() throws IOException {
        byte[] compressedData = new byte[]{0x01, 0x02, 0x03};
        InputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        long skippedBytes = deflateCompressorInputStream.skip(5);
        Assert.assertEquals(3, skippedBytes);
    }

    @Test
    public void testAvailableMethod() throws IOException {
        byte[] compressedData = new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
        InputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        long availableBytes = deflateCompressorInputStream.available();
        Assert.assertEquals(6 - 1, availableBytes); // 1 byte is read when calling available()
    }

    @Test
    public void testCloseMethod() throws IOException {
        byte[] compressedData = new byte[]{0x01, 0x02, 0x03};
        InputStream inputStream = new ByteArrayInputStream(compressedData);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        deflateCompressorInputStream.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeflateCompressorInputStreamConstructorWithNegativeBufferSize() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream, -5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeflateCompressorInputStreamReadWithNullBuffer() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        deflateCompressorInputStream.read(null, 0, 0);
    }

    @Test(expected = IOException.class)
    public void testDeflateCompressorInputStreamReadWithNegativeOffset() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[5];
        deflateCompressorInputStream.read(buffer, -5, 5);
    }

    @Test(expected = IOException.class)
    public void testDeflateCompressorInputStreamReadWithNegativeLength() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[5];
        deflateCompressorInputStream.read(buffer, 0, -5);
    }
}