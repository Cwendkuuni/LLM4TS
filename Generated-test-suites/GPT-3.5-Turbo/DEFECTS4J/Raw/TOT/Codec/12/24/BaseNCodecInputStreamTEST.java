import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.apache.commons.codec.binary.BaseNCodec;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class BaseNCodecInputStreamTest {

    @Mock
    private BaseNCodec mockBaseNCodec;

    private BaseNCodecInputStream baseNCodecInputStream;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        InputStream mockInputStream = new ByteArrayInputStream(new byte[0]);
        baseNCodecInputStream = new BaseNCodecInputStream(mockInputStream, mockBaseNCodec, true);
    }

    @Test
    public void testRead_singleByte() throws Exception {
        when(mockBaseNCodec.hasData()).thenReturn(true);
        when(mockBaseNCodec.readResults(any(byte[].class), anyInt(), anyInt())).thenReturn(1);
        int result = baseNCodecInputStream.read();
        assertEquals(1, result);
    }

    @Test
    public void testRead_noData() throws Exception {
        when(mockBaseNCodec.hasData()).thenReturn(false);
        int result = baseNCodecInputStream.read();
        assertEquals(-1, result);
    }

    @Test
    public void testRead_buffer() throws Exception {
        when(mockBaseNCodec.hasData()).thenReturn(true);
        when(mockBaseNCodec.readResults(any(byte[].class), anyInt(), anyInt())).thenReturn(5);
        byte[] buffer = new byte[10];
        int result = baseNCodecInputStream.read(buffer, 0, 10);
        assertEquals(5, result);
    }

    @Test(expected = NullPointerException.class)
    public void testRead_nullBuffer() throws Exception {
        baseNCodecInputStream.read(null, 0, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_negativeOffset() throws Exception {
        byte[] buffer = new byte[10];
        baseNCodecInputStream.read(buffer, -1, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_negativeLen() throws Exception {
        byte[] buffer = new byte[10];
        baseNCodecInputStream.read(buffer, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_offsetOutOfRange() throws Exception {
        byte[] buffer = new byte[10];
        baseNCodecInputStream.read(buffer, 11, 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_offsetPlusLenOutOfRange() throws Exception {
        byte[] buffer = new byte[10];
        baseNCodecInputStream.read(buffer, 5, 6);
    }

    @Test
    public void testRead_zeroLen() throws Exception {
        byte[] buffer = new byte[10];
        int result = baseNCodecInputStream.read(buffer, 0, 0);
        assertEquals(0, result);
    }

    @Test
    public void testMarkSupported() throws Exception {
        assertFalse(baseNCodecInputStream.markSupported());
    }

    @Test
    public void testAvailable_eof() throws Exception {
        when(mockBaseNCodec.hasData()).thenReturn(false);
        int result = baseNCodecInputStream.available();
        assertEquals(0, result);
    }

    @Test
    public void testAvailable_dataAvailable() throws Exception {
        when(mockBaseNCodec.hasData()).thenReturn(true);
        int result = baseNCodecInputStream.available();
        assertEquals(1, result);
    }
}