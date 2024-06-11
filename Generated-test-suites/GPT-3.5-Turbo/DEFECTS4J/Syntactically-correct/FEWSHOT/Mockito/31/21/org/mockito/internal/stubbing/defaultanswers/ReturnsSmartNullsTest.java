package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {
    
    private ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
    
    @Test
    public void testAnswer() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);
    }
    
    @Test
    public void testAnswerWithDefaultReturnValue() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(String.class);
        
        Object result = returnsSmartNulls.answer(invocation);
        assertNotNull(result);
        assertEquals("", result);
    }
    
    @Test
    public void testAnswerWithNonMockableReturnType() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(Integer.class);
        
        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);
    }
    
    // TODO Add more test cases to cover all the scenarios
    
}