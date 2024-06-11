import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_withNonNullReturnValue() throws Throwable {
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Answer<Object> answer = smartNulls.answer(invocation);
        
        Object result = answer.answer(invocation);
        
        assertNotNull(result);
    }
    
    @Test
    public void testAnswer_withNullReturnValue() throws Throwable {
        ReturnsSmartNulls smartNulls = new ReturnsSmartNulls();
        Answer<Object> delegate = new ReturnsMoreEmptyValues();
        
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(delegate.answer(invocation)).thenReturn(null);
        
        Object result = smartNulls.answer(invocation);
        
        assertNull(result);
    }
}