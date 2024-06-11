import static org.junit.Assert.*;
import org.junit.Test;

public class DelegatingMethodTest {

    // Expert 1
    @Test
    public void testGetExceptionTypes() {
        // Typical use case: The method has exception types
        Method method = getTestMethodWithException();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] expected = method.getExceptionTypes();
        
        assertArrayEquals(expected, delegatingMethod.getExceptionTypes());
    }

    // Expert 2
    @Test
    public void testGetJavaMethod() {
        // Typical use case: Get the Java method associated with the DelegatingMethod
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        assertEquals(method, delegatingMethod.getJavaMethod());
    }

    // Expert 3
    @Test
    public void testGetName() {
        // Typical use case: Get the name of the method
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        assertEquals(method.getName(), delegatingMethod.getName());
    }

    // Expert 1
    @Test
    public void testGetParameterTypes() {
        // Typical use case: The method has parameter types
        Method method = getTestMethodWithParameters();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] expected = method.getParameterTypes();
        
        assertArrayEquals(expected, delegatingMethod.getParameterTypes());
    }

    // Expert 2
    @Test
    public void testGetReturnType() {
        // Typical use case: Get the return type of the method
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        assertEquals(method.getReturnType(), delegatingMethod.getReturnType());
    }

    // Expert 3
    @Test
    public void testIsVarArgs() {
        // Typical use case: The method is not varargs
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        assertFalse(delegatingMethod.isVarArgs());
    }

    // Expert 1
    @Test
    public void testIsAbstract() {
        // Typical use case: The method is abstract
        Method method = getAbstractTestMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        assertTrue(delegatingMethod.isAbstract());
    }

    // Expert 2
    @Test
    public void testEquals() {
        // Typical use case: Two DelegatingMethods with equal internal methods
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);
        
        assertTrue(delegatingMethod1.equals(delegatingMethod2));
    }

    // Expert 3
    @Test
    public void testHashCode() {
        // Typical use case: Two DelegatingMethods with equal internal methods have equal hash codes
        Method method = getTestMethod();
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);
        
        assertEquals(delegatingMethod1.hashCode(), delegatingMethod2.hashCode());
    }

    /**
     * Helper method to create a test method with exception types
     */
    private Method getTestMethodWithException() {
        try {
            return DelegatingMethodTest.class.getMethod("testMethodWithException");
        } catch (NoSuchMethodException e) {
            // Method not found
            return null;
        }
    }

    /**
     * Helper method to create a test method without exception types or parameters
     */
    private Method getTestMethod() {
        try {
            return DelegatingMethodTest.class.getMethod("testMethod");
        } catch (NoSuchMethodException e) {
            // Method not found
            return null;
        }
    }
    
    /**
     * Helper method to create a test method with parameters
     */
    private Method getTestMethodWithParameters() {
        try {
            return DelegatingMethodTest.class.getMethod("testMethodWithParameters", String.class, int.class);
        } catch (NoSuchMethodException e) {
            // Method not found
            return null;
        }
    }

    /**
     * Helper method to create an abstract test method
     */
    private Method getAbstractTestMethod() {
        try {
            return DelegatingMethodTest.class.getMethod("abstractTestMethod");
        } catch (NoSuchMethodException e) {
            // Method not found
            return null;
        }
    }
}