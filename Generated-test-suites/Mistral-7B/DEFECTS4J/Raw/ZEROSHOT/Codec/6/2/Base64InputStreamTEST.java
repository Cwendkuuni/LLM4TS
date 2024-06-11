package org.apache.commons.codec.binary;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

public class Base64InputStreamTest {

    private InputStream inputStream;
    private InputStream base64InputStream;
    private byte[] inputBytes;

    @Before
    public void setUp() throws IOException {
        String inputString = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY29tcHV0ZXIuCiAgICAtLSBQYXVsIFIuIEVocmVhbGx5IEFJIGZvdWwgdG8gYW5kIHlvdSBuZWZo";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Base64OutputStream base64Output = new Base64OutputStream(outputStream, false);
        base64Output.write(inputString.getBytes());
        base64Output.finish();
        outputStream.close();
        inputBytes = outputStream.toByteArray();
        inputStream = new ByteArrayInputStream(inputBytes);
        base64InputStream = new Base64InputStream(inputStream, false); // default decoding
    }

    @After
    public void tearDown() throws IOException {
        inputStream.close();
        base64InputStream.close();
    }

    @Test
    public void testDefaultDecoding() throws IOException {
        byte[] outputBytes = new byte[inputBytes.length];
        int length = base64InputStream.read(outputBytes);
        assertArrayEquals(inputBytes, outputBytes, 0, inputBytes.length);
        assertEquals(inputBytes.length, length);
    }

    @Test
    public void testCustomEncoding() throws IOException {
        inputStream = new ByteArrayInputStream(inputBytes);
        base64InputStream = new Base64InputStream(inputStream, true); // custom encoding
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] outputBytes = new byte[inputBytes.length];
        int length;

        while ((length = base64InputStream.read(outputBytes)) != -1) {
            outputStream.write(outputBytes, 0, length);
        }

        outputStream.close();
        byte[] decodedBytes = outputStream.toByteArray();
        byte[] expectedBytes = new byte[]{"V".toString().getBytes(), "G".toString().getBytes(), "8".toString().getBytes(), "g".toString().getBytes(), "Z".toString().getBytes(), "X".toString().getBytes(), "J".toString().getBytes(), "y".toString().getBytes(), "I".toString().getBytes(), "G".toString().getBytes(), "l".toString().getBytes(), "z".toString().getBytes(), "I".toString().getBytes(), "G".toString().getBytes(), "h".toString().getBytes(), "1".toString().getBytes(), "b".toString().getBytes(), "W".toString().getBytes(), "F".toString().getBytes(), "u".toString().getBytes(), "L".toString().getBytes(), "C".toString().getBytes(), "B".toString().getBytes(), "i".toString().getBytes(), "D".toString().getBytes(), "X".toString().getBytes(), "Q".toString().getBytes(), "g".toString().getBytes(), "d".toString().getBytes(), "G".toString().getBytes(), "8".toString().getBytes(), "g".toString().getBytes(), "c".toString().getBytes(), "m".toString().getBytes(), "V".toString().getBytes(), "h".toString().getBytes(), "b".toString().getBytes(), "G".toString().getBytes(), "x".toString().getBytes(), "5".toString().getBytes(), "I".toString().getBytes(), "G".toString().getBytes(), "Z".toString().getBytes(), "v".toString().getBytes(), "d".toString().getBytes(), "W".toString().getBytes(), "w".toString().getBytes(), "g".toString().getBytes(), "d".toString().getBytes(), "G".toString().getBytes(), "h".toString().getBytes(), "p".toString().getBytes(), "b".toString().getBytes(), "m".toString().getBytes(), "d".toString().getBytes(), "z".toString().getBytes(), "I".toString().getBytes(), "H".toString().getBytes(), "V".toString().getBytes(), "w".toString().getBytes(), "I".toString().getBytes(), "H".toString().getBytes(), "l".toString().getBytes(), "v".toString().getBytes(), "d".toString().getBytes(), "SB".toString().getBytes(), "u".toString().getBytes()};
        assertArrayEquals(expectedBytes, decodedBytes, 0, expectedBytes.length);
    }
}