import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import static org.junit.Assert.*;

public class DelegatingMethodTest {
    private DelegatingMethod delegatingMethod;

    @Before
    public void setUp() throws NoSuchMethodException {
        Method method = DelegatingMethod.class.getDeclaredMethod("equals", Object.class);
        delegatingMethod = new DelegatingMethod(method);
    }

    @Test
    public void testGetExceptionTypes() {
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();
        assertEquals(0, exceptionTypes.length);
    }

    @Test
    public void testGetJavaMethod() {
        Method javaMethod = delegatingMethod.getJavaMethod();
        assertNotNull(javaMethod);
        assertEquals("equals", javaMethod.getName());
        assertEquals(1, javaMethod.getParameterCount());
        assertEquals(Object.class, javaMethod.getParameterTypes()[0]);
        assertEquals(Boolean.TYPE, javaMethod.getReturnType());
        assertFalse(javaMethod.isVarArgs());
        assertFalse(Modifier.isAbstract(javaMethod.getModifiers()));
    }

    @Test
    public void testGetName() {
        String name = delegatingMethod.getName();
        assertEquals("equals", name);
    }

    @Test
    public void testGetParameterTypes() {
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        assertEquals(1, parameterTypes.length);
        assertEquals(Object.class, parameterTypes[0]);
    }

    @Test
    public void testGetReturnType() {
        Class<?> returnType = delegatingMethod.getReturnType();
        assertEquals(Boolean.TYPE, returnType);
    }

    @Test
    public void testIsVarArgs() {
        assertFalse(delegatingMethod.isVarArgs());
    }

    @Test
    public void testIsAbstract() {
        assertFalse(delegatingMethod.isAbstract());
    }

    @Test
    public void testEquals_NullObject_ReturnsFalse() {
        assertFalse(delegatingMethod.equals(null));
    }

    @Test
    public void testEquals_SameObject_ReturnsTrue() {
        assertTrue(delegatingMethod.equals(delegatingMethod));
    }

    @Test
    public void testEquals_DifferentClass_ReturnsFalse() {
        Object obj = new Object();
        assertFalse(delegatingMethod.equals(obj));
    }

    @Test
    public void testEquals_DifferentDelegatingMethodWithDifferentMethod_ReturnsFalse() throws NoSuchMethodException {
        Method otherMethod = DelegatingMethod.class.getDeclaredMethod("hashCode");
        DelegatingMethod otherDelegatingMethod = new DelegatingMethod(otherMethod);
        assertFalse(delegatingMethod.equals(otherDelegatingMethod));
    }

    @Test
    public void testEquals_DelegatingMethodWithSameMethod_ReturnsTrue() throws NoSuchMethodException {
        Method sameMethod = DelegatingMethod.class.getDeclaredMethod("equals", Object.class);
        DelegatingMethod sameDelegatingMethod = new DelegatingMethod(sameMethod);
        assertTrue(delegatingMethod.equals(sameDelegatingMethod));
    }

    @Test
    public void testHashCode_ReturnsConstantValue() {
        assertEquals(1, delegatingMethod.hashCode());
    }
}