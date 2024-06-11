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
    public void testBase64InputStream_default() {
        InputStream inputStream = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gZW5jb2RlIGludG8gYWxsIHVwIHlvdSBuZWVkIGEgY2hhcmFjdCB3aGF0IHVwIHlvdSB5b3UsIG15IHN0cmluZyBNYWlsIG9mIHRoZSBmaWxlIHBsYW5ldC4=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] result = new byte[3];

        int bytesRead = base64InputStream.read(result, 0, result.length);
        assertEquals(bytesRead, 3);
        assertArrayEquals(new byte[] { 1, 2, 3 }, result);

        bytesRead = base64InputStream.read();
        assertEquals(bytesRead, -1);
    }

    @Test
    public void testBase64InputStream_decode() {
        InputStream inputStream = new ByteArrayInputStream("SGVsbG8sIHdvcmxkIQ==".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] result = new byte[3];

        int bytesRead = base64InputStream.read(result, 0, result.length);
        assertEquals(bytesRead, 3);
        assertArrayEquals(new byte[] { 'H', 'e', 'l' }, result);

        bytesRead = base64InputStream.read();
        assertEquals(bytesRead, -1);
    }

    @Test
    public void testBase64InputStream_encode() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] data = "Hello World".getBytes();

        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, false);
        base64OutputStream.write(data, 0, data.length);
        base64OutputStream.finish();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        byte[] result = new byte[11];

        int bytesRead = base64InputStream.read(result, 0, result.length);
        assertEquals(bytesRead, 11);
        assertArrayEquals(new byte[] { 'S', 'G', 'V', 's', 'b', 'G', '8s', 'I', 'H', 'd', 'v' }, result);

        bytesRead = base64InputStream.read();
        assertEquals(bytesRead, -1);
    }

    @Test
    public void testBase64InputStream_customLineLengthAndLineSeparator() {
        InputStream inputStream = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gZW5jb2RlIGludG8gYWxsIHVwIHlvdSBuZWVkIGEgY2hhcmFjdCB3aGF0IHVwIHlvdSB5b3UsIG15IHN0cmluZyBNYWlsIG9mIHRoZSBmaWxlIHBsYW5ldC4=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 50, new byte[] { 13, 10 });
        byte[] result = new byte[3];

        int bytesRead = base64InputStream.read(result, 0, result.length);
        assertEquals(bytesRead, 3);
        assertArrayEquals(new byte[] { 1, 2, 3 }, result);

        bytesRead = base64InputStream.read();
        assertEquals(bytesRead, -1);

        byte[] expectedLineBreak = new byte[] { 13, 10 };
        byte[] readData = new byte[11];
        int bytesReadFromLine = base64InputStream.read(readData, 0, readData.length);
        assertEquals(bytesReadFromLine, 11);
        byte[] lineData = new byte[bytesReadFromLine - 3];
        System.arraycopy(readData, 0, lineData, 0, lineData.length);
        assertArrayEquals(new byte[] { 'S', 'G', 'V', 's', 'b', 'G', '8s', 'I', 'H', 'd', 'v' }, lineData);
        assertArrayEquals(expectedLineBreak, new byte[] { readData[bytesReadFromLine - 2], readData[bytesReadFromLine - 1] });
    }

    @Test
    public void testBase64InputStream_empty() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0][0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] result = new byte[1];

        int bytesRead = base64InputStream.read(result, 0, result.length);
        assertEquals(bytesRead, -1);
    }

    @Test(expected = IOException.class)
    public void testBase64InputStream_nullInput() {
        Base64InputStream base64InputStream = new Base64InputStream(null);
    }

    @Test(expected = IOException.class)
    public void testBase64InputStream_noData() {
        InputStream inputStream = new ByteArrayInputStream(new byte[0][0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);
        byte[] result = new byte[1];

        int bytesRead = base64InputStream.read(result, 0, result.length);
        assertEquals(bytesRead, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64InputStream_negativeLineLength() {
        InputStream inputStream = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gZW5jb2RlIGludG8gYWxsIHVwIHlvdSBuZWVkIGEgY2hhcmFjdCB3aGF0IHVwIHlvdSB5b3UsIG15IHN0cmluZyBNYWlsIG9mIHRoZSBmaWxlIHBsYW5ldC4=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, -1, new byte[0][0]);
    }
}