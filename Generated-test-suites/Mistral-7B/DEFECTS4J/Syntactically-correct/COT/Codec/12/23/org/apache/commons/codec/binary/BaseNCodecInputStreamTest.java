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

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link BaseNCodecInputStream}.
 */
public class BaseNCodecInputStreamTest {

    private InputStream inputStream;
    private BaseNCodec baseNCodec;
    private BaseNCodecInputStream baseNCodecInputStream;

    @Before
    public void setUp() {
        inputStream = new ByteArrayInputStream(new byte[0]);
        baseNCodec = mock(BaseNCodec.class);
        baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
    }

    @Test
    public void testRead() throws IOException {
        when(baseNCodec.hasData()).thenReturn(false);
        // test reading EOF
        assertEquals(BaseNCodecInputStream.EOF, baseNCodecInputStream.read());

        when(baseNCodec.hasData()).thenReturn(true);
        when(baseNCodec.decode(new byte[0], 0, 1)).thenReturn(1);
        assertEquals(1, baseNCodecInputStream.read());

        when(baseNCodec.decode(new byte[0], 0, 1)).thenReturn(256);
        assertEquals(257, baseNCodecInputStream.read());

        when(baseNCodec.decode(new byte[0], 0, 1)).thenThrow(new IOException());
        assertThrows(IOException.class, () -> baseNCodecInputStream.read());
    }

    @Test
    public void testReadByteArray() throws IOException {
        when(baseNCodec.hasData()).thenReturn(false);

        byte[] b = new byte[1];
        assertEquals(0, baseNCodecInputStream.read(b, 0, 1));

        when(baseNCodec.hasData()).thenReturn(true);
        when(baseNCodec.decode(new byte[0], 0, 1)).thenReturn(1);
        assertEquals(1, baseNCodecInputStream.read(b, 0, 1));

        when(baseNCodec.decode(new byte[0], 0, 1)).thenReturn(256);
        assertEquals(1, baseNCodecInputStream.read(b, 0, 1));
        assertEquals(257, b[0]);

        when(baseNCodec.decode(new byte[0], 0, 1)).thenThrow(new IOException());
        assertThrows(IOException.class, () -> baseNCodecInputStream.read(b, 0, 1));

        when(baseNCodec.decode(new byte[0], 0, 2)).thenReturn(1, 256);
        assertEquals(2, baseNCodecInputStream.read(b, 0, 2));
        assertEquals(1, b[0]);
        assertEquals(257, b[1]);

        when(baseNCodec.decode(new byte[0], 0, 2)).thenReturn(1, 256, 256);
        assertEquals(2, baseNCodecInputStream.read(b, 0, 2));
        assertEquals(1, b[0]);
        assertEquals(257, b[1]);
        assertEquals(257, b[1]);
    }

    @Test
    public void testMarkSupported() {
        assertFalse(baseNCodecInputStream.markSupported());
    }

    @Test
    public void testSkip() throws IOException {
        // not an easy job to test as it uses the EOF flag of the underlying codec instance
        // and the logic is similar to the InflaterInputStream
        // just ensure that the method does not throw an exception
        baseNCodecInputStream.skip(1);
    }

    @Test
    public void testClose() throws IOException {
        baseNCodecInputStream.close();
        baseNCodecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);
        baseNCodecInputStream.close();
    }
}