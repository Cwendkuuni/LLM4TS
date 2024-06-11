import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.creation.DelegatingMethod;

import java.lang.reflect.Method;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = String.class.getDeclaredMethods()[0]; // any method from String class for testing
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertArrayEquals(method.getExceptionTypes(), delegatingMethod.getExceptionTypes());
    }

    @Test
    public void testGetJavaMethod() {
        Method method = String.class.getDeclaredMethods()[0]; 
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertEquals(method, delegatingMethod.getJavaMethod());
    }

    @Test
    public void testGetName() {
        Method method = String.class.getDeclaredMethods()[0]; 
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertEquals(method.getName(), delegatingMethod.getName());
    }

    @Test
    public void testGetParameterTypes() {
        Method method = String.class.getDeclaredMethods()[0]; 
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertArrayEquals(method.getParameterTypes(), delegatingMethod.getParameterTypes());
    }

    @Test
    public void testGetReturnType() {
        Method method = String.class.getDeclaredMethods()[0]; 
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertEquals(method.getReturnType(), delegatingMethod.getReturnType());
    }

    @Test
    public void testIsVarArgs() {
        Method method = String.class.getDeclaredMethods()[0]; 
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertEquals(method.isVarArgs(), delegatingMethod.isVarArgs());
    }

    @Test
    public void testIsAbstract() {
        Method method = String.class.getDeclaredMethods()[0]; 
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertEquals(Modifier.isAbstract(method.getModifiers()), delegatingMethod.isAbstract());
    }

    @Test
    public void testEquals() {
        Method method1 = String.class.getDeclaredMethods()[0]; 
        Method method2 = String.class.getDeclaredMethods()[1]; 
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        assertFalse(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testHashCode() {
        Method method = String.class.getDeclaredMethods()[0]; 
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertEquals(1, delegatingMethod.hashCode());
    }
}