import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class Base64InputStreamTest {

    private InputStream mockedInputStream;
    private Base64InputStream base64InputStream;

    @Before
    public void setUp() {
        mockedInputStream = mock(InputStream.class);
        base64InputStream = new Base64InputStream(mockedInputStream);
    }

    @Test
    public void testRead_WhenInputStreamReturnsPositiveValue_ReturnsDecodedByte() throws Exception {
        when(mockedInputStream.read(any(byte[].class), anyInt(), anyInt())).thenReturn(80);
        
        int result = base64InputStream.read();

        assertEquals(80, result);
    }

    @Test
    public void testRead_WhenInputStreamReturnsNegativeValue_ReturnsNegativeOne() throws Exception {
        when(mockedInputStream.read(any(byte[].class), anyInt(), anyInt())).thenReturn(-1);
        
        int result = base64InputStream.read();

        assertEquals(-1, result);
    }

    @Test
    public void testRead_WhenInputStreamReturnsZero_ReadsAgain() throws Exception {
        when(mockedInputStream.read(any(byte[].class), anyInt(), anyInt())).thenReturn(0, 80);
        
        int result = base64InputStream.read();

        assertEquals(80, result);
    }

    @Test
    public void testRead_WhenByteArrayIsNull_ThrowsNullPointerException() throws Exception {
        try {
            base64InputStream.read(null);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // test passed
        }
    }

    @Test
    public void testRead_WhenOffsetAndLengthAreNegative_ThrowsIndexOutOfBoundsException() throws Exception {
        try {
            base64InputStream.read(new byte[10], -1, -1);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            // test passed
        }
    }

    @Test
    public void testRead_WhenOffsetIsGreaterThanArrayLength_ThrowsIndexOutOfBoundsException() throws Exception {
        try {
            base64InputStream.read(new byte[10], 11, 5);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            // test passed
        }
    }

    @Test
    public void testRead_WhenOffsetPlusLengthIsGreaterThanArrayLength_ThrowsIndexOutOfBoundsException() throws Exception {
        try {
            base64InputStream.read(new byte[10], 5, 10);
            fail("Expected IndexOutOfBoundsException to be thrown");
        } catch (IndexOutOfBoundsException e) {
            // test passed
        }
    }

    @Test
    public void testRead_WhenLengthIsZero_ReturnsZero() throws Exception {
        int result = base64InputStream.read(new byte[10], 0, 0);
        assertEquals(0, result);
    }

    @Test
    public void testRead_WhenDecodeIsFalse_ReadsDataFromInputStreamAndReturnsDecodedBytes() throws Exception {
        Base64 base64Mock = mock(Base64.class);
        byte[] buf = new byte[10];
        when(mockedInputStream.read(buf)).thenReturn(10);
        whenNew(Base64.class).withArguments(false).thenReturn(base64Mock);

        base64InputStream = new Base64InputStream(mockedInputStream, false);

        base64InputStream.read(buf);

        verify(base64Mock).decode(buf, 0, 10);
    }

    @Test
    public void testRead_WhenDecodeIsTrue_ReadsDataFromInputStreamAndReturnsEncodedBytes() throws Exception {
        Base64 base64Mock = mock(Base64.class);
        byte[] buf = new byte[10];
        when(mockedInputStream.read(buf)).thenReturn(10);
        whenNew(Base64.class).withArguments(true).thenReturn(base64Mock);

        base64InputStream = new Base64InputStream(mockedInputStream, true);

        base64InputStream.read(buf);

        verify(base64Mock).encode(buf, 0, 10);
    }

    @Test
    public void testRead_WhenBase64HasData_ReturnsDecodedBytesToBuffer() throws Exception {
        Base64 base64Mock = mock(Base64.class);
        byte[] buf = new byte[10];
        when(mockedInputStream.read(buf)).thenReturn(10);
        whenNew(Base64.class).withArguments(false).thenReturn(base64Mock);
        when(base64Mock.hasData()).thenReturn(true);

        base64InputStream.read(buf);

        verify(base64Mock).readResults(buf, 0, 10);
    }

    @Test
    public void testMarkSupported_Always_ReturnsFalse() {
        boolean result = base64InputStream.markSupported();

        assertFalse(result);
    }
}