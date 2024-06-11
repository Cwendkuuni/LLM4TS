import org.junit.Test;
import org.junit.Assert;

public class Base64InputStreamTest {

    @Test
    public void testRead() {
        // Test case 1: read a single byte from Base64InputStream
        byte[] input = "Hello".getBytes();
        ByteArrayInputStream inStream = new ByteArrayInputStream(Base64.encode(input));

        Base64InputStream base64Stream = new Base64InputStream(inStream, false);
        int result = base64Stream.read();

        Assert.assertEquals(72, result);
        
        // Test case 2: read multiple bytes from Base64InputStream
        byte[] expectedOutput = "V29ybGQh".getBytes();
        byte[] output = new byte[expectedOutput.length];

        int bytesRead = base64Stream.read(output, 0, output.length);

        Assert.assertEquals(expectedOutput.length, bytesRead);
        Assert.assertArrayEquals(expectedOutput, output);
        Assert.assertEquals(-1, base64Stream.read());
        
        // Test case 3: read zero bytes from Base64InputStream
        output = new byte[0];

        int bytesRead = base64Stream.read(output, 0, output.length);

        Assert.assertEquals(0, bytesRead);
        Assert.assertEquals(-1, base64Stream.read());
    }

    @Test
    public void testMarkSupported() {
        ByteArrayInputStream inStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64Stream = new Base64InputStream(inStream, false);

        boolean result = base64Stream.markSupported();

        Assert.assertEquals(false, result);
    }
}