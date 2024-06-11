import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class BaseNCodecInputStreamTest {

    @Mock
    private InputStream mockInputStream;

    private BaseNCodec mockBaseNCodec;

    private BaseNCodecInputStream baseNCodecInputStream;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mockBaseNCodec = mock(BaseNCodec.class);
        when(mockBaseNCodec.hasData()).thenReturn(true);
        baseNCodecInputStream = new BaseNCodecInputStream(mockInputStream, mockBaseNCodec, true);
    }

    @Test
    public void testRead() throws IOException {
        when(mockInputStream.read()).thenReturn(65);
        int result = baseNCodecInputStream.read();
        verify(mockInputStream, times(1)).read();
        assertEquals(65, result);
    }

    @Test
    public void testReadWithEOF() throws IOException {
        when(mockInputStream.read()).thenReturn(-1);
        int result = baseNCodecInputStream.read();
        verify(mockInputStream, times(1)).read();
        assertEquals(-1, result);
    }

    @Test
    public void testReadWithZeroBytes() throws IOException {
        when(mockInputStream.read(any(byte[].class), anyInt(), anyInt())).thenReturn(0).thenReturn(65);
        int result = baseNCodecInputStream.read(new byte[1], 0, 1);
        verify(mockInputStream, times(2)).read(any(byte[].class), anyInt(), anyInt());
        assertEquals(65, result);
    }

    @Test
    public void testReadWithNonZeroBytes() throws IOException {
        when(mockInputStream.read(any(byte[].class), anyInt(), anyInt())).thenReturn(1);
        int result = baseNCodecInputStream.read(new byte[1], 0, 1);
        verify(mockInputStream, times(1)).read(any(byte[].class), anyInt(), anyInt());
        assertEquals(1, result);
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullByteArray() throws IOException {
        baseNCodecInputStream.read(null, 0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeOffset() throws IOException {
        baseNCodecInputStream.read(new byte[1], -1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeLength() throws IOException {
        baseNCodecInputStream.read(new byte[1], 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        baseNCodecInputStream.read(new byte[1], 2, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLength() throws IOException {
        baseNCodecInputStream.read(new byte[1], 0, 2);
    }

    @Test
    public void testMarkSupported() {
        assertFalse(baseNCodecInputStream.markSupported());
    }
}