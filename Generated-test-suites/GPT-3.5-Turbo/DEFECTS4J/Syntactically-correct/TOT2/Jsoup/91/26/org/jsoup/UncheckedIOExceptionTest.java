import org.junit.Test;
import org.mockito.Mockito;

public class UncheckedIOExceptionTest {

    @Test
    public void testConstructor() {
        IOException ioException = Mockito.mock(IOException.class);
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        
        assertEquals(ioException, uncheckedIOException.ioException());
    }
    
    @Test(expected = NullPointerException.class)
    public void testConstructor_NullPointerException() {
        new UncheckedIOException(null);
    }
    
    @Test
    public void testIoException() {
        IOException ioException = Mockito.mock(IOException.class);
        UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
        
        assertEquals(ioException, uncheckedIOException.ioException());
    }
    
    @Test
    public void testIoException_Null() {
        UncheckedIOException uncheckedIOException = new UncheckedIOException(null);
        
        assertNull(uncheckedIOException.ioException());
    }
}