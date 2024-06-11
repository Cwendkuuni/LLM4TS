import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Base64InputStreamTest {

    @Test
    public void testRead_singleByte() throws IOException {
        byte[] testData = "SGVsbG8gd29ybGQh".getBytes("UTF-8");
        InputStream is = new ByteArrayInputStream(testData);
        Base64InputStream bis = new Base64InputStream(is, false);

        assertEquals(72, bis.read()); // 'H'
        assertEquals(101, bis.read()); // 'e'
        // ... continue for remaining characters
    }

    @Test
    public void testRead_byteArray() throws IOException {
        byte[] testData = "SGVsbG8gd29ybGQh".getBytes("UTF-8");
        InputStream is = new ByteArrayInputStream(testData);
        Base64InputStream bis = new Base64InputStream(is, false);

        byte[] buffer = new byte[10];
        int bytesRead = bis.read(buffer);

        assertEquals(10, bytesRead);
        assertEquals("SGVsbG8gd2", new String(buffer));
    }

    @Test
    public void testRead_byteArray_offset() throws IOException {
        byte[] testData = "SGVsbG8gd29ybGQh".getBytes("UTF-8");
        InputStream is = new ByteArrayInputStream(testData);
        Base64InputStream bis = new Base64InputStream(is, false);

        byte[] buffer = new byte[10];
        int bytesRead = bis.read(buffer, 2, 5);

        assertEquals(5, bytesRead);
        assertEquals(0, buffer[0]);
        assertEquals(72, buffer[2]); // 'H'
        assertEquals(101, buffer[3]); // 'e'
        assertEquals(111, buffer[7]); // 'o'
    }

    @Test
    public void testRead_byteArray_offset_length() throws IOException {
        byte[] testData = "SGVsbG8gd29ybGQh".getBytes("UTF-8");
        InputStream is = new ByteArrayInputStream(testData);
        Base64InputStream bis = new Base64InputStream(is, false);

        byte[] buffer = new byte[10];
        int bytesRead = bis.read(buffer, 2, 8);

        assertEquals(8, bytesRead);
        assertEquals(0, buffer[0]);
        assertEquals(72, buffer[2]); // 'H'
        assertEquals(111, buffer[9]); // 'o'
    }

    @Test
    public void testRead_eof() throws IOException {
        byte[] testData = "SGVsbG8gd29ybGQh".getBytes("UTF-8");
        InputStream is = new ByteArrayInputStream(testData);
        Base64InputStream bis = new Base64InputStream(is, false);

        byte[] buffer = new byte[20];
        int bytesRead = bis.read(buffer);

        assertEquals(13, bytesRead);
        assertEquals("SGVsbG8gd29ybGQh", new String(buffer, 0, bytesRead, "UTF-8"));

        assertEquals(-1, bis.read());
    }

    @Test
    public void testMarkSupported() {
        byte[] testData = "SGVsbG8gd29ybGQh".getBytes("UTF-8");
        InputStream is = new ByteArrayInputStream(testData);
        Base64InputStream bis = new Base64InputStream(is, false);

        assertFalse(bis.markSupported());
    }

    @Test
    public void testBase64InputStream_Decode() throws IOException {
        byte[] testData = "SGVsbG8gd29ybGQh".getBytes("UTF-8");
        InputStream is = new ByteArrayInputStream(testData);
        Base64InputStream bis = new Base64InputStream(is, false);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[10];
        int bytesRead;
        while ((bytesRead = bis.read(buffer)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }
        bis.close();

        assertEquals("Hello world!", bos.toString("UTF-8"));
    }

    @Test
    public void testBase64InputStream_Decode_multipleReads() throws IOException {
        byte[] testData = "SGVsbG8gd29ybGQh".getBytes("UTF-8");
        InputStream is = new ByteArrayInputStream(testData);
        Base64InputStream bis = new Base64InputStream(is, false);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[5];
        int bytesRead;
        while ((bytesRead = bis.read(buffer)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }
        bis.close();

        assertEquals("Hello world!", bos.toString("UTF-8"));
    }

    @Test
    public void testBase64InputStream_Encode() throws IOException {
        byte[] testData = "Hello world!".getBytes("UTF-8");
        InputStream is = new ByteArrayInputStream(testData);
        Base64InputStream bis = new Base64InputStream(is, true);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[10];
        int bytesRead;
        while ((bytesRead = bis.read(buffer)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }
        bis.close();

        assertEquals("SGVsbG8gd29ybGQh", bos.toString("UTF-8"));
    }

    @Test
    public void testBase64InputStream_Encode_multipleReads() throws IOException {
        byte[] testData = "Hello world!".getBytes("UTF-8");
        InputStream is = new ByteArrayInputStream(testData);
        Base64InputStream bis = new Base64InputStream(is, true);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[5];
        int bytesRead;
        while ((bytesRead = bis.read(buffer)) != -1) {
            bos.write(buffer, 0, bytesRead);
        }
        bis.close();

        assertEquals("SGVsbG8gd29ybGQh", bos.toString("UTF-8"));
    }

}