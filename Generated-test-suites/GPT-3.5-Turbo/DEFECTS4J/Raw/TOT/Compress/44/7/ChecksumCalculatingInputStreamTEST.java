
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.zip.Adler32;

public class ChecksumCalculatingInputStreamTest {

    private ChecksumCalculatingInputStream inputStream;
    private byte[] testData;

    @Before
    public void setUp() {
        testData = "This is a test data".getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(testData);
        Adler32 adler32 = new Adler32();
        inputStream = new ChecksumCalculatingInputStream(adler32, byteArrayInputStream);
    }

    @Test
    public void testRead_SingleByte_Success() throws IOException {
        int expected = testData[0];
        int actual = inputStream.read();
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(expected, inputStream.getValue());
    }

    @Test
    public void testRead_ByteArray_Success() throws IOException {
        byte[] expectedBytes = new byte[testData.length];
        int bytesRead = inputStream.read(expectedBytes);
        Assert.assertEquals(testData.length, bytesRead);
        Assert.assertArrayEquals(testData, expectedBytes);
        Assert.assertEquals(inputStream.getValue(), 0x198AE99F);
    }

    @Test
    public void testRead_ByteArray_Offset_Success() throws IOException {
        byte[] expectedBytes = new byte[testData.length];
        int bytesRead = inputStream.read(expectedBytes, 5, 10);
        Assert.assertEquals(10, bytesRead);
        Assert.assertArrayEquals(new byte[5], expectedBytes);
        Assert.assertEquals(inputStream.getValue(), 0x198AE99F);
    }

    @Test
    public void testRead_ByteArray_Offset_InvalidInput() throws IOException {
        byte[] expectedBytes = new byte[5];
        int bytesRead = inputStream.read(expectedBytes, 0, 10);
        Assert.assertEquals(5, bytesRead);
        Assert.assertArrayEquals(testData, expectedBytes);
        Assert.assertEquals(inputStream.getValue(), 0x198AE99F);
    }

    @Test
    public void testSkip_Success() throws IOException {
        long bytesSkipped = inputStream.skip(5);
        Assert.assertEquals(5, bytesSkipped);
        Assert.assertEquals(inputStream.getValue(), 0x198AE99F);
    }

    @Test
    public void testSkip_InvalidInput() throws IOException {
        long bytesSkipped = inputStream.skip(-5);
        Assert.assertEquals(0, bytesSkipped);
        Assert.assertEquals(inputStream.getValue(), 0x198AE99F);
    }
}
