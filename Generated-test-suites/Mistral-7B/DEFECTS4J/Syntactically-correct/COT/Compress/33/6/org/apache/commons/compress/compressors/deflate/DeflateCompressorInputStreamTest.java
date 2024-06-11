import org.apache.commons.compress.compressors.deflate.DeflateCompressorInputStream;
import org.apache.commons.compress.compressors.deflate.DeflateParameters;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Inflater;

public class DeflateCompressorInputStreamTest {

    @Test
    public void testConstructorWithInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x78, (byte) 0xDA, (byte) 0x10, (byte) 0x00, (byte) 0x00, (byte) 0x08, (byte) 0x00, (byte) 0xCC, (byte) 0xDE, (byte) 0xAD, (byte) 0xDE, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x58, (byte) 0x44, (byte) 0x33, (byte) 0x22, (byte) 0x11, (byte) 0x00});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        Assert.assertNotNull(deflateCompressorInputStream);
    }

    @Test
    public void testConstructorWithInputStreamAndParameters() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x78, (byte) 0xDA, (byte) 0x10, (byte) 0x00, (byte) 0x00, (byte) 0x08, (byte) 0x00, (byte) 0xCC, (byte) 0xDE, (byte) 0xAD, (byte) 0xDE, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x02, (byte) 0x00, (byte) 0x58, (byte) 0x44, (byte) 0x33, (byte) 0x22, (byte) 0x11, (byte) 0x00});
        DeflateParameters parameters = new DeflateParameters();
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream, parameters);
        Assert.assertNotNull(deflateCompressorInputStream);
    }

    @Test
    public void testReadMethod() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03}), new DeflateParameters());
        byte readByte = 0;
        int count = 0;
        while ((readByte = deflateCompressorInputStream.read()) != -1) {
            outputStream.write(readByte);
            count++;
        }
        Assert.assertEquals(3, count);
        Assert.assertEquals("010203", new String(outputStream.toByteArray()));
    }

    @Test
    public void testReadByteArrayMethod() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03}), new DeflateParameters());
        byte[] buffer = new byte[3];
        int length = deflateCompressorInputStream.read(buffer, 0, buffer.length);
        Assert.assertEquals(3, length);
        for (int i = 0; i < buffer.length; i++) {
            Assert.assertEquals(buffer[i], (byte) (i + 1));
        }
        Assert.assertEquals("010203", new String(buffer));
    }

    @Test
    public void testSkipMethod() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03}), new DeflateParameters());
        long skippedBytes = deflateCompressorInputStream.skip(2);
        Assert.assertEquals(2, skippedBytes);
        Assert.assertEquals("03", new String(outputStream.toByteArray()));
    }

    @Test(expected = IOException.class)
    public void testCloseMethod() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());
        deflateCompressorInputStream.close();
        deflateCompressorInputStream.read();
    }

    @Test(expected = IOException.class)
    public void testReadAfterClosed() throws IOException {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream, new DeflateParameters());
        deflateCompressorInputStream.close();
        deflateCompressorInputStream.read();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLenInReadByteArray() {
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03}), new DeflateParameters());
        byte[] buffer = new byte[3];
        int length = deflateCompressorInputStream.read(buffer, 0, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeLenInRead() {
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(new ByteArrayInputStream(new byte[]{(byte) 0x01, (byte) 0x02, (byte) 0x03}), new DeflateParameters());
        int length = deflateCompressorInputStream.read(-1);
    }
}