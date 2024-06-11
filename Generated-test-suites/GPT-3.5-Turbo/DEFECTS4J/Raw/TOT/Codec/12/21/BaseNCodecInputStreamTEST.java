import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class BaseNCodecInputStreamTest {

    @Mock
    private BaseNCodec baseNCodec;
    private BaseNCodecInputStream inputStream;
    private InputStream mockInputStream;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockInputStream = mock(InputStream.class);
        inputStream = new BaseNCodecInputStream(mockInputStream, baseNCodec, false);
    }

    @Test
    public void testRead_NormalCase_ReturnsByte() throws IOException {
        byte[] data = {1, 2, 3};
        when(mockInputStream.read()).thenReturn(1);
        int result = inputStream.read();
        assertEquals(1, result);
    }

    @Test
    public void testRead_EmptyInputStream_ReturnsEOF() throws IOException {
        when(mockInputStream.read()).thenReturn(-1);
        int result = inputStream.read();
        assertEquals(-1, result);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_InvalidOffset_ThrowsIndexOutOfBoundsException() throws IOException {
        byte[] data = {1, 2, 3};
        inputStream.read(data, 4, 2);
    }

    @Test
    public void testRead_NormalCase_ReadsBytesIntoArray() throws IOException {
        byte[] data = new byte[3];
        byte[] expectedResult = {1, 2, 3};
        when(mockInputStream.read(data, 0, 3)).thenReturn(3);
        int result = inputStream.read(data, 0, 3);
        assertEquals(3, result);
        assertArrayEquals(expectedResult, data);
    }

    @Test(expected = NullPointerException.class)
    public void testRead_NullArray_ThrowsNullPointerException() throws IOException {
        inputStream.read(null, 0, 3);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_InvalidLength_ThrowsIndexOutOfBoundsException() throws IOException {
        byte[] data = {1, 2, 3};
        inputStream.read(data, 0, 4);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRead_InvalidOffsetAndLength_ThrowsIndexOutOfBoundsException() throws IOException {
        byte[] data = {1, 2, 3};
        inputStream.read(data, 4, 4);
    }

    @Test
    public void testMarkSupported_ReturnsFalse() {
        boolean result = inputStream.markSupported();
        assertFalse(result);
    }

    @Test
    public void testAvailable_InputStreamNotEOF_Returns1() throws IOException {
        when(mockInputStream.read()).thenReturn(1);
        int result = inputStream.available();
        assertEquals(1, result);
    }

    @Test
    public void testAvailable_InputStreamEOF_Returns0() throws IOException {
        when(mockInputStream.read()).thenReturn(-1);
        int result = inputStream.available();
        assertEquals(0, result);
    }

    @Test
    public void testAvailable_InputStreamHasData_Returns1() throws IOException {
        when(baseNCodec.hasData()).thenReturn(true);
        int result = inputStream.available();
        assertEquals(1, result);
    }

    @Test
    public void testAvailable_InputStreamDoesNotHaveData_Returns0() throws IOException {
        when(baseNCodec.hasData()).thenReturn(false);
        int result = inputStream.available();
        assertEquals(0, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSkip_NegativeLength_ThrowsIllegalArgumentException() throws IOException {
        inputStream.skip(-10);
    }

    @Test
    public void testSkip_NormalCase_SkipsBytes() throws IOException {
        when(mockInputStream.skip(512)).thenReturn(512L);
        long result = inputStream.skip(512);
        assertEquals(512L, result);
    }

    @Test
    public void testSkip_InputStreamEOF_Returns0() throws IOException {
        when(mockInputStream.skip(512)).thenReturn(0L);
        long result = inputStream.skip(512);
        assertEquals(0L, result);
    }
    
    @Test
    public void testBaseNCodecInputStreamConstructor_ValidParameters_CreatesInstance() {
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(mockInputStream, baseNCodec, false);
        assertNotNull(inputStream);
        assertEquals(mockInputStream, inputStream.in);
        assertEquals(baseNCodec, inputStream.baseNCodec);
        assertEquals(false, inputStream.doEncode);
    }

}