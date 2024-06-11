package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setUp() {
        returnsDeepStubs = spy(new ReturnsDeepStubs());
    }

    @Test
    public void testAnswer() throws Throwable {
        ReturnsDeepStubs.GenericMetadataSupport returnTypeGenericMetadata = mock(ReturnsDeepStubs.GenericMetadataSupport.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(returnsDeepStubs);
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        when(container.getInvocationForStubbing()).thenReturn(invocation);
        when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())).thenReturn(true);

        assertSame(stubbedInvocationMatcher.answer(invocation), returnsDeepStubs.answer(invocation));
    }

    @Test(expected = Throwable.class)
    public void testAnswerTypeNotMockable() throws Throwable {
        ReturnsDeepStubs.GenericMetadataSupport returnTypeGenericMetadata = mock(ReturnsDeepStubs.GenericMetadataSupport.class);
        MockitoCore mockitoCore = mock(MockitoCore.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(returnsDeepStubs);
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(mockitoCore.isTypeMockable(Object.class)).thenReturn(false);

        assertSame(returnsDeepStubs.delegate().returnValueFor(Object.class), returnsDeepStubs.answer(invocation));
    }

    @Test
    public void testDeepStub() throws Throwable {
        ReturnsDeepStubs.GenericMetadataSupport returnTypeGenericMetadata = mock(ReturnsDeepStubs.GenericMetadataSupport.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        Object mockObject = new Object();

        when(invocation.getMock()).thenReturn(returnsDeepStubs);
        when(invocation.getMethod()).thenReturn(mock(Method.class));
        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        when(container.getInvocationForStubbing()).thenReturn(invocation);
        when(container.getInvocationForStubbing().matches(stubbedInvocationMatcher.getInvocation())).thenReturn(false);


        when(returnsDeepStubs.actualParameterizedType(returnsDeepStubs)).thenReturn(returnTypeGenericMetadata);
        when(newDeepStubMock(returnTypeGenericMetadata)).thenReturn(mockObject);
        when(recordDeepStubAnswer(mockObject, container)).thenReturn(mockObject);

        assertSame(mockObject, returnsDeepStubs.deepStub(invocation, returnTypeGenericMetadata));
    }

    @Test
    public void testNewDeepStubMock() {
        ReturnsDeepStubs.GenericMetadataSupport returnTypeGenericMetadata = mock(ReturnsDeepStubs.GenericMetadataSupport.class);
        ReturnsDeepStubs.GenericMetadataSupport returnTypeGenericMetadataMock = mock(ReturnsDeepStubs.GenericMetadataSupport.class);
        MockUtil mockUtil = mock(MockUtil.class);

        when(returnTypeGenericMetadata.rawType()).thenReturn(Object.class);
        when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(true);
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class[]{Serializable.class});
        when(mockUtil.mock(Object.class, withSettingsUsing(returnTypeGenericMetadataMock))).thenReturn(new Object());

        assertNotNull(returnsDeepStubs.newDeepStubMock(returnTypeGenericMetadata));
    }

    @Test
    public void testWithSettingsUsing() {
        ReturnsDeepStubs.GenericMetadataSupport returnTypeGenericMetadata = mock(ReturnsDeepStubs.GenericMetadataSupport.class);
        MockSettings mockSettings = mock(MockSettings.class);

        when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(true);
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class[]{Serializable.class});
        when(withSettings().extraInterfaces(returnTypeGenericMetadata.rawExtraInterfaces())).thenReturn(mockSettings);

        assertSame(mockSettings, returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata));
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        ReturnsDeepStubs.GenericMetadataSupport returnTypeGenericMetadata = mock(ReturnsDeepStubs.GenericMetadataSupport.class);
        ReturnsDeepStubsSerializationFallback returnsDeepStubsSerializationFallback = mock(ReturnsDeepStubsSerializationFallback.class);

        when(returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata))
                .thenReturn(returnsDeepStubsSerializationFallback);

        assertSame(returnsDeepStubsSerializationFallback, returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata));
    }

    @Test
    public void testRecordDeepStubAnswer() {
        ReturnsDeepStubs.GenericMetadataSupport returnTypeGenericMetadata = mock(ReturnsDeepStubs.GenericMetadataSupport.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        Object mock = new Object();

        when(recordDeepStubAnswer(mock, container)).thenReturn(mock);

        assertSame(mock, returnsDeepStubs.recordDeepStubAnswer(mock, container));
    }

    @Test
    public void testActualParameterizedType() {
        ReturnsDeepStubs.MockUtil mockUtil = mock(ReturnsDeepStubs.MockUtil.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        ReturnsDeepStubs.GenericMetadataSupport genericMetadataSupport = mock(ReturnsDeepStubs.GenericMetadataSupport.class);

        when(mockUtil.getMockHandler(returnsDeepStubs)).thenReturn(mockUtil);
        when(mockUtil.getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(Object.class);
        when(GenericMetadataSupport.inferFrom(mockSettings.getTypeToMock())).thenReturn(genericMetadataSupport);

        assertSame(genericMetadataSupport, returnsDeepStubs.actualParameterizedType(returnsDeepStubs));
    }

    @Test
    public void testReturnsDeepStubsSerializationFallback() throws IOException {
        ReturnsDeepStubs.GenericMetadataSupport returnTypeGenericMetadata = mock(ReturnsDeepStubs.GenericMetadataSupport.class);
        Mockito.RETURNS_DEEP_STUBS.returnsDeepStubsSerializationFallback = returnTypeGenericMetadata;
        Object returnsDeepStubsSerializationFallback = new ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback(returnTypeGenericMetadata);

        assertEquals(returnTypeGenericMetadata, ((ReturnsDeepStubs.ReturnsDeepStubsSerializationFallback) returnsDeepStubsSerializationFallback).returnTypeGenericMetadata);
    }

    @Test
    public void testDeeplyStubbedAnswer() throws Throwable {
        Object mockObject = new Object();
        ReturnsDeepStubs.DeeplyStubbedAnswer deeplyStubbedAnswer = new ReturnsDeepStubs.DeeplyStubbedAnswer(mockObject);
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        assertSame(mockObject, deeplyStubbedAnswer.answer(invocation));
    }

    @Test
    public void testMockitoCore() {
        ReturnsDeepStubs.LazyHolder.MOCKITO_CORE = new MockitoCore();

        assertNotNull(returnsDeepStubs.mockitoCore());
    }

    @Test
    public void testDelegate() {
        ReturnsEmptyValues returnsEmptyValues = mock(ReturnsEmptyValues.class);
        ReturnsDeepStubs.LazyHolder.DELEGATE = returnsEmptyValues;

        assertNotNull(returnsDeepStubs.delegate());
    }
}