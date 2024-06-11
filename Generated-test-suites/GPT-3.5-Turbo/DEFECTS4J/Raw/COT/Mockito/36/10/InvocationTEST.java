
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class InvocationTest {

    private Invocation invocation;
    private Object mock;
    private MockitoMethod mockitoMethod;
    private Object[] args;
    private int sequenceNumber;
    private RealMethod realMethod;

    @Before
    public void setUp() {
        mock = mock(Object.class);
        mockitoMethod = mock(MockitoMethod.class);
        args = new Object[0];
        sequenceNumber = 0;
        realMethod = mock(RealMethod.class);
        invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);
    }

    @Test
    public void testGetMock() {
        assertEquals(mock, invocation.getMock());
    }

    @Test
    public void testGetMethod() {
        assertEquals(mockitoMethod, invocation.getMethod());
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
        Invocation other = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);
        assertTrue(invocation.equals(other));
    }

    @Test
    public void testHashCode() {
        try {
            invocation.hashCode();
            fail("hashCode() should throw an exception");
        } catch (RuntimeException e) {
            // expected
        }
    }

    @Test
    public void testToString() {
        String expected = mock.toString() + "." + mockitoMethod.getName();
        assertEquals(expected, invocation.toString());
    }

    @Test
    public void testIsToString() {
        InvocationOnMock invocationOnMock = mock(InvocationOnMock.class);
        MockitoMethod method = mock(MockitoMethod.class);
        when(invocationOnMock.getMethod()).thenReturn(method);
        when(method.getName()).thenReturn("toString");
        assertTrue(Invocation.isToString(invocationOnMock));
    }

    @Test
    public void testIsValidException() {
        Throwable throwable = new Exception();
        MockitoMethod method = mock(MockitoMethod.class);
        Class<?>[] exceptionTypes = new Class<?>[]{Exception.class};
        when(method.getExceptionTypes()).thenReturn(exceptionTypes);
        when(invocation.getMethod()).thenReturn(method);
        assertTrue(invocation.isValidException(throwable));
    }

    @Test
    public void testIsValidException_NotValid() {
        Throwable throwable = new RuntimeException();
        MockitoMethod method = mock(MockitoMethod.class);
        Class<?>[] exceptionTypes = new Class<?>[]{Exception.class};
        when(method.getExceptionTypes()).thenReturn(exceptionTypes);
        when(invocation.getMethod()).thenReturn(method);
        assertFalse(invocation.isValidException(throwable));
    }

    @Test
    public void testIsValidReturnType() {
        Class<?> clazz = Object.class;
        MockitoMethod method = mock(MockitoMethod.class);
        Class<?> returnType = Object.class;
        when(method.getReturnType()).thenReturn(returnType);
        when(invocation.getMethod()).thenReturn(method);
        assertTrue(invocation.isValidReturnType(clazz));
    }
    
    @Test
    public void testIsValidReturnType_PrimitiveTypes() {
        Class<?> clazz = int.class;
        MockitoMethod method = mock(MockitoMethod.class);
        Class<?> returnType = Integer.TYPE;
        when(method.getReturnType()).thenReturn(returnType);
        when(invocation.getMethod()).thenReturn(method);
        assertTrue(invocation.isValidReturnType(clazz));
    }

    @Test
    public void testIsValidReturnType_NotValid() {
        Class<?> clazz = String.class;
        MockitoMethod method = mock(MockitoMethod.class);
        Class<?> returnType = Integer.class;
        when(method.getReturnType()).thenReturn(returnType);
        when(invocation.getMethod()).thenReturn(method);
        assertFalse(invocation.isValidReturnType(clazz));
    }

    @Test
    public void testIsVoid() {
        MockitoMethod method = mock(MockitoMethod.class);
        Class<?> returnType = Void.TYPE;
        when(method.getReturnType()).thenReturn(returnType);
        when(invocation.getMethod()).thenReturn(method);
        assertTrue(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        MockitoMethod method = mock(MockitoMethod.class);
        Class<?> returnType = Object.class;
        when(method.getReturnType()).thenReturn(returnType);
        when(invocation.getMethod()).thenReturn(method);
        assertEquals(returnType.getSimpleName(), invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        MockitoMethod method = mock(MockitoMethod.class);
        String methodName = "methodName";
        when(method.getName()).thenReturn(methodName);
        when(invocation.getMethod()).thenReturn(method);
        assertEquals(methodName, invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        MockitoMethod method = mock(MockitoMethod.class);
        Class<?> returnType = int.class;
        when(method.getReturnType()).thenReturn(returnType);
        when(invocation.getMethod()).thenReturn(method);
        assertTrue(invocation.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        assertNotNull(invocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        assertEquals(args.length, invocation.getArgumentsCount());
    }

    @Test
    public void testGetRawArguments() {
        assertEquals(args, invocation.getRawArguments());
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        Object returnValue = new Object();
        when(realMethod.invoke(mock, args)).thenReturn(returnValue);
        assertEquals(returnValue, invocation.callRealMethod());
    }

    @Test
    public void testToString_PrintSettings() {
        PrintSettings printSettings = mock(PrintSettings.class);
        String expected = mock.toString() + "." + mockitoMethod.getName();
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
        assertFalse(invocation.isVerified());
        assertFalse(invocation.isVerifiedInOrder());
        invocation.markVerifiedInOrder();
        assertTrue(invocation.isVerified());
        assertTrue(invocation.isVerifiedInOrder());
    }

}

