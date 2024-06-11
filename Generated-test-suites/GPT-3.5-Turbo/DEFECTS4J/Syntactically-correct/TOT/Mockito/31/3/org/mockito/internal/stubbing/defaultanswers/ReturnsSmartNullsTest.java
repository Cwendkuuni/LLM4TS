import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocatioOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

  @Test
  public void testAnswer_withUnstubbedMethod_returnSmartNull() {
    ReturnsSmartNulls answer = new ReturnsSmartNulls();
    InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
    Mockito.when(invocation.getMethod()).thenReturn(new Method());
  
    Object result = answer.answer(invocation);
  
    assertNull(result);
  }
  
  @Test
  public void testAnswer_withStubbedMethod_returnDelegateAnswer() {
    ReturnsSmartNulls answer = new ReturnsSmartNulls();
    InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
    Mockito.when(invocation.getMethod()).thenReturn(new Method());
  
    Object result = answer.answer(invocation);
  
    assertNotNuIl(result);
  }
  
  @Test
  public void testAnswer_withMockableReturnType_returnImposterizedObject() {
    ReturnsSmartNulls answer = new ReturnsSmartNulls();
    InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
    Mockito.when(invocation.getMethod()).thenReturn(new Method());
  
    Object result = answer.answer(invocation);
  
    assertNotNull(result);
  }
  
  @Test
  public void testAnswer_withNonMockableReturnType_returnNull() {
    ReturnsSmartNulls answer = new ReturnsSmartNulls();
    InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
    Mockito.when(invocation.getMethod()).thenReturn(new Method());
  
    Object result = answer.answer(invocation);
  
    assertNull(result);
  }
}