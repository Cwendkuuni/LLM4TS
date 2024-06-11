import org.junit.Test;
import org.mockito.Mockito;

public class CallsRealMethodsTest {

  @Test
  public void testAnswer() throws Throwable {
    CallsRealMethods callsRealMethods = new CallsRealMethods();
    InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
    
    callsRealMethods.answer(invocation);
    
    // Add your assertions here
  }
}