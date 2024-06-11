import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mockito.Mockito;

public class ReturnsSmartNullsTest {

  @Test
  public void testNullReturnValue() {
    ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

    InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
    
    try {
      Object result = returnsSmartNulls.answer(invocation);
      assertNull(result);
    } catch (Throwable e) {
      fail("Expected no exception, but got:" + e.getMessage());
    }
  }
  
  @Test
  public void testUnmockableReturnValue() {
    ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

    InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
    Mockito.when(invocation.getMethod().getReturnType()).thenReturn(Void.class);
   
    try {
      Object result = returnsSmartNulls.answer(invocation);
      assertNull(result);
    } catch (Throwable e) {
      fail("Expected no exception, but got:" + e.getMessage());
    }
  }
  
  @Test
  public void testToStringReturnValue() {
    ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
    
    InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
    Mockito.when(invocation.getMethod().getName()).thenReturn("toString");
    
    try {
      Object result = returnsSmartNulls.answer(invocation);
      assertEquals("SmartNull returned by unstubbed toString() method on mock", result);
    } catch (Throwable e) {
      fail("Expected no exception, but got:" + e.getMessage());
    }
  }
  
  @Test
  public void testDelegateReturnValue() {
    ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
    
    ReturnsMoreEmptyValues delegate = new ReturnsMoreEmptyValues();
    Mockito.when(delegate.answer(Mockito.any(InvocationOnMock.class))).thenReturn("DelegateReturnValue");
    Mockito.doCallRealMethod().when(delegate).answer(Mockito.any(InvocationOnMock.class));
    
    InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
    Mockito.when(invocation.getMethod().getReturnType()).thenReturn(String.class);
    
    try {
      Object result = returnsSmartNulls.answer(invocation);
      assertEquals("DelegateReturnValue", result);
    } catch (Throwable e) {
      fail("Expected no exception, but got:" + e.getMessage());
    }
  }
}