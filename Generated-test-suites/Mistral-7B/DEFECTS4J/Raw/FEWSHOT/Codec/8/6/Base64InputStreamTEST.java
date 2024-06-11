package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

public class Base64InputStreamTest {

    private InputStream inputStream;
    private Base64InputStream base64InputStream;
    private ByteArrayOutputStream outputStream;
    private Base64OutputStream base64OutputStream;

    @Before
    public void setup() {
        String data = "VG8gZXJyIGlzIGh1bWFuLCBidXQgdG8gcmVhbGx5IGZvdWwgdGhpbmdzIHVwIHlvdSBuZWVkIGEgY2hhcmFjdC4K";
        outputStream = new ByteArrayOutputStream();
        byte[] inputData = data.getBytes();
        base64OutputStream = new Base64OutputStream(outputStream, false);
        base64OutputStream.write(inputData, 0, inputData.length);
        base64OutputStream.finish();
        inputStream = new ByteArrayInputStream(outputStream.toByteArray());
    }

    @Test
    public void testBase64InputStreamDecoding() throws IOException {
        base64InputStream = new Base64InputStream(inputStream);
        byte[] expectedOutput = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        byte[] output = new byte[expectedOutput.length];
        int bytesRead = base64InputStream.read(output, 0, expectedOutput.length);
        assertArrayEquals(expectedOutput, output);
        assertEquals(-1, base64InputStream.read());
    }

    @Test
    public void testBase64InputStreamDecodingWithoutLineLengthAndLineSeparator() throws IOException {
        base64InputStream = new Base64InputStream(inputStream, false, 0, null);
        byte[] expectedOutput = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        byte[] output = new byte[expectedOutput.length];
        int bytesRead = base64InputStream.read(output, 0, expectedOutput.length);
        assertArrayEquals(expectedOutput, output);
        assertEquals(-1, base64InputStream.read());
    }

    @Test
    public void testBase64InputStreamDecodingWithLineLengthAndLineSeparator() throws IOException {
        base64InputStream = new Base64InputStream(inputStream, false, 72, new byte[] { (byte) 0x0D, (byte) 0x0A });
        byte[] expectedOutput = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        byte[] output = new byte[expectedOutput.length];
        int bytesRead = base64InputStream.read(output, 0, expectedOutput.length);
        assertArrayEquals(expectedOutput, output);
        assertEquals(-1, base64InputStream.read());
    }

    @Test
    public void testBase64InputStreamDecodingWithDoEncodeSetToTrue() throws IOException {
        base64InputStream = new Base64InputStream(inputStream, true);
        byte[] expectedOutput = new byte[] { 76, -67, -62, -61, -66, -63, -61, -66, -65, -61, -62, -65, -64, -69, -66,
                -66, -65, -62, -65, -65, -62, -65, -61, -63, -63, -61, -66, -66, -65, -63, -68, -66, -61, -63,
                -68, -66, -63, -61, -66, -66, -65, -63, -63, -61, -66, -66, -65, -63, -68, -66, -61, -63, -68,
                -66, -63, -61, -66, -66, -65, -63, -63, -61, -66, -66, -65, -63, -63, -61, -66, -66, -65, -63,
                -68, -66, -61, -63, -68, -66, -63, -61, -66, -66, -65, -63, -63, -61, -66, -66, -65, -63, -63,
                -61, -66, -66, -65, -63, -63, -61, -66, -66, -65, -63, -63, -61, -66, -66, -65, -63, -63, -61,
                -66, -66, -65, -63, -63, -61 };
        byte[] output = new byte[expectedOutput.length];
        int bytesRead = base64InputStream.read(output, 0, expectedOutput.length);
        assertArrayEquals(expectedOutput, output);
        assertEquals(-1, base64InputStream.read());
    }

    @Test
    public void testBase64InputStreamDecodingWithEmptyInput() throws IOException {
        inputStream = new ByteArrayInputStream(new byte[0]);
        base64InputStream = new Base64InputStream(inputStream, false);
        byte[] output = new byte[0];
        int bytesRead = base64InputStream.read(output, 0, output.length);
        assertArrayEquals(output, new byte[0]);
        assertEquals(-1, base64InputStream.read());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBase64InputStreamDecodingWithNullInputStream() throws IOException {
        base64InputStream = new Base64InputStream(null, false);
    }

    @Test
    public void testBase64InputStreamEncoding() throws IOException {
        byte[] input = new byte[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
        base64InputStream = new Base64InputStream(new ByteArrayInputStream(input), true);
        byte[] expectedOutput = new byte[] { 72, 61, 73, 73, 77, 73, 73, 68, 79, 71, 75, 75, 71, 69, 62, 62, 65,
                62, 72, 75, 63, 75, 72, 61, 76, 76, 72, 65, 63, 69, 68, 65, 72, 65, 73, 74, 62, 79, 76, 65, 78,
                68, 72, 65, 66, 63, 62, 65, 68, 72, 63, 63, 68, 65, 72, 65, 63, 68, 66, 69, 76, 65, 61, 72, 69,
                62, 62, 65, 63, 74, 72, 69, 65, 73, 61, 74, 65, 64, 65, 78, 76, 73, 73, 69, 69, 69, 71, 69, 71,
                75, 75, 71, 69, 71, 69, 69, 71, 69, 69, 69, 71, 75, 75, 71, 71, 71, 71, 69, 71, 71, 71, 71, 71,
                71, 71, 69, 71, 71, 71, 71, 71, 71, 71, 71, 71, 71, 71, 71, 71, 71, 71, 71, 71, 71, 71, 71, 71,
                71, 71, 71, 71, 69, 71, 65 };
        byte[] output = new byte[expectedOutput.length];
        int bytesRead = base64InputStream.read(output, 0, expectedOutput.length);
        assertArrayEquals(expectedOutput, output);
        assertEquals(-1, base64InputStream.read());
    }
}