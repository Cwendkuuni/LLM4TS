import org.junit.Assert;
import org.junit.Test;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws Exception {
        // Test with empty input stream
        byte[] buffer = new byte[10];
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        int bytesRead = base64InputStream.read(buffer);
        Assert.assertEquals(-1, bytesRead);

        // Test with input stream containing data
        byte[] inputData = "SGVsbG8gd29ybGQ=".getBytes();
        inputStream = new ByteArrayInputStream(inputData);
        base64InputStream = new Base64InputStream(inputStream, false);
        bytesRead = base64InputStream.read(buffer);
        Assert.assertEquals("Hello world".getBytes().length, bytesRead);
        Assert.assertEquals("Hello world", new String(buffer, 0, bytesRead));

        // Test with input stream containing data when doEncode is true
        inputStream = new ByteArrayInputStream(inputData);
        base64InputStream = new Base64InputStream(inputStream, true);
        bytesRead = base64InputStream.read(buffer);
        Assert.assertEquals(inputData.length, bytesRead);
        Assert.assertArrayEquals(inputData, buffer);

        // Test with invalid input stream
        inputStream = new ByteArrayInputStream(inputData);
        base64InputStream = new Base64InputStream(inputStream, false);
        base64InputStream.close();
        try {
            bytesRead = base64InputStream.read(buffer);
            Assert.fail("Should have thrown an IOException");
        } catch (IOException e) {
        }
    }

    @Test
    public void testReadWithOffsetAndLength() throws Exception {
        byte[] buffer = new byte[10];
        byte[] inputData = "SGVsbG8gd29ybGQ=".getBytes();

        // Test with valid input stream and offset and length within range
        InputStream inputStream = new ByteArrayInputStream(inputData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        int bytesRead = base64InputStream.read(buffer, 2, 6);
        Assert.assertEquals(6, bytesRead);
        Assert.assertEquals(0, buffer[0]);
        Assert.assertEquals(0, buffer[1]);
        Assert.assertEquals("Hello ", new String(buffer, 2, bytesRead));

        // Test with valid input stream and offset and length exceeding buffer size
        inputStream = new ByteArrayInputStream(inputData);
        base64InputStream = new Base64InputStream(inputStream, false);
        bytesRead = base64InputStream.read(buffer, 4, 10);
        Assert.assertEquals(6, bytesRead);
        Assert.assertEquals(0, buffer[0]);
        Assert.assertEquals(0, buffer[1]);
        Assert.assertEquals(0, buffer[2]);
        Assert.assertEquals(0, buffer[3]);
        Assert.assertEquals("Hello ", new String(buffer, 4, bytesRead));

        // Test with invalid input stream and offset and length within range
        inputStream = new ByteArrayInputStream(inputData);
        base64InputStream = new Base64InputStream(inputStream, false);
        base64InputStream.close();
        try {
            bytesRead = base64InputStream.read(buffer, 2, 6);
            Assert.fail("Should have thrown an IOException");
        } catch (IOException e) {
        }
    }

    @Test
    public void testReadWithNullBuffer() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        try {
            base64InputStream.read(null);
            Assert.fail("Should have thrown a NullPointerException");
        } catch (NullPointerException e) {
        }
    }

    @Test
    public void testReadWithNegativeOffset() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        try {
            base64InputStream.read(new byte[10], -1, 5);
            Assert.fail("Should have thrown an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
        }
    }

    @Test
    public void testReadWithNegativeLength() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        try {
            base64InputStream.read(new byte[10], 0, -1);
            Assert.fail("Should have thrown an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
        }
    }

    @Test
    public void testReadWithInvalidRange() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        try {
            base64InputStream.read(new byte[10], 5, 6);
            Assert.fail("Should have thrown an IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
        }
    }

    @Test
    public void testReadWithEmptyInputStreamAndDoEncodeTrue() throws Exception {
        byte[] buffer = new byte[10];
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);
        int bytesRead = base64InputStream.read(buffer);
        Assert.assertEquals(-1, bytesRead);
    }

    @Test
    public void testReadWithEmptyInputStreamAndDoEncodeFalse() throws Exception {
        byte[] buffer = new byte[10];
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        int bytesRead = base64InputStream.read(buffer);
        Assert.assertEquals(-1, bytesRead);
    }

    @Test
    public void testMarkSupported() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(new byte[10]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);
        Assert.assertFalse(base64InputStream.markSupported());
    }
}