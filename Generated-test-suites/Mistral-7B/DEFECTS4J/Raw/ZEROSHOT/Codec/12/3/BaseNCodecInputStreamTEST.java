package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodec.EOF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.junit.Test;

/**
 * Test class for {@link BaseNCodecInputStream}.
 *
 * @since 1.0
 */
public class BaseNCodecInputStreamTest {

    /**
     * Test constructor with encoding.
     */
    @Test
    public void testEncodeConstructor() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'A' });
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);
        assertEquals(true, baseNCodecInputStream.doEncode);
        assertEquals(baseNCodec, baseNCodecInputStream.baseNCodec);
    }

    /**
     * Test constructor without encoding.
     */
    @Test
    public void testDecodeConstructor() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'A' });
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        assertEquals(false, baseNCodecInputStream.doEncode);
        assertEquals(baseNCodec, baseNCodecInputStream.baseNCodec);
    }

    /**
     * Test reading single byte.
     */
    @Test
    public void testReadSingleByte() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'A' });
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);
        assertEquals(255 + (int) 'A', baseNCodecInputStream.read());
        assertEquals(EOF, baseNCodecInputStream.read());
    }

    /**
     * Test reading multiple bytes.
     */
    @Test
    public void testReadMultipleBytes() throws IOException {
        byte[] expected = new byte[] { 'A', 'B', 'C' };
        InputStream inputStream = new ByteArrayInputStream(expected);
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);
        byte[] buffer = new byte[expected.length];
        int read = baseNCodecInputStream.read(buffer, 0, buffer.length);
        assertEquals(expected.length, read);
        assertSame(expected, buffer);
    }

    /**
     * Test markSupported method.
     */
    @Test
    public void testMarkSupported() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'A' });
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);
        assertFalse(baseNCodecInputStream.markSupported());
    }

    /**
     * Test skip method.
     */
    @Test
    public void testSkip() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'A' });
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);
        baseNCodecInputStream.skip(512);
        assertNull(baseNCodecInputStream.baseNCodec.getEncodedBuffer());
        assertNull(baseNCodecInputStream.baseNCodec.getDecodedBuffer());
    }

    /**
     * Test close method.
     */
    @Test
    public void testClose() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 'A' });
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);
        baseNCodecInputStream.close();
        assertNull(baseNCodecInputStream.baseNCodec.getEncodedBuffer());
        assertNull(baseNCodecInputStream.baseNCodec.getDecodedBuffer());
        assertFalse(inputStream.markSupported());
        assertFalse(baseNCodecInputStream.markSupported());
        inputStream.close();
    }

    /**
     * Test EOF behavior.
     */
    @Test
    public void testEndOfFile() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[0]);
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, true);
        assertEquals(EOF, baseNCodecInputStream.read());
        assertEquals(EOF, baseNCodecInputStream.read());
        assertEquals(EOF, baseNCodecInputStream.read());
    }

}