import org.junit.Test;
import static org.junit.Assert.*;

public class DelegatingMethodTest {

    // Test cases for getExceptionTypes() method
    @Test
    public void testGetExceptionTypes() {
        DelegatingMethod method = new DelegatingMethod(null); // Method with null
        assertArrayEquals(null, method.getExceptionTypes());

        method = new DelegatingMethod(Object.class.getMethods()[0]); // Method without exceptions
        assertArrayEquals(new Class<?>[0], method.getExceptionTypes());

        method = new DelegatingMethod(String.class.getMethods()[0]); // Method with exceptions
        assertArrayEquals(new Class<?>[]{NoSuchMethodException.class}, method.getExceptionTypes());
    }

    // Test cases for getJavaMethod() method
    @Test
    public void testGetJavaMethod() {
        Method m = Object.class.getMethods()[0];
        DelegatingMethod method = new DelegatingMethod(m);
        assertEquals(m, method.getJavaMethod());
    }

    // Test case for getName() method
    @Test
    public void testGetName() {
        Method m = Object.class.getMethods()[0];
        DelegatingMethod method = new DelegatingMethod(m);
        assertEquals("toString", method.getName());
    }

    // Test cases for getParameterTypes() method
    @Test
    public void testGetParameterTypes() {
        Method m = Object.class.getMethods()[0];
        DelegatingMethod method = new DelegatingMethod(m);
        assertArrayEquals(new Class<?>[]{}, method.getParameterTypes());
    }

    // Test case for getReturnType() method
    @Test
    public void testGetReturnType() {
        Method m = Object.class.getMethods()[0];
        DelegatingMethod method = new DelegatingMethod(m);
        assertEquals(Object.class, method.getReturnType());
    }

    // Test case for isVarArgs() method
    @Test
    public void testIsVarArgs() {
        Method m = Object.class.getMethods()[0];
        DelegatingMethod method = new DelegatingMethod(m);
        assertFalse(method.isVarArgs());
    }

    // Test cases for isAbstract() method
    @Test
    public void testIsAbstract() {
        Method m = Object.class.getMethods()[0];
        DelegatingMethod method = new DelegatingMethod(m);
        assertFalse(method.isAbstract());
    }

    // Test case for equals() method with equal object
    @Test
    public void testEqualsWithEqualObject() {
        Method m = Object.class.getMethods()[0];
        DelegatingMethod method1 = new DelegatingMethod(m);
        DelegatingMethod method2 = new DelegatingMethod(m);
        assertTrue(method1.equals(method2));
    }

    // Test case for equals() method with different object
    @Test
    public void testEqualsWithDifferentObject() {
        Method m1 = Object.class.getMethods()[0];
        Method m2 = String.class.getMethods()[0];
        DelegatingMethod method1 = new DelegatingMethod(m1);
        DelegatingMethod method2 = new DelegatingMethod(m2);
        assertFalse(method1.equals(method2));
    }

    // Test case for hashCode() method
    @Test
    public void testHashCode() {
        Method m = Object.class.getMethods()[0];
        DelegatingMethod method = new DelegatingMethod(m);
        assertEquals(1, method.hashCode());
    }

}