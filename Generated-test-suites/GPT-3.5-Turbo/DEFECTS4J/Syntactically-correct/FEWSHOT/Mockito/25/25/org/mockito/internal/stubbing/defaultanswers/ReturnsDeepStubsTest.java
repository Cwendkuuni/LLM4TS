package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsEmptyValues delegate = mock(ReturnsEmptyValues.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InvocationOnMock invocation2 = mock(InvocationOnMock.class);
        InvocationOnMock invocation3 = mock(InvocationOnMock.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        
        when(invocation.getMock()).thenReturn(delegate);
        when(invocation.getMethod()).thenReturn(null);
        when(delegate.returnValueFor(null)).thenReturn(null);
        assertNotNull(returnDeepStubs.answer(invocation));
        
        when(invocation.getMock()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(null);
        assertEquals(returnsDeepStubs.recordDeepStubMock(invocation, container), returnsDeepStubs.answer(invocation));
        
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(stubbedInvocationMatcher.getInvocation()).thenReturn(invocation2);
        when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[] {stubbedInvocationMatcher});
        when(container.getInvocationForStubbing().matches(invocation2)).thenReturn(true);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn(null);
        assertNotNull(returnsDeepStubs.answer(invocation));
        
        when(container.getInvocationForStubbing().matches(invocation3)).thenReturn(false);
        assertNotNull(returnsDeepStubs.answer(invocation3));
    }
}