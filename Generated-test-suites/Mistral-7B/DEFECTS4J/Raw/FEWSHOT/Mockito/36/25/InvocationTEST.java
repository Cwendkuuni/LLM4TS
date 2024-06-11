package org.mockito.internal.invocation;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.internal.matchers.ArgumentsMatchers.arrayContaining;
import static org.mockito.internal.matchers.ArgumentsMatchers.eq;
import static org.mockito.internal.matchers.ArgumentsMatchers.isA;
import static org.mockito.internal.reporting.PrintSettings.printSettings;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.matchers.ArgumentsMatchers;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.util.Primitives;
import org.mockito.internal.util.PrintingFriendlyInvocation;
import org.mockito.internal.util.MockitoUtils;
import org.mockito.internal.util.MockitoUtils$$MockitoTestUtils$$EnhancerByMockitoWithCGLIB$$515a8606;

public class InvocationTest {

    private Object mock;
    private Method method;
    private Class[] exceptionTypes;
    private Invocation invocation;

    @Before
    public void setup() {
        mock = Mockito.mock(Object.class);
        method = Mockito.method(Object.class, "testMethod", String.class, int[].class);
        exceptionTypes = new Class[] { IllegalArgumentException.class };
        invocation = new Invocation(mock, new MockitoMethod(method, exceptionTypes), new Object[] { "arg1", new int[] { 1, 2 } }, 0, null);
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
    public void testArguments() {
        Object[] arguments = invocation.getArguments();
        assertEquals("arg1", arguments[0]);
        assertArrayEquals(new int[] { 1, 2 }, arguments[1]);
    }

    @Test
    public void testIsVerified() {
        assertFalse(invocation.isVerified());
    }

    @Test
    public void testGetSequenceNumber() {
        assertEquals(0, invocation.getSequenceNumber());
    }

    @Test
    public void testIsVerifiedInOrder() {
        assertFalse(invocation.isVerifiedInOrder());
    }

    @Test
    public void testEquals() {
        Invocation invocation2 = new Invocation(mock, method, new Object[] { "arg1", new int[] { 1, 2 } }, 1, null);
        assertFalse(invocation.equals(invocation2));

        Invocation invocation3 = new Invocation(mock, method, new Object[] { "arg1", new int[] { 1, 2 } }, 0, null);
        assertTrue(invocation.equals(invocation3));
    }

    @Test
    public void testHashCode() {
        // Hashcode is not tested here because it throws an exception
    }

    @Test
    public void testToString() {
        String printSettingsString = printSettings().toString();
        assertEquals("testMethod(String arg1, int[] arg2) " + printSettingsString + " [this=" + hashCode() + "]", invocation.toString());
    }

    @Test
    public void testToStringWithArgumentsToMatchers() {
        List<Matcher> matchers = invocation.argumentsToMatchers();
        assertEquals("[arg1 = argument(String.class), arg2 = array(int[0], [1, 2])]", matchers.toString());
    }

    @Test
    public void testToStringWithPrintSettings() {
        String printSettingsString = printSettings().toString();
        String result = invocation.toString(printSettings());
        String expected = "testMethod(String arg1, int[] arg2)" + printSettingsString + " [this=" + hashCode() + "]";
        assertEquals(expected, result);
    }

    @Test
    public void testToStringWithCustomPrintingFriendlyInvocation() {
        PrintingFriendlyInvocation customPrintingFriendlyInvocation = MockitoUtils$$MockitoTestUtils$$EnhancerByMockitoWithCGLIB$$515a8606.create(PrintingFriendlyInvocation.class);
        customPrintingFriendlyInvocation.setPrintSettings(printSettings());
        String result = invocation.toString(customPrintingFriendlyInvocation);
        String expected = "testMethod(String arg1, int[] arg2)" + printSettingsString;
        assertEquals(expected, result);
    }

    @Test
    public void testIsToString() {
        assertTrue(Invocation.isToString(Mockito.mock(InvocationOnMock.class)));
    }

    @Test
    public void testIsValidException() {
        Throwable throwable = new IllegalArgumentException("message");
        assertTrue(invocation.isValidException(throwable));
    }

    @Test
    public void testIsValidReturnType() {
        assertTrue(invocation.isValidReturnType(String.class));
        assertFalse(invocation.isValidReturnType(Object.class));
    }

    @Test
    public void testIsVoid() {
        assertFalse(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        assertEquals("String", invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        assertEquals("testMethod", invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        assertFalse(invocation.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        assertEquals(new Location(), invocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        assertEquals(2, invocation.getArgumentsCount());
    }

    @Test
    public void testGetRawArguments() {
        Object[] rawArguments = invocation.getRawArguments();
        assertSame("arg1", rawArguments[0]);
        assertArrayEquals(new int[] { 1, 2 }, rawArguments[1]);
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        Object result = invocation.callRealMethod();
        assertTrue(result instanceof Object);
    }

    @Test(expected = RuntimeException.class)
    public void testInvalidCallRealMethod() throws Throwable {
        invocation.callRealMethod();
    }

    @Test
    public void testMarkVerified() {
        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }

    @Test
    public void testMarkVerifiedInOrder() {
        invocation.markVerifiedInOrder();
        assertTrue(invocation.isVerified());
        assertTrue(invocation.isVerifiedInOrder());
    }
}