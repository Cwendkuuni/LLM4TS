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
    public void testConstructorWithInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x78, (byte) 0x49, (byte) 0x4d, (byte) 0x4c, (byte) 0x4b, (byte) 0x05, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0xff});
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        Assert.assertNotNull(deflateCompressorInputStream);
    }

    @Test
    public void testConstructorWithInputStreamAndParameters() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{(byte) 0x78, (byte) 0x49, (byte) 0x4d, (byte) 0x4c, (byte) 0x4b});
        DeflateParameters deflateParameters = new DeflateParameters();
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream, deflateParameters);
        Assert.assertNotNull(deflateCompressorInputStream);
    }

    @Test
    public void testRead() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream inputStream = new ByteArrayOutputStream();
        inputStream.write(new byte[]{(byte) 0x78, (byte) 0x49, (byte) 0x4d, (byte) 0x4c, (byte) 0x4b, (byte) 0x05, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0xff});
        inputStream.flush();

        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[1];
        int read = 0;
        while ((read = deflateCompressorInputStream.read(buffer)) != -1) {
            outputStream.write(buffer);
        }
        outputStream.flush();

        byte[] expected = new byte[]{(byte) 0x78, (byte) 0x49, (byte) 0x4d, (byte) 0x4c, (byte) 0x4b, (byte) 0x05, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0xff};
        Assert.assertArrayEquals(expected, outputStream.toByteArray());
    }

    @Test
    public void testReadWithByteArray() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream inputStream = new ByteArrayOutputStream();
        inputStream.write(new byte[]{(byte) 0x78, (byte) 0x49, (byte) 0x4d, (byte) 0x4c, (byte) 0x4b, (byte) 0x05, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0xff});
        inputStream.flush();

        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[12];
        int read = deflateCompressorInputStream.read(buffer);
        outputStream.write(buffer, 0, read);
        outputStream.flush();

        byte[] expected = new byte[]{(byte) 0x78, (byte) 0x49, (byte) 0x4d, (byte) 0x4c, (byte) 0x4b, (byte) 0x05, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0xff};
        Assert.assertArrayEquals(expected, outputStream.toByteArray());
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream inputStream = new ByteArrayOutputStream();
        inputStream.write(new byte[]{(byte) 0x78, (byte) 0x49, (byte) 0x4d, (byte) 0x4c, (byte) 0x4b, (byte) 0x05, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0xff});
        inputStream.flush();

        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        long skipped = deflateCompressorInputStream.skip(5L);
        Assert.assertEquals(5L, skipped);

        byte[] buffer = new byte[1];
        int read = 0;
        while ((read = deflateCompressorInputStream.read(buffer)) != -1) {
            outputStream.write(buffer);
        }
        outputStream.flush();

        byte[] expected = new byte[]{(byte) 0x4d, (byte) 0x4c, (byte) 0x4b, (byte) 0x05, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0xff};
        Assert.assertArrayEquals(expected, outputStream.toByteArray());
    }

    @Test
    public void testAvailable() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream inputStream = new ByteArrayOutputStream();
        inputStream.write(new byte[]{(byte) 0x78, (byte) 0x49, (byte) 0x4d, (byte) 0x4c, (byte) 0x4b, (byte) 0x05, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0xff});
        inputStream.flush();

        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        long available = deflateCompressorInputStream.available();
        Assert.assertEquals(12L, available);

        byte[] buffer = new byte[12];
        int read = deflateCompressorInputStream.read(buffer);
        Assert.assertEquals(12, read);

        long availableAfterRead = deflateCompressorInputStream.available();
        Assert.assertEquals(0L, availableAfterRead);
    }

    @Test(expected = IOException.class)
    public void testClose() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream inputStream = new ByteArrayOutputStream();
        inputStream.write(new byte[]{(byte) 0x78, (byte) 0x49, (byte) 0x4d, (byte) 0x4c, (byte) 0x4b, (byte) 0x05, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0xff});
        inputStream.flush();

        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        deflateCompressorInputStream.close();

        byte[] buffer = new byte[1];
        int read = deflateCompressorInputStream.read(buffer);
    }

    @Test
    public void testConstructorWithNullInputStream() {
        Assert.assertThrows(NullPointerException.class, () -> new DeflateCompressorInputStream(null));
    }

    @Test
    public void testCountWithNegativeValue() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        deflateCompressorInputStream.count((short) -1);
    }

    @Test
    public void testCountWithNonIntegerValue() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        deflateCompressorInputStream.count((short) 3.5f);
    }

    @Test
    public void testCountWithZeroValue() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        deflateCompressorInputStream.count(0);
    }

    @Test
    public void testCountWithPositiveValue() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        deflateCompressorInputStream.count(1);
    }

    @Test
    public void testCountWithLargeValue() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        deflateCompressorInputStream.count(Integer.MAX_VALUE);
    }

    @Test
    public void testCountWithNegativeByteValue() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        deflateCompressorInputStream.count((byte) -1);
    }

    @Test
    public void testCountWithZeroByteValue() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        deflateCompressorInputStream.count((byte) 0);
    }

    @Test
    public void testCountWithByteValueLargerThanByte() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        deflateCompressorInputStream.count((byte) 128);
    }

    @Test
    public void testCountWithValidByteValues() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        deflateCompressorInputStream.count((byte) 1);
        deflateCompressorInputStream.count((byte) 2);
        deflateCompressorInputStream.count((byte) 127);
    }

    @Test
    public void testCloseWithIOException() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayOutputStream inputStream = new ByteArrayOutputStream();
        inputStream.write(new byte[]{(byte) 0x78, (byte) 0x49, (byte) 0x4d, (byte) 0x4c, (byte) 0x4b, (byte) 0x05, (byte) 0x01, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff, (byte) 0xff});
        inputStream.flush();

        DeflateCompressorInputStream deflateCompressorInputStream = new DeflateCompressorInputStream(inputStream);
        Assert.assertThrows(IOException.class, deflateCompressorInputStream::close);
    }
}