
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;

public class Base64InputStreamTest {

    private Base64InputStream base64InputStream;

    @Before
    public void setUp() {
        byte[] byteArray = Base64.encodeBase64("This is a test string.".getBytes());
        InputStream inputStream = new ByteArrayInputStream(byteArray);
        base64InputStream = new Base64InputStream(inputStream);
    }

    @Test
    public void testReadSingleByte() {
        try {
            int result = base64InputStream.read();
            assertEquals('T', result);
        } catch (Exception e) {
            fail("Exception caught: " + e.getMessage());
        }
    }

    @Test
    public void testReadByteArray() {
        try {
            byte[] buffer = new byte[4];
            int bytesRead = base64InputStream.read(buffer, 0, buffer.length);
            assertEquals(4, bytesRead);
            assertEquals('T', buffer[0]);
            assertEquals('G', buffer[1]);
            assertEquals('l', buffer[2]);
            assertEquals('0', buffer[3]);
        } catch (Exception e) {
            fail("Exception caught: " + e.getMessage());
        }
    }

    @Test
    public void testReadEmptyByteArray() {
        try {
            byte[] buffer = new byte[0];
            int bytesRead = base64InputStream.read(buffer, 0, buffer.length);
            assertEquals(0, bytesRead);
        } catch (Exception e) {
            fail("Exception caught: " + e.getMessage());
        }
    }

    @Test
    public void testReadByteArrayWithInvalidOffsetsAndLengths() {
        try {
            byte[] buffer = new byte[4];
            // Invalid offset and length
            base64InputStream.read(buffer, -1, 4);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Test passed
        } catch (Exception e) {
            fail("Exception caught: " + e.getMessage());
        }
        try {
            byte[] buffer = new byte[4];
            // Invalid offset and length
            base64InputStream.read(buffer, 0, -4);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Test passed
        } catch (Exception e) {
            fail("Exception caught: " + e.getMessage());
        }
        try {
            byte[] buffer = new byte[4];
            // Invalid offset and length
            base64InputStream.read(buffer, 5, 5);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Test passed
        } catch (Exception e) {
            fail("Exception caught: " + e.getMessage());
        }
    }

    @Test
    public void testReadByteArrayWithNullBuffer() {
        try {
            // Null buffer
            base64InputStream.read(null, 0, 4);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Test passed
        } catch (Exception e) {
            fail("Exception caught: " + e.getMessage());
        }
    }

    @Test
    public void testReadByteArrayWithZeroLength() {
        try {
            byte[] buffer = new byte[4];
            // Zero length
            int bytesRead = base64InputStream.read(buffer, 0, 0);
            assertEquals(0, bytesRead);
        } catch (Exception e) {
            fail("Exception caught: " + e.getMessage());
        }
    }

    @Test
    public void testMarkSupported() {
        assertFalse(base64InputStream.markSupported());
    }

    @Test
    public void testClose() {
        try {
            base64InputStream.close();
        } catch (Exception e) {
            fail("Exception caught: " + e.getMessage());
        }
    }
}

