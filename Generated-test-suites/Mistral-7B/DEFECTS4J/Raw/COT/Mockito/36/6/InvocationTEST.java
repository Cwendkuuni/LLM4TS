import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.RealMethod;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.MockitoMethod;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.util.Primitives;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class InvocationTest {

    @Test
    public void testConstructor() {
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = new MockitoMethod(new Method("testMethod", Integer[].class), mock.getClass());
        Object[] arguments = new Object[]{1, new Integer[0]};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, new RealMethod(mockitoMethod));

        Assert.assertEquals(mock, invocation.getMock());
        Assert.assertEquals(mockitoMethod, invocation.getMethod());
        Assert.assertEquals(arguments, invocation.getArguments());
        Assert.assertArrayEquals(arguments, invocation.getRawArguments());
        Assert.assertEquals(1, invocation.getSequenceNumber());
        Assert.assertFalse(invocation.isVerified());
        Assert.assertFalse(invocation.isVerifiedInOrder());
    }

    @Test
    public void testEquals() {
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = new MockitoMethod(new Method("testMethod", Integer.class), mock.getClass());
        Object[] arguments1 = new Object[]{1};
        Object[] arguments2 = new Object[]{1};
        Invocation invocation1 = new Invocation(mock, mockitoMethod, arguments1, 1, new RealMethod(mockitoMethod));
        Invocation invocation2 = new Invocation(mock, mockitoMethod, arguments2, 1, new RealMethod(mockitoMethod));

        Assert.assertEquals(invocation1, invocation1);
        Assert.assertEquals(invocation1, invocation2);
        Assert.assertNotEquals(invocation1, null);
        Assert.assertNotEquals(invocation1, new Object());

        Object[] argumentsDifferentLength = new Object[]{1, new Integer[0]};
        Invocation invocationDifferentArguments = new Invocation(mock, mockitoMethod, argumentsDifferentLength, 1, new RealMethod(mockitoMethod));
        Assert.assertNotEquals(invocation1, invocationDifferentArguments);

        Object[] argumentsDifferent = new Object[]{1, "string"};
        Invocation invocationDifferentArgumentsType = new Invocation(mock, mockitoMethod, argumentsDifferent, 1, new RealMethod(mockitoMethod));
        Assert.assertNotEquals(invocation1, invocationDifferentArgumentsType);

        Object mockDifferent = new Object() {};
        Invocation invocationDifferentMock = new Invocation(mockDifferent, mockitoMethod, arguments1, 1, new RealMethod(mockitoMethod));
        Assert.assertNotEquals(invocation1, invocationDifferentMock);

        MockitoMethod differentMethod = new MockitoMethod(new Method("testMethod2", Integer.class), mock.getClass());
        Invocation invocationDifferentMethod = new Invocation(mock, differentMethod, arguments1, 1, new RealMethod(differentMethod));
        Assert.assertNotEquals(invocation1, invocationDifferentMethod);
    }

    @Test
    public void testToString() {
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = new MockitoMethod(new Method("testMethod", Integer.class), mock.getClass());
        Object[] arguments = new Object[]{1};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, new RealMethod(mockitoMethod));

        List<Matcher> matchers = invocation.argumentsToMatchers();
        PrintSettings printSettings = new PrintSettings();
        String expected = "testMethod(1)";
        String actual = invocation.toString(matchers, printSettings);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testToStringMultiline() {
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = new MockitoMethod(new Method("testMethod", Integer[].class, Integer.class), mock.getClass());
        Object[] arguments = new Object[]{new Integer[]{1, 2}, 3};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, new RealMethod(mockitoMethod));

        List<Matcher> matchers = invocation.argumentsToMatchers();
        PrintSettings printSettings = new PrintSettings();
        String expected = "testMethod([1, 2], 3)";
        String actual = invocation.toString(matchers, printSettings);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsToString() {
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = new MockitoMethod(new Method("toString", Void.TYPE), mock.getClass());
        Invocation invocation = new Invocation(mock, mockitoMethod, null, 1, new RealMethod(mockitoMethod));

        Assert.assertTrue(Invocation.isToString(invocation));
    }

    @Test
    public void testIsVoid() {
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = new MockitoMethod(new Method("voidMethod", Void.TYPE), mock.getClass());
        Invocation invocation = new Invocation(mock, mockitoMethod, null, 1, new RealMethod(mockitoMethod));

        Assert.assertTrue(invocation.isVoid());
    }

    @Test
    public void testGetMethodName() {
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = new MockitoMethod(new Method("testMethod", Integer.class), mock.getClass());
        Invocation invocation = new Invocation(mock, mockitoMethod, null, 1, new RealMethod(mockitoMethod));

        String expected = "testMethod";
        String actual = invocation.getMethodName();

        Assert.assertEquals(expected, actual);
    }

    @Test (expected = RuntimeException.class)
    public void testHashCode() {
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = new MockitoMethod(new Method("testMethod", Integer.class), mock.getClass());
        Object[] arguments = new Object[]{1};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, new RealMethod(mockitoMethod));

        invocation.hashCode();
    }

    @Test
    public void testIsVerified() {
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = new MockitoMethod(new Method("testMethod", Integer.class), mock.getClass());
        Object[] arguments = new Object[]{1};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, new RealMethod(mockitoMethod));

        Assert.assertFalse(invocation.isVerified());

        invocation.markVerified();
        Assert.assertTrue(invocation.isVerified());
    }

    @Test
    public void testIsVerifiedInOrder() {
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = new MockitoMethod(new Method("testMethod", Integer.class), mock.getClass());
        Object[] arguments = new Object[]{1};
        Invocation invocation1 = new Invocation(mock, mockitoMethod, arguments, 1, new RealMethod(mockitoMethod));
        Invocation invocation2 = new Invocation(mock, mockitoMethod, arguments, 2, new RealMethod(mockitoMethod));

        Assert.assertFalse(invocation1.isVerifiedInOrder());

        invocation1.markVerifiedInOrder();
        Assert.assertTrue(invocation1.isVerifiedInOrder());
        Assert.assertFalse(invocation2.isVerifiedInOrder());

        invocation2.markVerifiedInOrder();
        Assert.assertTrue(invocation2.isVerifiedInOrder());
    }

    @Test
    public void testGetLocation() {
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = new MockitoMethod(new Method("testMethod", Integer.class), mock.getClass());
        Object[] arguments = new Object[]{1};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, new RealMethod(mockitoMethod));

        Assert.assertNotNull(invocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = new MockitoMethod(new Method("testMethod", Integer[].class), mock.getClass());
        Object[] arguments = new Object[]{new Integer[]{1, 2}};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, new RealMethod(mockitoMethod));

        Assert.assertEquals(2, invocation.getArgumentsCount());
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = new MockitoMethod(new Method("testMethod", Integer.class), mock.getClass());
        Object[] arguments = new Object[]{1};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, new RealMethod(mockitoMethod));

        Integer expected = 2;
        Method realMethod = mockitoMethod.getRealMethod();
        Integer actual = (Integer) invocation.callRealMethod();

        Assert.assertEquals(expected, actual);
    }

    @Test (expected = Throwable.class)
    public void testCallRealMethodWithException() throws Throwable {
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = new MockitoMethod(new Method("testMethod", Integer.class), mock.getClass());
        Object[] arguments = new Object[]{1};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, new RealMethod(mockitoMethod));

        Class<?> expectedExceptionType = IllegalArgumentException.class;
        throw new IllegalArgumentException();

        // This line should not be executed because of the exception above
        Integer actual = (Integer) invocation.callRealMethod();
    }

    @Test
    public void testIsValidReturnType() {
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = new MockitoMethod(new Method("testMethod", Integer.class), mock.getClass());
        Invocation invocation = new Invocation(mock, mockitoMethod, null, 1, new RealMethod(mockitoMethod));

        Class<?> expected = Integer.class;
        Class<?> actual = invocation.isValidReturnType(Integer.class);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsValidReturnTypePrimitive() {
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = new MockitoMethod(new Method("testMethod", int.class), mock.getClass());
        Invocation invocation = new Invocation(mock, mockitoMethod, null, 1, new RealMethod(mockitoMethod));

        Class<?> expected = int.class;
        Class<?> actual = invocation.isValidReturnType(int.class);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsValidReturnTypeWrongType() {
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = new MockitoMethod(new Method("testMethod", Integer.class), mock.getClass());
        Invocation invocation = new Invocation(mock, mockitoMethod, null, 1, new RealMethod(mockitoMethod));

        Class<?> expected = String.class;
        Class<?> actual = invocation.isValidReturnType(String.class);

        Assert.assertFalse(actual.isAssignableFrom(expected));
    }

    @Test
    public void testIsValidException() {
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = new MockitoMethod(new Method("testMethod", Integer.class), mock.getClass());
        Invocation invocation = new Invocation(mock, mockitoMethod, null, 1, new RealMethod(mockitoMethod));

        Class<?> expectedExceptionType = IllegalArgumentException.class;
        Throwable exception = new IllegalArgumentException();

        Assert.assertTrue(invocation.isValidException(exception));
    }

    @Test
    public void testIsValidExceptionWrongType() {
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = new MockitoMethod(new Method("testMethod", Integer.class), mock.getClass());
        Invocation invocation = new Invocation(mock, mockitoMethod, null, 1, new RealMethod(mockitoMethod));

        Class<?> expectedExceptionType = RuntimeException.class;
        Throwable exception = new RuntimeException();

        Assert.assertFalse(invocation.isValidException(exception));
    }
}