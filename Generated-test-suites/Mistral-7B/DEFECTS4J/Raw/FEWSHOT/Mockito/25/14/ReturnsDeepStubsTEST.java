package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.GenericMetadataSupport;
import org.mockito.internal.util.reflection.GenericTypeUtil;
import org.mockito.stubbing.AnswerWithMock;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswerWithMock() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        assertNotNull(mock);

        Object answer = new ReturnsDeepStubs().answer(new InvocationOnMock(mock, Object.class, new Object[0], new Object[0]));
        assertNotNull(answer);
        assertEquals(mock, answer);
    }

    @Test
    public void testAnswerWithExistingMock() {
        Object mock = mock(Object.class);
        Object deepMock = mock(Object.class, new ReturnsDeepStubs());

        InvocationContainerImpl container = new InvocationContainerImpl(new Object[0], null, new Object[0]);
        when(mock).thenReturn(deepMock);

        Object answer = new ReturnsDeepStubs().answer(new InvocationOnMock(mock, Object.class, new Object[0], new Object[0]));
        assertNotNull(answer);
        assertEquals(deepMock, answer);
    }

    @Test
    public void testAnswerWithRecordedMock() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationContainerImpl container = new InvocationContainerImpl(new Object[0], null, new Object[0]);

        Object recordedMock = new ReturnsDeepStubs().recordDeepStubMock(new InvocationOnMock(mock, Object.class, new Object[0], new Object[0]), container);
        assertNotNull(recordedMock);
        assertEquals(mock, recordedMock);

        Object answer = new ReturnsDeepStubs().answer(new InvocationOnMock(mock, Object.class, new Object[0], new Object[0]));
        assertNotNull(answer);
        assertEquals(recordedMock, answer);
    }

    @Test
    public void testAnswerWithRecordedMockForVerification() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationContainerImpl container = new InvocationContainerImpl(new Object[0], null, new Object[0]);

        Object recordedMock = new ReturnsDeepStubs().recordDeepStubMock(new InvocationOnMock(mock, Object.class, new Object[0], new Object[0]), container);
        assertNotNull(recordedMock);
        assertEquals(mock, recordedMock);

        when(mock.toString()).thenReturn("Deep Stub");

        InvocationOnMock invocation = new InvocationOnMock(mock, String.class, new Object[]{null}, new Object[0]);
        StubbedInvocationMatcher stubbedInvocationMatcher = new StubbedInvocationMatcher(invocation, new AnswerWithMock<String, Object>(recordedMock));
        container.addStubbedInvocation(stubbedInvocationMatcher);

        String answer = new ReturnsDeepStubs().answer(invocation);
        assertNotNull(answer);
        assertEquals("Deep Stub", answer);
        verify(mock).toString();
        verifyNoMoreInteractions(mock);
    }

    @Test
    public void testAnswerWithGenericType() {
        Object mock = mock(Map.class, new ReturnsDeepStubs());
        InvocationContainerImpl container = new InvocationContainerImpl(new Object[0], null, new Object[0]);

        Object recordedMock = new ReturnsDeepStubs().recordDeepStubMock(new InvocationOnMock(mock, LinkedHashMap.class, new Object[]{Map.class}, new Object[0]), container);
        assertNotNull(recordedMock);
        assertEquals(mock, recordedMock);

        InvocationOnMock invocation = new InvocationOnMock(mock, Map.class, new Object[]{LinkedHashMap.class}, new Object[0]);

        StubbedInvocationMatcher<Map<Object, Object>> stubbedInvocationMatcher = new StubbedInvocationMatcher<>(invocation, new AnswerWithMock<Map<Object, Object>, Object>(recordedMock));
        container.addStubbedInvocation(stubbedInvocationMatcher);

        Map<Object, Object> answer = new ReturnsDeepStubs().answer(invocation);
        assertNotNull(answer);
        assertEquals(recordedMock, answer);

        Type actualType = GenericTypeUtil.getRawType(invocation.getMethod().getReturnType());
        Type expectedType = LinkedHashMap.class;
        assertEquals(expectedType, actualType);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAnswerWithUnsupportedType() {
        Object mock = mock(Number[].class, new ReturnsDeepStubs());

        InvocationOnMock invocation = new InvocationOnMock(mock, Number[].class, new Object[0], new Object[0]);
        new ReturnsDeepStubs().answer(invocation);
    }
}