import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;

public class BitInputStreamTest {
    
    private InputStream inputStream;
    private BitInputStream bitInputStream;
    
    @Before
    public void setUp() {
        inputStream = mock(InputStream.class);
        bitInputStream = new BitInputStream(inputStream, ByteOrder.LITTLE_ENDIAN);
    }
    
    @Test
    public void testClose() throws IOException {
        bitInputStream.close();
    }
    
    @Test
    public void testClearBitCache() {
        bitInputStream.clearBitCache();
    }
    
    @Test
    public void testReadBits() throws IOException {
        int count = 8;
        long expected = 255L;
        
        when(inputStream.read()).thenReturn(255);
        
        long result = bitInputStream.readBits(count);
        
        assertEquals(expected, result);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testReadBitsInvalidCount() throws IOException {
        int count = -1;
        
        bitInputStream.readBits(count);
    }
    
    @Test
    public void testReadBitsEndOfStream() throws IOException {
        int count = 8;
        long expected = -1L;
        
        when(inputStream.read()).thenReturn(-1);
        
        long result = bitInputStream.readBits(count);
        
        assertEquals(expected, result);
    }
}