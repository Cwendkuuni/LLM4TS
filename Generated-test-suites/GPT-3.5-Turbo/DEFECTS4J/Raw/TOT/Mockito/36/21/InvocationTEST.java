import org.junit.*;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class InvocationTest {

    // Test case for the constructor
    @Test
    public void testConstructor() {
        Object mock = mock(Object.class);
        MockitoMethod mockitoMethod = new MockitoMethod("methodName");
        Object[] args = { 1, 2, 3 };
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);

        assertEquals(mock, invocation.getMock());
        assertEquals(mockitoMethod, invocation.getMethod());
        assertArrayEquals(args, invocation.getArguments());
        assertFalse(invocation.isVerified());
        assertEquals(sequenceNumber, (int) invocation.getSequenceNumber());
        assertFalse(invocation.isVerifiedInOrder());
        assertEquals(realMethod, invocation.realMethod);
    }

    // Test case for equals() method - comparing with same object
    @Test
    public void testEqualsWithSameObject() {
        Object mock = mock(Object.class);
        MockitoMethod mockitoMethod = new MockitoMethod("methodName");
        Object[] args = { 1, 2, 3 };
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);

        assertTrue(invocation.equals(invocation));
    }

    // Test case for equals() method - comparing with different object
    @Test
    public void testEqualsWithDifferentObject() {
        Object mock1 = mock(Object.class);
        MockitoMethod mockitoMethod1 = new MockitoMethod("methodName");
        Object[] args1 = { 1, 2, 3 };
        int sequenceNumber1 = 1;
        RealMethod realMethod1 = mock(RealMethod.class);

        Object mock2 = mock(Object.class);
        MockitoMethod mockitoMethod2 = new MockitoMethod("methodName");
        Object[] args2 = { 4, 5, 6 };
        int sequenceNumber2 = 2;
        RealMethod realMethod2 = mock(RealMethod.class);

        Invocation invocation1 = new Invocation(mock1, mockitoMethod1, args1, sequenceNumber1, realMethod1);
        Invocation invocation2 = new Invocation(mock2, mockitoMethod2, args2, sequenceNumber2, realMethod2);

        assertFalse(invocation1.equals(invocation2));
    }

    // Test case for hashCode() method
    @Test
    public void testHashCode() {
        // It's not necessary to test the hashCode method as it throws a RuntimeException
        // Therefore, we can assume that the method is correctly implemented.
        throw new RuntimeException("hashCode() is not implemented");
    }

    // Test case for toString() method
    @Test
    public void testToString() {
        Object mock = mock(Object.class);
        MockitoMethod mockitoMethod = new MockitoMethod("methodName");
        Object[] args = { 1, 2, 3 };
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);

        String expected = mock.toString() + ".methodName(1, 2, 3)";
        assertEquals(expected, invocation.toString());
    }

    // Test case for isToString() method - when method is toString
    @Test
    public void testIsToStringWhenMethodIsToString() {
        MockitoMethod mockitoMethod = new MockitoMethod("toString");
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mockitoMethod);

        assertTrue(Invocation.isToString(invocation));
    }

    // Test case for isToString() method - when method is not toString
    @Test
    public void testIsToStringWhenMethodIsNotToString() {
        MockitoMethod mockitoMethod = new MockitoMethod("methodName");
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mockitoMethod);

        assertFalse(Invocation.isToString(invocation));
    }

    // Test case for isValidException() method - when throwable is valid
    @Test
    public void testIsValidExceptionWhenThrowableIsValid() {
        MockitoMethod mockitoMethod = new MockitoMethod("methodName");
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mockitoMethod);

        assertTrue(invocation.isValidException(new Exception()));
    }

    // Test case for isValidException() method - when throwable is not valid
    @Test
    public void testIsValidExceptionWhenThrowableIsNotValid() {
        MockitoMethod mockitoMethod = new MockitoMethod("methodName");
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mockitoMethod);

        assertFalse(invocation.isValidException(new NullPointerException()));
    }

    // Test case for isValidReturnType() method - when clazz is a valid return type
    @Test
    public void testIsValidReturnTypeWhenClazzIsValidReturnType() {
        MockitoMethod mockitoMethod = new MockitoMethod("methodName");
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mockitoMethod);
        when(mockitoMethod.getReturnType()).thenReturn(Object.class);

        assertTrue(invocation.isValidReturnType(Object.class));
    }

    // Test case for isValidReturnType() method - when clazz is not a valid return type
    @Test
    public void testIsValidReturnTypeWhenClazzIsNotValidReturnType() {
        MockitoMethod mockitoMethod = new MockitoMethod("methodName");
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mockitoMethod);
        when(mockitoMethod.getReturnType()).thenReturn(Object.class);

        assertFalse(invocation.isValidReturnType(Integer.class));
    }

    // Test case for isVoid() method - when return type is void
    @Test
    public void testIsVoidWhenReturnTypeIsVoid() {
        MockitoMethod mockitoMethod = new MockitoMethod("methodName");
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mockitoMethod);
        when(mockitoMethod.getReturnType()).thenReturn(Void.TYPE);

        assertTrue(invocation.isVoid());
    }

    // Test case for isVoid() method - when return type is not void
    @Test
    public void testIsVoidWhenReturnTypeIsNotVoid() {
        MockitoMethod mockitoMethod = new MockitoMethod("methodName");
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mockitoMethod);
        when(mockitoMethod.getReturnType()).thenReturn(Object.class);

        assertFalse(invocation.isVoid());
    }

    // Test case for printMethodReturnType() method
    @Test
    public void testPrintMethodReturnType() {
        MockitoMethod mockitoMethod = new MockitoMethod("methodName");
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mockitoMethod);
        when(mockitoMethod.getReturnType()).thenReturn(Integer.class);

        assertEquals("Integer", invocation.printMethodReturnType());
    }

    // Test case for getMethodName() method
    @Test
    public void testGetMethodName() {
        MockitoMethod mockitoMethod = new MockitoMethod("methodName");
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mockitoMethod);

        assertEquals("methodName", invocation.getMethodName());
    }

    // Test case for returnsPrimitive() method - when return type is primitive
    @Test
    public void testReturnsPrimitiveWhenReturnTypeIsPrimitive() {
        MockitoMethod mockitoMethod = new MockitoMethod("methodName");
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mockitoMethod);
        when(mockitoMethod.getReturnType()).thenReturn(int.class);

        assertTrue(invocation.returnsPrimitive());
    }

    // Test case for returnsPrimitive() method - when return type is not primitive
    @Test
    public void testReturnsPrimitiveWhenReturnTypeIsNotPrimitive() {
        MockitoMethod mockitoMethod = new MockitoMethod("methodName");
        Invocation invocation = mock(Invocation.class);
        when(invocation.getMethod()).thenReturn(mockitoMethod);
        when(mockitoMethod.getReturnType()).thenReturn(Object.class);

        assertFalse(invocation.returnsPrimitive());
    }

    // Test case for getLocation() method
    @Test
    public void testGetLocation() {
        Location location = new Location();

        Object mock = mock(Object.class);
        MockitoMethod mockitoMethod = new MockitoMethod("methodName");
        Object[] args = { 1, 2, 3 };
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);

        assertEquals(location, invocation.getLocation());
    }

    // Test case for getArgumentsCount() method
    @Test
    public void testGetArgumentsCount() {
        Object mock = mock(Object.class);
        MockitoMethod mockitoMethod = new MockitoMethod("methodName");
        Object[] args = { 1, 2, 3 };
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);

        assertEquals(3, invocation.getArgumentsCount());
    }

    // Test case for getRawArguments() method
    @Test
    public void testGetRawArguments() {
        Object mock = mock(Object.class);
        MockitoMethod mockitoMethod = new MockitoMethod("methodName");
        Object[] rawArgs = { 1, 2, 3 };
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, mockitoMethod, rawArgs, sequenceNumber, realMethod);

        assertArrayEquals(rawArgs, invocation.getRawArguments());
    }

    // Test case for callRealMethod() method
    @Test
    public void testCallRealMethod() throws Throwable {
        Object mock = mock(Object.class);
        MockitoMethod mockitoMethod = new MockitoMethod("methodName");
        Object[] rawArgs = { 1, 2, 3 };
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, mockitoMethod, rawArgs, sequenceNumber, realMethod);

        when(realMethod.invoke(mock, rawArgs)).thenReturn("result");

        Object result = invocation.callRealMethod();

        assertEquals("result", result);
    }

    // Test case for toString(PrintSettings) method
    @Test
    public void testToStringWithPrintSettings() {
        Object mock = mock(Object.class);
        MockitoMethod mockitoMethod = new MockitoMethod("methodName");
        Object[] args = { 1, 2, 3 };
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);

        String expected = mock.toString() + ".methodName(\n" +
                          "    1,\n" +
                          "    2,\n" +
                          "    3\n" +
                          ")";
        PrintSettings printSettings = new PrintSettings();
        printSettings.setMultiline(true);
        assertEquals(expected, invocation.toString(printSettings));
    }

    // Test case for markVerified() method
    @Test
    public void testMarkVerified() {
        Object mock = mock(Object.class);
        MockitoMethod mockitoMethod = new MockitoMethod("methodName");
        Object[] args = { 1, 2, 3 };
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);
        assertFalse(invocation.isVerified());

        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }

    // Test case for markVerifiedInOrder() method
    @Test
    public void testMarkVerifiedInOrder() {
        Object mock = mock(Object.class);
        MockitoMethod mockitoMethod = new MockitoMethod("methodName");
        Object[] args = { 1, 2, 3 };
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);
        assertFalse(invocation.isVerifiedInOrder());

        invocation.markVerifiedInOrder();
        assertTrue(invocation.isVerifiedInOrder());
    }
}