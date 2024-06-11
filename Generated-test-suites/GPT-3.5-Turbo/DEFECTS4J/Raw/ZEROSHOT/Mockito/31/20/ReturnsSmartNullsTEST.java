import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

  ReturnsSmartNulls returnsSmartNulls;

  @Test
  public void testAnswer() throws Throwable {
    Answer<Object> delegateAnswer = Mockito.mock(Answer.class);
    when(delegateAnswer.answer(Mockito.any(InvocationOnMock.class))).thenReturn("value");
    returnsSmartNulls = new ReturnsSmartNulls(delegateAnswer);

    InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
    Object result = returnsSmartNulls.answer(invocation);

    assertEquals("value", result);
    verify(delegateAnswer, times(1)).answer(invocation);
  }

  @Test
  public void testAnswerNullReturnValue() throws Throwable {
    Answer<Object> delegateAnswer = Mockito.mock(Answer.class);
    when(delegateAnswer.answer(Mockito.any(InvocationOnMock.class))).thenReturn(null);
    returnsSmartNulls = new ReturnsSmartNulls(delegateAnswer);

    InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
    Object result = returnsSmartNulls.answer(invocation);

    assertNull(result);
    verify(delegateAnswer, times(1)).answer(invocation);
  }

  @Test
  public void testAnswerNonMockableReturnType() throws Throwable {
    Answer<Object> delegateAnswer = Mockito.mock(Answer.class);
    when(delegateAnswer.answer(Mockito.any(InvocationOnMock.class))).thenReturn("value");
    returnsSmartNulls = new ReturnsSmartNulls(delegateAnswer);

    InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
    when(invocation.getMethod().getReturnType()).thenReturn(String.class);
    Object result = returnsSmartNulls.answer(invocation);

    assertNull(result);
    verify(delegateAnswer, times(0)).answer(invocation);
  }
}