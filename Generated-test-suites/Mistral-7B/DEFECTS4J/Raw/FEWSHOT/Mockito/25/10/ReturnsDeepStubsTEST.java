package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.stubbing.Answer;

import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer_returnsNullForNonMockableType() {
        Class<Object> type = Object.class;
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        Object result = returnsDeepStubs.answer(new InvocationOnMock(null, null, type, null, null));

        assertNull(result);
    }

    @Test
    public void testAnswer_returnsDeepStubForMockableType() {
        Map<Integer, List<String>> map = mock(Map.class, new ReturnsDeepStubs());
        List<String> list = mock(List.class);
        list.add("one");
        map.put(1, list);

        List<String> result = (List<String>) map.entrySet().iterator().next().getValue();
        String firstElement = result.iterator().next();

        assertEquals("one", firstElement);
        verify(map).entrySet();
    }

    @Test
    public void testAnswer_returnsEmptyValueForEmptyCollection() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        Object result = returnsDeepStubs.answer(new InvocationOnMock(null, null, Collection.class, null, null));

        assertNotNull(result);
        assertInstanceOf(Collections.class, result);
    }

    @Test
    public void testGetMock_returnsExistingMock() {
        Map<Integer, List<String>> map = mock(Map.class, new ReturnsDeepStubs());
        List<String> list = mock(List.class);
        list.add("one");
        map.put(1, list);

        Map<Integer, List<String>> result = (Map<Integer, List<String>>) ReturnsDeepStubs.this.getMock(new InvocationOnMock(map, new Invocation(null, null, Map.class, new Object[0], null), null, null, new Object[0], null));

        assertEquals(map, result);
    }

    @Test
    public void testGetMock_returnsNewMock() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

        Map<Integer, List<String>> result = (Map<Integer, List<String>>) ReturnsDeepStubs.this.getMock(new InvocationOnMock(null, null, Map.class, null, null));

        assertNotNull(result);
        verify(ReturnsDeepStubs.this).recordDeepStubMock(eq(null), isA(InvocationContainerImpl.class));
    }

    @Test
    public void testRecordDeepStubMock_recordsAnswerForProvidedInvocation() {
        ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();
        Map<Integer, List<String>> map = mock(Map.class, new ReturnsDeepStubs());

        Answer<Object> answer = mock(Answer.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        when(container.getInvocationForStubbing()).thenReturn(new InvocationOnMock(map, new Invocation(null, null, Map.class, new Object[0], null), null, null, new Object[0], null));

        ReturnsDeepStubs.this.recordDeepStubMock(new InvocationOnMock(map, new Invocation(null, null, Map.class, new Object[0], null), null, null, new Object[0], null), container);

        verify(container).addAnswer(answer, false);
    }

    private static class Invocation {
        private final Type returnType;
        private final Object[] arguments;

        Invocation(Type returnType, Object[] arguments) {
            this.returnType = returnType;
            this.arguments = arguments;
        }
    }
}