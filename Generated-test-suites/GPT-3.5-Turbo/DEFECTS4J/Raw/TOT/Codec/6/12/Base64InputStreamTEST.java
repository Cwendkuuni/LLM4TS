import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

    private static final byte[] ENCODED_BYTES = new byte[]{72, 101, 108, 108, 111};

    @Test
    public void testRead_singleByte_returnsDecodedBytes() throws IOException {
        // Arrange
        byte[] expectedBytes = new byte[]{'H', 'e', 'l', 'l', 'o'};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(ENCODED_BYTES);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        // Act
        byte[] actualBytes = new byte[expectedBytes.length];
        for (int i = 0; i < expectedBytes.length; i++) {
            int byteValue = base64InputStream.read();
            actualBytes[i] = (byte) byteValue;
        }

        // Assert
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testRead_byteArray_returnsDecodedBytes() throws IOException {
        // Arrange
        byte[] expectedBytes = new byte[]{'H', 'e', 'l', 'l', 'o'};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(ENCODED_BYTES);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        // Act
        byte[] actualBytes = new byte[expectedBytes.length];
        int bytesRead = base64InputStream.read(actualBytes, 0, expectedBytes.length);

        // Assert
        assertEquals(expectedBytes.length, bytesRead);
        assertArrayEquals(expectedBytes, actualBytes);
    }

    @Test
    public void testRead_byteArrayWithOffsetAndLength_returnsDecodedBytes() throws IOException {
        // Arrange
        byte[] expectedBytes = new byte[]{'H', 'e', 'l', 'l', 'o'};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(ENCODED_BYTES);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        // Act
        byte[] actualBytes = new byte[expectedBytes.length + 2];
        int bytesRead = base64InputStream.read(actualBytes, 1, expectedBytes.length);

        // Assert
        assertEquals(expectedBytes.length, bytesRead);
        assertArrayEquals(new byte[]{0, 'H', 'e', 'l', 'l', 'o', 0}, actualBytes);
    }

    @Test
    public void testRead_zeroBytes_returnsZero() throws IOException {
        // Arrange
        ByteArrayInputStream inputStream = new ByteArrayInputStream(ENCODED_BYTES);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        // Act
        int bytesRead = base64InputStream.read(new byte[0]);

        // Assert
        assertEquals(0, bytesRead);
    }

    @Test(expected = NullPointerException.class)
    public void testRead_nullByteArray_throwsNullPointerException() throws IOException {
        // Arrange
        ByteArrayInputStream inputStream = new ByteArrayInputStream(ENCODED_BYTES);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        // Act
        base64InputStream.read(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_negativeOffset_throwsIndexOutOfBoundsException() throws IOException {
        // Arrange
        ByteArrayInputStream inputStream = new ByteArrayInputStream(ENCODED_BYTES);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        // Act
        base64InputStream.read(new byte[5], -1, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_negativeLength_throwsIndexOutOfBoundsException() throws IOException {
        // Arrange
        ByteArrayInputStream inputStream = new ByteArrayInputStream(ENCODED_BYTES);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        // Act
        base64InputStream.read(new byte[5], 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_invalidOffset_throwsIndexOutOfBoundsException() throws IOException {
        // Arrange
        ByteArrayInputStream inputStream = new ByteArrayInputStream(ENCODED_BYTES);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        // Act
        base64InputStream.read(new byte[5], 6, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_invalidOffsetAndLength_throwsIndexOutOfBoundsException() throws IOException {
        // Arrange
        ByteArrayInputStream inputStream = new ByteArrayInputStream(ENCODED_BYTES);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        // Act
        base64InputStream.read(new byte[5], 4, 2);
    }

    // Add more test cases for other methods if required

}