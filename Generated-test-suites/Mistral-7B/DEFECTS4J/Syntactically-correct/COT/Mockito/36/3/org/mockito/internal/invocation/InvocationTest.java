import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.RealMethod;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.MockitoMethod;
import org.mockito.internal.util.ObjectsMethodsGuru;
import org.mockito.internal.util.Primitives;

public class InvocationTest {

    @Test
    public void testConstructor() {
        MockitoMethod mockitoMethod = new MockitoMethod(MockitoTestUtils.class, "testMethod", new Class[]{Integer.class, String[].class});
        Object mock = Mockito.mock(String.class);
        Object[] arguments = new Object[]{1, new Object[]{1, 2, 3}};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, new RealMethod(MockitoTestUtils.class, "testMethod", new Class[]{Integer.class, String[].class}));

        assertEquals(mockitoMethod, invocation.getMethod());
        assertEquals(mock, invocation.getMock());
        assertEquals(1, invocation.getSequenceNumber());
        assertArrayEquals(new Object[]{arguments}, invocation.getArguments());
        assertArrayEquals(new Object[]{arguments}, invocation.getRawArguments());
    }

    @Test
    public void testExpandVarArgs() {
        MockitoMethod mockitoMethod = new MockitoMethod(MockitoTestUtils.class, "testMethod", new Class[]{Integer.class, int[].class});
        Object[] arguments = new Object[]{1, new int[]{1, 2, 3}};
        Object[] expandedArguments = Invocation.expandVarArgs(mockitoMethod.isVarArgs(), arguments);

        assertEquals(arguments.length, expandedArguments.length);
        assertEquals(arguments[0], expandedArguments[0]);
        assertEquals(arguments[1], expandedArguments[1]);
        assertArrayEquals(new int[]{1, 2, 3}, expandedArguments[expandedArguments.length - 1]);
    }

    @Test
    public void testEquals() {
        MockitoMethod mockitoMethod = new MockitoMethod(MockitoTestUtils.class, "testMethod", new Class[]{Integer.class, String[].class});
        Object mock = Mockito.mock(String.class);
        Object[] arguments1 = new Object[]{1, new Object[]{1, 2, 3}};
        Object[] arguments2 = new Object[]{1, new Object[]{1, 2, 3}};
        Invocation invocation1 = new Invocation(mock, mockitoMethod, arguments1, 1, new RealMethod(MockitoTestUtils.class, "testMethod", new Class[]{Integer.class, String[].class}));
        Invocation invocation2 = new Invocation(mock, mockitoMethod, arguments2, 1, new RealMethod(MockitoTestUtils.class, "testMethod", new Class[]{Integer.class, String[].class}));

        assertTrue(invocation1.equals(invocation2));
        assertFalse(invocation1.equals(new Object()));
    }

    @Test
    public void testToString() {
        MockitoMethod mockitoMethod = new MockitoMethod(MockitoTestUtils.class, "testMethod", new Class[]{Integer.class, String[].class});
        Object mock = Mockito.mock(String.class);
        Object[] arguments = new Object[]{1, new Object[]{1, 2, 3}};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, new RealMethod(MockitoTestUtils.class, "testMethod", new Class[]{Integer.class, String[].class}));

        String expectedToString = "testMethod(int arg1, [Ljava.lang.Object; arg2)";
        String toString = invocation.toString();
        assertEquals(expectedToString, toString);
    }

    @Test
    public void testIsToString() {
        MockitoMethod mockitoMethod = new MockitoMethod(MockitoTestUtils.class, "testToString", new Class[0]);
        Invocation invocation = new Invocation(Mockito.mock(String.class), mockitoMethod, new Object[0], 1, new RealMethod(MockitoTestUtils.class, "testToString", new Class[0]));

        assertTrue(Invocation.isToString(invocation));
    }

    @Test
    public void testIsValidException() {
        MockitoMethod mockitoMethod = new MockitoMethod(MockitoTestUtils.class, "testMethod", new Class[]{Integer.class, String[].class});
        Object mock = Mockito.mock(String.class);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[0], 1, new RealMethod(MockitoTestUtils.class, "testMethod", new Class[]{Integer.class, String[].class}));

        Class<?> expectedExceptionType = RuntimeException.class;
        Throwable throwable = new RuntimeException();

        assertTrue(invocation.isValidException(throwable));
        assertEquals(expectedExceptionType, invocation.getMethod().getExceptionTypes()[0]);
    }

    @Test
    public void testIsValidReturnType() {
        MockitoMethod mockitoMethod = new MockitoMethod(MockitoTestUtils.class, "testMethod", new Class[]{Integer.class, String[].class});
        Invocation invocation = new Invocation(Mockito.mock(String.class), mockitoMethod, new Object[0], 1, new RealMethod(MockitoTestUtils.class, "testMethod", new Class[]{Integer.class, String[].class}));

        Class<?> expectedReturnType = Integer.class;
        assertTrue(invocation.isValidReturnType(expectedReturnType));
    }

    @Test
    public void testIsVoid() {
        MockitoMethod mockitoMethod = new MockitoMethod(MockitoTestUtils.class, "testVoidMethod", new Class[0]);
        Invocation invocation = new Invocation(Mockito.mock(String.class), mockitoMethod, new Object[0], 1, new RealMethod(MockitoTestUtils.class, "testVoidMethod", new Class[0]));

        assertTrue(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        MockitoMethod mockitoMethod = new MockitoMethod(MockitoTestUtils.class, "testPrintInteger", new Class[]{Integer.class});
        Invocation invocation = new Invocation(Mockito.mock(String.class), mockitoMethod, new Object[]{1}, 1, new RealMethod(MockitoTestUtils.class, "testPrintInteger", new Class[]{Integer.class}));

        String expectedPrintMethodReturnType = "int";
        assertEquals(expectedPrintMethodReturnType, invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        MockitoMethod mockitoMethod = new MockitoMethod(MockitoTestUtils.class, "testPrintInteger", new Class[]{Integer.class});
        Invocation invocation = new Invocation(Mockito.mock(String.class), mockitoMethod, new Object[]{1}, 1, new RealMethod(MockitoTestUtils.class, "testPrintInteger", new Class[]{Integer.class}));

        String expectedMethodName = "testPrintInteger";
        assertEquals(expectedMethodName, invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        MockitoMethod mockitoMethod = new MockitoMethod(MockitoTestUtils.class, "testPrintBoolean", new Class[]{Boolean.class});
        Invocation invocation = new Invocation(Mockito.mock(String.class), mockitoMethod, new Object[]{true}, 1, new RealMethod(MockitoTestUtils.class, "testPrintBoolean", new Class[]{Boolean.class}));

        assertTrue(invocation.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        MockitoMethod mockitoMethod = new MockitoMethod(MockitoTestUtils.class, "testPrintInteger", new Class[]{Integer.class});
        Object mock = Mockito.mock(String.class);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[]{1}, 1, new RealMethod(MockitoTestUtils.class, "testPrintInteger", new Class[]{Integer.class}));

        Location expectedLocation = new Location();
        assertEquals(expectedLocation, invocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        MockitoMethod mockitoMethod = new MockitoMethod(MockitoTestUtils.class, "testPrintInteger", new Class[]{Integer.class});
        Object mock = Mockito.mock(String.class);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[]{1}, 1, new RealMethod(MockitoTestUtils.class, "testPrintInteger", new Class[]{Integer.class}));

        int expectedArgumentsCount = 1;
        assertEquals(expectedArgumentsCount, invocation.getArgumentsCount());
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        MockitoMethod mockitoMethod = new MockitoMethod(MockitoTestUtils.class, "testPrintInteger", new Class[]{Integer.class});
        Object mock = Mockito.mock(String.class);
        Invocation invocation = new Invocation(mock, mockitoMethod, new Object[]{1}, 1, new RealMethod(MockitoTestUtils.class, "testPrintInteger", new Class[]{Integer.class}));

        int expectedReturnValue = 42;
        assertEquals(expectedReturnValue, invocation.callRealMethod().intValue());
    }
}