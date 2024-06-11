package org.mockito.internal.stubbing.defaultanswers;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        assertNull(returnsDeepStubs.answer(null));
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(false);
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(null);
        Object mock = mock(Object.class);
        mockitoCore().mock(Object.class, withSettings().defaultAnswer(returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata)));
        assertEquals(mock, returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata));
        when(returnTypeGenericMetadata.rawType()).thenReturn(null);
        assertNull(returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata));
    }

    @Test
    public void testNewDeepStubMock() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(false);
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(null);
        assertNotNull(returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata));
        when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(true);
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class<?>[]{Object.class});
        assertNotNull(returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata));
    }

    @Test
    public void testWithSettingsUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(false);
        assertEquals(withSettings().serializable().defaultAnswer(null), returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata));
        when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(true);
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class<?>[]{Object.class});
        assertEquals(withSettings().serializable().extraInterfaces(new Class<?>[]{Object.class}).defaultAnswer(null), returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata));
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        assertEquals(new ReturnsDeepStubsSerializationFallback(returnTypeGenericMetadata), returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata));
    }

    @Test
    public void testRecordDeepStubAnswer() throws Throwable {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object mock = mock(Object.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(container.getInvocationForStubbing()).thenReturn(null);
        assertThatCode(() -> returnsDeepStubs.recordDeepStubAnswer(mock, container)).doesNotThrowAnyException();
        when(container.getInvocationForStubbing()).thenReturn(mock(InvocationOnMock.class));
        assertThatCode(() -> returnsDeepStubs.recordDeepStubAnswer(mock, container)).doesNotThrowAnyException();
    }

    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        MockUtil mockUtil = mock(MockUtil.class);
        MockUtil.setMockUtil(mockUtil);
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        GenericMetadataSupport genericMetadataSupport = mock(GenericMetadataSupport.class);
        when(mockUtil.getMockHandler(mock).getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(Object.class);
        when(genericMetadataSupport.inferFrom(Object.class)).thenReturn(mock(GenericMetadataSupport.class));
        assertEquals(genericMetadataSupport, returnsDeepStubs.actualParameterizedType(mock));
    }

    @Test
    public void testReturnsDeepStubsSerializationFallback() throws IOException {
        ReturnsDeepStubsSerializationFallback returnsDeepStubsSerializationFallback = new ReturnsDeepStubsSerializationFallback(mock(GenericMetadataSupport.class));
        assertEquals(Mockito.RETURNS_DEEP_STUBS, returnsDeepStubsSerializationFallback.writeReplace());
    }

    @Test
    public void testDeeplyStubbedAnswer() {
        Object mock = mock(Object.class);
        DeeplyStubbedAnswer deeplyStubbedAnswer = new DeeplyStubbedAnswer(mock);
        assertEquals(mock, deeplyStubbedAnswer.answer(null));
    }

    @Test
    public void testMockitoCore() {
        assertNotNull(LazyHolder.MOCKITO_CORE);
    }

    @Test
    public void testDelegate() {
        assertNotNull(LazyHolder.DELEGATE);
    }
}