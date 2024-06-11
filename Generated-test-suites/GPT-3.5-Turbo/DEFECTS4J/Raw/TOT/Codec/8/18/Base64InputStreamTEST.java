import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class Base64InputStreamTest {

    @Mock
    private InputStream mockInputStream;

    @Test
    public void testRead() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(mockInputStream);

        when(mockInputStream.read()).thenReturn(65);
        int result = base64InputStream.read();
        assertEquals(65, result);

        verify(mockInputStream, times(1)).read();
    }

    @Test
    public void testReadWithNegativeValue() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(mockInputStream);

        when(mockInputStream.read()).thenReturn(-1);
        int result = base64InputStream.read();
        assertEquals(-1, result);

        verify(mockInputStream, times(1)).read();
    }

    @Test
    public void testReadWithZeroLengthBuffer() throws IOException {
        byte[] buffer = new byte[0];
        Base64InputStream base64InputStream = new Base64InputStream(mockInputStream);

        int result = base64InputStream.read(buffer, 0, 0);
        assertEquals(0, result);

        verifyZeroInteractions(mockInputStream);
    }

    @Test(expected = NullPointerException.class)
    public void testReadWithNullBuffer() throws IOException {
        Base64InputStream base64InputStream = new Base64InputStream(mockInputStream);

        base64InputStream.read(null, 0, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeOffset() throws IOException {
        byte[] buffer = new byte[10];
        Base64InputStream base64InputStream = new Base64InputStream(mockInputStream);

        base64InputStream.read(buffer, -1, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithNegativeLength() throws IOException {
        byte[] buffer = new byte[10];
        Base64InputStream base64InputStream = new Base64InputStream(mockInputStream);

        base64InputStream.read(buffer, 0, -1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidOffset() throws IOException {
        byte[] buffer = new byte[10];
        Base64InputStream base64InputStream = new Base64InputStream(mockInputStream);

        base64InputStream.read(buffer, 20, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadWithInvalidLength() throws IOException {
        byte[] buffer = new byte[10];
        Base64InputStream base64InputStream = new Base64InputStream(mockInputStream);

        base64InputStream.read(buffer, 0, 20);
    }

    @Test
    public void testReadWithDataInBase64() throws IOException {
        byte[] buffer = new byte[10];
        byte[] input = new byte[] {65, 66, 67, 68, 69, 70, 71, 72, 73, 74};
        Base64InputStream base64InputStream = new Base64InputStream(mockInputStream);

        when(mockInputStream.read(any(byte[].class))).thenReturn(10);
        when(base64.hasData()).thenReturn(true, false);
        when(base64.readResults(any(byte[].class), anyInt(), anyInt())).thenReturn(10);

        int result = base64InputStream.read(buffer, 0, 10);

        assertArrayEquals(input, buffer);
        assertEquals(10, result);

        verify(mockInputStream, times(1)).read(buffer);
    }

    @Test
    public void testReadWithEncodedData() throws IOException {
        byte[] buffer = new byte[10];
        byte[] input = new byte[] {65, 66, 67, 68, 69, 70, 71, 72, 73, 74};
        Base64InputStream base64InputStream = new Base64InputStream(mockInputStream, true, 0, null);

        when(mockInputStream.read(any(byte[].class))).thenReturn(10);
        when(base64.hasData()).thenReturn(true, false);
        when(base64.readResults(any(byte[].class), anyInt(), anyInt())).thenReturn(10);

        int result = base64InputStream.read(buffer, 0, 10);

        assertArrayEquals(input, buffer);
        assertEquals(10, result);

        verify(mockInputStream, times(1)).read(buffer);
    }

    @Test
    public void testReadWithDecodedData() throws IOException {
        byte[] buffer = new byte[10];
        byte[] input = new byte[] {65, 66, 67, 68, 69, 70, 71, 72, 73, 74};
        Base64InputStream base64InputStream = new Base64InputStream(mockInputStream, false, 0, null);

        when(mockInputStream.read(any(byte[].class))).thenReturn(10);
        when(base64.hasData()).thenReturn(true, false);
        when(base64.readResults(any(byte[].class), anyInt(), anyInt())).thenReturn(10);

        int result = base64InputStream.read(buffer, 0, 10);

        assertArrayEquals(input, buffer);
        assertEquals(10, result);

        verify(mockInputStream, times(1)).read(buffer);
    }

    @Test
    public void testMarkSupported() {
        Base64InputStream base64InputStream = new Base64InputStream(mockInputStream);

        boolean result = base64InputStream.markSupported();

        assertFalse(result);
    }
}