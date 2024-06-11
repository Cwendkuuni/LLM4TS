package org.apache.commons.codec.binary;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Base64InputStreamTest {

    private Base64InputStream base64InputStream;

    private byte[] byteArray;

    @Before
    public void setUp() {
        byteArray = "This is a test string.".getBytes();
        base64InputStream = new Base64InputStream(new ByteArrayInputStream(byteArray));
    }

    @Test
    public void testRead() throws IOException {
        int result = base64InputStream.read();
        assertEquals(84, result); // Check the ASCII value of 'T'
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] buffer = new byte[8];
        int bytesRead = base64InputStream.read(buffer);
        assertEquals(8, bytesRead); // Check if 8 bytes are read
        String decodedString = new String(buffer);
        assertTrue(decodedString.contains("This is")); // Check if the decoded string contains "This is"
    }

    @Test
    public void testReadOffsetLength() throws IOException {
        byte[] buffer = new byte[8];
        int bytesRead = base64InputStream.read(buffer, 2, 5);
        assertEquals(5, bytesRead); // Check if 5 bytes are read
        String decodedString = new String(buffer);
        assertTrue(decodedString.contains(" is")); // Check if the decoded string contains " is"
    }

    @Test(expected = NullPointerException.class)
    public void testReadNullByteArray() throws IOException {
        base64InputStream.read(null);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        byte[] buffer = new byte[8];
        base64InputStream.read(buffer, -1, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLength() throws IOException {
        byte[] buffer = new byte[8];
        base64InputStream.read(buffer, 2, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidBufferSize() throws IOException {
        byte[] buffer = new byte[4];
        base64InputStream.read(buffer);
    }

    @After
    public void tearDown() throws IOException {
        base64InputStream.close();
    }
}