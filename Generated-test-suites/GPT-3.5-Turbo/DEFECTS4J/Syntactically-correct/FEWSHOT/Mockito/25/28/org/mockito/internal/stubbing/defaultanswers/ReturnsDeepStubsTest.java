package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.junit.Test;

public class ReturnsDeepStubsTest {
    
    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        Class<?> rawType = mock(Class.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(returnTypeGenericMetadata.rawType()).thenReturn(rawType);
        when(new MockCreationValidator().isTypeMockable(rawType)).thenReturn(true);
        when(new MockUtil().getMockHandler(invocation.getMock())).thenReturn(handler);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[]{});
        when(container.getInvocationForStubbing()).thenReturn(mock(StubbedInvocationMatcher.class));
        when(container.getInvocationForStubbing().matches(any())).thenReturn(false);
        when(rawType.getReturnType()).thenReturn(mock(Class.class));
        when(handler.addAnswer(any(), anyBoolean())).thenReturn(null);
        
        Object result = returnsDeepStubs.answer(invocation);
        
        assertNotNull(result);
    }
    
    @Test
    public void testGetMock() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(new MockUtil().getMockHandler(invocation.getMock())).thenReturn(handler);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[]{stubbedInvocationMatcher});
        when(container.getInvocationForStubbing()).thenReturn(mock(StubbedInvocationMatcher.class));
        when(container.getInvocationForStubbing().matches(any())).thenReturn(true);
        when(stubbedInvocationMatcher.answer(any())).thenReturn(null);
        
        Object result = returnsDeepStubs.getMock(invocation);
        
        assertNotNull(result);
    }
    
    @Test
    public void testRecordDeepStubMock() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        Class<?> clz = mock(Class.class);
        Object mock = mock(Object.class);
        
        when(invocation.getMethod()).thenReturn(mock(java.lang.reflect.Method.class));
        when(invocation.getMethod().getReturnType()).thenReturn(clz);
        when(org.mockito.Mockito.mock(clz, returnsDeepStubs)).thenReturn(mock);
        when(container.addAnswer(any(), anyBoolean())).thenReturn(null);
        
        Object result = returnsDeepStubs.recordDeepStubMock(invocation, container);
        
        assertNotNull(result);
    }
    
    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        
        when(new MockUtil().getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(mock(Class.class));
        when(GenericMetadataSupport.inferFrom(mockSettings.getTypeToMock())).thenReturn(genericMetadataSupport);
        
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);
        
        assertNotNull(result);
    }
    
}