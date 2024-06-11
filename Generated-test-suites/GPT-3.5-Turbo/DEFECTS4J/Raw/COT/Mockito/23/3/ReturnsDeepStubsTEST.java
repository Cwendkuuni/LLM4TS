
import static org.mockito.Mockito.*;
import org.mockito.internal.stubbing.defaultanswers.*;
import org.junit.Test;

public class ReturnsDeepStubsTest {

  // Test method for ReturnsDeepStubs#answer()
  @Test
  public void testAnswer() throws Throwable {
    ReturnsEmptyValues delegate = new ReturnsEmptyValues();
    ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

    // Mock objects
    Object mock1 = mock(Object.class);
    Object mock2 = mock(Object.class);

    // Test case 1: returnTypeGenericMetadata is not mockable
    InvocationOnMock invocation1 = mock(InvocationOnMock.class);
    when(invocation1.getMock()).thenReturn(mock1);
    when(invocation1.getMethod()).thenReturn(Object.class.getMethod("toString"));
    Object result1 = returnsDeepStubs.answer(invocation1);
    assertEquals(delegate.returnValueFor(Object.class), result1);

    // Test case 2: returnTypeGenericMetadata is mockable, invocation matches stubbed invocation
    InvocationOnMock invocation2 = mock(InvocationOnMock.class);
    when(invocation2.getMock()).thenReturn(mock2);
    when(invocation2.getMethod()).thenReturn(Object.class.getMethod("toString"));
    when(mock2.toString()).thenReturn("Mock Object");
    Object result2 = returnsDeepStubs.answer(invocation2);
    assertEquals("Mock Object", result2);

    // Test case 3: returnTypeGenericMetadata is mockable, deep stub
    InvocationOnMock invocation3 = mock(InvocationOnMock.class);
    when(invocation3.getMock()).thenReturn(mock2);
    when(invocation3.getMethod()).thenReturn(Object.class.getMethod("hashCode"));
    Object result3 = returnsDeepStubs.answer(invocation3);
    assertNotNull(result3);
    assertEquals(mock1, result3);

  }
}
