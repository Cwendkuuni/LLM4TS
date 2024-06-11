package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodecInputStream.EOF;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

/**
 * Test cases for {@link BaseNCodecInputStream}.
 */
public class BaseNCodecInputStreamTest {

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        InputStream input = new ByteArrayInputStream(new byte[0]);
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);
        assertEquals(input, inputStream.in);
        assertEquals(baseNCodec, inputStream.baseNCodec);
        assertFalse(inputStream.doEncode);
    }

    /**
     * Test {@link BaseNCodecInputStream#read()}.
     */
    @Test
    public void testRead() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { (byte) 0x61 });
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        when(baseNCodec.hasData()).thenReturn(true);
        when(baseNCodec.decode(anyByteArray(), anyInt(), anyInt())).thenReturn(new byte[] { 0x61 });

        assertEquals((int) 0x61, inputStream.read());

        when(baseNCodec.decode(anyByteArray(), anyInt(), anyInt())).thenReturn(new byte[0]);
        assertEquals(BaseNCodecInputStream.EOF, inputStream.read());
    }

    /**
     * Test {@link BaseNCodecInputStream#read(byte[], int, int)}.
     */
    @Test
    public void testReadByteArray() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { (byte) 0x61, (byte) 0x62 });
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        when(baseNCodec.hasData()).thenReturn(true);
        when(baseNCodec.decode(anyByteArray(), anyInt(), anyInt())).thenReturn(new byte[] { 0x61, 0x62 });

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write(inputStream.read(output.toByteArray(), 0, 2));

        assertEquals(Hex.encodeHex(output.toByteArray()), "6162");
    }

    /**
     * Test {@link BaseNCodecInputStream#markSupported()}.
     */
    @Test
    public void testMarkSupported() {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]),
                mock(BaseNCodec.class), false);
        assertFalse(inputStream.markSupported());
    }

    /**
     * Test {@link BaseNCodecInputStream#skip(long)}.
     */
    @Test
    public void testSkip() throws IOException {
        InputStream input = new ByteArrayInputStream(new byte[] { (byte) 0x61, (byte) 0x62 });
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        when(baseNCodec.hasData()).thenReturn(true);
        when(baseNCodec.decode(anyByteArray(), anyInt(), anyInt())).thenReturn(new byte[] { 0x61, 0x62 });

        inputStream.skip(5);

        assertEquals(2, inputStream.read(new byte[2][], 0, 2).length);
    }

    /**
     * Test {@link BaseNCodecInputStream#close()}.
     */
    @Test
    public void testClose() {
        InputStream input = mock(InputStream.class);
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        inputStream.close();

        verify(input).close();
    }

    /**
     * Test {@link BaseNCodecInputStream#read(byte[], int, int) with negative offset}.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadNegativeOffset() throws IOException {
        InputStream input = mock(InputStream.class);
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        inputStream.read(new byte[1][], -1, 1);
    }

    /**
     * Test {@link BaseNCodecInputStream#read(byte[], int, int) with negative length}.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadNegativeLength() throws IOException {
        InputStream input = mock(InputStream.class);
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        inputStream.read(new byte[1][], 0, -1);
    }

    /**
     * Test {@link BaseNCodecInputStream#read(byte[], int, int) with invalid length}.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidLength() throws IOException {
        InputStream input = mock(InputStream.class);
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        inputStream.read(new byte[1][], 0, input.available() + 1);
    }

    /**
     * Test {@link BaseNCodecInputStream#read(byte[], int, int) with null byte array}.
     */
    @Test(expected = NullPointerException.class)
    public void testReadNullByteArray() throws IOException {
        InputStream input = mock(InputStream.class);
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        inputStream.read(null, 0, 1);
    }

    /**
     * Test {@link BaseNCodecInputStream#read(byte[], int, int) with EOF}.
     */
    @Test
    public void testReadEOF() throws IOException {
        InputStream input = mock(InputStream.class);
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        when(baseNCodec.hasData()).thenReturn(false);

        byte[] buffer = new byte[1];
        int bytesRead = inputStream.read(buffer, 0, 1);

        assertEquals(BaseNCodecInputStream.EOF, bytesRead);
    }

    /**
     * Test {@link BaseNCodecInputStream#read(byte[], int, int) with empty buffer}.
     */
    @Test
    public void testReadEmptyBuffer() throws IOException {
        InputStream input = mock(InputStream.class);
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNCodec, false);

        byte[] buffer = new byte[0];
        int bytesRead = inputStream.read(buffer, 0, 1);

        assertEquals(0, bytesRead);
    }

    /**
     * Test {@link BaseNCodecInputStream#read(byte[], int, int) with BaseNCodec.decode returning empty byte array}.
     */
    @Test
    public void testReadEmptyByteArray() throws IOException {
        InputStream input = mock(InputStream.class);
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNcodec, false);

        when(baseNCodec.hasData()).thenReturn(true);
        when(baseNCodec.decode(anyByteArray(), anyInt(), anyInt())).thenReturn(new byte[0]);

        byte[] buffer = new byte[1];
        int bytesRead = inputStream.read(buffer, 0, 1);

        assertEquals(0, bytesRead);
    }

    /**
     * Test {@link BaseNCodecInputStream#read(byte[], int, int) with BaseNCodec.decode returning null}.
     */
    @Test
    public void testReadNullByteArrayFromDecode() throws IOException {
        InputStream input = mock(InputStream.class);
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNcodec, false);

        when(baseNCodec.hasData()).thenReturn(true);
        when(baseNCodec.decode(anyByteArray(), anyInt(), anyInt()))
                .thenThrow(new NullPointerException());

        byte[] buffer = new byte[1];
        int bytesRead = inputStream.read(buffer, 0, 1);

        assertEquals(BaseNCodecInputStream.EOF, bytesRead);
    }

    /**
     * Test {@link BaseNCodecInputStream#read(byte[], int, int) with BaseNCodec.decode throwing IOException}.
     */
    @Test
    public void testReadIOExceptionFromDecode() throws IOException {
        InputStream input = mock(InputStream.class);
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNcodec, false);

        when(baseNCodec.hasData()).thenReturn(true);
        when(baseNCodec.decode(anyByteArray(), anyInt(), anyInt()))
                .thenThrow(new IOException());

        byte[] buffer = new byte[1];
        int bytesRead = inputStream.read(buffer, 0, 1);

        assertEquals(BaseNCodecInputStream.EOF, bytesRead);
    }

    /**
     * Test {@link BaseNCodecInputStream#markSupported()} with markSupported() returning false.
     */
    @Test
    public void testMarkSupportedReturnsFalse() {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(new ByteArrayInputStream(new byte[0]),
                mock(BaseNCodec.class), false);

        assertFalse(inputStream.markSupported());
    }

    /**
     * Test {@link BaseNCodecInputStream#skip(long)} with skip() throwing IOException.
     */
    @Test
    public void testSkipIOException() throws IOException {
        InputStream input = mock(InputStream.class);
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNcodec, false);

        when(input.skip(anyLong())).thenThrow(new IOException());

        assertThrows(IOException.class, () -> inputStream.skip(1));
    }

    /**
     * Test {@link BaseNCodecInputStream#skip(long)} with skip() returning a negative number.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testSkipNegativeNumber() throws IOException {
        InputStream input = mock(InputStream.class);
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNcodec, false);

        inputStream.skip(-1);
    }

    /**
     * Test {@link BaseNCodecInputStream#toString()}.
     */
    @Test
    public void testToString() {
        InputStream input = mock(InputStream.class);
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNcodec, false);

        String expected = "BaseNCodecInputStream{"
                + "in=" + input.getClass().getName()
                + ", baseNCodec=" + baseNCodec.getClass().getName()
                + ", doEncode=false" + '}';

        assertEquals(expected, inputStream.toString());
    }

    /**
     * Test {@link BaseNCodecInputStream#close()}.
     */
    @Test
    public void testClose() throws IOException {
        InputStream input = mock(InputStream.class);
        BaseNCodec baseNCodec = mock(BaseNCodec.class);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(input, baseNcodec, false);

        inputStream.close();

        verify(input).close();
    }
}