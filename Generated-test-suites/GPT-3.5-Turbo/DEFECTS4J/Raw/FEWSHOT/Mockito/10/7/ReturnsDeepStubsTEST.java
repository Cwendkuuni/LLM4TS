package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsDeepStubsTest {

    ReturnsDeepStubs returnsDeepStubs;

    @Test
    public void testAnswer() throws Throwable {
        returnsDeepStubs = new ReturnsDeepStubs();

        // Mocks
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        Object mock = mock(Object.class);

        // Stubbing
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock);
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(true);
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class[]{Serializable.class});

        Object result = returnsDeepStubs.answer(invocation);
        
        // Verification
        assertEquals(mock, result);
    }

    @Test
    public void testNewDeepStubMock() {
        returnsDeepStubs = new ReturnsDeepStubs();

        // Mocks
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        // Stubbing
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);

        Object result = returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata);
        
        // Verification
        assertEquals(Object.class, result.getClass());
    }

    @Test
    public void testWithSettingsUsing() {
        returnsDeepStubs = new ReturnsDeepStubs();

        // Mocks
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        MockSettings mockSettings = mock(MockSettings.class);

        // Stubbing
        when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(true);
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class[]{Serializable.class});
        when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(true);
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class[]{Serializable.class});

        MockSettings result = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);
        
        // Verification
        assertEquals(mockSettings, result);
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        returnsDeepStubs = new ReturnsDeepStubs();

        // Mocks
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        ReturnsDeepStubsSerializationFallback result = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);
        
        // Verification
        assertEquals(ReturnsDeepStubsSerializationFallback.class, result.getClass());
    }

    @Test
    public void testRecordDeepStubAnswer() throws Throwable {
        returnsDeepStubs = new ReturnsDeepStubs();

        // Mocks
        Object mock = mock(Object.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        // Stubbing
        returnsDeepStubs.recordDeepStubAnswer(mock, container);
        
        // Verification
        // verify that container.addAnswer() was called
    }

    @Test
    public void testActualParameterizedType() {
        returnsDeepStubs = new ReturnsDeepStubs();

        // Mocks
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);

        // Stubbing
        when(mockSettings.getTypeToMock()).thenReturn(Object.class);
        when(new MockUtil().getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);
        when(returnsDeepStubs.actualParameterizedType(mock)).thenCallRealMethod();
        when(GenericMetadataSupport.inferFrom(Object.class)).thenReturn(returnTypeGenericMetadata);

        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);
        
        // Verification
        assertEquals(returnTypeGenericMetadata, result);
    }
}