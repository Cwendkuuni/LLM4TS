import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.RealMethod;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.Primitives;
import org.mockito.internal.util.ObjectMethodsGuru;

public class InvocationTest {

    @Test
    public void testConstructor() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(Void.TYPE, "testMethod", new Class[0]);
        Object[] rawArguments = new Object[0];
        RealMethod realMethod = new RealMethod(mockitoMethod, mock, rawArguments) {
            @Override
            public Object invoke(Object obj, Object[] objects) throws Throwable {
                return null;
            }
        };
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, realMethod);

        Assert.assertEquals(mock, invocation.getMock());
        Assert.assertEquals(mockitoMethod, invocation.getMethod());
        Assert.assertArrayEquals(new Object[0], invocation.getArguments());
        Assert.assertArrayEquals(new Object[0], invocation.getRawArguments());
        Assert.assertEquals(1, invocation.getSequenceNumber());
    }

    @Test
    public void testGetMock() {
        Object mock = new Object();
        Invocation invocation = new Invocation(mock, null, null, 1, null);

        Assert.assertEquals(mock, invocation.getMock());
    }

    @Test
    public void testGetMethod() {
        MockitoMethod mockitoMethod = new MockitoMethod(String.class, "testMethod", new Class[0]);
        Invocation invocation = new Invocation(new Object(), mockitoMethod, new Object[0], 1, null);

        Assert.assertEquals(mockitoMethod, invocation.getMethod());
    }

    @Test
    public void testGetArguments() {
        Object mock = new Object();
        Object[] arguments = new Object[]{"arg1", new Object[]{1, 2}};
        Invocation invocation = new Invocation(mock, null, arguments, 1, null);

        Assert.assertArrayEquals(arguments, invocation.getArguments());
    }

    @Test
    public void testIsVerified() {
        Invocation invocation = new Invocation(new Object(), null, new Object[0], 1, null);

        Assert.assertFalse(invocation.isVerified());

        invocation.markVerified();

        Assert.assertTrue(invocation.isVerified());
    }

    @Test
    public void testIsVerifiedInOrder() {
        Invocation invocation1 = new Invocation(new Object(), null, new Object[0], 1, null);
        Invocation invocation2 = new Invocation(new Object(), null, new Object[0], 2, null);

        Assert.assertFalse(invocation1.isVerifiedInOrder());
        Assert.assertFalse(invocation2.isVerifiedInOrder());

        invocation1.markVerifiedInOrder();
        invocation2.markVerifiedInOrder();

        Assert.assertTrue(invocation1.isVerifiedInOrder());
        Assert.assertTrue(invocation2.isVerifiedInOrder());

        invocation1.markVerified();
        invocation2.markVerified();

        Assert.assertFalse(invocation1.isVerifiedInOrder());
        Assert.assertFalse(invocation2.isVerifiedInOrder());
    }

    @Test(expected = RuntimeException.class)
    public void testHashCode() {
        Invocation invocation = new Invocation(new Object(), null, new Object[0], 1, null);
        invocation.hashCode();
    }

    @Test
    public void testToString() {
        Object[] arguments = new Object[]{"arg1", new Object[]{1, 2}};
        MockitoMethod mockitoMethod = new MockitoMethod(String.class, "testMethod", new Class[0]);
        Invocation invocation = new Invocation(new Object(), mockitoMethod, arguments, 1, null);

        List<Matcher> matchers = invocation.argumentsToMatchers();
        PrintSettings printSettings = new PrintSettings();

        String expected = "testMethod(arg1, [1, 2])";
        String result = invocation.toString(matchers, printSettings);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testToStringWithMultiline() {
        Object[] arguments = new Object[]{"arg1", new Object[]{1, 2}};
        MockitoMethod mockitoMethod = new MockitoMethod(String.class, "testMethod", new Class[0]);
        Invocation invocation = new Invocation(new Object(), mockitoMethod, arguments, 1, null);

        List<Matcher> matchers = invocation.argumentsToMatchers();
        PrintSettings printSettings = new PrintSettings(true);

        String expected = "testMethod(arg1,\n [1,\n  2]\n)";
        String result = invocation.toString(matchers, printSettings);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsToString() {
        MockitoMethod mockitoMethod = new MockitoMethod(String.class, "toString", new Class[]{});
        Invocation invocation = new Invocation(new Object(), mockitoMethod, new Object[0], 1, null);

        Assert.assertTrue(Invocation.isToString(invocation));
    }

    @Test
    public void testIsValidException() {
        MockitoMethod mockitoMethod = new MockitoMethod(String.class, "testMethod", new Class[]{Throwable.class});
        Invocation invocation = new Invocation(new Object(), mockitoMethod, new Object[0], 1, null);

        Throwable throwable = new Throwable();

        Assert.assertTrue(invocation.isValidException(throwable));
    }

    @Test
    public void testIsValidReturnType() {
        Invocation invocation = new Invocation(new Object(), new MockitoMethod(String.class, "testMethod", new Class[0]), new Object[0], 1, null);

        Assert.assertTrue(invocation.isValidReturnType(String.class));
    }

    @Test
    public void testIsVoid() {
        Invocation invocation = new Invocation(new Object(), new MockitoMethod(Void.TYPE, "testMethod", new Class[0]), new Object[0], 1, null);

        Assert.assertTrue(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        Invocation invocation = new Invocation(new Object(), new MockitoMethod(Integer.TYPE, "testMethod", new Class[0]), new Object[0], 1, null);

        Assert.assertEquals("int", invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        Invocation invocation = new Invocation(new Object(), new MockitoMethod(String.class, "testMethod", new Class[0]), new Object[0], 1, null);

        Assert.assertEquals("testMethod", invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        Invocation invocation = new Invocation(new Object(), new MockitoMethod(boolean.class, "testMethod", new Class[0]), new Object[0], 1, null);

        Assert.assertTrue(invocation.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        Invocation invocation = new Invocation(new Object(), null, new Object[0], 1, null);

        Assert.assertNotNull(invocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        Invocation invocation = new Invocation(new Object(), null, new Object[0], 1, null);

        Assert.assertEquals(0, invocation.getArgumentsCount());

        Object[] arguments = new Object[]{"arg1", new Object[]{1, 2}};
        Invocation invocationWithArguments = new Invocation(new Object(), null, arguments, 1, null);

        Assert.assertEquals(2, invocationWithArguments.getArgumentsCount());
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(Void.TYPE, "testMethod", new Class[0]);
        Object[] rawArguments = new Object[0];
        RealMethod realMethod = new RealMethod(mockitoMethod, mock, rawArguments) {
            @Override
            public Object invoke(Object obj, Object[] objects) throws Throwable {
                return "result";
            }
        };
        Invocation invocation = new Invocation(mock, mockitoMethod, rawArguments, 1, realMethod);

        String result = (String) invocation.callRealMethod();

        Assert.assertEquals("result", result);
    }
}