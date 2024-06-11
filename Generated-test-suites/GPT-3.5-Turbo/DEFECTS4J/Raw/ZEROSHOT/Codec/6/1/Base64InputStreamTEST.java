import org.junit.Test;

import static org.junit.Assert.*;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws Exception {
        // Given
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // When
        int result = base64InputStream.read();

        // Then
        assertEquals(116, result);
    }

    @Test
    public void testReadWithByteArray() throws Exception {
        // Given
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[4];

        // When
        int result = base64InputStream.read(buffer, 0, buffer.length);

        // Then
        assertEquals(4, result);
        assertArrayEquals("test".getBytes(), buffer);
    }

    @Test
    public void testReadWithEmptyByteArray() throws Exception {
        // Given
        InputStream inputStream = new ByteArrayInputStream("".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[0];

        // When
        int result = base64InputStream.read(buffer, 0, buffer.length);

        // Then
        assertEquals(0, result);
    }

    @Test
    public void testReadWithNullByteArray() throws Exception {
        // Given
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = null;

        // When
        try {
            base64InputStream.read(buffer, 0, 4);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Then
            assertEquals("Buffer cannot be null", e.getMessage());
        }
    }

    @Test
    public void testReadWithNegativeOffset() throws Exception {
        // Given
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[4];

        // When
        try {
            base64InputStream.read(buffer, -1, buffer.length);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Then
            assertEquals("Offset cannot be negative", e.getMessage());
        }
    }

    @Test
    public void testReadWithNegativeLength() throws Exception {
        // Given
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[4];

        // When
        try {
            base64InputStream.read(buffer, 0, -1);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Then
            assertEquals("Length cannot be negative", e.getMessage());
        }
    }

    @Test
    public void testReadWithInvalidOffset() throws Exception {
        // Given
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[4];

        // When
        try {
            base64InputStream.read(buffer, 5, buffer.length);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Then
            assertEquals("Offset is outside the bounds of the buffer", e.getMessage());
        }
    }

    @Test
    public void testReadWithInvalidLength() throws Exception {
        // Given
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] buffer = new byte[4];

        // When
        try {
            base64InputStream.read(buffer, 0, 5);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            // Then
            assertEquals("Length is longer than the buffer", e.getMessage());
        }
    }

    @Test
    public void testMarkSupported() throws Exception {
        // Given
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        // When
        boolean result = base64InputStream.markSupported();

        // Then
        assertFalse(result);
    }
}