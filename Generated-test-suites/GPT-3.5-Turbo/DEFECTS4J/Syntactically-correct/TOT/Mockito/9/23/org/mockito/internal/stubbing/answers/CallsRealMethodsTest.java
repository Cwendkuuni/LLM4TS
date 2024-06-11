import org.junit.Test;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.Mockito;

import static org.junit.Assert.*;

public class CallsRealMethodsTest {

  @Test
  public void answer_withInvocation_callsRealMethod() throws Throwable {
    InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
    CallsRealMethods callsRealMethods = new CallsRealMethods();

    Object result = callsRealMethods.answer(invocation);

    // Verify that the answer calls the real method
    Mockito.verify(invocation).callRealMethod();
  }
  
  @Test(expected = Exception.class)
  public void answer_withInvocation_callsThrowException() throws Throwable {
    InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
    CallsRealMethods callsRealMethods = new CallsRealMethods();

    // Throw an exception from the callRealMethod() when answer() is invoked
    Mockito.when(invocation.callRealMethod()).thenThrow(new Exception());

    Object result = callsRealMethods.answer(invocation); // This invocation would throw an exception

    // Verify that the answer calls the real method
    Mockito.verify(invocation).callRealMethod();
  }
 
  @Test
  public void answer_withInvocation_returnsResult() throws Throwable {
    InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
    CallsRealMethods callsRealMethods = new CallsRealMethods();
    Object expected = new Object();

    // Return the expected result from the callRealMethod() when answer() is invoked
    Mockito.when(invocation.callRealMethod()).thenReturn(expected);

    Object result = callsRealMethods.answer(invocation);

    // Verify that the answer calls the real method
    Mockito.verify(invocation).callRealMethod();

    // Verify that the result is the expected result
    assertEquals(expected, result);
  }

}