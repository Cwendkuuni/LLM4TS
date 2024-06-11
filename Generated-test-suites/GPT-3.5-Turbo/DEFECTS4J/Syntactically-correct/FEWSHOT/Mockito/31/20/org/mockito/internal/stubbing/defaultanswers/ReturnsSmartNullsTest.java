package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        when(delegate.answer(mock(InvocationOnMock.class))).thenReturn(null);
        
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        returnsSmartNulls.delegate = delegate;
        
        Object result = returnsSmartNulls.answer(mock(InvocationOnMock.class));
        assertNull(result);
    }
    
    @Test
    public void testAnswer_nonMockableType() throws Throwable {
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        when(delegate.answer(mock(InvocationOnMock.class))).thenReturn(null);
        
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        returnsSmartNulls.delegate = delegate;
        
        Object result = returnsSmartNulls.answer(mock(InvocationOnMock.class));
        assertNull(result);
    }
    
    @Test
    public void testAnswer_defaultReturnValueNotNull() throws Throwable {
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        when(delegate.answer(mock(InvocationOnMock.class))).thenReturn(new Object());
        
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        returnsSmartNulls.delegate = delegate;
        
        Object result = returnsSmartNulls.answer(mock(InvocationOnMock.class));
        assertEquals(new Object(), result);
    }
}