package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsDeepStubsTest {
    @Test
    public void testAnswer() throws Throwable {
        // Create mock objects
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        Object mock3 = mock(Object.class);
        
        // Set up the invocation
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock1);
        
        // Create an instance of the ReturnsDeepStubs class
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        
        // Test the answer method
        assertEquals(mock1, returnsDeepStubs.answer(invocation));
        
        // Set up another invocation
        when(invocation.getMock()).thenReturn(mock2);
        
        // Test the answer method again
        assertEquals(mock2, returnsDeepStubs.answer(invocation));
        
        // Set up a third invocation
        when(invocation.getMock()).thenReturn(mock3);
        
        // Test the answer method once more
        assertEquals(mock3, returnsDeepStubs.answer(invocation));
    }
}