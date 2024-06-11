import static org.mockito.Mockito.*;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class CallsRealMethodsTest {

  @Test
  public void testAnswer() throws Throwable {
    CallsRealMethods callRealMethods = new CallsRealMethods();

    InvocationOnMock invocation = mock(InvocationOnMock.class);
    
    // Test1: Calls the real method
    when(invocation.callRealMethod()).thenAnswer(callRealMethods);
    callRealMethods.answer(invocation);
    verify(invocation).callRealMethod();

    // Test2: Calls the real method
    when(invocation.callRealMethod()).thenAnswer(callRealMethods);
    callRealMethods.answer(invocation);
    verify(invocation).callRealMethod();
  }
}