package org.apache.commons.codec.binary;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;

/**
 * Test class for {@link BaseNCodecInputStream}.
 */
public class BaseNCodecInputStreamTest {

    /**
     * Test method for {@link BaseNCodecInputStream#BaseNCodecInputStream(InputStream, BaseNCodec, boolean)}.
     */
    @org.junit.Test
    public void testBaseNCodecInputStream() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43 });
        BaseNCodecInputStream inputStreamFilter = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] buffer = new byte[3];
        int bytesRead = inputStreamFilter.read(buffer, 0, 3);
        assertArrayEquals(new byte[] { 0x41, 0x42, 0x43 }, buffer);
        assertEquals(3, bytesRead);

        inputStreamFilter.close();
        inputStream.close();
    }

    /**
     * Test method for {@link BaseNCodecInputStream#BaseNCodecInputStream(InputStream, BaseNCodec, boolean)}.
     */
    @org.junit.Test
    public void testBaseNCodecInputStreamEncode() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x61, 0x62, 0x63 });
        BaseNCodecInputStream inputStreamFilter = new BaseNCodecInputStream(inputStream, baseNCodec, true);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[3];

        int bytesRead = -1;
        while ((bytesRead = inputStreamFilter.read(buffer, 0, buffer.length)) > 0) {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }

        inputStreamFilter.close();
        inputStream.close();

        byte[] expected = baseNCodec.encode("ABC".getBytes());
        assertEquals(expected, byteArrayOutputStream.toByteArray());
    }

    /**
     * Test method for {@link BaseNCodecInputStream#read()}.
     */
    @org.junit.Test
    public void testRead() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43 });
        BaseNCodecInputStream inputStreamFilter = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        int byteRead = inputStreamFilter.read();
        assertEquals(0x41, byteRead);

        byteRead = inputStreamFilter.read();
        assertEquals(0x42, byteRead);

        byteRead = inputStreamFilter.read();
        assertEquals(0x43, byteRead);

        byteRead = inputStreamFilter.read();
        assertEquals(-1, byteRead);

        inputStreamFilter.close();
        inputStream.close();
    }

    /**
     * Test method for {@link BaseNCodecInputStream#read(byte[], int, int)}.
     */
    @org.junit.Test
    public void testReadByteArray() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43, 0x44 });
        BaseNCodecInputStream inputStreamFilter = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] buffer = new byte[4];
        int bytesRead = inputStreamFilter.read(buffer, 0, 4);
        assertArrayEquals(new byte[] { 0x41, 0x42, 0x43, 0x44 }, buffer);
        assertEquals(4, bytesRead);

        inputStreamFilter.close();
        inputStream.close();
    }

    /**
     * Test method for {@link BaseNCodecInputStream#markSupported()}.
     */
    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testMarkSupported() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43 });
        BaseNCodecInputStream inputStreamFilter = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        inputStreamFilter.markSupported();
    }

    /**
     * Test method for {@link BaseNCodecInputStream#skip(long)}.
     */
    @org.junit.Test
    public void testSkip() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x41, 0x42, 0x43 });
        BaseNCodecInputStream inputStreamFilter = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        long bytesToSkip = 2;
        int bytesSkipped = (int) Math.min(bytesToSkip, Long.valueOf(inputStreamFilter.available()));

        inputStreamFilter.skip(bytesToSkip);

        byte[] buffer = new byte[1];
        int bytesRead = inputStreamFilter.read(buffer, 0, 1);
        assertEquals(0x43, bytesRead);
        assertEquals(0x43, buffer[0]);

        inputStreamFilter.close();
        inputStream.close();
    }
}