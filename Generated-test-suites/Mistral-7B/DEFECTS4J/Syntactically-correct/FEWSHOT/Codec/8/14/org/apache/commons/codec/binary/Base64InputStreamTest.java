package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class Base64InputStreamTest {

    @Test
    public void testDefaultConstructor() {
        InputStream input = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gZW1iIG5vdCBzbyBhbiBmaWxlIGNvbXBsZS4=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] target = new byte[2];

        int read = 0;
        while ((read = base64InputStream.read(target, 0, target.length)) != -1) {
            outputStream.write(target, 0, read);
        }

        byte[] expected = new byte[] { 65, 66, 67 };
        assertArrayEquals(expected, outputStream.toByteArray());
    }

    @Test
    public void testEncodeConstructor() throws IOException {
        InputStream input = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gZW1iIG5vdCBzbyBhbiBmaWxlIGNvbXBsZS4=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input, true);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] target = new byte[4];

        int read = 0;
        while ((read = base64InputStream.read(target, 0, target.length)) != -1) {
            outputStream.write(target, 0, read);
        }

        byte[] expected = new byte[] { 83, 78, 67, 65 };
        assertArrayEquals(expected, outputStream.toByteArray());
    }

    @Test
    public void testEncodeConstructorWithLineLength() throws IOException {
        InputStream input = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gZW1iIG5vdCBzbyBhbiBmaWxlIGNvbXBsZS4=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input, true, 50, new byte[] { 13, 10 });

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] target = new byte[5];

        int read = 0;
        while ((read = base64InputStream.read(target, 0, target.length)) != -1) {
            outputStream.write(target, 0, read);
        }

        byte[] expected = new byte[] { 83, 78, 65, 13, 10 };
        assertArrayEquals(expected, outputStream.toByteArray());
    }

    @Test
    public void testDecodeConstructor() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 65, 66, 67, 61, 62, 63 });
        Base64InputStream base64InputStream = new Base64InputStream(input, false);

        byte[] target = new byte[3];

        int read = 0;
        while ((read = base64InputStream.read(target, 0, target.length)) != -1) {
            // Do nothing
        }

        assertEquals("ABC", new String(target, 0, read));
    }

    @Test
    public void testDecodeConstructorWithLineLength() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { 65, 66, 67, 13, 10, 61, 62, 63, 13, 10 });
        Base64InputStream base64InputStream = new Base64InputStream(input, false, 50, new byte[] { 13, 10 });

        byte[] target = new byte[3];

        int read = 0;
        while ((read = base64InputStream.read(target, 0, target.length)) != -1) {
            // Do nothing
        }

        assertEquals("ABC", new String(target, 0, read));
    }

    @Test
    public void testMarkNotSupported() {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(input, false);

        assertNull(base64InputStream.markSupported());
    }
}