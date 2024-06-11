package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodecInputStream.EOF;
import static org.apache.commons.codec.binary.BaseNCodecInputStream.FALSE;
import static org.apache.commons.codec.binary.BaseNCodecInputStream.TRUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base32;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.BaseNCodec;
import org.junit.Test;

/**
 * Test class for {@link BaseNCodecInputStream}.
 */
public class BaseNCodecInputStreamTest {

    /**
     * Test method for {@link BaseNCodecInputStream#BaseNCodecInputStream(InputStream, BaseNCodec, boolean)}.
     */
    @Test
    public void testBaseNCodecInputStream() {
        final InputStream in = new ByteArrayInputStream(new byte[] { 'A' });
        final BaseNCodec baseNCodec = new Base32();
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, FALSE);
        assertEquals(in, inputStream.in);
        assertEquals(baseNCodec, inputStream.baseNCodec);
        assertFalse(inputStream.doEncode);
    }

    /**
     * Test method for {@link BaseNCodecInputStream#BaseNCodecInputStream(InputStream, BaseNCodec, boolean)}.
     */
    @Test
    public void testBaseNCodecInputStreamEncode() {
        final InputStream in = new ByteArrayInputStream(new byte[] { 'A' });
        final BaseNCodec baseNCodec = new Base64();
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, TRUE);
        assertEquals(in, inputStream.in);
        assertEquals(baseNCodec, inputStream.baseNCodec);
        assertTrue(inputStream.doEncode);
    }

    /**
     * Test method for {@link BaseNCodecInputStream#read()}.
     */
    @Test
    public void testReadMethod() throws IOException {
        final InputStream in = new ByteArrayInputStream(new byte[] { 'A' });
        final BaseNCodec baseNCodec = new Base32();
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, FALSE);
        final int read = inputStream.read();
        assertEquals(65, read);
        final int eof = inputStream.read();
        assertEquals(EOF, eof);
    }

    /**
     * Test method for {@link BaseNCodecInputStream#read(byte[], int, int)}.
     */
    @Test
    public void testReadMethodArray() throws IOException {
        final byte[] expected = new byte[] { 'A' };
        final InputStream in = new ByteArrayInputStream(new byte[] { 'A' });
        final BaseNCodec baseNCodec = new Base32();
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, FALSE);
        final int read = inputStream.read(new byte[1], 0, 1);
        assertEquals(1, read);
        assertEquals(expected[0], inputStream.singleByte[0]);
        final int eof = inputStream.read(new byte[1], 0, 1);
        assertEquals(EOF, eof);
    }

    /**
     * Test method for {@link BaseNCodecInputStream#markSupported()}.
     */
    @Test
    public void testMarkSupportedMethod() {
        final InputStream in = new ByteArrayInputStream(new byte[] { 'A' });
        final BaseNCodec baseNCodec = new Base32();
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, FALSE);
        assertFalse(inputStream.markSupported());
    }

    /**
     * Test method for {@link BaseNCodecInputStream#skip(long)}.
     */
    @Test
    public void testSkipMethod() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final BaseNCodec baseNCodec = new Base64();
        final InputStream in = new ByteArrayInputStream(new byte[] { 'A' });
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, FALSE);
        inputStream.skip(1);
        byte[] bytes = out.toByteArray();
        assertEquals(0, bytes.length);
    }

    /**
     * Test method for {@link BaseNCodecInputStream#available()}.
     */
    @Test
    public void testAvailableMethod() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final InputStream in = new ByteArrayInputStream(new byte[] { 'A' });
        final BaseNCodec baseNCodec = new Base64();
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, FALSE);
        assertEquals(1, inputStream.available());
    }

    /**
     * Test method for {@link BaseNCodecInputStream#close()}.
     */
    @Test
    public void testCloseMethod() throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final InputStream in = new ByteArrayInputStream(new byte[] { 'A' });
        final BaseNCodec baseNCodec = new Base64();
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, FALSE);
        inputStream.close();
        assertEquals(EOF, inputStream.read());
    }

    /**
     * Test method for {@link BaseNCodecInputStream#close()}.
     */
    @Test(expected = IOException.class)
    public void testCloseMethodWithoutBaseNCodec() throws IOException {
        final InputStream in = new ByteArrayInputStream(new byte[] { 'A' });
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, null, FALSE);
        inputStream.close();
    }

    /**
     * Test method for {@link BaseNCodecInputStream#close()}.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testCloseMethodNegativeSkip() throws IOException {
        final InputStream in = new ByteArrayInputStream(new byte[] { 'A' });
        final BaseNCodec baseNCodec = new Base64();
        final BaseNCodecInputStream inputStream = new BaseNCodecInputStream(in, baseNCodec, FALSE);
        inputStream.skip(-1);
    }
}