
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

  @Test
  public void testAnswer() throws Throwable {
    ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
    InvocationOnMock invocation = mock(InvocationOnMock.class);
    
    Answer<Object> delegate = (Answer<Object>) mock(Answer.class);
    when(delegate.answer(invocation)).thenReturn(null);
    
    Object result = returnsSmartNulls.answer(invocation);
    
    assertNull(result);
    verify(delegate).answer(invocation);
  }
}
