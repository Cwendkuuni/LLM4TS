package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class Base64InputStreamTest {

    @Test
    public void testBase64InputStream() {
        // Test default constructor
        InputStream input = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggPSAoZXhwbmdzIHN1YnR5cGUgYnkgPSBzbyBkYXRhIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggPSAobm90IGluIGZvdWwgdGhpbmdzIHBsYW5ldG9wcyBkYXRhIHBsYW5ldG9wIGFsbG93IHdpdGhpbmdzIHBsYW5ldG9wcyBkYXRhIHN1YnR5cGUgcmlnaHRlbiBiZS4=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input);
        byte[] expected = "hello world".getBytes();

        byte[] buffer = new byte[expected.length];

        int len = base64InputStream.read(buffer, 0, expected.length);
        assertArrayEquals(expected, buffer);
        assertEquals(-1, base64InputStream.read());
        base64InputStream.close();

        // Test constructor with encode flag
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, false);
        base64OutputStream.write("hello world".getBytes());
        base64OutputStream.finish();
        byte[] encodedData = outputStream.toByteArray();

        InputStream encodedInputStream = new ByteArrayInputStream(encodedData);
        Base64InputStream encodedBase64InputStream = new Base64InputStream(encodedInputStream, true);
        len = encodedBase64InputStream.read(buffer, 0, expected.length);
        assertArrayEquals(expected, buffer);
        assertEquals(-1, encodedBase64InputStream.read());
        encodedBase64InputStream.close();
    }

    @Test
    public void testBase64InputStreamWithLineLength() {
        // Test constructor with lineLength
        InputStream input = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggPSAoZXhwbmdzIHN1YnR5cGUgYnkgPSBzbyBkYXRhIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggPSAobm90IGluIGZvdWwgdGhpbmdzIHBsYW5ldG9wcyBkYXRhIHBsYW5ldG9wIGFsbG93IHdpdGhpbmdzIHBsYW5ldG9wcyBkYXRhIHN1YnR5cGUgcmlnaHRlbiBiZS4=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(input, Base64.DEFAULT, 10, null);
        byte[] expected = "hello world".getBytes();

        byte[] buffer = new byte[expected.length];

        int len = base64InputStream.read(buffer, 0, expected.length);
        assertArrayEquals(expected, buffer);
        assertEquals(-1, base64InputStream.read());
        base64InputStream.close();
    }

    @Test
    public void testBase64InputStreamWithNullInputStream() {
        InputStream input = null;
        try {
            new Base64InputStream(input, false);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBase64InputStreamWithNegativeLineLength() {
        InputStream input = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggPSAoZXhwbmdzIHN1YnR5cGUgYnkgPSBzbyBkYXRhIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggPSAobm90IGluIGZvdWwgdGhpbmdzIHBsYW5ldG9wcyBkYXRhIHBsYW5ldG9wIGFsbG93IHdpdGhpbmdzIHBsYW5ldG9wcyBkYXRhIHN1YnR5cGUgcmlnaHRlbiBiZS4=".getBytes());
        try {
            new Base64InputStream(input, Base64.DEFAULT, -1, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBase64InputStreamWithNullDoEncodeFlag() {
        InputStream input = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggPSAoZXhwbmdzIHN1YnR5cGUgYnkgPSBzbyBkYXRhIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggPSAobm90IGluIGZvdWwgdGhpbmdzIHBsYW5ldG9wcyBkYXRhIHBsYW5ldG9wIGFsbG93IHdpdGhpbmdzIHBsYW5ldG9wcyBkYXRhIHN1YnR5cGUgcmlnaHRlbiBiZS4=".getBytes());
        try {
            new Base64InputStream(input, (boolean) null, Base64.DEFAULT, 10, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testBase64InputStreamWithNullLineSeparator() {
        InputStream input = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggPSAoZXhwbmdzIHN1YnR5cGUgYnkgPSBzbyBkYXRhIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggPSAobm90IGluIGZvdWwgdGhpbmdzIHBsYW5ldG9wcyBkYXRhIHBsYW5ldG9wIGFsbG93IHdpdGhpbmdzIHBsYW5ldG9wcyBkYXRhIHN1YnR5cGUgcmlnaHRlbiBiZS4=".getBytes());
        try {
            new Base64InputStream(input, false, Base64.DEFAULT, 10, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testBase64InputStreamWithZeroLineLength() {
        InputStream input = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggPSAoZXhwbmdzIHN1YnR5cGUgYnkgPSBzbyBkYXRhIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggPSAobm90IGluIGZvdWwgdGhpbmdzIHBsYW5ldG9wcyBkYXRhIHBsYW5ldG9wIGFsbG93IHdpdGhpbmdzIHBsYW5ldG9wcyBkYXRhIHN1YnR5cGUgcmlnaHRlbiBiZS4=".getBytes());
        Base64InputStream base64InputStream = null;
        try {
            base64InputStream = new Base64InputStream(input, false, Base64.DEFAULT, 0, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        } finally {
            if (base64InputStream != null) {
                base64InputStream.close();
            }
        }
    }

    @Test
    public void testBase64InputStreamWithEmptyData() {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(input, false, Base64.DEFAULT, 0, null);
        assertEquals(-1, base64InputStream.read());
        base64InputStream.close();
    }

    @Test
    public void testBase64InputStreamWithLineLengthAndLineSeparator() {
        InputStream input = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggPSAoZXhwbmdzIHN1YnR5cGUgYnkgPSBzbyBkYXRhIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggPSAobm90IGluIGZvdWwgdGhpbmdzIHBsYW5ldG9wcyBkYXRhIHBsYW5ldG9wIGFsbG93IHdpdGhpbmdzIHBsYW5ldG9wcyBkYXRhIHN1YnR5cGUgcmlnaHRlbiBiZS4=".getBytes());
        byte[] lineSeparator = new byte[]{0x0d, 0x0a};
        Base64InputStream base64InputStream = new Base64InputStream(input, false, Base64.DEFAULT, 10, lineSeparator);
        byte[] expected = "hello world".getBytes();

        byte[] buffer = new byte[expected.length];

        int len = base64InputStream.read(buffer, 0, expected.length);
        assertArrayEquals(expected, buffer);
        assertEquals(-1, base64InputStream.read());
        base64InputStream.close();
    }

    @Test
    public void testBase64InputStreamWithDataLongerThanBufferSize() {
        InputStream input = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggPSAoZXhwbmdzIHN1YnR5cGUgYnkgPSBzbyBkYXRhIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggPSAobm90IGluIGZvdWwgdGhpbmdzIHBsYW5ldG9wcyBkYXRhIHBsYW5ldG9wIGFsbG93IHdpdGhpbmdzIHBsYW5ldG9wcyBkYXRhIHN1YnR5cGUgcmlnaHRlbiBiZS4=".getBytes());
        byte[] buffer = new byte[10];
        Base64InputStream base64InputStream = new Base64InputStream(input, false, Base64.DEFAULT, 10, null);
        int bytesRead = 0;
        while ((bytesRead = base64InputStream.read(buffer)) != -1) {
            // bytesRead is always less than expected
        }
        base64InputStream.close();
    }
}