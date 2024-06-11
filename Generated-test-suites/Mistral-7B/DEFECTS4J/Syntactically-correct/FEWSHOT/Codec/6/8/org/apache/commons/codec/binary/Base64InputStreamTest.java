package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class Base64InputStreamTest {

    @Test
    public void testBase64InputStream_DefaultConstruction() {
        InputStream in = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43, 0x44 });
        Base64InputStream base64is = new Base64InputStream(in);

        byte[] expected = new byte[] { 0x41, 0x42, 0x43, 0x44 };

        byte[] buffer = new byte[4];
        int len;

        try {
            while ((len = base64is.read(buffer)) > 0) {
                assertArrayEquals(expected, Arrays.copyOfRange(buffer, 0, len));
            }
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testBase64InputStream_Decode() {
        InputStream in = new ByteArrayInputStream(new byte[] { (byte) 0x41, (byte) 0x42, (byte) 0x43, (byte) 0x44, (byte) 0x3D, (byte) 0x3D, (byte) 0x3D });
        Base64InputStream base64is = new Base64InputStream(in);

        byte[] expected = new byte[] { 0x41, 0x42, 0x43, 0x44 };

        byte[] buffer = new byte[4];
        int len;

        try {
            while ((len = base64is.read(buffer)) > 0) {
                assertArrayEquals(expected, Arrays.copyOfRange(buffer, 0, len));
            }
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testBase64InputStream_Encode() {
        InputStream in = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43, 0x44 });
        Base64InputStream base64is = new Base64InputStream(in, true);

        byte[] expected = new byte[] { 0x31, 0x32, 0x33, 0x34, 0x3D, 0x3D, 0x3D };

        byte[] buffer = new byte[8];
        int len;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] destination = new byte[10];

        try {
            while ((len = base64is.read(buffer)) > 0) {
                base64is.read(destination, 0, len);
                baos.write(destination, 0, len);
            }

            baos.flush();
            byte[] result = baos.toByteArray();

            assertArrayEquals(expected, result);
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testBase64InputStream_WithLineLength() {
        InputStream in = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43, 0x44 });
        Base64InputStream base64is = new Base64InputStream(in, true, 5, new byte[] { (byte) 0x0D, (byte) 0x0A });

        byte[] expected = new byte[] { 0x31, 0x32, 0x33, 0x34, 0x0D, 0x0A };

        byte[] buffer = new byte[6];
        int len;

        try {
            while ((len = base64is.read(buffer)) > 0) {
                assertArrayEquals(expected, Arrays.copyOfRange(buffer, 0, len));
            }
        } catch (IOException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testBase64InputStream_WithEmptyInput() {
        InputStream in = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64is = new Base64InputStream(in);

        assertNull(base64is.read(new byte[10], 0, 10));
    }

    @Test
    public void testBase64InputStream_MarkSupported() {
        InputStream in = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43, 0x44 });
        Base64InputStream base64is = new Base64InputStream(in);

        assertFalse(base64is.markSupported());
    }
}