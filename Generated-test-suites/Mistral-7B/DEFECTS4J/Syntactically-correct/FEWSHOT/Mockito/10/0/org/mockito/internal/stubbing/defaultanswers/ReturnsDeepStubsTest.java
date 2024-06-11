package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.DeeplyStubbedAnswer;
import org.mockito.internal.stubbing.InvocationContainerImpl;
import org.mockito.internal.stubbing.ReturnsDeepStubsSerializationFallback;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.invocation.InvocationOnMock;

import java.io.Serializable;

public class ReturnsDeepStubsTest {

    @Test
    public void testDeepStubs() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        Object deepStub = mock(Object.class, new ReturnsDeepStubs());

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[]{
                new StubbedInvocationMatcher(invocation, new DeeplyStubbedAnswer(deepStub))
        });

        when(mockitoCore().mock(Object.class)).thenReturn(mock);
        when(mockitoCore().newMock(Object.class, withSettings())).thenReturn(mock);

        Object result1 = deepStubsAnswer(mock, null);
        Object result2 = deepStubsAnswer(mock, container);

        assertSame(deepStub, result1);
        assertSame(mock, result2);
    }

    @Test
    public void testDeepStubsWithSerializableInterface() {
        Object mock = mock(Serializable.class, new ReturnsDeepStubs());
        Object deepStub = mock(Serializable.class, new ReturnsDeepStubs());

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[]{
                new StubbedInvocationMatcher(invocation, new DeeplyStubbedAnswer(deepStub))
        });

        when(mockitoCore().mock(Serializable.class)).thenReturn(mock);
        when(mockitoCore().newMock(Serializable.class, withSettings().serializable())).thenReturn(mock);

        Object result1 = deepStubsAnswer(mock, null);
        Object result2 = deepStubsAnswer(mock, container);

        assertSame(deepStub, result1);
        assertSame(mock, result2);
    }

    @Test
    public void testDeepStubsWithExtraInterfaces() {
        Object mock = mock(Interface1.class, new ReturnsDeepStubs());
        Object deepStub = mock(Interface1.class, new ReturnsDeepStubs());

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        when(container.getStubbedInvocations()).thenReturn(new StubbedInvocationMatcher[]{
                new StubbedInvocationMatcher(invocation, new DeeplyStubbedAnswer(deepStub))
        });

        when(mockitoCore().mock(Interface1.class)).thenReturn(mock);
        when(mockitoCore().newMock(Interface1.class, withSettings().extraInterfaces(new Class[0]))).thenReturn(mock);

        when(mock(Interface2.class)).thenReturn(mock);

        Object result = deepStubsAnswer(mock, container);
        assertSame(deepStub, result);
    }

    private Object deepStubsAnswer(Object mock, InvocationContainerImpl container) {
        try {
            return ((ReturnsDeepStubs) answer(mock)).answer(mock(InvocationOnMock.class));
        } catch (Throwable e) {
            if (container != null) {
                return container.getAnswerFor(new DeeplyStubbedAnswer(mock), true);
            }
            throw e;
        }
    }
}