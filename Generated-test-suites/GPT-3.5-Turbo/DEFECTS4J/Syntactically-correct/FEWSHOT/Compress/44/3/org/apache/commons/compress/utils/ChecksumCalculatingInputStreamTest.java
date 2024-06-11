import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.apache.commons.compress.utils.ChecksumCalculatingInputStream;
import java.io.ByteArrayInputStream;
import java.util.zip.Checksum;
import static org.junit.Assert.assertEquals;

public class ChecksumCalculatingInputStreamTest {

    private Checksum checksum;
    private ByteArrayInputStream inputStream;
    private ChecksumCalculatingInputStream calculatingInputStream;

    @Before
    public void setUp() {
        checksum = mock(Checksum.class);
        inputStream = new ByteArrayInputStream(new byte[] {1, 2, 3, 4, 5});
        calculatingInputStream = new ChecksumCalculatingInputStream(checksum, inputStream);
    }

    @Test
    public void testRead() throws Exception {
        when(inputStream.read()).thenReturn(1).thenReturn(2).thenReturn(3).thenReturn(-1);
        
        assertEquals(1, calculatingInputStream.read());
        verify(checksum).update(1);
        
        assertEquals(2, calculatingInputStream.read());
        verify(checksum).update(2);
        
        assertEquals(3, calculatingInputStream.read());
        verify(checksum).update(3);
        
        assertEquals(-1, calculatingInputStream.read());
    }

    @Test
    public void testReadByteArray() throws Exception {
        byte[] buffer = new byte[3];
        when(inputStream.read(buffer)).thenReturn(3).thenReturn(-1);
        
        assertEquals(3, calculatingInputStream.read(buffer));
        verify(checksum).update(buffer, 0, 3);
        
        assertEquals(-1, calculatingInputStream.read(buffer));
    }

    @Test
    public void testReadByteArrayWithOffsetAndLength() throws Exception {
        byte[] buffer = new byte[5];
        when(inputStream.read(buffer, 1, 3)).thenReturn(3).thenReturn(-1);
        
        assertEquals(3, calculatingInputStream.read(buffer, 1, 3));
        verify(checksum).update(buffer, 1, 3);
        
        assertEquals(-1, calculatingInputStream.read(buffer, 1, 3));
    }

    @Test
    public void testSkip() throws Exception {
        assertEquals(1, calculatingInputStream.skip(1));
    }

    @Test
    public void testGetValue() throws Exception {
        long expectedValue = 12345L;
        when(checksum.getValue()).thenReturn(expectedValue);
        
        assertEquals(expectedValue, calculatingInputStream.getValue());
    }
}