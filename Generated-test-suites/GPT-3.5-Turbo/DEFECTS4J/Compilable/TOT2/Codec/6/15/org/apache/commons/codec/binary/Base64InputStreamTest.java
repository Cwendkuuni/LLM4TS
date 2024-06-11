package org.apache.commons.codec.binary;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws IOException {
        byte[] inputBytes = {65, 66, 67}; // ASCII values for A, B, C
        ByteArrayInputStream in = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(in);

        assertEquals(65, base64InputStream.read()); // A
        assertEquals(66, base64InputStream.read()); // B
        assertEquals(67, base64InputStream.read()); // C
        assertEquals(-1, base64InputStream.read()); // EOF
    }

    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException {
        byte[] inputBytes = {65, 66, 67}; // ASCII values for A, B, C
        ByteArrayInputStream in = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(in);

        byte[] outputBytes = new byte[3];
        assertEquals(3, base64InputStream.read(outputBytes, 0, 3));
        assertArrayEquals(inputBytes, outputBytes);
    }

    @Test(expected = NullPointerException.class)
    public void testReadByteArrayNullArray() throws IOException {
        byte[] inputBytes = {65, 66, 67}; // ASCII values for A, B, C
        ByteArrayInputStream in = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(in);

        base64InputStream.read(null, 0, 3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayInvalidOffset() throws IOException {
        byte[] inputBytes = {65, 66, 67}; // ASCII values for A, B, C
        ByteArrayInputStream in = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(in);

        byte[] outputBytes = new byte[3];
        base64InputStream.read(outputBytes, 4, 3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayInvalidLength() throws IOException {
        byte[] inputBytes = {65, 66, 67}; // ASCII values for A, B, C
        ByteArrayInputStream in = new ByteArrayInputStream(inputBytes);
        Base64InputStream base64InputStream = new Base64InputStream(in);

        byte[] outputBytes = new byte[3];
        base64InputStream.read(outputBytes, 0, 4); // Length greater than output array length
    }

    // Add more test cases for edge cases and error scenarios

}