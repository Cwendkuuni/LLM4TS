import org.junit.*;
import org.mockito.internal.creation.DelegatingMethod;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class DelegatingMethodTest {

    private DelegatingMethod delegatingMethod;

    @Before
    public void setUp() {
        Method method = null;
        try {
            method = DelegatingMethod.class.getDeclaredMethod("equals", Object.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        delegatingMethod = new DelegatingMethod(method);
    }

    @Test
    public void testGetExceptionTypes() {
        Class<?>[] exceptionTypes = delegatingMethod.getExceptionTypes();

        Assert.assertNotNull(exceptionTypes);
        Assert.assertEquals(0, exceptionTypes.length);
    }

    @Test
    public void testGetJavaMethod() {
        Method javaMethod = delegatingMethod.getJavaMethod();

        Assert.assertNotNull(javaMethod);
        Assert.assertEquals("equals", javaMethod.getName());
    }

    @Test
    public void testGetName() {
        String name = delegatingMethod.getName();

        Assert.assertNotNull(name);
        Assert.assertEquals("equals", name);
    }

    @Test
    public void testGetParameterTypes() {
        Class<?>[] parameterTypes = delegatingMethod.getParameterTypes();

        Assert.assertNotNull(parameterTypes);
        Assert.assertEquals(1, parameterTypes.length);
        Assert.assertEquals(Object.class, parameterTypes[0]);
    }

    @Test
    public void testGetReturnType() {
        Class<?> returnType = delegatingMethod.getReturnType();

        Assert.assertNotNull(returnType);
        Assert.assertEquals(boolean.class, returnType);
    }

    @Test
    public void testIsVarArgs() {
        boolean isVarArgs = delegatingMethod.isVarArgs();

        Assert.assertFalse(isVarArgs);
    }

    @Test
    public void testIsAbstract() {
        boolean isAbstract = delegatingMethod.isAbstract();

        Assert.assertFalse(isAbstract);
    }

    @Test
    public void testEquals() {
        DelegatingMethod otherDelegatingMethod = new DelegatingMethod(delegatingMethod.getJavaMethod());

        Assert.assertTrue(delegatingMethod.equals(otherDelegatingMethod));
        Assert.assertEquals(delegatingMethod.hashCode(), otherDelegatingMethod.hashCode());
    }

    @Test
    public void testEqualsWithDifferentObject() {
        boolean isEqual = delegatingMethod.equals(new Object());

        Assert.assertFalse(isEqual);
    }
}