package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
import org.mockito.stubbing.AnswerWithMock;

public class ReturnsDeepStubsTest {

    @Test
    public void testGetMock() {
        Class<?> clz = MockitoMockitoHelper.classWithGenericType(List.class, Collections.class, List.class);
        Object mock = mock(clz, new ReturnsDeepStubs());
        InvocationOnMock invocation = MockitoMockitoHelper.invocationOnMock(mock, "size");

        InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock);
        InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();

        Object deepStub = ReturnsDeepStubsTest.getDeepStub(container, invocation);
        assertNotNull(deepStub);
        assertSame(mock, deepStub);
    }

    @Test
    public void testRecordDeepStubMock() {
        Class<?> clz = MockitoMockitoHelper.classWithGenericType(List.class, Collections.class, List.class);
        Object mock = mock(clz, new ReturnsDeepStubs());
        Object deepStub = mock(Number.class);

        InvocationOnMock invocation = MockitoMockitoHelper.invocationOnMock(mock, "size");
        InvocationContainerImpl container = (InvocationContainerImpl) new MockUtil().getMockHandler(mock).getInvocationContainer();

        when(container.getInvocationForStubbing()).thenReturn(invocation);
        when(container.getStubbedInvocations()).thenReturn(Arrays.asList(new StubbedInvocationMatcher(invocation, deepStub)));

        Object result = ReturnsDeepStubsTest.getDeepStub(container, invocation);
        assertSame(deepStub, result);
    }

    @Test
    public void testAnswerMethodReturnsMockIfReturnTypeIsMockable() {
        Class<?> clz = MockitoMockitoHelper.classWithGenericType(List.class, Collections.class, List.class);
        Object mock = mock(clz, new ReturnsDeepStubs());

        InvocationOnMock invocation = MockitoMockitoHelper.invocationOnMock(mock, "size");
        InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock);
        InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();

        Object deepStub = ReturnsDeepStubsTest.getDeepStub(container, invocation);
        assertNotNull(deepStub);
        assertSame(mock, deepStub);
    }

    @Test
    public void testAnswerMethodReturnsEmptyValuesIfReturnTypeIsNotMockable() {
        Class<?> clz = String.class;
        Object mock = mock(clz, new ReturnsDeepStubs());

        InvocationOnMock invocation = MockitoMockitoHelper.invocationOnMock(mock, "toString");
        InternalMockHandler<Object> handler = new MockUtil().getMockHandler(mock);
        InvocationContainerImpl container = (InvocationContainerImpl) handler.getInvocationContainer();

        Object emptyValue = new ReturnsEmptyValues().returnValueFor(clz);
        Object deepStub = ReturnsDeepStubsTest.getDeepStub(container, invocation);
        assertSame(emptyValue, deepStub);
    }

    private static Object getDeepStub(InvocationContainerImpl container, InvocationOnMock invocation) {
        return ReturnsDeepStubs.getMock(invocation);
    }

    static class MockitoMockitoHelper {
        static Class<?> classWithGenericType(Class<?>... typeArguments) {
            // ...
        }

        static InvocationOnMock invocationOnMock(Object mock, String methodName) {
            // ...
        }
    }
}

class ReturnsEmptyValues implements Answer<Object> {
    // ...
}