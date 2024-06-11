package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsDeepStubsTest {
    
    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object result = returnsDeepStubs.answer(mock(InvocationOnMock.class));
        assertNotNull(result);
    }

    @Test
    public void testDeepStub() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        Object result = returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata);
        assertNotNull(result);
    }
    
    @Test
    public void testNewDeepStubMock() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        Object result = returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata);
        assertNotNull(result);
    }

    @Test
    public void testWithSettingsUsing() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        MockSettings mockSettings = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);
        assertNotNull(mockSettings);
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        ReturnsDeepStubsSerializationFallback result = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);
        assertNotNull(result);
    }

    @Test
    public void testRecordDeepStubAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        Object result = returnsDeepStubs.recordDeepStubAnswer(mock, container);
        assertNotNull(result);
    }
    
    @Test
    public void testActualParameterizedType() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);
        assertNotNull(result);
    }

    @Test
    public void testReturnsDeepStubsSerializationFallback() throws Throwable {
        ReturnsDeepStubs.SerializationFallback returnsDeepStubs = new ReturnsDeepStubs.SerializationFallback(mock(GenericMetadataSupport.class));
        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock(Object.class));
        assertNotNull(result);
    }

    @Test
    public void testDeeplyStubbedAnswer() throws Throwable {
        ReturnsDeepStubs.DeeplyStubbedAnswer deeplyStubbedAnswer = new ReturnsDeepStubs.DeeplyStubbedAnswer(mock(Object.class));
        Object result = deeplyStubbedAnswer.answer(mock(InvocationOnMock.class));
        assertNotNull(result);
    }

    @Test
    public void testMockitoCore() throws Throwable {
        MockitoCore result = ReturnsDeepStubs.mockitoCore();
        assertNotNull(result);
    }

    @Test
    public void testDelegate() throws Throwable {
        ReturnsEmptyValues result = ReturnsDeepStubs.delegate();
        assertNotNull(result);
    }
}