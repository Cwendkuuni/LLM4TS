
import static org.mockito.Mockito.*;
import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;

public class ReturnsDeepStubsTest {
  @Test
  public void testAnswer() throws Throwable {
    ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    returnsDeepStubs.answer(invocation);
  }
}
