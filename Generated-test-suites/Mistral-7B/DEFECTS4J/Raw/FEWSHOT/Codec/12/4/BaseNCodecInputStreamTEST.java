package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

public class BaseNCodecInputStreamTest {

    @Test
    public void testRead() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(baos.toByteArray()), baseNCodec, false);

        byte[] result = new byte[1];

        assertEquals(0, inputStream.read(result, 0, 0));
        assertTrue(inputStream.hasNextByte());
        assertEquals(1, inputStream.read(result, 0, 1));
        assertEquals(result[0], (byte) 0x01);
        assertTrue(inputStream.hasNextByte());
        assertEquals(1, inputStream.read(result, 0, 1));
        assertEquals(result[0], (byte) 0x02);
        assertFalse(inputStream.hasNextByte());
    }

    @Test
    public void testReadWithLimit() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(baos.toByteArray()), baseNCodec, false);

        byte[] result = new byte[3];

        assertEquals(3, inputStream.read(result, 0, 3));
        assertArrayEquals(new byte[] { 0x01, 0x02, 0x03 }, result);
        assertFalse(inputStream.hasNextByte());
    }

    @Test
    public void testReadWithNegativeOffset() {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(null, null, false);
        try {
            inputStream.read(new byte[1], -1, 1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testReadWithNegativeLimit() {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(null, null, false);
        try {
            inputStream.read(new byte[1], 0, -1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test
    public void testReadWithNullByteArray() {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(null, null, false);
        try {
            inputStream.read(null, 0, 1);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testMarkSupported() {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(null, null, false);
        assertFalse(inputStream.markSupported());
    }

    @Test
    public void testSkip() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(baos.toByteArray()), baseNCodec, false);

        inputStream.skip(512);
        inputStream.skip(4096);
        assertFalse(inputStream.hasNextByte());
    }

    @Test
    public void testAvailable() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(baos.toByteArray()), baseNCodec, false);

        assertEquals(4097, inputStream.available());
        inputStream.skip(4096);
        assertEquals(1, inputStream.available());

        baos.write("Test".getBytes());
        inputStream.skip(5);
        assertEquals(0, inputStream.available());
    }

    @Test
    public void testClose() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(baos.toByteArray()), baseNCodec, false);

        inputStream.close();
        assertNull(inputStream.in);
    }

    @Test
    public void testReset() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(baos.toByteArray()), baseNCodec, false);

        inputStream.skip(512);
        inputStream.reset();
        assertEquals(4097, inputStream.available());
    }

    @Test
    public void testReadWithBaseNCodecInputStream() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(baos.toByteArray()), baseNCodec, false);

        assertEquals(4097, inputStream.available());

        byte[] result = new byte[1];

        assertEquals(1, inputStream.read(result, 0, 1));
        assertEquals(result[0], (byte) 0x01);
        assertEquals(4096, inputStream.available());

        assertEquals(1, inputStream.read(result, 0, 1));
        assertEquals(result[0], (byte) 0x02);
        assertEquals(4095, inputStream.available());

        assertEquals(3, inputStream.read(result, 0, 3));
        assertArrayEquals(new byte[] { 0x03, 0x04, 0x05 }, result);
        assertEquals(4094, inputStream.available());

        assertFalse(inputStream.hasNextByte());
    }

    @Test
    public void testReadWithBaseNCodecInputStreamEncoded() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();

        byte[] encoded = new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 };
        ByteArrayInputStream inputStream = new ByteArrayInputStream(encoded);
        BaseNCodecInputStream decoder = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        byte[] result = new byte[1];

        assertEquals(1, decoder.read(result, 0, 1));
        assertEquals(result[0], (byte) 0x01);
        assertEquals(4, decoder.available());

        assertEquals(1, decoder.read(result, 0, 1));
        assertEquals(result[0], (byte) 0x02);
        assertEquals(3, decoder.available());

        assertEquals(2, decoder.read(result, 0, 2));
        assertArrayEquals(new byte[] { 0x03, 0x04 }, result);
        assertEquals(1, decoder.available());

        assertEquals(1, decoder.read(result, 0, 1));
        assertEquals(result[0], (byte) 0x05);
        assertFalse(decoder.hasNextByte());
    }
}