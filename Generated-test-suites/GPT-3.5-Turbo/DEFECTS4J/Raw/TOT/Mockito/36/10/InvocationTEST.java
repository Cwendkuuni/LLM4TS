import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.invocation.InvocationOnMock;

public class InvocationTest {

    @Test
    public void testGetMock() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[]{1, 2, 3};
        
        Invocation invocation = new Invocation(mock, method, args, 1, new RealMethod());
        
        assertEquals(mock, invocation.getMock());
    }
    
    @Test
    public void testGetMethod() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[]{1, 2, 3};
        
        Invocation invocation = new Invocation(mock, method, args, 1, new RealMethod());
        
        assertEquals(method, invocation.getMethod());
    }
    
    @Test
    public void testGetArguments() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[]{1, 2, 3};
        
        Invocation invocation = new Invocation(mock, method, args, 1, new RealMethod());
        
        assertArrayEquals(args, invocation.getArguments());
    }
    
    @Test
    public void testIsVerified() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[]{1, 2, 3};
        
        Invocation invocation = new Invocation(mock, method, args, 1, new RealMethod());
        
        assertFalse(invocation.isVerified());
        
        invocation.markVerified();
        
        assertTrue(invocation.isVerified());
    }
    
    @Test
    public void testGetSequenceNumber() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[]{1, 2, 3};
        
        Invocation invocation = new Invocation(mock, method, args, 1, new RealMethod());
        
        assertEquals(Integer.valueOf(1), invocation.getSequenceNumber());
    }
    
    @Test
    public void testIsVerifiedInOrder() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[]{1, 2, 3};
        
        Invocation invocation = new Invocation(mock, method, args, 1, new RealMethod());
        
        assertFalse(invocation.isVerifiedInOrder());
        
        invocation.markVerifiedInOrder();
        
        assertTrue(invocation.isVerifiedInOrder());
    }
    
    @Test
    public void testEquals() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args1 = new Object[]{1, 2, 3};
        Object[] args2 = new Object[]{4, 5};
        
        Invocation invocation1 = new Invocation(mock, method, args1, 1, new RealMethod());
        Invocation invocation2 = new Invocation(mock, method, args1, 1, new RealMethod());
        Invocation invocation3 = new Invocation(mock, method, args2, 1, new RealMethod());
        
        assertTrue(invocation1.equals(invocation2));
        assertFalse(invocation1.equals(invocation3));
    }
    
    @Test
    public void testHashCode() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[]{1, 2, 3};
        
        Invocation invocation = new Invocation(mock, method, args, 1, new RealMethod());
        
        try {
            invocation.hashCode();
            fail("Expected exception to be thrown");
        } catch (RuntimeException e) {
            assertEquals("hashCode() is not implemented", e.getMessage());
        }
    }
    
    @Test
    public void testToString() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[]{1, 2, 3};
        
        Invocation invocation = new Invocation(mock, method, args, 1, new RealMethod());
        
        String expected = "org.mockito.internal.invocation.Invocation.$EnhancerByMockitoWithCGLIB$$xxx.toString(1, 2, 3)";
        assertEquals(expected, invocation.toString());
    }
    
    @Test
    public void testIsToString() {
        MockitoMethod method = new MockitoMethod();
        method.setName("toString");
        
        InvocationOnMock invocation = new InvocationOnMock() {
            public Object[] getArguments() { return null; }
            public Object getMock() { return null; }
            public java.lang.reflect.Method getMethod() { return method; }
            public void callRealMethod() throws Throwable {}
            public boolean isToString() { return true; }
        };
        
        assertTrue(Invocation.isToString(invocation));
    }
    
    @Test
    public void testIsValidException() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        method.setExceptionTypes(new Class[]{RuntimeException.class, IOException.class});
        
        Invocation invocation = new Invocation(mock, method, null, 1, new RealMethod());
        
        assertTrue(invocation.isValidException(new RuntimeException()));
        assertTrue(invocation.isValidException(new IOException()));
        assertFalse(invocation.isValidException(new Exception()));
    }
    
    @Test
    public void testIsValidReturnType() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        method.setReturnType(String.class);
        
        Invocation invocation1 = new Invocation(mock, method, null, 1, new RealMethod());
        assertTrue(invocation1.isValidReturnType(String.class));
        assertFalse(invocation1.isValidReturnType(Integer.class));
        
        MockitoMethod method2 = new MockitoMethod();
        method2.setReturnType(int.class);
        
        Invocation invocation2 = new Invocation(mock, method2, null, 1, new RealMethod());
        assertTrue(invocation2.isValidReturnType(int.class));
        assertFalse(invocation2.isValidReturnType(Integer.class));
    }
    
    @Test
    public void testIsVoid() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        method.setReturnType(Void.TYPE);
        
        Invocation invocation1 = new Invocation(mock, method, null, 1, new RealMethod());
        assertTrue(invocation1.isVoid());
        
        MockitoMethod method2 = new MockitoMethod();
        method2.setReturnType(String.class);
        
        Invocation invocation2 = new Invocation(mock, method2, null, 1, new RealMethod());
        assertFalse(invocation2.isVoid());
    }
    
    @Test
    public void testPrintMethodReturnType() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        method.setReturnType(String.class);
        
        Invocation invocation = new Invocation(mock, method, null, 1, new RealMethod());
        
        assertEquals("String", invocation.printMethodReturnType());
    }
    
    @Test
    public void testGetMethodName() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        method.setName("methodName");
        
        Invocation invocation = new Invocation(mock, method, null, 1, new RealMethod());
        
        assertEquals("methodName", invocation.getMethodName());
    }
    
    @Test
    public void testReturnsPrimitive() {
        Object mock = new Object();
        MockitoMethod method1 = new MockitoMethod();
        method1.setReturnType(int.class);
        
        Invocation invocation1 = new Invocation(mock, method1, null, 1, new RealMethod());
        assertTrue(invocation1.returnsPrimitive());
        
        MockitoMethod method2 = new MockitoMethod();
        method2.setReturnType(Integer.class);
        
        Invocation invocation2 = new Invocation(mock, method2, null, 1, new RealMethod());
        assertFalse(invocation2.returnsPrimitive());
    }
    
    @Test
    public void testGetLocation() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[]{1, 2, 3};
        
        Invocation invocation = new Invocation(mock, method, args, 1, new RealMethod());
        
        assertNotNull(invocation.getLocation());
    }
    
    @Test
    public void testGetArgumentsCount() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[]{1, 2, 3};
        
        Invocation invocation = new Invocation(mock, method, args, 1, new RealMethod());
        
        assertEquals(3, invocation.getArgumentsCount());
    }
    
    @Test
    public void testGetRawArguments() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[]{1, 2, 3};
        
        Invocation invocation = new Invocation(mock, method, args, 1, new RealMethod());
        
        assertArrayEquals(args, invocation.getRawArguments());
    }
    
    @Test
    public void testCallRealMethod() throws Throwable {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[]{"input"};
        RealMethod realMethod = new RealMethod();
        
        Invocation invocation = new Invocation(mock, method, args, 1, realMethod);
        
        assertEquals("output", invocation.callRealMethod());
    }
    
    @Test
    public void testToStringWithPrintSettings() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[]{"input"};
        
        Invocation invocation = new Invocation(mock, method, args, 1, new RealMethod());
        PrintSettings printSettings = new PrintSettings();
        
        String expected = "org.mockito.internal.invocation.Invocation.$EnhancerByMockitoWithCGLIB$$xxx.toString(0)";
        assertEquals(expected, invocation.toString(printSettings));
    }
    
    @Test
    public void testMarkVerified() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[]{1, 2, 3};
        
        Invocation invocation = new Invocation(mock, method, args, 1, new RealMethod());
        
        assertFalse(invocation.isVerified());
        
        invocation.markVerified();
        
        assertTrue(invocation.isVerified());
    }
    
    @Test
    public void testMarkVerifiedInOrder() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[]{1, 2, 3};
        
        Invocation invocation = new Invocation(mock, method, args, 1, new RealMethod());
        
        assertFalse(invocation.isVerified());
        assertFalse(invocation.isVerifiedInOrder());
        
        invocation.markVerifiedInOrder();
        
        assertTrue(invocation.isVerified());
        assertTrue(invocation.isVerifiedInOrder());
    }
}