import org.junit.Assert;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class InvocationTest {

    @Test
    public void testGetMock() {
        Object mock = new Object();
        Invocation invocation = new Invocation(mock, null, null, 0, null);
        
        Assert.assertEquals(mock, invocation.getMock());
    }
    
    @Test
    public void testGetMethod() {
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Invocation invocation = new Invocation(null, mockitoMethod, null, 0, null);
        
        Assert.assertEquals(mockitoMethod, invocation.getMethod());
    }
    
    @Test
    public void testGetArguments() {
        Object[] args = {1, "test", true};
        Invocation invocation = new Invocation(null, null, args, 0, null);
        
        Assert.assertArrayEquals(args, invocation.getArguments());
    }
    
    @Test
    public void testIsVerified() {
        Invocation invocation = new Invocation(null, null, null, 0, null);
        
        Assert.assertFalse(invocation.isVerified());
    }
    
    @Test
    public void testGetSequenceNumber() {
        int sequenceNumber = 1;
        Invocation invocation = new Invocation(null, null, null, sequenceNumber, null);
        
        Assert.assertEquals(Integer.valueOf(sequenceNumber), invocation.getSequenceNumber());
    }
    
    @Test
    public void testIsVerifiedInOrder() {
        Invocation invocation = new Invocation(null, null, null, 0, null);
        
        Assert.assertFalse(invocation.isVerifiedInOrder());
    }
    
    @Test
    public void testEquals() {
        Object mock1 = new Object();
        Object mock2 = new Object();
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args1 = {1, "test", true};
        Object[] args2 = {1, "test", false};
        
        Invocation invocation1 = new Invocation(mock1, method, args1, 0, null);
        Invocation invocation2 = new Invocation(mock1, method, args1, 0, null);
        Invocation invocation3 = new Invocation(mock2, method, args1, 0, null);
        Invocation invocation4 = new Invocation(mock1, method, args2, 0, null);
        
        Assert.assertTrue(invocation1.equals(invocation2));
        Assert.assertFalse(invocation1.equals(invocation3));
        Assert.assertFalse(invocation1.equals(invocation4));
    }
    
    @Test
    public void testHashCode() {
        Invocation invocation = new Invocation(null, null, null, 0, null);
        
        try {
            invocation.hashCode();
            Assert.fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Success
        }
    }
    
    @Test
    public void testToString() {
        MockitoMethod method = mock(MockitoMethod.class);
        when(method.getName()).thenReturn("test");
        Object[] args = {1, "test", true};
        Invocation invocation = new Invocation(null, method, args, 0, null);
        
        String expected = "mock.test(1, \"test\", true)";
        Assert.assertEquals(expected, invocation.toString());
    }
    
    @Test
    public void testArgumentsToMatchers() {
        Object[] args = {1, "test", true};
        Invocation invocation = new Invocation(null, null, args, 0, null);
        
        Assert.assertEquals(3, invocation.argumentsToMatchers().size());
    }
    
    @Test
    public void testIsToString() {
        MockitoMethod method = mock(MockitoMethod.class);
        when(method.getName()).thenReturn("toString");
        Invocation invocation = new Invocation(null, method, null, 0, null);
        
        Assert.assertTrue(invocation.isToString(invocation));
    }
    
    @Test
    public void testIsValidException() {
        MockitoMethod method = mock(MockitoMethod.class);
        when(method.getExceptionTypes()).thenReturn(new Class[] {Exception.class});
        Throwable throwable = new Exception();
        Invocation invocation = new Invocation(null, method, null, 0, null);
        
        Assert.assertTrue(invocation.isValidException(throwable));
    }
    
    @Test
    public void testIsValidReturnType() {
        Class<?> returnType = int.class;
        Class<?> clazz = Integer.class;
        MockitoMethod method = mock(MockitoMethod.class);
        when(method.getReturnType()).thenReturn(returnType);
        Invocation invocation = new Invocation(null, method, null, 0, null);
        
        Assert.assertTrue(invocation.isValidReturnType(clazz));
    }
    
    @Test
    public void testIsVoid() {
        MockitoMethod method = mock(MockitoMethod.class);
        when(method.getReturnType()).thenReturn(Void.TYPE);
        Invocation invocation = new Invocation(null, method, null, 0, null);
        
        Assert.assertTrue(invocation.isVoid());
    }
    
    @Test
    public void testPrintMethodReturnType() {
        MockitoMethod method = mock(MockitoMethod.class);
        when(method.getReturnType()).thenReturn(int.class);
        Invocation invocation = new Invocation(null, method, null, 0, null);
        
        Assert.assertEquals("int", invocation.printMethodReturnType());
    }
    
    @Test
    public void testGetMethodName() {
        String methodName = "test";
        MockitoMethod method = mock(MockitoMethod.class);
        when(method.getName()).thenReturn(methodName);
        Invocation invocation = new Invocation(null, method, null, 0, null);
        
        Assert.assertEquals(methodName, invocation.getMethodName());
    }
    
    @Test
    public void testReturnsPrimitive() {
        MockitoMethod method = mock(MockitoMethod.class);
        when(method.getReturnType()).thenReturn(int.class);
        Invocation invocation = new Invocation(null, method, null, 0, null);
        
        Assert.assertTrue(invocation.returnsPrimitive());
    }
    
    @Test
    public void testGetLocation() {
        Location location = new Location();
        Invocation invocation = new Invocation(null, null, null, 0, null);
        invocation.setLocation(location);
        
        Assert.assertEquals(location, invocation.getLocation());
    }
    
    @Test
    public void testGetArgumentsCount() {
        Object[] args = {1, "test", true};
        Invocation invocation = new Invocation(null, null, args, 0, null);
        
        Assert.assertEquals(3, invocation.getArgumentsCount());
    }
    
    @Test
    public void testGetRawArguments() {
        Object[] args = {1, "test", true};
        Invocation invocation = new Invocation(null, null, args, 0, null);
        
        Assert.assertEquals(args, invocation.getRawArguments());
    }
    
    @Test(expected = Throwable.class)
    public void testCallRealMethod() throws Throwable {
        RealMethod realMethod = mock(RealMethod.class);
        Invocation invocation = new Invocation(null, null, null, 0, realMethod);
        
        invocation.callRealMethod();
        
        verify(realMethod).invoke(anyObject(), any(Object[].class));
    }
    
    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = new PrintSettings();
        printSettings.setMultiline(true);
        
        MockitoMethod method = mock(MockitoMethod.class);
        when(method.getName()).thenReturn("test");
        Object[] args = {1, "test", true};
        Invocation invocation = new Invocation(null, method, args, 0, null);
        
        String expected = "mock.test(\n"
                        + "  1,\n"
                        + "  \"test\",\n"
                        + "  true\n"
                        + ")";
        Assert.assertEquals(expected, invocation.toString(printSettings));
    }
    
    @Test
    public void testMarkVerified() {
        Invocation invocation = new Invocation(null, null, null, 0, null);
        
        invocation.markVerified();
        
        Assert.assertTrue(invocation.isVerified());
    }
    
    @Test
    public void testMarkVerifiedInOrder() {
        Invocation invocation = new Invocation(null, null, null, 0, null);
        
        invocation.markVerifiedInOrder();
        
        Assert.assertTrue(invocation.isVerified());
        Assert.assertTrue(invocation.isVerifiedInOrder());
    }
}