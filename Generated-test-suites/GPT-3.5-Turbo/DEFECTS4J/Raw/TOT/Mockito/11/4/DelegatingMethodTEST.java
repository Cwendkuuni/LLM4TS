import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.creation.DelegatingMethod;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import static org.junit.Assert.*;

public class DelegatingMethodTest {

    private Method testMethod;
    private DelegatingMethod delegatingMethod;

    @Before
    public void setUp() throws Exception {
        testMethod = DelegatingMethod.class.getMethod("equals", Object.class);
        delegatingMethod = new DelegatingMethod(testMethod);
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
        assertNotNull(javaMethod);
        assertEquals(testMethod, javaMethod);
    }
    
    @Test
    public void testGetName() {
        String name = delegatingMethod.getName();
        assertNotNull(name);
        assertEquals("equals", name);
    }
    
    @Test
    public void testGetParameterTypes() {
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();
        assertNotNull(parameterTypes);
        assertEquals(1, parameterTypes.length);
        assertEquals(Object.class, parameterTypes[0]);
    }
    
    @Test
    public void testGetReturnType() {
        Class<?> returnType = delegatingMethod.getReturnType();
        assertNotNull(returnType);
        assertEquals(boolean.class, returnType);
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
    public void testEquals_SameInstance() {
        assertTrue(delegatingMethod.equals(delegatingMethod));
    }
    
    @Test
    public void testEquals_EqualMethods() throws NoSuchMethodException {
        Method equalsMethod = DelegatingMethod.class.getMethod("equals", Object.class);
        DelegatingMethod otherMethod = new DelegatingMethod(equalsMethod);
        assertTrue(delegatingMethod.equals(otherMethod));
    }
    
    @Test
    public void testEquals_EqualMethodsAsObjects() throws NoSuchMethodException {
        Method equalsMethod = DelegatingMethod.class.getMethod("equals", Object.class);
        assertTrue(delegatingMethod.equals(equalsMethod));
    }
    
    @Test
    public void testEquals_DifferentMethod() throws NoSuchMethodException {
        Method hashCodeMethod = DelegatingMethod.class.getMethod("hashCode");
        DelegatingMethod otherMethod = new DelegatingMethod(hashCodeMethod);
        assertFalse(delegatingMethod.equals(otherMethod));
    }
    
    @Test
    public void testHashCode() {
        int hashCode = delegatingMethod.hashCode();
        assertEquals(1, hashCode);
    }
}