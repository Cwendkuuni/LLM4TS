import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws Exception {
        byte[] inputBytes = "Hello World!".getBytes();
        byte[] expectedBytes = {72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100, 33};

        ByteArrayInputStream in = new ByteArrayInputStream(Base64.encodeBase64(inputBytes));
        Base64InputStream base64InputStream = new Base64InputStream(in);

        byte[] outputBytes = new byte[inputBytes.length];

        int bytesRead = base64InputStream.read(outputBytes, 0, outputBytes.length);

        assertNotNull(outputBytes);
        assertEquals(inputBytes.length, bytesRead);
        assertArrayEquals(expectedBytes, outputBytes);

        base64InputStream.close();
    }

    @Test
    public void testReadWithOffset() throws Exception {
        byte[] inputBytes = "Hello World!".getBytes();
        byte[] expectedBytes = {0, 0, 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100, 33};

        ByteArrayInputStream in = new ByteArrayInputStream(Base64.encodeBase64(inputBytes));
        Base64InputStream base64InputStream = new Base64InputStream(in);

        byte[] outputBytes = new byte[inputBytes.length + 2];

        int bytesRead = base64InputStream.read(outputBytes, 2, outputBytes.length - 2);

        assertNotNull(outputBytes);
        assertEquals(inputBytes.length, bytesRead);
        assertArrayEquals(expectedBytes, outputBytes);

        base64InputStream.close();
    }

    @Test
    public void testReadWithInvalidOffset() throws Exception {
        byte[] inputBytes = "Hello World!".getBytes();

        ByteArrayInputStream in = new ByteArrayInputStream(Base64.encodeBase64(inputBytes));
        Base64InputStream base64InputStream = new Base64InputStream(in);

        byte[] outputBytes = new byte[inputBytes.length + 2];

        try {
            base64InputStream.read(outputBytes, 10, outputBytes.length - 10);
            fail("Should have thrown IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("IndexOutOfBoundsException occurred", e.getMessage());
        }

        base64InputStream.close();
    }

    @Test
    public void testReadWithZeroLength() throws Exception {
        byte[] inputBytes = "Hello World!".getBytes();

        ByteArrayInputStream in = new ByteArrayInputStream(Base64.encodeBase64(inputBytes));
        Base64InputStream base64InputStream = new Base64InputStream(in);

        byte[] outputBytes = new byte[inputBytes.length];

        int bytesRead = base64InputStream.read(outputBytes, 0, 0);

        assertNotNull(outputBytes);
        assertEquals(0, bytesRead);

        base64InputStream.close();
    }

    @Test
    public void testReadWithNullByteArray() throws Exception {
        byte[] inputBytes = "Hello World!".getBytes();

        ByteArrayInputStream in = new ByteArrayInputStream(Base64.encodeBase64(inputBytes));
        Base64InputStream base64InputStream = new Base64InputStream(in);

        try {
            base64InputStream.read(null, 0, inputBytes.length);
            fail("Should have thrown NullPointerException");
        } catch (NullPointerException e) {
            assertEquals("NullPointerException occurred", e.getMessage());
        }

        base64InputStream.close();
    }

    @Test
    public void testReadWithInvalidByteArraySize() throws Exception {
        byte[] inputBytes = "Hello World!".getBytes();

        ByteArrayInputStream in = new ByteArrayInputStream(Base64.encodeBase64(inputBytes));
        Base64InputStream base64InputStream = new Base64InputStream(in);

        byte[] outputBytes = new byte[inputBytes.length - 2];

        try {
            base64InputStream.read(outputBytes, 0, outputBytes.length);
            fail("Should have thrown IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("IndexOutOfBoundsException occurred", e.getMessage());
        }

        base64InputStream.close();
    }

    @Test
    public void testReadWithInputStreamException() throws Exception {
        byte[] inputBytes = "Hello World!".getBytes();

        ByteArrayInputStream in = new ByteArrayInputStream(Base64.encodeBase64(inputBytes));
        Base64InputStream base64InputStream = new Base64InputStream(in);

        // Close the input stream to trigger an IOException
        in.close();

        byte[] outputBytes = new byte[inputBytes.length];

        try {
            base64InputStream.read(outputBytes, 0, outputBytes.length);
            fail("Should have thrown IOException");
        } catch (IOException e) {
            assertEquals("IOException occurred", e.getMessage());
        }

        base64InputStream.close();
    }

    @Test
    public void testMarkSupported() {
        ByteArrayInputStream in = new ByteArrayInputStream(Base64.encodeBase64("Hello World!".getBytes()));
        Base64InputStream base64InputStream = new Base64InputStream(in);

        boolean markSupported = base64InputStream.markSupported();

        assertFalse(markSupported);

        base64InputStream.close();
    }
}