import org.junit.Test;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.Assert.*;

public class DelegatingMethodTest {
    @Test
    public void testConstructor() throws NoSuchMethodException {
        Method method = String.class.getMethod("toLowerCase");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(method, delegatingMethod.getJavaMethod());
    }

    @Test(expected = AssertionError.class)
    public void testConstructorNullMethod() {
        new DelegatingMethod(null);
    }

    @Test
    public void testGetExceptionTypes() throws NoSuchMethodException {
        Method method = String.class.getMethod("toLowerCase");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertArrayEquals(method.getExceptionTypes(), delegatingMethod.getExceptionTypes());
    }

    @Test
    public void testGetName() throws NoSuchMethodException {
        Method method = String.class.getMethod("toLowerCase");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(method.getName(), delegatingMethod.getName());
    }

    @Test
    public void testGetParameterTypes() throws NoSuchMethodException {
        Method method = String.class.getMethod("toLowerCase");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertArrayEquals(method.getParameterTypes(), delegatingMethod.getParameterTypes());
    }

    @Test
    public void testGetReturnType() throws NoSuchMethodException {
        Method method = String.class.getMethod("toLowerCase");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(method.getReturnType(), delegatingMethod.getReturnType());
    }

    @Test
    public void testIsVarArgs() throws NoSuchMethodException {
        Method method = String.class.getMethod("format", String.class, Object[].class);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertTrue(delegatingMethod.isVarArgs());
    }

    @Test
    public void testIsAbstractTrue() throws NoSuchMethodException {
        Method method = AbstractClass.class.getMethod("abstractMethod");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertTrue(delegatingMethod.isAbstract());
    }

    @Test
    public void testIsAbstractFalse() throws NoSuchMethodException {
        Method method = ConcreteClass.class.getMethod("concreteMethod");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertFalse(delegatingMethod.isAbstract());
    }

    @Test
    public void testEquals() throws NoSuchMethodException {
        Method method1 = String.class.getMethod("toLowerCase");
        Method method2 = String.class.getMethod("toUpperCase");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method1);

        assertTrue(delegatingMethod.equals(method1));
        assertFalse(delegatingMethod.equals(method2));
        assertTrue(delegatingMethod.equals(delegatingMethod));
    }

    @Test
    public void testHashCode() throws NoSuchMethodException {
        Method method = String.class.getMethod("toLowerCase");
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        assertEquals(1, delegatingMethod.hashCode());
    }

    private abstract class AbstractClass {
        public abstract void abstractMethod();
    }

    private class ConcreteClass {
        public void concreteMethod() {
        }
    }
}