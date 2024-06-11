import org.junit.Test;
import static org.junit.Assert.*;

public class CallsRealMethodsTest {

  @Test
  public void testAnswer() {
    CallsRealMethods callsRealMethods = new CallsRealMethods();

    // Test cases for answer method
    InvocationOnMock invocation1 = Mockito.mock(InvocationOnMock.class);
    assertEquals(callsRealMethods.answer(invocation1), invocation1.callRealMethod());

    InvocationOnMock invocation2 = Mockito.mock(InvocationOnMock.class);
    assertEquals(callsRealMethods.answer(invocation2), invocation2.callRealMethod());

    InvocationOnMock invocation3 = Mockito.mock(InvocationOnMock.class);
    assertEquals(callsRealMethods.answer(invocation3), invocation3.callRealMethod());
  }
}