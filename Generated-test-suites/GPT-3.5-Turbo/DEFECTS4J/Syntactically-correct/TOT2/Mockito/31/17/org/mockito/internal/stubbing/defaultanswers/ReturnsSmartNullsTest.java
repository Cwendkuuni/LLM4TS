import static org.junit.Assert.*;
import org.junit.Test;

public class ReturnsSmartNullsTest {

  @Test
  public void testAnswerWithNonNullDefaultReturnValue() {
    ReturnsSmartNulls testClass = new ReturnsSmartNulls();
    InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
    Mockito.when(invocation.getMethod().getReturnType()).thenReturn(String.class);
    Mockito.when(delegate.answer(invocation)).thenReturn("Mocked String");
    Object result = testClass.answer(invocation);
    assertNotNull(result);
    assertEquals("Mocked String", result);
  }

  @Test
  public void testAnswerWithNullDefaultReturnValueAndMockableType() {
    ReturnsSmartNulls testClass = new ReturnsSmartNulls();
    InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
    Mockito.when(invocation.getMethod().getReturnType()).thenReturn(Object.class);
    ClassImposterizer imposterizer = Mockito.mock(ClassImposterizer.class);
    Mockito.when(ClassImposterizer.INSTANCE.canImposterise(Object.class)).thenReturn(true);
    Mockito.when(ClassImposterizer.INSTANCE.imposterise(Mockito.any(), Object.class)).thenReturn("SmartNull");
    Object result = testClass.answer(invocation);
    assertEquals("SmartNull", result);
  }

  @Test
  public void testAnswerWithNullDefaultReturnValueAndNonMockableType() {
    ReturnsSmartNulls testClass = new ReturnsSmartNulls();
    InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
    Mockito.when(invocation.getMethod().getReturnType()).thenReturn(String.class);
    Mockito.when(ClassImposterizer.INSTANCE.canImposterise(String.class)).thenReturn(false);
    Object result = testClass.answer(invocation);
    assertNull(result);
  }

}