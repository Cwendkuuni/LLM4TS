package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.internal.creation.settings.CreationSettings;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.InvocationOnMock;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.InvocationContainer;
import org.mockito.internal.stubbing.StubbedInvocationMatcher;
import org.mockito.internal.util.MockCreationValidator;
import org.mockito.internal.util.reflection.GenericMetadataSupport;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsDeepStubsTest {

    @Test
    public void testAnswer_ReturnsSameInstance() throws Exception {
        Object mock = mock(Map.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(newMethodWithReturnType(Map.Entry.class, Map.class, "entrySet", new Class[0]));
        when(invocation.getMock()).thenReturn(mock);

        Object answer = new ReturnsDeepStubs().answer(invocation);
        assertNotNull(answer);
        verify(mock).answer(invocation);
    }

    @Test
    public void testAnswer_ReturnsNewInstance() throws Exception {
        Object mock1 = mock(Map.class, new ReturnsDeepStubs());
        Object mock2 = mock(Map.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(newMethodWithReturnType(Map.Entry.class, Map.class, "entrySet", new Class[0]));
        when(invocation.getMock()).thenReturn(mock1);

        Object answer1 = new ReturnsDeepStubs().answer(invocation);
        assertNotNull(answer1);
        Object answer2 = new ReturnsDeepStubs().answer(invocation);
        assertNotNull(answer2);
        assertNotSame(answer1, answer2);
    }

    @Test
    public void testGetMock_ReturnsSameInstance() throws Exception {
        Object mock = mock(Map.class, new ReturnsDeepStubs());
        InvocationOnMock invocation1 = Mockito.mock(InvocationOnMock.class);
        when(invocation1.getMethod()).thenReturn(newMethodWithReturnType(Map.Entry.class, Map.class, "entrySet", new Class[0]));
        when(invocation1.getMock()).thenReturn(mock);

        InvocationOnMock invocation2 = Mockito.mock(InvocationOnMock.class);
        when(invocation2.getMethod()).thenReturn(newMethodWithReturnType(Map.Entry.class, Map.class, "entrySet", new Class[0]));
        when(invocation2.getMock()).thenReturn(mock);

        Object result1 = new ReturnsDeepStubs().getMock(invocation1);
        Object result2 = new ReturnsDeepStubs().getMock(invocation2);
        assertSame(result1, result2);
    }

    @Test
    public void testGetMock_ReturnsNewInstance() throws Exception {
        Object mock1 = mock(Map.class, new ReturnsDeepStubs());
        Object mock2 = mock(Map.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(newMethodWithReturnType(Map.Entry.class, Map.class, "entrySet", new Class[0]));
        when(invocation.getMock()).thenReturn(mock1);

        InvocationContainer invocationContainer = Mockito.mock(InvocationContainer.class);
        when(invocationContainer.getInvocationForStubbing()).thenReturn(invocation);
        when(invocationContainer.getStubbedInvocations()).thenReturn(Arrays.asList(Mockito.mock(StubbedInvocationMatcher.class)));

        Object result1 = new ReturnsDeepStubs().getMock(invocation);
        Object result2 = new ReturnsDeepStubs().getMock(invocation);
        assertNotSame(result1, result2);
    }

    @Test
    public void testRecordDeepStubMock_ReturnsSameInstance() throws Exception {
        Object mock = mock(Map.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(newMethodWithReturnType(Map.Entry.class, Map.class, "entrySet", new Class[0]));
        when(invocation.getMock()).thenReturn(mock);

        InvocationContainer invocationContainer = Mockito.mock(InvocationContainer.class);
        when(invocationContainer.getInvocationForStubbing()).thenReturn(invocation);

        Object stubbedMock = new ReturnsDeepStubs().recordDeepStubMock(invocation, invocationContainer);
        Object stubbedMock2 = new ReturnsDeepStubs().recordDeepStubMock(invocation, invocationContainer);
        assertSame(stubbedMock, stubbedMock2);
    }

    @Test
    public void testRecordDeepStubMock_ReturnsNewInstance() throws Exception {
        Object mock1 = mock(Map.class, new ReturnsDeepStubs());
        Object mock2 = mock(Map.class, new ReturnsDeepStubs());
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(newMethodWithReturnType(Map.Entry.class, Map.class, "entrySet", new Class[0]));
        when(invocation.getMock()).thenReturn(mock1);

        InvocationContainer invocationContainer = Mockito.mock(InvocationContainer.class);
        when(invocationContainer.getInvocationForStubbing()).thenReturn(invocation);

        Object stubbedMock1 = new ReturnsDeepStubs().recordDeepStubMock(invocation, invocationContainer);
        Object stubbedMock2 = new ReturnsDeepStubs().recordDeepStubMock(invocation, invocationContainer);
        assertNotSame(stubbedMock1, stubbedMock2);
    }

    @Test
    public void testGetMock_ReturnsNewInstanceWhenInvocationIsDifferent() throws Exception {
        Object mock1 = mock(Map.class, new ReturnsDeepStubs());
        Object mock2 = mock(Map.class, new ReturnsDeepStubs());
        InvocationOnMock invocation1 = Mockito.mock(InvocationOnMock.class);
        when(invocation1.getMethod()).thenReturn(newMethodWithReturnType(Map.Entry.class, Map.class, "entrySet", new Class[0]));
        when(invocation1.getMock()).thenReturn(mock1);

        InvocationOnMock invocation2 = Mockito.mock(InvocationOnMock.class);
        when(invocation2.getMethod()).thenReturn(newMethodWithReturnType(Map.class, Map.class, "size", new Class[0]));
        when(invocation2.getMock()).thenReturn(mock2);

        InvocationContainer invocationContainer = Mockito.mock(InvocationContainer.class);
        when(invocationContainer.getInvocationForStubbing()).thenReturn(invocation1);

        Object result1 = new ReturnsDeepStubs().getMock(invocation1);
        Object result2 = new ReturnsDeepStubs().getMock(invocation2);
        assertNotSame(result1, result2);
    }

    private Method newMethodWithReturnType(Class<?> returnType, Class<?>... parameterTypes) throws NoSuchMethodException {
        return Mockito.mock(Method.class, new Object[] { returnType, Mockito.mock(Class[].class, new Object[] { parameterTypes }), "methodName", new Object[0] });
    }

    private Object newInstance(Class<?> clazz) throws InstantiationException, IllegalAccessException {
        return clazz.newInstance();
    }

    @Test(expected = IllegalStateException.class)
    public void testAnswer_ThrowsExceptionWhenTypeCannotBeMocked() throws Exception {
        Class<?> clazz = Object.class;
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(newMethodWithReturnType(clazz, new Class[0]));
        when(invocation.getMock()).thenReturn(mock(clazz));

        new ReturnsDeepStubs().answer(invocation);
    }
}