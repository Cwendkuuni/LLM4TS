package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);

        Object result = returnsDeepStubs.answer(invocation);

        assertNotNull(result);
    }

    @Test
    public void testDeepStub() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport();

        Object result = returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata);

        assertNotNull(result);
    }

    @Test
    public void testNewDeepStubMock() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport();

        Object result = returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata);

        assertNotNull(result);
    }

    @Test
    public void testWithSettingsUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport();

        Object result = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);

        assertNotNull(result);
    }

    @Test
    public void testRecordDeepStubAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = new Object();
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);

        Object result = returnsDeepStubs.recordDeepStubAnswer(mock, container);

        assertNotNull(result);
    }

    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = new Object();

        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);

        assertNotNull(result);
    }

    @Test
    public void testReturnsDeepStubsSerializationFallback() {
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport();

        ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback returnsDeepStubsSerializationFallback = new ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback(returnTypeGenericMetadata);

        assertNotNull(returnsDeepStubsSerializationFallback);
    }

    @Test
    public void testDeeplyStubbedAnswer() {
        Object mock = new Object();

        ReturnsDeepStubs.DeeplyStubbedAnswer deeplyStubbedAnswer = new ReturnsDeepStubs.DeeplyStubbedAnswer(mock);

        assertNotNull(deeplyStubbedAnswer);
    }

    @Test
    public void testMockitoCore() {
        MockitoCore mockitoCore = ReturnsDeepStubs.mockitoCore();

        assertNotNull(mockitoCore);
    }

    @Test
    public void testDelegate() {
        ReturnsEmptyValues delegate = ReturnsDeepStubs.delegate();

        assertNotNull(delegate);
    }
}