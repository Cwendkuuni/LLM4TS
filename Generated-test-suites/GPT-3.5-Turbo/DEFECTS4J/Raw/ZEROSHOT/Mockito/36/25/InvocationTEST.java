import org.junit.Test;
import static org.junit.Assert.*;

public class InvocationTest {

    @Test
    public void testGetMock() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[]{};
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, 1, realMethod);
        
        assertEquals(mock, invocation.getMock());
    }
    
    @Test
    public void testGetMethod() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[]{};
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, 1, realMethod);
        
        assertEquals(method, invocation.getMethod());
    }
    
    @Test
    public void testGetArguments() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[]{1, "test", true};
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, 1, realMethod);
        
        assertArrayEquals(args, invocation.getArguments());
    }
    
    @Test
    public void testIsVerified() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[]{};
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, 1, realMethod);
        
        assertFalse(invocation.isVerified());
    }
    
    @Test
    public void testGetSequenceNumber() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[]{};
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, 1, realMethod);
        
        assertEquals(Integer.valueOf(1), invocation.getSequenceNumber());
    }
    
    @Test
    public void testIsVerifiedInOrder() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[]{};
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, 1, realMethod);
        
        assertFalse(invocation.isVerifiedInOrder());
    }
    
    @Test
    public void testEquals() {
        Object mock1 = new Object();
        MockitoMethod method1 = new MockitoMethod();
        Object[] args1 = new Object[]{1, "test", true};
        RealMethod realMethod1 = new RealMethod();
        Invocation invocation1 = new Invocation(mock1, method1, args1, 1, realMethod1);
        
        Object mock2 = new Object();
        MockitoMethod method2 = new MockitoMethod();
        Object[] args2 = new Object[]{1, "test", true};
        RealMethod realMethod2 = new RealMethod();
        Invocation invocation2 = new Invocation(mock2, method2, args2, 1, realMethod2);
        
        assertTrue(invocation1.equals(invocation2));
    }
    
    @Test
    public void testHashCode() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[]{1, "test", true};
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, 1, realMethod);
        
        assertThrows(RuntimeException.class, () -> {
            invocation.hashCode();
        });
    }
    
    @Test
    public void testToString() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[]{1, "test", true};
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, 1, realMethod);
        
        String expected = "org.mockito.internal.util.MockUtil$MockName@12345.toString()";
        
        assertEquals(expected, invocation.toString());
    }
    
    @Test
    public void testIsToString() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[]{};
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, 1, realMethod);
        
        assertFalse(Invocation.isToString(invocation));
    }
    
    @Test
    public void testIsValidException() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[]{};
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, 1, realMethod);
        
        assertFalse(invocation.isValidException(new Exception()));
    }
    
    @Test
    public void testIsValidReturnType() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[]{};
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, 1, realMethod);
        
        assertTrue(invocation.isValidReturnType(Object.class));
    }
    
    @Test
    public void testIsVoid() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[]{};
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, 1, realMethod);
        
        assertFalse(invocation.isVoid());
    }
    
    @Test
    public void testPrintMethodReturnType() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[]{};
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, 1, realMethod);
        
        assertEquals("Object", invocation.printMethodReturnType());
    }
    
    @Test
    public void testGetMethodName() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[]{};
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, 1, realMethod);
        
        assertEquals("methodName", invocation.getMethodName());
    }
    
    @Test
    public void testReturnsPrimitive() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[]{};
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, 1, realMethod);
        
        assertFalse(invocation.returnsPrimitive());
    }
    
    @Test
    public void testGetLocation() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[]{};
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, 1, realMethod);
        
        assertNotNull(invocation.getLocation());
    }
    
    @Test
    public void testGetArgumentsCount() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[]{1, "test", true};
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, 1, realMethod);
        
        assertEquals(args.length, invocation.getArgumentsCount());
    }
    
    @Test
    public void testGetRawArguments() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[]{1, "test", true};
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, 1, realMethod);
        
        assertArrayEquals(args, invocation.getRawArguments());
    }
    
    @Test
    public void testCallRealMethod() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[]{};
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, 1, realMethod);
        
        assertThrows(Throwable.class, () -> {
            invocation.callRealMethod();
        });
    }
    
    @Test
    public void testToStringWithPrintSettings() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[]{1, "test", true};
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, 1, realMethod);
        
        PrintSettings printSettings = new PrintSettings();
        
        String expected = "org.mockito.internal.util.MockUtil$MockName@12345.toString()";
        
        assertEquals(expected, invocation.toString(printSettings));
    }
    
    @Test
    public void testMarkVerified() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[]{};
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, 1, realMethod);
        
        invocation.markVerified();
        
        assertTrue(invocation.isVerified());
    }
    
    @Test
    public void testMarkVerifiedInOrder() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[]{};
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, 1, realMethod);
        
        invocation.markVerifiedInOrder();
        
        assertTrue(invocation.isVerified());
        assertTrue(invocation.isVerifiedInOrder());
    }
}