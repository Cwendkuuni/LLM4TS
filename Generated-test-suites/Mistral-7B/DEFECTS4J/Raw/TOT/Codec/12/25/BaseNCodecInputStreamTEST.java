package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodec.EOF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.apache.commons.codec.binary.BaseNCodecOutputStream;

/**
 * Test class for {@link BaseNCodecInputStream}.
 */
public class BaseNCodecInputStreamTest {

    /**
     * Test method for {@link BaseNCodecInputStream#BaseNCodecInputStream(InputStream, BaseNCodec, boolean)}.
     */
    @org.junit.Test
    public void testConstructor() {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final InputStream inputStream = new ByteArrayInputStream(new byte[] { 'A' });
        final BaseNCodecInputStream inputStreamFilter = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        assertTrue(inputStreamFilter instanceof BaseNCodecInputStream);
        assertFalse(inputStreamFilter instanceof BaseNCodecOutputStream);
    }

    /**
     * Test method for {@link BaseNCodecInputStream#BaseNCodecInputStream(InputStream, BaseNCodec, boolean)}.
     */
    @org.junit.Test
    public void testConstructorWithEncode() {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final InputStream inputStream = new ByteArrayInputStream(new byte[] { 'A' });
        final BaseNCodecInputStream inputStreamFilter = new BaseNCodecInputStream(inputStream, baseNCodec, true);
        assertTrue(inputStreamFilter instanceof BaseNCodecInputStream);
        assertFalse(inputStreamFilter instanceof BaseNCodecOutputStream);

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final BaseNCodecOutputStream outputStreamFilter = new BaseNCodecOutputStream(outputStream, baseNCodec);
        inputStreamFilter.transferTo(outputStreamFilter);
        assertEquals(new String(outputStream.toByteArray(), StandardCharsets.UTF_8), "DQ==");
    }

    /**
     * Test method for {@link BaseNCodecInputStream#read()}.
     */
    @org.junit.Test
    public void testRead() throws IOException {
        final byte[] input = new byte[] { 0x41, 0x42, 0x43, EOF };
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        final BaseNCodec baseNCodec = new BaseNCodec();
        final BaseNCodecInputStream inputStreamFilter = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        final int read;

        read = inputStreamFilter.read();
        assertEquals(input[0], read);

        read = inputStreamFilter.read();
        assertEquals(input[1], read);

        read = inputStreamFilter.read();
        assertEquals(input[2], read);

        read = inputStreamFilter.read();
        assertEquals(EOF, read);

        read = inputStreamFilter.read();
        assertEquals(EOF, read);
    }

    /**
     * Test method for {@link BaseNCodecInputStream#read(byte[], int, int)}.
     */
    @org.junit.Test
    public void testReadWithByteArray() throws IOException {
        final byte[] input = new byte[] { 0x41, 0x42, 0x43, EOF };
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        final BaseNCodec baseNCodec = new BaseNCodec();
        final BaseNCodecInputStream inputStreamFilter = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        final byte[] buffer = new byte[3];
        final int read;

        read = inputStreamFilter.read(buffer);
        assertEquals(3, read);
        assertEquals(input[0], buffer[0]);
        assertEquals(input[1], buffer[1]);
        assertEquals(input[2], buffer[2]);

        read = inputStreamFilter.read(buffer, 1, 2);
        assertEquals(2, read);
        assertEquals(input[1], buffer[1]);
        assertEquals(input[2], buffer[2]);

        read = inputStreamFilter.read(buffer, 4, 3);
        assertEquals(0, read);
        assertNull(buffer);
    }

    /**
     * Test method for {@link BaseNCodecInputStream#markSupported()}.
     */
    @org.junit.Test(expected = UnsupportedOperationException.class)
    public void testMarkSupported() {
        final BaseNCodec baseNCodec = new BaseNCodec();
        final BaseNCodecInputStream inputStreamFilter = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), baseNCodec, false);
        inputStreamFilter.markSupported();
    }

    /**
     * Test method for {@link BaseNCodecInputStream#skip(long)}.
     */
    @org.junit.Test
    public void testSkip() throws IOException {
        final byte[] input = new byte[] { 0x41, 0x42, 0x43, EOF };
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        final BaseNCodec baseNCodec = new BaseNCodec();
        final BaseNCodecInputStream inputStreamFilter = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        final long skip = 2;
        final int skipped;

        skipped = inputStreamFilter.skip(skip);
        assertEquals(skip, skipped);
        assertEquals(input[2], inputStreamFilter.read());
    }

    /**
     * Test method for {@link BaseNCodecInputStream#close()}.
     */
    @org.junit.Test
    public void testClose() throws IOException {
        final byte[] input = new byte[] { 0x41, 0x42, 0x43, EOF };
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(input);
        final BaseNCodec baseNCodec = new BaseNCodec();
        final BaseNCodecInputStream inputStreamFilter = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        inputStreamFilter.close();
        assertFalse(inputStreamFilter.isOpen());
    }

}