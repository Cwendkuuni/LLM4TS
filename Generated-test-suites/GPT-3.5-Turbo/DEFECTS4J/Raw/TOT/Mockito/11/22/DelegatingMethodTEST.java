import org.junit.Test;
import static org.junit.Assert.*;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        // Test case 1: No exception types
        DelegatingMethod method = new DelegatingMethod(SomeClass.class.getMethod("method1"));
        Class<?>[] expected = {};
        assertArrayEquals(expected, method.getExceptionTypes());
        
        // Test case 2: Single exception type
        method = new DelegatingMethod(SomeClass.class.getMethod("method2"));
        expected = new Class<?>[]{SomeException.class};
        assertArrayEquals(expected, method.getExceptionTypes());
        
        // Test case 3: Multiple exception types
        method = new DelegatingMethod(SomeClass.class.getMethod("method3"));
        expected = new Class<?>[]{Exception1.class, Exception2.class};
        assertArrayEquals(expected, method.getExceptionTypes());
    }

    @Test
    public void testGetJavaMethod() {
        // Test case 1: Valid method
        Method expected = SomeClass.class.getMethod("method1");
        DelegatingMethod method = new DelegatingMethod(expected);
        assertEquals(expected, method.getJavaMethod());
        
        // Test case 2: Null method
        try {
            method = new DelegatingMethod(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Method cannot be null", e.getMessage());
        }
    }

    @Test
    public void testGetName() {
        // Test case 1: Valid name
        DelegatingMethod method = new DelegatingMethod(SomeClass.class.getMethod("method1"));
        assertEquals("method1", method.getName());
    }

    @Test
    public void testGetParameterTypes() {
        // Test case 1: No parameters
        DelegatingMethod method = new DelegatingMethod(SomeClass.class.getMethod("method1"));
        Class<?>[] expected = {};
        assertArrayEquals(expected, method.getParameterTypes());
        
        // Test case 2: Single parameter
        method = new DelegatingMethod(SomeClass.class.getMethod("method2", String.class));
        expected = new Class<?>[]{String.class};
        assertArrayEquals(expected, method.getParameterTypes());
        
        // Test case 3: Multiple parameters
        method = new DelegatingMethod(SomeClass.class.getMethod("method3", int.class, String.class));
        expected = new Class<?>[]{int.class, String.class};
        assertArrayEquals(expected, method.getParameterTypes());
    }

    @Test
    public void testGetReturnType() {
        // Test case 1: Void return type
        DelegatingMethod method = new DelegatingMethod(SomeClass.class.getMethod("method1"));
        assertEquals(Void.TYPE, method.getReturnType());
        
        // Test case 2: Non-void return type
        method = new DelegatingMethod(SomeClass.class.getMethod("method2"));
        assertEquals(String.class, method.getReturnType());
    }

    @Test
    public void testIsVarArgs() {
        // Test case 1: Varargs method
        DelegatingMethod method = new DelegatingMethod(SomeClass.class.getMethod("method1", String.class, String[].class));
        assertTrue(method.isVarArgs());
        
        // Test case 2: Non-varargs method
        method = new DelegatingMethod(SomeClass.class.getMethod("method2"));
        assertFalse(method.isVarArgs());
    }

    @Test
    public void testIsAbstract() {
        // Test case 1: Abstract method
        DelegatingMethod method = new DelegatingMethod(SomeClass.class.getMethod("method1"));
        assertTrue(method.isAbstract());
        
        // Test case 2: Non-abstract method
        method = new DelegatingMethod(SomeClass.class.getMethod("method2"));
        assertFalse(method.isAbstract());
    }

    @Test
    public void testEquals() {
        // Test case 1: Equal objects
        DelegatingMethod method1 = new DelegatingMethod(SomeClass.class.getMethod("method1"));
        DelegatingMethod method2 = new DelegatingMethod(SomeClass.class.getMethod("method1"));
        assertTrue(method1.equals(method2));
        
        // Test case 2: Different objects
        method1 = new DelegatingMethod(SomeClass.class.getMethod("method1"));
        Method method3 = SomeClass.class.getMethod("method1");
        assertTrue(method1.equals(method3));
        
        // Test case 3: Not equal objects
        method1 = new DelegatingMethod(SomeClass.class.getMethod("method1"));
        method2 = new DelegatingMethod(SomeClass.class.getMethod("method2"));
        assertFalse(method1.equals(method2));
    }

    @Test
    public void testHashCode() {
        // Test case 1: Equal objects
        DelegatingMethod method1 = new DelegatingMethod(SomeClass.class.getMethod("method1"));
        DelegatingMethod method2 = new DelegatingMethod(SomeClass.class.getMethod("method1"));
        assertEquals(method1.hashCode(), method2.hashCode());
        
        // Test case 2: Different objects
        method1 = new DelegatingMethod(SomeClass.class.getMethod("method1"));
        Method method3 = SomeClass.class.getMethod("method1");
        assertEquals(method1.hashCode(), method3.hashCode());
    }
}

class SomeClass {
    public void method1() {}
    public String method2() throws SomeException { return ""; }
    public void method3() throws Exception1, Exception2 {}
}

class SomeException extends Exception {}
class Exception1 extends Exception {}
class Exception2 extends Exception {}