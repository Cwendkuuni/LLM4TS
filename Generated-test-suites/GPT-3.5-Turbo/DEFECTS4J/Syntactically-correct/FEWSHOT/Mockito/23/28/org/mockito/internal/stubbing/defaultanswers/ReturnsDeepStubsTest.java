package org.mockito.internal.stubbing.defaultanswers;

import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.internal.InternalMockHandler;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsDeepStubsTest {
    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        returnsDeepStubs.answer(invocation);
        
        InternalMockHandler<Object> mockHandler = mock(InternalMockHandler.class);
        when(mockHandler.getInvocationContainer()).thenReturn(mock(InvocationContainerImpl.class));
        when(invocation.getMock()).thenReturn(mockHandler);
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("hashCode"));
        returnsDeepStubs.answer(invocation);
    }

    @Test
    public void testGetMock() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(returnTypeGenericMetadata.rawType()).thenReturn(Class.class);
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class[0]);
        
        returnsDeepStubs.getMock(invocation, returnTypeGenericMetadata);
    }

    @Test
    public void testCreateNewDeepStubMock() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(Class.class);
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class[0]);
        
        returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata);
    }

    @Test
    public void testWithSettingsUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class[0]);
        
        returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);
        
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class[]{Serializable.class});
        
        returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(Class.class);
        
        returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(container.addAnswer(any(Answer.class), anyBoolean())).thenReturn(mock(Object.class));
        
        Object mock = mock(Object.class);
        
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock);
        
        returnsDeepStubs.recordDeepStubMock(mock, container);
    }

    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        
        MockUtil mockUtil = mock(MockUtil.class);
        when(mockUtil.getMockHandler(any())).thenReturn(mock(InternalMockHandler.class));
        returnsDeepStubs.actualParameterizedType(mockUtil);
    }
}