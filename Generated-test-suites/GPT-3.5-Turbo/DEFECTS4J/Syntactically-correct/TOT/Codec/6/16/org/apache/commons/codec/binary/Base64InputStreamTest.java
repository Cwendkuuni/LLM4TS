import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws Exception {
        // Typical use case
        // Given
        byte[] testData = "Hello, World!".getBytes();
        byte[] expectedData = java.util.Base64.getDecoder().decode(java.util.Base64.getEncoder().encodeToString(testData).getBytes());
        java.io.ByteArrayInputStream in = new java.io.ByteArrayInputStream(expectedData);
        Base64InputStream base64InputStream = new Base64InputStream(in, false);

        // When
        byte[] actualData = new byte[testData.length];
        int readBytes = base64InputStream.read(actualData, 0, testData.length);

        // Then
        assertArrayEquals(expectedData, actualData);
        assertEquals(testData.length, readBytes);

        // Edge case
        // Given
        in = new java.io.ByteArrayInputStream(new byte[0]);
        base64InputStream = new Base64InputStream(in, false);

        // When
        readBytes = base64InputStream.read(actualData, 0, testData.length);

        // Then
        assertEquals(-1, readBytes);

        // Error scenario
        // Given
        in = null;
        base64InputStream = new Base64InputStream(in, false);

        // When
        readBytes = base64InputStream.read(actualData, 0, testData.length);

        // Then
        assertEquals(-1, readBytes);
    }

    @Test
    public void testReadSingleByte() throws Exception {
        // Typical use case
        // Given
        byte testData = 127;
        byte[] expectedData = java.util.Base64.getDecoder().decode(java.util.Base64.getEncoder().encodeToString(new byte[] { testData }).getBytes());
        java.io.ByteArrayInputStream in = new java.io.ByteArrayInputStream(expectedData);
        Base64InputStream base64InputStream = new Base64InputStream(in, false);

        // When
        int readByte = base64InputStream.read();

        // Then
        assertEquals(testData, (byte) readByte);

        // Edge case
        // Given
        in = new java.io.ByteArrayInputStream(new byte[0]);
        base64InputStream = new Base64InputStream(in, false);

        // When
        readByte = base64InputStream.read();

        // Then
        assertEquals(-1, readByte);
    }

    @Test
    public void testMarkSupported() throws Exception {
        // Given
        java.io.ByteArrayInputStream in = new java.io.ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(in, false);

        // When
        boolean markSupported = base64InputStream.markSupported();

        // Then
        assertFalse(markSupported);
    }

}