import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.apache.commons.codec.binary.BaseNCodec;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;


public class BaseNCodecInputStreamTest {
    
    private BaseNCodec baseNCodec;
    private InputStream inputStream;

    @Before
    public void setUp() {
        baseNCodec = mock(BaseNCodec.class);
        inputStream = mock(InputStream.class);
    }

    @Test
    public void testRead() throws Exception {
        byte[] singleByte = new byte[1];
        when(inputStream.read(singleByte, 0, 1)).thenReturn(1).thenReturn(-1);
        when(baseNCodec.hasData()).thenReturn(true).thenReturn(false);
        when(baseNCodec.readResults(eq(singleByte), eq(0), eq(1))).thenReturn(1).thenReturn(-1);

        BaseNCodecInputStream codecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        assertEquals(0, codecInputStream.read());
        assertEquals(-1, codecInputStream.read());
    }

    @Test
    public void testReadWithZeroReadResults() throws Exception {
        byte[] singleByte = new byte[1];
        when(inputStream.read(singleByte, 0, 1)).thenReturn(1).thenReturn(0).thenReturn(-1);
        when(baseNCodec.hasData()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(baseNCodec.readResults(eq(singleByte), eq(0), eq(1))).thenReturn(0).thenReturn(1).thenReturn(-1);

        BaseNCodecInputStream codecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        assertEquals(0, codecInputStream.read());
        assertEquals(0, codecInputStream.read());
        assertEquals(1, codecInputStream.read());
        assertEquals(-1, codecInputStream.read());
    }

    @Test
    public void testReadWithMultipleChunks() throws Exception {
        byte[] singleByte = new byte[1];
        when(inputStream.read(singleByte, 0, 1)).thenReturn(1).thenReturn(2).thenReturn(-1);
        when(baseNCodec.hasData()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        when(baseNCodec.readResults(eq(singleByte), eq(0), eq(1))).thenReturn(1);
        when(baseNCodec.readResults(eq(singleByte), eq(0), eq(1))).thenReturn(2);
        when(baseNCodec.readResults(eq(singleByte), eq(0), eq(1))).thenReturn(-1);

        BaseNCodecInputStream codecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        assertEquals(0, codecInputStream.read());
        assertEquals(1, codecInputStream.read());
        assertEquals(2, codecInputStream.read());
        assertEquals(-1, codecInputStream.read());
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullByteArray() throws Exception {
        BaseNCodecInputStream codecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        codecInputStream.read(null, 0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeOffset() throws Exception {
        BaseNCodecInputStream codecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        codecInputStream.read(new byte[1], -1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeLength() throws Exception {
        BaseNCodecInputStream codecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        codecInputStream.read(new byte[1], 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws Exception {
        BaseNCodecInputStream codecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        codecInputStream.read(new byte[1], 2, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidBounds() throws Exception {
        BaseNCodecInputStream codecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        codecInputStream.read(new byte[1], 0, 2);
    }

    @Test
    public void testReadWithZeroLength() throws Exception {
        BaseNCodecInputStream codecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        assertEquals(0, codecInputStream.read(new byte[1], 0, 0));
    }

    @Test
    public void testReadWithEmptyBuffer() throws Exception {
        BaseNCodecInputStream codecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        assertEquals(0, codecInputStream.read(new byte[0], 0, 0));
    }

    @Test
    public void testReadWithNoData() throws Exception {
        byte[] singleByte = new byte[1];
        when(inputStream.read(singleByte, 0, 1)).thenReturn(0).thenReturn(1).thenReturn(-1);
        when(baseNCodec.hasData()).thenReturn(false).thenReturn(true).thenReturn(false);
        when(baseNCodec.readResults(any(byte[].class), anyInt(), anyInt())).thenReturn(0).thenReturn(1);

        BaseNCodecInputStream codecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        assertEquals(1, codecInputStream.read());
        assertEquals(-1, codecInputStream.read());
    }

    @Test
    public void testMarkSupported() {
        BaseNCodecInputStream codecInputStream = new BaseNCodecInputStream(inputStream, baseNCodec, false);

        assertEquals(false, codecInputStream.markSupported());
    }
}