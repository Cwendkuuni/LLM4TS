
package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class Base64InputStreamTest {
    private Base64InputStream base64InputStream;

    @Before
    public void setup() {
        // Create sample data for testing
        byte[] data = new byte[] { 97, 98, 99, 100 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        base64InputStream = new Base64InputStream(inputStream);
    }

    @Test
    public void testRead() throws IOException {
        assertEquals(97, base64InputStream.read());
        assertEquals(98, base64InputStream.read());
        assertEquals(99, base64InputStream.read());
        assertEquals(100, base64InputStream.read());
    }

    @Test
    public void testReadWithArray() throws IOException {
        byte[] array = new byte[4];
        assertEquals(4, base64InputStream.read(array, 0, 4));
        assertEquals(97, array[0]);
        assertEquals(98, array[1]);
        assertEquals(99, array[2]);
        assertEquals(100, array[3]);
    }

    @Test
    public void testReadWithOffset() throws IOException {
        byte[] array = new byte[4];
        assertEquals(2, base64InputStream.read(array, 1, 2));
        assertEquals(0, array[0]);
        assertEquals(97, array[1]);
        assertEquals(98, array[2]);
        assertEquals(0, array[3]);
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] array = new byte[4];
        assertEquals(2, base64InputStream.read(array, 1, 3));
        assertEquals(0, array[0]);
        assertEquals(97, array[1]);
        assertEquals(98, array[2]);
        assertEquals(0, array[3]);
    }
}
