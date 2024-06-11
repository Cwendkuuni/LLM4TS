import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.creation.DelegatingMethod;
import java.lang.reflect.Method;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(new Method(TestClass.class, "testMethod", new Class[] {String.class, int.class}, new Class[] {String.class, Exception.class}));
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        Assert.assertArrayEquals(new Class[] {Exception.class}, exceptionTypes);
    }

    @Test
    public void testGetJavaMethod() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(new Method(TestClass.class, "testMethod", new Class[] {String.class, int.class}));
        Method javaMethod = delegatingMethod.getJavaMethod();
        Assert.assertNotNull(javaMethod);
        Assert.assertEquals("testMethod", javaMethod.getName());
    }

    @Test
    public void testGetName() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(new Method(TestClass.class, "testMethod", new Class[] {String.class, int.class}));
        String name = delegatingMethod.getName();
        Assert.assertEquals("testMethod", name);
    }

    @Test
    public void testGetParameterTypes() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(new Method(TestClass.class, "testMethod", new Class[] {String.class, int.class}));
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        Assert.assertArrayEquals(new Class[] {String.class, int.class}, parameterTypes);
    }

    @Test
    public void testGetReturnType() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(new Method(TestClass.class, "testMethod", new Class[] {String.class, int.class}, new Class[] {}));
        Class<?> returnType = delegatingMethod.getReturnType();
        Assert.assertNull(returnType);
    }

    @Test
    public void testIsVarArgs() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(new Method(TestClass.class, "testMethod", new Class[] {String.class, int.class}, new Class[] {}));
        boolean isVarArgs = delegatingMethod.isVarArgs();
        Assert.assertFalse(isVarArgs);

        delegatingMethod = new DelegatingMethod(new Method(TestClass.class, "testMethod", new Class[] {String.class}, new Class[] {}));
        isVarArgs = delegatingMethod.isVarArgs();
        Assert.assertTrue(isVarArgs);
    }

    @Test
    public void testIsAbstract() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(new Method(TestClass.class, "testMethod", new Class[] {String.class, int.class}));
        boolean isAbstract = delegatingMethod.isAbstract();
        Assert.assertFalse(isAbstract);

        delegatingMethod = new DelegatingMethod(new Method(TestClass.class, "testMethod", new Class[] {}, new Class[] {}));
        isAbstract = delegatingMethod.isAbstract();
        Assert.assertTrue(isAbstract);
    }

    @Test(expected = AssertionError.class)
    public void testEqualsNull() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(new Method(TestClass.class, "testMethod", new Class[] {String.class, int.class}));
        delegatingMethod.equals(null);
    }

    @Test
    public void testEqualsSameObject() {
        DelegatingMethod delegatingMethod = new DelegatingMethod(new Method(TestClass.class, "testMethod", new Class[] {String.class, int.class}));
        DelegatingMethod sameObject = delegatingMethod;
        assertTrue(delegatingMethod.equals(sameObject));
    }

    @Test
    public void testEqualsDifferentObjects() {
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(new Method(TestClass.class, "testMethod1", new Class[] {String.class, int.class}));
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(new Method(TestClass.class, "testMethod2", new Class[] {String.class, int.class}));
        assertFalse(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testHashCodeSameObjects() {
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(new Method(TestClass.class, "testMethod", new Class[] {String.class, int.class}));
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(delegatingMethod1.getJavaMethod());
        assertEquals(delegatingMethod1.hashCode(), delegatingMethod2.hashCode());
    }

    @Test
    public void testHashCodeDifferentObjects() {
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(new Method(TestClass.class, "testMethod1", new Class[] {String.class, int.class}));
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(new Method(TestClass.class, "testMethod2", new Class[] {String.class, int.class}));
        assertNotEquals(delegatingMethod1.hashCode(), delegatingMethod2.hashCode());
    }
}

class TestClass {
    public String testMethod(String s, int i) throws Exception {
        return "";
    }
}