import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.creation.DelegatingMethod;
import org.mockito.internal.invocation.MockitoMethod;
import java.lang.reflect.Method;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method method = new Method(() -> {}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        Assert.assertArrayEquals(new Class[0], exceptionTypes);

        method = new Method(Object.class, new Class[]{"int"});
        delegatingMethod = new DelegatingMethod(method);
        exceptionTypes = delegatingMethod.getExceptionTypes();
        Assert.assertArrayEquals(new Class[]{Exception.class}, exceptionTypes);
    }

    @Test
    public void testGetJavaMethod() {
        Method method = new Method(() -> {}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Method javaMethod = delegatingMethod.getJavaMethod();
        assertEquals(method, javaMethod);

        method = new Method(Object.class, new Class[]{"int"});
        delegatingMethod = new DelegatingMethod(method);
        javaMethod = delegatingMethod.getJavaMethod();
        assertEquals(method, javaMethod);
    }

    @Test
    public void testGetName() {
        Method method = new Method(() -> {}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        String name = delegatingMethod.getName();
        assertEquals("", name);

        method = new Method(Object.class, new Class[]{"int"});
        delegatingMethod = new DelegatingMethod(method);
        name = delegatingMethod.getName();
        assertEquals("int", name);
    }

    @Test
    public void testGetParameterTypes() {
        Method method = new Method(() -> {}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        Assert.assertArrayEquals(new Class[0], parameterTypes);

        method = new Method(Object.class, new Class[]{"int"});
        delegatingMethod = new DelegatingMethod(method);
        parameterTypes = delegatingMethod.getParameterTypes();
        Assert.assertArrayEquals(new Class[]{Integer.TYPE}, parameterTypes);
    }

    @Test
    public void testGetReturnType() {
        Method method = new Method(() -> {}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        Class<?> returnType = delegatingMethod.getReturnType();
        assertEquals(Void.TYPE, returnType);

        method = new Method(Object.class, new Class[0]);
        delegatingMethod = new DelegatingMethod(method);
        returnType = delegatingMethod.getReturnType();
        assertEquals(Object.class, returnType);
    }

    @Test
    public void testIsVarArgs() {
        Method method = new Method(() -> {}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean varArgs = delegatingMethod.isVarArgs();
        assertFalse(varArgs);

        method = new Method(Object.class, new Class[]{"int", "int..."});
        delegatingMethod = new DelegatingMethod(method);
        varArgs = delegatingMethod.isVarArgs();
        assertTrue(varArgs);
    }

    @Test
    public void testIsAbstract() {
        Method method = new Method(() -> {}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        boolean abstractMethod = delegatingMethod.isAbstract();
        assertFalse(abstractMethod);

        method = new Method(AbstractClass.class, new Class[]{"int"});
        delegatingMethod = new DelegatingMethod(method);
        abstractMethod = delegatingMethod.isAbstract();
        assertTrue(abstractMethod);
    }

    @Test
    public void testEquals() {
        Method method1 = new Method(() -> {}, new Class[0]);
        Method method2 = new Method(() -> {}, new Class[0]);
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(method1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(method2);

        assertFalse(delegatingMethod1.equals(delegatingMethod2));
        assertFalse(delegatingMethod1.equals(new Object()));

        method2 = method1;
        delegatingMethod2 = new DelegatingMethod(method2);
        assertTrue(delegatingMethod1.equals(delegatingMethod2));

        method1 = method2;
        delegatingMethod1 = new DelegatingMethod(method1);
        assertTrue(delegatingMethod1.equals(delegatingMethod2));
    }

    @Test
    public void testHashCode() {
        Method method = new Method(() -> {}, new Class[0]);
        DelegatingMethod delegatingMethod = new DelegatingMethod(method);

        int hashCode = delegatingMethod.hashCode();
        assertEquals(hashCode, delegatingMethod.hashCode());

        method = new Method(Object.class, new Class[0]);
        delegatingMethod = new DelegatingMethod(method);
        hashCode = delegatingMethod.hashCode();
        assertNotEquals(hashCode, delegatingMethod.hashCode());
    }
}