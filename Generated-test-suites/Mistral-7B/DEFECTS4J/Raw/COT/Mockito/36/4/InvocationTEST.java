import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.Equals;
import org.mockito.invocation.InvocationOnMock;

public class InvocationTest {

    @Test
    public void testConstructor() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod("methodName", new Class[0]);
        Object[] arguments = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod(() -> {});

        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, sequenceNumber, realMethod);

        Assert.assertEquals(mock, invocation.getMock());
        Assert.assertEquals(mockitoMethod, invocation.getMethod());
        Assert.assertArrayEquals(arguments, invocation.getArguments());
        Assert.assertEquals(sequenceNumber, invocation.getSequenceNumber());
        Assert.assertNotNull(invocation.getLocation());
        Assert.assertFalse(invocation.isVerified());
        Assert.assertFalse(invocation.isVerifiedInOrder());
        Assert.assertNull(invocation.realMethod);
    }

    @Test
    public void testExpandVarArgs() {
        Object[] varArgs = new Object[]{1, new Object[]{2, 3}};
        Object[] expected = new Object[]{1, 2, 3};

        Object[] result = Invocation.expandVarArgs(true, varArgs);

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testGetMock() {
        Object mock = new Object();
        Invocation invocation = new Invocation(mock, null, new Object[0], 0, null);

        Assert.assertEquals(mock, invocation.getMock());
    }

    @Test
    public void testGetMethod() {
        MockitoMethod mockitoMethod = new MockitoMethod("methodName", new Class[0]);
        Invocation invocation = new Invocation(null, mockitoMethod, new Object[0], 0, null);

        Assert.assertEquals(mockitoMethod, invocation.getMethod());
    }

    @Test
    public void testGetArguments() {
        Object[] arguments = new Object[]{"arg1", "arg2"};
        Invocation invocation = new Invocation(null, null, arguments, 0, null);

        Assert.assertArrayEquals(arguments, invocation.getArguments());
    }

    @Test
    public void testIsVerified() {
        Invocation invocation = new Invocation(null, null, new Object[0], 0, null);

        Assert.assertFalse(invocation.isVerified());
    }

    @Test
    public void testGetSequenceNumber() {
        Invocation invocation = new Invocation(null, null, new Object[0], 1, null);

        Assert.assertEquals(1, invocation.getSequenceNumber());
    }

    @Test
    public void testIsVerifiedInOrder() {
        Invocation invocation = new Invocation(null, null, new Object[0], 0, null);

        Assert.assertFalse(invocation.isVerifiedInOrder());
    }

    @Test
    public void testEquals() {
        Object mock1 = new Object();
        Object mock2 = new Object();
        MockitoMethod method1 = new MockitoMethod("methodName", new Class[0]);
        MockitoMethod method2 = new MockitoMethod("methodName", new Class[0]);
        Object[] arguments1 = new Object[]{"arg1", "arg2"};
        Object[] arguments2 = new Object[]{"arg1", "arg2"};

        Invocation invocation1 = new Invocation(mock1, method1, arguments1, 0, null);
        Invocation invocation2 = new Invocation(mock2, method2, arguments2, 0, null);

        Assert.assertTrue(invocation1.equals(invocation1));
        Assert.assertTrue(invocation1.equals(invocation2));
        Assert.assertTrue(invocation2.equals(invocation1));
        Assert.assertFalse(invocation1.equals(null));
        Assert.assertFalse(invocation1.equals(new Object()));
        Assert.assertFalse(invocation1.equals(new Integer(1)));

        Object[] arguments3 = new Object[]{"arg1", "arg3"};

        Assert.assertFalse(invocation1.equals(new Invocation(mock1, method1, arguments3, 0, null)));
        Assert.assertFalse(invocation1.equals(new Invocation(mock2, method2, arguments1, 1, null)));
        Assert.assertFalse(invocation1.equals(new Invocation(mock1, method2, arguments1, 0, null)));
    }

    @Test
    public void testToString() {
        MockitoMethod mockitoMethod = new MockitoMethod("methodName", new Class[0]);
        Object[] arguments = new Object[]{"arg1", "arg2"};

        Invocation invocation = new Invocation(null, mockitoMethod, arguments, 0, null);

        List<Matcher> matchers = invocation.argumentsToMatchers();
        String expected = "methodName(arg1, arg2)";

        Assert.assertEquals(expected, invocation.toString());
    }

    @Test
    public void testIsToString() {
        MockitoMethod mockitoMethod = new MockitoMethod("toString", new Class[0]);

        Assert.assertTrue(Invocation.isToString(new Invocation(null, mockitoMethod, new Object[0], 0, null)));
    }

    @Test(expected = RuntimeException.class)
    public void testHashCode() {
        Invocation invocation = new Invocation(null, null, new Object[0], 0, null);

        invocation.hashCode();
    }

    @Test
    public void testValidateExceptions() {
        Invocation invocation = new Invocation(null, new MockitoMethod("methodWithException", new Class[]{Integer.class}), new Object[0], 0, null);

        Assert.assertFalse(invocation.isValidException(new IllegalArgumentException()));
        Assert.assertFalse(invocation.isValidException(new NullPointerException()));
        Assert.assertTrue(invocation.isValidException(new RuntimeException()));
        Assert.assertTrue(invocation.isValidException(new NumberFormatException()));
        Assert.assertTrue(invocation.isValidException(new Exception("error message")));
    }

    @Test
    public void testValidateReturnType() {
        Invocation invocation = new Invocation(null, new MockitoMethod("methodWithPrimitiveReturnType", new Class[]{Integer.class}), new Object[0], 0, null);

        Assert.assertFalse(invocation.isValidReturnType(String.class));
        Assert.assertTrue(invocation.isValidReturnType(int.class));
        Assert.assertTrue(invocation.isValidReturnType(Integer.class));
        Assert.assertTrue(invocation.isValidReturnType(Void.TYPE));
    }

    @Test
    public void testIsVoid() {
        Invocation invocation = new Invocation(null, new MockitoMethod("methodWithVoidReturnType", new Class[0]), new Object[0], 0, null);

        Assert.assertTrue(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        Invocation invocation = new Invocation(null, new MockitoMethod("methodWithStringReturnType", new Class[]{Integer.class}), new Object[0], 0, null);

        Assert.assertEquals("String", invocation.printMethodReturnType());
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation(null, null, new Object[0], 0, null);

        Assert.assertNotNull(invocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        Invocation invocation = new Invocation(null, null, new Object[]{"arg1", "arg2"}, 0, null);

        Assert.assertEquals(2, invocation.getArgumentsCount());
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        Object mock = new Object();
        RealMethod realMethod = new RealMethod(() -> "return value");
        InvocationOnMock invocationOnMock = Mockito.mock(mock.getClass());

        Invocation invocation = new Invocation(invocationOnMock, null, new Object[0], 0, realMethod);

        Assert.assertEquals("return value", invocation.callRealMethod());
    }
}