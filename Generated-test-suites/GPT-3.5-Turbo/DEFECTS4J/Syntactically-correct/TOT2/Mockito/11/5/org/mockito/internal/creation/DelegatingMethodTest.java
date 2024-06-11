import org.junit.Test;
import static org.junit.Assert.*;
import java.lang.reflect.Method;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(someMethod);
        assertArrayEquals(someMethod.getExceptionTypes(), delegatingMethod.getExceptionTypes());
    }

    @Test
    public void testGetJavaMethod() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(someMethod);
        assertEquals(someMethod, delegatingMethod.getJavaMethod());
    }

    @Test
    public void testGetName() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(someMethod);
        assertEquals(someMethod.getName(), delegatingMethod.getName());
    }

    @Test
    public void testGetParameterTypes() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(someMethod);
        assertArrayEquals(someMethod.getParameterTypes(), delegatingMethod.getParameterTypes());
    }

    @Test
    public void testGetReturnType() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(someMethod);
        assertEquals(someMethod.getReturnType(), delegatingMethod.getReturnType());
    }

    @Test
    public void testIsVarArgs() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(someMethod);
        assertEquals(someMethod.isVarArgs(), delegatingMethod.isVarArgs());
    }

    @Test
    public void testIsAbstract() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(someMethod);
        assertEquals((someMethod.getModifiers() & Modifier.ABSTRACT) != 0, delegatingMethod.isAbstract());
    }

    @Test
    public void testEquals() {
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(someMethod);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(someMethod);
        assertTrue(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testHashCode() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(someMethod);
        assertEquals(1, delegatingMethod.hashCode());
    }
}