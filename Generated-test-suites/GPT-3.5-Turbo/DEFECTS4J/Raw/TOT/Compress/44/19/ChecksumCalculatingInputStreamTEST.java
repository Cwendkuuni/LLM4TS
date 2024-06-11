import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.CRC32;

public class ChecksumCalculatingInputStreamTest {

    private ChecksumCalculatingInputStream inputStream;
    private CRC32 checksum;

    @Before
    public void setUp() {
        checksum = new CRC32();
        byte[] data = "This is a test".getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
        inputStream = new ChecksumCalculatingInputStream(checksum, byteArrayInputStream);
    }

    @Test
    public void testRead_SingleByte() throws IOException {
        int result = inputStream.read();
        Assert.assertEquals(84, result);
        Assert.assertEquals(1, inputStream.getValue());
    }

    @Test
    public void testRead_ByteArray() throws IOException {
        byte[] buffer = new byte[5];
        int result = inputStream.read(buffer);
        Assert.assertEquals(5, result);
        Assert.assertEquals(5, inputStream.getValue());
    }

    @Test
    public void testRead_ByteArray_Offset_Length() throws IOException {
        byte[] buffer = new byte[10];
        int result = inputStream.read(buffer, 2, 5);
        Assert.assertEquals(5, result);
        Assert.assertEquals(5, inputStream.getValue());
    }

    @Test
    public void testSkip() throws IOException {
        long result = inputStream.skip(5);
        Assert.assertEquals(5, result);
        Assert.assertEquals(5, inputStream.getValue());
    }

    @Test
    public void testSkip_NoDataToSkip() throws IOException {
        long result = inputStream.skip(20);
        Assert.assertEquals(0, result);
        Assert.assertEquals(0, inputStream.getValue());
    }

    @Test
    public void testGetValue() {
        long result = inputStream.getValue();
        Assert.assertEquals(0, result);
    }

    @Test(expected = IOException.class)
    public void testRead_Exception() throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[0]);
        inputStream = new ChecksumCalculatingInputStream(checksum, byteArrayInputStream);
        inputStream.read();
    }
}