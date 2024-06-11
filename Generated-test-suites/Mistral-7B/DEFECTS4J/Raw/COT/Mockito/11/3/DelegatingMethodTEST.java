import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.creation.DelegatingMethod;
import java.lang.reflect.Method;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = new Method(() -> {}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] expected = new Class[0];
        Class<?>[] actual = delegatingMethod.getExceptionTypes();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = new Method(() -> {}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Method expected = method;
        Method actual = delegatingMethod.getJavaMethod();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetName() {
        Method method = new Method("testMethod", new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        String expected = "testMethod";
        String actual = delegatingMethod.getName();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetParameterTypes() {
        Method method = new Method("testMethod", new Class[]{String.class, int.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] expected = new Class[]{String.class, int.class};
        Class<?>[] actual = delegatingMethod.getParameterTypes();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetReturnType() {
        Method method = new Method("testMethod", new Class[0], new Class[]{String.class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?> expected = String.class;
        Class<?> actual = delegatingMethod.getReturnType();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsVarArgs() {
        Method method = new Method("testMethod", new Class[]{String.class, int.class, String[].class});
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean expected = true;
        boolean actual = delegatingMethod.isVarArgs();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsAbstract() {
        Method method = new Method("testMethod", new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        boolean expected = false;
        boolean actual = delegatingMethod.isAbstract();
        assertEquals(expected, actual);

        Method abstractMethod = new Method("", new Class[0]);
        delegatingMethod = new DelegatingMethod(abstractMethod);
        expected = true;
        actual = delegatingMethod.isAbstract();
        assertEquals(expected, actual);
    }

    @Test(expected = AssertionError.class)
    public void testEqualsNull() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(new Method(""));
        delegatingMethod.equals(null);
    }

    @Test
    public void testEqualsSameObject() {
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(new Method(""));
        delegatingMethod1.equals(delegatingMethod1);
    }

    @Test
    public void testEqualsDifferentObject() {
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(new Method(""));
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(new Method(""));
        assertFalse(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testEqualsSameMethod() {
        Method method = new Method("");
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);
        assertTrue(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testEqualsDifferentMethod() {
        Method method1 = new Method("");
        Method method2 = new Method("");
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        assertFalse(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testHashCode() {
        Method method = new Method("");
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);
        int expected = delegatingMethod1.hashCode();
        int actual = delegatingMethod2.hashCode();
        assertEquals(expected, actual);
    }
}