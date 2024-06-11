package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

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
import org.mockito.stubbing.AnswerWithStub;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswerDeepStub() {
        InterfaceA mockA = mock(InterfaceA.class, new ReturnsDeepStubs());
        InterfaceB mockB = mockA.getB();
        mockB.doSomething();

        verify(mockB).doSomething();
    }

    @Test
    public void testAnswerDeepStubWithNestedGenerics() {
        InterfaceGeneric<InterfaceNested<String>> mock = mock(InterfaceGeneric.class, new ReturnsDeepStubs());
        InterfaceNested<Integer> nested = mock.getNested();

        assertEquals("nested", nested.getValue());
    }

    @Test
    public void testAnswerDeepStubWithNestedGenericsAndMethods() {
        InterfaceGeneric<InterfaceNested<Integer>> mock = mock(InterfaceGeneric.class, new ReturnsDeepStubs());
        InterfaceNested<String> nested = mock.getNested();
        String value = nested.getValue();

        assertEquals("nested value", value);
        assertEquals(1, nested.getIndex());

        verify(nested).doSomething();
    }

    @Test
    public void testAnswerDeepStubWithNestedGenericsAndMethodsWithSpy() {
        InterfaceGeneric<InterfaceNested<Integer>> mock = mock(InterfaceGeneric.class, new ReturnsDeepStubs());
        InterfaceNested<String> nested = mock.getNested();
        InterfaceNested<String> spiedNested = spy(nested);

        String value = spiedNested.getValue();

        assertEquals("nested value", value);
        assertEquals(1, spiedNested.getIndex());

        verify(nested).doSomething();
    }

    @Test
    public void testAnswerDeepStubWithNestedGenericsAndMethodsWithAnswer() {
        InterfaceGeneric<InterfaceNested<Integer>> mock = mock(InterfaceGeneric.class, new ReturnsDeepStubs());
        InterfaceNested<String> nested = mock.getNested();
        AnswerWithStub<String> answer = AnswerWithStub.callRealAnswer();

        when(nested.getValue()).thenReturn(answer.getReturnValue());

        String value = nested.getValue();

        assertEquals("mocked value", value);
    }

    @Test(expected = IllegalStateException.class)
    public void testAnswerDeepStubWithNonMockableType() {
        mock(String.class, new ReturnsDeepStubs());
    }

    @Test
    public void testGetMock() {
        InterfaceA mockA = mock(InterfaceA.class, new ReturnsDeepStubs());
        InterfaceB mockB = mockA.getB();

        InvocationContainerImpl containerA = new MockUtil().getMockHandler(mockA).getInvocationContainer();
        InvocationContainerImpl containerB = new MockUtil().getMockHandler(mockB).getInvocationContainer();

        for (StubbedInvocationMatcher stubbedInvocationMatcher : containerB.getStubbedInvocations()) {
            if (stubbedInvocationMatcher.getInvocation().getTarget() == mockA) {
                Object mock = stubbedInvocationMatcher.answer(new InvocationOnMock(stubbedInvocationMatcher.getInvocation()));
                assertEquals(mockB, mock);
                break;
            }
        }

        assertNull(containerA.getInvocationForStubbing());
    }

    @Test
    public void testRecordDeepStubMock() {
        InterfaceA mockA = mock(InterfaceA.class, new ReturnsDeepStubs());
        InterfaceB mockB = mockA.getB();

        InvocationContainerImpl containerB = new MockUtil().getMockHandler(mockB).getInvocationContainer();

        for (StubbedInvocationMatcher stubbedInvocationMatcher : containerB.getStubbedInvocations()) {
            if (stubbedInvocationMatcher.getInvocation().getTarget() == mockA) {
                Object mock = stubbedInvocationMatcher.answer(new InvocationOnMock(stubbedInvocationMatcher.getInvocation()));
                assertEquals(mockB, mock);
                break;
            }
        }

        GenericMetadataSupport returnTypeGenericMetadata = new ReturnsDeepStubs().actualParameterizedType(mockB);
        Class<?> rawType = returnTypeGenericMetadata.rawType();

        assertThat(rawType, is(InterfaceB.class));

        verify(mockB).doSomething();
    }

    public static interface InterfaceA {
        InterfaceB getB();
    }

    public static interface InterfaceB {
        void doSomething();

        InterfaceNested<String> getNested();

        int getIndex();
    }

    public static interface InterfaceGeneric<T> {
        T getNested();
    }

    public static interface InterfaceNested<T> {
        T getValue();
    }
}

class AnswerWithStub implements Answer<String> {
    private Object returnValue;

    public String answer(InvocationOnMock invocation) throws Throwable {
        return (String) returnValue;
    }

    public Object getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }
}