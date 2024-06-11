
import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

    @Test
    public void testRead_singleByte() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        int expectedResult = 't';
        int actualResult = base64InputStream.read();

        assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testRead_byteArray() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("test".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] expectedResult = "test".getBytes();
        byte[] actualResult = new byte[4];
        int bytesRead = base64InputStream.read(actualResult, 0, 4);

        assertEquals(4, bytesRead);
        assertArrayEquals(expectedResult, actualResult);
    }
}
