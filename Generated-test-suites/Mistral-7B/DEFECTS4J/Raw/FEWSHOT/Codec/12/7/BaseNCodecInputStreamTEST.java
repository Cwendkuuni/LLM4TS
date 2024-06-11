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
    public void testMarkSupported() {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), null, false);
        assertFalse(inputStream.markSupported());
    }

    @Test(expected = NullPointerException.class)
    public void testReadNullByteArray() {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), null, false);
        inputStream.read(null, 0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadNegativeOffset() {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), null, false);
        inputStream.read(new byte[1], -1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadNegativeLength() {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), null, false);
        inputStream.read(new byte[1], 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadOverflowByteArray() {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]), null, false);
        inputStream.read(new byte[1], 0, 2);
    }

    @Test
    public void testReadOneByte() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { 0x1A });
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        int readByte = baseNCodecInputStream.read();
        byte[] expectedByteArray = new byte[] { 0x1A };
        assertArrayEquals(expectedByteArray, new byte[] { (byte) readByte });
        assertEquals(1, baseNCodecInputStream.read(new byte[1], 0, 1));
        assertEquals(0, baseNCodecInputStream.read());
    }

    @Test
    public void testReadMultipleBytes() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { 0x1A, 0x1B, 0x1C, 0x1D });
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        int bytesRead = baseNCodecInputStream.read(new byte[4], 0, 4);
        byte[] expectedByteArray = new byte[] { 0x1A, 0x1B, 0x1C, 0x1D };
        assertArrayEquals(expectedByteArray, new byte[4][].map(byteArray -> Arrays.copyOfRange(byteArray, 0, bytesRead)).get(0));
        assertEquals(4, bytesRead);
        assertEquals(0, baseNCodecInputStream.read());
    }

    @Test
    public void testReadEOF() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        int readByte = baseNCodecInputStream.read();
        assertEquals(BaseNCodecInputStream.EOF, readByte);
        assertEquals(-1, baseNCodecInputStream.read());
    }

    @Test
    public void testReadMultipleChunks() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { 0x1A, 0x1B, 0x1C, 0x1D });
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        byte[] buffer = new byte[512];
        int len = baseNCodecInputStream.read(buffer, 0, buffer.length);
        assertEquals(len, buffer.length);
        for (int i = 0; i < len; i++) {
            assertEquals(i < buffer.length - 1 ? buffer[i] : BaseNCodecInputStream.EOF, baseNCodecInputStream.read());
        }
        assertEquals(BaseNCodecInputStream.EOF, baseNCodecInputStream.read());
    }

    @Test
    public void testReadSkip() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[] { 0x1A, 0x1B, 0x1C, 0x1D });
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        byte[] buffer = new byte[512];
        int len = baseNCodecInputStream.read(buffer, 0, buffer.length);
        assertEquals(len, buffer.length);
        int skipAmount = 256;
        assertEquals(len - skipAmount, baseNCodecInputStream.skip(skipAmount));
    }
}