package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodecInputStream.EOF;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class BaseNCodecInputStreamTest {

    @Test
    public void testBaseNCodecInputStream() {
        BaseNCodec baseNCodec = new BaseNCodec(); // assuming BaseNCodec is tested before

        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x11, 0x22, 0x33, 0x44, 0x55 });
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        assertNotNull(baseNCodecInputStream);
        assertEquals(inputStream, baseNCodecInputStream.in);
        assertEquals(baseNCodec, baseNCodecInputStream.baseNCodec);
        assertFalse(baseNCodecInputStream.doEncode);
    }

    @Test
    public void testReadByte() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x11 });
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        int readByte = baseNCodecInputStream.read();

        assertEquals(0x11, readByte);
        assertEquals(0, baseNCodecInputStream.read()); // read EOF
    }

    @Test
    public void testReadByteArray() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] byteArray = new byte[5];
        int readLength = baseNCodecInputStream.read(byteArray, 0, byteArray.length);

        assertEquals(byteArray.length, readLength);
        assertArrayEquals(byteArray, new byte[] { 0x11, 0x22, 0x33, 0x44, 0x55 });
    }

    @Test(expected = IOException.class)
    public void testReadByteArrayNegativeOffset() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x11, 0x22, 0x33, 0x44, 0x55 });
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] byteArray = new byte[5];
        int readLength = baseNCodecInputStream.read(byteArray, -1, byteArray.length);
    }

    @Test(expected = IOException.class)
    public void testReadByteArrayNegativeLength() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x11, 0x22, 0x33, 0x44, 0x55 });
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] byteArray = new byte[5];
        int readLength = baseNCodecInputStream.read(byteArray, 0, -1);
    }

    @Test(expected = NullPointerException.class)
    public void testReadByteArrayNullByteArray() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x11, 0x22, 0x33, 0x44, 0x55 });
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        int readLength = baseNCodecInputStream.read(null, 0, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayInvalidOffset() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x11, 0x22, 0x33, 0x44, 0x55 });
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] byteArray = new byte[5];
        int readLength = baseNCodecInputStream.read(byteArray, 10, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadByteArrayInvalidOffsetAndLength() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x11, 0x22, 0x33, 0x44, 0x55 });
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] byteArray = new byte[5];
        int readLength = baseNCodecInputStream.read(byteArray, 10, 10);
    }

    @Test
    public void testMarkSupported() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x11, 0x22, 0x33, 0x44, 0x55 });
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        assertFalse(baseNCodecInputStream.markSupported());
    }

    @Test(expected = IOException.class)
    public void testSkip() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x11, 0x22, 0x33, 0x44, 0x55 });
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        baseNCodecInputStream.skip(10);
    }

    @Test
    public void testAvailable() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x11, 0x22, 0x33, 0x44, 0x55 });
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        assertEquals(5, baseNCodecInputStream.available());
    }

    @Test
    public void testClose() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x11, 0x22, 0x33, 0x44, 0x55 });
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        baseNCodecInputStream.close();

        assertNull(baseNCodecInputStream.baseNCodec);
        assertNull(baseNCodecInputStream.in);
    }

    @Test
    public void testReadWithEncoding() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x11, 0x22, 0x33, 0x44, 0x55 });
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        byte[] byteArray = new byte[5];
        int readLength = baseNCodecInputStream.read(byteArray, 0, byteArray.length);

        assertEquals(byteArray.length, readLength);
        assertArrayEquals(new byte[] { 0x11, 0x22, 0x33, 0x44, 0x55 }, byteArray);
    }
}