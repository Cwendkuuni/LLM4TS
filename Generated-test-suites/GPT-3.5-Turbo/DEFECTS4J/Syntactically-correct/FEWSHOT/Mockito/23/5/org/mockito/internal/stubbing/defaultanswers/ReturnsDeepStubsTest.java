package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.InternalMockHandler;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class ReturnsDeepStubsTest {
    private ReturnsDeepStubs returnsDeepStubs;

    @Before
    public void setUp() {
        returnsDeepStubs = new ReturnsDeepStubs();
    }

    @Test
    public void testAnswer() throws Throwable {
        InvocationOnMock invocationOnMock = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocationOnMock.getMock()).thenReturn(Map.class);
        Mockito.when(invocationOnMock.getMethod()).thenReturn(Map.class.getMethod("entrySet"));
        Object result = returnsDeepStubs.answer(invocationOnMock);
        assertTrue(result instanceof Set);
    }

    @Test
    public void testGetMock() throws Throwable {
        InvocationOnMock invocationOnMock = Mockito.mock(InvocationOnMock.class);
        GenericMetadataSupport metadataSupport = Mockito.mock(GenericMetadataSupport.class);
        InternalMockHandler<Object> handler = Mockito.mock(InternalMockHandler.class);
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = Mockito.mock(StubbedInvocationMatcher.class);

        Mockito.when(invocationOnMock.getMock()).thenReturn(Map.class);
        Mockito.when(metadataSupport.rawType()).thenReturn(Map.class);
        Mockito.when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        Mockito.when(stubbedInvocationMatcher.getInvocation()).thenReturn(Mockito.mock(Invocation.class));
        Mockito.when(container.getInvocationForStubbing().matches(Mockito.any(Invocation.class))).thenReturn(true);
        Mockito.when(stubbedInvocationMatcher.answer(Mockito.any(InvocationOnMock.class))).thenReturn("Mock Answer");

        Mockito.when(handler.getInvocationContainer()).thenReturn(container);
        Mockito.when(handler.invoke(Mockito.any(InvocationOnMock.class))).thenReturn("Mock Answer");

        Mockito.when(invocationOnMock.getMethod()).thenReturn(Map.class.getMethod("entrySet"));
        Mockito.when(invocationOnMock.getMethod()).thenReturn(Map.class.getMethod("entrySet"));

        Mockito.when(metadataSupport.rawType()).thenReturn(Map.class);

        Object result = returnsDeepStubs.getMock(invocationOnMock, metadataSupport);
        assertEquals("Mock Answer", result);
    }

    @Test
    public void testCreateNewDeepStubMock() {
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
        Mockito.when(returnTypeGenericMetadata.rawType()).thenReturn(Map.class);
        Object result = returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata);
        assertTrue(result instanceof Map);
    }

    @Test
    public void testWithSettingsUsing() {
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
        Mockito.when(returnTypeGenericMetadata.rawType()).thenReturn(Map.class);
        MockSettings mockSettings = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);
        assertTrue(mockSettings.getDefaultAnswer() instanceof ReturnsDeepStubs);
    }

    @Test
    public void testReturnsDeepStubAnswerUsing() {
        GenericMetadataSupport returnTypeGenericMetadata = Mockito.mock(GenericMetadataSupport.class);
        Mockito.when(returnTypeGenericMetadata.rawType()).thenReturn(Map.class);
        ReturnsDeepStubs deepStubsAnswer = returnsDeepStubs.returnsDeepStubsAnswerUsing(returnTypeGenericMetadata);
        assertEquals(returnTypeGenericMetadata, deepStubsAnswer.actualParameterizedType(null));
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        Object mock = Mockito.mock(Object.class);
        InternalMockHandler<Object> handler = Mockito.mock(InternalMockHandler.class);
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);

        Mockito.when(handler.getInvocationContainer()).thenReturn(container);

        returnsDeepStubs.recordDeepStubMock(mock, container);

        Mockito.verify(container, Mockito.times(1)).addAnswer(Mockito.any(Answer.class), Mockito.anyBoolean());
    }

    @Test
    public void testActualParameterizedType() {
        MockUtil mockUtil = Mockito.mock(MockUtil.class);
        CreationSettings creationSettings = Mockito.mock(CreationSettings.class);
        Mockito.when(mockUtil.getMockHandler(Mockito.any(Object.class))).thenReturn(creationSettings);
        Mockito.when(creationSettings.getTypeToMock()).thenReturn(Map.class);

        ReturnsDeepStubs deepStubs = new ReturnsDeepStubs();
        deepStubs.setMockUtil(mockUtil);

        GenericMetadataSupport metadataSupport = deepStubs.actualParameterizedType(mockUtil);

        assertEquals(Map.class, metadataSupport.rawType());
    }
}