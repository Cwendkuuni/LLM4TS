package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import static org.mockito.internal.matchers.ArgumentsMatchers.*;

import java.util.Arrays;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.realmethod.RealMethod;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.util.MockitoMethod;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.util.Primitives;
import org.mockito.invocation.InvocationOnMock;

public class InvocationTest {

    @Test
    public void testConstructor() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod(MockitoMethodType.METHOD, new Class[]{}, String.class);
        Object[] arguments = new Object[]{};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod(new Object[]{}, String.class);
        Invocation invocation = new Invocation(mock, method, arguments, sequenceNumber, realMethod);

        assertEquals(mock, invocation.getMock());
        assertEquals(method, invocation.getMethod());
        assertArrayEquals(arguments, invocation.getArguments());
        assertEquals(sequenceNumber, invocation.getSequenceNumber().intValue());
        assertEquals(realMethod, invocation.getRealMethod());
    }

    @Test
    public void testGetMock() {
        Object mock = new Object();
        Invocation invocation = new Invocation(mock, null, null, 0, null);
        assertEquals(mock, invocation.getMock());
    }

    @Test
    public void testGetMethod() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod(MockitoMethodType.METHOD, new Class[]{}, String.class);
        Invocation invocation = new Invocation(mock, method, null, 0, null);
        assertEquals(method, invocation.getMethod());
    }

    @Test
    public void testGetArguments() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod(MockitoMethodType.METHOD, new Class[]{String.class, int.class}, String.class);
        Object[] arguments = new Object[]{new String("foo"), 1};
        Invocation invocation = new Invocation(mock, method, arguments, 0, null);
        assertArrayEquals(arguments, invocation.getArguments());
    }

    @Test
    public void testIsVerified() {
        Invocation invocation = new Invocation(null, null, null, 0, null);
        assertFalse(invocation.isVerified());

        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }

    @Test
    public void testGetSequenceNumber() {
        Invocation invocation = new Invocation(null, null, null, 1, null);
        assertEquals(1, invocation.getSequenceNumber().intValue());
    }

    @Test
    public void testIsVerifiedInOrder() {
        Invocation invocation = new Invocation(null, null, null, 1, null);
        assertFalse(invocation.isVerifiedInOrder());

        invocation.markVerifiedInOrder();
        assertTrue(invocation.isVerifiedInOrder());
    }

    @Test
    public void testToString() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod(MockitoMethodType.METHOD, new Class[]{String.class, int.class}, String.class);
        Object[] arguments = new Object[]{new String("foo"), 1};
        Invocation invocation = new Invocation(mock, method, arguments, 0, null);

        String expected = String.format("%s(%s)%n", qualifiedMethodName(method), argumentsToMatchers(arguments).toString());
        assertEquals(expected, invocation.toString());
    }

    @Test
    public void testToStringWithPrintSettings() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod(MockitoMethodType.METHOD, new Class[]{String.class, int.class}, String.class);
        Object[] arguments = new Object[]{new String("foo"), 1};
        Invocation invocation = new Invocation(mock, method, arguments, 0, null);

        PrintSettings printSettings = new PrintSettings();
        String expected = String.format("%s(%s)%n", qualifiedMethodName(method), printSettings.getArgumentsBlock(argumentsToMatchers(arguments), printSettings).toString());
        assertEquals(expected, invocation.toString(printSettings));
    }

    @Test
    public void testEquals() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod(MockitoMethodType.METHOD, new Class[]{String.class, int.class}, String.class);
        Object[] arguments1 = new Object[]{new String("foo"), 1};
        Object[] arguments2 = new Object[]{new String("bar"), 1};
        Invocation invocation1 = new Invocation(mock, method, arguments1, 0, null);
        Invocation invocation2 = new Invocation(mock, method, arguments2, 0, null);

        assertFalse(invocation1.equals(null));
        assertFalse(invocation1.equals(new Object()));
        assertFalse(invocation1.equals(invocation2));
        assertTrue(invocation1.equals(invocation1));
    }

    @Test
    public void testHashCode() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod(MockitoMethodType.METHOD, new Class[]{String.class, int.class}, String.class);
        Object[] arguments = new Object[]{new String("foo"), 1};
        Invocation invocation1 = new Invocation(mock, method, arguments, 0, null);
        Invocation invocation2 = new Invocation(mock, method, arguments, 0, null);

        int hashCode1 = invocation1.hashCode();
        int hashCode2 = invocation2.hashCode();

        assertNotSame(hashCode1, hashCode2);
    }

    @Test
    public void testExpandVarArgs() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod(MockitoMethodType.METHOD, new Class[]{}, String.class, true);
        Object[] arguments = new Object[]{new Object[]{new String("foo"), new String("bar")}};

        Object[] expected = new Object[]{new String("foo"), new String("bar")};
        Object[] result = Invocation.expandVarArgs(method.isVarArgs(), arguments);
        assertArrayEquals(expected, result);
    }

    @Test
    public void testIsToString() {
        Invocation invocation = new Invocation(new Object() {
            @Override
            public String toString() {
                return "toString";
            }
        }, new MockitoMethod(MockitoMethodType.METHOD, new Class[]{}, String.class), null, 0, null);

        assertTrue(Invocation.isToString(invocation));
    }

    @Test
    public void testIsValidException() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod(MockitoMethodType.METHOD, new Class[]{}, String.class);

        Invocation invocation = new Invocation(mock, method, null, 0, null);

        assertFalse(invocation.isValidException(new IllegalArgumentException()));
        assertTrue(invocation.isValidException(new RuntimeException()));
    }

    @Test
    public void testIsValidReturnType() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod(MockitoMethodType.METHOD, new Class[]{String.class, int.class}, String.class);

        Invocation invocation = new Invocation(mock, method, null, 0, null);

        assertTrue(invocation.isValidReturnType(String.class));
        assertTrue(invocation.isValidReturnType(int.class));
        assertFalse(invocation.isValidReturnType(double.class));
    }

    @Test
    public void testIsVoid() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod(MockitoMethodType.METHOD, new Class[]{String.class, int.class}, Void.TYPE);

        Invocation invocation = new Invocation(mock, method, null, 0, null);

        assertTrue(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod(MockitoMethodType.METHOD, new Class[]{String.class, int.class}, String.class);

        Invocation invocation = new Invocation(mock, method, null, 0, null);

        assertEquals("String", invocation.printMethodReturnType());
    }

    @Test
    public void testToStringWithMultipleArguments() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod(MockitoMethodType.METHOD, new Class[]{String.class, int.class, String.class}, String.class);
        Object[] arguments = new Object[]{"foo", 1, "bar"};
        Invocation invocation = new Invocation(mock, method, arguments, 0, null);

        String expected = String.format("%s(%s, %s, %s)%n", qualifiedMethodName(method), argumentsToMatchers(arguments[0]), argumentsToMatchers(arguments[1]), argumentsToMatchers(arguments[2]).toString());
        assertEquals(expected, invocation.toString());
    }

    @Test
    public void testToStringWithPrintSettingsAndMultipleArguments() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod(MockitoMethodType.METHOD, new Class[]{String.class, int.class, String.class}, String.class);
        Object[] arguments = new Object[]{"foo", 1, "bar"};
        Invocation invocation = new Invocation(mock, method, arguments, 0, null);

        PrintSettings printSettings = new PrintSettings();
        String expected = String.format("%s(%s)\n%s%s%n", qualifiedMethodName(method), printSettings.getArgumentsLine(argumentsToMatchers(arguments[0]), printSettings), "\n", printSettings.getArgumentsBlock(Arrays.asList(argumentsToMatchers(arguments[1]), argumentsToMatchers(arguments[2])), printSettings).toString());
        assertEquals(expected, invocation.toString(printSettings));
    }

    @Test
    public void testCallRealMethod() throws Exception {
        Object mock = new Object() {
            public String foo() {
                return "foo";
            }
        };
        MockitoMethod method = new MockitoMethod(MockitoMethodType.METHOD, new Class[]{String.class}, String.class);
        Invocation invocation = new Invocation(mock, method, null, 0, null);

        String result = (String) invocation.callRealMethod();
        assertEquals("foo", result);
    }

    private String qualifiedMethodName(MockitoMethod method) {
        return MockUtil.getMockName(mock) + "." + method.getName();
    }

    private List<Matcher> argumentsToMatchers(Object[] arguments) {
        List<Matcher> matchers = new ArrayList<>();
        for (Object arg : arguments) {
            if (arg != null && arg.getClass().isArray()) {
                matchers.add(new ArrayEquals((Object[]) arg));
            } else {
                matchers.add(new Equals(arg));
            }
        }
        return matchers;
    }
}