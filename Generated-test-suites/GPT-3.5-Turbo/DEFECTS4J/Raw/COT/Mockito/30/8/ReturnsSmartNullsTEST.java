
import static org.mockito.Mockito.*;
import org.junit.Test;

public class ReturnsSmartNullsTest {

  @Test
  public void testAnswer() throws Throwable {
    // Test public Object answer(final InvocationOnMock invocation) throws Throwable
    ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
    InvocationOnMock invocation = mock(InvocationOnMock.class);

    // Test when defaultReturnValue is not null
    Answer<Object> delegate = mock(Answer.class);
    when(delegate.answer(invocation)).thenReturn("Default return value");
    Object result1 = returnsSmartNulls.answer(invocation);
    assertEquals("Default return value", result1);

    // Test when type is imposterizable and defaultReturnValue is null
    delegate = mock(Answer.class);
    when(delegate.answer(invocation)).thenReturn(null);
    ClassImposterizer imposterizer = mock(ClassImposterizer.class);
    when(imposterizer.canImposterise(Object.class)).thenReturn(true);
    when(imposterizer.imposterise(any(), eq(Object.class))).thenReturn("SmartNull");
    ReturnsSmartNulls.INSTANCE.setImposterizer(imposterizer);
    Object result2 = returnsSmartNulls.answer(invocation);
    assertEquals("SmartNull", result2);

    // Test when type is not imposterizable
    delegate = mock(Answer.class);
    when(delegate.answer(invocation)).thenReturn(null);
    ClassImposterizer imposterizer2 = mock(ClassImposterizer.class);
    when(imposterizer2.canImposterise(Object.class)).thenReturn(false);
    ReturnsSmartNulls.INSTANCE.setImposterizer(imposterizer2);
    Object result3 = returnsSmartNulls.answer(invocation);
    assertNull(result3);
  }
}
