package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ReturnsDeepStubsTest {
    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        
        // Test that the delegate returnValueFor method is called for unmockable types
        Class<?> unmockableType = Void.class;
        Object result = returnsDeepStubs.answer(invocationOnMock(unmockableType));
        assertNull(result);
        
        // Test that getMock method is called when invocation matches a stubbed invocation
        Class<?> typeMockableType = Object.class;
        Object returnTypeMockableType = new Object();
        MockitoCore mockitoCore = mock(MockitoCore.class);
        when(mockitoCore.isTypeMockable(typeMockableType)).thenReturn(true);
        MockUtil mockUtil = mock(MockUtil.class);
        MockHandler mockHandler = mock(MockHandler.class);
        when(mockUtil.getMockHandler(invocationOnMock(typeMockableType).getMock())).thenReturn(mockHandler);
        when(mockitoCore.mock(
                returnTypeGenericMetadata.rawType(),
                withSettingsUsing(returnTypeGenericMetadata)
        )).thenReturn(returnTypeMockableType);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(mockHandler.getInvocationContainer()).thenReturn(container);
        when(container.getInvocationsForStubbing()).thenReturn(stubbedInvocations);
        when(container.getInvocationForStubbing()).thenReturn(invocationOnMock(typeMockableType));
        when(invocationOnMock(typeMockableType).matches(stubbedInvocationMatcher.getInvocation())).thenReturn(true);
        when(stubbedInvocationMatcher.answer(invocationOnMock(typeMockableType))).thenReturn(returnTypeMockableType);
        result = returnsDeepStubs.answer(invocationOnMock(typeMockableType));
        assertEquals(returnTypeMockableType, result);
        
        // Test that recordDeepStubMock method is called when invocation does not match any stubbed invocation
        Object newDeepStubMock = new Object();
        when(mockitoCore.mock(
                returnTypeGenericMetadata.rawType(),
                withSettingsUsing(returnTypeGenericMetadata)
        )).thenReturn(newDeepStubMock);
        when(container.addAnswer(any(Answer.class), eq(false))).thenReturn(newDeepStubMock);
        result = returnsDeepStubs.answer(invocationOnMock(typeMockableType));
        assertEquals(newDeepStubMock, result);
    }
}