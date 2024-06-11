import org.junit.Test;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import static org.junit.Assert.*;

public class DelegatingMethodTest {

    @Test
    public void testConstructor() {
        Method method = new Method(); // Change to actual method
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertNotNull(delegatingMethod);
        assertEquals(method, delegatingMethod.getJavaMethod());
    }

    @Test
    public void testGetExceptionTypes() {
        Method method = new Method(); // Change to actual method
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertArrayEquals(method.getExceptionTypes(), delegatingMethod.getExceptionTypes());
    }

    @Test
    public void testGetName() {
        Method method = new Method(); // Change to actual method
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertEquals(method.getName(), delegatingMethod.getName());
    }

    @Test
    public void testGetParameterTypes() {
        Method method = new Method(); // Change to actual method
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertArrayEquals(method.getParameterTypes(), delegatingMethod.getParameterTypes());
    }

    @Test
    public void testGetReturnType() {
        Method method = new Method(); // Change to actual method
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertEquals(method.getReturnType(), delegatingMethod.getReturnType());
    }

    @Test
    public void testIsVarArgs() {
        Method method = new Method(); // Change to actual method
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertEquals(method.isVarArgs(), delegatingMethod.isVarArgs());
    }

    @Test
    public void testIsAbstract() {
        Method method = new Method(); // Change to actual method
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertEquals((method.getModifiers() & Modifier.ABSTRACT) != 0, delegatingMethod.isAbstract());
    }

    @Test
    public void testEquals() {
        Method method = new Method(); // Change to actual method
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertTrue(delegatingMethod.equals(method));
        assertFalse(delegatingMethod.equals(null));
        assertFalse(delegatingMethod.equals("not a method object"));
    }

    @Test
    public void testHashCode() {
        Method method = new Method(); // Change to actual method
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        assertEquals(1, delegatingMethod.hashCode());
    }
}