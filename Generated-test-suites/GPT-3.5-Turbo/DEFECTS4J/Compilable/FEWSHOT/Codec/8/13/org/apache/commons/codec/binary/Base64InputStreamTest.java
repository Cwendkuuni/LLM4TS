
package org.apache.commons.codec.binary;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

public class Base64InputStreamTest {

    private Base64InputStream base64InputStream;

    @Before
    public void setup() {
        String testString = "SGVsbG8gV29ybGQh";
        InputStream inputStream = new ByteArrayInputStream(testString.getBytes());
        base64InputStream = new Base64InputStream(inputStream);
    }

    @Test
    public void testRead() throws IOException {
        int expected = 'H';
        int actual = base64InputStream.read();
        assertEquals(expected, actual);
    }

    @Test
    public void testReadByteArray() throws IOException {
        byte[] expected = "Hello World!".getBytes();
        byte[] actual = new byte[expected.length];
        base64InputStream.read(actual);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testReadByteArrayWithOffset() throws IOException {
        byte[] expected = "ello".getBytes();
        byte[] actual = new byte[expected.length];
        base64InputStream.read(actual, 1, 4);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testReadByteArrayWithOffsetAndLength() throws IOException {
        byte[] expected = "ello W".getBytes();
        byte[] actual = new byte[expected.length];
        base64InputStream.read(actual, 1, 6);
        assertArrayEquals(expected, actual);
    }
}
