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
    public void testConstructorWithInputStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        DeflateCompressorInputStream testee = new DeflateCompressorInputStream(inputStream);
        Assert.assertNotNull(testee);
    }

    @Test
    public void testConstructorWithInputStreamAndDeflateParameters() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        DeflateParameters deflateParameters = new DeflateParameters();
        DeflateCompressorInputStream testee = new DeflateCompressorInputStream(inputStream, deflateParameters);
        Assert.assertNotNull(testee);
    }

    @Test
    public void testReadMethod() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x78, 0x5E, 0x06, 0x08, 0x00, 0x00, 0x00, 0x0C});
        DeflateCompressorInputStream testee = new DeflateCompressorInputStream(inputStream);
        int result = testee.read();
        Assert.assertEquals(-1, result);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] expected = new byte[]{0x00, 0x00, 0x00, 0x04};
        testee.read(outputStream.getBuffer(), 0, expected.length);
        Assert.assertArrayEquals(expected, outputStream.toByteArray());
    }

    @Test
    public void testReadMethodWithByteArray() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x78, 0x5E, 0x06, 0x08, 0x00, 0x00, 0x00, 0x0C});
        DeflateCompressorInputStream testee = new DeflateCompressorInputStream(inputStream);
        byte[] buffer = new byte[4];
        int length = testee.read(buffer, 0, buffer.length);
        Assert.assertEquals(length, 4);
        Assert.assertArrayEquals(new byte[]{0x00, 0x00, 0x00, 0x04}, buffer);
    }

    @Test(expected = IOException.class)
    public void testReadMethodWithNegativeLength() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        DeflateCompressorInputStream testee = new DeflateCompressorInputStream(inputStream);
        testee.read(-1);
    }

    @Test
    public void testSkipMethod() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x78, 0x5E, 0x06, 0x08, 0x00, 0x00, 0x00, 0x0C});
        DeflateCompressorInputStream testee = new DeflateCompressorInputStream(inputStream);
        long skipped = testee.skip(5L);
        Assert.assertEquals(3L, skipped);
    }

    @Test
    public void testAvailableMethodWithAvailableData() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{0x78, 0x5E, 0x06, 0x08, 0x00, 0x00, 0x00, 0x0C});
        DeflateCompressorInputStream testee = new DeflateCompressorInputStream(inputStream);
        long available = testee.available();
        Assert.assertEquals(2L, available);
    }

    @Test
    public void testAvailableMethodWithNoData() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        DeflateCompressorInputStream testee = new DeflateCompressorInputStream(inputStream);
        long available = testee.available();
        Assert.assertEquals(0L, available);
    }

    @Test
    public void testCloseMethod() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        DeflateCompressorInputStream testee = new DeflateCompressorInputStream(inputStream);
        testee.close();
    }

    @Test(expected = IOException.class)
    public void testReadMethodWithClosedStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        DeflateCompressorInputStream testee = new DeflateCompressorInputStream(inputStream);
        testee.close();
        int result = testee.read();
    }

    @Test(expected = NullPointerException.class)
    public void testReadMethodWithNullByteArray() {
        DeflateCompressorInputStream testee = new DeflateCompressorInputStream(new ByteArrayInputStream(new byte[0]));
        testee.read(null, 0, 0);
    }
}