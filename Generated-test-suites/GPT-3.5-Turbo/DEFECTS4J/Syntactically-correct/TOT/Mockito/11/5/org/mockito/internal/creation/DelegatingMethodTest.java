import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = null; // create a method object for testing
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        Class<?>[] expected = method.getExceptionTypes();
        Class<?>[] actual = delegatingMethod.getExceptionTypes();
        
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = null; // create a method object for testing
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        Method expected = method;
        Method actual = delegatingMethod.getJavaMethod();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetName() {
        Method method = null; // create a method object for testing
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        String expected = method.getName();
        String actual = delegatingMethod.getName();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetParameterTypes() {
        Method method = null; // create a method object for testing
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        Class<?>[] expected = method.getParameterTypes();
        Class<?>[] actual = delegatingMethod.getParameterTypes();
        
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testGetReturnType() {
        Method method = null; // create a method object for testing
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        Class<?> expected = method.getReturnType();
        Class<?> actual = delegatingMethod.getReturnType();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsVarArgs() {
        Method method = null; // create a method object for testing
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        boolean expected = method.isVarArgs();
        boolean actual = delegatingMethod.isVarArgs();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsAbstract() {
        Method method = null; // create a method object for testing
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        boolean expected = (method.getModifiers() & Modifier.ABSTRACT) != 0;
        boolean actual = delegatingMethod.isAbstract();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEquals() {
        Method method = null; // create a method object for testing
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        boolean expected = method.equals(delegatingMethod) || method.equals(delegatingMethod.getJavaMethod());
        boolean actual = delegatingMethod.equals(delegatingMethod);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHashCode() {
        Method method = null; // create a method object for testing
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        int expected = 1;
        int actual = delegatingMethod.hashCode();
        
        assertEquals(expected, actual);
    }

}