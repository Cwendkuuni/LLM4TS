import org.junit.Test;
import org.junit.Assert;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {
  
  @Test
  public void testAnswer() throws Throwable {
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    ReturnsEmptyValues delegate = mock(ReturnsEmptyValues.class);
    InvocationContainerImpl container = mock(InvocationContainerImpl.class);
    Object mock = mock(Object.class);

    when(delegate.returnValueFor(any())).thenReturn(null);
    when(invocation.getMock()).thenReturn(mock);
    when(container.getInvocationForStubbing().matches(any())).thenReturn(true);
    when(container.getStubbedInvocations().contains(any())).thenReturn(true);
    when(container.getStubbedInvocations().stream().filter(any())).thenReturn(null);
    when(delegate.returnValueFor(any())).thenReturn(null);
    when(invocation.getMethod().getReturnType()).thenReturn(Object.class);
    when(mock(Object.class, any())).thenReturn(mock);
    when(invocation.getMethod()).thenReturn(null);
    when(mock.getClass()).thenReturn(null);
    when(mock(Object.class, any())).thenReturn(mock);
    when(mock.getClass().getClassLoader()).thenReturn(null);
    when(delegate.returnValueFor(any())).thenReturn(null);

    ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
    Object result = returnsDeepStubs.answer(invocation);

    Assert.assertEquals(mock, result);
  }
}