package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        InvocationOnMock invocation1 = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata1 = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata1.rawType()).thenReturn(Object.class);
        when(invocation1.getMock()).thenReturn(returnTypeGenericMetadata1);
        assertEquals(Object.class, returnsDeepStubs.answer(invocation1));

        InvocationOnMock invocation2 = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata2 = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata2.rawType()).thenReturn(String.class);
        when(invocation2.getMock()).thenReturn(returnTypeGenericMetadata2);
        assertEquals(String.class, returnsDeepStubs.answer(invocation2));
    }

    @Test
    public void testDeepStub() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        InvocationOnMock invocation1 = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata1 = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata1.rawType()).thenReturn(Object.class);
        when(invocation1.getMock()).thenReturn(returnTypeGenericMetadata1);

        InvocationOnMock invocation2 = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata2 = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata2.rawType()).thenReturn(Object.class);
        when(invocation2.getMock()).thenReturn(returnTypeGenericMetadata2);
        when(invocation2.getMethod()).thenReturn(Object.class.getMethod("toString"));

        // Add stubbed invocation matcher here

        assertEquals("", returnsDeepStubs.deepStub(invocation1, returnTypeGenericMetadata1));
        assertEquals("", returnsDeepStubs.deepStub(invocation2, returnTypeGenericMetadata2));
    }

    @Test
    public void testNewDeepStubMock() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        GenericMetadataSupport returnTypeGenericMetadata1 = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata1.rawType()).thenReturn(Object.class);

        GenericMetadataSupport returnTypeGenericMetadata2 = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata2.rawType()).thenReturn(String.class);

        assertEquals(Object.class, returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata1).getClass());
        assertEquals(String.class, returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata2).getClass());
    }

    @Test
    public void testWithSettingsUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        GenericMetadataSupport returnTypeGenericMetadata1 = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata1.rawExtraInterfaces()).thenReturn(new Class<?>[]{Comparable.class, Cloneable.class});

        GenericMetadataSupport returnTypeGenericMetadata2 = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata2.rawExtraInterfaces()).thenReturn(null);

        assertEquals(returnTypeGenericMetadata1.rawExtraInterfaces().length + 1,
                returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata1).extraInterfaces(returnTypeGenericMetadata1.rawExtraInterfaces()).getMockableTypeMap().size());
        assertEquals(1, returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata2).getMockableTypeMap().size());
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        GenericMetadataSupport returnTypeGenericMetadata1 = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata1.rawType()).thenReturn(Object.class);

        GenericMetadataSupport returnTypeGenericMetadata2 = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata2.rawType()).thenReturn(String.class);

        assertEquals(returnsDeepStubs, returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata1));
        assertEquals(returnsDeepStubs, returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata2));
    }

    @Test
    public void testRecordDeepStubAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        Object mock1 = mock(Object.class);
        InternalMockHandler<Object> mockHandler1 = mock(InternalMockHandler.class);
        InvocationContainerImpl container1 = mock(InvocationContainerImpl.class);
        when(mockHandler1.getInvocationContainer()).thenReturn(container1);
        when(mock1.getMockHandler()).thenReturn(mockHandler1);

        Object mock2 = mock(Object.class);
        InternalMockHandler<Object> mockHandler2 = mock(InternalMockHandler.class);
        InvocationContainerImpl container2 = mock(InvocationContainerImpl.class);
        when(mockHandler2.getInvocationContainer()).thenReturn(container2);
        when(mock2.getMockHandler()).thenReturn(mockHandler2);

        when(container1.getInvocationForStubbing().matches(container2.getInvocationForStubbing())).thenReturn(true);

        assertEquals(mock1, returnsDeepStubs.recordDeepStubAnswer(mock1, container1));
        assertEquals(mock2, returnsDeepStubs.recordDeepStubAnswer(mock2, container2));
    }

    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(mockSettings.getTypeToMock()).thenReturn(Object.class);
        when(mock.getMockSettings()).thenReturn(mockSettings);

        assertEquals(Object.class, returnsDeepStubs.actualParameterizedType(mock).rawType());
    }

    @Test
    public void testReturnsDeepStubsSerializationFallback() throws IOException {
        ReturnsDeepStubs.SerializationFallback serializationFallback = new ReturnsDeepStubs.SerializationFallback(null);

        assertEquals(Mockito.RETURNS_DEEP_STUBS, serializationFallback.writeReplace());
    }

    @Test
    public void testDeeplyStubbedAnswer() throws Throwable {
        ReturnsDeepStubs.DeeplyStubbedAnswer deeplyStubbedAnswer = new ReturnsDeepStubs.DeeplyStubbedAnswer(null);
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        assertEquals(null, deeplyStubbedAnswer.answer(invocation));
    }
}