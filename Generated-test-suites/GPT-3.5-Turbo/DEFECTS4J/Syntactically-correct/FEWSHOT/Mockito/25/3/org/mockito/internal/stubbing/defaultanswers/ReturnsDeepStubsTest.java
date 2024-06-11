package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.mockito.Mockito;

public class ReturnsDeepStubsTest {
    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMock()).thenReturn(Object.class);
        returnsDeepStubs.answer(invocation);
        // add assertions here
    }
    
    @Test
    public void testGetMock() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(Object.class);
        returnsDeepStubs.getMock(invocation);
        // add assertions here
    }
    
    @Test
    public void testRecordDeepStubMock() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
        returnsDeepStubs.recordDeepStubMock(invocation, container);
        // add assertions here
    }
    
    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = Mockito.mock(Object.class);
        returnsDeepStubs.actualParameterizedType(mock);
        // add assertions here
    }
}