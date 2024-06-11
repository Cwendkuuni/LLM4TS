import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DeflateCompressorInputStreamTest {

    private InputStream mockInputStream;

    @Before
    public void setup() {
        mockInputStream = mock(InputStream.class);
    }

    @Test
    public void testRead() throws IOException {
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(mockInputStream);
        when(mockInputStream.read()).thenReturn(1);

        int result = inputStream.read();

        assertEquals(1, result);
        verify(inputStream, times(1)).count(1);
    }

    @Test
    public void testReadArray() throws IOException {
        byte[] buf = new byte[5];
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(mockInputStream);
        when(mockInputStream.read(buf, 0, 5)).thenReturn(5);

        int result = inputStream.read(buf, 0, 5);

        assertEquals(5, result);
        verify(inputStream, times(1)).count(5);
    }

    @Test
    public void testSkip() throws IOException {
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(mockInputStream);
        when(mockInputStream.skip(10)).thenReturn(10L);

        long result = inputStream.skip(10);

        assertEquals(10L, result);
    }

    @Test
    public void testAvailable() throws IOException {
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(mockInputStream);
        when(mockInputStream.available()).thenReturn(100);

        int result = inputStream.available();

        assertEquals(100, result);
    }

    @Test
    public void testClose() throws IOException {
        DeflateCompressorInputStream inputStream = new DeflateCompressorInputStream(mockInputStream);

        inputStream.close();

        verify(mockInputStream, times(1)).close();
    }

}