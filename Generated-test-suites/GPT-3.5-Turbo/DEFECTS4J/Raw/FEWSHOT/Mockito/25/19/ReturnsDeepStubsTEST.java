package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsDeepStubsTest {
    
    private ReturnsEmptyValues delegate = mock(ReturnsEmptyValues.class);
    
    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn("test");
        
        Object result = returnsDeepStubs.answer(invocation);
        
        assertEquals(delegate.returnValueFor(String.class), result);
        verifyZeroInteractions(delegate);
    }
    
    @Test
    public void testGetMock() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        MockUtil mockUtil = mock(MockUtil.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn("test");
        
        when(mockUtil.getMockHandler("test")).thenReturn(handler);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(Arrays.asList(stubbedInvocationMatcher));
        when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())).thenReturn(true);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn("result");
        
        Object result = returnsDeepStubs.getMock(invocation);
        
        assertEquals("result", result);
        verify(mockUtil).getMockHandler("test");
        verify(handler).getInvocationContainer();
        verify(container).getStubbedInvocations();
        verify(container).getInvocationForStubbing();
        verify(stubbedInvocationMatcher).answer(invocation);
        verifyZeroInteractions(delegate);
    }
    
    @Test
    public void testRecordDeepStubMock() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(invocation.getMethod().getReturnType()).thenReturn(String.class);
        when(container.addAnswer(any(), anyBoolean())).thenReturn(true);
        
        Object result = returnsDeepStubs.recordDeepStubMock(invocation, container);
        
        assertNotNull(result);
        verify(container).addAnswer(any(), anyBoolean());
        verifyZeroInteractions(delegate);
    }
    
    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        MockUtil mockUtil = mock(MockUtil.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(mockSettings.getTypeToMock()).thenReturn(String.class);
        when(mockUtil.getMockHandler("test")).thenReturn("test");
        
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType("test");
        
        assertNotNull(result);
        verify(mockUtil).getMockHandler("test");
        verify(mockSettings).getTypeToMock();
        verifyZeroInteractions(delegate);
    }
}