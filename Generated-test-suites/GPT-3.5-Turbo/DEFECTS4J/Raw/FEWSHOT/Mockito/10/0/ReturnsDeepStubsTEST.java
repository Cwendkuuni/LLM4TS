package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.defaultanswers.ReturnsDeepStubs;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;

import java.util.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Throwable {
        // Create a mock to test ReturnsDeepStubs answer
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(List.class);
        ReturnsDeepStubs answer = new ReturnsDeepStubs();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        invocation.getMock();
        invocation.getMethod();
        MockUtil mockUtil = mock(MockUtil.class);
        mockUtil.getMockHandler(invocation.getMock());
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        when(mockUtil.getMockHandler(invocation.getMock())).thenReturn(handler);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getInvocationForStubbing().matches(any(Invocation.class))).thenReturn(true);
        when(container.getStubbedInvocations()).thenReturn(getStubbedInvocations());
        assertEquals(deepStubbedAnswer, answer.answer(invocation));
        verify(container, times(1)).getInvocationForStubbing();
        verify(container, times(1)).getStubbedInvocations();
        verify(handler, times(1)).getInvocationContainer();
    }

    @Test
    public void testDeepStub() throws Throwable {
        // Create a mock to test deepStub method
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(null);
        GenericMetadataSupport returnTypeGenericMetadata1 = new GenericMetadataSupport(Object.class);
        when(invocation.getMock()).thenReturn(mock(Object.class));
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getInvocationForStubbing().matches(any(Invocation.class))).thenReturn(false);
        when(actualParameterizedType(invocation.getMock())).thenReturn(returnTypeGenericMetadata, returnTypeGenericMetadata1);
        when(recordDeepStubAnswer(newDeepStubMock(returnTypeGenericMetadata), container)).thenReturn(mock(Object.class));
        when(recordDeepStubAnswer(newDeepStubMock(returnTypeGenericMetadata1), container)).thenReturn(mock(Object.class));
        assertEquals(mock(Object.class), deepStub(invocation, returnTypeGenericMetadata));
        assertEquals(mock(Object.class), deepStub(invocation, returnTypeGenericMetadata1));
        verify(container, times(2)).getInvocationForStubbing();
        verify(actualParameterizedType(invocation.getMock()), times(2)).resolveGenericReturnType(invocation.getMethod());
        verify(handler, times(2)).getInvocationContainer();
    }

    @Test
    public void testNewDeepStubMock() {
        // Create a mock to test newDeepStubMock method
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(Map.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(Map.class);
        when(mockitoCore.mock(returnTypeGenericMetadata.rawType(), withSettingsUsing(returnTypeGenericMetadata))).thenReturn(mock(Map.class));
        assertEquals(mock(Map.class), newDeepStubMock(returnTypeGenericMetadata));
        verify(mockitoCore, times(1)).mock(returnTypeGenericMetadata.rawType(), withSettingsUsing(returnTypeGenericMetadata));
    }

    @Test
    public void testWithSettingsUsing() {
        // Create a mock to test withSettingsUsing method
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(Map.class);
        GenericMetadataSupport returnTypeGenericMetadata1 = new GenericMetadataSupport(Map.class, List.class);
        MockSettings mockSettings = mock(MockSettings.class);
        when(returnTypeGenericMetadata.hasRawExtraInterfaces()).thenReturn(false);
        when(mockSettings.serializable()).thenReturn(mockSettings);
        when(mockSettings.defaultAnswer(returnsDeepStubsAnswerUsing(returnTypeGenericMetadata))).thenReturn(mockSettings);
        when(withSettings()).thenReturn(mockSettings);
        assertEquals(mockSettings, withSettingsUsing(returnTypeGenericMetadata));
        verify(withSettings(), times(1)).extraInterfaces(returnTypeGenericMetadata.rawExtraInterfaces());

        when(returnTypeGenericMetadata1.hasRawExtraInterfaces()).thenReturn(true);
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class[]{List.class});
        assertEquals(mockSettings, withSettingsUsing(returnTypeGenericMetadata1));
        verify(withSettings(), times(1)).extraInterfaces(returnTypeGenericMetadata1.rawExtraInterfaces());
    }

    @Test
    public void testReturnsDeepStubsAnswerUsing() {
        // Create a mock to test returnsDeepStubsAnswerUsing method
        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(Map.class);
        ReturnsDeepStubsSerializationFallback fallback = mock(ReturnsDeepStubsSerializationFallback.class);
        when(fallback.writeReplace()).thenReturn(Mockito.RETURNS_DEEP_STUBS);
        assertEquals(fallback, returnsDeepStubsAnswerUsing(returnTypeGenericMetadata));
        verify(fallback, times(1)).writeReplace();
    }
    
    @Test
    public void testRecordDeepStubAnswer() throws Throwable {
        // Create a mock to test recordDeepStubAnswer method
        Object mock = mock(Object.class);
        when(handler.getInvocationContainer()).thenReturn(container);
        assertEquals(mock, recordDeepStubAnswer(mock, container));
        verify(container, times(1)).addAnswer(any(DeeplyStubbedAnswer.class), anyBoolean());
    }

    @Test
    public void testActualParameterizedType() {
        // Create a mock to test actualParameterizedType method
        Object mock = mock(Object.class);
        CreationSettings mockSettings = mock(CreationSettings.class);
        when(mockHandler.getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(mock);
        assertEquals(returnTypeGenericMetadata, actualParameterizedType(mock));
        verify(mockSettings, times(1)).getTypeToMock();
    }

    private Set<StubbedInvocationMatcher> getStubbedInvocations() {
        Set<StubbedInvocationMatcher> stubbedInvocations = new HashSet<>();
        stubbedInvocations.add(mock(StubbedInvocationMatcher.class));
        return stubbedInvocations;
    }

}