import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class DelegatingMethodTest {

    private Method method;

    @Test
    public void testGetExceptionTypes() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        assertNotNull(exceptionTypes);
    }

    @Test
    public void testGetJavaMethod() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Method javaMethod = delegatingMethod.getJavaMethod();
        assertNotNull(javaMethod);
    }
    
    @Test
    public void testGetName() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        String name = delegatingMethod.getName();
        assertNotNull(name);
    }
    
    @Test
    public void testGetParameterTypes() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        assertNotNull(parameterTypes);
    }
    
    @Test
    public void testGetReturnType() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?> returnType = delegatingMethod.getReturnType();
        assertNotNull(returnType);
    }
    
    @Test
    public void testIsVarArgs() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean isVarArgs = delegatingMethod.isVarArgs();
        assertFalse(isVarArgs);
    }
    
    @Test
    public void testIsAbstract() {
        Method abstractMethod = null; //create a new abstract Method for testing purposes
        DelegatingMethod delegatingMethod = new DelegatingMethod(abstractMethod);
        boolean isAbstract = delegatingMethod.isAbstract();
        assertTrue(isAbstract);
    }
    
    @Test
    public void testEquals() {
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);
        assertTrue(delegatingMethod1.equals(delegatingMethod2));
    }
    
    @Test
    public void testHashCode() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        int hashCode = delegatingMethod.hashCode();
        assertEquals(1, hashCode);
    }
}