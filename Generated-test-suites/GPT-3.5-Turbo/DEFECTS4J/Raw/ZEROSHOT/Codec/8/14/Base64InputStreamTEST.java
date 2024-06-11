import org.junit.Test;
import static org.junit.Assert.*;

public class Base64InputStreamTest {

    @Test
    public void testRead() {
        // Test with empty InputStream
        InputStream emptyInput = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64Input = new Base64InputStream(emptyInput);
        assertEquals(-1, base64Input.read());

        // Test with single byte InputStream
        InputStream singleByteInput = new ByteArrayInputStream(new byte[]{65}); // 'A' in ASCII
        Base64InputStream singleByteBase64Input = new Base64InputStream(singleByteInput);
        assertEquals(0, singleByteBase64Input.read()); // Should read first byte as '0'

        // Test with multiple byte InputStream
        InputStream multipleByteInput = new ByteArrayInputStream(new byte[]{65, 66, 67}); // 'ABC' in ASCII
        Base64InputStream multipleByteBase64Input = new Base64InputStream(multipleByteInput);
        assertEquals(0, multipleByteBase64Input.read());
        assertEquals(0, multipleByteBase64Input.read());
        assertEquals(2, multipleByteBase64Input.read()); // Should read third byte as '2'
    }

    @Test
    public void testRead_byteArray() throws IOException {
        // Test with empty InputStream
        InputStream emptyInput = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64Input = new Base64InputStream(emptyInput);
        byte[] resultArray = new byte[10];
        assertEquals(-1, base64Input.read(resultArray, 0, 10));

        // Test with single byte InputStream
        InputStream singleByteInput = new ByteArrayInputStream(new byte[]{65}); // 'A' in ASCII
        Base64InputStream singleByteBase64Input = new Base64InputStream(singleByteInput);
        byte[] singleByteResultArray = new byte[10];
        assertEquals(1, singleByteBase64Input.read(singleByteResultArray, 0, 10));
        assertEquals(0, singleByteResultArray[0]); // Should read first byte as '0'

        // Test with multiple byte InputStream
        InputStream multipleByteInput = new ByteArrayInputStream(new byte[]{65, 66, 67}); // 'ABC' in ASCII
        Base64InputStream multipleByteBase64Input = new Base64InputStream(multipleByteInput);
        byte[] multipleByteResultArray = new byte[10];
        assertEquals(3, multipleByteBase64Input.read(multipleByteResultArray, 0, 10));
        assertEquals(0, multipleByteResultArray[0]);
        assertEquals(0, multipleByteResultArray[1]);
        assertEquals(2, multipleByteResultArray[2]); // Should read third byte as '2'
    }

    @Test(expected = NullPointerException.class)
    public void testRead_byteArray_NullArray() throws IOException {
        InputStream emptyInput = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64Input = new Base64InputStream(emptyInput);
        base64Input.read(null, 0, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_byteArray_InvalidOffset() throws IOException {
        InputStream emptyInput = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64Input = new Base64InputStream(emptyInput);
        byte[] resultArray = new byte[10];
        base64Input.read(resultArray, -1, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_byteArray_InvalidLength() throws IOException {
        InputStream emptyInput = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64Input = new Base64InputStream(emptyInput);
        byte[] resultArray = new byte[10];
        base64Input.read(resultArray, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_byteArray_InvalidBufferSize() throws IOException {
        InputStream emptyInput = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64Input = new Base64InputStream(emptyInput);
        byte[] resultArray = new byte[10];
        base64Input.read(resultArray, 0, 20);
    } 
}