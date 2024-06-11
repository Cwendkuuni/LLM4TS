import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class IOUtilsTest {

    private IOUtils ioUtils;
    
    @Mock
    private InputStream inputStream;
    
    @Mock
    private OutputStream outputStream;
    
    @Before
    public void setup() {
        ioUtils = new IOUtils();
    }
    
    @Test
    public void testCopy_NoBytesRead_ReturnsZero() throws IOException {
        long count = ioUtils.copy(inputStream, outputStream);
        
        assertEquals(0, count);
        verify(inputStream, never()).read(any(byte[].class)); // make sure read() was never called
        verify(outputStream, never()).write(any(byte[].class), anyInt(), anyInt()); // make sure write() was never called
    }
    
    @Test
    public void testCopy_LessBytesThanBuffer_ReturnsCorrectCount() throws IOException {
        byte[] inputBytes = {1, 2, 3};
        when(inputStream.read(any(byte[].class))).thenReturn(3, -1);
        
        long count = ioUtils.copy(inputStream, outputStream);
        
        assertEquals(3, count);
        verify(inputStream, times(2)).read(any(byte[].class)); // make sure read() was called twice
        verify(outputStream).write(inputBytes, 0, 3); // make sure write() was called with correct arguments
    }
    
    @Test
    public void testCopy_MoreBytesThanBuffer_ReturnsCorrectCount() throws IOException {
        byte[] inputBytes = new byte[IOUtils.COPY_BUF_SIZE];
        byte[] buffer = new byte[IOUtils.COPY_BUF_SIZE];
        for (int i = 0; i < IOUtils.COPY_BUF_SIZE; i++) {
            inputBytes[i] = (byte) (i+1);
            buffer[i] = (byte) (i+1);
        }
        
        when(inputStream.read(any(byte[].class))).thenReturn(IOUtils.COPY_BUF_SIZE, IOUtils.COPY_BUF_SIZE, -1);
        
        long count = ioUtils.copy(inputStream, outputStream);
        
        assertEquals(IOUtils.COPY_BUF_SIZE * 2, count);
        verify(inputStream, times(3)).read(any(byte[].class)); // make sure read() was called three times
        verify(outputStream).write(buffer, 0, IOUtils.COPY_BUF_SIZE); // make sure write() was called with correct arguments
        verify(outputStream).write(buffer, 0, IOUtils.COPY_BUF_SIZE); // make sure write() was called with correct arguments
    }
    
    @Test
    public void testSkip_SkipMoreThanAvailable_ReturnsPartialSkipCount() throws IOException {
        long available = IOUtils.COPY_BUF_SIZE - 1;
        when(inputStream.skip(anyLong())).thenReturn(available, 0L);
        
        long skipped = ioUtils.skip(inputStream, IOUtils.COPY_BUF_SIZE + 1);
        
        assertEquals(available, skipped);
        verify(inputStream, times(1)).skip(anyLong()); // make sure skip() was called once
    }
    
    @Test
    public void testSkip_SkipLessThanAvailable_ReturnsCorrectSkipCount() throws IOException {
        long numToSkip = IOUtils.COPY_BUF_SIZE - 1;
        when(inputStream.skip(anyLong())).thenReturn(numToSkip);
        
        long skipped = ioUtils.skip(inputStream, numToSkip);
        
        assertEquals(numToSkip, skipped);
        verify(inputStream, times(1)).skip(anyLong()); // make sure skip() was called once
    }
    
    @Test
    public void testSkip_NothingToSkip_ReturnsZero() throws IOException {
        long skipped = ioUtils.skip(inputStream, 0);
        
        assertEquals(0, skipped);
        verify(inputStream, never()).skip(anyLong()); // make sure skip() was never called
    }
    
    @Test
    public void testReadFully_ExactBytesRequested_ReturnsCorrectBytesRead() throws IOException {
        byte[] inputBytes = new byte[IOUtils.COPY_BUF_SIZE];
        byte[] buffer = new byte[IOUtils.COPY_BUF_SIZE];
        for (int i = 0; i < IOUtils.COPY_BUF_SIZE; i++) {
            inputBytes[i] = (byte) (i+1);
            buffer[i] = (byte) (i+1);
        }
        
        when(inputStream.read(any(byte[].class), anyInt(), anyInt())).thenReturn(IOUtils.COPY_BUF_SIZE);
        
        int bytesRead = ioUtils.readFully(inputStream, buffer, 0, IOUtils.COPY_BUF_SIZE);
        
        assertEquals(IOUtils.COPY_BUF_SIZE, bytesRead);
        assertArrayEquals(inputBytes, buffer);
        verify(inputStream, times(1)).read(any(byte[].class), anyInt(), anyInt()); // make sure read() was called once
    }
    
    @Test
    public void testReadFully_LessBytesThanRequested_ReturnsPartialBytesRead() throws IOException {
        byte[] inputBytes = new byte[5];
        byte[] buffer = new byte[5];
        for (int i = 0; i < 5; i++) {
            inputBytes[i] = (byte) (i+1);
            buffer[i] = (byte) (i+1);
        }
        
        when(inputStream.read(any(byte[].class), anyInt(), anyInt())).thenReturn(2);
        
        int bytesRead = ioUtils.readFully(inputStream, buffer, 0, 5);
        
        assertEquals(2, bytesRead);
        assertArrayEquals(inputBytes, buffer);
        verify(inputStream, times(1)).read(any(byte[].class), anyInt(), anyInt()); // make sure read() was called once
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFully_InvalidOffset_ThrowsIndexOutOfBoundsException() throws IOException {
        ioUtils.readFully(inputStream, new byte[IOUtils.COPY_BUF_SIZE], -1, IOUtils.COPY_BUF_SIZE);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFully_InvalidLength_ThrowsIndexOutOfBoundsException() throws IOException {
        ioUtils.readFully(inputStream, new byte[IOUtils.COPY_BUF_SIZE], 0, -1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testReadFully_InvalidRange_ThrowsIndexOutOfBoundsException() throws IOException {
        ioUtils.readFully(inputStream, new byte[IOUtils.COPY_BUF_SIZE], 0, IOUtils.COPY_BUF_SIZE + 1);
    }
    
    @Test
    public void testToByteArray_InputStreamNotEmpty_ReturnsCorrectByteArray() throws IOException {
        byte[] inputBytes = {1, 2, 3};
        when(inputStream.read(any(byte[].class))).thenReturn(3, -1);
        
        byte[] byteArray = ioUtils.toByteArray(inputStream);
        
        assertArrayEquals(inputBytes, byteArray);
        verify(inputStream, times(2)).read(any(byte[].class)); // make sure read() was called twice
    }
    
    @Test
    public void testToByteArray_EmptyInputStream_ReturnsEmptyByteArray() throws IOException {
        when(inputStream.read(any(byte[].class))).thenReturn(-1);
        
        byte[] byteArray = ioUtils.toByteArray(inputStream);
        
        assertEquals(0, byteArray.length);
        verify(inputStream).read(any(byte[].class)); // make sure read() was called once
    }
    
    @Test
    public void testCloseQuietly_InputValid_InputClosed() throws IOException {
        ioUtils.closeQuietly(inputStream);
        
        verify(inputStream).close();
    }
    
    @Test
    public void testCloseQuietly_InputNull_NoExceptionThrown() throws IOException {
        ioUtils.closeQuietly(null);
        
        // No exception should be thrown
    }
}