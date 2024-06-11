package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.realmethod.RealMethod;
import org.mockito.internal.matchers.*;
import org.mockito.internal.reporting.LogicalPrinter;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.internal.util.*;
import org.mockito.invocation.InvocationOnMock;

public class InvocationTest {
    private Object mock;
    private MockitoMethod method;
    private Object[] arguments;
    private Object[] rawArguments;
    private int sequenceNumber;
    private RealMethod realMethod;
    private Invocation invocation;

    @Before
    public void setup() {
        mock = mock(Object.class);
        method = mock(MockitoMethod.class);
        arguments = new Object[]{"arg1", "arg2"};
        rawArguments = new Object[]{"arg1", "arg2"};
        sequenceNumber = 1;
        realMethod = mock(RealMethod.class);
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
        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }

    @Test
    public void testGetSequenceNumber() {
        assertEquals(sequenceNumber, (int)invocation.getSequenceNumber());
    }

    @Test
    public void testIsVerifiedInOrder() {
        assertFalse(invocation.isVerifiedInOrder());
        invocation.markVerifiedInOrder();
        assertTrue(invocation.isVerifiedInOrder());
    }

    @Test
    public void testEquals() {
        Invocation other = new Invocation(mock, method, arguments, sequenceNumber, realMethod);
        assertTrue(invocation.equals(other));

        other = new Invocation(mock, method, new Object[]{"arg1", "arg2"}, sequenceNumber, realMethod);
        assertTrue(invocation.equals(other));

        other = new Invocation(mock, method, new Object[]{"arg1"}, sequenceNumber, realMethod);
        assertFalse(invocation.equals(other));

        other = new Invocation(mock, method, arguments, sequenceNumber + 1, realMethod);
        assertFalse(invocation.equals(other));

        other = new Invocation(mock, mock(MockitoMethod.class), arguments, sequenceNumber, realMethod);
        assertFalse(invocation.equals(other));
    }

    @Test
    public void testHashCode() {
        try {
            invocation.hashCode();
        } catch (RuntimeException e) {
            assertTrue(e.getMessage().contains("hashCode() is not implemented"));
        }
    }

    @Test
    public void testToString() {
        MockitoMethod mockMethod = mock(MockitoMethod.class);
        when(mockMethod.getName()).thenReturn("mockMethod");
        when(mockMethod.getReturnType()).thenReturn(String.class);

        invocation = new Invocation(mock, mockMethod, new Object[]{1, 2}, sequenceNumber, realMethod);
        String expected = "Mock for Object, hashCode: " + mock.hashCode() + ".mockMethod(1, 2)";
        assertEquals(expected, invocation.toString());

        expected = "Mock for Object, hashCode: " + mock.hashCode() + ".mockMethod(\n    1,\n    2\n)";
        PrintSettings printSettings = new PrintSettings();
        printSettings.multiline();
        assertEquals(expected, invocation.toString(printSettings));
    }

    @Test
    public void testQualifiedMethodName() {
        invocation = new Invocation(mock, method, arguments, sequenceNumber, realMethod);
        assertEquals("Mock for Object, hashCode: " + mock.hashCode() + ".method", invocation.qualifiedMethodName());
    }

    @Test
    public void testArgumentsToMatchers() {
        List<Matcher> expectedMatchers = new ArrayList<Matcher>();
        expectedMatchers.add(new Equals("arg1"));
        expectedMatchers.add(new Equals("arg2"));

        invocation = new Invocation(mock, method, arguments, sequenceNumber, realMethod);
        assertEquals(expectedMatchers, invocation.argumentsToMatchers());

        Object[] arrayArgs = new Object[]{"arg1", new String[]{"arg2", "arg3"}};
        invocation = new Invocation(mock, method, arrayArgs, sequenceNumber, realMethod);
        expectedMatchers.clear();
        expectedMatchers.add(new Equals("arg1"));
        expectedMatchers.add(new ArrayEquals(new String[]{"arg2", "arg3"}));
        assertEquals(expectedMatchers, invocation.argumentsToMatchers());
    }

    @Test
    public void testIsToString() {
        MockitoMethod toStringMethod = mock(MockitoMethod.class);
        when(toStringMethod.getName()).thenReturn("toString");
        when(toStringMethod.getReturnType()).thenReturn(String.class);

        MockitoMethod otherMethod = mock(MockitoMethod.class);
        when(otherMethod.getName()).thenReturn("otherMethod");
        when(otherMethod.getReturnType()).thenReturn(String.class);

        invocation = new Invocation(mock, toStringMethod, arguments, sequenceNumber, realMethod);
        assertTrue(invocation.isToString());

        invocation = new Invocation(mock, otherMethod, arguments, sequenceNumber, realMethod);
        assertFalse(invocation.isToString());
    }

