package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodec.EOF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;

/**
 * Tests for {@link BaseNCodecInputStream}.
 */
public class BaseNCodecInputStreamTest {

    /**
     * Test reading bytes with {@code BaseNCodecInputStream}.
     */
    @org.junit.Test
    public void testReadBytes() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream("ABCDEFG".getBytes());
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        // read single bytes
        assertEquals(64, inputStream.read());
        assertEquals('A', (char) inputStream.read());
        assertEquals('B', (char) inputStream.read());
        assertEquals('C', (char) inputStream.read());
        assertEquals('D', (char) inputStream.read());
        assertEquals('E', (char) inputStream.read());
        assertEquals('F', (char) inputStream.read());
        assertEquals('G', (char) inputStream.read());
        assertEquals(EOF, inputStream.read());

        // read byte arrays
        byte[] buffer = new byte[7];
        inputStream.read(buffer);
        assertEquals(7, buffer.length);
        assertEquals('A', buffer[0]);
        assertEquals('B', buffer[1]);
        assertEquals('C', buffer[2]);
        assertEquals('D', buffer[3]);
        assertEquals('E', buffer[4]);
        assertEquals('F', buffer[5]);
        assertEquals('G', buffer[6]);

        // read empty byte array
        buffer = new byte[0];
        inputStream.read(buffer);
        assertEquals(0, buffer.length);
    }

    /**
     * Test encoding and decoding {@code BaseNCodecInputStream} with {@code BaseNCodec}.
     */
    @org.junit.Test
    public void testEncodeDecode() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream input = new ByteArrayInputStream("ABCDEFG".getBytes());
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // encoding
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, true);
        byte[] encodedBytes = new byte[input.available()];
        int bytesRead = inputStream.read(encodedBytes);
        assertNotNull(encodedBytes);
        assertEquals(bytesRead, encodedBytes.length);

        // decode
        BaseNCodecInputStream decoder = new BaseNCodecInputStream(new ByteArrayInputStream(encodedBytes), baseNCodec, false);
        byte[] decodedBytes = new byte[bytesRead];
        int bytesDecoded = decoder.read(decodedBytes);
        assertEquals(bytesDecoded, bytesRead);
        assertArrayEquals(new byte[] { 'A', 'B', 'C', 'D', 'E', 'F', 'G' }, decodedBytes);
    }

    /**
     * Test handling of invalid inputs.
     */
    @org.junit.Test
    public void testInvalidInputs() {
        BaseNCodec baseNCodec = new BaseNCodec();

        // test null input stream
        assertThrows(NullPointerException.class, () -> new BaseNCodecInputStream(null, baseNCodec, false));

        // test invalid codec
        assertThrows(IllegalArgumentException.class, () -> new BaseNCodecInputStream(new ByteArrayInputStream("".getBytes()), null, false));

        // test unsupported marking
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream("".getBytes()), baseNCodec, false);
        assertFalse(inputStream.markSupported());

        // test negative skip length
        assertThrows(IllegalArgumentException.class, () -> inputStream.skip(-1));

        // test reading beyond available data
        byte[] buffer = new byte[8];
        inputStream.read(buffer);
        inputStream.skip(1);
        assertThrows(IOException.class, () -> inputStream.read(buffer));
    }
}