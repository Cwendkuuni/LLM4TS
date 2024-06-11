package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;

import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.realmethod.RealMethod;
import org.mockito.internal.matchers.*;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.internal.util.*;
import org.mockito.invocation.InvocationOnMock;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InvocationTest {
    private Invocation invocation;
    private Object mock;
    private MockitoMethod method;
    private Object[] arguments;
    private Object[] rawArguments;
    private int sequenceNumber;
    private RealMethod realMethod;
    
    @Before
    public void setup() {
        mock = mock(Object.class);
        Method mockitoMethod = Object.class.getMethod("equals", Object.class);
        method = new MockitoMethod(mockitoMethod);
        arguments = new Object[] {"arg1", "arg2"};
        rawArguments = arguments;
        sequenceNumber = 1;
        realMethod = new RealMethod() {
            @Override
            public Object invoke(Object target, Object[] arguments) throws Throwable {
                return null;
            }
        };
        invocation = new Invocation(mock, method, arguments, sequenceNumber, realMethod);
    }
    
    @Test
    public void testGetMock() {
        assertEquals(mock, invocation.getMock());
    }

    @Test
    public void testGetMethod() {
        assertEquals(method, invocation.getMethod());
    }

    @Test
    public void testGetArguments() {
        assertEquals(arguments, invocation.getArguments());
    }

    @Test
    public void testIsVerified() {
        assertFalse(invocation.isVerified());
    }

    @Test
    public void testGetSequenceNumber() {
        assertEquals(sequenceNumber, invocation.getSequenceNumber().intValue());
    }

    @Test
    public void testIsVerifiedInOrder() {
        assertFalse(invocation.isVerifiedInOrder());
    }

    @Test
    public void testEquals() {
        Invocation other = new Invocation(mock, method, arguments, sequenceNumber, realMethod);
        assertTrue(invocation.equals(other));
    }

    @Test
    public void testHashCode() {
        assertThrows(RuntimeException.class, () -> {
            invocation.hashCode();
        });
    }

    @Test
    public void testToString() {
        String expected = mock.getClass().getName() + "." + method.getName() + "arg1, arg2";
        assertEquals(expected, invocation.toString());
    }

    @Test
    public void testToStringWithMatchers() {
        Matcher<Object> arg1Matcher = sameInstance("arg1");
        Matcher<Object> arg2Matcher = sameInstance("arg2");
        List<Matcher> matchers = Arrays.asList(arg1Matcher, arg2Matcher);
        String expected = mock.getClass().getName() + "." + method.getName() + "arg1, arg2";
        assertEquals(expected, invocation.toString(matchers, new PrintSettings()));
    }

    @Test
    public void testIsToString() {
        InvocationOnMock invocationOnMock = mock(InvocationOnMock.class);
        when(invocationOnMock.getMethod()).thenReturn(new MockitoMethod(Object.class.getMethod("toString")));
        assertTrue(Invocation.isToString(invocationOnMock));
    }

    @Test
    public void testIsValidException() {
        Throwable throwable = new Throwable();
        assertTrue(invocation.isValidException(throwable));
    }

    @Test
    public void testIsValidReturnType() {
        Class<?> clazz = String.class;
        assertTrue(invocation.isValidReturnType(clazz));
    }

    @Test
    public void testIsVoid() {
        assertFalse(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        String expected = method.getReturnType().getSimpleName();
        assertEquals(expected, invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        String expected = method.getName();
        assertEquals(expected, invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        assertFalse(invocation.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        assertNotNull(invocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        assertEquals(arguments.length, invocation.getArgumentsCount());
    }

    @Test
    public void testGetRawArguments() {
        assertEquals(rawArguments, invocation.getRawArguments());
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        assertNull(invocation.callRealMethod());
    }
}