import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;

public class ReturnsSmartNullsTest {

  @Test
  public void testAnswer() throws Throwable {
    ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
    
    // Test case when the delegate answer returns a default value
    Object returnValue = new Object();
    Mockito.when(delegate.answer(invocation)).thenReturn(returnValue);
    Object result = returnsSmartNulls.answer(invocation);
    assertEquals(returnValue, result);

    // Test case when the delegate answer returns null
    Mockito.when(delegate.answer(invocation)).thenReturn(null);
    Object result = returnsSmartNulls.answer(invocation);
    assertNull(result);

    // Test case when the delegate answer returns a non-mockable object
    Mockito.when(delegate.answer(invocation)).thenReturn(new NonMockableObject());
    Object result = returnsSmartNulls.answer(invocation);
    assertNull(result);

    // Test case when the delegate answer returns a mockable object
    Mockito.when(delegate.answer(invocation)).thenReturn(new MockableObject());
    Object result = returnsSmartNulls.answer(invocation);
    assertTrue(result instanceof MockableObject);
  }

}