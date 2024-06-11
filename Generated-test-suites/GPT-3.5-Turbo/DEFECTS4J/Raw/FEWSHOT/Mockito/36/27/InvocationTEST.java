package org.mockito.internal.invocation;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationTest {

    @Test
    public void testGetMock() {
        Invocation invocation = new Invocation(mock(Object.class), null, null, 0, null);
        assertNotNull(invocation.getMock());
    }

    @Test
    public void testGetMethod() {
        Invocation invocation = new Invocation(mock(Object.class), mock(MockitoMethod.class), null, 0, null);
        assertNotNull(invocation.getMethod());
    }

    @Test
    public void testGetArguments() {
        Object[] args = {1, 2, 3};
        Invocation invocation = new Invocation(mock(Object.class), mock(MockitoMethod.class), args, 0, null);
        assertNotNull(invocation.getArguments());
        assertArrayEquals(args, invocation.getArguments());
    }

    @Test
    public void testIsVerified() {
        Invocation invocation = new Invocation(mock(Object.class), null, null, 0, null);
        assertFalse(invocation.isVerified());
        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }

    @Test
    public void testGetSequenceNumber() {
        Invocation invocation = new Invocation(mock(Object.class), null, null, 0, null);
        assertEquals(Integer.valueOf(0), invocation.getSequenceNumber());
    }

    @Test
    public void testIsVerifiedInOrder() {
        Invocation invocation = new Invocation(mock(Object.class), null, null, 0, null);
        assertFalse(invocation.isVerifiedInOrder());
        invocation.markVerifiedInOrder();
        assertTrue(invocation.isVerifiedInOrder());
    }

    @Test
    public void testEquals() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args1 = {1, 2};
        Object[] args2 = {3, 4};
        Invocation invocation1 = new Invocation(mock, method, args1, 0, null);
        Invocation invocation2 = new Invocation(mock, method, args1, 1, null);
        Invocation invocation3 = new Invocation(mock, method, args2, 0, null);
        Invocation invocation4 = new Invocation(mock, method, args1, 0, null);

        assertFalse(invocation1.equals(null));
        assertFalse(invocation1.equals(new Object()));
        assertFalse(invocation1.equals(invocation2));
        assertFalse(invocation1.equals(invocation3));
        assertTrue(invocation1.equals(invocation4));
    }

    @Test
    public void testHashCode() {
        Invocation invocation = new Invocation(mock(Object.class), null, null, 0, null);
        assertThrows(RuntimeException.class, invocation::hashCode);
    }

    @Test
    public void testToString() {
        Invocation invocation = new Invocation(mock(Object.class), null, null, 0, null);
        assertNotNull(invocation.toString());
    }

    @Test
    public void testArgumentsToMatchers() {
        Object[] args = {1, "test"};
        Invocation invocation = new Invocation(mock(Object.class), null, args, 0, null);
        assertNotNull(invocation.argumentsToMatchers());
        assertEquals(2, invocation.argumentsToMatchers().size());
    }

    @Test
    public void testIsToString() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        MockitoMethod method = mock(MockitoMethod.class);
        when(invocation.getMethod()).thenReturn(method);
        when(method.getName()).thenReturn("toString");

        invocationIsToString(invocation);
    }

    @Test
    public void testIsValidException() {
        Invocation invocation = new Invocation(mock(Object.class), null, null, 0, null);
        assertFalse(invocation.isValidException(new Throwable()));
        assertTrue(invocation.isValidException(new Exception()));
    }

    @Test
    public void testIsValidReturnType() {
        MockitoMethod method = mock(MockitoMethod.class);
        when(method.getReturnType()).thenReturn(Object.class);
        Invocation invocation = new Invocation(mock(Object.class), method, null, 0, null);
        assertTrue(invocation.isValidReturnType(Object.class));
        assertFalse(invocation.isValidReturnType(Integer.class));
    }

    @Test
    public void testIsVoid() {
        MockitoMethod method = mock(MockitoMethod.class);
        when(method.getReturnType()).thenReturn(void.class);
        Invocation invocation = new Invocation(mock(Object.class), method, null, 0, null);
        assertTrue(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        MockitoMethod method = mock(MockitoMethod.class);
        when(method.getReturnType()).thenReturn(Integer.class);
        Invocation invocation = new Invocation(mock(Object.class), method, null, 0, null);
        assertEquals("Integer", invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        MockitoMethod method = mock(MockitoMethod.class);
        when(method.getName()).thenReturn("testMethod");
        Invocation invocation = new Invocation(mock(Object.class), method, null, 0, null);
        assertEquals("testMethod", invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        MockitoMethod method = mock(MockitoMethod.class);
        when(method.getReturnType()).thenReturn(int.class);
        Invocation invocation = new Invocation(mock(Object.class), method, null, 0, null);
        assertTrue(invocation.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation(mock(Object.class), null, null, 0, null);
        assertNotNull(invocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        Object[] args = {1, 2, 3};
        Invocation invocation = new Invocation(mock(Object.class), null, args, 0, null);
        assertEquals(3, invocation.getArgumentsCount());
    }

    @Test
    public void testGetRawArguments() {
        Object[] args = {1, 2, 3};
        Invocation invocation = new Invocation(mock(Object.class), null, args, 0, null);
        assertNotNull(invocation.getRawArguments());
        assertArrayEquals(args, invocation.getRawArguments());
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        RealMethod realMethod = mock(RealMethod.class);
        Invocation invocation = new Invocation(mock(Object.class), null, null, 0, realMethod);
        when(realMethod.invoke(any(), any())).thenReturn("test");
        Object result = invocation.callRealMethod();
        assertEquals("test", result);
    }

    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = mock(PrintSettings.class);
        Invocation invocation = new Invocation(mock(Object.class), null, null, 0, null);
        assertNotNull(invocation.toString(printSettings));
    }

    @Test
    public void testMarkVerified() {
        Invocation invocation = new Invocation(mock(Object.class), null, null, 0, null);
        assertFalse(invocation.isVerified());
        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }

    @Test
    public void testMarkVerifiedInOrder() {
        Invocation invocation = new Invocation(mock(Object.class), null, null, 0, null);
        assertFalse(invocation.isVerified());
        assertFalse(invocation.isVerifiedInOrder());
        invocation.markVerifiedInOrder();
        assertTrue(invocation.isVerified());
        assertTrue(invocation.isVerifiedInOrder());
    }

    private void invocationIsToString(InvocationOnMock invocation) {
        assert(invocation.getMethod().getName().equals("toString()"));
    }
}