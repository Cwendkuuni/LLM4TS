package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class InvocationTest {

    private Invocation invocation;

    @Before
    public void setup() {
        invocation = new Invocation(null, null, null, 0, null);
    }

    @Test
    public void testGetMock() {
        assertNull(invocation.getMock());
    }

    @Test
    public void testGetMethod() {
        assertNull(invocation.getMethod());
    }

    @Test
    public void testGetArguments() {
        assertNull(invocation.getArguments());
    }

    @Test
    public void testIsVerified() {
        assertFalse(invocation.isVerified());
    }

    @Test
    public void testGetSequenceNumber() {
        assertEquals(Integer.valueOf(0), invocation.getSequenceNumber());
    }

    @Test
    public void testIsVerifiedInOrder() {
        assertFalse(invocation.isVerifiedInOrder());
    }

    @Test
    public void testEquals() {
        assertTrue(invocation.equals(invocation));
    }

    @Test
    public void testHashCode() {
        try {
            invocation.hashCode();
            fail("Expected exception to be thrown");
        } catch (RuntimeException e) {
            assertEquals("hashCode() is not implemented", e.getMessage());
        }
    }

    @Test
    public void testToString() {
        assertEquals("null.null()", invocation.toString());
    }

    @Test
    public void testArgumentsToMatchers() {
        assertEquals(0, invocation.argumentsToMatchers().size());
    }

    @Test
    public void testIsToString() {
        assertFalse(Invocation.isToString(null));
    }

    @Test
    public void testIsValidException() {
        assertTrue(invocation.isValidException(null));
    }

    @Test
    public void testIsValidReturnType() {
        assertTrue(invocation.isValidReturnType(null));
    }

    @Test
    public void testIsVoid() {
        assertFalse(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        assertEquals("", invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        assertNull(invocation.getMethodName());
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
        assertEquals(0, invocation.getArgumentsCount());
    }

    @Test
    public void testGetRawArguments() {
        assertNull(invocation.getRawArguments());
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        assertNull(invocation.callRealMethod());
    }

    @Test
    public void testToStringWithPrintSettings() {
        assertEquals("null.null()", invocation.toString(null));
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