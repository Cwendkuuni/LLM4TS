import org.junit.Test;
import org.apache.commons.codec.binary.Base64InputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] inputBytes = {97, 98, 99, 100, 101}; // "abcde"
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        int result = base64InputStream.read();
        assertEquals(97, result); // "a"

        result = base64InputStream.read();
        assertEquals(98, result); // "b"

        result = base64InputStream.read();
        assertEquals(99, result); // "c"

        result = base64InputStream.read();
        assertEquals(100, result); // "d"

        result = base64InputStream.read();
        assertEquals(101, result); // "e"

        result = base64InputStream.read();
        assertEquals(-1, result); // EOF
    }

    @Test
    public void testRead_withEmptyByteArray() throws IOException {
        byte[] inputBytes = {};
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        int result = base64InputStream.read();
        assertEquals(-1, result); // EOF
    }

    @Test
    public void testRead_withNullByteArray() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(null);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        assertThrows(NullPointerException.class, () -> {
            base64InputStream.read();
        });
    }

    @Test
    public void testRead_withNegativeOffset() {
        byte[] inputBytes = {97, 98, 99, 100, 101}; // "abcde"
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        assertThrows(IndexOutOfBoundsException.class, () -> {
            base64InputStream.read(inputBytes, -1, 5);
        });
    }

    @Test
    public void testRead_withNegativeLength() {
        byte[] inputBytes = {97, 98, 99, 100, 101}; // "abcde"
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        assertThrows(IndexOutOfBoundsException.class, () -> {
            base64InputStream.read(inputBytes, 0, -1);
        });
    }

    @Test
    public void testRead_withInvalidOffset() {
        byte[] inputBytes = {97, 98, 99, 100, 101}; // "abcde"
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        assertThrows(IndexOutOfBoundsException.class, () -> {
            base64InputStream.read(inputBytes, 6, 5);
        });
    }

    @Test
    public void testRead_withInvalidLength() {
        byte[] inputBytes = {97, 98, 99, 100, 101}; // "abcde"
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        assertThrows(IndexOutOfBoundsException.class, () -> {
            base64InputStream.read(inputBytes, 0, 6);
        });
    }

    @Test
    public void testRead_withEncodedData() throws IOException {
        byte[] inputBytes = {97, 98, 99, 100, 101}; // "abcde"
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        int result = base64InputStream.read();
        assertEquals(66, result); // "B" (base64 encoded "a")

        result = base64InputStream.read();
        assertEquals(77, result); // "M" (base64 encoded "b")

        result = base64InputStream.read();
        assertEquals(81, result); // "Q" (base64 encoded "c")

        result = base64InputStream.read();
        assertEquals(100, result); // "d"

        result = base64InputStream.read();
        assertEquals(86, result); // "V" (base64 encoded "e")

        result = base64InputStream.read();
        assertEquals(-1, result); // EOF
    }
}