package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mockito.Answers;
import org.mockito.InvocationOnMock;
import org.mockito.MockitoCore;
import org.mockito.MockSettings;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.invocation.InvocationContainerImpl;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.InvocationMatcher;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.Invocation;

public class ReturnsDeepStubsTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testAnswer() {
        MockitoCore mockitoCore = spy(new MockitoCore());
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs() {
            @Override
            protected MockitoCore mockitoCore() {
                return mockitoCore;
            }
        };
        doReturn(mockitoCore).when(mockitoCore).newMockitoCore();

        Class<?> rawType = String.class;
        InvocationOnMock invocation = mock(rawType);
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(rawType);

        Object answer = returnsDeepStubs.answer(invocation);

        assertEquals(mockitoCore, answer);

        verify(mockitoCore, times(1)).mock(rawType, any());
    }

    @Test
    public void testDelegateReturnsEmptyValue() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Object answer = returnsDeepStubs.delegate.returnValueFor(Integer.class);
        assertEquals(Collections.emptyList(), answer);
    }

    @Test
    public void testGetMock_MatchedInvocation() {
        Class<?> stringListClass = List.class.getGenericType();
        Class<?> stringClass = String.class;

        Object mock = mock(Map.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(stubbedInvocationMatcher.answersTo(any(InvocationOnMock.class))).thenReturn(mock);
        when(container.getStubbedInvocations()).thenReturn(Arrays.asList(stubbedInvocationMatcher));

        when(stubbedInvocationMatcher.getInvocation()).thenReturn(mock(Invocation.class));

        when(stubbedInvocationMatcher.answer(any(InvocationOnMock.class))).thenReturn("answer");

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        Object answer = returnsDeepStubs.getMock(mock(InvocationOnMock.class), mock(GenericMetadataSupport.class));

        assertEquals("answer", answer);

        verify(stubbedInvocationMatcher, times(1)).answer(any(InvocationOnMock.class));
    }

    @Test
    public void testGetMock_NotMatchedInvocation() {
        Class<?> stringListClass = List.class.getGenericType();
        Class<?> stringClass = String.class;

        Object mock = mock(Map.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        when(container.getInvocationForStubbing()).thenReturn(mock(Invocation.class));

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        Object answer = returnsDeepStubs.getMock(mock(InvocationOnMock.class), mock(GenericMetadataSupport.class));

        assertNull(answer);

        verify(container, never()).addAnswer(any(Answer.class), anyBoolean());
    }

    @Test
    public void testCreateNewDeepStubMock() {
        Class<?> stringClass = String.class;
        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(stringClass);

        Object mock = mock(stringClass);

        MockitoCore mockitoCore = mock(MockitoCore.class);
        when(mockitoCore.mock(stringClass, withSettings())).thenReturn(mock);

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs() {
            @Override
            protected MockitoCore mockitoCore() {
                return mockitoCore;
            }
        };

        Object answer = returnsDeepStubs.createNewDeepStubMock(returnTypeGenericMetadata);

        assertEquals(mock, answer);

        verify(mockitoCore, times(1)).mock(stringClass, withSettings());
    }

    @Test
    public void testWithSettingsUsing() {
        Class<?> listClass = List.class;
        Class<?> stringClass = String.class;

        GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
        when(returnTypeGenericMetadata.rawType()).thenReturn(listClass);
        when(returnTypeGenericMetadata.rawExtraInterfaces()).thenReturn(new Class[0]);

        MockSettings mockSettings = mock(MockSettings.class);
        when(mockSettings.defaultAnswer(any())).thenReturn(Answers.RETURNS_DEEP_STUBS);

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        Object answer = returnsDeepStubs.withSettingsUsing(returnTypeGenericMetadata);

        assertEquals(mockSettings, answer);

        verify(returnTypeGenericMetadata, times(1)).rawExtraInterfaces();
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        Class<?> stringClass = String.class;
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        Object mock = mock(stringClass);

        when(container.addAnswer(any(Answer.class), anyBoolean())).thenReturn(mock);

        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        Object answer = returnsDeepStubs.recordDeepStubMock(mock, container);

        assertEquals(mock, answer);

        verify(container, times(1)).addAnswer(any(Answer.class), anyBoolean());
    }
}