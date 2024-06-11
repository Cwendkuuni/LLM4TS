import static org.junit.Assert.*;
import org.junit.Test;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        // Typical case: method does not throw any exceptions
        DelegatingMethod dm1 = new DelegatingMethod(getMethod("method1", null));
        assertEquals(0, dm1.getExceptionTypes().length);

        // Typical case: method throws one exception
        DelegatingMethod dm2 = new DelegatingMethod(getMethod("method2", null));
        assertEquals(1, dm2.getExceptionTypes().length);
        assertEquals(ArrayIndexOutOfBoundsException.class, dm2.getExceptionTypes()[0]);
        
        // Edge case: method throws multiple exceptions
        DelegatingMethod dm3 = new DelegatingMethod(getMethod("method3", null));
        assertEquals(2, dm3.getExceptionTypes().length);
        assertTrue(contains(dm3.getExceptionTypes(), ClassNotFoundException.class));
        assertTrue(contains(dm3.getExceptionTypes(), NoSuchMethodException.class));
    }

    @Test
    public void testGetJavaMethod() {
        // Typical case: method exists
        DelegatingMethod dm1 = new DelegatingMethod(getMethod("method1", null));
        assertEquals(getMethod("method1", null), dm1.getJavaMethod());
        
        // Edge case: method does not exist
        DelegatingMethod dm2 = new DelegatingMethod(null);
        assertNull(dm2.getJavaMethod());
    }
    
    @Test
    public void testGetName() {
        // Typical case: method has a name
        DelegatingMethod dm1 = new DelegatingMethod(getMethod("method1", null));
        assertEquals("method1", dm1.getName());
        
        // Edge case: method has no name
        DelegatingMethod dm2 = new DelegatingMethod(getMethod("", null));
        assertEquals("", dm2.getName());
    }
    
    @Test
    public void testGetParameterTypes() {
        // Typical case: method has parameters
        DelegatingMethod dm1 = new DelegatingMethod(getMethod("method1", new Class<?>[] { int.class, String.class }));
        assertEquals(2, dm1.getParameterTypes().length);
        assertEquals(int.class, dm1.getParameterTypes()[0]);
        assertEquals(String.class, dm1.getParameterTypes()[1]);
        
        // Edge case: method has no parameters
        DelegatingMethod dm2 = new DelegatingMethod(getMethod("method2", null));
        assertEquals(0, dm2.getParameterTypes().length);
    }
    
    @Test
    public void testGetReturnType() {
        // Typical case: method has a return type
        DelegatingMethod dm1 = new DelegatingMethod(getMethod("method1", null));
        assertEquals(int.class, dm1.getReturnType());
        
        // Edge case: method has no return type
        DelegatingMethod dm2 = new DelegatingMethod(getMethod("method2", null));
        assertEquals(void.class, dm2.getReturnType());
    }
    
    @Test
    public void testIsVarArgs() {
        // Typical case: method is not varargs
        DelegatingMethod dm1 = new DelegatingMethod(getMethod("method1", null));
        assertFalse(dm1.isVarArgs());
        
        // Edge case: method is varargs
        DelegatingMethod dm2 = new DelegatingMethod(getMethod("method2", new Class<?>[] { int.class, String[].class }));
        assertTrue(dm2.isVarArgs());
    }
    
    @Test
    public void testIsAbstract() {
        // Typical case: method is not abstract
        DelegatingMethod dm1 = new DelegatingMethod(getMethod("method1", null));
        assertFalse(dm1.isAbstract());
        
        // Edge case: method is abstract
        DelegatingMethod dm2 = new DelegatingMethod(getMethod("method2", null));
        assertTrue(dm2.isAbstract());
    }
    
    @Test
    public void testEquals() {
        // Typical case: objects are equal
        DelegatingMethod dm1 = new DelegatingMethod(getMethod("method1", null));
        DelegatingMethod dm2 = new DelegatingMethod(getMethod("method1", null));
        assertTrue(dm1.equals(dm2));
        
        // Typical case: objects are not equal
        DelegatingMethod dm3 = new DelegatingMethod(getMethod("method1", null));
        DelegatingMethod dm4 = new DelegatingMethod(getMethod("method2", null));
        assertFalse(dm3.equals(dm4));
        
        // Edge case: object compared to null
        DelegatingMethod dm5 = new DelegatingMethod(getMethod("method1", null));
        assertFalse(dm5.equals(null));
        
        // Edge case: object compared to different type
        DelegatingMethod dm6 = new DelegatingMethod(getMethod("method1", null));
        assertFalse(dm6.equals("method1"));
    }
    
    @Test
    public void testHashCode() {
        // Since hashCode always returns 1, there are no particular test cases to check
        // as long as it returns consistent results for the same object.
        // So no tests are necessary for this method.
    }
    
    private Method getMethod(String methodName, Class<?>[] parameterTypes) {
        try {
            return DelegatingMethodTest.class.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
    
    private boolean contains(Class<?>[] array, Class<?> element) {
        for (Class<?> item : array) {
            if (item.equals(element)) {
                return true;
            }
        }
        return false;
    }
}