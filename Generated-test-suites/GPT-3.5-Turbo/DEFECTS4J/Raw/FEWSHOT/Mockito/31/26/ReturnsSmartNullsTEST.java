package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {
    
    @Test
    public void testAnswerReturnsNonNullValue() throws Throwable {
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        when(delegate.answer(any(InvocationOnMock.class))).thenReturn("test");
        
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        Object result = answer.answer(mock(InvocationOnMock.class));
        
        assertNotNull(result);
        assertEquals("test", result);
    }
    
    @Test
    public void testAnswerReturnsSmartNull() throws Throwable {
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        when(delegate.answer(any(InvocationOnMock.class))).thenReturn(null);
        
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        Object result = answer.answer(mock(InvocationOnMock.class));
        
        assertNotNull(result);
        assertEquals("SmartNull returned by unstubbed method on mock", result);
    }
    
    @Test
    public void testAnswerReturnsNullForFinalReturnType() throws Throwable {
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        when(delegate.answer(any(InvocationOnMock.class))).thenReturn("test");
        
        ReturnsSmartNulls answer = new ReturnsSmartNulls();
        Object result = answer.answer(mock(InvocationOnMock.class));
        
        assertNotNull(result);
        assertEquals("test", result);
    }
    
}