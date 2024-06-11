package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.apache.commons.cli.Util;
import org.apache.commons.cli.UtilTest;
import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unchecked")
public class InvocationTest {

    private Invocation invocation;

    @Before
    public void setUp() {
        Object mock = mock(Object.class);
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object[] args = { "arg1", "arg2" };
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);
        invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);
    }

    @Test
    public void testGetMock() {
        assertEquals(mock(Object.class), invocation.getMock());
    }

    @Test
    public void testGetMethod() {
        assertEquals(mock(MockitoMethod.class), invocation.getMethod());
    }

    @Test
    public void testGetArguments() {
        assertEquals(2, invocation.getArguments().length);
        assertEquals("arg1", invocation.getArguments()[0]);
        assertEquals("arg2", invocation.getArguments()[1]);
    }

    @Test
    public void testIsVerified() {
        assertFalse(invocation.isVerified());
    }

    @Test
    public void testGetSequenceNumber() {
        assertEquals(new Integer(1), invocation.getSequenceNumber());
    }

    @Test
    public void testIsVerifiedInOrder() {
        assertFalse(invocation.isVerifiedInOrder());
    }

    @Test
    public void testEquals() {
        Invocation other = new Invocation(mock(Object.class), mock(MockitoMethod.class), new Object[] { "arg1", "arg2" }, 1,
                mock(RealMethod.class));
        assertEquals(invocation, other);
    }

    @Test
    public void testHashCode() {
        // Test not implemented exception
    }

    @Test
    public void testToString() {
        assertEquals("mock.Object .method(arg1, arg2)", invocation.toString());
    }

    @Test
    public void testArgumentsToMatchers() {
        List<Matcher> matchers = new ArrayList<>(Arrays.asList(new Equals("arg1"), new Equals("arg2")));
        assertEquals(matchers, invocation.argumentsToMatchers());
    }

    @Test
    public void testIsToString() {
        assertFalse(Invocation.isToString(invocation));
    }

    @Test
    public void testIsValidException() {
        assertFalse(invocation.isValidException(new Exception()));
    }

    @Test
    public void testIsValidReturnType() {
        assertFalse(invocation.isValidReturnType(String.class));
    }

    @Test
    public void testIsVoid() {
        assertFalse(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        assertEquals("ReturnType", invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        assertEquals("method", invocation.getMethodName());
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
        assertEquals(2, invocation.getArgumentsCount());
    }

    @Test
    public void testGetRawArguments() {
        assertArrayEquals(new Object[] { "arg1", "arg2" }, invocation.getRawArguments());
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        // Test not implemented exception
    }

    @Test
    public void testToStringWithPrintSettings() {
        // Test not implemented exception
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