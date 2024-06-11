import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.binary.Base64InputStream;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

public class Base64InputStreamTest {
    @Test
    public void testRead() throws IOException {
        byte[] inputBytes = {65, 66, 67, 68, 69};
        InputStream input = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(input, true);

        int result = base64InputStream.read();
        assertEquals(65, result);

        result = base64InputStream.read();
        assertEquals(66, result);

        result = base64InputStream.read();
        assertEquals(67, result);

        result = base64InputStream.read();
        assertEquals(68, result);

        result = base64InputStream.read();
        assertEquals(69, result);

        result = base64InputStream.read();
        assertEquals(-1, result);

        base64InputStream.close();
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] inputBytes = {65, 66, 67, 68, 69};
        InputStream input = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(input, true);

        byte[] result = new byte[3];
        int bytesRead = base64InputStream.read(result, 1, 2);
        assertEquals(2, bytesRead);
        assertEquals(0, result[0]);
        assertEquals(65, result[1]);
        assertEquals(66, result[2]);

        bytesRead = base64InputStream.read(result, 0, 3);
        assertEquals(1, bytesRead);
        assertEquals(67, result[0]);
        assertEquals(66, result[1]);
        assertEquals(68, result[2]);

        bytesRead = base64InputStream.read(result, 1, 1);
        assertEquals(1, bytesRead);
        assertEquals(69, result[1]);

        base64InputStream.close();
    }

    @Test
    public void testReadWithInvalidOffsetAndLength() throws IOException {
        byte[] inputBytes = {65, 66, 67, 68, 69};
        InputStream input = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(input, true);

        byte[] result = new byte[3];

        try {
            base64InputStream.read(result, -1, 2);
        } catch (IndexOutOfBoundsException e) {
            assertTrue(e.getMessage().startsWith("offset"));
        }

        try {
            base64InputStream.read(result, 0, -1);
        } catch (IndexOutOfBoundsException e) {
            assertTrue(e.getMessage().startsWith("length"));
        }

        try {
            base64InputStream.read(result, 4, 2);
        } catch (IndexOutOfBoundsException e) {
            assertTrue(e.getMessage().startsWith("offset"));
        }

        try {
            base64InputStream.read(result, 2, 3);
        } catch (IndexOutOfBoundsException e) {
            assertTrue(e.getMessage().startsWith("length"));
        }

        base64InputStream.close();
    }

    @Test
    public void testReadWithZeroLength() throws IOException {
        byte[] inputBytes = {65, 66, 67, 68, 69};
        InputStream input = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(input, true);

        byte[] result = new byte[3];
        int bytesRead = base64InputStream.read(result, 1, 0);
        assertEquals(0, bytesRead);
        assertEquals(0, result[0]);
        assertEquals(0, result[1]);
        assertEquals(0, result[2]);

        base64InputStream.close();
    }

    @Test
    public void testMarkSupported() {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(input, true);

        assertFalse(base64InputStream.markSupported());

        base64InputStream.close();
    }
}