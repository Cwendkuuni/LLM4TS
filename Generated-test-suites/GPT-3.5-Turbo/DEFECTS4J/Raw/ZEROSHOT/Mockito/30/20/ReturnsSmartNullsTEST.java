import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsMoreEmptyValues;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> delegate = mock(ReturnsMoreEmptyValues.class);
        
        when(delegate.answer(invocation)).thenReturn(null);
        
        Object result = returnsSmartNulls.answer(invocation);
        
        verify(delegate).answer(invocation);
        assertNull(result);
    }
}