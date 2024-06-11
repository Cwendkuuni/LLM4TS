import org.junit.Test;
import static org.junit.Assert.*;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        // Test if no exception types are returned
        DelegatingMethod delegatingMethod = new DelegatingMethod(getTestMethod("noExceptionTypes"));
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        assertEquals(0, exceptionTypes.length);
        
        // Test if exception types are returned
        delegatingMethod = new DelegatingMethod(getTestMethod("exceptionTypes"));
        exceptionTypes = delegatingMethod.getExceptionTypes();
        assertEquals(2, exceptionTypes.length);
        assertTrue(exceptionTypes[0].equals(Exception.class));
        assertTrue(exceptionTypes[1].equals(RuntimeException.class));
    }
    
    @Test
    public void testGetJavaMethod() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(getTestMethod("getJavaMethod"));
        Method javaMethod = delegatingMethod.getJavaMethod();
        assertNotNull(javaMethod);
        assertTrue(javaMethod.getName().equals("getJavaMethod"));
    }
    
    @Test
    public void testGetName() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(getTestMethod("getName"));
        String name = delegatingMethod.getName();
        assertNotNull(name);
        assertTrue(name.equals("getName"));
    }
    
    @Test
    public void testGetParameterTypes() {
        // Test with no parameters
        DelegatingMethod delegatingMethod = new DelegatingMethod(getTestMethod("noParameters"));
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        assertEquals(0, parameterTypes.length);
        
        // Test with parameters
        delegatingMethod = new DelegatingMethod(getTestMethod("parameters"));
        parameterTypes = delegatingMethod.getParameterTypes();
        assertEquals(1, parameterTypes.length);
        assertTrue(parameterTypes[0].equals(int.class));
    }
    
    @Test
    public void testGetReturnType() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(getTestMethod("getReturnType"));
        Class<?> returnType = delegatingMethod.getReturnType();
        assertNotNull(returnType);
        assertTrue(returnType.equals(Void.class));
    }
    
    @Test
    public void testIsVarArgs() {
        // Test if method is varargs
        DelegatingMethod delegatingMethod = new DelegatingMethod(getTestMethod("isVarArgs"));
        assertTrue(delegatingMethod.isVarArgs());
        
        // Test if method is not varargs
        delegatingMethod = new DelegatingMethod(getTestMethod("notVarArgs"));
        assertFalse(delegatingMethod.isVarArgs());
    }
    
    @Test
    public void testIsAbstract() {
        // Test if method is abstract
        DelegatingMethod delegatingMethod = new DelegatingMethod(getTestMethod("isAbstract"));
        assertTrue(delegatingMethod.isAbstract());
        
        // Test if method is not abstract
        delegatingMethod = new DelegatingMethod(getTestMethod("notAbstract"));
        assertFalse(delegatingMethod.isAbstract());
    }
    
    @Test
    public void testEquals() {
        // Test if equals returns true for same DelegatingMethod instance
        DelegatingMethod delegatingMethod = new DelegatingMethod(getTestMethod("equals"));
        assertTrue(delegatingMethod.equals(delegatingMethod));
        
        // Test if equals returns true for equal DelegatingMethod instances
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(getTestMethod("equals"));
        assertTrue(delegatingMethod.equals(delegatingMethod2));
        
        // Test if equals returns true for Method instance equal to DelegatingMethod
        Method method = getTestMethod("equals");
        assertTrue(delegatingMethod.equals(method));
        
        // Test if equals returns false for different DelegatingMethod instances
        DelegatingMethod delegatingMethod3 = new DelegatingMethod(getTestMethod("notEquals"));
        assertFalse(delegatingMethod.equals(delegatingMethod3));
        
        // Test if equals returns false for different Method instance
        Method method2 = getTestMethod("notEquals");
        assertFalse(delegatingMethod.equals(method2));
        
        // Test if equals returns false for null
        assertFalse(delegatingMethod.equals(null));
    }
    
    @Test
    public void testHashCode() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(getTestMethod("hashCode"));
        assertEquals(1, delegatingMethod.hashCode());
    }
    
    private Method getTestMethod(String methodName) {
        try {
            return DelegatingMethod.class.getDeclaredMethod(methodName);
        } catch (NoSuchMethodException e) {
            fail("Method not found: " + methodName);
        }
        return null;
    }
    
    private void noExceptionTypes() {}
    
    private void exceptionTypes() throws Exception, RuntimeException {}
    
    private void getJavaMethod() {}
    
    private void getName() {}
    
    private void noParameters() {}
    
    private void parameters(int parameter) {}
    
    private void getReturnType() {}
    
    private void isVarArgs(String... args) {}
    
    private void notVarArgs(String arg) {}
    
    private abstract void isAbstract();
    
    private void notAbstract() {}
    
    private void equals() {}
    
    private void notEquals() {}
    
    private void hashCode() {}
}