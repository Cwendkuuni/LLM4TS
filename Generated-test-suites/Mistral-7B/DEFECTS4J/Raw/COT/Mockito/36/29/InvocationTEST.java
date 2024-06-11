import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.matchers.ArgumentsMatchers;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.reporting.PrintSettings;

public class InvocationTest {

    @Test
    public void testConstructor_ValidArguments() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[0], "methodName", new Class[0]);
        Object[] args = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod(mock, mockitoMethod, args, sequenceNumber);

        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);

        assertEquals(mock, invocation.getMock());
        assertEquals(mockitoMethod, invocation.getMethod());
        assertArrayEquals(args, invocation.getArguments());
        assertEquals(sequenceNumber, invocation.getSequenceNumber());
    }

    @Test
    public void testConstructor_VarArgs() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(String[].class, "methodName", new Class[]{String[].class});
        Object[] args = new Object[]{new Object[]{"arg1", "arg2"}};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod(mock, mockitoMethod, args, sequenceNumber);

        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);

        assertEquals(mock, invocation.getMock());
        assertEquals(mockitoMethod, invocation.getMethod());
        assertArrayEquals(args, invocation.getArguments());
        assertEquals(sequenceNumber, invocation.getSequenceNumber());
    }

    @Test(expected = RuntimeException.class)
    public void testHashCode_NotImplemented() {
        Invocation invocation = new Invocation(null, null, null, 0, null);
        invocation.hashCode();
    }

    @Test
    public void testToString_EmptyArguments() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[0], "methodName", new Class[0]);
        Object[] arguments = new Object[0];
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, null);

        String result = invocation.toString();
        String expected = mock.getClass().getName() + "." + mockitoMethod.getName() + "()";
        assertEquals(expected, result);
    }

    @Test
    public void testToString_ArgumentsWithMatchers() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[0], "methodName", new Class[0]);
        Object[] arguments = new Object[]{new Integer(1), ArgumentMatchers.isNull()};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, null);

        List<Matcher> matchers = invocation.argumentsToMatchers();
        PrintSettings printSettings = new PrintSettings();
        String result = invocation.toString(matchers, printSettings);

        String expected = mock.getClass().getName() + "." + mockitoMethod.getName() + "(" +
                matchers.get(0).toString() + ", " +
                matchers.get(1).toString() + ")";
        assertEquals(expected, result);
    }

    @Test
    public void testToString_MultilineArgMacters() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[0], "methodName", new Class[0]);
        Object[] arguments = new Object[]{new Object[]{new Integer(1), new Integer(2)}, new String[]{"str1", "str2"}};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, null);

        List<Matcher> matchers = invocation.argumentsToMatchers();
        PrintSettings printSettings = new PrintSettings();
        printSettings.setMultiline(true);
        String result = invocation.toString(matchers, printSettings);

        String expected = mock.getClass().getName() + "." + mockitoMethod.getName() + "(" +
                "[[1], [2]]," +
                "[\"str1\", \"str2\"]]";
        assertEquals(expected, result);
    }

    @Test
    public void testIsToString_InvocationWithToStringMethod() {
        Object mock = new Object() {
            @Override
            public String toString() {
                return "mock";
            }
        };
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[0], "toString", new Class[0]);
        Object[] arguments = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod(mock, mockitoMethod, arguments, sequenceNumber);

        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, sequenceNumber, realMethod);

        assertTrue(Invocation.isToString(invocation));
    }

    @Test
    public void testIsToString_InvocationWithoutToStringMethod() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[0], "toString", new Class[0]);
        Object[] arguments = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod(mock, mockitoMethod, arguments, sequenceNumber);

        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, sequenceNumber, realMethod);

        assertFalse(Invocation.isToString(invocation));
    }

    @Test(expected = AssertionError.class)
    public void testIsValidException_InvalidExceptionType() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[0], "methodName", new Class[0]);
        Object[] arguments = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod(mock, mockitoMethod, arguments, sequenceNumber);

        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, sequenceNumber, realMethod);

        Throwable throwable = new RuntimeException();
        invocation.isValidException(throwable);
    }

    @Test
    public void testIsValidException_ValidExceptionType() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[0], "methodName", new Class[]{Exception.class});
        Object[] arguments = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod(mock, mockitoMethod, arguments, sequenceNumber);

        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, sequenceNumber, realMethod);

        Throwable throwable = new Exception();
        assertTrue(invocation.isValidException(throwable));
    }

    @Test(expected = AssertionError.class)
    public void testIsValidReturnType_InvalidReturnType() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(Integer.class, "methodName", new Class[0]);
        Object[] arguments = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod(mock, mockitoMethod, arguments, sequenceNumber);

        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, sequenceNumber, realMethod);

        Class clazz = Double.class;
        invocation.isValidReturnType(clazz);
    }

    @Test
    public void testIsValidReturnType_ValidReturnType() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(Integer.class, "methodName", new Class[0]);
        Object[] arguments = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod(mock, mockitoMethod, arguments, sequenceNumber);

        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, sequenceNumber, realMethod);

        Class clazz = Integer.class;
        assertTrue(invocation.isValidReturnType(clazz));
    }

    @Test(expected = AssertionError.class)
    public void testIsVoid_NonVoidMethod() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(Integer.class, "methodName", new Class[0]);
        Object[] arguments = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod(mock, mockitoMethod, arguments, sequenceNumber);

        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, sequenceNumber, realMethod);

        invocation.isVoid();
    }

    @Test
    public void testPrintMethodReturnType() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(Integer.class, "methodName", new Class[0]);
        Object[] arguments = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod(mock, mockitoMethod, arguments, sequenceNumber);

        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, sequenceNumber, realMethod);

        String result = invocation.printMethodReturnType();
        String expected = "Integer";
        assertEquals(expected, result);
    }

    @Test(expected = RuntimeException.class)
    public void testCallRealMethod_NonMockedMethod() {
        Object mock = new Object();
        Object target = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[0], "methodName", new Class[0]);
        Object[] arguments = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod(target, mockitoMethod, arguments, sequenceNumber);

        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, sequenceNumber, realMethod);

        invocation.callRealMethod();
    }

    @Test
    public void testCallRealMethod_MockedMethod() throws Throwable {
        Object mock = new Object();
        Object target = new Object() {
            public Integer methodName(Object arg1) {
                return 1;
            }
        };
        MockitoMethod mockitoMethod = new MockitoMethod(Integer.class, "methodName", new Class[]{Object.class});
        Object[] arguments = new Object[]{new Object()};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod(target, mockitoMethod, arguments, sequenceNumber);

        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, sequenceNumber, realMethod);

        assertEquals(1, invocation.callRealMethod());
    }
}