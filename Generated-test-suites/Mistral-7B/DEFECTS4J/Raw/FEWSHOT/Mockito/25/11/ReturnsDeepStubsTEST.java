package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.Invocation;
import org.mockito.stubbing.Answer;

import java.io.Serializable;
import java.lang.reflect.Type;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswerDeepStubForSimpleType() {
        MockitoAnswer answer = mock(Answer.class, new ReturnsDeepStubs());
        Object mock = answer.answer(mock(Object.class));
        assertEquals(mock, answer.answer(mock(Object.class)));
    }

    @Test
    public void testAnswerDeepStubForGenericType() {
        MockitoAnswer answer = mock(Answer.class, new ReturnsDeepStubs());

        Class<Map<String, List<Integer>>> clazz = (Class<Map<String, List<Integer>>>) Collection.class.getGenericSuperclass();
        Map<String, List<Integer>> map = answer.answer(mock(clazz));
        List<Integer> list = map.values().iterator().next();
        Integer number = list.iterator().next();
        assertEquals(number, answer.answer(mock(number.getClass())));
    }

    @Test
    public void testGetMockForMockableType() throws Throwable {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationContainerImpl container = new MockUtil().getInvocationContainer(mock);

        Object deepStub = ReturnsDeepStubs.this.getMock(mock(InvocationOnMock.class).withArguments(mock));
        assertEquals(deepStub, container.getInvocationForStubbing().getMock());
    }

    @Test
    public void testGetMockForNonMockableType() throws Throwable {
        Object mock = mock(String.class);
        InvocationContainerImpl container = new MockUtil().getInvocationContainer(mock);

        assertNull(ReturnsDeepStubs.this.getMock(mock(InvocationOnMock.class).withArguments(mock)));
    }

    @Test
    public void testRecordDeepStubMockForSimpleType() throws Throwable {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = mock(InvocationOnMock.class).withArguments(mock);
        InvocationContainerImpl container = new MockUtil().getInvocationContainer(mock);

        Object deepStub = ReturnsDeepStubs.this.recordDeepStubMock(invocation, container);
        assertEquals(deepStub, container.getInvocationForStubbing().getMock());
    }

    @Test
    public void testRecordDeepStubMockForGenericType() throws Throwable {
        Class<Map<String, List<Integer>>> clazz = (Class<Map<String, List<Integer>>>) Collection.class.getGenericSuperclass();
        Object mock = mock(clazz, new ReturnsDeepStubs());
        InvocationOnMock invocation = mock(InvocationOnMock.class).withArguments(mock);
        InvocationContainerImpl container = new MockUtil().getInvocationContainer(mock);

        Object deepStub = ReturnsDeepStubs.this.recordDeepStubMock(invocation, container);
        assertEquals(deepStub, container.getInvocationForStubbing().getMock());
    }

    @Test
    public void testAnswerForDeepStub() throws Throwable {
        Object mock = mock(Map.class, new ReturnsDeepStubs());
        Map<String, List<Integer>> map = (Map<String, List<Integer>>) mock;
        List<Integer> list = map.values().iterator().next();
        Integer number = list.iterator().next();

        InvocationOnMock invocation = mock(InvocationOnMock.class).withArguments(mock);
        InvocationContainerImpl container = new MockUtil().getInvocationContainer(mock);

        when(container.getInvocationForStubbing()).thenReturn(invocation);
        when(container.getAnswer()).thenReturn(new ConstansAnswer(map));

        Object answer = ReturnsDeepStubs.this.answer(invocation);
        assertEquals(map, answer);
    }

    @Test
    public void testAnswerForEmptyValue() throws Throwable {
        Answer<Object> answer = mock(Answer.class, new ReturnsEmptyValues());
        Object emptyValue = answer.returnValueFor(String.class);
        assertNull(emptyValue);
    }
}