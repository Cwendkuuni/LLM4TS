import org.junit.Test;
import org.mockito.Mockito;

public class BaseNCodecInputStreamTest {
  
    private BaseNCodec mockCodec;
    private InputStream mockInputStream;
    
    @Test
    public void testRead() throws IOException {
        mockCodec = Mockito.mock(BaseNCodec.class);
        mockInputStream = Mockito.mock(InputStream.class);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(mockInputStream, mockCodec, false);
        
        Mockito.when(mockInputStream.read())
            .thenReturn(0)
            .thenReturn(1)
            .thenReturn(-1);
        
        int result1 = inputStream.read();
        int result2 = inputStream.read();
        int result3 = inputStream.read();
        
        assert(result1 == 0);
        assert(result2 == 1);
        assert(result3 == -1);
        
        Mockito.verify(mockCodec, Mockito.times(3)).hasData();
        Mockito.verify(mockCodec, Mockito.times(2)).readResults(Mockito.any(byte[].class), Mockito.anyInt(), Mockito.anyInt());
    }
    
    @Test(expected = NullPointerException.class)
    public void testReadNullByteArray() throws IOException {
        mockCodec = Mockito.mock(BaseNCodec.class);
        mockInputStream = Mockito.mock(InputStream.class);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(mockInputStream, mockCodec, false);
        
        Mockito.when(mockInputStream.read())
            .thenReturn(0);
        
        int result = inputStream.read(null, 0, 1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadInvalidOffset() throws IOException {
        mockCodec = Mockito.mock(BaseNCodec.class);
        mockInputStream = Mockito.mock(InputStream.class);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(mockInputStream, mockCodec, false);
        
        Mockito.when(mockInputStream.read())
            .thenReturn(0);
        
        byte[] buffer = new byte[5];
        int result = inputStream.read(buffer, 10, 1);
    }
    
    @Test
    public void testReadEmptyByteArray() throws IOException {
        mockCodec = Mockito.mock(BaseNCodec.class);
        mockInputStream = Mockito.mock(InputStream.class);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(mockInputStream, mockCodec, false);
        
        Mockito.when(mockInputStream.read())
            .thenReturn(0);
        
        byte[] buffer = new byte[0];
        int result = inputStream.read(buffer, 0, 0);
        
        assert(result == 0);
        Mockito.verify(mockCodec, Mockito.times(1)).hasData();
        Mockito.verify(mockCodec, Mockito.times(0)).readResults(Mockito.any(byte[].class), Mockito.anyInt(), Mockito.anyInt());
    }
    
    @Test
    public void testReadMultipleBytes() throws IOException {
        mockCodec = Mockito.mock(BaseNCodec.class);
        mockInputStream = Mockito.mock(InputStream.class);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(mockInputStream, mockCodec, false);
        
        Mockito.when(mockInputStream.read(Mockito.any(byte[].class)))
            .thenReturn(5);
        
        byte[] buffer = new byte[5];
        int result = inputStream.read(buffer, 0, 5);
        
        assert(result == 5);
        Mockito.verify(mockCodec, Mockito.times(1)).hasData();
        Mockito.verify(mockCodec, Mockito.times(1)).readResults(Mockito.any(byte[].class), Mockito.anyInt(), Mockito.anyInt());
    }
    
    @Test
    public void testMarkSupported() {
        mockCodec = Mockito.mock(BaseNCodec.class);
        mockInputStream = Mockito.mock(InputStream.class);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(mockInputStream, mockCodec, false);
        
        boolean result = inputStream.markSupported();
        
        assert(result == false);
    }
    
    @Test
    public void testAvailable() throws IOException {
        mockCodec = Mockito.mock(BaseNCodec.class);
        mockInputStream = Mockito.mock(InputStream.class);
        BaseNCodecInputStream inputStream = new BaseNCodecInputStream(mockInputStream, mockCodec, false);
        
        Mockito.when(mockCodec.hasData())
            .thenReturn(true)
            .thenReturn(false);
        
        int result1 = inputStream.available();
        int result2 = inputStream.available();
        
        assert(result1 == 1);
        assert(result2 == 0);
        
        Mockito.verify(mockCodec, Mockito.times(2)).hasData();
    }
}