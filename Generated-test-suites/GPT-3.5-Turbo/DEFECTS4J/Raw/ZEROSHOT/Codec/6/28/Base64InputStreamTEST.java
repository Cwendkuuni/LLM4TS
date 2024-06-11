import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.codec.binary.Base64InputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    @Test
    public void testReadSingleByte() {
        byte[] inputBytes = new byte[] { 65, 66, 67, 68 }; // ASCII values for A, B, C, D
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        try {
            int result = base64InputStream.read();
            Assert.assertEquals(0, result);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadByteArray() {
        byte[] inputBytes = new byte[] { 65, 66, 67, 68 }; // ASCII values for A, B, C, D
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] outputBytes = new byte[4];
        try {
            int bytesRead = base64InputStream.read(outputBytes, 0, 4);
            Assert.assertEquals(4, bytesRead);
            Assert.assertArrayEquals(inputBytes, outputBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadWithOffsetAndLength() {
        byte[] inputBytes = new byte[] { 65, 66, 67, 68, 69, 70 }; // ASCII values for A, B, C, D, E, F
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] outputBytes = new byte[4];
        try {
            int bytesRead = base64InputStream.read(outputBytes, 1, 3);
            Assert.assertEquals(3, bytesRead);
            Assert.assertArrayEquals(new byte[] { 0, 65, 66, 67 }, outputBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadEmptyByteArray() {
        byte[] inputBytes = new byte[0];
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] outputBytes = new byte[4];
        try {
            int bytesRead = base64InputStream.read(outputBytes, 0, 4);
            Assert.assertEquals(-1, bytesRead);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = NullPointerException.class)
    public void testReadNullByteArray() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(null);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] outputBytes = new byte[4];
        try {
            base64InputStream.read(outputBytes, 0, 4);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() {
        byte[] inputBytes = new byte[] { 65, 66, 67, 68 }; // ASCII values for A, B, C, D
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] outputBytes = new byte[4];
        try {
            base64InputStream.read(outputBytes, -1, 4);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLength() {
        byte[] inputBytes = new byte[] { 65, 66, 67, 68 }; // ASCII values for A, B, C, D
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] outputBytes = new byte[4];
        try {
            base64InputStream.read(outputBytes, 0, -1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidBufferSize() {
        byte[] inputBytes = new byte[] { 65, 66, 67, 68 }; // ASCII values for A, B, C, D
        ByteArrayInputStream inputStream = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] outputBytes = new byte[3];
        try {
            base64InputStream.read(outputBytes, 0, 4);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}