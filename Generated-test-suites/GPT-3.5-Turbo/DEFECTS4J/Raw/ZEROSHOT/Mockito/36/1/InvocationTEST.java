import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

@SuppressWarnings("unchecked")
public class InvocationTest {

    @Test
    public void testGetMock() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = {1, 2, 3};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);
        
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
        
        assertEquals(mock, invocation.getMock());
    }

    @Test
    public void testGetMethod() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = {1, 2, 3};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);
        
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
        
        assertEquals(method, invocation.getMethod());
    }

    @Test
    public void testGetArguments() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = {1, 2, 3};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);
        
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
        
        assertArrayEquals(args, invocation.getArguments());
    }

    @Test
    public void testIsVerified() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = {1, 2, 3};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);
        
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
        
        assertFalse(invocation.isVerified());
    }

    @Test
    public void testGetSequenceNumber() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = {1, 2, 3};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);
        
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
        
        assertEquals(sequenceNumber, (int) invocation.getSequenceNumber());
    }

    @Test
    public void testIsVerifiedInOrder() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = {1, 2, 3};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);
        
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
        
        assertFalse(invocation.isVerifiedInOrder());
    }

    @Test
    public void testEquals() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args1 = {1, 2, 3};
        Object[] args2 = {4, 5, 6};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);
        
        Invocation invocation1 = new Invocation(mock, method, args1, sequenceNumber, realMethod);
        Invocation invocation2 = new Invocation(mock, method, args1, sequenceNumber, realMethod);
        Invocation invocation3 = new Invocation(mock, method, args2, sequenceNumber, realMethod);
        
        assertTrue(invocation1.equals(invocation2));
        assertFalse(invocation1.equals(invocation3));
    }

    @Test
    public void testHashCode() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = {1, 2, 3};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);
        
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
        
        assertThrows(RuntimeException.class, () -> invocation.hashCode());
    }

    @Test
    public void testToString() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = {1, 2, 3};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);
        
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
        
        assertEquals("mock.ObjectName.methodName(1, 2, 3)", invocation.toString());
    }

    @Test
    public void testIsToString() {
        assertFalse(Invocation.isToString(mock(InvocationOnMock.class)));
    }

    @Test
    public void testIsValidException() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Class<?>[] exceptions = {Exception.class};
        Object[] args = {1, 2, 3};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);
        
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
        
        assertTrue(invocation.isValidException(new Exception()));
        assertFalse(invocation.isValidException(new RuntimeException()));
    }

    @Test
    public void testIsValidReturnType() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Class<?> returnType = String.class;
        Object[] args = {1, 2, 3};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);
        
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
        
        assertTrue(invocation.isValidReturnType(returnType));
        assertFalse(invocation.isValidReturnType(Integer.class));
    }

    @Test
    public void testIsVoid() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        method.getReturnType();
        Object[] args = {1, 2, 3};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);
        
        Invocation invocation1 = new Invocation(mock, method, args, sequenceNumber, realMethod);
        
        method = mock(MockitoMethod.class);
        when(method.getReturnType()).thenReturn(Void.TYPE);
        Invocation invocation2 = new Invocation(mock, method, args, sequenceNumber, realMethod);
        
        assertFalse(invocation1.isVoid());
        assertTrue(invocation2.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Class<?> returnType = String.class;
        Object[] args = {1, 2, 3};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);
        
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
        
        assertEquals("String", invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        String name = "methodName";
        Object[] args = {1, 2, 3};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);
        
        when(method.getName()).thenReturn(name);
        
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
        
        assertEquals(name, invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Class<?> returnType = Integer.TYPE;
        Object[] args = {1, 2, 3};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);
        
        Invocation invocation1 = new Invocation(mock, method, args, sequenceNumber, realMethod);
        
        method = mock(MockitoMethod.class);
        when(method.getReturnType()).thenReturn(Integer.class);
        Invocation invocation2 = new Invocation(mock, method, args, sequenceNumber, realMethod);
        
        assertTrue(invocation1.returnsPrimitive());
        assertFalse(invocation2.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = {1, 2, 3};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);
        Location location = new Location();
        
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
        
        assertNotNull(invocation.getLocation());
        assertEquals(location, invocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = {1, 2, 3};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);
        
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
        
        assertEquals(args.length, invocation.getArgumentsCount());
    }

    @Test
    public void testGetRawArguments() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = {1, 2, 3};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);
        
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
        
        assertArrayEquals(args, invocation.getRawArguments());
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = {1, 2, 3};
        Object[] rawArgs = {1, 2, 3};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);
        
        when(realMethod.invoke(mock, rawArgs)).thenReturn("result");
        
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
        
        assertEquals("result", invocation.callRealMethod());
    }

    @Test
    public void testToStringWithPrintSettings() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = {1, 2, 3};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);
        
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
        
        PrintSettings printSettings = new PrintSettings();
        
        assertEquals("mock.ObjectName.methodName(1, 2, 3)", invocation.toString(printSettings));
    }

    @Test
    public void testMarkVerified() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = {1, 2, 3};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);
        
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
        
        invocation.markVerified();
        
        assertTrue(invocation.isVerified());
    }

    @Test
    public void testMarkVerifiedInOrder() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = {1, 2, 3};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);
        
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
        
        invocation.markVerifiedInOrder();
        
        assertTrue(invocation.isVerified());
        assertTrue(invocation.isVerifiedInOrder());
    }
}