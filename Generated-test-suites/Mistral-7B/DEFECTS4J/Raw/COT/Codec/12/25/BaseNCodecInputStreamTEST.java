package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodecInputStream.EOF;
import static org.apache.commons.codec.binary.BaseNCodecInputStream.EOF_VALUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.apache.commons.codec.binary.BaseNCodecOutputStream;
import org.junit.Test;

/**
 * JUnit test file for the BaseNCodecInputStream class.
 */
public class BaseNCodecInputStreamTest {

    /**
     * Test for the constructor.
     */
    @Test
    public void testConstructor() {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02 });
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        assertEquals(inputStream, baseNCodecInputStream.in);
        assertEquals(baseNCodec, baseNCodecInputStream.baseNCodec);
        assertFalse(baseNCodecInputStream.doEncode);
    }

    /**
     * Test for the read method.
     */
    @Test
    public void testRead() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01 });
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        // read single byte
        assertEquals(0x01, baseNCodecInputStream.read());

        // read EOF
        assertEquals(EOF_VALUE, baseNCodecInputStream.read());
        assertEquals(EOF_VALUE, baseNCodecInputStream.read());
        assertEquals(EOF_VALUE, baseNCodecInputStream.read());

        // read multiple bytes
        byte[] buffer = new byte[3];
        int read = baseNCodecInputStream.read(buffer, 0, 3);
        assertEquals(3, read);
        assertEquals(0x01, buffer[0]);
        assertEquals(EOF_VALUE, buffer[1]);
        assertEquals(EOF_VALUE, buffer[2]);
    }

    /**
     * Test for the read method with encoding.
     */
    @Test
    public void testReadEncode() throws IOException {
        BaseNCodec baseNCodec = new BaseNCodec();
        BaseNCodecOutputStream baseNCodecOutputStream = new BaseNCodecOutputStream();
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(new ByteArrayInputStream(baseNCodecOutputStream.toByteArray()), baseNCodec, true);

        // read single byte
        assertEquals(0x01, baseNCodecInputStream.read());

        // read EOF
        assertEquals(EOF_VALUE, baseNCodecInputStream.read());
        assertEquals(EOF_VALUE, baseNCodecInputStream.read());
        assertEquals(EOF_VALUE, baseNCodecInputStream.read());

        // read multiple bytes
        byte[] buffer = new byte[3];
        int read = baseNCodecInputStream.read(buffer, 0, 3);
        assertEquals(3, read);
        assertEquals(0x01, buffer[0]);
        assertEquals(EOF_VALUE, buffer[1]);
        assertEquals(EOF_VALUE, buffer[2]);

        // check decoded bytes
        byte[] decoded = new byte[3];
        baseNCodec.decode(buffer, 0, 3, decoded, 0, decoded.length);
        assertEquals(0x01, decoded[0]);
        assertEquals(0, decoded[1]);
        assertEquals(0, decoded[2]);
    }

    /**
     * Test for the read method with negative length.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadNegativeLength() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        baseNCodecInputStream.read(-1);
    }

    /**
     * Test for the read method with null byte array.
     */
    @Test(expected = NullPointerException.class)
    public void testReadNullByteArray() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        baseNCodecInputStream.read(null, 0, 1);
    }

    /**
     * Test for the read method with offset out of bounds.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadOffsetOutOfBounds() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02 });
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        byte[] buffer = new byte[2];
        baseNCodecInputStream.read(buffer, 2, 1);
    }

    /**
     * Test for the read method with length out of bounds.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadLengthOutOfBounds() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01, 0x02 });
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        byte[] buffer = new byte[2];
        baseNCodecInputStream.read(buffer, 0, 3);
    }

    /**
     * Test for the read method with EOF.
     */
    @Test
    public void testReadEOF() throws IOException {
        InputStream inputStream = new ByteArrayInputStream(new byte[] { 0x01 });
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        byte[] buffer = new byte[3];
        int read = baseNCodecInputStream.read(buffer, 0, 3);
        assertEquals(1, read);
        assertEquals(0x01, buffer[0]);
        assertEquals(EOF_VALUE, buffer[1]);
        assertEquals(EOF_VALUE, buffer[2]);
    }

    /**
     * Test for the markSupported method.
     */
    @Test
    public void testMarkSupported() {
        InputStream inputStream = mock(InputStream.class);
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        assertFalse(baseNCodecInputStream.markSupported());
    }

    /**
     * Test for the skip method.
     */
    @Test
    public void testSkip() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        when(inputStream.skip(512)).thenReturn(512);
        baseNCodecInputStream.skip(512);
    }

    /**
     * Test for the skip method with negative length.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSkipNegativeLength() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        baseNCodecInputStream.skip(-1);
    }

    /**
     * Test for the close method.
     */
    @Test
    public void testClose() throws IOException {
        InputStream inputStream = mock(InputStream.class);
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        baseNCodecInputStream.close();
        verify(inputStream).close();
    }
}