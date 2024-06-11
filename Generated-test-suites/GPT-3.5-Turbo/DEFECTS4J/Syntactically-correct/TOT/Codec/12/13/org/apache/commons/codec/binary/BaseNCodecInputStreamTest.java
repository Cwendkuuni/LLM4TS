import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BaseNCodecInputStreamTest {

    private BaseNCodecInputStream inputStream;

    @Mock
    private BaseNCodec baseNCodec;

    @Before
    public void setUp() {
        InputStream in = new ByteArrayInputStream(new byte[10]);
        baseNCodec = mock(BaseNCodec.class);
        inputStream = new BaseNCodecInputStream(in, baseNCodec, true);
    }

    @Test
    public void testRead_NormalInput_ReturnsByte() throws IOException {
        when(baseNCodec.hasData()).thenReturn(true);
        when(baseNCodec.readResults(any(byte[].class), anyInt(), anyInt())).thenReturn(1);

        int result = inputStream.read();

        assertEquals(0, result);
    }

    @Test
    public void testRead_NoDataAvailable_ReturnsEOF() throws IOException {
        when(baseNCodec.hasData()).thenReturn(false);

        int result = inputStream.read();

        assertEquals(-1, result);
    }

    @Test(expected = NullPointerException.class)
    public void testRead_NullDestinationArray_ThrowsNullPointerException() throws IOException {
        inputStream.read(null, 0, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_InvalidOffset_ThrowsIndexOutOfBoundsException() throws IOException {
        inputStream.read(new byte[10], -1, 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_InvalidLength_ThrowsIndexOutOfBoundsException() throws IOException {
        inputStream.read(new byte[10], 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_InvalidBufferSize_ThrowsIndexOutOfBoundsException() throws IOException {
        inputStream.read(new byte[10], 0, 11);
    }

    @Test
    public void testRead_ZeroLength_ReturnsZero() throws IOException {
        int result = inputStream.read(new byte[10], 0, 0);

        assertEquals(0, result);
    }

    @Test
    public void testRead_NormalInput_ReturnsNumberOfBytesRead() throws IOException {
        when(baseNCodec.hasData()).thenReturn(true);
        when(baseNCodec.readResults(any(byte[].class), anyInt(), anyInt())).thenReturn(5);

        byte[] buffer = new byte[10];
        int result = inputStream.read(buffer, 0, 10);

        assertEquals(5, result);
    }

    @Test
    public void testMarkSupported_AlwaysReturnsFalse() {
        assertFalse(inputStream.markSupported());
    }

    @Test
    public void testAvailable_WithMoreData_Returns1() throws IOException {
        when(baseNCodec.hasData()).thenReturn(true);

        int result = inputStream.available();

        assertEquals(1, result);
    }

    @Test
    public void testAvailable_NoMoreData_Returns0() throws IOException {
        when(baseNCodec.hasData()).thenReturn(false);

        int result = inputStream.available();

        assertEquals(0, result);
    }

}