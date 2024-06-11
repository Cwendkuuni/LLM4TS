import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.invocation.InvocationOnMock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationTest {

    @Test
    public void testGetMock() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[]{};
        int sequenceNumber = 0;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertEquals(mock, invocation.getMock());
    }

    @Test
    public void testGetMethod() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[]{};
        int sequenceNumber = 0;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertEquals(method, invocation.getMethod());
    }

    @Test
    public void testGetArguments() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[]{"arg1", "arg2"};
        int sequenceNumber = 0;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertArrayEquals(args, invocation.getArguments());
    }

    @Test
    public void testisVerified() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[]{};
        int sequenceNumber = 0;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertFalse(invocation.isVerified());

        invocation.markVerified();

        assertTrue(invocation.isVerified());
    }

    @Test
    public void testGetSequenceNumber() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[]{};
        int sequenceNumber = 10;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertEquals(new Integer(sequenceNumber), invocation.getSequenceNumber());
    }

    @Test
    public void testisVerifiedInOrder() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[]{};
        int sequenceNumber = 0;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertFalse(invocation.isVerifiedInOrder());

        invocation.markVerifiedInOrder();

        assertTrue(invocation.isVerifiedInOrder());
    }

    @Test
    public void testEquals() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        MockitoMethod method1 = mock(MockitoMethod.class);
        MockitoMethod method2 = mock(MockitoMethod.class);
        Object[] args1 = new Object[]{"arg1", "arg2"};
        Object[] args2 = new Object[]{"arg3", "arg4"};
        int sequenceNumber1 = 0;
        int sequenceNumber2 = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation1 = new Invocation(mock1, method1, args1, sequenceNumber1, realMethod);
        Invocation invocation2 = new Invocation(mock1, method1, args1, sequenceNumber1, realMethod);
        Invocation invocation3 = new Invocation(mock1, method2, args1, sequenceNumber1, realMethod);
        Invocation invocation4 = new Invocation(mock1, method1, args2, sequenceNumber1, realMethod);
        Invocation invocation5 = new Invocation(mock1, method1, args1, sequenceNumber2, realMethod);
        Invocation invocation6 = new Invocation(mock2, method1, args1, sequenceNumber1, realMethod);

        assertTrue(invocation1.equals(invocation2));
        assertFalse(invocation1.equals(invocation3));
        assertFalse(invocation1.equals(invocation4));
        assertFalse(invocation1.equals(invocation5));
        assertFalse(invocation1.equals(invocation6));
    }

    @Test
    public void testHashCode() {
        Invocation invocation = new Invocation(null, null, null, 0, null);

        try {
            invocation.hashCode();
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            assertEquals("hashCode() is not implemented", e.getMessage());
        }
    }

    @Test
    public void testToString() {
        Invocation invocation = new Invocation(null, null, new Object[]{"arg1", "arg2"}, 0, null);

        assertEquals("null.null(arg1, arg2)", invocation.toString());
    }

    @Test
    public void testIsToString() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        MockitoMethod method = mock(MockitoMethod.class);
        when(invocation.getMethod()).thenReturn(method);
        when(method.getName()).thenReturn("toString");

        assertTrue(Invocation.isToString(invocation));
    }

    @Test
    public void testIsValidException() {
        Invocation invocation = new Invocation(null, null, null, 0, null);
        Throwable throwable = new Exception();
        MockitoMethod method = mock(MockitoMethod.class);
        when(method.getExceptionTypes()).thenReturn(new Class[]{RuntimeException.class, Exception.class});

        invocation = spy(invocation);
        when(invocation.getMethod()).thenReturn(method);

        assertTrue(invocation.isValidException(throwable));
    }

    @Test
    public void testIsValidReturnTypePrimitive() {
        Invocation invocation = new Invocation(null, null, null, 0, null);
        MockitoMethod method = mock(MockitoMethod.class);
        when(method.getReturnType()).thenReturn(int.class);

        invocation = spy(invocation);
        when(invocation.getMethod()).thenReturn(method);

        assertTrue(invocation.isValidReturnType(int.class));
        assertTrue(invocation.isValidReturnType(Integer.class));
        assertFalse(invocation.isValidReturnType(String.class));
    }

    @Test
    public void testIsValidReturnTypeNonPrimitive() {
        Invocation invocation = new Invocation(null, null, null, 0, null);
        MockitoMethod method = mock(MockitoMethod.class);
        when(method.getReturnType()).thenReturn(Object.class);

        invocation = spy(invocation);
        when(invocation.getMethod()).thenReturn(method);

        assertTrue(invocation.isValidReturnType(Object.class));
        assertFalse(invocation.isValidReturnType(String.class));
    }

    @Test
    public void testIsVoid() {
        Invocation invocation = new Invocation(null, null, null, 0, null);
        MockitoMethod method = mock(MockitoMethod.class);
        when(method.getReturnType()).thenReturn(void.class);

        invocation = spy(invocation);
        when(invocation.getMethod()).thenReturn(method);

        assertTrue(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        Invocation invocation = new Invocation(null, null, null, 0, null);
        MockitoMethod method = mock(MockitoMethod.class);
        when(method.getReturnType()).thenReturn(Object.class);

        invocation = spy(invocation);
        when(invocation.getMethod()).thenReturn(method);

        assertEquals("Object", invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        Invocation invocation = new Invocation(null, null, null, 0, null);
        MockitoMethod method = mock(MockitoMethod.class);
        when(method.getName()).thenReturn("methodName");

        invocation = spy(invocation);
        when(invocation.getMethod()).thenReturn(method);

        assertEquals("methodName", invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        Invocation invocation = new Invocation(null, null, null, 0, null);
        MockitoMethod method = mock(MockitoMethod.class);
        when(method.getReturnType()).thenReturn(int.class);

        invocation = spy(invocation);
        when(invocation.getMethod()).thenReturn(method);

        assertTrue(invocation.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation(null, null, null, 0, null);

        assertNotNull(invocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        Invocation invocation = new Invocation(null, null, new Object[]{"arg1", "arg2"}, 0, null);

        assertEquals(2, invocation.getArgumentsCount());
    }

    @Test
    public void testGetRawArguments() {
        Invocation invocation = new Invocation(null, null, new Object[]{"arg1", "arg2"}, 0, null);

        assertArrayEquals(new Object[]{"arg1", "arg2"}, invocation.getRawArguments());
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        Invocation invocation = new Invocation(null, null, null, 0, null);
        RealMethod realMethod = mock(RealMethod.class);
        when(realMethod.invoke(any(), any())).thenReturn("result");

        invocation = spy(invocation);
        when(invocation.getMock()).thenReturn(new Object());
        when(invocation.getRawArguments()).thenReturn(new Object[]{});

        assertEquals("result", invocation.callRealMethod());
    }

    @Test
    public void testToStringWithPrintSettings() {
        Invocation invocation = new Invocation(null, null, new Object[]{"arg1", "arg2", "arg3"}, 0, null);
        PrintSettings printSettings = new PrintSettings();
        printSettings.setMultiline(true);

        assertEquals("null.null(\n" +
                "    arg1,\n" +
                "    arg2,\n" +
                "    arg3\n" +
                ")", invocation.toString(printSettings));
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
        assertTrue(invocation.isVerified());
    }

}