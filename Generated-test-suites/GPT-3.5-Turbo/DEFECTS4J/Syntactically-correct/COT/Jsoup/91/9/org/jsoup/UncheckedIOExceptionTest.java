import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;

public class UncheckedIOExceptionTest {

  @Test
  public void testUncheckedIOException() {
    IOException ioException = new IOException("Test IOException");
    UncheckedIOException uncheckedIOException = new UncheckedIOException(ioException);
    
    assertEquals(ioException, uncheckedIOException.ioException());
  }
  
  @Test
  public void testUncheckedIOExceptionWithNullCause() {
    UncheckedIOException uncheckedIOException = new UncheckedIOException(null);
    
    assertNull(uncheckedIOException.ioException());
  }
  
  @Test(expected = ClassCastException.class)
  public void testIOExceptionType() {
    RuntimeException runtimeException = new RuntimeException("Test RuntimeException");
    UncheckedIOException uncheckedIOException = new UncheckedIOException(runtimeException);
    
    uncheckedIOException.ioException();
  }
  
}