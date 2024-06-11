import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class DelegatingMethodTest {
    @Test
    public void testConstructor() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertNotNull(delegatingMethod);
    }
    
    @Test
    public void testGetExceptionTypes() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        assertNotNull(exceptionTypes);
    }
    
    @Test
    public void testGetJavaMethod() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Method javaMethod = delegatingMethod.getJavaMethod();
        assertNotNull(javaMethod);
    }
    
    @Test
    public void testGetName() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        String name = delegatingMethod.getName();
        assertNotNull(name);
    }
    
    @Test
    public void testGetParameterTypes() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        assertNotNull(parameterTypes);
    }
    
    @Test
    public void testGetReturnType() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?> returnType = delegatingMethod.getReturnType();
        assertNotNull(returnType);
    }
    
    @Test
    public void testIsVarArgs() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean isVarArgs = delegatingMethod.isVarArgs();
        assertFalse(isVarArgs);
    }
    
    @Test
    public void testIsAbstract() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean isAbstract = delegatingMethod.isAbstract();
        assertFalse(isAbstract);
    }
    
    @Test
    public void testEquals() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Object object = null;
        boolean equals = delegatingMethod.equals(object);
        assertFalse(equals);
    }
    
    @Test
    public void testHashCode() {
        Method method = null;
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        int hashCode = delegatingMethod.hashCode();
        assertEquals(1, hashCode);
    }
}