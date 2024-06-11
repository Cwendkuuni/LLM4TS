package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.realmethod.RealMethod;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.matchers.MatchersPrinter;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.util.Primitives;
import org.mockito.invocation.InvocationOnMock;

public class InvocationTest {
    @Test
    public void testGetMock() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod(getClass().getMethod("testGetMock"), new Object[0]);
        Invocation invocation = new Invocation(mock, method, new Object[0], 0, null);

        assertEquals(mock, invocation.getMock());
    }

    @Test
    public void testGetMethod() throws NoSuchMethodException {
        MockitoMethod method = new MockitoMethod(getClass().getMethod("testGetMethod"), new Object[0]);
        Invocation invocation = new Invocation(null, method, new Object[0], 0, null);

        assertEquals(method, invocation.getMethod());
    }

    @Test
    public void testGetArguments() {
        Object[] arguments = new Object[] {"arg1", 2};
        MockitoMethod method = new MockitoMethod(getClass().getMethod("testGetArguments"), arguments);
        Invocation invocation = new Invocation(null, method, arguments, 0, null);

        assertEquals(arguments, invocation.getArguments());
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
        Invocation invocation = new Invocation(null, null, null, 5, null);

        assertEquals(Integer.valueOf(5), invocation.getSequenceNumber());
    }

    @Test
    public void testIsVerifiedInOrder() {
        Invocation invocation = new Invocation(null, null, null, 0, null);

        assertFalse(invocation.isVerifiedInOrder());

        invocation.markVerifiedInOrder();

        assertTrue(invocation.isVerifiedInOrder());
    }

    @Test
    public void testEquals() throws NoSuchMethodException {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod(getClass().getMethod("testEquals"), new Object[0]);
        Invocation invocation1 = new Invocation(mock, method, new Object[0], 0, null);
        Invocation invocation2 = new Invocation(mock, method, new Object[0], 0, null);
        Invocation invocation3 = new Invocation(mock, method, new Object[0], 1, null);

        assertTrue(invocation1.equals(invocation1));
        assertFalse(invocation1.equals(null));
        assertFalse(invocation1.equals(new Object()));
        assertTrue(invocation1.equals(invocation2));
        assertFalse(invocation1.equals(invocation3));
    }

    @Test
    public void testHashCode() {
        Invocation invocation = new Invocation(null, null, null, 0, null);

        try {
            invocation.hashCode();
        } catch (RuntimeException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testToString() {
        Object[] arguments = new Object[] {"arg1", 2};
        MockitoMethod method = new MockitoMethod(getClass().getMethod("testToString"), arguments);
        Invocation invocation = new Invocation(null, method, arguments, 0, null);
        List<Matcher> matchers = Arrays.asList(new Equals("arg1"), new Equals(2));
        PrintSettings printSettings = new PrintSettings();

        MatchersPrinter matchersPrinter = new MatchersPrinter();
        String methodString = "InvocationTest.testToString";
        String expected = methodString + matchersPrinter.getArgumentsLine(matchers, printSettings);

        assertEquals(expected, invocation.toString());
    }

    @Test
    public void testIsToString() throws NoSuchMethodException {
        MethodInvocationOnMock invocation = new MethodInvocationOnMock(getClass().getMethod("toString"), new Object[0]);

        assertTrue(Invocation.isToString(invocation));
    }

    @Test
    public void testIsValidException() throws NoSuchMethodException {
        Invocation invocation = new Invocation(null, null, null, 0, null);

        assertTrue(invocation.isValidException(new RuntimeException()));
        assertFalse(invocation.isValidException(new Exception()));
    }

    @Test
    public void testIsValidReturnType() throws NoSuchMethodException {
        MockitoMethod method = new MockitoMethod(getClass().getMethod("testIsValidReturnType"), new Object[0]);
        Invocation invocation = new Invocation(null, method, new Object[0], 0, null);

        assertTrue(invocation.isValidReturnType(Void.TYPE));
        assertFalse(invocation.isValidReturnType(Integer.class));
    }

    @Test
    public void testIsVoid() throws NoSuchMethodException {
        MockitoMethod method = new MockitoMethod(getClass().getMethod("testIsVoid"), new Object[0]);
        Invocation invocation1 = new Invocation(null, method, new Object[0], 0, null);
        MockitoMethod method2 = new MockitoMethod(getClass().getMethod("testIsVoid"), new Object[0], Void.TYPE);
        Invocation invocation2 = new Invocation(null, method2, new Object[0], 0, null);

        assertTrue(invocation1.isVoid());
        assertTrue(invocation2.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() throws NoSuchMethodException {
        MockitoMethod method = new MockitoMethod(getClass().getMethod("testPrintMethodReturnType"), new Object[0]);
        Invocation invocation = new Invocation(null, method, new Object[0], 0, null);

        assertEquals("void", invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() throws NoSuchMethodException {
        MockitoMethod method = new MockitoMethod(getClass().getMethod("testGetMethodName"), new Object[0]);
        Invocation invocation = new Invocation(null, method, new Object[0], 0, null);

        assertEquals("testGetMethodName", invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() throws NoSuchMethodException {
        MockitoMethod method1 = new MockitoMethod(getClass().getMethod("testReturnsPrimitive"), new Object[0], int.class);
        Invocation invocation1 = new Invocation(null, method1, new Object[0], 0, null);
        MockitoMethod method2 = new MockitoMethod(getClass().getMethod("testReturnsPrimitive"), new Object[0],
                Integer.class);
        Invocation invocation2 = new Invocation(null, method2, new Object[0], 0, null);

        assertTrue(invocation1.returnsPrimitive());
        assertFalse(invocation2.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation(null, null, null, 0, null);

        assertNotNull(invocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        Invocation invocation = new Invocation(null, null, new Object[] {"arg1", 2}, 0, null);

        assertEquals(2, invocation.getArgumentsCount());
    }

    @Test
    public void testGetRawArguments() {
        Object[] arguments = new Object[] {"arg1", 2};
        Invocation invocation = new Invocation(null, null, arguments, 0, null);

        assertEquals(arguments, invocation.getRawArguments());
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        RealMethod realMethod = new RealMethod() {
            public Object invoke(Object target, Object[] arguments) throws Throwable {
                return "Real method called";
            }
        };
        Invocation invocation = new Invocation(null, null, null, 0, realMethod);

        assertEquals("Real method called", invocation.callRealMethod());
    }

    @Test
    public void testToStringWithPrintSettings() {
        Object[] arguments = new Object[] {"arg1", 2};
        MockitoMethod method = new MockitoMethod(getClass().getMethod("testToStringWithPrintSettings"), arguments);
        Invocation invocation = new Invocation(null, method, arguments, 0, null);
        List<Matcher> matchers = Arrays.asList(new Equals("arg1"), new Equals(2));
        PrintSettings printSettings = new PrintSettings();
        printSettings.setMultiline(true);

        MatchersPrinter matchersPrinter = new MatchersPrinter();
        String methodString = "InvocationTest.testToStringWithPrintSettings";
        String expected = methodString + matchersPrinter.getArgumentsBlock(matchers, printSettings);

        assertEquals(expected, invocation.toString(printSettings));
    }

    @Test
    public void testMarkVerified() {
        Invocation invocation = new Invocation(null, null, null, 0, null);

        assertFalse(invocation.isVerified());

        invocation.markVerified();

        assertTrue(invocation.isVerified());
    }

    @Test
    public void testMarkVerifiedInOrder() {
        Invocation invocation = new Invocation(null, null, null, 0, null);

        assertFalse(invocation.isVerifiedInOrder());

        invocation.markVerifiedInOrder();

        assertTrue(invocation.isVerifiedInOrder());
    }
}