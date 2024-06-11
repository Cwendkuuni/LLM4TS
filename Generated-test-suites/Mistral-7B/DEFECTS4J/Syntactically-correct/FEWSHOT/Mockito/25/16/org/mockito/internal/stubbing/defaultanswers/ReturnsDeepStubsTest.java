package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReturnsDeepStubsTest {
    @Test
    public void testAnswer() throws Exception {
        Object mock = mock(Map.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = new InvocationOnMock(mock, Arrays.asList(new Object[0]), Arrays.asList(new Class[0]), new Object[0][], new Throwable[0]);

        GenericMetadataSupport returnTypeGenericMetadata = new GenericMetadataSupport(Map.class);
        Class<?> rawType = returnTypeGenericMetadata.rawType();

        Object answer = new ReturnsDeepStubs().answer(invocation);
        assertNotNull(answer);
        verify(mock).answersQuestionsAbout(invocation.getMethod(), new Object[0]);

        Class<?> clz = invocation.getMethod().getReturnType();
        Map<Object, Object> map = (Map) answer;
        assertNotNull(map);
        map.put(1, "one");
        map.put(2, "two");

        Object deepStub = new ReturnsDeepStubs().answer(invocation);
        assertNotNull(deepStub);
        assert (deepStub == map);
    }

    @Test
    public void testDelegate() throws Exception {
        Object mock = mock(Map.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = new InvocationOnMock(mock, Arrays.asList(new Object[0]), Arrays.asList(new Class[0]), new Object[0][], new Throwable[0]);

        when(mock.entrySet().iterator().next()).thenReturn(new HashMap.SimpleEntry(1, "one"));

        Object answer = new ReturnsDeepStubs().answer(invocation);
        Map<Object, Object> map = (Map) answer;
        assertNotNull(map);
        map.put(1, "one");
        map.put(2, "two");

        Object emptyAnswer = new ReturnsEmptyValues().returnValueFor(Map.class);
        assertNull(emptyAnswer);

        Object answerForList = new ReturnsDeepStubs().answer(new InvocationOnMock(mock, Arrays.asList(new Object[0]), Arrays.asList(List.class), new Object[0][], new Throwable[0]));
        List<Object> list = (List) answerForList;
        assertNotNull(list);
        list.add("one");
        list.add("two");

        Object emptyAnswerForList = new ReturnsEmptyValues().returnValueFor(List.class);
        assertNull(emptyAnswerForList);
    }

    @Test
    public void testGetMock() throws Exception {
        Object mock = mock(Map.class, new ReturnsDeepStubs());

        InvocationOnMock invocation = new InvocationOnMock(mock, Arrays.asList(new Object[0]), Arrays.asList(new Class[0]), new Object[0][], new Throwable[0]);
        InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock);
        InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();

        Class<?> clz = invocation.getMethod().getReturnType();
        Object answer = new ReturnsDeepStubs().getMock(invocation);
        Map<Object, Object> map = (Map) answer;
        assertNotNull(map);
        map.put(1, "one");
        map.put(2, "two");

        verify(container).addAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return map;
            }
        }, false);

        Object deepStub = new ReturnsDeepStubs().getMock(invocation);
        assertNotNull(deepStub);
        assert (deepStub == map);
    }

    @Test
    public void testRecordDeepStubMock() throws Exception {
        Object mock = mock(Map.class, new ReturnsDeepStubs());

        InvocationOnMock invocation = new InvocationOnMock(mock, Arrays.asList(new Object[0]), Arrays.asList(new Class[0]), new Object[0][], new Throwable[0]);
        InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock);
        InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();

        Class<?> clz = invocation.getMethod().getReturnType();
        Object answer = new ReturnsDeepStubs().recordDeepStubMock(invocation, container);
        Map<Object, Object> map = (Map) answer;
        assertNotNull(map);
        map.put(1, "one");
        map.put(2, "two");

        verify(container).addAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return map;
            }
        }, false);

        Object deepStub = new ReturnsDeepStubs().getMock(invocation);
        assertNotNull(deepStub);
        assert (deepStub == map);
    }
}