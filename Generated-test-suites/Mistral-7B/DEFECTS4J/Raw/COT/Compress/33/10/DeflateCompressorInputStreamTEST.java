import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testDeflateCompressorInputStreamConstruction() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        Assert.assertNotNull(deflateCompressorInputStream);
    }

    @Test
    public void testDeflateCompressorInputStreamWithZlibHeaderConstruction() {
        byte[] zlibHeader = new byte[2];
        zlibHeader[0] = (byte) 0x78;
        zlibHeader[1] = (byte) 0x5E;

        InputStream inputStream = new ByteArrayInputStream(new byte[][]{zlibHeader, new byte[0]});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());
        Assert.assertNotNull(deflateCompressorInputStream);
    }

    @Test(expected = IOException.class)
    public void testDeflateCompressorInputStreamWithoutInputStream() {
        new DeflateCompressorInputStream(null);
    }

    @Test
    public void testReadMethod() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(new byte[]{0x01});
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        int result = deflateCompressorInputStream.read();
        Assert.assertEquals((int) 0x01, result);
    }

    @Test
    public void testReadMethodWithEmptyBuffer() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(new byte[]{0x01});
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        byte[] buffer = new byte[0];
        int result = deflateCompressorInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testReadMethodWithValidBuffer() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(new byte[]{0x01, 0x02});
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        byte[] buffer = new byte[2];
        int result = deflateCompressorInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(2, result);
        Assert.assertArrayEquals(new byte[]{0x01, 0x02}, buffer);
    }

    @Test
    public void testSkipMethod() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05});
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        long result = deflateCompressorInputStream.skip(2L);
        Assert.assertEquals(2, result);
    }

    @Test
    public void testAvailableMethod() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05});
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        long result = deflateCompressorInputStream.available();
        Assert.assertEquals(5, result);
    }

    @Test
    public void testCloseMethod() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05});
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);

        deflateCompressorInputStream.close();

        byte[] buffer = new byte[5];
        int result = deflateCompressorInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(-1, result);
    }
}