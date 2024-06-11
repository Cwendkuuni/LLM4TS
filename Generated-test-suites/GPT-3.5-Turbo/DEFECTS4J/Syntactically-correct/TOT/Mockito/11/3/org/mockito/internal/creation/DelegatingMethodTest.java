import org.junit.Test;
import static org.junit.Assert.*;

public class DelegatingMethodTest {
    
    private final Method sampleMethod;
    private final DelegatingMethod delegatingMethod;
    
    public DelegatingMethodTest() throws NoSuchMethodException {
        // Get a sample method for testing
        sampleMethod = MyClass.class.getMethod("sampleMethod", String.class, int.class);
        delegatingMethod = new DelegatingMethod(sampleMethod);
    }
    
    @Test
    public void testGetExceptionTypes() {
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        assertNotNull(exceptionTypes);
        assertEquals(0, exceptionTypes.length);
    }
    
    @Test
    public void testGetJavaMethod() {
        Method javaMethod = delegatingMethod.getJavaMethod();
        assertNotNull(javaMethod);
        assertEquals(sampleMethod, javaMethod);
    }
    
    @Test
    public void testGetName() {
        String name = delegatingMethod.getName();
        assertNotNull(name);
        assertEquals("sampleMethod", name);
    }
    
    @Test
    public void testGetParameterTypes() {
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        assertNotNull(parameterTypes);
        assertEquals(2, parameterTypes.length);
        assertEquals(String.class, parameterTypes[0]);
        assertEquals(int.class, parameterTypes[1]);
    }
    
    @Test
    public void testGetReturnType() {
        Class<?> returnType = delegatingMethod.getReturnType();
        assertNotNull(returnType);
        assertEquals(void.class, returnType);
    }
    
    @Test
    public void testIsVarArgs() {
        assertFalse(delegatingMethod.isVarArgs());
    }
    
    @Test
    public void testIsAbstract() {
        assertFalse(delegatingMethod.isAbstract());
    }
    
    @Test
    public void testEquals_SameObject() {
        assertTrue(delegatingMethod.equals(delegatingMethod));
    }
    
    @Test
    public void testEquals_SameMethod() throws NoSuchMethodException {
        Method sameMethod = MyClass.class.getMethod("sampleMethod", String.class, int.class);
        DelegatingMethod sameDelegatingMethod = new DelegatingMethod(sameMethod);
        assertTrue(delegatingMethod.equals(sameDelegatingMethod));
        assertTrue(delegatingMethod.equals(sampleMethod));
    }
    
    @Test
    public void testEquals_DifferentMethod() throws NoSuchMethodException {
        Method differentMethod = MyClass.class.getMethod("anotherMethod");
        DelegatingMethod differentDelegatingMethod = new DelegatingMethod(differentMethod);
        assertFalse(delegatingMethod.equals(differentDelegatingMethod));
        assertFalse(delegatingMethod.equals(differentMethod));
    }
    
    @Test
    public void testHashCode() {
        int hashCode = delegatingMethod.hashCode();
        assertEquals(1, hashCode);
    }
    
    private static class MyClass {
        public void sampleMethod(String param1, int param2) {
            // Sample method for testing
        }
        
        public void anotherMethod() {
            // Another method for testing
        }
    }
}