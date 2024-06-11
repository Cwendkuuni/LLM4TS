import org.junit.Test;
import org.junit.Assert;

public class Base64InputStreamTest {

    private final byte[] testData = "Hello, World!".getBytes();

    @Test
    public void testRead() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        int result = base64InputStream.read();
        
        Assert.assertEquals('H', result);
    }
    
    @Test
    public void testReadArray() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[5];
        int result = base64InputStream.read(buffer, 0, 5);
        
        byte[] expected = "Hello".getBytes();
        Assert.assertArrayEquals(expected, buffer);
    }
    
    @Test
    public void testReadArrayWithOffset() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[10];
        int result = base64InputStream.read(buffer, 2, 5);
        
        byte[] expected = new byte[10];
        expected[2] = 'H';
        expected[3] = 'e';
        expected[4] = 'l';
        expected[5] = 'l';
        expected[6] = 'o';
        Assert.assertArrayEquals(expected, buffer);
    }
    
    @Test
    public void testMarkSupported() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        boolean result = base64InputStream.markSupported();
        
        Assert.assertFalse(result);
    }
}