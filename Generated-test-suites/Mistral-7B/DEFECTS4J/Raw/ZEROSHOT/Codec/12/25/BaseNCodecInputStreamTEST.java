package org.apache.commons.codec.binary;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link BaseNCodecInputStream}.
 */
public class BaseNCodecInputStreamTest {

    private BaseNCodec baseNCodec;
    private BaseNCodecInputStream inputStream;
    private ByteArrayOutputStream outputStream;

    @Before
    public void setup() {
        baseNCodec = new BaseNCodec();
        inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[]{}), baseNCodec, false);
        outputStream = new ByteArrayOutputStream();
    }

    @Test
    public void testRead() throws IOException {
        // Test empty input stream
        assertEquals(BaseNCodecInputStream.EOF, inputStream.read());

        // Test input stream with data
        outputStream.write("ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ".getBytes());
        inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(outputStream.toByteArray()), baseNCodec, false);

        byte[] buffer = new byte[33];
        int length = 0;
        for (int i = 0; i < 32; i++) {
            length += inputStream.read(buffer, length, buffer.length - length);
            assertEquals(32, length);
            assertTrue(baseNCodec.isBaseNValid(buffer));
        }
        assertEquals(BaseNCodecInputStream.EOF, inputStream.read());
    }

    @Test
    public void testReadArray() throws IOException {
        // Test empty input stream
        byte[] buffer = new byte[1];
        int length = inputStream.read(buffer, 0, buffer.length);
        assertEquals(BaseNCodecInputStream.EOF, length);

        // Test input stream with data
        outputStream.write("ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ".getBytes());
        inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(outputStream.toByteArray()), baseNCodec, false);

        length = inputStream.read(buffer, 0, buffer.length);
        assertEquals(32, length);
        assertTrue(baseNCodec.isBaseNValid(buffer));

        length = inputStream.read(buffer, 0, buffer.length);
        assertEquals(BaseNCodecInputStream.EOF, length);
    }

    @Test
    public void testMarkSupported() {
        assertFalse(inputStream.markSupported());
    }

    @Test
    public void testSkip() throws IOException {
        // Test empty input stream
        inputStream.skip(10);
        assertEquals(BaseNCodecInputStream.EOF, inputStream.read());

        // Test input stream with data
        outputStream.write("ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGHIJKLMNOPQRSTUVWXYZ".getBytes());
        inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(outputStream.toByteArray()), baseNCodec, false);

        inputStream.skip(512);
        int length = inputStream.read(new byte[32], 0, 32);
        assertEquals(32, length);
        assertTrue(baseNCodec.isBaseNValid(new byte[]{'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M'}));

        inputStream.skip(512);
        length = inputStream.read(new byte[32], 0, 32);
        assertEquals(BaseNCodecInputStream.EOF, length);
    }
}