import org.apache.commons.compress.compressors.CompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testDeflateCompressorInputStreamConstructorWithInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x78, (byte) 0x49, (byte) 0x44, (byte) 0x44, (byte) 0, (byte) 0, (byte) 0, (byte) 0}); // zlib header
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        Assert.assertNotNull(deflateCompressorInputStream);
    }

    @Test
    public void testDeflateCompressorInputStreamConstructorWithInputStreamAndParameters() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x78, (byte) 0x49, (byte) 0x44, (byte) 0x44, (byte) 0, (byte) 0, (byte) 0, (byte) 0}); // zlib header
        DeflateParameters deflateParameters = new DeflateParameters();
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream, deflateParameters);
        Assert.assertNotNull(deflateCompressorInputStream);
    }

    @Test
    public void testReadMethod() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x78, (byte) 0x49, (byte) 0x44, (byte) 0x44, (byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0x00}); // zlib header + 3 bytes of data
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        int readByte = deflateCompressorInputStream.read();
        Assert.assertEquals((int) 0x00, readByte);

        readByte = deflateCompressorInputStream.read();
        Assert.assertEquals((int) 0x03, readByte);

        readByte = deflateCompressorInputStream.read();
        Assert.assertEquals((int) -1, readByte);

        deflateCompressorInputStream.close();
    }

    @Test
    public void testReadMethodWithNullBuffer() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x78, (byte) 0x49, (byte) 0x44, (byte) 0x44, (byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0x00}); // zlib header + 3 bytes of data
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        int readBytes = deflateCompressorInputStream.read(null, 0, 0);
        Assert.assertNull(readBytes);

        deflateCompressorInputStream.close();
    }

    @Test
    public void testReadMethodWithInvalidOffset() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x78, (byte) 0x49, (byte) 0x44, (byte) 0x44, (byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0x00}); // zlib header + 3 bytes of data
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        int readBytes = deflateCompressorInputStream.read(new byte[1][], -1);
        Assert.assertNull(readBytes);

        deflateCompressorInputStream.close();
    }

    @Test
    public void testReadMethodWithInvalidLength() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x78, (byte) 0x49, (byte) 0x44, (byte) 0x44, (byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0x00}); // zlib header + 3 bytes of data
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        int readBytes = deflateCompressorInputStream.read(new byte[1][], 4);
        Assert.assertNull(readBytes);

        deflateCompressorInputStream.close();
    }

    @Test
    public void testSkipMethod() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x78, (byte) 0x49, (byte) 0x44, (byte) 0x44, (byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0x00}); // zlib header + 3 bytes of data
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        long skippedBytes = deflateCompressorInputStream.skip(3L);
        Assert.assertEquals(3, skippedBytes);

        skippedBytes = deflateCompressorInputStream.skip(1L);
        Assert.assertEquals(1, skippedBytes);

        skippedBytes = deflateCompressorInputStream.skip(0L);
        Assert.assertEquals(0, skippedBytes);

        deflateCompressorInputStream.close();
    }

    @Test
    public void testAvailableMethod() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x78, (byte) 0x49, (byte) 0x44, (byte) 0x44, (byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0x00}); // zlib header + 3 bytes of data
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        int availableBytes = deflateCompressorInputStream.available();
        Assert.assertEquals(3, availableBytes);

        deflateCompressorInputStream.skip(3);

        availableBytes = deflateCompressorInputStream.available();
        Assert.assertEquals(0, availableBytes);

        deflateCompressorInputStream.close();
    }

    @Test(expected = IOException.class)
    public void testCloseMethod() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x78, (byte) 0x49, (byte) 0x44, (byte) 0x44, (byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0x00}); // zlib header + 3 bytes of data
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        deflateCompressorInputStream.close();
        deflateCompressorInputStream.read();
    }

    @Test
    public void testCountMethod() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x78, (byte) 0x49, (byte) 0x44, (byte) 0x44, (byte) 0x00, (byte) 0x03, (byte) 0x00, (byte) 0x00}); // zlib header + 3 bytes of data
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        int readBytes = deflateCompressorInputStream.read();
        deflateCompressorInputStream.count(readBytes);

        readBytes = deflateCompressorInputStream.read();
        deflateCompressorInputStream.count(readBytes);

        readBytes = deflateCompressorInputStream.read();
        deflateCompressorInputStream.count(readBytes);

        readBytes = deflateCompressorInputStream.read();
        deflateCompressorInputStream.count(readBytes);

        deflateCompressorInputStream.close();
    }
}