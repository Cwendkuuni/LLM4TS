package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;

import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {
    @Test
    public void testAnswer() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        when(invocation.getMock()).thenReturn(returnTypeGenericMetadata);
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        doReturn(false).when(ReturnsDeepStubs.mockitoCore()).isTypeMockable(Object.class);
        
        returnsDeepStubs.answer(invocation);

        verify(ReturnsDeepStubs.mockitoCore(), times(1)).isTypeMockable(Object.class);
    }

    @Test
    public void testDeepStub() throws Throwable {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        ReturnsDeepStubs returnsDeepStubs = spy(new ReturnsDeepStubs());
        
        when(invocation.getMock()).thenReturn(handler);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[]{stubbedInvocationMatcher});
        when(container.getInvocationForStubbing()).thenReturn(stubbedInvocationMatcher);
        when(stubbedInvocationMatcher.getInvocation()).thenReturn(invocation);
        
        returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata);

        verify(container, times(1)).addAnswer(any(), eq(false));
    }

    @Test
    public void testNewDeepStubMock() {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        
        returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata);

        verify(ReturnsDeepStubs.mockitoCore(), times(1)).mock(returnTypeGenericMetadata.rawType(), withSettingsUsing(returnTypeGenericMetadata));
    }

    @Test
    public void testWithSettingsUsing() {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        
        returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);

        verify(returnTypeGenericMetadata, times(1)).hasRawExtraInterfaces();
        verify(returnTypeGenericMetadata, times(1)).rawExtraInterfaces();
        verify(returnTypeGenericMetadata, times(1)).rawType();
        verify(ReturnsDeepStubs.withSettings(), times(1)).serializable();
        verify(ReturnsDeepStubs.withSettings(), times(1)).defaultAnswer(returnsDeepStubsAnswerUsing(returnTypeGenericMetadata));
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        
        returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);

        verify(new ReturnsDeepStubsSerializationFallback(returnTypeGenericMetadata), times(1)).returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);
    }

    @Test
    public void testRecordDeepStubAnswer() throws Throwable {
        Object mock = mock(Object.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        
        returnsDeepStubs.recordDeepStubAnswer(mock, container);

        verify(container, times(1)).addAnswer(any(), eq(false));
    }

    @Test
    public void testActualParameterizedType() {
        Object mock = mock(Object.class);
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        
        returnsDeepStubs.actualParameterizedType(mock);

        verify(new MockUtil(), times(1)).getMockHandler(mock);
    }
    
    @Test
    public void testReturnsDeepStubsSerializationFallback() throws IOException {
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        ReturnsDeepStubsSerializationFallback returnsDeepStubsSerializationFallback = new ReturnsDeepStubsSerializationFallback(returnTypeGenericMetadata);
        
        returnsDeepStubsSerializationFallback.writeReplace();

        verify(Mockito.RETURNS_DEEP_STUBS, times(1)).writeReplace();
    }

    @Test
    public void testDeeplyStubbedAnswer() throws Throwable {
        Object mock = mock(Object.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        DeeplyStubbedAnswer deeplyStubbedAnswer = new DeeplyStubbedAnswer(mock);
        
        deeplyStubbedAnswer.answer(invocation);

        verify(invocation, times(1)).getMock();
    }

    @Test
    public void testMockitoCore() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        
        returnsDeepStubs.mockitoCore();

        verify(ReturnsDeepStubs.LazyHolder.MOCKITO_CORE, times(1)).times(Mockito.RETURNS_DEEP_STUBS);
    }

    @Test
    public void testDelegate() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        
        returnsDeepStubs.delegate();

        verify(ReturnsDeepStubs.LazyHolder.DELEGATE, times(1)).times(Mockito.RETURNS_DEEP_STUBS);
    }
}