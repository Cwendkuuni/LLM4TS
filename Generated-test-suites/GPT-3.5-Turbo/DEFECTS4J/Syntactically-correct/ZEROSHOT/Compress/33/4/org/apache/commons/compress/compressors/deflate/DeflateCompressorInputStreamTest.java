import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import org.apache.commons.compress.compressors.CompressorInputStream;
import org.junit.Before;
import org.junit.Test;

public class DeflateCompressorInputStreamTest {
    
    private InputStream mockInputStream;
    private DeflateCompressorInputStream deflateCompressorInputStream;

    @Before
    public void setup() {
        mockInputStream = mock(InputStream.class);
        deflateCompressorInputStream = new DeflateCompressorInputStream(mockInputStream);
    }
    
    @Test
    public void testRead() throws IOException {
        int expected = 1;
        when(mockInputStream.read()).thenReturn(expected);
        
        int actual = deflateCompressorInputStream.read();
        
        assertEquals(expected, actual);
        verify(mockInputStream, times(1)).read();
    }
    
    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] buffer = new byte[10];
        int off = 0;
        int len = 5;
        int expected = 5;
        when(mockInputStream.read(buffer, off, len)).thenReturn(expected);
        
        int actual = deflateCompressorInputStream.read(buffer, off, len);
        
        assertEquals(expected, actual);
        verify(mockInputStream, times(1)).read(buffer, off, len);
    }
    
    @Test
    public void testSkip() throws IOException {
        long expected = 10L;
        when(mockInputStream.skip(expected)).thenReturn(expected);
        
        long actual = deflateCompressorInputStream.skip(expected);
        
        assertEquals(expected, actual);
        verify(mockInputStream, times(1)).skip(expected);
    }
    
    @Test
    public void testAvailable() throws IOException {
        int expected = 10;
        when(mockInputStream.available()).thenReturn(expected);
        
        int actual = deflateCompressorInputStream.available();
        
        assertEquals(expected, actual);
        verify(mockInputStream, times(1)).available();
    }
    
    @Test
    public void testClose() throws IOException {
        deflateCompressorInputStream.close();
        
        verify(mockInputStream, times(1)).close();
    }
}