package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswerWithMockedType() throws Exception {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        when(mock.toString()).thenReturn("mock");

        Object mockReturn = new ReturnsDeepStubs().answer(new InvocationOnMock(mock, new MethodTestClass(), "toString", new Class[]{Object.class}, null, new Object[0]));
        assertNotNull(mockReturn);
        assertEquals("mock", mockReturn.toString());
        verify(mock).toString();
    }

    @Test
    public void testAnswerWithNonMockableType() throws Exception {
        Class<Integer> intClass = Integer.class;
        Object mock = mock(Object.class, new ReturnsDeepStubs());

        Object nonMockableReturn = new ReturnsDeepStubs().answer(new InvocationOnMock(mock, intClass, "toString", new Class[]{Object.class}, null, new Object[0]));
        assertNotNull(nonMockableReturn);
        assertEquals(new ReturnsEmptyValues().returnValueFor(intClass), nonMockableReturn);
    }

    @Test
    public void testAnswerWithNestedGenericType() throws Exception {
        interface NestedGenerics<K extends Comparable<K> & Cloneable, V extends Number> extends Map<K, Set<V>> {
        }

        Object mock = mock(NestedGenerics.class, new ReturnsDeepStubs());

        when(mock.entrySet().iterator().next().getValue()).thenReturn(mock(Number.class));

        Object nestedGenericReturn = new ReturnsDeepStubs().answer(new InvocationOnMock(mock, NestedGenerics.class, "entrySet", new Class[0], null, new Object[0]));

        NestedGenerics<Object, Object> nestedGenericsMock = (NestedGenerics<Object, Object>) mock;
        assertEquals(nestedGenericsMock.entrySet().iterator().next().getValue(), nestedGenericReturn);
    }

    @Test
    public void testAnswerWithDeepStub() throws Exception {
        interface NestedGenerics<K extends Comparable<K> & Cloneable, V extends Number> extends Map<K, Set<V>> {
        }

        Object mock = mock(Object.class, new ReturnsDeepStubs());
        NestedGenerics<Object, Object> nestedGenericsMock = mock(NestedGenerics.class, new ReturnsDeepStubs());

        when(nestedGenericsMock.entrySet().iterator().next().getValue()).thenReturn(mock);
        when(mock.toString()).thenReturn("mock");

        Object deepStubReturn = new ReturnsDeepStubs().answer(new InvocationOnMock(nestedGenericsMock, NestedGenerics.class, "entrySet", new Class[0], null, new Object[0]));
        assertNotNull(deepStubReturn);
        assertEquals("mock", deepStubReturn.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testAnswerWithDuplicateInvocations() throws Exception {
        Object mock = mock(Object.class, new ReturnsDeepStubs());

        InvocationContainerImpl container = new InvocationContainerImpl(mock, new MockCreationSettings());
        StubbedInvocationMatcher stubbedInvocationMatcher = new StubbedInvocationMatcher(container, new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                throw new IllegalStateException("Duplicate invocation");
            }
        });
        container.addAnswer(stubbedInvocationMatcher, false);

        new ReturnsDeepStubs().answer(new InvocationOnMock(mock, Object.class, "toString", new Class[0], null, new Object[0]));
        new ReturnsDeepStubs().answer(new InvocationOnMock(mock, Object.class, "toString", new Class[0], null, new Object[0]));
    }

    @Test
    public void testRecordDeepStubMock() throws Exception {
        interface NestedGenerics<K extends Comparable<K> & Cloneable, V extends Number> extends Map<K, Set<V>> {
        }

        Object mock = mock(Object.class, new ReturnsDeepStubs());
        NestedGenerics<Object, Object> nestedGenericsMock = mock(NestedGenerics.class, new ReturnsDeepStubs());

        when(nestedGenericsMock.entrySet().iterator().next().getValue()).thenReturn(mock);
        when(mock.toString()).thenReturn("mock");

        Object deepStubReturn = new ReturnsDeepStubs().recordDeepStubMock(new InvocationOnMock(nestedGenericsMock, NestedGenerics.class, "entrySet", new Class[0], null, new Object[0]), new InvocationContainerImpl(mock, new MockCreationSettings()));
        assertNotNull(deepStubReturn);
        assertEquals("mock", deepStubReturn.toString());

        verify(nestedGenericsMock).entrySet();
    }

    private static class MethodTestClass {
        public String toString() {
            return "MethodTestClass";
        }
    }
}