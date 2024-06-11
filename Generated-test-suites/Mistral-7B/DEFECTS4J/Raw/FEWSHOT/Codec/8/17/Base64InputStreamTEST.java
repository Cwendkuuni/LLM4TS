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
    public void testBase64InputStreamDecoding() throws IOException {
        String base64 = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=";
        InputStream is = new ByteArrayInputStream(base64.getBytes());
        ByteArrayOutputStream os = new ByteArrayOutputStream();

        Base64InputStream bis = new Base64InputStream(is);
        int read = 0;
        byte[] buffer = new byte[4096];
        while ((read = bis.read(buffer)) != -1) {
            os.write(buffer, 0, read);
        }

        assertEquals("This is a test string", new String(os.toByteArray()));
    }

    @Test
    public void testBase64InputStreamEncoding() throws IOException {
        String source = "This is a test string";
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        InputStream is = new ByteArrayInputStream(source.getBytes());

        Base64InputStream bis = new Base64InputStream(is, true);
        int read = 0;
        byte[] buffer = new byte[4096];
        while ((read = bis.read(buffer)) != -1) {
            bos.write(buffer, 0, read);
        }

        String encodedString = new String(bos.toByteArray());
        assertEquals("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=", encodedString);
    }

    @Test
    public void testBase64InputStreamWithCustomOptions() throws IOException {
        String source = "This is a test string";
        byte[] lineSeparator = new byte[] { '\r', '\n' };
        int lineLength = 50;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        InputStream is = new ByteArrayInputStream(source.getBytes());

        Base64InputStream bis = new Base64InputStream(is, true, lineLength, lineSeparator);
        int read = 0;
        byte[] buffer = new byte[4096];
        while ((read = bis.read(buffer)) != -1) {
            bos.write(buffer, 0, read);
        }

        String encodedString = new String(bos.toByteArray());
        String expectedLineSeparator = new String(lineSeparator);
        int index = encodedString.indexOf(expectedLineSeparator);
        assertTrue(index != -1);
        assertEquals("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=\r\n", encodedString.substring(0, index + expectedLineSeparator.length()));
    }

    @Test
    public void testEmptyBase64InputStream() throws IOException {
        InputStream is = new ByteArrayInputStream(new byte[0]);
        Base64InputStream bis = new Base64InputStream(is, false);
        byte[] buffer = new byte[1024];
        int read = bis.read(buffer, 0, buffer.length);
        assertNull(read);
    }

    @Test(expected = NullPointerException.class)
    public void testNullInputStream() {
        Base64InputStream bis = new Base64InputStream(null, false);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testNegativeOffset() {
        byte[] source = new byte[] { 1, 2, 3, 4 };
        InputStream is = new ByteArrayInputStream(source);
        Base64InputStream bis = new Base64InputStream(is, false);
        byte[] buffer = new byte[1024];
        bis.read(buffer, -1, buffer.length);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testNegativeLength() {
        byte[] source = new byte[] { 1, 2, 3, 4 };
        InputStream is = new ByteArrayInputStream(source);
        Base64InputStream bis = new Base64InputStream(is, false);
        byte[] buffer = new byte[1024];
        bis.read(buffer, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testNegativeBufferSize() {
        byte[] source = new byte[] { 1, 2, 3, 4 };
        InputStream is = new ByteArrayInputStream(source);
        Base64InputStream bis = new Base64InputStream(is, false);
        byte[] buffer = new byte[] { 1, 2 };
        bis.read(buffer);
       bis.read(buffer, -1);
    }

    @Test
    public void testBase64InputStreamWithCustomLineLengthAndLineSeparator() throws IOException {
        String source = "This is a test string";
        byte[] lineSeparator = new byte[] { '\r', '\n' };
        int lineLength = 50;

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        InputStream is = new ByteArrayInputStream(source.getBytes());

        Base64InputStream bis = new Base64InputStream(is, true, lineLength, lineSeparator);
        int read = 0;
        byte[] buffer = new byte[4096];
        while ((read = bis.read(buffer)) != -1) {
            bos.write(buffer, 0, read);
        }

        String encodedString = new String(bos.toByteArray());
        String expectedLineSeparator = new String(lineSeparator);
        int index = encodedString.indexOf(expectedLineSeparator);
        assertTrue(index != -1);
        assertEquals("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=\r\nVG8gZXJyIGlzIGh1bWFuLCBidXQgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmxpY2g=\r\n", encodedString.substring(0, index + expectedLineSeparator.length()));
    }

}