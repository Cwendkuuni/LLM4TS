import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.RealMethod;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.util.MockitoMethod;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectsMethodsGuru;
import org.mockito.internal.util.PrintSettings;
import org.mockito.internal.util.Primitives;
import org.mockito.internal.util.PrintingFriendlyInvocation;
import org.mockito.internal.util.PrintSettings.PrintType;

public class InvocationTest {

    @Test
    public void testConstructor() {
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[] { Integer.TYPE, String[].class }, "testMethod", mock.getClass());
        Object[] arguments = new Object[] { 1, new String[] { "test" } };
        RealMethod realMethod = new RealMethod(() -> System.out.println("Test"), new Class[] {}, String.class, null);

        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, realMethod);

        Assert.assertEquals(mock, invocation.getMock());
        Assert.assertEquals(mockitoMethod, invocation.getMethod());
        Assert.assertArrayEquals(arguments, invocation.getArguments());
        Assert.assertArrayEquals(invocation.getRawArguments(), new Object[] { mock, mockitoMethod, arguments });
        Assert.assertEquals(1, invocation.getSequenceNumber());
        Assert.assertFalse(invocation.isVerified());
        Assert.assertFalse(invocation.isVerifiedInOrder());
        Assert.assertFalse(invocation.isVerified());
        Assert.assertFalse(invocation.isVerifiedInOrder());
        Assert.assertNotNull(invocation.location);
        Assert.assertNull(invocation.realMethod.getInterceptableMethod());
    }

    @Test
    public void testToString() {
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[] { Integer.TYPE, String[].class }, "testMethod", mock.getClass());
        Object[] arguments = new Object[] { 1, new String[] { "test" } };
        RealMethod realMethod = new RealMethod(() -> System.out.println("Test"), new Class[] {}, String.class, null);

        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, realMethod);

        PrintSettings printSettings = new PrintSettings(PrintType.NORMAL);
        String expected = "testMethod(1, [" + "test" + "])";
        String actual = invocation.toString(printSettings);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testToStringWithMatchers() {
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[] { Integer.TYPE, String[].class }, "testMethod", mock.getClass());
        Object[] arguments = new Object[] { 1, new String[] { new Equals("test") } };
        RealMethod realMethod = new RealMethod(() -> System.out.println("Test"), new Class[] {}, String.class, null);

        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, realMethod);

        PrintSettings printSettings = new PrintSettings(PrintType.PRETTY_PRINT);
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(new Equals("test"));

        String expected = "testMethod(1, [" + matchers.get(0) + "])";
        String actual = invocation.toString(matchers, printSettings);

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testToStringWithMultiline() {
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[] { Integer.TYPE, String[].class }, "testMethod", mock.getClass());
        Object[] arguments = new Object[] { 1, new String[] { "test1", "test2" } };
        RealMethod realMethod = new RealMethod(() -> System.out.println("Test"), new Class[] {}, String.class, null);

        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, realMethod);

        PrintSettings printSettings = new PrintSettings(PrintType.MULTILINE);
        List<Matcher> matchers = new ArrayList<>();

        String expected = "testMethod(" +
                "1," +
                "[" +
                "  \"test1\"" +
                "," +
                "  \"test2\"" +
                "])";
        String actual = invocation.toString(matchers, printSettings);

        Assert.assertEquals(expected, actual);
    }

    @Test(expected = RuntimeException.class)
    public void testHashCode() {
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[] { Integer.TYPE, String[].class }, "testMethod", mock.getClass());
        Object[] arguments = new Object[] { 1, new String[] { "test" } };
        RealMethod realMethod = new RealMethod(() -> System.out.println("Test"), new Class[] {}, String.class, null);

        Invocation invocation1 = new Invocation(mock, mockitoMethod, arguments, 1, realMethod);
        Invocation invocation2 = new Invocation(mock, mockitoMethod, arguments, 1, realMethod);

        int hashCode1 = invocation1.hashCode();
        int hashCode2 = invocation2.hashCode();

        Assert.assertEquals(hashCode1, hashCode2);

        invocation1.markVerified();
        invocation2.markVerified();

        Assert.assertNotSame(hashCode1, hashCode2);
    }

    @Test
    public void testEquals() {
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[] { Integer.TYPE, String[].class }, "testMethod", mock.getClass());
        Object[] arguments = new Object[] { 1, new String[] { "test" } };
        RealMethod realMethod = new RealMethod(() -> System.out.println("Test"), new Class[] {}, String.class, null);

        Invocation invocation1 = new Invocation(mock, mockitoMethod, arguments, 1, realMethod);
        Invocation invocation2 = new Invocation(mock, mockitoMethod, arguments, 1, realMethod);

        Assert.assertTrue(invocation1.equals(invocation2));
        Assert.assertTrue(invocation2.equals(invocation1));

        invocation1.markVerified();
        invocation2.markVerified();

        Assert.assertFalse(invocation1.equals(invocation2));
        Assert.assertFalse(invocation2.equals(invocation1));

        Object[] differentArguments = new Object[] { 2, new String[] { "test" } };

        Invocation invocation3 = new Invocation(mock, mockitoMethod, arguments, 1, realMethod);

        Assert.assertFalse(invocation1.equals(invocation3));
        Assert.assertFalse(invocation3.equals(invocation1));
    }

    @Test
    public void testIsToString() {
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[] {}, "toString", mock.getClass());

        Invocation invocation = new Invocation(mock, mockitoMethod, null, 1, new RealMethod(() -> {}, new Class[] {}, String.class, null));

        Assert.assertTrue(Invocation.isToString(invocation));
    }

    @Test
    public void testIsVoid() {
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[] {}, "testMethod", mock.getClass());

        Invocation invocation = new Invocation(mock, mockitoMethod, null, 1, new RealMethod(() -> {}, new Class[] {}, Void.TYPE, null));

        Assert.assertTrue(invocation.isVoid());
    }

    @Test
    public void testIsValidException() {
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[] { Integer.TYPE }, "testMethod", mock.getClass());
        Throwable exception = new RuntimeException("Test exception");

        Invocation invocation = new Invocation(mock, mockitoMethod, null, 1, new RealMethod(() -> {}, new Class[] {}, String.class, null));

        Assert.assertFalse(invocation.isValidException(exception));

        MockitoMethod mockitoMethodWithException = new MockitoMethod(new Class[] { Integer.TYPE }, "testMethod", mock.getClass(), new Class[] { RuntimeException.class });

        Invocation invocationWithException = new Invocation(mock, mockitoMethodWithException, null, 1, new RealMethod(() -> {}, new Class[] {}, String.class, null));

        Assert.assertTrue(invocationWithException.isValidException(exception));
    }

    @Test
    public void testIsValidReturnType() {
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[] { Integer.TYPE }, "testMethod", mock.getClass());
        Class expectedReturnType = String.class;

        Invocation invocation = new Invocation(mock, mockitoMethod, null, 1, new RealMethod(() -> "", new Class[] {}, expectedReturnType, null));

        Assert.assertTrue(invocation.isValidReturnType(String.class));
        Assert.assertFalse(invocation.isValidReturnType(Integer.TYPE));
    }

    @Test
    public void testExpandVarArgs() {
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[] { Integer.TYPE, Integer[].class }, "testMethod", mock.getClass());

        Object[] arguments = new Object[] { 1, new Object[] { 2, 3 } };
        Object[] expandedArguments = Invocation.expandVarArgs(mockitoMethod.isVarArgs(), arguments);

        Assert.assertArrayEquals(new Object[] { 1, 2, 3 }, expandedArguments);
    }

    @Test
    public void testVerify() {
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[] { Integer.TYPE }, "testMethod", mock.getClass());
        RealMethod realMethod = new RealMethod(() -> {
        }, new Class[] {}, Integer.TYPE, null);

        Invocation invocation = new Invocation(mock, mockitoMethod, null, 1, realMethod);

        invocation.markVerified();

        Assert.assertTrue(invocation.isVerified());
        Assert.assertTrue(invocation.isVerifiedInOrder());
    }

    @Test(expected = IllegalStateException.class)
    public void testVerifyAfterVerifyInOrder() {
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[] { Integer.TYPE }, "testMethod", mock.getClass());
        RealMethod realMethod = new RealMethod(() -> {
        }, new Class[] {}, Integer.TYPE, null);

        Invocation invocation = new Invocation(mock, mockitoMethod, null, 1, realMethod);

        invocation.markVerifiedInOrder();
        invocation.markVerified();
    }

    @Test
    public void testVerifyInOrder() {
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[] { Integer.TYPE, Integer[].class }, "testMethod", mock.getClass());
        RealMethod realMethod = new RealMethod(() -> {
        }, new Class[] {}, Integer.TYPE, null);

        Invocation invocation1 = new Invocation(mock, mockitoMethod, new Object[] { 1 }, 1, realMethod);
        Invocation invocation2 = new Invocation(mock, mockitoMethod, new Object[] { 1 }, 2, realMethod);

        invocation1.markVerifiedInOrder();
        invocation2.markVerifiedInOrder();

        Assert.assertTrue(invocation1.isVerifiedInOrder());
        Assert.assertTrue(invocation2.isVerifiedInOrder());
    }

    @Test(expected = IllegalStateException.class)
    public void testVerifyInOrderAfterVerify() {
        Object mock = new Object() {};
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[] { Integer.TYPE, Integer[].class }, "testMethod", mock.getClass());
        RealMethod realMethod = new RealMethod(() -> {
        }, new Class[] {}, Integer.TYPE, null);

        Invocation invocation1 = new Invocation(mock, mockitoMethod, new Object[] { 1 }, 1, realMethod);
        Invocation invocation2 = new Invocation(mock, mockitoMethod, new Object[] { 1 }, 2, realMethod);

        invocation1.markVerified();
        invocation2.markVerifiedInOrder();
    }
}