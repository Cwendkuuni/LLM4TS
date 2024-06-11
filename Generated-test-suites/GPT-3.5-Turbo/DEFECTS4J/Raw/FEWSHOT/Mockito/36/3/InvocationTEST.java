package org.mockito.internal.invocation;

import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.*;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InvocationTest {
    private Invocation invocation;

    @Before
    public void setup() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);
        invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
    }

    @Test
    public void testGetMock() {
        Object mock = invocation.getMock();
        assertEquals(mock, mock(Object.class));
    }

    @Test
    public void testGetMethod() {
        MockitoMethod method = invocation.getMethod();
        assertEquals(method, mock(MockitoMethod.class));
    }

    @Test
    public void testGetArguments() {
        Object[] arguments = invocation.getArguments();
        assertEquals(arguments.length, 0);
    }

    @Test
    public void testIsVerified() {
        boolean isVerified = invocation.isVerified();
        assertFalse(isVerified);
    }

    @Test
    public void testGetSequenceNumber() {
        Integer sequenceNumber = invocation.getSequenceNumber();
        assertEquals(sequenceNumber, Integer.valueOf(1));
    }

    @Test
    public void testIsVerifiedInOrder() {
        boolean isVerifiedInOrder = invocation.isVerifiedInOrder();
        assertFalse(isVerifiedInOrder);
    }

    @Test
    public void testEquals() {
        Invocation other = new Invocation(mock(Object.class), mock(MockitoMethod.class), new Object[0], 1, mock(RealMethod.class));
        boolean equals = invocation.equals(other);
        assertTrue(equals);
    }

    @Test
    public void testEqualsWithDifferentMock() {
        Invocation other = new Invocation(mock(String.class), mock(MockitoMethod.class), new Object[0], 1, mock(RealMethod.class));
        boolean equals = invocation.equals(other);
        assertFalse(equals);
    }

    @Test
    public void testEqualsWithDifferentMethod() {
        MockitoMethod method = mock(MockitoMethod.class);
        when(method.getName()).thenReturn("differentMethod");
        Invocation other = new Invocation(mock(Object.class), method, new Object[0], 1, mock(RealMethod.class));
        boolean equals = invocation.equals(other);
        assertFalse(equals);
    }

    @Test
    public void testEqualsWithDifferentArguments() {
        Object[] args = new Object[]{1};
        Invocation other = new Invocation(mock(Object.class), mock(MockitoMethod.class), args, 1, mock(RealMethod.class));
        boolean equals = invocation.equals(other);
        assertFalse(equals);
    }

    @Test
    public void testHashCode() {
        try {
            invocation.hashCode();
            fail("Expected exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("hashCode() is not implemented", e.getMessage());
        }
    }

    @Test
    public void testToString() {
        String toString = invocation.toString();
        assertEquals("org.mockito.internal.invocation.Invocation@" + Integer.toHexString(invocation.hashCode()), toString);
    }

    @Test
    public void testToStringWithMatchers() {
        List<Matcher> matchers = new ArrayList<>();
        MatchersPrinter matchersPrinter = new MatchersPrinter();
        String argumentsBlock = matchersPrinter.getArgumentsBlock(matchers, new PrintSettings());
        String toString = invocation.toString(matchers, new PrintSettings());
        assertEquals(invocation.getMethod().getName() + argumentsBlock, toString);
    }

    @Test
    public void testToStringWithMultiline() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(new Equals("1"));
        MatchersPrinter matchersPrinter = new MatchersPrinter();
        String argumentsBlock = matchersPrinter.getArgumentsBlock(matchers, new PrintSettings());
        String toString = invocation.toString(matchers, new PrintSettings().multiline(true));
        assertEquals(invocation.getMethod().getName() + argumentsBlock, toString);
    }

    @Test
    public void testToStringWithLongInvocation() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(new Equals("1"));
        MatchersPrinter matchersPrinter = new MatchersPrinter();
        String argumentsLine = matchersPrinter.getArgumentsLine(matchers, new PrintSettings());
        String toString = invocation.toString(matchers, new PrintSettings());
        assertEquals(invocation.getMethod().getName() + argumentsLine, toString);
    }

    @Test
    public void testToStringWithMultilineAndLongInvocation() {
        List<Matcher> matchers = new ArrayList<>();
        String toString = invocation.toString(matchers, new PrintSettings().multiline(true));
        assertEquals(invocation.getMethod().getName() + matchers.printMultiline(matchers, new PrintSettings()), toString);
    }

    @Test
    public void testIsToString() {
        String methodName = invocation.getMethod().getName();
        boolean isToString = Invocation.isToString(invocation);
        assertEquals(methodName.equals("toString"), isToString);
    }

    @Test
    public void testIsValidException() {
        boolean isValidException = invocation.isValidException(new RuntimeException());
        assertFalse(isValidException);
    }

    @Test
    public void testIsValidReturnType() {
        boolean isValidReturnType = invocation.isValidReturnType(String.class);
        assertTrue(isValidReturnType);
    }

    @Test
    public void testIsValidReturnTypeWithInvalidReturnType() {
        boolean isValidReturnType = invocation.isValidReturnType(Integer.class);
        assertFalse(isValidReturnType);
    }

    @Test
    public void testIsValidReturnTypeWithPrimitiveReturnType() {
        boolean isValidReturnType = invocation.isValidReturnType(int.class);
        assertTrue(isValidReturnType);
    }

    @Test
    public void testIsVoid() {
        boolean isVoid = invocation.isVoid();
        assertFalse(isVoid);
    }

    @Test
    public void testPrintMethodReturnType() {
        String printMethodReturnType = invocation.printMethodReturnType();
        assertTrue(printMethodReturnType.isEmpty());
    }

    @Test
    public void testGetMethodName() {
        String methodName = invocation.getMethodName();
        assertTrue(methodName.isEmpty());
    }

    @Test
    public void testReturnsPrimitive() {
        boolean returnsPrimitive = invocation.returnsPrimitive();
        assertFalse(returnsPrimitive);
    }

    @Test
    public void testGetLocation() {
        Location location = invocation.getLocation();
        assertNotNull(location);
    }

    @Test
    public void testGetArgumentsCount() {
        int argumentsCount = invocation.getArgumentsCount();
        assertEquals(argumentsCount, 0);
    }

    @Test
    public void testGetRawArguments() {
        Object[] rawArguments = invocation.getRawArguments();
        assertEquals(rawArguments.length, 0);
    }

    @Test
    public void testCallRealMethod() {
        try {
            invocation.callRealMethod();
            fail("Expected exception not thrown");
        } catch (Throwable throwable) {
            assertTrue(throwable instanceof RuntimeException);
        }
    }

    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = new PrintSettings();
        String toString = invocation.toString(printSettings);
        assertEquals("org.mockito.internal.invocation.Invocation@" + Integer.toHexString(invocation.hashCode()), toString);
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