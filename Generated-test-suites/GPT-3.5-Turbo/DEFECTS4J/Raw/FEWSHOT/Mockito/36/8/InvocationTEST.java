package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.hamcrest.Matcher;
import org.junit.Before;
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
import org.mockito.invocation.InvocationOnMock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
public class InvocationTest {
    private Invocation invocation;
    private Object mock;
    private MockitoMethod method;
    private Object[] args;
    private int sequenceNumber;
    private RealMethod realMethod;

    @Before
    public void setUp() {
        mock = new Object();
        method = new MockitoMethod();
        args = new Object[1];
        sequenceNumber = 1;
        realMethod = new RealMethod();
        invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
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
        assertEquals(args, invocation.getArguments());
    }

    @Test
    public void testIsVerified() {
        assertFalse(invocation.isVerified());
    }

    @Test
    public void testGetSequenceNumber() {
        assertEquals(Integer.valueOf(sequenceNumber), invocation.getSequenceNumber());
    }

    @Test
    public void testIsVerifiedInOrder() {
        assertFalse(invocation.isVerifiedInOrder());
    }

    @Test
    public void testEquals() {
        Invocation other = new Invocation(mock, method, args, sequenceNumber, realMethod);
        assertTrue(invocation.equals(other));
    }

    @Test
    public void testEqualArguments() {
        Object[] arguments = new Object[1];
        assertTrue(invocation.equalArguments(arguments));
    }

    @Test
    public void testHashCode() {
        Throwable throwable = new Throwable();
        assertFalse(invocation.isValidException(throwable));
    }

    @Test
    public void testToString() {
        String expected = invokeToString(invocation, invocation.argumentsToMatchers(), new PrintSettings());
        assertEquals(expected, invocation.toString());
    }

    @Test
    public void testArgumentsToMatchers() {
        List<Matcher> matchers = new ArrayList<Matcher>();
        matchers.add(new Equals(mock));
        assertEquals(matchers, invocation.argumentsToMatchers());
    }

    @Test
    public void testIsToString() {
        InvocationOnMock invocation = new InvocationOnMock();
        assertFalse(Invocation.isToString(invocation));
    }

    @Test
    public void testIsValidReturnType() {
        Class clazz = new Class();
        assertFalse(invocation.isValidReturnType(clazz));
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
        assertEquals(method.getName(), invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        assertFalse(invocation.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        Location location = new Location();
        assertEquals(location, invocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        assertEquals(args.length, invocation.getArgumentsCount());
    }

    @Test
    public void testGetRawArguments() {
        assertEquals(invocation.getRawArguments(), invocation.getRawArguments());
    }

    @Test(expected = Throwable.class)
    public void testCallRealMethod() throws Throwable {
        invocation.callRealMethod();
    }

    @Test
    public void testToStringPrintSettings() {
        PrintSettings printSettings = new PrintSettings();
        String expected = invokeToString(invocation, invocation.argumentsToMatchers(), printSettings);
        assertEquals(expected, invocation.toString(printSettings));
    }

    @Test
    public void testMarkVerified() {
        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }

    @Test
    public void testMarkVerifiedInOrder() {
        invocation.markVerifiedInOrder();
        assertTrue(invocation.isVerifiedInOrder());
    }

    private String invokeToString(PrintingFriendlyInvocation invocation, List<Matcher> matchers, PrintSettings printSettings) {
        MatchersPrinter matchersPrinter = new MatchersPrinter();
        String method = invocation.qualifiedMethodName();
        String invocationString = method + matchersPrinter.getArgumentsLine(matchers, printSettings);
        if (printSettings.isMultiline() || (!matchers.isEmpty() && invocationString.length() > Invocation.MAX_LINE_LENGTH)) {
            return method + matchersPrinter.getArgumentsBlock(matchers, printSettings);
        } else {
            return invocationString;
        }
    }
}