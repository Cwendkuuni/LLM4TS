import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.RealMethod;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.Equals;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.exceptions.base.MockitoException;

public class InvocationTest {

    @Test
    public void testConstructorWithMockObjectAndMockitoMethodAndArgumentsAndSequenceNumberAndRealMethod() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new MockitoSession(), "testMethod", new Class[0], new Type[0]);
        Object[] arguments = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod(mockitoMethod, arguments);

        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, sequenceNumber, realMethod);

        assertEquals(mock, invocation.getMock());
        assertEquals(mockitoMethod, invocation.getMethod());
        assertEquals(arguments, invocation.getArguments());
        assertEquals(sequenceNumber, invocation.getSequenceNumber());
        assertEquals(realMethod, invocation.realMethod);
    }

    @Test
    public void testExpandVarArgs() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new MockitoSession(), "testMethod", new Class[]{int[].class}, new Type[0]);
        Object[] args = new Object[]{1, new int[0]};

        Object[] expandedArgs = Invocation.expandVarArgs(mockitoMethod.isVarArgs(), args);

        assertEquals(args.length, expandedArgs.length);
        assertTrue(args[args.length - 1] instanceof Object[]);
        assertEquals(args[0], expandedArgs[0]);
        assertEquals(args[1], expandedArgs[args.length - 1]);
    }

    @Test
    public void testGetMock() {
        Object mock = new Object();
        Invocation invocation = new Invocation(mock, new MockitoMethod(new MockitoSession(), "testMethod", new Class[0], new Type[0]), new Object[0], 1, null);
        assertEquals(mock, invocation.getMock());
    }

    @Test
    public void testGetMethod() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new MockitoSession(), "testMethod", new Class[0], new Type[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, null);
        assertEquals(mockitoMethod, invocation.getMethod());
    }

    @Test
    public void testGetArguments() {
        Object mock = new Object();
        Object[] arguments = new Object[]{"arg1", "arg2"};
        Invocation invocation = new Invocation(mock, new MockitoMethod(new MockitoSession(), "testMethod", new Class[0], new Type[0]), arguments, 1, null);
        assertEquals(arguments, invocation.getArguments());
    }

    @Test
    public void testIsVerified() {
        Invocation invocation = new Invocation(new Object(), new MockitoMethod(new MockitoSession(), "testMethod", new Class[0], new Type[0]), new Object[0], 1, null);
        assertFalse(invocation.isVerified());

        invocation.markVerified();

        assertTrue(invocation.isVerified());
    }

    @Test
    public void testIsVerifiedInOrder() {
        Invocation invocation = new Invocation(new Object(), new MockitoMethod(new MockitoSession(), "testMethod1", new Class[0], new Type[0]), new Object[0], 1, null);
        invocation.markVerifiedInOrder();

        assertTrue(invocation.isVerified());
        assertTrue(invocation.isVerifiedInOrder());
    }

    @Test(expected = RuntimeException.class)
    public void testHashCode() {
        Invocation invocation = new Invocation(new Object(), new MockitoMethod(new MockitoSession(), "testMethod", new Class[0], new Type[0]), new Object[0], 1, null);
        invocation.hashCode();
    }

    @Test
    public void testToString() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new MockitoSession(), "testMethod", new Class[0], new Type[0]);
        Object[] arguments = new Object[]{"arg1", "arg2"};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, null);

        List<Matcher> matchers = invocation.argumentsToMatchers();
        PrintSettings printSettings = new PrintSettings();

        String result = invocation.toString(matchers, printSettings);

        String expected = "testMethod(arg1, arg2)";
        assertEquals(expected, result);
    }

    @Test
    public void testToStringWithMultilineAndArgumentMatchers() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new MockitoSession(), "testMethod", new Class[]{String[].class}, new Type[0]);
        Object[] arguments = new Object[]{"arg1", new String[]{"arg2a", "arg2b"}};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, null);

        List<Matcher> matchers = invocation.argumentsToMatchers();
        PrintSettings printSettings = new PrintSettings();

        String result = invocation.toString(matchers, printSettings);

        String expected = "testMethod(\n" +
                "    arg1,\n" +
                "    [arg2a, arg2b]\n" +
                ")";
        assertEquals(expected, result);
    }

    @Test
    public void testIsToString() {
        MockitoMethod mockitoMethod = new MockitoMethod(new MockitoSession(), "toString", new Class[0], new Type[0]);
        assertTrue(Invocation.isToString(new Invocation(new Object(), mockitoMethod, new Object[0], 1, null)));
    }

    @Test(expected = MockitoException.class)
    public void testIsToStringWithNonToStringMethod() {
        MockitoMethod mockitoMethod = new MockitoMethod(new MockitoSession(), "nonToStringMethod", new Class[0], new Type[0]);
        Invocation invocation = new Invocation(new Object(), mockitoMethod, new Object[0], 1, null);
        invocation.isToString();
    }

    @Test
    public void testIsValidException() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new MockitoSession(), "testMethod", new Class[0], new Class[]{Exception.class});
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, null);

        Throwable throwable = new Exception();

        assertTrue(invocation.isValidException(throwable));
    }

    @Test
    public void testIsValidExceptionWithInvalidException() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new MockitoSession(), "testMethod", new Class[0], new Class[]{Exception.class});
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, null);

        Throwable throwable = new RuntimeException();

        assertFalse(invocation.isValidException(throwable));
    }

    @Test
    public void testIsValidReturnType() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new MockitoSession(), "testMethod", new Class[]{Integer.class}, new Type[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, null);

        assertTrue(invocation.isValidReturnType(Integer.class));
    }

    @Test
    public void testIsValidReturnTypeWithInvalidClass() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new MockitoSession(), "testMethod", new Class[]{Integer.class}, new Type[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, null);

        assertFalse(invocation.isValidReturnType(String.class));
    }

    @Test
    public void testIsVoid() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new MockitoSession(), "testMethod", Void.TYPE, new Type[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, null);

        assertTrue(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new MockitoSession(), "testMethod", new Class[]{Integer.class}, new Type[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, null);

        assertEquals("Integer", invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new MockitoSession(), "testMethod", new Class[]{Integer.class}, new Type[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, null);

        assertEquals("testMethod", invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new MockitoSession(), "testMethod", byte.class, new Type[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, null);

        assertTrue(invocation.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        Object mock = new Object();
        Invocation invocation = new Invocation(mock, new MockitoMethod(new MockitoSession(), "testMethod", new Class[0], new Type[0]), new Object[0], 1, null);
        assertEquals(new Location(), invocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new MockitoSession(), "testMethod", new Class[0], new Type[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, null);

        assertEquals(0, invocation.getArgumentsCount());
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new MockitoSession(), "testMethod", int[].class, new Type[0]);
        Object[] arguments = new Object[]{new int[]{1, 2, 3}};
        RealMethod realMethod = new RealMethod(mockitoMethod, arguments);
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, realMethod);

        int result = invocation.callRealMethod();

        assertEquals(6, result);
    }
}