package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import static org.mockito.internal.matchers.ArgumentsMatchers.*;
import static org.mockito.internal.matchers.MockitoTypeMatchers.*;

import java.util.Arrays;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoBaseException;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.realmethod.RealMethod;
import org.mockito.internal.matchers.*;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class InvocationTest {

    @Test
    public void testConstructor() {
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object mock = mock(Object.class);
        Object[] arguments = new Object[] { "foo" };
        RealMethod realMethod = new RealMethod() {
            @Override
            public Object invoke(Object proxy, Object[] args) throws Throwable {
                throw new MockitoBaseException("Mockito exception");
            }
        };

        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, realMethod);
        assertEquals(mockitoMethod, invocation.getMethod());
        assertEquals(mock, invocation.getMock());
        assertEquals(1, invocation.getSequenceNumber());
        assertEquals(realMethod, invocation.realMethod);
        assertArrayEquals(arguments, invocation.getArguments());
        assertNull(invocation.location);
        assertFalse(invocation.isVerified());
        assertFalse(invocation.isVerifiedInOrder());
    }

    @Test
    public void testGetMock() {
        Object mock = "mock";
        Invocation invocation = new Invocation(mock, null, null, 0, null);
        assertEquals(mock, invocation.getMock());
    }

    @Test
    public void testGetMethod() {
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Invocation invocation = new Invocation(null, mockitoMethod, null, 0, null);
        assertEquals(mockitoMethod, invocation.getMethod());
    }

    @Test
    public void testGetArguments() {
        Object[] arguments = new Object[] { "foo" };
        Invocation invocation = new Invocation(null, null, arguments, 0, null);
        assertArrayEquals(arguments, invocation.getArguments());
    }

    @Test
    public void testIsVerified() {
        Invocation invocation = new Invocation(null, null, null, 0, null);
        assertFalse(invocation.isVerified());

        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testHashCode() {
        Invocation invocation = new Invocation(null, null, null, 0, null);
        invocation.hashCode();
    }

    @Test
    public void testToString() {
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object[] arguments = new Object[] { "foo" };
        Invocation invocation = new Invocation(null, mockitoMethod, arguments, 0, null);

        String expected = "Invocation on mock [mock] with method [public java.lang.String org.mockito.internal.invocation.MockitoMethod0 testMethod(java.lang.String)] "
                + "with arguments [foo]";

        String toString = invocation.toString();
        assertEquals(expected, toString);
    }

    @Test
    public void testToStringWithPrintSettings() {
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object[] arguments = new Object[] { "foo" };
        Invocation invocation = new Invocation(null, mockitoMethod, arguments, 0, null);

        PrintSettings printSettings = new PrintSettings();
        printSettings.setMultiline(true);
        String toString = invocation.toString(printSettings);
        String expected = "Invocation on mock [mock]\n"
                + "with method [public java.lang.String org.mockito.internal.invocation.MockitoMethod0 testMethod(java.lang.String)]\n"
                + "with arguments [\n" + "    foo\n" + "]\n";
        assertEquals(expected, toString);
    }

    @Test
    public void testToStringWithArgumentsToMatchers() {
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object[] arguments = new Object[] { "foo" };
        Invocation invocation = new Invocation(null, mockitoMethod, arguments, 0, null);

        List<Matcher> matchers = invocation.argumentsToMatchers();
        assertEquals(1, matchers.size());
        assertEquals(argThat(isInstanceOf(String.class)), matchers.get(0));

        String toString = invocation.toString(matchers, new PrintSettings());
        String expected = "Invocation on mock [mock]\n"
                + "with method [public java.lang.String org.mockito.internal.invocation.MockitoMethod0 testMethod(java.lang.String)]\n"
                + "with arguments [\n" + "    <String>\n" + "]\n";
        assertEquals(expected, toString);
    }

    @Test
    public void testToStringWithPrintSettingsAndArgumentsToMatchers() {
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object[] arguments = new Object[] { "foo" };
        Invocation invocation = new Invocation(null, mockitoMethod, arguments, 0, null);

        List<Matcher> matchers = Arrays.asList(argThat(isInstanceOf(String.class)), argThat(isNull()));
        String toString = invocation.toString(matchers, new PrintSettings());
        String expected = "Invocation on mock [mock]\n"
                + "with method [public java.lang.String org.mockito.internal.invocation.MockitoMethod0 testMethod(java.lang.String, java.lang.Object)]\n"
                + "with arguments [\n" + "    <String>\n" + ",\n" + "    null\n" + "]\n";
        assertEquals(expected, toString);
    }

    @Test
    public void testArgumentsToMatchers() {
        Object[] arguments = new Object[] { "foo" };
        Invocation invocation = new Invocation(null, null, arguments, 0, null);
        List<Matcher> argumentsToMatchers = invocation.argumentsToMatchers();
        assertEquals(1, argumentsToMatchers.size());
        assertEquals(argThat(isInstanceOf(String.class)), argumentsToMatchers.get(0));
    }

    @Test
    public void testInvokeRealMethodWithNoArguments() throws Throwable {
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object mock = mock(Object.class);
        RealMethod realMethod = new RealMethod() {
            @Override
            public Object invoke(Object proxy, Object[] args) throws Throwable {
                return "result";
            }
        };

        Invocation invocation = new Invocation(mock, mockitoMethod, null, 0, realMethod);
        Object result = invocation.callRealMethod();
        assertEquals("result", result);
    }

    @Test
    public void testInvokeRealMethodWithArguments() throws Throwable {
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object mock = mock(Object.class);
        Object[] arguments = new Object[] { "foo" };
        RealMethod realMethod = new RealMethod() {
            @Override
            public Object invoke(Object proxy, Object[] args) throws Throwable {
                return args[0].toString();
            }
        };

        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 0, realMethod);
        Object result = invocation.callRealMethod();
        assertEquals("foo", result);
    }

    @Test
    public void testToStringWithArgumentsAndAnswer() {
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object[] arguments = new Object[] { "foo" };
        Object answer = "answer";
        Invocation invocation = new Invocation(null, mockitoMethod, arguments, 0, null);

        Answer answerMock = answer("answer");
        when(mockitoMethod.getName()).thenReturn("getName");
        when(mockitoMethod.getReturnType()).thenReturn(String.class);

        doAnswer(answerMock).when(invocation).toString();
        String toString = invocation.toString();
        assertEquals("answer", toString);
    }

    @Test
    public void testToStringWithArgumentsAndAnswerAndPrintSettings() {
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object[] arguments = new Object[] { "foo" };
        Object answer = "answer";
        Invocation invocation = new Invocation(null, mockitoMethod, arguments, 0, null);

        Answer answerMock = answer("answer");
        when(mockitoMethod.getName()).thenReturn("getName");
        when(mockitoMethod.getReturnType()).thenReturn(String.class);
        PrintSettings printSettings = new PrintSettings();
        printSettings.setPrintStacktrace(true);

        doAnswer(answerMock).when(invocation).toString(printSettings);
        String toString = invocation.toString(printSettings);
        assertEquals("answer", toString);
    }

    @Test
    public void testMarkVerified() {
        Invocation invocation = new Invocation(null, null, null, 0, null);
        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }

    @Test
    public void testMarkVerifiedInOrder() {
        Invocation invocation = new Invocation(null, null, null, 0, null);
        invocation.markVerifiedInOrder();
        assertTrue(invocation.isVerified());
        assertTrue(invocation.isVerifiedInOrder());
    }

    @Test
    public void testIsValidException() {
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Invocation invocation = new Invocation(null, mockitoMethod, null, 0, null);
        assertFalse(invocation.isValidException(new IllegalArgumentException()));
        assertTrue(invocation.isValidException(new NullPointerException()));
    }

    @Test
    public void testIsValidReturnType() {
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Invocation invocation = new Invocation(null, mockitoMethod, null, 0, null);
        assertTrue(invocation.isValidReturnType(int.class));
        assertTrue(invocation.isValidReturnType(String.class));
        assertFalse(invocation.isValidReturnType(Object.class));
    }

    @Test
    public void testIsVoid() {
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Invocation invocation = new Invocation(null, mockitoMethod, null, 0, null);
        assertFalse(invocation.isVoid());

        MockitoMethod mockitoMethodVoid = mock(MockitoMethod.class);
        Invocation invocationVoid = new Invocation(null, mockitoMethodVoid, null, 0, null);
        assertTrue(invocationVoid.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Invocation invocation = new Invocation(null, mockitoMethod, null, 0, null);
        assertEquals("Object", invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Invocation invocation = new Invocation(null, mockitoMethod, null, 0, null);
        assertEquals("getName", invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Invocation invocation = new Invocation(null, mockitoMethod, null, 0, null);
        assertFalse(invocation.returnsPrimitive());

        MockitoMethod mockitoMethodBoolean = mock(MockitoMethod.class);
        Invocation invocationBoolean = new Invocation(null, mockitoMethodBoolean, null, 0, null);
        assertTrue(invocationBoolean.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation(null, null, null, 0, null);
        assertNull(invocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object[] arguments = new Object[] { "foo" };
        Invocation invocation = new Invocation(null, mockitoMethod, arguments, 0, null);
        assertEquals(1, invocation.getArgumentsCount());
    }

    @Test
    public void testGetRawArguments() {
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object[] arguments = new Object[] { "foo" };
        Invocation invocation = new Invocation(null, mockitoMethod, arguments, 0, null);
        assertArrayEquals(arguments, invocation.getRawArguments());
    }
}