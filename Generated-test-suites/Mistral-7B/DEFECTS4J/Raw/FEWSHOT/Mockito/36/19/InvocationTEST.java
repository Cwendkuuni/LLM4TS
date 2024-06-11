package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.realmethod.RealMethod;
import org.mockito.internal.matchers.*;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.util.*;
import org.mockito.invocation.InvocationOnMock;

public class InvocationTest {

    @Test
    public void testConstructor() {
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = mockMethod(String.class, "someMethod", new Class[0]);
        Object[] arguments = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, sequenceNumber, realMethod);

        assertEquals(mockitoMethod, invocation.getMethod());
        assertEquals(mock, invocation.getMock());
        assertArrayEquals(arguments, invocation.getArguments());
        assertEquals(sequenceNumber, invocation.getSequenceNumber());
        assertEquals(realMethod, invocation.realMethod);
    }

    @Test
    public void testExpandVarArgs() {
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = mockMethod(String.class, "someMethod", new Class[] { String[].class });
        Object[] args = new Object[] { new String[] { "foo", "bar" } };
        Object[] expectedArgs = new Object[] { "foo", "bar" };

        Object[] expandedArgs = Invocation.expandVarArgs(mockitoMethod.isVarArgs(), args);

        assertArrayEquals(expectedArgs, expandedArgs);
    }

    @Test
    public void testEquals() {
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = mockMethod(String.class, "someMethod", new Class[0]);
        Object[] arguments1 = new Object[] { "foo" };
        Object[] arguments2 = new Object[] { "bar" };

        Invocation invocation1 = new Invocation(mock, mockitoMethod, arguments1, 1, null);
        Invocation invocation2 = new Invocation(mock, mockitoMethod, arguments2, 1, null);

        assertFalse(invocation1.equals(invocation2));

        invocation2 = new Invocation(mock, mockitoMethod, arguments1, 1, null);

        assertTrue(invocation1.equals(invocation2));
    }

    @Test
    public void testToString() {
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = mockMethod(String.class, "someMethod", new Class[] { String.class, Integer.TYPE });
        Object[] arguments = new Object[] { "foo", 1 };

        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, null);

        String expected = String.format("%s(%s)", qualifiedMethodName(mockitoMethod), argumentsToMatchers(arguments).toString());
        String result = invocation.toString();

        assertEquals(expected, result);
    }

    @Test
    public void testToStringWithMultiline() {
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = mockMethod(String.class, "someMethod", new Class[] { String[].class });
        Object[] arguments = new Object[] { new String[] { "foo", "bar" } };

        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, null);

        String expected = String.format("%s(%s)", qualifiedMethodName(mockitoMethod), argumentsToMatchers(arguments).toString());
        String result = invocation.toString(new PrintSettings(true));

        assertEquals(expected, result);
    }

    @Test
    public void testIsToString() {
        MockitoMethod mockitoMethod = mockMethod(String.class, "toString", new Class[0]);

        assertTrue(Invocation.isToString(mockitoMethod));
    }

    @Test
    public void testIsValidException() {
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = mockMethod(String.class, "someMethod", new Class[] {});
        Throwable throwable = new RuntimeException();

        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, null);

        assertFalse(invocation.isValidException(throwable));

        mockitoMethod = mockMethod(String.class, "someMethod", new Class[] { Exception.class });

        assertTrue(invocation.isValidException(throwable));
    }

    @Test
    public void testIsValidReturnType() {
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = mockitoMethod(String.class, "someMethod", int.class);

        assertTrue(invocation.isValidReturnType(int.class));

        mockitoMethod = mockitoMethod(String.class, "someMethod", String[].class);

        assertTrue(invocation.isValidReturnType(String[].class));
    }

    @Test(expected = RuntimeException.class)
    public void testHashCode() {
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = mockMethod(String.class, "someMethod", new Class[0]);
        Object[] arguments = new Object[] { "foo" };

        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, null);

        invocation.hashCode();
    }

    private MockitoMethod mockitoMethod(Class returnType, String name, Class... parameterTypes) {
        return mock(MockitoMethod.class, new Class[] { MockitoMethod.class, Class[].class, Class[].class, String[].class }, new Object[] { null, parameterTypes, new String[] { name }, new String[0][] });
    }

    private MockitoMethod mockMethod(Class returnType, String name, Class[] parameterTypes) {
        return mock(MockitoMethod.class, new Class[] { MockitoMethod.class, Class[].class, Class[].class, String[].class, String[].class }, new Object[] { null, parameterTypes, new String[] { name }, new String[0][] });
    }

    private String qualifiedMethodName() {
        return new MockUtil().getMockName(new Object() {});
    }

    private List<Matcher> argumentsToMatchers(Object[] arguments) {
        List<Matcher> matchers = new ArrayList<>();
        for (Object arg : arguments) {
            if (arg != null) {
                matchers.add(new Equals(arg));
            } else {
                matchers.add(Matchers.nullValue());
            }
        }
        return matchers;
    }
}