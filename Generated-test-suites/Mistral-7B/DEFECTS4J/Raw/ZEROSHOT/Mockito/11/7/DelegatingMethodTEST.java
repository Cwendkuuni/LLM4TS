import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.creation.DelegatingMethod;
import org.mockito.internal.invocation.MockitoMethod;
import java.lang.reflect.Method;

public class DelegatingMethodTest {

    @Test
    public void testGetExceptionTypes() {
        Method testMethod = new MockitoMethod(String.class.getMethod("length", new Class[0])).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);

        Class<?>[] expected = new Class[0];
        Class<?>[] actual = delegatingMethod.getExceptionTypes();

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetJavaMethod() {
        Method testMethod = new MockitoMethod(String.class.getMethod("length", new Class[0])).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);

        Method expected = testMethod;
        Method actual = delegatingMethod.getJavaMethod();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testgetName() {
        Method testMethod = new MockitoMethod(String.class.getMethod("length", new Class[0])).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);

        String expected = "length";
        String actual = delegatingMethod.getName();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetParameterTypes() {
        Method testMethod = new MockitoMethod(String.class.getMethod("length", new Class[0])).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);

        Class<?>[] expected = new Class[0];
        Class<?>[] actual = delegatingMethod.getParameterTypes();

        Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetReturnType() {
        Method testMethod = new MockitoMethod(String.class.getMethod("length", new Class[0])).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);

        Class<?> expected = int.class;
        Class<?> actual = delegatingMethod.getReturnType();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsVarArgs() {
        Method testMethod = new MockitoMethod(String.class.getMethod("split", String[].class)).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);

        boolean expected = true;
        boolean actual = delegatingMethod.isVarArgs();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsAbstract() {
        Method testMethod = new MockitoMethod(String.class.getMethod("length", new Class[0])).getJavaMethod();
        DelegatingMethod delegatingMethod = new DelegatingMethod(testMethod);

        boolean expected = false;
        boolean actual = delegatingMethod.isAbstract();

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        Method testMethod1 = new MockitoMethod(String.class.getMethod("length", new Class[0])).getJavaMethod();
        Method testMethod2 = new MockitoMethod(String.class.getMethod("length", new Class[0])).getJavaMethod();
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(testMethod1);
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(testMethod2);

        boolean expected = true;
        boolean actual = delegatingMethod1.equals(delegatingMethod2);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testHashCode() {
        Method testMethod = new MockitoMethod(String.class.getMethod("length", new Class[0])).getJavaMethod();
        DelegatingMethod delegatingMethod1 = new DelegatingMethod(testMethod);
        Method testMethod2 = new MockitoMethod(String.class.getMethod("length", new Class[0])).getJavaMethod();
        DelegatingMethod delegatingMethod2 = new DelegatingMethod(testMethod2);

        int expected1 = 1;
        int expected2 = 1;
        int actual1 = delegatingMethod1.hashCode();
        int actual2 = delegatingMethod2.hashCode();

        Assert.assertEquals(expected1, actual1);
        Assert.assertEquals(expected2, actual2);
    }
}