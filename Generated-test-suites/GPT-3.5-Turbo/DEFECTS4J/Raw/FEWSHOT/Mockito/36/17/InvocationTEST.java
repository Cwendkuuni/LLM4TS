package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.ArrayList;

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
import org.mockito.internal.util.ArrayUtil;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.util.Primitives;
import org.mockito.invocation.InvocationOnMock;

public class InvocationTest {
    @Test
    public void testExpansionOfVarArgs() {
        Invocation invocation1 = new Invocation(null, null, new Object[] { 1,
                new Object[] { 2, 3, 4 } }, 0, null);
        assertArrayEquals(new Object[] { 1, 2, 3, 4 },
            invocation1.getArguments());

        Invocation invocation2 = new Invocation(null, null, new Object[] { 1,
                new String[] { "a", "b", "c" } }, 0, null);
        assertArrayEquals(new Object[] { 1, "a", "b", "c" },
            invocation2.getArguments());

        Invocation invocation3 = new Invocation(null, null, new Object[] { 1,
                null }, 0, null);
        assertArrayEquals(new Object[] { 1, null },
            invocation3.getArguments());

        Invocation invocation4 = new Invocation(null, null, new Object[] { null }, 0, null);
        assertArrayEquals(new Object[] { null }, invocation4.getArguments());

        Invocation invocation5 = new Invocation(null, null, null, 0, null);
        assertArrayEquals(new Object[0], invocation5.getArguments());
    }

    @Test
    public void testEqualsWithEqualInvocations() {
        Invocation invocation1 = new Invocation(null, null, new Object[] { 1 },
            0, null);
        Invocation invocation2 = new Invocation(null, null, new Object[] { 1 },
            0, null);

        assertTrue(invocation1.equals(invocation2));
        assertTrue(invocation2.equals(invocation1));
        assertTrue(invocation1.hashCode() == invocation2.hashCode());
    }

    @Test
    public void testEqualsWithNotEqualInvocations() {
        Invocation invocation1 = new Invocation(null, null, new Object[] { 1 },
            0, null);
        Invocation invocation2 = new Invocation(null, null, new Object[] { 2 },
            0, null);

        assertFalse(invocation1.equals(invocation2));
        assertFalse(invocation2.equals(invocation1));
    }

    @Test
    public void testToString() {
        Invocation invocation1 = new Invocation(null, null, new Object[] { 1 },
            0, null);
        assertEquals("foo.1", invocation1.toString());

        Invocation invocation2 = new Invocation(null, null, new Object[] { "a", "b", "c" },
            0, null);
        assertEquals("foo.a,b,c", invocation2.toString());
    }

    @Test
    public void testToStringWithMatchers() {
        Invocation invocation1 = new Invocation(null, null, new Object[] { 1,
                new Object[] { 2, 3, 4 } }, 0, null);
        List<Matcher> matchers1 = new ArrayList<Matcher>();
        matchers1.add(new Equals(1));
        matchers1.add(new ArrayEquals(new Object[] { 2, 3, 4 }));
        assertEquals("foo(1, [2, 3, 4])", invocation1.toString(matchers1, new PrintSettings()));

        Invocation invocation2 = new Invocation(null, null, new Object[] { "a", "b", "c" },
            0, null);
        List<Matcher> matchers2 = new ArrayList<Matcher>();
        matchers2.add(new Equals("a"));
        matchers2.add(new Equals("b"));
        matchers2.add(new Equals("c"));
        assertEquals("foo(\"a\", \"b\", \"c\")", invocation2.toString(matchers2, new PrintSettings()));
    }

    @Test
    public void testIsToString() {
        InvocationOnMock invocation1 = new Invocation(null, null, new Object[0], null, null);
        assertTrue(Invocation.isToString(invocation1));

        InvocationOnMock invocation2 = new Invocation(null, null, new Object[] { 1 }, null, null);
        assertFalse(Invocation.isToString(invocation2));
    }

    @Test
    public void testIsValidExceptionWithValidThrowable() throws Exception {
        Invocation invocation = new Invocation(null, null, new Object[0], 0, null);
        assertTrue(invocation.isValidException(new Exception()));
    }

    @Test
    public void testIsValidExceptionWithInvalidThrowable() throws Exception {
        Invocation invocation = new Invocation(null, null, new Object[0], 0, null);
        assertFalse(invocation.isValidException(new RuntimeException()));
    }

    @Test
    public void testIsValidReturnTypeWithMatchingClass() {
        Invocation invocation = new Invocation(null, null, new Object[0], 0, null);
        assertTrue(invocation.isValidReturnType(Object.class));
    }

    @Test
    public void testIsValidReturnTypeWithNonMatchingClass() {
        Invocation invocation = new Invocation(null, null, new Object[0], 0, null);
        assertFalse(invocation.isValidReturnType(Integer.class));
    }

    @Test
    public void testIsVoidWithVoidReturnType() {
        Invocation invocation = new Invocation(null, null, new Object[0], 0, null);
        assertTrue(invocation.isVoid());
    }

    @Test
    public void testIsVoidWithNonVoidReturnType() {
        Invocation invocation = new Invocation(null, null, new Object[0], 0, null);
        assertFalse(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        Invocation invocation = new Invocation(null, null, new Object[0], 0, null);
        assertEquals("Object", invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        Invocation invocation = new Invocation(null, null, new Object[0], 0, null);
        assertNull(invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitiveWithPrimitiveReturnType() {
        Invocation invocation = new Invocation(null, null, new Object[0], 0, null);
        assertTrue(invocation.returnsPrimitive());
    }

    @Test
    public void testReturnsPrimitiveWithNonPrimitiveReturnType() {
        Invocation invocation = new Invocation(null, null, new Object[0], 0, null);
        assertFalse(invocation.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation(null, null, new Object[0], 0, null);
        assertTrue(invocation.getLocation() instanceof Location);
    }

    @Test
    public void testGetArgumentsCount() {
        Invocation invocation = new Invocation(null, null, new Object[0], 0, null);
        assertEquals(0, invocation.getArgumentsCount());
    }

    @Test
    public void testGetRawArguments() {
        Object[] rawArguments = {1, "a", true};
        Invocation invocation = new Invocation(null, null, rawArguments, 0, null);
        assertArrayEquals(rawArguments, invocation.getRawArguments());
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        Invocation invocation = new Invocation(null, null, new Object[0], 0, null);
        assertNull(invocation.callRealMethod());
    }

    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = new Invocation(null, null, new Object[] { "a", "b", "c" },
            0, null);
        PrintSettings printSettings = new PrintSettings();
        printSettings.setMultiline(true);
        List<Matcher> matchers = new ArrayList<Matcher>();
        matchers.add(new Equals("a"));
        matchers.add(new Equals("b"));
        matchers.add(new Equals("c"));
        assertEquals("foo(\n    \"a\",\n    \"b\",\n    \"c\"\n)", invocation.toString(matchers, printSettings));
    }

    @Test
    public void testMarkVerified() {
        Invocation invocation = new Invocation(null, null, new Object[0], 0, null);
        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }

    @Test
    public void testMarkVerifiedInOrder() {
        Invocation invocation = new Invocation(null, null, new Object[0], 0, null);
        invocation.markVerifiedInOrder();
        assertTrue(invocation.isVerified());
        assertTrue(invocation.isVerifiedInOrder());
    }

}