import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class CallsRealMethodsTest {
  
  CallsRealMethods callsRealMethods = new CallsRealMethods();

  @Test
  public void testAnswer() throws Throwable {
    InvocationOnMock invocationOnMock = mock(InvocationOnMock.class);
    
    // Test 1: Verifying the return value of answer method
    when(invocationOnMock.callRealMethod()).thenReturn("mocked");
    assertEquals("mocked", callsRealMethods.answer(invocationOnMock));

    // Test 2: Verifying the invocation.callRealMethod() is called
    callsRealMethods.answer(invocationOnMock);
    verify(invocationOnMock, times(1)).callRealMethod();
  }

}