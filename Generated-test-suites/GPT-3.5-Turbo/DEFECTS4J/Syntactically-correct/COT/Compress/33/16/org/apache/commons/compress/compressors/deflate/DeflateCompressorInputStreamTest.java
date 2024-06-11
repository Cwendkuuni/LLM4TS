import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.mockito.Mockito.*;

public class DeflateCompressorInputStreamTest {

    private DeflateCompressorInputStream deflateInputStream;
    private InputStream mockInputStream;

    @Before
    public void setup() throws IOException {
        byte[] data = { 120, 94, 83, 0, 0, 0 };
        mockInputStream = spy(new ByteArrayInputStream(data));
        deflateInputStream = new DeflateCompressorInputStream(mockInputStream);
    }

    @Test
    public void testRead() throws IOException {
        int result = deflateInputStream.read();
        Assert.assertEquals(120, result);
        verify(mockInputStream, times(1)).read();
    }

    @Test
    public void testReadWithBuffer() throws IOException {
        byte[] buffer = new byte[4];
        int result = deflateInputStream.read(buffer, 0, 4);
        Assert.assertEquals(4, result);
        verify(mockInputStream, times(1)).read(buffer, 0, 4);
    }

    @Test
    public void testSkip() throws IOException {
        long result = deflateInputStream.skip(2);
        Assert.assertEquals(2, result);
        verify(mockInputStream, times(1)).skip(2);
    }

    @Test
    public void testAvailable() throws IOException {
        int result = deflateInputStream.available();
        Assert.assertEquals(0, result);
        verify(mockInputStream, times(1)).available();
    }

    @Test
    public void testClose() throws IOException {
        deflateInputStream.close();
        verify(mockInputStream, times(1)).close();
    }

    @Test
    public void testReadWhenInputStreamReturnsNegativeOne() throws IOException {
        when(mockInputStream.read()).thenReturn(-1);
        
        int result = deflateInputStream.read();
        
        Assert.assertEquals(-1, result);
        verify(mockInputStream, times(1)).read();
    }

    @Test
    public void testReadWithBufferWhenInputStreamReturnsNegativeOne() throws IOException {
        when(mockInputStream.read(any(byte[].class), anyInt(), anyInt())).thenReturn(-1);
        
        byte[] buffer = new byte[4];
        int result = deflateInputStream.read(buffer, 0, 4);
        
        Assert.assertEquals(-1, result);
        verify(mockInputStream, times(1)).read(buffer, 0, 4);
    }

    @Test
    public void testSkipWhenInputStreamSkipsPartialAmount() throws IOException {
        when(mockInputStream.skip(2)).thenReturn(1L);
        
        long result = deflateInputStream.skip(2);
        
        Assert.assertEquals(1, result);
        verify(mockInputStream, times(1)).skip(2);
    }
    
    @Test
    public void testSignatureCheck() {
        DeflateParameters parameters = new DeflateParameters();
        parameters.setWithZlibHeader(true);
        DeflateCompressorInputStream deflateInputStream = new DeflateCompressorInputStream(mockInputStream, parameters);
        
        byte[] signature = { 120, 94, 83, 0, 0, 0 };
        boolean result = deflateInputStream.checkSignature(signature, signature.length);
        
        Assert.assertTrue(result);
        verify(mockInputStream, never()).read();
    }
}