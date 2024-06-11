import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {

  @Test
  public void testAnswer() throws Throwable {
    ReturnsSmartNulls rs = new ReturnsSmartNulls();

    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMethod().getReturnType()).thenReturn(String.class);
    when(invocation.getMethod().getName()).thenReturn("getString");
    Object result = rs.answer(invocation);
    assertNull(result);

    when(invocation.getMethod().getReturnType()).thenReturn(Integer.class);
    when(invocation.getMethod().getName()).thenReturn("getInteger");
    result = rs.answer(invocation);
    assertNull(result);

    when(invocation.getMethod().getReturnType()).thenReturn(Object.class);
    when(invocation.getMethod().getName()).thenReturn("getObject");
    result = rs.answer(invocation);
    assertNull(result);

    when(invocation.getMethod().getReturnType()).thenReturn(ClassImposterizer.class);
    when(invocation.getMethod().getName()).thenReturn("getClassImposterizer");
    result = rs.answer(invocation);
    assertNull(result);
  }

  @Test
  public void testAnswerWithDelegate() throws Throwable {
    ReturnsSmartNulls rs = new ReturnsSmartNulls();

    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMethod().getReturnType()).thenReturn(String.class);
    when(invocation.getMethod().getName()).thenReturn("getString");

    ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
    when(delegate.answer(invocation)).thenReturn("abc");
    rs.setDelegate(delegate);

    Object result = rs.answer(invocation);
    assertEquals("abc", result);
  }

  @Test
  public void testAnswerWithNullDelegate() throws Throwable {
    ReturnsSmartNulls rs = new ReturnsSmartNulls();

    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMethod().getReturnType()).thenReturn(String.class);
    when(invocation.getMethod().getName()).thenReturn("getString");

    rs.setDelegate(null);

    Object result = rs.answer(invocation);
    assertNull(result);
  }

  @Test
  public void testAnswerWithFinalReturnType() throws Throwable {
    ReturnsSmartNulls rs = new ReturnsSmartNulls();

    InvocationOnMock invocation = mock(InvocationOnMock.class);
    when(invocation.getMethod().getReturnType()).thenReturn(ThrowsExceptionType.class);
    when(invocation.getMethod().getName()).thenReturn("getThrowsExceptionType");

    Object result = rs.answer(invocation);
    assertNull(result);
  }

  @Test(expected = NullPointerException.class)
  public void testAnswerWithNullInvocation() throws Throwable {
    ReturnsSmartNulls rs = new ReturnsSmartNulls();

    rs.answer(null);
  }

  private static class ThrowsExceptionType {
    public final void getThrowsExceptionType() throws Exception {
      throw new Exception();
    }
  }
}