import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class DeflateCompressorInputStreamTest {

    private DeflateCompressorInputStream inflaterInputStream;
    
    @Before
    public void setUp() {
        byte[] data = {120, -100, -77, 110, 13, 0, 4, -44, -95, 14, 32, -32, -61, 10, -92, -126, -14, 96};
        InputStream inputStream = new ByteArrayInputStream(data);
        inflaterInputStream = new DeflateCompressorInputStream(inputStream);
    }
    
    @Test
    public void testRead() throws IOException {
        int value = inflaterInputStream.read();
        Assert.assertEquals(120, value);
    }
    
    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] buffer = new byte[10];
        int bytesRead = inflaterInputStream.read(buffer, 0, 10);
        Assert.assertEquals(10, bytesRead);
    }
    
    @Test
    public void testSkip() throws IOException {
        long skipped = inflaterInputStream.skip(5);
        Assert.assertEquals(5, skipped);
    }
    
    @Test
    public void testAvailable() throws IOException {
        int available = inflaterInputStream.available();
        Assert.assertNotEquals(0, available);
    }
    
    @Test
    public void testClose() throws IOException {
        inflaterInputStream.close();
        Assert.assertTrue(true);
    }
}