package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsDeepStubsTest {
    private ReturnsEmptyValues delegate = mock(ReturnsEmptyValues.class);
    private InvocationOnMock invocation = mock(InvocationOnMock.class);
    private GenericMetadataSupport returnTypeGenericMetadata = mock(GenericMetadataSupport.class);
    private InternalMockHandler<Object> handler = mock(InternalMockHandler.class);
    private InvocationContainerImpl container = mock(InvocationContainerImpl.class);

    private ReturnsDeepStubs returnsDeepStubs = new ReturnsDeepStubs();

    @Test
    public void testAnswer_nonMockable() throws Throwable {
        Class<?> rawType = Object.class;
        when(invocation.getMock()).thenReturn(rawType);
        when(delegate.returnValueFor(rawType)).thenReturn(null);

        Object result = returnsDeepStubs.answer(invocation);

        assertNull(result);
    }

    @Test
    public void testAnswer_mockable() throws Throwable {
        Class<?> rawType = String.class;
        when(invocation.getMock()).thenReturn(rawType);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getInvocationForStubbing()).thenReturn(invocation);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn("mocked");

        Object result = returnsDeepStubs.answer(invocation);

        assertEquals("mocked", result);
    }

    @Test
    public void testGetMock() throws Throwable {
        when(invocation.getMock()).thenReturn(mock);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.getInvocationForStubbing()).thenReturn(invocation);
        when(container.getStubbedInvocations()).thenReturn(Collections.singletonList(stubbedInvocationMatcher));
        when(stubbedInvocationMatcher.getInvocation()).thenReturn(invocation);
        when(invocation.matches(invocation)).thenReturn(true);
        when(stubbedInvocationMatcher.answer(invocation)).thenReturn("mocked");

        Object result = returnsDeepStubs.getMock(invocation);

        assertEquals("mocked", result);
    }

    @Test
    public void testRecordDeepStubMock() throws Throwable {
        Class<?> clz = String.class;
        when(invocation.getMethod()).thenReturn(method);
        when(method.getReturnType()).thenReturn(clz);
        when(handler.getInvocationContainer()).thenReturn(container);
        when(container.addAnswer(answer, false)).thenReturn(null);
        when(answer.answer(invocation)).thenReturn("mocked");

        Object result = returnsDeepStubs.recordDeepStubMock(invocation, container);

        assertEquals("mocked", result);
    }

    @Test
    public void testActualParameterizedType() {
        Object mock = mock(Object.class);
        when(handler.getMockSettings()).thenReturn(mockSettings);
        when(mockSettings.getTypeToMock()).thenReturn(mock);
        when(GenericMetadataSupport.inferFrom(mock)).thenReturn(returnTypeGenericMetadata);

        GenericMetadataSupport result = returnsDeepStubs.actualParameterizedType(mock);

        assertEquals(returnTypeGenericMetadata, result);
    }
}