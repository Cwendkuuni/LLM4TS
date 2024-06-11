import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import java.io.InputStream;
import java.io.IOException;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class DeflateCompressorInputStreamTest {
    
    @Mock
    private InputStream mockedInputStream;
    private DeflateCompressorInputStream deflateCompressorInputStream;
    
    @Before
    public void setup() {
        mockedInputStream = mock(InputStream.class);
        deflateCompressorInputStream = new DeflateCompressorInputStream(mockedInputStream);
    }
    
    @Test
    public void testRead_shouldReturnOneByte() throws IOException {
        when(mockedInputStream.read()).thenReturn(65);
        int result = deflateCompressorInputStream.read();
        verify(mockedInputStream, times(1)).read();
        assertEquals(65, result);
    }
    
    @Test
    public void testRead_shouldReturnMinusOneAndCountZero() throws IOException {
        when(mockedInputStream.read()).thenReturn(-1);
        int result = deflateCompressorInputStream.read();
        verify(mockedInputStream, times(1)).read();
        assertEquals(-1, result);
        assertEquals(0, deflateCompressorInputStream.getCount());
    }
    
    @Test
    public void testRead_shouldReturnNumberOfBytesRead() throws IOException {
        byte[] buffer = new byte[10];
        when(mockedInputStream.read(any(byte[].class), anyInt(), anyInt())).thenReturn(5);
        int result = deflateCompressorInputStream.read(buffer, 0, 10);
        verify(mockedInputStream, times(1)).read(any(byte[].class), anyInt(), anyInt());
        assertEquals(5, result);
    }
    
    @Test
    public void testSkip_shouldDelegateToInputStream() throws IOException {
        long skipAmount = 10L;
        when(mockedInputStream.skip(skipAmount)).thenReturn(skipAmount);
        long result = deflateCompressorInputStream.skip(skipAmount);
        verify(mockedInputStream, times(1)).skip(skipAmount);
        assertEquals(skipAmount, result);
    }
    
    @Test
    public void testAvailable_shouldDelegateToInputStream() throws IOException {
        when(mockedInputStream.available()).thenReturn(100);
        int result = deflateCompressorInputStream.available();
        verify(mockedInputStream, times(1)).available();
        assertEquals(100, result);
    }
    
    @Test
    public void testClose_shouldDelegateToInputStream() throws IOException {
        deflateCompressorInputStream.close();
        verify(mockedInputStream, times(1)).close();
    }
}