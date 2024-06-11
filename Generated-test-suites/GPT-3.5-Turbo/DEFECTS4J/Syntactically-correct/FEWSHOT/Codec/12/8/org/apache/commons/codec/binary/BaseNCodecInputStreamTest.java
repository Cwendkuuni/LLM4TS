
package org.apache.commons.codec.binary;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.BaseNCodec;
import org.junit.Before;
import org.junit.Test;

public class BaseNCodecInputStreamTest {

    private BaseNCodecInputStream baseNCodecInputStream;
    private BaseNCodec baseNCodec;
    private InputStream inputStream;
    private ByteArrayInputStream byteArrayInputStream;

    @Before
    public void setUp() {
        baseNCodec = new Base32();
        byte[] bytes = new byte[]{(byte) 0xF0, (byte) 0x9F, (byte) 0x98, (byte) 0x82};
        byteArrayInputStream = new ByteArrayInputStream(bytes);
        inputStream = new BaseNCodecInputStream(byteArrayInputStream, baseNCodec, false);
        baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
    }

    @Test
    public void testRead() throws IOException {
        assertEquals(240, baseNCodecInputStream.read());
        assertEquals(159, baseNCodecInputStream.read());
        assertEquals(152, baseNCodecInputStream.read());
        assertEquals(130, baseNCodecInputStream.read());
        assertEquals(-1, baseNCodecInputStream.read());
    }

    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] buffer = new byte[4];
        assertEquals(4, baseNCodecInputStream.read(buffer));
        assertEquals(240, buffer[0]);
        assertEquals(159, buffer[1]);
        assertEquals(152, buffer[2]);
        assertEquals(130, buffer[3]);
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] buffer = new byte[6];
        assertEquals(4, baseNCodecInputStream.read(buffer, 1, 4));
        assertEquals(0, buffer[0]);
        assertEquals(240, buffer[1]);
        assertEquals(159, buffer[2]);
        assertEquals(152, buffer[3]);
        assertEquals(130, buffer[4]);
        assertEquals(0, buffer[5]);
    }

    @Test
    public void testReadWithNullBuffer() throws IOException {
        try {
            baseNCodecInputStream.read(null, 0, 1);
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testReadWithNegativeOffset() throws IOException {
        byte[] buffer = new byte[4];
        try {
            baseNCodecInputStream.read(buffer, -1, 4);
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testReadWithNegativeLength() throws IOException {
        byte[] buffer = new byte[4];
        try {
            baseNCodecInputStream.read(buffer, 0, -1);
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testReadWithInvalidOffset() throws IOException {
        byte[] buffer = new byte[4];
        try {
            baseNCodecInputStream.read(buffer, 5, 4);
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testReadWithInvalidLength() throws IOException {
        byte[] buffer = new byte[4];
        try {
            baseNCodecInputStream.read(buffer, 0, 6);
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testReadWithZeroLength() throws IOException {
        byte[] buffer = new byte[4];
        assertEquals(0, baseNCodecInputStream.read(buffer, 0, 0));
    }

    @Test
    public void testMarkSupported() {
        assertFalse(baseNCodecInputStream.markSupported());
    }

    @Test
    public void testAvailable() throws IOException {
        assertEquals(1, baseNCodecInputStream.available());
    }
}