    @Test
    public void testIsValidException() {
        MockitoMethod mockMethod = mock(MockitoMethod.class);
        when(mockMethod.getExceptionTypes()).thenReturn(new Class<?>[]{RuntimeException.class, NullPointerException.class});

        Throwable runtimeException = new RuntimeException();
        Throwable illegalArgumentException = new IllegalArgumentException();

        invocation = new Invocation(mock, mockMethod, arguments, sequenceNumber, realMethod);
        assertTrue(invocation.isValidException(runtimeException));
        assertTrue(invocation.isValidException(new NullPointerException()));
        assertFalse(invocation.isValidException(illegalArgumentException));
    }

    @Test
    public void testIsValidReturnType() {
        MockitoMethod mockMethod = mock(MockitoMethod.class);
        when(mockMethod.getReturnType()).thenReturn(String.class);

        invocation = new Invocation(mock, mockMethod, arguments, sequenceNumber, realMethod);
        assertTrue(invocation.isValidReturnType(String.class));
        assertTrue(invocation.isValidReturnType(Object.class));
        assertFalse(invocation.isValidReturnType(Integer.class));
    }

    @Test
    public void testIsVoid() {
        MockitoMethod voidMethod = mock(MockitoMethod.class);
        when(voidMethod.getReturnType()).thenReturn(Void.TYPE);

        MockitoMethod nonVoidMethod = mock(MockitoMethod.class);
        when(nonVoidMethod.getReturnType()).thenReturn(String.class);

        invocation = new Invocation(mock, voidMethod, arguments, sequenceNumber, realMethod);
        assertTrue(invocation.isVoid());

        invocation = new Invocation(mock, nonVoidMethod, arguments, sequenceNumber, realMethod);
        assertFalse(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        MockitoMethod mockMethod = mock(MockitoMethod.class);
        when(mockMethod.getReturnType()).thenReturn(String.class);

        invocation = new Invocation(mock, mockMethod, arguments, sequenceNumber, realMethod);
        assertEquals("String", invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        MockitoMethod mockMethod = mock(MockitoMethod.class);
        when(mockMethod.getName()).thenReturn("mockMethod");

        invocation = new Invocation(mock, mockMethod, arguments, sequenceNumber, realMethod);
        assertEquals("mockMethod", invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        MockitoMethod mockMethod = mock(MockitoMethod.class);
        when(mockMethod.getReturnType()).thenReturn(int.class);

        invocation = new Invocation(mock, mockMethod, arguments, sequenceNumber, realMethod);
        assertTrue(invocation.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        Location location = invocation.getLocation();
        assertEquals(Location.class, location.getClass());
    }

    @Test
    public void testGetArgumentsCount() {
        invocation = new Invocation(mock, method, new Object[]{}, sequenceNumber, realMethod);
        assertEquals(0, invocation.getArgumentsCount());

        invocation = new Invocation(mock, method, arguments, sequenceNumber, realMethod);
        assertEquals(2, invocation.getArgumentsCount());
    }

    @Test
    public void testGetRawArguments() {
        assertEquals(rawArguments, invocation.getRawArguments());
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        Object[] args = new Object[]{1, 2};
        when(realMethod.invoke(mock, rawArguments)).thenReturn("result");

        invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertEquals("result", invocation.callRealMethod());
        verify(realMethod).invoke(mock, rawArguments);
    }

    @Test
    public void testToStringWithPrintSettings() {
        MockitoMethod mockMethod = mock(MockitoMethod.class);
        when(mockMethod.getName()).thenReturn("mockMethod");
        when(mockMethod.getReturnType()).thenReturn(String.class);

        invocation = new Invocation(mock, mockMethod, new Object[]{1, 2}, sequenceNumber, realMethod);
        PrintSettings printSettings = new PrintSettings();
        printSettings.multiline();

        String expected = "Mock for Object, hashCode: " + mock.hashCode() + ".mockMethod(\n    1,\n    2\n)";
        assertEquals(expected, invocation.toString(printSettings));
    }

    @Test
    public void testMarkVerified() {
        assertFalse(invocation.isVerified());
        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }

    @Test
    public void testMarkVerifiedInOrder() {
        assertFalse(invocation.isVerifiedInOrder());
        assertFalse(invocation.isVerified());

        invocation.markVerifiedInOrder();

        assertTrue(invocation.isVerifiedInOrder());
        assertTrue(invocation.isVerified());
    }
}