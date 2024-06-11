package org.apache.commons.codec.binary;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.Test;

public class BaseNCodecInputStreamTest {

    @Test
    public void testReadSingleByte() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BaseNCodecEncoder encoder = new BaseNCodecEncoder();
        encoder.setOutputLength(6);
        encoder.encode(out.toByteArray(), 0, 5);

        BaseNCodecInputStream input = new BaseNCodecInputStream(new ByteArrayInputStream(out.toByteArray()), new BaseNCodec(), false);
        byte[] singleByte = new byte[1];
        int b = input.read(singleByte, 0, 1);
        assertEquals(singleByte[0], 5);
        assertEquals(b, singleByte[0] + 256);

        b = input.read();
        assertEquals(b, EOF);
    }

    @Test
    public void testReadByteArray() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BaseNCodecEncoder encoder = new BaseNCodecEncoder();
        encoder.setOutputLength(6);
        encoder.encode(out.toByteArray(), 0, 5);

        BaseNCodecInputStream input = new BaseNCodecInputStream(new ByteArrayInputStream(out.toByteArray()), new BaseNCodec(), false);
        byte[] b = new byte[3];
        int len = input.read(b, 0, 3);
        assertArrayEquals(new byte[] { 5, 1, 2 }, b);
        assertEquals(len, 3);

        len = input.read(b, 1, 1);
        assertEquals(len, 0);
        assertNull(b);

        len = input.read(b, 0, 3);
        assertEquals(len, 0);
        assertNull(b);
    }

    @Test
    public void testMarkSupported() throws IOException {
        BaseNCodecInputStream input = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), new BaseNCodec(), false);
        assertFalse(input.markSupported());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeOffset() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BaseNCodecEncoder encoder = new BaseNCodecEncoder();
        encoder.setOutputLength(6);
        encoder.encode(out.toByteArray(), 0, 5);

        BaseNCodecInputStream input = new BaseNCodecInputStream(new ByteArrayInputStream(out.toByteArray()), new BaseNCodec(), false);
        byte[] b = new byte[1];
        input.read(b, -1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeLength() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BaseNCodecEncoder encoder = new BaseNCodecEncoder();
        encoder.setOutputLength(6);
        encoder.encode(out.toByteArray(), 0, 5);

        BaseNCodecInputStream input = new BaseNCodecInputStream(new ByteArrayInputStream(out.toByteArray()), new BaseNCodec(), false);
        byte[] b = new byte[1];
        input.read(b, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithLargerLength() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BaseNCodecEncoder encoder = new BaseNCodecEncoder();
        encoder.setOutputLength(6);
        encoder.encode(out.toByteArray(), 0, 5);

        BaseNCodecInputStream input = new BaseNCodecInputStream(new ByteArrayInputStream(out.toByteArray()), new BaseNCodec(), false);
        byte[] b = new byte[4];
        input.read(b, 0, 4);
    }

    @Test
    public void testSkip() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BaseNCodecEncoder encoder = new BaseNCodecEncoder();
        encoder.setOutputLength(12);
        encoder.encode(out.toByteArray(), 0, 6);

        BaseNCodecInputStream input = new BaseNCodecInputStream(new ByteArrayInputStream(out.toByteArray()), new BaseNCodec(), false);
        input.skip(512);
        input.skip(512);
        input.skip(512);

        byte[] b = new byte[5];
        int len = input.read(b, 0, 5);
        assertArrayEquals(new byte[] { 1, 2, 3, 4, 5 }, b);
        assertEquals(len, 5);
    }

    @Test
    public void testAvailable() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        BaseNCodecEncoder encoder = new BaseNCodecEncoder();
        encoder.setOutputLength(12);
        encoder.encode(out.toByteArray(), 0, 6);

        BaseNCodecInputStream input = new BaseNCodecInputStream(new ByteArrayInputStream(out.toByteArray()), new BaseNCodec(), false);
        assertEquals(1, input.available());

        byte[] b = new byte[5];
        int len = input.read(b, 0, 5);
        assertEquals(len, 5);
        assertEquals(7, input.available());
    }
}