
package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsDeepStubsTest {
    
    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        
        // Create a mock for InvocationOnMock
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Test when rawType is not mockable
        MockitoCore mockitoCore = mock(MockitoCore.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(String.class);
        when(mockitoCore.isTypeMockable(String.class)).thenReturn(false);
        answer.mockitoCore = mockitoCore;
        ReturnsEmptyValues delegate = mock(ReturnsEmptyValues.class);
        when(delegate.returnValueFor(String.class)).thenReturn("String");
        answer.delegate = delegate;
        assertEquals("String", answer.answer(invocation));
        
        // Test when rawType is mockable
        when(returnTypeGenericMetadata.rawType()).thenReturn(List.class);
        when(mockitoCore.isTypeMockable(List.class)).thenReturn(true);
        when(invocation.getMock()).thenReturn(mock(List.class));
        InternalMockHandler<Object> mockHandler = mock(InternalMockHandler.class);
        when(new MockUtil().getMockHandler(invocation.getMock())).thenReturn(mockHandler);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(mockHandler.getInvocationContainer()).thenReturn(container);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())).thenReturn(true);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn("MockedAnswer");
        assertEquals("MockedAnswer", answer.answer(invocation));
        
        // Test when rawType is mockable and deep stub
        when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())).thenReturn(false);
        Object mock = mock(Object.class);
        when(mockitoCore.mock(List.class, answer.withSettingsUsing(returnTypeGenericMetadata))).thenReturn(mock);
        when(answer.createNewDeepStubMock(returnTypeGenericMetadata)).thenReturn(mock);
        when(answer.recordDeepStubMock(mock, container)).thenReturn(mock);
        assertEquals(mock, answer.answer(invocation));
        
        // Test actualParameterizedType method
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(mockHandler.getMockSettings()).thenReturn(mockSettings);
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        when(genericMetadataSupport.getTypeToMock()).thenReturn(List.class);
        when(new MockUtil().getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);
        assertEquals(genericMetadataSupport, answer.actualParameterizedType(mock));
    }

}
