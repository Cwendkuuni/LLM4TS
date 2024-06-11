
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        // Mock the InvocationOnMock object
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        // Test when the defaultReturnValue is not null
        Answer<Object> delegate = mock(Answer.class);
        Object returnValue = new Object();
        when(delegate.answer(invocation)).thenReturn(returnValue);
        Object result = returnsSmartNulls.answer(invocation);
        assertEquals(returnValue, result);

        // Test when the defaultReturnValue is null and the return type is mockable
        delegate = mock(Answer.class);
        when(delegate.answer(invocation)).thenReturn(null);
        Class<?> type = Object.class;
        when(invocation.getMethod().getReturnType()).thenReturn(type);
        Object mockObject = mock(Object.class);
        when(ClassImposterizer.INSTANCE.canImposterise(type)).thenReturn(true);
        when(ClassImposterizer.INSTANCE.imposterise(any(Answer.class), eq(type))).thenReturn(mockObject);
        result = returnsSmartNulls.answer(invocation);
        assertEquals(mockObject, result);

        // Test when the defaultReturnValue is null and the return type is not mockable
        delegate = mock(Answer.class);
        when(delegate.answer(invocation)).thenReturn(null);
        type = String.class;
        when(invocation.getMethod().getReturnType()).thenReturn(type);
        result = returnsSmartNulls.answer(invocation);
        assertNull(result);
    }
}
