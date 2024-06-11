import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.BaseNCodec;
import org.apache.commons.codec.binary.BaseNCodecInputStream;
import org.junit.Before;
import org.junit.Test;

public class BaseNCodecInputStreamTest {

    private InputStream mockInputStream;
    private BaseNCodec mockBaseNCodec;
    private BaseNCodecInputStream baseNCodecInputStream;

    @Before
    public void setUp() {
        mockInputStream = mock(InputStream.class);
        mockBaseNCodec = mock(BaseNCodec.class);
        baseNCodecInputStream = new BaseNCodecInputStream(mockInputStream, mockBaseNCodec, true);
    }

    @Test
    public void testRead() throws IOException {
        when(mockInputStream.read()).thenReturn(97);
        
        int result = baseNCodecInputStream.read();
        
        assert result == 97;
        verify(mockInputStream, times(1)).read();
    }
    
    @Test
    public void testRead_withNegativeByte() throws IOException {
        when(mockInputStream.read()).thenReturn(-1);
        
        int result = baseNCodecInputStream.read();
        
        assert result == -1;
        verify(mockInputStream, times(1)).read();
    }
    
    @Test
    public void testRead_withZeroBytesRead() throws IOException {
        when(mockInputStream.read()).thenReturn(0, 97);
        
        int result = baseNCodecInputStream.read();
        
        assert result == 97;
        verify(mockInputStream, times(2)).read();
    }

    @Test
    public void testRead_withByteArray() throws IOException {
        byte[] buffer = new byte[5];
        when(mockInputStream.read(buffer, 0, 5)).thenReturn(3);
        
        int result = baseNCodecInputStream.read(buffer, 0, 5);
        
        assert result == 3;
        verify(mockInputStream, times(1)).read(buffer, 0, 5);
    }

    @Test
    public void testRead_withNullByteArray() throws IOException {
        byte[] buffer = null;
        
        try {
            baseNCodecInputStream.read(buffer, 0, 5);
            assert false;
        } catch (NullPointerException e) {
            assert true;
        }
    }

    @Test
    public void testRead_withNegativeOffset() throws IOException {
        byte[] buffer = new byte[5];
        
        try {
            baseNCodecInputStream.read(buffer, -1, 5);
            assert false;
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }
    }

    @Test
    public void testRead_withNegativeLength() throws IOException {
        byte[] buffer = new byte[5];
        
        try {
            baseNCodecInputStream.read(buffer, 0, -1);
            assert false;
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }
    }

    @Test
    public void testRead_withInvalidOffset() throws IOException {
        byte[] buffer = new byte[5];
        
        try {
            baseNCodecInputStream.read(buffer, 6, 5);
            assert false;
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }
    }

    @Test
    public void testRead_withInvalidLength() throws IOException {
        byte[] buffer = new byte[5];
        
        try {
            baseNCodecInputStream.read(buffer, 0, 6);
            assert false;
        } catch (IndexOutOfBoundsException e) {
            assert true;
        }
    }

    @Test
    public void testRead_withZeroLength() throws IOException {
        byte[] buffer = new byte[5];
        when(mockInputStream.read(buffer, 0, 5)).thenReturn(0);
        
        int result = baseNCodecInputStream.read(buffer, 0, 5);
        
        assert result == 0;
        verify(mockInputStream, times(1)).read(buffer, 0, 5);
    }

    @Test
    public void testRead_withEmptyBufferAndZeroLength() throws IOException {
        byte[] buffer = new byte[0];
        
        int result = baseNCodecInputStream.read(buffer, 0, 0);
        
        assert result == 0;
    }

    @Test
    public void testRead_withNoDataInCodec() throws IOException {
        byte[] buffer = new byte[5];
        when(mockBaseNCodec.hasData()).thenReturn(false);
        when(mockInputStream.read(buffer)).thenReturn(5);
        
        int result = baseNCodecInputStream.read(buffer, 0, 5);
        
        assert result == 5;
        verify(mockBaseNCodec, times(1)).hasData();
        verify(mockInputStream, times(1)).read(buffer);
    }

    @Test
    public void testRead_withEncode() throws IOException {
        byte[] buffer = new byte[10];
        when(mockBaseNCodec.hasData()).thenReturn(false);
        when(mockInputStream.read(buffer)).thenReturn(10);
        
        BaseNCodecInputStream baseNCodecInputStreamEncode = new BaseNCodecInputStream(mockInputStream, mockBaseNCodec, true);
        int result = baseNCodecInputStreamEncode.read(buffer);
        
        assert result == 10;
        verify(mockBaseNCodec, times(1)).hasData();
        verify(mockInputStream, times(1)).read(buffer);
    }

    @Test
    public void testRead_withDecode() throws IOException {
        byte[] buffer = new byte[20];
        when(mockBaseNCodec.hasData()).thenReturn(false);
        when(mockInputStream.read(buffer)).thenReturn(20);
        
        BaseNCodecInputStream baseNCodecInputStreamDecode = new BaseNCodecInputStream(mockInputStream, mockBaseNCodec, false);
        int result = baseNCodecInputStreamDecode.read(buffer);
        
        assert result == 20;
        verify(mockBaseNCodec, times(1)).hasData();
        verify(mockInputStream, times(1)).read(buffer);
    }

    @Test
    public void testMarkSupported() {
        boolean result = baseNCodecInputStream.markSupported();
        
        assert result == false;
    }
}