import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.RealMethod;
import org.mockito.invocation.InvocationOnMock;

import java.lang.reflect.Method;
import java.util.Arrays;

public class InvocationTest {

    @Test
    public void testConstructor() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = MockitoTestUtils.createMockitoMethod(new Class[]{}, new Class[]{String.class}, "testMethod", mock);
        Object[] arguments = new Object[]{"test"};
        RealMethod realMethod = new RealMethod(mockitoMethod, new Object[]{String.class}, "testMethod", mock);

        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, realMethod);

        Assert.assertEquals(mock, invocation.getMock());
        Assert.assertEquals(mockitoMethod, invocation.getMethod());
        Assert.assertArrayEquals(arguments, invocation.getArguments());
        Assert.assertEquals(1, invocation.getSequenceNumber());
    }

    @Test
    public void testEqualArguments() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = MockitoTestUtils.createMockitoMethod(new Class[]{}, new Class[]{String.class}, "testMethod", mock);
        Object[] arguments1 = new Object[]{"test1"};
        Object[] arguments2 = new Object[]{"test2"};

        Invocation invocation1 = new Invocation(mock, mockitoMethod, arguments1, 1, null);
        Invocation invocation2 = new Invocation(mock, mockitoMethod, arguments2, 1, null);

        Assert.assertFalse(invocation1.equalArguments(arguments2));
    }

    @Test
    public void testIsVerified() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = MockitoTestUtils.createMockitoMethod(new Class[]{}, new Class[]{String.class}, "testMethod", mock);
        Object[] arguments = new Object[]{"test"};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, null);

        Assert.assertFalse(invocation.isVerified());

        invocation.markVerified();

        Assert.assertTrue(invocation.isVerified());
    }

    @Test
    public void testIsVerifiedInOrder() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = MockitoTestUtils.createMockitoMethod(new Class[]{}, new Class[]{String.class}, "testMethod", mock);
        Object[] arguments1 = new Object[]{"test1"};
        Object[] arguments2 = new Object[]{"test2"};

        Invocation invocation1 = new Invocation(mock, mockitoMethod, arguments1, 1, null);
        Invocation invocation2 = new Invocation(mock, mockitoMethod, arguments2, 2, null);

        Assert.assertFalse(invocation1.isVerifiedInOrder());
        invocation1.markVerifiedInOrder();
        invocation2.markVerifiedInOrder();

        Assert.assertTrue(invocation1.isVerifiedInOrder());
        Assert.assertTrue(invocation2.isVerifiedInOrder());
        Assert.assertEquals(2, invocation2.getSequenceNumber());
    }

    @Test(expected = RuntimeException.class)
    public void testHashCode() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = MockitoTestUtils.createMockitoMethod(new Class[]{}, new Class[]{String.class}, "testMethod", mock);
        Object[] arguments = new Object[]{"test"};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, null);

        invocation.hashCode();
    }

    @Test
    public void testToString() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = MockitoTestUtils.createMockitoMethod(new Class[]{}, new Class[]{String. class}, "testMethod", mock);
        Object[] arguments = new Object[]{"test"};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, null);

        String expected = "testMethod(String)";
        String result = invocation.getMethodName();
        Assert.assertEquals(expected, result);

        List<Matcher> matchers = invocation.argumentsToMatchers();
        String toString = invocation.toString(matchers, new PrintSettings());

        String expectedToString = "org.mockito.internal.invocation.Invocation@" + invocation.hashCode() + "\n" +
                "testMethod(String)\n" +
                "Arguments:\n" +
                "test\n";
        Assert.assertEquals(expectedToString, toString);
    }

    @Test
    public void testIsToString() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(PrintingFriendlyInvocation.class, "toString", new Class[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, null);

        Assert.assertFalse(Invocation.isToString(invocation));
    }

    @Test
    public void testIsValidException() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = MockitoTestUtils.createMockitoMethod(new Class[]{}, new Class[0], "testExceptionMethod", mock);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, null);

        Assert.assertFalse(invocation.isValidException(new RuntimeException("test")));

        Class[] exceptionTypes = mockitoMethod.getExceptionTypes();
        Assert.assertArrayEquals(new Class[]{RuntimeException.class}, exceptionTypes);

        Assert.assertTrue(invocation.isValidException(new RuntimeException("test")));
    }

    @Test
    public void testIsValidReturnType() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = MockitoTestUtils.createMockitoMethod(new Class[]{}, new Class[]{String.class}, "testMethod", mock);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, null);

        Assert.assertFalse(invocation.isValidReturnType(int.class));
        Assert.assertTrue(invocation.isValidReturnType(String.class));
    }

    @Test
    public void testIsVoid() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = MockitoTestUtils.createMockitoMethod(new Class[]{}, Void.TYPE, "testVoidMethod", mock);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, null);

        Assert.assertTrue(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = MockitoTestUtils.createMockitoMethod(new Class[]{}, new Class[]{String.class}, "testMethod", mock);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, null);

        String expected = "String";
        String result = invocation.printMethodReturnType();
        Assert.assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCallRealMethodWithNullMock() {
        Object mockitoMethod = null;
        Invocation invocation = new Invocation(null, (MockitoMethod) mockitoMethod, new Object[0], 1, null);

        invocation.callRealMethod();
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        Object mock = new Object();
        MockitoMethod mockitoMethod = MockitoTestUtils.createMockitoMethod(new Class[]{}, new Class[]{String.class}, "testMethod", mock);
        Object[] arguments = new Object[]{"test"};
        RealMethod realMethod = new RealMethod(mockitoMethod, new Class[]{String.class}, "testMethod", mock);
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, realMethod);

        String result = (String) invocation.callRealMethod();

        Assert.assertEquals("test", result);
    }
}