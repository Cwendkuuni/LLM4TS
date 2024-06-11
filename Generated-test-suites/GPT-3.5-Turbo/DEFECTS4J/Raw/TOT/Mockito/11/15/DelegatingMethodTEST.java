import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.MockitoMethod;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.Assert.*;

public class DelegatingMethodTest {

    private DelegatingMethod delegatingMethod;
    private Method method;

    @Before
    public void setUp() throws Exception {
        method = String.class.getMethod("length");
        delegatingMethod = new DelegatingMethod(method);
    }

    @Test
    public void testGetExceptionTypes() {
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        assertNotNull(exceptionTypes);
        assertEquals(0, exceptionTypes.length);
    }

    @Test
    public void testGetJavaMethod() {
        Method javaMethod = delegatingMethod.getJavaMethod();
        assertEquals(method, javaMethod);
    }

    @Test
    public void testGetName() {
        String name = delegatingMethod.getName();
        assertEquals("length", name);
    }

    @Test
    public void testGetParameterTypes() {
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        assertNotNull(parameterTypes);
        assertEquals(0, parameterTypes.length);
    }

    @Test
    public void testGetReturnType() {
        Class<?> returnType = delegatingMethod.getReturnType();
        assertEquals(int.class, returnType);
    }

    @Test
    public void testIsVarArgs() {
        boolean isVarArgs = delegatingMethod.isVarArgs();
        assertFalse(isVarArgs);
    }

    @Test
    public void testIsAbstract() {
        boolean isAbstract = delegatingMethod.isAbstract();
        assertFalse(isAbstract);
    }

    @Test
    public void testEquals_sameObject() {
        assertTrue(delegatingMethod.equals(delegatingMethod));
    }

    @Test
    public void testEquals_equalMethod() throws NoSuchMethodException {
        Method sameMethod = String.class.getMethod("length");
        DelegatingMethod otherDelegatingMethod = new DelegatingMethod(sameMethod);
        assertTrue(delegatingMethod.equals(otherDelegatingMethod));
    }

    @Test
    public void testEquals_notEqualMethod() throws NoSuchMethodException {
        Method otherMethod = String.class.getMethod("toUpperCase");
        DelegatingMethod otherDelegatingMethod = new DelegatingMethod(otherMethod);
        assertFalse(delegatingMethod.equals(otherDelegatingMethod));
    }

    @Test
    public void testEquals_notDelegatingMethod() {
        assertFalse(delegatingMethod.equals(method));
    }

    @Test
    public void testHashCode() {
        assertEquals(1, delegatingMethod.hashCode());
    }
}