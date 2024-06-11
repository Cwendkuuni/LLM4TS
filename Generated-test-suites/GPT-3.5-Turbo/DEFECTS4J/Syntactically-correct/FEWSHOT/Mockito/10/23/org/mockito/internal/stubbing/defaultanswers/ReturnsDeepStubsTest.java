package org.mockito.internal.stubbing.defaultanswers;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsDeepStubsTest {
    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        
        // Initialize InvocationOnMock
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        // Mock actualParameterizedType method
        MockUtil mockUtil = mock(MockUtil.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        when(handler.getInvocationContainer()).thenReturn(new InvocationContainerImpl());
        when(mockUtil.getMockHandler(invocation.getMock())).thenReturn(handler);
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(mockSettings.getTypeToMock()).thenReturn(Object.class);
        when(handler.getMockSettings()).thenReturn(mockSettings);
        when(handler.getInvocationContainer()).thenReturn(new InvocationContainerImpl());
        when(mockUtil.getMockHandler(invocation.getMock())).thenReturn(handler);
        
        // Mock GenericMetadataSupport
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        when(genericMetadataSupport.rawType()).thenReturn(Object.class);
        when(mockSettings.getTypeToMock()).thenReturn(genericMetadataSupport);
        when(genericMetadataSupport.resolveGenericReturnType(any())).thenReturn(genericMetadataSupport);
        when(genericMetadataSupport.rawType()).thenReturn(Object.class);
        when(mockitoCore().isTypeMockable(Object.class)).thenReturn(false);
        
        // Mock mockitoCore()
        MockitoCore mockitoCore = mock(MockitoCore.class);
        when(mockitoCore().isTypeMockable(Object.class)).thenReturn(false);
        
        // Test answer() method
        Object result = returnsDeepStubs.answer(invocation);
        
        // Verify result
        assertTrue(result instanceof ReturnsEmptyValues);
    }

}