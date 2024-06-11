import static org.junit.Assert.*;
import org.junit.Test;

public class ReturnsSmartNullsTest {

  @Test
  public void testAnswer_NullValueReturnsSmartNull() {
    ReturnsSmartNulls answer = new ReturnsSmartNulls();
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMethod()).thenReturn(method);
    when(method.getReturnType()).thenReturn(null);

    Object result = answer.answer(invocation);

    assertNotNull(result);
    assertTrue(result instanceof SmartNull);
  }

  @Test
  public void testAnswer_OtherValueReturnsMoreEmptyValues() {
    ReturnsSmartNulls answer = new ReturnsSmartNulls();
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMethod()).thenReturn(method);
    when(method.getReturnType()).thenReturn(SomeClass.class);

    Object result = answer.answer(invocation);

    assertNotNull(result);
    assertTrue(result instanceof SomeClass);
  }

  private class SomeClass {}

}