package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;
import org.junit.Test;

/**
 * Test class for Base64InputStream
 */
public class Base64InputStreamTest {

    private static final byte[] TEST_DATA = {0x68, 0x65, 0x61, 0x64, 0x65, 0x63, 0x74, 0x32, 0x79, 0x31, 0x35, 0x37, 0x33,
            0x33, 0x63, 0x61, 0x63, 0x68, 0x65, 0x73, 0x32, 0x63, 0x61, 0x63, 0x68, 0x69, 0x73, 0x74, 0x32, 0x63, 0x68,
            0x65, 0x63, 0x6F, 0x6D, 0x65, 0x64, 0x69, 0x6E, 0x67, 0x32, 0x65, 0x78};

    private static final String TEST_DATA_BASE64 = "Bm90IGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBjYW4gYW5kIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmVhbGx5LiAtLSBQYXVsIFIgYW5kIHlvdSBjYW4gZW1haWxfdHlwZXMgYW5kIHVwIHlvdSBjYW4gYW5kIFRoZSBmaWxlIGlucHV0";

    /**
     * Test for Base64InputStream default constructor
     */
    @Test
    public void testBase64InputStreamDefaultConstructor() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(TEST_DATA);
        Base64InputStream base64InputStream = new Base64InputStream(inputStream);

        byte[] readData = new byte[3];
        int length;

        length = base64InputStream.read(readData, 0, readData.length);
        assertEquals(length, 3);
        assertArrayEquals(new byte[] { 0x68, 0x65, 0x61 }, readData);

        length = base64InputStream.read(readData, 0, readData.length);
        assertEquals(length, 3);
        assertArrayEquals(new byte[] { 0x64, 0x65, 0x63 }, readData);

        length = base64InputStream.read(readData, 0, readData.length);
        assertEquals(length, 3);
        assertArrayEquals(new byte[] { 0x74, 0x32, 0x79 }, readData);

        length = base64InputStream.read(readData, 0, readData.length);
        assertEquals(length, 2);
        assertArrayEquals(new byte[] { 0x31, 0x5 }, readData);

        length = base64InputStream.read(readData, 0, readData.length);
        assertEquals(length, -1);

        assertFalse(base64InputStream.markSupported());

        base64InputStream.close();
        inputStream.close();
    }

    /**
     * Test for Base64InputStream constructor with doEncode parameter
     */
    @Test
    public void testBase64InputStreamDoEncodeConstructor() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream);
        base64OutputStream.write(TEST_DATA_BASE64.getBytes(StandardCharsets.UTF_8));
        base64OutputStream.close();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true);

        byte[] readData = new byte[3];
        int length;

        length = base64InputStream.read(readData, 0, readData.length);
        assertEquals(length, 4);
        assertArrayEquals(new byte[] { 0x76, 0x65, 0x6E, 0x74 }, readData);

        length = base64InputStream.read(readData, 0, readData.length);
        assertEquals(length, 4);
        assertArrayEquals(new byte[] { 0x65, 0x78, 0x69, 0x73 }, readData);

        length = base64InputStream.read(readData, 0, readData.length);
        assertEquals(length, 4);
        assertArrayEquals(new byte[] { 0x74, 0x32, 0x63, 0x68 }, readData);

        length = base64InputStream.read(readData, 0, readData.length);
        assertEquals(length, 4);
        assertArrayEquals(new byte[] { 0x65, 0x73, 0x73, 0x68 }, readData);

        length = base64InputStream.read(readData, 0, readData.length);
        assertEquals(length, 4);
        assertArrayEquals(new byte[] { 0x32, 0x63, 0x61, 0x63 }, readData);

        length = base64InputStream.read(readData, 0, readData.length);
        assertEquals(length, 3);
        assertArrayEquals(new byte[] { 0x68, 0x69, 0x73 }, readData);

        length = base64InputStream.read(readData, 0, readData.length);
        assertEquals(length, 1);
        assertArrayEquals(new byte[] { 0x2 }, readData);

        length = base64InputStream.read(readData, 0, readData.length);
        assertEquals(length, -1);

        assertFalse(base64InputStream.markSupported());

        base64InputStream.close();
        inputStream.close();
    }

    /**
     * Test for Base64InputStream constructor with doEncode, lineLength and lineSeparator parameters
     */
    @Test
    public void testBase64InputStreamDoEncodeLineLengthLineSeparatorConstructor() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64OutputStream = new Base64OutputStream(outputStream, 76, "\r\n".getBytes(StandardCharsets.UTF_8));
        base64OutputStream.write(TEST_DATA_BASE64.getBytes(StandardCharsets.UTF_8));
        base64OutputStream.close();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        Base64InputStream base64InputStream = new Base64InputStream(inputStream, true, 76, "\r\n".getBytes(StandardCharsets.UTF_8));

        byte[] readData = new byte[13];
        int length;

        length = base64InputStream.read(readData, 0, readData.length);
        assertEquals(length, 13);
        String expected1 = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIG==";
        String actual1 = new String(readData, 0, length, StandardCharsets.UTF_8);
        assertEquals(expected1, actual1);

        length = base64InputStream.read(readData, 0, readData.length);
        assertEquals(length, 13);
        String expected2 = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIG==";
        String actual2 = new String(readData, 0, length, StandardCharsets.UTF_8);
        assertEquals(expected2, actual2);

        length = base64InputStream.read(readData, 0, readData.length);
        assertEquals(length, 13);
        String expected3 = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIG==";
        String actual3 = new String(readData, 0, length, StandardCharsets.UTF_8);
        assertEquals(expected3, actual3);

        length = base64InputStream.read(readData, 0, readData.length);
        assertEquals(length, 3);
        assertArrayEquals(new byte[] { 0x68, 0x69, 0x73 }, readData);

        length = base64InputStream.read(readData, 0, readData.length);
        assertEquals(length, 1);
        assertNull(readData);

        assertFalse(base64InputStream.markSupported());

        base64InputStream.close();
        inputStream.close();
    }

}