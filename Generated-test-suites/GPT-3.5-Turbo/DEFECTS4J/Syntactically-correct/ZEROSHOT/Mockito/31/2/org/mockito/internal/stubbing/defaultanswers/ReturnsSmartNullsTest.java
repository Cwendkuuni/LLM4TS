import static org.junit.Assert.*;
import org.junit.Test;

public class ReturnsSmartNullsTest {

  @Test
  public void testAnswer() throws Throwable {
    ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

    InvocationOnMock mockInvocation = mock(InvocationOnMock.class);
    when(mockInvocation.getMethod()).thenReturn(ReturnsSmartNulls.class.getMethod("answer", InvocationOnMock.class));

    Object result = returnsSmartNulls.answer(mockInvocation);

    assertNotNull(result);
  }

  @Test
  public void testThrowingInterceptor() throws Throwable {
    ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = returnsSmartNulls.new ThrowingInterceptor(mock(InvocationOnMock.class));

    Object obj = new Object();
    Method method = Object.class.getMethod("hashCode");
    Object[] args = {};

    Object result = throwingInterceptor.intercept(obj, method, args, null);

    assertNotNull(result);
  }

  @Test
  public void testFormatMethodCall() throws Throwable {
    ReturnsSmartNulls.ThrowingInterceptor throwingInterceptor = returnsSmartNulls.new ThrowingInterceptor(mock(InvocationOnMock.class));

    String result = throwingInterceptor.formatMethodCall();

    assertEquals("answer()", result);
  }

  @Test
  public void testAnswerWithDefaultReturnValue() throws Throwable {
    ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

    InvocationOnMock mockInvocation = mock(InvocationOnMock.class);
    when(mockInvocation.getMethod()).thenReturn(ReturnsSmartNulls.class.getMethod("answer", InvocationOnMock.class));
    when(delegate.answer(mockInvocation)).thenReturn(new Object());

    Object result = returnsSmartNulls.answer(mockInvocation);

    assertNotNull(result);
    assertEquals(Object.class, result.getClass());
  }

  @Test
  public void testAnswerWithNullDefaultReturnValue() throws Throwable {
    ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

    InvocationOnMock mockInvocation = mock(InvocationOnMock.class);
    when(mockInvocation.getMethod()).thenReturn(ReturnsSmartNulls.class.getMethod("answer", InvocationOnMock.class));
    when(delegate.answer(mockInvocation)).thenReturn(null);

    Object result = returnsSmartNulls.answer(mockInvocation);

    assertNull(result);
  }

}