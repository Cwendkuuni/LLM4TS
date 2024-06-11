import org.junit.Test;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.Assert.*;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = null; // TODO: Set the method value
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        Class<?>[] expected = null; // TODO: Set the expected value
        Class<?>[] actual = delegatingMethod.getExceptionTypes();
        
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testGetJavaMethod() {
        Method method = null; // TODO: Set the method value
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        Method expected = null; // TODO: Set the expected value
        Method actual = delegatingMethod.getJavaMethod();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetName() {
        Method method = null; // TODO: Set the method value
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        String expected = null; // TODO: Set the expected value
        String actual = delegatingMethod.getName();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetParameterTypes() {
        Method method = null; // TODO: Set the method value
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        Class<?>[] expected = null; // TODO: Set the expected value
        Class<?>[] actual = delegatingMethod.getParameterTypes();
        
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void testGetReturnType() {
        Method method = null; // TODO: Set the method value
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        Class<?> expected = null; // TODO: Set the expected value
        Class<?> actual = delegatingMethod.getReturnType();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsVarArgs() {
        Method method = null; // TODO: Set the method value
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        boolean expected = false; // TODO: Set the expected value
        boolean actual = delegatingMethod.isVarArgs();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsAbstract() {
        Method method = null; // TODO: Set the method value
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        boolean expected = false; // TODO: Set the expected value
        boolean actual = delegatingMethod.isAbstract();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testEquals() {
        Method method = null; // TODO: Set the method value
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        Object o = null; // TODO: Set the object value
        
        boolean expected = false; // TODO: Set the expected value
        boolean actual = delegatingMethod.equals(o);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHashCode() {
        Method method = null; // TODO: Set the method value
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        
        int expected = 1; // TODO: Set the expected value
        int actual = delegatingMethod.hashCode();
        
        assertEquals(expected, actual);
    }
}