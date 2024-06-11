package org.mockito.internal.stubbing.defaultanswers;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsSmartNullsTest {
    
    @Test
    public void testAnswer() throws Throwable {
        // Mock invocation
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Set up delegate
        ReturnsMoreEmptyValues delegate = mock(ReturnsMoreEmptyValues.class);
        when(delegate.answer(invocation)).thenReturn(null);
        
        // Create ReturnsSmartNulls object
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        returnsSmartNulls.delegate = delegate;
        
        // Test answer method
        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);
        
        // Verify delegate method was called
        verify(delegate, times(1)).answer(invocation);
    }
}