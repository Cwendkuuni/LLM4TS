import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {
  
  @Test
  public void testAnswer() throws Throwable {
    ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    Answer<Object> answer = returnsDeepStubs;
    Object result = answer.answer(invocation);
    // test method functionality here
  }
  
}