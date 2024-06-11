import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mockito;

public class ReturnsDeepStubsTest {
    
    @Test
    public void testAnswer() throws Throwable {
        ReturnsEmptyValues delegate = mock(ReturnsEmptyValues.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        Class<?> rawType = mock(Class.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        
        when(invocation.getMock()).thenReturn(mock);
        when(returnTypeGenericMetadata.rawType()).thenReturn(rawType);
        when(new MockCreationValidator().isTypeMockable(rawType)).thenReturn(true);
        when(new MockUtil().getMockHandler(mock)).thenReturn(handler);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())).thenReturn(true);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn(mock);
        when(new MockUtil().getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(Object.class);
        
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object actual = returnsDeepStubs.answer(invocation);
        
        assertEquals(mock, actual);
        
        verify(delegate, never()).returnValueFor(rawType);
        verify(invocation, times(1)).getMock();
        verify(invocation, times(1)).getMethod();
        verify(returnTypeGenericMetadata, times(1)).rawType();
        verify(new MockCreationValidator(), times(1)).isTypeMockable(rawType);
        verify(new MockUtil(), times(1)).getMockHandler(mock);
        verify(handler, times(1)).getInvocationContainer();
        verify(container, times(1)).getStubbedInvocations();
        verify(container, times(1)).getInvocationForStubbing();
        verify(stubbedInvocationMatcher, times(1)).getInvocation();
        verify(stubbedInvocationMatcher, times(1)).answer(invocation);
        verify(new MockUtil(), times(1)).getMockHandler(mock);
        verify(handler, times(1)).getMockSettings();
        verify(mockSettings, times(1)).getTypeToMock();
        
        reset(delegate, invocation, returnTypeGenericMetadata, rawType, handler, container, stubbedInvocationMatcher, mock, mockSettings);

        
        when(invocation.getMock()).thenReturn(mock);
        when(returnTypeGenericMetadata.rawType()).thenReturn(rawType);
        when(new MockCreationValidator().isTypeMockable(rawType)).thenReturn(false);
        when(delegate.returnValueFor(rawType)).thenReturn(mock);
        when(new MockUtil().getMockHandler(mock)).thenReturn(handler);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(Collections.emptyList());
        when(handler.getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(Object.class);
        
        actual = returnsDeepStubs.answer(invocation);
        
        assertEquals(mock, actual);
        
        verify(delegate, times(1)).returnValueFor(rawType);
        verify(invocation, times(1)).getMock();
        verify(invocation, times(2)).getMethod();
        verify(returnTypeGenericMetadata, times(1)).rawType();
        verify(new MockCreationValidator(), times(1)).isTypeMockable(rawType);
        verify(new MockUtil(), times(1)).getMockHandler(mock);
        verify(handler, times(1)).getInvocationContainer();
        verify(container, times(1)).getStubbedInvocations();
        verify(handler, times(1)).getMockSettings();
        verify(mockSettings, times(1)).getTypeToMock();

    }
    
    @Test
    public void testGetMock() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        Object mock = mock(Object.class);
        
        when(new MockUtil().getMockHandler(invocation.getMock())).thenReturn(handler);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())).thenReturn(true);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn(mock);
        
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object actual = returnsDeepStubs.getMock(invocation);
        
        assertEquals(mock, actual);
        
        verify(invocation, times(2)).getMock();
        verify(handler, times(1)).getInvocationContainer();
        verify(container, times(1)).getStubbedInvocations();
        verify(container, times(1)).getInvocationForStubbing();
        verify(stubbedInvocationMatcher, times(1)).getInvocation();
        verify(stubbedInvocationMatcher, times(1)).answer(invocation);
        
        reset(invocation, handler, container, stubbedInvocationMatcher, mock);
        
        when(new MockUtil().getMockHandler(invocation.getMock())).thenReturn(handler);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())).thenReturn(false);
        when(new MockUtil().getMockHandler(invocation.getMock())).thenReturn(handler);
        when(handler.getMockSettings().getDefaultAnswer()).thenReturn(Mockito.RETURNS_DEFAULTS);
        when(Mockito.RETURNS_DEFAULTS.answer(invocation)).thenReturn(mock);
        
        actual = returnsDeepStubs.getMock(invocation);
        
        assertEquals(mock, actual);
        
        verify(invocation, times(4)).getMock();
        verify(handler, times(2)).getInvocationContainer();
        verify(container, times(2)).getStubbedInvocations();
        verify(container, times(2)).getInvocationForStubbing();
        verify(stubbedInvocationMatcher, times(1)).getInvocation();
        verify(new MockUtil(), times(1)).getMockHandler(invocation.getMock());
        verify(handler, times(1)).getMockSettings();
        verify(handler.getMockSettings().getDefaultAnswer(), times(1)).answer(invocation);
        
        
    }
    
    @Test
    public void testRecordDeepStubMock() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        Object mock = mock(Object.class);
        Class<?> clz = mock(Class.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        
        when(invocation.getMethod()).thenReturn(mock(java.lang.reflect.Method.class));
        when(invocation.getMethod().getReturnType()).thenReturn(clz);
        when(org.mockito.Mockito.mock(clz, new ReturnsDeepStubs())).thenReturn(mock);
        when(new MockUtil().getMockHandler(invocation.getMock())).thenReturn(mock(InternalMockHandler.class));
        when(new MockUtil().getMockHandler(invocation.getMock()).getInvocationContainer()).thenReturn(container);
        doNothing().when(container).addAnswer(any(Answer.class), anyBoolean());
        when(new MockUtil().getMockHandler(invocation.getMock())).thenReturn(mock(InternalMockHandler.class));
        when(new MockUtil().getMockHandler(invocation.getMock()).getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(clz);
        
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object actual = returnsDeepStubs.recordDeepStubMock(invocation, container);
        
        assertEquals(mock, actual);
        
        verify(invocation, times(1)).getMethod();
        verify(invocation.getMethod(), times(1)).getReturnType();
        verify(org.mockito.Mockito.class, times(1)).mock(clz, new ReturnsDeepStubs());
        verify(new MockUtil(), times(1)).getMockHandler(invocation.getMock());
        verify(new MockUtil().getMockHandler(invocation.getMock()).getInvocationContainer(), times(1)).addAnswer(any(Answer.class), anyBoolean());
        verify(new MockUtil(), times(1)).getMockHandler(invocation.getMock());
        verify(new MockUtil().getMockHandler(invocation.getMock()), times(1)).getMockSettings();
        verify(mockSettings, times(1)).getTypeToMock();
        
    }
    
    @Test
    public void testActualParameterizedType() throws Throwable {
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        
        when(new MockUtil().getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(mock(Class.class));
        when(GenericMetadataSupport.inferFrom(mockSettings.getTypeToMock())).thenReturn(genericMetadataSupport);
        
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport actual = returnsDeepStubs.actualParameterizedType(mock);
        
        assertEquals(genericMetadataSupport, actual);
        
        verify(new MockUtil(), times(1)).getMockHandler(mock);
        verify(new MockUtil().getMockHandler(mock), times(1)).getMockSettings();
        verify(mockSettings, times(1)).getTypeToMock();
        verify(GenericMetadataSupport.class, times(1)).inferFrom(mockSettings.getTypeToMock());
    }
    
}