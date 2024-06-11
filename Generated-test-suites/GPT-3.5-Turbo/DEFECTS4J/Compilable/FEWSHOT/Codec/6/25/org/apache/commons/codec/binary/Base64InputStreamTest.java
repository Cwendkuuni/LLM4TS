package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.junit.Test;

public class Base64InputStreamTest {

    @Test
    public void testRead() throws Exception {
        byte[] input = {-1, 0, 1, 2, 3};
        InputStream in = new ByteArrayInputStream(input);
        Base64InputStream base64Input = new Base64InputStream(in, false);

        assertEquals(255, base64Input.read());
        assertEquals(0, base64Input.read());
        assertEquals(1, base64Input.read());
        assertEquals(2, base64Input.read());
        assertEquals(3, base64Input.read());
        assertEquals(-1, base64Input.read());

        base64Input.close();
    }

    @Test
    public void testReadWithOffsetAndLength() throws Exception {
        byte[] input = {-1, 0, 1, 2, 3};
        byte[] output = {-1, 0, 1, 2, 3};
        InputStream in = new ByteArrayInputStream(input);
        Base64InputStream base64Input = new Base64InputStream(in, false);
        byte[] buffer = new byte[5];

        assertEquals(5, base64Input.read(buffer, 0, 5));
        for (int i = 0; i < 5; i++) {
            assertEquals(output[i], buffer[i]);
        }
        assertEquals(-1, base64Input.read(buffer, 0, 5));

        base64Input.close();
    }

    @Test
    public void testReadWithInvalidOffsetAndLength() throws Exception {
        byte[] input = {-1, 0, 1, 2, 3};
        InputStream in = new ByteArrayInputStream(input);
        Base64InputStream base64Input = new Base64InputStream(in, false);
        byte[] buffer = new byte[5];

        try {
            base64Input.read(buffer, -1, 5);
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
        try {
            base64Input.read(buffer, 0, -1);
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
        try {
            base64Input.read(buffer, 10, 5);
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }

        base64Input.close();
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullBuffer() throws Exception {
        byte[] input = {-1, 0, 1, 2, 3};
        InputStream in = new ByteArrayInputStream(input);
        Base64InputStream base64Input = new Base64InputStream(in, false);
        byte[] buffer = null;

        base64Input.read(buffer, 0, 5);

        base64Input.close();
    }

    @Test
    public void testMarkSupported() throws Exception {
        byte[] input = {-1, 0, 1, 2, 3};
        InputStream in = new ByteArrayInputStream(input);
        Base64InputStream base64Input = new Base64InputStream(in, false);

        assertEquals(false, base64Input.markSupported());

        base64Input.close();
    }
}