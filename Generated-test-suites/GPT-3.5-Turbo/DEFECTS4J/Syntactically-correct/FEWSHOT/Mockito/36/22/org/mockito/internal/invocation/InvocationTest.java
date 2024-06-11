package org.mockito.internal.invocation;

import static org.junit.Assert.*;
import org.junit.Test;

public class InvocationTest {
    
    @Test
    public void testConstructor() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod("methodName", null);
        Object[] args = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();
        
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
        
        assertEquals(mock, invocation.getMock());
        assertEquals(method, invocation.getMethod());
        assertArrayEquals(args, invocation.getArguments());
        assertNull(invocation.getRawArguments());
        assertEquals(sequenceNumber, (int) invocation.getSequenceNumber());
        assertFalse(invocation.isVerified());
        assertFalse(invocation.isVerifiedInOrder());
    }
    
    @Test
    public void testEquals() {
        Object mock1 = new Object();
        MockitoMethod method1 = new MockitoMethod("methodName1", null);
        Object[] args1 = new Object[]{1, 2, 3};
        int sequenceNumber1 = 1;
        RealMethod realMethod1 = new RealMethod();
        
        Object mock2 = new Object();
        MockitoMethod method2 = new MockitoMethod("methodName2", null);
        Object[] args2 = new Object[]{4, 5, 6};
        int sequenceNumber2 = 2;
        RealMethod realMethod2 = new RealMethod();
        
        Invocation invocation1 = new Invocation(mock1, method1, args1, sequenceNumber1, realMethod1);
        Invocation invocation2 = new Invocation(mock2, method2, args2, sequenceNumber2, realMethod2);
        
        assertFalse(invocation1.equals(null));
        assertFalse(invocation1.equals(new Object()));
        assertFalse(invocation1.equals(invocation2));
        assertTrue(invocation1.equals(invocation1));
    }
    
    @Test
    public void testHashCode() {
        Invocation invocation = new Invocation(null, null, null, 0, null);
        
        try {
            invocation.hashCode();
            fail("Expected exception not thrown");
        } catch (RuntimeException e) {
            assertEquals("hashCode() is not implemented", e.getMessage());
        }
    }
    
    @Test
    public void testToString() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod("methodName", null);
        Object[] args = new Object[]{1, 2, 3};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();
        
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
        
        assertEquals("methodName[1, 2, 3]", invocation.toString());
    }
    
    @Test
    public void testIsToString() {
        MockitoMethod method = new MockitoMethod("toString", null);
        
        Invocation invocation = new Invocation(null, method, null, 0, null);
        
        assertTrue(invocation.isToString(invocation));
        assertFalse(invocation.isToString(null));
    }
    
    @Test
    public void testIsValidException() {
        MockitoMethod method = new MockitoMethod("methodName", new Class[]{IllegalArgumentException.class});
        
        Invocation invocation = new Invocation(null, method, null, 0, null);
        
        assertTrue(invocation.isValidException(new IllegalArgumentException()));
        assertFalse(invocation.isValidException(new NullPointerException()));
    }
    
    @Test
    public void testIsValidReturnType() {
        MockitoMethod method = new MockitoMethod("methodName", null);
        method.setReturnType(int.class);
        
        Invocation invocation = new Invocation(null, method, null, 0, null);
        
        assertTrue(invocation.isValidReturnType(int.class));
        assertTrue(invocation.isValidReturnType(Integer.class));
        assertFalse(invocation.isValidReturnType(double.class));
    }
    
    @Test
    public void testIsVoid() {
        MockitoMethod method1 = new MockitoMethod("", int.class);
        MockitoMethod method2 = new MockitoMethod("", Void.TYPE);
        
        Invocation invocation1 = new Invocation(null, method1, null, 0, null);
        Invocation invocation2 = new Invocation(null, method2, null, 0, null);
        
        assertFalse(invocation1.isVoid());
        assertTrue(invocation2.isVoid());
    }
    
    @Test
    public void testPrintMethodReturnType() {
        MockitoMethod method = new MockitoMethod("", int.class);
        
        Invocation invocation = new Invocation(null, method, null, 0, null);
        
        assertEquals("int", invocation.printMethodReturnType());
    }
    
    @Test
    public void testGetMethodName() {
        MockitoMethod method = new MockitoMethod("methodName", null);
        
        Invocation invocation = new Invocation(null, method, null, 0, null);
        
        assertEquals("methodName", invocation.getMethodName());
    }
    
    @Test
    public void testReturnsPrimitive() {
        MockitoMethod method1 = new MockitoMethod("", int.class);
        MockitoMethod method2 = new MockitoMethod("", Integer.class);
        
        Invocation invocation1 = new Invocation(null, method1, null, 0, null);
        Invocation invocation2 = new Invocation(null, method2, null, 0, null);
        
        assertTrue(invocation1.returnsPrimitive());
        assertFalse(invocation2.returnsPrimitive());
    }
    
    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation(null, null, null, 0, null);
        
        assertNotNull(invocation.getLocation());
    }
    
    @Test
    public void testGetArgumentsCount() {
        Invocation invocation = new Invocation(null, null, new Object[]{1, 2}, 0, null);
        
        assertEquals(2, invocation.getArgumentsCount());
    }
    
    @Test
    public void testGetRawArguments() {
        Invocation invocation = new Invocation(null, null, new Object[]{1, 2}, 0, null);
        
        assertArrayEquals(new Object[]{1, 2}, invocation.getRawArguments());
    }
    
    @Test
    public void testCallRealMethod() throws Throwable {
        MockitoMethod method = new MockitoMethod("methodName", null);
        
        Invocation invocation = new Invocation(null, method, null, 0, null);
        
        assertNull(invocation.callRealMethod());
    }
    
    @Test
    public void testToStringWithPrintSettings() {
        MockitoMethod method = new MockitoMethod("methodName", null);
        
        Invocation invocation = new Invocation(null, method, null, 0, null);
        
        assertEquals("methodName", invocation.toString(new PrintSettings()));
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
        
        assertFalse(invocation.isVerified());
        assertFalse(invocation.isVerifiedInOrder());
        
        invocation.markVerifiedInOrder();
        
        assertTrue(invocation.isVerified());
        assertTrue(invocation.isVerifiedInOrder());
    }
}