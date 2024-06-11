package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswerWithEmptyValuesIfNotMockableType() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock);
        Type returnType = null; // use null as a test case for not mockable type

        Object result = new ReturnsDeepStubs().answer(invocation);
        assertNotNull(result);

        // Verify that the delegate method was called and returned an empty value
        verify(new ReturnsEmptyValues()).returnValueFor(returnType.getTypeName());
    }

    @Test
    public void testGetMockWhenInvocationMatchesStubbedInvocation() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock);

        InvocationContainerImpl container = mock(InvocationContainerImpl.class);
        StubbedInvocationMatcher stubbedInvocationMatcher = mock(StubbedInvocationMatcher.class);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn(new Object());
        when(container.getStubbedInvocations()).thenReturn(Arrays.asList(stubbedInvocationMatcher));

        when(new MockUtil().getMockHandler(mock)).thenReturn(new MockitoInternalMockHandler(container));

        Object result = new ReturnsDeepStubs().answer(invocation);
        assertNotNull(result);

        // Verify that the deepStubMock method was called and returned the stubbed value
        verify(container).addAnswer(argThat(answer -> answer.getClass().isInstance(stubbedInvocationMatcher.answer(invocation))), eq(true));
    }

    @Test
    public void testGetMockWhenInvocationDoesNotMatchAnyStubbedInvocation() {
        Object mock = mock(Object.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock);

        InvocationContainerImpl container = mock(InvocationContainerImpl.class);

        when(new MockUtil().getMockHandler(mock)).thenReturn(new MockitoInternalMockHandler(container));

        Object result = new ReturnsDeepStubs().answer(invocation);
        assertNotNull(result);

        // Verify that the recordDeepStubMock method was called and returned a new mock
        verify(container).addAnswer(argThat(answer -> answer.getClass().isInstance(new ReturnsDeepStubs())), eq(true));
        verify(container).addAnswer(any(Answer.class), eq(false));
    }

    @Test
    public void testGetMockWithGenericTypes() {
        interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
        }

        GenericsNest<Number> mock = mock(GenericsNest.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock);

        when(new MockUtil().getMockHandler(mock)).thenReturn(new MockitoInternalMockHandler(mock));

        Object result = new ReturnsDeepStubs().answer(invocation);
        assertNotNull(result);

        // Verify that the recordDeepStubMock method was called and returned a new mock of the generic type
        verify(mock, atLeastOnce()).equals(result);
    }

    @Test
    public void testAnswerWithEmptyValuesForNestedGenericTypes() {
        interface GenericsNest<K extends Comparable<K> & Cloneable> extends Map<K, Set<Number>> {
        }

        GenericsNest<Map<String, Number>> mock = mock(GenericsNest.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMock()).thenReturn(mock);

        Type returnType = Arrays.asList(Map.class, String.class, Number.class).getTypeParameters()[0];

        Object result = new ReturnsDeepStubs().answer(invocation);
        assertNotNull(result);

        // Verify that the delegate method was called and returned an empty value for the nested generic type
        verify(new ReturnsEmptyValues()).returnValueFor(returnType.getTypeName());
    }
}