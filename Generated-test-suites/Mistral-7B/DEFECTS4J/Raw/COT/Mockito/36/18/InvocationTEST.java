import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.*;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.RealMethod;
import java.lang.reflect.Method;
import java.util.Arrays;

public class InvocationTest {

    @Test
    public void testConstructor() {
        Mockito mockito = Mockito.mock(Object.class);
        Method method = Mockito.method(Object.class, "methodWithOneArgument", new Class[]{Integer.class});
        Object[] arguments = new Object[]{1};
        Invocation invocation = new Invocation(mockito, Mockito.stub(method), arguments, 1, new RealMethod(mockito, method));

        Assert.assertEquals(mockito, invocation.getMock());
        Assert.assertEquals(method, invocation.getMethod());
        Assert.assertArrayEquals(arguments, invocation.getArguments());
        Assert.assertEquals(1, invocation.getSequenceNumber());
    }

    @Test
    public void testExpandVarArgs() {
        Object[] args = new Object[]{1, new Object[]{2, 3}};
        Object[] expectedArgs = new Object[]{1, 2, 3};

        Invocation invocation = new Invocation(null, null, args, 1, null);
        Object[] result = invocation.expandVarArgs(true, args);

        Assert.assertArrayEquals(expectedArgs, result);
    }

    @Test
    public void testGetMock() {
        Mockito mockito = Mockito.mock(Object.class);
        Invocation invocation = new Invocation(mockito, null, null, 1, null);

        Assert.assertEquals(mockito, invocation.getMock());
    }

    @Test
    public void testGetMethod() {
        Class<?> clazz = Object.class;
        Method method = Mockito.method(clazz, "methodWithOneArgument", new Class[]{Integer.class});
        Invocation invocation = new Invocation(null, method, null, 1, null);

        Assert.assertEquals(method, invocation.getMethod());
    }

    @Test
    public void testGetArguments() {
        Object[] arguments = new Object[]{1};
        Invocation invocation = new Invocation(null, null, arguments, 1, null);

        Assert.assertArrayEquals(arguments, invocation.getArguments());
    }

    @Test
    public void testIsVerified() {
        Invocation invocation = new Invocation(null, null, null, 1, null);

        Assert.assertFalse(invocation.isVerified());
    }

    @Test
    public void testIsVerifiedInOrder() {
        Invocation invocation = new Invocation(null, null, null, 1, null);

        Assert.assertFalse(invocation.isVerifiedInOrder());
    }

    @Test(expected = RuntimeException.class)
    public void testHashCode() {
        Invocation invocation = new Invocation(null, null, null, 1, null);
        invocation.hashCode();
    }

    @Test
    public void testToString() {
        Mockito mockito = Mockito.mock(Object.class);
        Method method = Mockito.method(Object.class, "methodWithOneArgument", new Class[]{Integer.class});
        Object[] arguments = new Object[]{1};
        Invocation invocation = new Invocation(mockito, method, arguments, 1, new RealMethod(mockito, method));

        String expected = "1 org.mockito.internal.invocation.Invocation: methodWithOneArgument(int)";
        String result = invocation.toString();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testEquals() {
        Mockito mockito1 = Mockito.mock(Object.class);
        Mockito mockito2 = Mockito.mock(Object.class);
        Method method = Mockito.method(Object.class, "methodWithOneArgument", new Class[]{Integer.class});
        Object[] arguments1 = new Object[]{1};
        Object[] arguments2 = new Object[]{2};

        Invocation invocation1 = new Invocation(mockito1, method, arguments1, 1, new RealMethod(mockito1, method));
        Invocation invocation2 = new Invocation(mockito2, method, arguments2, 2, new RealMethod(mockito2, method));
        Invocation invocation3 = new Invocation(mockito1, method, arguments1, 1, new RealMethod(mockito1, method));

        Assert.assertFalse(invocation1.equals(invocation2));
        Assert.assertTrue(invocation1.equals(invocation3));
    }

    @Test
    public void testIsToString() {
        Method method = Mockito.method(Object.class, "toString", new Class[0]);
        Invocation invocation = new Invocation(Mockito.mock(Object.class), method, new Object[0], 1, null);

        Assert.assertTrue(Invocation.isToString(invocation));
    }

    @Test
    public void testIsValidException() {
        Mockito mockito = Mockito.mock(Object.class);
        Method method = Mockito.method(Object.class, "methodWithException", new Class[]{});
        Throwable exception = new RuntimeException("Test exception");

        Invocation invocation = new Invocation(mockito, method, null, 1, new RealMethod(mockito, method));

        Assert.assertFalse(invocation.isValidException(exception));
    }

    @Test
    public void testIsValidReturnType() {
        Invocation invocation = new Invocation(null, null, null, 1, null);
        Class<String> clazz = String.class;

        Assert.assertFalse(invocation.isValidReturnType(clazz));
    }

    @Test
    public void testIsVoid() {
        Method method = Mockito.method(Object.class, "voidMethod", new Class[0]);
        Invocation invocation = new Invocation(null, method, null, 1, new RealMethod(null, method));

        Assert.assertTrue(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        Method method = Mockito.method(Object.class, "methodWithStringReturnType", new Class[]{String.class});
        Invocation invocation = new Invocation(null, method, null, 1, new RealMethod(null, method));

        String expected = "String";
        String result = invocation.printMethodReturnType();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetMethodName() {
        Method method = Mockito.method(Object.class, "methodWithOneArgument", new Class[]{Integer.class});
        Invocation invocation = new Invocation(null, method, null, 1, new RealMethod(null, method));

        String expected = "methodWithOneArgument";
        String result = invocation.getMethodName();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testReturnsPrimitive() {
        Method method = Mockito.method(Object.class, "methodWithBooleanReturnType", new Class[]{});
        Invocation invocation = new Invocation(null, method, null, 1, new RealMethod(null, method));

        Assert.assertTrue(invocation.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation(null, null, null, 1, null);

        Assert.assertNotNull(invocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        Object[] arguments = new Object[]{1, 2, 3};
        Invocation invocation = new Invocation(null, null, arguments, 1, null);

        Assert.assertEquals(arguments.length, invocation.getArgumentsCount());
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        Mockito mockito = Mockito.mock(Object.class);
        Method method = Mockito.method(Object.class, "methodWithOneArgument", new Class[]{Integer.class});
        Object[] arguments = new Object[]{1};
        Invocation invocation = new Invocation(mockito, method, arguments, 1, new RealMethod(mockito, method));

        Object result = invocation.callRealMethod();

        Assert.assertEquals(result, method.invoke(mockito, arguments));
    }
}