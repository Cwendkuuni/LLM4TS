package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodec.EOF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;

import org.junit.Test;

/**
 * Test class for {@link BaseNCodecInputStream}.
 */
public class BaseNCodecInputStreamTest {

    /**
     * Test constructor.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Test
    public void testConstructor() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x2A, 0x27, 0x12, 0x34 });
        BaseNCodec baseNCodec = new BaseNCodec();

        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        assertEquals(inputStream, testInstance.in);
        assertEquals(baseNCodec, testInstance.baseNCodec);
        assertFalse(testInstance.doEncode);
    }

    /**
     * Test constructor.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Test
    public void testConstructorEncode() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x2A, 0x27, 0x12, 0x34 });
        BaseNCodec baseNCodec = new BaseNCodec();

        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(inputStream, baseNCodec, true);
        assertEquals(inputStream, testInstance.in);
        assertEquals(baseNCodec, testInstance.baseNCodec);
        assertTrue(testInstance.doEncode);
    }

    /**
     * Test method {@link BaseNCodecInputStream#read()}.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Test
    public void testRead() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x2A, 0x27, 0x12, 0x34 });
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        assertEquals(0x2A, testInstance.read());
        assertEquals(0x27, testInstance.read());
        assertEquals(0x12, testInstance.read());
        assertEquals(0x34, testInstance.read());
        assertEquals(BaseNCodecInputStream.EOF, testInstance.read());
    }

    /**
     * Test method {@link BaseNCodecInputStream#read(byte[], int, int)}.
     *
     * @throws IOException
     *             if an I/O error occurs.
     */
    @Test
    public void testReadByteArray() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x2A, 0x27, 0x12, 0x34, 0x11, 0x43, 0x22, 0x44 });
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        byte[] buffer = new byte[4];
        int length = testInstance.read(buffer, 0, 4);

        assertEquals(length, 4);
        assertEquals(0x2A, buffer[0]);
        assertEquals(0x27, buffer[1]);
        assertEquals(0x12, buffer[2]);
        assertEquals(0x34, buffer[3]);

        byte[] buffer2 = new byte[5];
        length = testInstance.read(buffer2, 0, 5);

        assertEquals(4, length);
        assertEquals(0x11, buffer2[0]);
        assertEquals(0x43, buffer2[1]);
        assertEquals(0x22, buffer2[2]);
        assertEquals(0x44, buffer2[3]);
        assertEquals(BaseNCodecInputStream.EOF, buffer2[4]);
    }

    /**
     * Test method {@link BaseNCodecInputStream#markSupported()} with a false return value.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void testMarkSupported() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x2A, 0x27, 0x12, 0x34 });
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        testInstance.markSupported();
    }

    /**
     * Test method {@link BaseNCodecInputStream#skip(long)} with a positive value.
     */
    @Test
    public void testSkipPositive() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x2A, 0x27, 0x12, 0x34 });
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        long length = 2;
        testInstance.skip(length);

        byte[] buffer = new byte[2];
        int read = testInstance.read(buffer, 0, 2);

        assertEquals(0x12, (int) buffer[0]);
        assertEquals(0x34, (int) buffer[1]);

        testInstance.skip(1);

        buffer = new byte[1];
        read = testInstance.read(buffer, 0, 1);

        assertEquals(BaseNCodecInputStream.EOF, (int) read);
        assertEquals(BaseNCodecInputStream.EOF, buffer[0]);
    }

    /**
     * Test method {@link BaseNCodecInputStream#skip(long)} with a negative value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSkipNegative() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x2A, 0x27, 0x12, 0x34 });
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecInputStream testInstance = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        testInstance.skip(-1);
    }
}