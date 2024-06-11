
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito.*;

import java.io.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class BaseNCodecInputStreamTest {

    private InputStream inputStream;

    @Before
    public void setUp() {
        inputStream = mock(InputStream.class);
    }

    @Test
    public void testReadOneByte() throws IOException {
        byte[] expectedBytes = new byte[]{65}; // ASCII 'A'
        when(inputStream.read()).thenReturn(65);

        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, new BaseNCodec(), true);
        int result = baseNCodecInputStream.read();
        baseNCodecInputStream.close();

        verify(inputStream, times(1)).read();
        assertEquals(expectedBytes[0], (byte) result);
    }

    @Test
    public void testReadMultipleBytes() throws IOException {
        byte[] expectedBytes = {84, 101, 115, 116}; // ASCII 'Test'
        when(inputStream.read(any(byte[].class), anyInt(), anyInt())).thenAnswer(invocation -> {
            byte[] buffer = invocation.getArgument(0);
            buffer[0] = 84;
            buffer[1] = 101;
            buffer[2] = 115;
            buffer[3] = 116;
            return 4;
        });

        byte[] buffer = new byte[4];
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, new BaseNCodec(), true);
        int result = baseNCodecInputStream.read(buffer, 0, 4);
        baseNCodecInputStream.close();

        verify(inputStream, times(1)).read(any(byte[].class), anyInt(), anyInt());
        assertEquals(expectedBytes[0], buffer[0]);
        assertEquals(expectedBytes[1], buffer[1]);
        assertEquals(expectedBytes[2], buffer[2]);
        assertEquals(expectedBytes[3], buffer[3]);
        assertEquals(4, result);
    }

    @Test(expected = NullPointerException.class)
    public void testReadNullByteArray() throws IOException {
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, new BaseNCodec(), true);
        baseNCodecInputStream.read(null, 0, 4);
        baseNCodecInputStream.close();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidOffset() throws IOException {
        byte[] buffer = new byte[4];
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, new BaseNCodec(), true);
        baseNCodecInputStream.read(buffer, -1, 4);
        baseNCodecInputStream.close();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidLength() throws IOException {
        byte[] buffer = new byte[4];
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, new BaseNCodec(), true);
        baseNCodecInputStream.read(buffer, 0, -1);
        baseNCodecInputStream.close();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidBufferSize() throws IOException {
        byte[] buffer = new byte[4];
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, new BaseNCodec(), true);
        baseNCodecInputStream.read(buffer, 0, 5);
        baseNCodecInputStream.close();
    }

    @Test
    public void testReadEmptyByteArray() throws IOException {
        byte[] buffer = new byte[0];
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, new BaseNCodec(), true);
        int result = baseNCodecInputStream.read(buffer, 0, 0);
        baseNCodecInputStream.close();

        assertEquals(0, result);
        verify(inputStream, never()).read();
    }

    @Test
    public void testReadZeroBytes() throws IOException {
        byte[] buffer = new byte[4];
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, new BaseNCodec(), true);
        int result = baseNCodecInputStream.read(buffer, 0, 0);
        baseNCodecInputStream.close();

        assertEquals(0, result);
        verify(inputStream, never()).read();
    }

    @Test
    public void testMarkNotSupported() {
        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, new BaseNCodec(), true);
        boolean result = baseNCodecInputStream.markSupported();
        baseNCodecInputStream.close();

        assertFalse(result);
    }

    @Test
    public void testAvailable() throws IOException {
        when(inputStream.read()).thenReturn(65);

        BaseNCodecInputStream baseNCodecInputStream = new BaseNCodecInputStream(inputStream, new BaseNCodec(), true);
        int result = baseNCodecInputStream.available();
        baseNCodecInputStream.close();

        verify(inputStream, times(1)).read();
        assertEquals(1, result);
    }
}

