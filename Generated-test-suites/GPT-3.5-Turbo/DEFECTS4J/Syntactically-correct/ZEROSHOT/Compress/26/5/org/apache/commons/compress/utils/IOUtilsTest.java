import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IOUtilsTest {

    @Test
    public void testCopy() throws IOException {
        InputStream mockInputStream = mock(InputStream.class);
        OutputStream mockOutputStream = mock(OutputStream.class);

        when(mockInputStream.read(any(byte[].class))).thenReturn(5).thenReturn(-1);
        long count = IOUtils.copy(mockInputStream, mockOutputStream);

        verify(mockInputStream, times(2)).read(any(byte[].class));
        verify(mockOutputStream).write(any(byte[].class), anyInt(), anyInt());
        assertEquals(5, count);
    }

    @Test
    public void testCopyWithBufferSize() throws IOException {
        InputStream mockInputStream = mock(InputStream.class);
        OutputStream mockOutputStream = mock(OutputStream.class);
        int bufferSize = 1024;

        when(mockInputStream.read(any(byte[].class))).thenReturn(1024).thenReturn(-1);
        long count = IOUtils.copy(mockInputStream, mockOutputStream, bufferSize);

        verify(mockInputStream, times(2)).read(any(byte[].class));
        verify(mockOutputStream).write(any(byte[].class), anyInt(), anyInt());
        assertEquals(1024, count);
    }

    @Test
    public void testSkip() throws IOException {
        InputStream mockInputStream = mock(InputStream.class);

        when(mockInputStream.skip(anyLong())).thenReturn(5L, 3L, 0L);
        long skipped = IOUtils.skip(mockInputStream, 10);

        verify(mockInputStream, times(3)).skip(anyLong());
        assertEquals(10L, skipped);
    }

    @Test
    public void testReadFully() throws IOException {
        InputStream mockInputStream = mock(InputStream.class);
        byte[] mockBuffer = new byte[10];

        when(mockInputStream.read(any(byte[].class), anyInt(), anyInt())).thenReturn(5).thenReturn(5).thenReturn(-1);
        int bytesRead = IOUtils.readFully(mockInputStream, mockBuffer, 0, 10);

        verify(mockInputStream, times(3)).read(any(byte[].class), anyInt(), anyInt());
        assertEquals(10, bytesRead);
    }

    @Test
    public void testToByteArray() throws IOException {
        InputStream mockInputStream = mock(InputStream.class);
        ByteArrayOutputStream mockOutputStream = mock(ByteArrayOutputStream.class);
        byte[] expectedByteArray = new byte[10];

        when(mockOutputStream.toByteArray()).thenReturn(expectedByteArray);
        when(mockInputStream.read(any(byte[].class))).thenReturn(5).thenReturn(-1);

        byte[] byteArray = IOUtils.toByteArray(mockInputStream);

        verify(mockInputStream, times(2)).read(any(byte[].class));
        verify(mockOutputStream).toByteArray();
        assertEquals(expectedByteArray, byteArray);
    }

    @Test
    public void testCloseQuietly() throws IOException {
        Closeable mockCloseable = mock(Closeable.class);

        IOUtils.closeQuietly(mockCloseable);

        verify(mockCloseable).close();
    }
}