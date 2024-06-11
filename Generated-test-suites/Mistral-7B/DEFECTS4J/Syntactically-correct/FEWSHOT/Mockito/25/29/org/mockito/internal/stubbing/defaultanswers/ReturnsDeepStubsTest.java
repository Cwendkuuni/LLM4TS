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

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Map;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer() throws Exception {
        Object mock = mock(Map.class, new ReturnsDeepStubs());
        when(mock.entrySet()).thenReturn(Collections.singletonMap("foo", mock(Number.class, new ReturnsEmptyValues())));

        Map<String, Number> map = (Map<String, Number>) mock;
        Number number = map.entrySet().iterator().next().getValue().iterator().next();

        assertEquals(mock(Number.class), number);
        verify(mock).answer(eq(any(InvocationOnMock.class)));
    }

    @Test
    public void testGetMock() throws Exception {
        Object mock = mock(Object.class);
        InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock);
        InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();

        Object deepStub = new ReturnsDeepStubs().getMock(mock(InvocationOnMock.class, any(Invocation.class)));

        assertNotSame(mock, deepStub);
        assertTrue(deepStub instanceof Object);
        verify(mock).answer(eq(any(InvocationOnMock.class)));
    }

    @Test
    public void testRecordDeepStubMock() throws Exception {
        Object mock = mock(Object.class);
        InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock);
        InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();
        InvocationOnMock invocation = mock(InvocationOnMock.class);

        Object deepStub = new ReturnsDeepStubs().recordDeepStubMock(invocation, container);

        assertNotSame(mock, deepStub);
        assertTrue(deepStub instanceof Object);
        verify(mock).answer(eq(invocation));
        verify(mock).answer(eq(invocation)); // verify that answer was called twice
    }

    @Test
    public void testActualParameterizedType() throws Exception {
        Object mock = mock(Map.class, new ReturnsDeepStubs());
        Type type = actualParameterizedType(mock);

        assertTrue(type instanceof GenericMetadataSupport);
        assertEquals(Map.class, type.getRawType());
        assertEquals(Map.class, type.getTypeName());
    }

    @Test
    public void testAnswerWithNonMockableType() throws Exception {
        Object mock = mock(Object.class, new ReturnsDeepStubs());

        assertSame(new ReturnsEmptyValues(), new ReturnsDeepStubs().answer(mock(InvocationOnMock.class, any(Invocation.class))));
    }

    @Test(expected = IllegalStateException.class)
    public void testAnswerWithNullInvocation() throws Exception {
        new ReturnsDeepStubs().answer(null);
    }
}