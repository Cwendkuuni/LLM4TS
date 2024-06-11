
package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsEmptyValues delegate = mock(ReturnsEmptyValues.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        when(invocation.getMock()).thenReturn(delegate);

        // Test 1: MockCreationValidator.isTypeMockable returns false
        when(delegate.returnValueFor(String.class)).thenReturn("test1");
        assertEquals("test1", returnsDeepStubs.answer(invocation));

        // Test 2: MockCreationValidator.isTypeMockable returns true
        when(delegate.returnValueFor(Integer.class)).thenReturn(123);
        
        // StubbedInvocations
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(delegate.getInvocationContainer()).thenReturn(container);
        
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(container.getStubbedInvocations()).thenReturn(Arrays.asList(stubbedInvocationMatcher));
        when(container.getInvocationForStubbing().matches(any())).thenReturn(true);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn("test2");
        
        // RecordDeepStubMock
        Object mock = mock(Object.class);
        when(Mockito.mock(any(Class.class), eq(returnsDeepStubs))).thenReturn(mock);
        
        assertEquals("test2", returnsDeepStubs.answer(invocation));
        verify(delegate, times(2)).returnValueFor(any(Class.class));
        verify(container).addAnswer(any(Answer.class), eq(false));
        verify(Mockito.class).mock(any(Class.class), eq(returnsDeepStubs));
    }
}
