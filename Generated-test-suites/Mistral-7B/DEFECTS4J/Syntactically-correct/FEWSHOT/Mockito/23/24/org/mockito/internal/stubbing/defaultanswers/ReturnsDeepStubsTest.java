package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.stubbing.AnswerWith;

public class ReturnsDeepStubsTest {

    @Test
    public void testGetMockWithGenericReturnType() throws Exception {
        interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
        }

        GenericsNest<Number> mock = mock(GenericsNest.class, new ReturnsDeepStubs());
        Number number = mock.entrySet().iterator().next().getValue().iterator().next();

        assertSame(mock, number);
        verify(mock).entrySet();
        verify(mock).iterator();
        verify(mock).getValue();
        verify(mock).iterator();
    }

    @Test
    public void testGetMockWithRawType() throws Exception {
        interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
        }

        GenericsNest<Number> mock = mock(GenericsNest.class, Answers.RETURNS_DEEP_STUBS);
        Map<Number, Set<Number>> mapMock = mock;

        assertSame(mapMock, mapMock);
        verify(mapMock).entrySet();
        verify(mapMock).iterator();
    }

    @Test
    public void testGetMockWithExtraInterfaces() throws Exception {
        interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>>, Comparable<GenericsNest<K>> {
        }

        GenericsNest<Number> mock = mock(GenericsNest.class, new ReturnsDeepStubs());
        GenericsNest<Number> otherMock = mock(GenericsNest.class, new ReturnsDeepStubs());

        assertSame(mock, otherMock);
        verify(mock).compareTo(otherMock);
    }

    @Test
    public void testGetMockWithSameInvocation() throws Exception {
        interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
        }

        GenericsNest<Number> mock = mock(GenericsNest.class, new ReturnsDeepStubs());

        InvocationOnMock invocation = mock(GenericsNest.class).entrySet().iterator().next();
        StubbedInvocationMatcher stubbedInvocationMatcher = new StubbedInvocationMatcher(invocation);

        when(mock).thenReturn(stubbedInvocationMatcher.answer(invocation));

        assertSame(mock, mock.entrySet().iterator().next().getValue());
    }

    @Test
    public void testGetMockWithNestedInvocation() throws Exception {
        interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
            Set<Number> getNumbers();
        }

        GenericsNest<Integer> mock = mock(GenericsNest.class, new ReturnsDeepStubs());
        Set<Number> numbers = mock.getNumbers();
        Number number = numbers.iterator().next();

        assertSame(mock, number);
        verify(mock).getNumbers();
        verify(mock).iterator();
    }

    @Test
    public void testRecordDeepStubMock() throws Exception {
        interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
        }

        GenericsNest<Number> mock = mock(GenericsNest.class, new ReturnsDeepStubs());
        InvocationContainerImpl container = Mockito.mock(InvocationContainerImpl.class);

        Object deepStubMock = createNewDeepStubMock(mock, container);

        InvocationOnMock invocation = mock(GenericsNest.class).entrySet().iterator().next();
        StubbedInvocationMatcher stubbedInvocationMatcher = new StubbedInvocationMatcher(invocation);

        container.addAnswer(new AnswerWith<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return deepStubMock;
            }
        }, false);

        assertSame(mock, deepStubMock);
    }

    private Object createNewDeepStubMock(Object mock, InvocationContainerImpl container) throws Throwable {
        Answer<Object> answer = new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return mock;
            }
        };

        container.addAnswer(answer, false);

        return mock;
    }
}