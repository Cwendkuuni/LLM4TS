package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.io.Serializable;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class ReturnsDeepStubsTest {

    private ReturnsDeepStubs returnsDeepStubs;
    private InvocationHandler mockHandler;
    private InternalMockHandler<Object> internalMockHandler;
    private InvocationContainerImpl invocationContainer;

    @Before
    public void setup() {
        returnsDeepStubs = new ReturnsDeepStubs();
        mockHandler = createMockHandler();
        internalMockHandler = (InternalMockHandler<Object>) mockHandler;
        invocationContainer = new InvocationContainerImpl();
    }

    @Test
    public void testAnswerMethodWithNoArguments() throws Throwable {
        when(returnsDeepStubs.answer(any(InvocationOnMock.class))).thenReturn(mock(String.class));
        String result = (String) returnsDeepStubs.answer(mock(Object.class));
        assertEquals("", result);
        verify(returnsDeepStubs, times(1)).answer(any(InvocationOnMock.class));
    }

    @Test
    public void testAnswerMethodWithArguments() throws Throwable {
        when(returnsDeepStubs.answer(any(InvocationOnMock.class))).thenAnswer((Answer<Object>) invocation -> "result");
        Object[] arguments = new Object[]{"arg1", 1};
        Object result = returnsDeepStubs.answer(mock(Object.class), arguments);
        assertEquals("result", result);
        verify(returnsDeepStubs, times(1)).answer(eq(argument(equalTo(arguments))), any());
    }

    @Test
    public void testGetMockMethodWithNoArguments() throws Throwable {
        when(returnsDeepStubs.getMock(any(InvocationOnMock.class))).thenReturn(mock(String.class));
        String result = (String) returnsDeepStubs.getMock(mock(Object.class));
        assertEquals("", result);
        verify(returnsDeepStubs, times(1)).getMock(any(InvocationOnMock.class));
    }

    @Test
    public void testGetMockMethodWithArguments() throws Throwable {
        String returnType = String.class.getName();
        Method method = mock(Method.class);
        when(method.getName()).thenReturn("testMethod");
        when(method.getParameterTypes()).thenReturn(new Class[]{Integer.class});
        when(method.getReturnType()).thenReturn(returnType);
        Class<?> clazz = Proxy.getProxyClass(new Class[]{Object.class, Method.class}, new Class[]{ReturnsDeepStubs.class, Method.class});
        Object invocation = Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{Object.class, Method.class}, mockHandler);
        Object result = returnsDeepStubs.getMock(Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{Object.class}, mockHandler));
        assertEquals(clazz, result.getClass());
        assertEquals(invocation, result);
        verify(returnsDeepStubs, times(1)).getMock(any(InvocationOnMock.class));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetMockMethodWithException() throws Throwable {
        when(returnsDeepStubs.getMock(any(InvocationOnMock.class))).thenThrow(new UnsupportedOperationException());
        returnsDeepStubs.getMock(mock(Object.class));
    }

    @Test
    public void testRecordDeepStubMockMethod() throws Throwable {
        when(mock(String.class).getMockHandler()).thenReturn(mockHandler);
        Object mock = returnsDeepStubs.recordDeepStubMock(any(InvocationOnMock.class), any(InvocationContainerImpl.class));
        assertEquals("", mock.getClass().getName());
        verify(mockHandler, times(1)).addAnswer(any(Answer.class), anyBoolean());
    }

    @Test
    public void testActualParameterizedTypeMethodWithNullMock() throws Throwable {
        assertEquals(Map.class, returnsDeepStubs.actualParameterizedType(null));
    }

    private InvocationHandler createMockHandler() {
        return mock(InvocationHandler.class);
    }
}