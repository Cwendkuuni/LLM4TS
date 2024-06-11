package org.apache.commons.codec.binary;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class Base64InputStreamTest {

    @Test
    public void testReadSingleByte() {
        byte[] testData = {65, 66, 67}; // Corresponds to 'ABC'
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        try {
            assertEquals(65, base64InputStream.read()); // A
            assertEquals(66, base64InputStream.read()); // B
            assertEquals(67, base64InputStream.read()); // C
            assertEquals(-1, base64InputStream.read()); // EOF check
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testReadByteArray() {
        byte[] testData = {65, 66, 67}; // Corresponds to 'ABC'
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] outputData = new byte[3];

        try {
            assertEquals(3, base64InputStream.read(outputData, 0, 3)); // read all data
            assertArrayEquals(testData, outputData); // check if data was read correctly
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testReadWithInvalidArguments() {
        byte[] testData = {65, 66, 67}; // Corresponds to 'ABC'
        ByteArrayInputStream inputStream = new ByteArrayInputStream(testData);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] outputData = new byte[3];

        try {
            assertThrows(NullPointerException.class, () -> base64InputStream.read(null, 0, 3)); // null buffer
            assertThrows(IndexOutOfBoundsException.class, () -> base64InputStream.read(outputData, -1, 3)); // negative offset
            assertThrows(IndexOutOfBoundsException.class, () -> base64InputStream.read(outputData, 0, 4)); // invalid buffer size
        } catch (Exception e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testMarkSupported() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        assertFalse(base64InputStream.markSupported());
    }
}