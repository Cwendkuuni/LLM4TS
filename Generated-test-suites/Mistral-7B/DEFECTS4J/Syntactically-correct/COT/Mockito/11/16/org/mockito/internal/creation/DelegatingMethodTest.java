import org.junit.Test;
import org.junit.Assert;
import java.lang.reflect.Method;
import java.util.Arrays;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = new Method(Object.class.getName(), "methodName", new Class[]{}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();

        if (exceptionTypes != null && exceptionTypes.length > 0) {
            Assert.assertArrayEquals(new Class[]{Exception.class}, exceptionTypes);
        } else {
            Assert.assertNull(exceptionTypes);
        }
    }

    @Test
    public void testGetJavaMethod() {
        Method method = new Method(Object.class.getName(), "methodName", new Class[]{}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Assert.assertNotNull(delegatingMethod.getJavaMethod());
        Assert.assertEquals(method, delegatingMethod.getJavaMethod());
    }

    @Test
    public void testGetName() {
        Method method = new Method(Object.class.getName(), "methodName", new Class[]{}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Assert.assertEquals("methodName", delegatingMethod.getName());
    }

    @Test
    public void testGetParameterTypes() {
        Class<?> clazz = String.class;
        Method method = new Method(Object.class.getName(), "methodName", new Class[]{clazz}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        Assert.assertArrayEquals(new Class[]{clazz}, parameterTypes);
    }

    @Test
    public void testGetReturnType() {
        Method method = new Method(Object.class.getName(), "methodName", new Class[]{}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Assert.assertEquals(Void.TYPE, delegatingMethod.getReturnType());
    }

    @Test
    public void testIsVarArgs() {
        Method method1 = new Method(Object.class.getName(), "methodName", new Class[0], new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        Assert.assertFalse(delegatingMethod1.isVarArgs());

        Method method2 = new Method(Object.class.getName(), "methodName", new Class[]{Object[].class}, new Class[0]);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        Assert.assertTrue(delegatingMethod2.isVarArgs());
    }

    @Test
    public void testIsAbstract() {
        Method method = new Method(Object.class.getName(), "methodName", new Class[0], new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        Assert.assertFalse(delegatingMethod.isAbstract());

        Method abstractMethod = new Method(AbstractClass.class.getName(), "abstractMethod", new Class[0], new Class[0]);
        Modifier[] modifiers = abstractMethod.getModifiers();
        Arrays.stream(modifiers).forEach(modifier -> Arrays.asList(Modifier.ABSTRACT).contains(modifier.ordinal()));
        DelegatingMethod abstractDelegatingMethod = new DelegatingMethod(abstractMethod);
        Assert.assertTrue(abstractDelegatingMethod.isAbstract());
    }

    @Test(expected = AssertionError.class)
    public void testEqualsNull() {
        Method method = new Method(Object.class.getName(), "methodName", new Class[0], new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);
        delegatingMethod.equals(null);
    }

    @Test
    public void testEqualsSameMethod() {
        Method method = new Method(Object.class.getName(), "methodName", new Class[0], new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);
        Assert.assertTrue(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testEqualsDifferentMethods() {
        Method method1 = new Method(Object.class.getName(), "methodName1", new Class[0], new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);

        Method method2 = new Method(Object.class.getName(), "methodName2", new Class[0], new Class[0]);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);
        Assert.assertFalse(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testEqualsSameDelegateMethod() {
        Method method = new Method(Object.class.getName(), "methodName", new Class[0], new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);

        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);
        Assert.assertTrue(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testHashCode() {
        Method method = new Method(Object.class.getName(), "methodName", new Class[0], new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method);
        Assert.assertEquals(delegatingMethod1.hashCode(), delegatingMethod2.hashCode());
    }
}