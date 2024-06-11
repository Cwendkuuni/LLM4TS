package org.apache.commons.codec.binary;

import static org.apache.commons.codec.binary.BaseNCodec.EOF;
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

import org.junit.Before;
import org.junit.Test;

/**
 * Test cases for {@link BaseNCodecInputStream}.
 */
public class BaseNCodecInputStreamTest {

    private BaseNCodec baseNCodec;
    private InputStream inputStream;
    private BaseNCodecInputStream baseNCodecInputStream;

    @Before
    public void setUp() {
        baseNCodec = mock(BaseNCodec.class);
        inputStream = new ByteArrayInputStream(new byte[] {});
        baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
    }

    @Test
    public void testRead() throws IOException {
        when(baseNCodec.hasData()).thenReturn(true);
        when(baseNCodec.decode(new byte[1], 0, 1)).thenReturn(EOF);

        assertEquals(EOF, baseNCodecInputStream.read());
        assertEquals(EOF, baseNCodecInputStream.read());
    }

    @Test
    public void testReadByteArray() throws IOException {
        when(baseNCodec.hasData()).thenReturn(true);
        when(baseNCodec.decode(new byte[1], 0, 1)).thenReturn((byte) 0xAB);
        when(baseNCodec.decode(new byte[1], 0, 1)).thenReturn(EOF);
        when(baseNCodec.decode(new byte[1], 0, 1)).thenReturn((byte) 0xCD);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[3];

        int read = baseNCodecInputStream.read(buffer, 0, buffer.length);
        outputStream.write(buffer, 0, read);

        assertEquals(2, read);
        assertEquals((byte) 0xAB, outputStream.toByteArray()[0]);
        assertEquals((byte) 0xCD, outputStream.toByteArray()[1]);

        // test EOF
        read = baseNCodecInputStream.read(buffer, 0, buffer.length);
        assertEquals(0, read);
    }

    @Test
    public void testMarkSupported() {
        assertFalse(baseNCodecInputStream.markSupported());
    }

    @Test
    public void testSkip() throws IOException {
        when(baseNCodec.hasData()).thenReturn(true);
        when(baseNCodec.decode(new byte[1], 0, 1)).thenReturn((byte) 0xAB);

        baseNCodecInputStream.skip(512);

        // we don't know how much data is read, so we just return 1 as a safe guess
        verify(baseNCodecInputStream).read();
        verify(baseNCodecInputStream).read();
        verify(baseNCodecInputStream).read();
        verify(baseNCodecInputStream).read();
        verify(baseNCodecInputStream).read();
    }

    @Test
    public void testNegativeSkip() {
        assertThrows(IllegalArgumentException.class, () -> baseNCodecInputStream.skip(-1));
    }

    @Test
    public void testEncodeInputStream() throws IOException {
        BaseNCodecOutputStream baseNCodecOutputStream = mock(BaseNCodecOutputStream.class);
        BaseNCodec baseNCodecEncode = mock(BaseNCodec.class);

        when(baseNCodecEncode.encode(anyByte(), anyInt(), anyInt())).thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0)[0]);
        when(baseNCodecEncode.hasData()).thenReturn(true);

        byte[] input = new byte[] { 0x01, 0x02 };
        InputStream inputStream = new ByteArrayInputStream(input);
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodecEncode, true);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        BaseNCodecOutputStream baseNCodecOutputStream2 = new BaseNCodecOutputStream(outputStream, baseNCodecEncode);

        baseNCodecInputStream.copyTo(baseNCodecOutputStream2);

        byte[] expectedOutput = new byte[] { 0x2B, 0x11, 0x2B, 0x01, 0x2B, 0x12, 0x1B, 0x02 };
        assertEquals(expectedOutput.length, outputStream.size());
        assertEquals(expectedOutput, outputStream.toByteArray());
    }

}