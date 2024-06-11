import org.junit.Test;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class Base64InputStreamTest {
    
    @Test
    public void testRead() throws IOException {
        byte[] input = "Hello World".getBytes();
        InputStream inMock = mock(InputStream.class);
        when(inMock.read()).thenReturn((int) 'H').thenReturn((int) 'e').thenReturn((int) 'l')
                .thenReturn((int) 'l').thenReturn((int) 'o').thenReturn((int) ' ')
                .thenReturn((int) 'W').thenReturn((int) 'o').thenReturn((int) 'r')
                .thenReturn((int) 'l').thenReturn((int) 'd');
        Base64InputStream base64InputStream = new Base64InputStream(inMock);
        for (int i = 0; i < input.length; i++) {
            assertEquals((int) input[i], base64InputStream.read());
        }
        verify(inMock, times(input.length)).read();
    }
    
    @Test
    public void testReadWithNullArray() throws IOException {
        byte[] input = "Hello World".getBytes();
        InputStream inMock = mock(InputStream.class);
        when(inMock.read()).thenReturn((int) 'H').thenReturn((int) 'e').thenReturn((int) 'l')
                .thenReturn((int) 'l').thenReturn((int) 'o').thenReturn((int) ' ')
                .thenReturn((int) 'W').thenReturn((int) 'o').thenReturn((int) 'r')
                .thenReturn((int) 'l').thenReturn((int) 'd');
        Base64InputStream base64InputStream = new Base64InputStream(inMock);
        int bytesRead = base64InputStream.read(null, 0, input.length);
        assertEquals(input.length, bytesRead);
        verify(inMock, times(input.length)).read();
    }
    
    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        byte[] input = "Hello World".getBytes();
        InputStream inMock = mock(InputStream.class);
        when(inMock.read()).thenReturn((int) 'H').thenReturn((int) 'e').thenReturn((int) 'l')
                .thenReturn((int) 'l').thenReturn((int) 'o').thenReturn((int) ' ')
                .thenReturn((int) 'W').thenReturn((int) 'o').thenReturn((int) 'r')
                .thenReturn((int) 'l').thenReturn((int) 'd');
        Base64InputStream base64InputStream = new Base64InputStream(inMock);
        byte[] buffer = new byte[input.length];
        int bytesRead = base64InputStream.read(buffer, 2, 5);
        assertEquals(5, bytesRead);
        assertEquals(0, buffer[0]);
        assertEquals(0, buffer[1]);
        assertEquals((int) 'H', buffer[2]);
        assertEquals((int) 'e', buffer[3]);
        assertEquals((int) 'l', buffer[4]);
        assertEquals((int) 'l', buffer[5]);
        assertEquals((int) 'o', buffer[6]);
        assertEquals(0, buffer[7]);
        assertEquals(0, buffer[8]);
        assertEquals(0, buffer[9]);
        verify(inMock, times(input.length)).read();
    }
    
    @Test
    public void testReadWithOffsetAndLengthZero() throws IOException {
        InputStream inMock = mock(InputStream.class);
        Base64InputStream base64InputStream = new Base64InputStream(inMock);
        byte[] buffer = new byte[1];
        int bytesRead = base64InputStream.read(buffer, 0, 0);
        assertEquals(0, bytesRead);
        verify(inMock, times(1)).read();
    }
    
    @Test
    public void testReadWithNegativeOffset() throws IOException {
        InputStream inMock = mock(InputStream.class);
        Base64InputStream base64InputStream = new Base64InputStream(inMock);
        byte[] buffer = new byte[1];
        try {
            base64InputStream.read(buffer, -1, 1);
        } catch (IndexOutOfBoundsException e) {
            // expected exception
        }
        verify(inMock, never()).read();
    }
    
    @Test
    public void testReadWithNegativeLength() throws IOException {
        InputStream inMock = mock(InputStream.class);
        Base64InputStream base64InputStream = new Base64InputStream(inMock);
        byte[] buffer = new byte[1];
        try {
            base64InputStream.read(buffer, 0, -1);
        } catch (IndexOutOfBoundsException e) {
            // expected exception
        }
        verify(inMock, never()).read();
    }
    
    @Test
    public void testReadWithInvalidLength() throws IOException {
        InputStream inMock = mock(InputStream.class);
        Base64InputStream base64InputStream = new Base64InputStream(inMock);
        byte[] buffer = new byte[1];
        try {
            base64InputStream.read(buffer, 0, 2);
        } catch (IndexOutOfBoundsException e) {
            // expected exception
        }
        verify(inMock, never()).read();
    }
}