package org.junit.test;

import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import java.io.ByteArrayInputStream;

public class BaseNCodecInputStreamTest {

    @Test
    public void testReadMethod() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{-1});
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, null, false);

        int readByte = baseNCodecInputStream.read();
        assertEquals(-1, readByte);

        byte[] buffer = new byte[1];
        int readLength = baseNCodecInputStream.read(buffer, 0, 1);
        assertEquals(-1, readLength);

        byte testByte = 10;
        inputStream = new ByteArrayInputStream(new byte[]{testByte});
        baseNCodecInputStream = new BaseNCodecInputStream(inputStream, null, false);

        readByte = baseNCodecInputStream.read();
        assertEquals(testByte, readByte);

        readLength = baseNCodecInputStream.read(buffer, 0, 1);
        assertEquals(1, readLength);
        assertEquals(testByte, buffer[0]);
    }

    @Test
    public void testReadArrayMethod() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{1, 2, 3});
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, null, false);

        byte[] buffer = new byte[3];
        int readLength = baseNCodecInputStream.read(buffer, 0, 3);
        assertEquals(3, readLength);

        for (int i = 0; i < 3; i++) {
            assertEquals(i + 1, buffer[i]);
        }

        readLength = baseNCodecInputStream.read(buffer, 0, 4);
        assertEquals(3, readLength);

        buffer = new byte[3];
        readLength = baseNCodecInputStream.read(buffer, 1, 2);
        assertEquals(2, readLength);

        for (int i = 0; i < 2; i++) {
            assertEquals(i + 2, buffer[i + 1]);
        }
    }

    @Test(expected = NullPointerException.class)
    public void testNullByteArray() {
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[]{1, 2, 3}), null, false);
        baseNCodecInputStream.read(null, 0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testNegativeOffset() {
        byte[] buffer = new byte[3];
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[]{1, 2, 3}), null, false);
        baseNCodecInputStream.read(buffer, -1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testInvalidLength() {
        byte[] buffer = new byte[3];
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[]{1, 2, 3}), null, false);
        baseNCodecInputStream.read(buffer, 0, 4);
    }

    @Test
    public void testMarkSupported() {
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[]{1, 2, 3}), null, false);
        assertFalse(baseNCodecInputStream.markSupported());
    }

    @Test(expected = IOException.class)
    public void testNegativeSkip() throws IOException {
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[]{1, 2, 3}), null, false);
        baseNCodecInputStream.skip(-1);
    }

    @Test(expected = IOException.class)
    public void testZeroSkip() throws IOException {
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[]{1, 2, 3}), null, false);
        baseNCodecInputStream.skip(0);
    }

    @Test
    public void testSkipFiveHundredBytes() throws IOException {
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[]{1, 2, 3}), null, false);
        baseNCodecInputStream.skip(512);
    }

    @Test
    public void testEndOfStream() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[]{});
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, null, false);

        byte[] buffer = new byte[1];
        int readLength = baseNCodecInputStream.read(buffer, 0, 1);
        assertEquals(-1, readLength);

        readLength = baseNCodecInputStream.read(buffer, 0, 1);
        assertEquals(-1, readLength);

        readLength = baseNCodecInputStream.read(buffer, 0, 1);
        assertEquals(-1, readLength);
    }
}