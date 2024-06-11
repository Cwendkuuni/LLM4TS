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
    public void testBase64InputStream_decode() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gZXJyIGlzIGh1bWFuIG1heFVuaXphdGlvbnM=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] result = new byte[10];
        int length = base64InputStream.read(result, 0, 10);
        assertEquals(length, 10);
        assertArrayEquals(new byte[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"}, result);
        base64InputStream.close();
    }

    @Test
    public void testBase64InputStream_decode_customLineLengthAndLineSeparator() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gZXJyIGlzIGh1bWFuIG1heFVuaXphdGlvbnM=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false, 48, "\r\n".getBytes());

        byte[] result = new byte[24];
        int length = base64InputStream.read(result, 0, 24);
        assertEquals(length, 24);
        assertArrayEquals(new byte[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "\r", "\n", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"}, result);
        base64InputStream.close();
    }

    @Test
    public void testBase64InputStream_encode() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("ABCDEFGHIJ".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int length = -1;
        byte[] buffer = new byte[1024];
        while ((length = base64InputStream.read(buffer, 0, buffer.length)) != -1) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.close();

        String expected = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggPSAxMjMsIG5vdCBvZiB0byBkYXRlIGlzIGhlYWx0IG5vdCB3aXRoIG1heFVuaXphdGlvbnM=";
        assertEquals(expected, new String(outputStream.toByteArray()));
        base64InputStream.close();
    }

    @Test
    public void testBase64InputStream_encode_customLineLengthAndLineSeparator() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("ABCDEFGHIJ".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 48, "\r\n".getBytes());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int length = -1;
        byte[] buffer = new byte[1024];
        while ((length = base64InputStream.read(buffer, 0, buffer.length)) != -1) {
            outputStream.write(buffer, 0, length);
        }
        outputStream.close();

        String expected = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLVBhdGggPSAxMjMsIG5vdCBvZiB0byBkYXRlIGlzIGhlYWx0IG5vdCB3aXRoIG1heFVuaXphdGlvbnM\r\n";
        assertEquals(expected, new String(outputStream.toByteArray()));
        base64InputStream.close();
    }

    @Test
    public void testBase64InputStream_noData() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        byte[] result = new byte[10];
        int length = base64InputStream.read(result, 0, 10);
        assertNull(length);
        base64InputStream.close();
    }

    @Test(expected = NullPointerException.class)
    public void testBase64InputStream_nullByteArray() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(null, false);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testBase64InputStream_invalidOffsetOrLength() throws IOException {
        InputStream inputStream = new ByteArrayInputStream("VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gZXJyIGlzIGh1bWFuIG1heFVuaXphdGlvbnM=".getBytes());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, false);

        byte[] result = new byte[10];
        int length = base64InputStream.read(result, 15, 5);
    }
}