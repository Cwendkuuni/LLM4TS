package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class ReturnsDeepStubsTest {
    
    @Test
    public void testAnswer() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        
        // Mock the invocation
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Create answer objects
        ReturnsEmptyValues returnsEmptyValues = mock(ReturnsEmptyValues.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        MockUtil mockUtil = mock(MockUtil.class);
        
        when(invocation.getMock()).thenReturn(mockObject);
        when(mockUtil.getMockHandler(mockObject)).thenReturn(mockHandler);
        when(mockHandler.getInvocationContainer()).thenReturn(container);
        
        // Test the answer method
        Object result = returnsEmptyValues.answer(invocation);
        
        // Verify the result
        assertEquals(mockObject, result);
    }

    @Test
    public void testGetMock() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        
        // Mock the invocation
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Create answer objects
        ReturnsEmptyValues returnsEmptyValues = mock(ReturnsEmptyValues.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        MockUtil mockUtil = mock(MockUtil.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        
        // Mock the method calls
        when(invocation.getMock()).thenReturn(mockObject);
        when(mockUtil.getMockHandler(mockObject)).thenReturn(mockHandler);
        when(mockHandler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(stubbedInvocationMatchers);
        when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())).thenReturn(true);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn(mockAnswer);
        
        // Test the getMock method
        Object result = returnsDeepStubs.getMock(invocation);
        
        // Verify the result
        assertEquals(mockAnswer, result);
    }

    @Test
    public void testRecordDeepStubMock() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        
        // Mock the invocation
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Create answer objects
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        MockUtil mockUtil = mock(MockUtil.class);
        MockHandler mockHandler = mock(MockHandler.class);
        Answer<Object> answer = mock(Answer.class);
        
        // Mock the method calls
        when(invocation.getMethod()).thenReturn(method);
        when(method.getReturnType()).thenReturn(returnType);
        when(mockUtil.mock(clz, returnsDeepStubs)).thenReturn(mockObject);
        when(mockUtil.getMockHandler(mockObject)).thenReturn(mockHandler);
        when(mockHandler.getInvocationContainer()).thenReturn(container);
        when(container.addAnswer(answer, false)).thenReturn(true);
        
        // Test the recordDeepStubMock method
        Object result = returnsDeepStubs.recordDeepStubMock(invocation, container);
        
        // Verify the result
        assertEquals(mockObject, result);
    }

    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        
        // Mock the mock object and the mock settings
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        MockUtil mockUtil = mock(MockUtil.class);
        
        // Mock the method calls
        when(mockUtil.getMockHandler(mock)).thenReturn(mockHandler);
        when(mockHandler.getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(typeToMock);
        
        // Test the actualParameterizedType method
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);
        
        // Verify the result
        assertEquals(inferredFrom, result);
    }
}