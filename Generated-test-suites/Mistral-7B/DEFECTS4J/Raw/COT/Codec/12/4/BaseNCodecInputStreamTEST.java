package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodec.EOF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
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
 */
public class BaseNCodecInputStreamTest {

    /**
     * Test constructor with valid arguments.
     */
    @Test
    public void testConstructorValidArguments() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        assertEquals(inputStream, baseNCodecInputStream.in);
        assertEquals(baseNCodec, baseNCodecInputStream.baseNCodec);
        assertFalse(baseNCodecInputStream.doEncode);
    }

    /**
     * Test constructor with null {@link InputStream}.
     */
    @Test(expected = NullPointerException.class)
    public void testConstructorNullInputStream() {
        new BaseNCodecInputStream(null, mock(BaseNCodec.class), false);
    }

    /**
     * Test constructor with null {@link BaseNCodec}.
     */
    @Test(expected = NullPointerException.class)
    public void testConstructorNullBaseNCodec() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        new BaseNCodecInputStream(inputStream, null, false);
    }

    /**
     * Test {@link BaseNCodecInputStream#read()} with valid arguments.
     *
     * @throws IOException on error
     */
    @Test
    public void testReadValidArguments() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        byte expectedByte = (byte) 0x01;
        byte[] expectedBytes = { 0x01 };
        assertEquals(expectedByte, baseNCodecInputStream.read());
        assertEquals(expectedBytes, baseNCodecInputStream.read(expectedBytes, 0, expectedBytes.length));
        assertEquals(EOF, baseNCodecInputStream.read());
        assertEquals(-1, baseNCodecInputStream.read());
    }

    /**
     * Test {@link BaseNCodecInputStream#read()} with invalid arguments.
     *
     * @throws IOException on error
     */
    @Test(expected = NullPointerException.class)
    public void testReadNullByteArray() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        baseNCodecInputStream.read(null, 0, 1);
    }

    /**
     * Test {@link BaseNCodecInputStream#read(byte[], int, int)} with valid arguments.
     *
     * @throws IOException on error
     */
    @Test
    public void testReadValidByteArray() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 });
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        byte[] expectedBytes1 = { 0x01, 0x02 };
        byte[] expectedBytes2 = { 0x03, 0x04 };
        byte[] expectedBytes3 = { 0x05 };
        assertEquals(expectedBytes1.length, baseNCodecInputStream.read(expectedBytes1, 0, expectedBytes1.length));
        assertEquals(expectedBytes1, expectedBytes1);
        assertEquals(expectedBytes2.length, baseNCodecInputStream.read(expectedBytes2, 0, expectedBytes2.length));
        assertEquals(expectedBytes2, expectedBytes2);
        assertEquals(expectedBytes3.length, baseNCodecInputStream.read(expectedBytes3, 0, expectedBytes3.length));
        assertEquals(expectedBytes3, expectedBytes3);
        assertEquals(EOF, baseNCodecInputStream.read(new byte[1], 0, 1));
        assertEquals(EOF, baseNCodecInputStream.read());
        assertEquals(EOF, baseNCodecInputStream.read(new byte[1][], 0, 1));
        assertEquals(EOF, baseNCodecInputStream.read(new byte[1][], 0, 1));
    }

    /**
     * Test {@link BaseNCodecInputStream#read(byte[], int, int)} with invalid arguments.
     *
     * @throws IOException on error
     */
    @Test(expected = NullPointerException.class)
    public void testReadNullByteArrayOrNullOffsetOrNegativeLength() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 });
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        baseNCodecInputStream.read(null, 0, 1);
        baseNCodecInputStream.read(new byte[] { 0x01, 0x02 }, -1, 1);
        baseNCodecInputStream.read(new byte[] { 0x01, 0x02 }, 0, -1);
    }

    /**
     * Test {@link BaseNCodecInputStream#markSupported()} with valid arguments.
     */
    @Test
    public void testMarkSupported() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03 });
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        assertFalse(baseNCodecInputStream.markSupported());
    }

    /**
     * Test {@link BaseNCodecInputStream#skip(long)} with valid arguments.
     *
     * @throws IOException on error
     */
    @Test
    public void testSkipValidArguments() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 });
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        assertEquals(4, baseNCodecInputStream.skip(4L));
        assertEquals(1, baseNCodecInputStream.skip(1L));
        assertEquals(0, baseNCodecInputStream.skip(0L));
        assertEquals(EOF, baseNCodecInputStream.read());
        assertEquals(EOF, baseNCodecInputStream.read());
        assertEquals(EOF, baseNCodecInputStream.read());
    }

    /**
     * Test {@link BaseNCodecInputStream#skip(long)} with invalid arguments.
     *
     * @throws IOException on error
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSkipNegativeLength() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02, 0x03, 0x04, 0x05 });
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        baseNCodecInputStream.skip(-1L);
    }
}