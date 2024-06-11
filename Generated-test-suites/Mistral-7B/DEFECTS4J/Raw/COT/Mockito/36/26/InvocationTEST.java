import org.junit.Test;
import org.junit.Assert.*;
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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.exceptions.PrintableInvocation;

public class InvocationTest {

    @Test
    public void testConstructor() {
        MockitoMethod mockitoMethod = new MockitoMethod(Mockito.class, "testMethod", new Class[]{}, new Class[]{}, null);
        Object mock = Mockito.mock(String.class);
        Object[] arguments = new Object[]{1, "test"};
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, new RealMethod(mock, mockitoMethod, arguments));

        assertEquals(mockitoMethod, invocation.getMethod());
        assertEquals(mock, invocation.getMock());
        assertArrayEquals(arguments, invocation.getArguments());
        assertEquals(1, invocation.getSequenceNumber());
    }

    @Test
    public void testExpandVarArgs() {
        Object[] args = new Object[]{1, new Object[]{2, 3, 4}};
        Object[] expectedArgs = new Object[]{1, 2, 3, 4};

        Object[] expandedArgs = Invocation.expandVarArgs(true, args);

        assertArrayEquals(expectedArgs, expandedArgs);
    }

    @Test
    public void testIsToString() {
        MockitoMethod toStringMethod = new MockitoMethod(String.class, "toString", new Class[0], new Class[]{String.class}, null);
        Invocation invocation = new Invocation(Mockito.mock(String.class), toStringMethod, null, 1, new RealMethod(Mockito.mock(String.class), toStringMethod, null));

        assertTrue(Invocation.isToString(invocation));
    }

    @Test(expected = RuntimeException.class)
    public void testHashCode() {
        Invocation invocation1 = new Invocation(Mockito.mock(String.class), new MockitoMethod("testMethod", new Class[0], new Class[0], null), new Object[0], 1, new RealMethod(Mockito.mock(String.class), new MockitoMethod("testMethod", new Class[0], new Class[0], null), new Object[0]));
        Invocation invocation2 = new Invocation(Mockito.mock(String.class), new MockitoMethod("testMethod", new Class[0], new Class[0], null), new Object[0], 1, new RealMethod(Mockito.mock(String.class), new MockitoMethod("testMethod", new Class[0], new Class[0], null), new Object[0]));

        invocation1.hashCode();
    }

    @Test
    public void testToString() {
        MockitoMethod getNameMethod = new MockitoMethod(String.class, "getName", new Class[0], new Class[]{String.class}, null);
        Invocation invocation = new Invocation(Mockito.mock(String.class), getNameMethod, new Object[]{null}, 1, new RealMethod(Mockito.mock(String.class), getNameMethod, new Object[]{null}));

        String expectedToString = "getName() null";
        String toString = invocation.toString();

        assertEquals(expectedToString, toString);
    }

    @Test
    public void testToStringWithMatchers() {
        MockitoMethod getNameMethod = new MockitoMethod(String.class, "getName", new Class[0], new Class[]{String.class}, null);
        Invocation invocation = new Invocation(Mockito.mock(String.class), getNameMethod, new Object[]{1}, 1, new RealMethod(Mockito.mock(String.class), getNameMethod, new Object[]{1}));

        List<Matcher> matchers = invocation.argumentsToMatchers();
        MatchersPrinter matchersPrinter = new MatchersPrinter();
        PrintSettings printSettings = new PrintSettings();

        String expectedToString = "getName() 1";
        String toString = invocation.toString(matchers, printSettings);

        assertEquals(expectedToString, toString);
    }

    @Test
    public void testIsValidException() {
        MockitoMethod method = new MockitoMethod(String.class, "testMethod", new Class[]{Integer.class}, new Class[]{Exception.class}, null);
        Invocation invocation = new Invocation(Mockito.mock(String.class), method, new Object[]{1}, 1, new RealMethod(Mockito.mock(String.class), method, new Object[]{1}));

        Throwable throwable = new Exception();

        assertTrue(invocation.isValidException(throwable));
    }

    @Test
    public void testIsValidReturnType() {
        Invocation invocation = new Invocation(Mockito.mock(String.class), new MockitoMethod("testMethod", new Class[0], new Class[]{Integer.class}, null), null, 1, new RealMethod(Mockito.mock(String.class), new MockitoMethod("testMethod", new Class[0], new Class[]{Integer.class}, null), null));

        assertTrue(invocation.isValidReturnType(int.class));
    }

    @Test
    public void testIsVoid() {
        MockitoMethod method = new MockitoMethod(void.class, "testMethod", new Class[0], new Class[0], null);
        Invocation invocation = new Invocation(Mockito.mock(String.class), method, null, 1, new RealMethod(Mockito.mock(String.class), method, null));

        assertTrue(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        MockitoMethod method = new MockitoMethod(String.class, "testMethod", new Class[0], new Class[]{Integer.class}, null);
        Invocation invocation = new Invocation(Mockito.mock(String.class), method, null, 1, new RealMethod(Mockito.mock(String.class), method, null));

        String expectedPrintMethodReturnType = "int";
        String printMethodReturnType = invocation.printMethodReturnType();

        assertEquals(expectedPrintMethodReturnType, printMethodReturnType);
    }

    @Test
    public void testMarkVerified() {
        Invocation invocation = new Invocation(Mockito.mock(String.class), new MockitoMethod("testMethod", new Class[0], new Class[0], null), new Object[]{1}, 1, new RealMethod(Mockito.mock(String.class), new MockitoMethod("testMethod", new Class[0], new Class[0], null), new Object[]{1}));

        invocation.markVerified();

        assertTrue(invocation.isVerified());
    }

    @Test
    public void testMarkVerifiedInOrder() {
        Invocation invocation1 = new Invocation(Mockito.mock(String.class), new MockitoMethod("testMethod1", new Class[0], new Class[0], null), new Object[]{1}, 1, new RealMethod(Mockito.mock(String.class), new MockitoMethod("testMethod1", new Class[0], new Class[0], null), new Object[]{1}));
        Invocation invocation2 = new Invocation(Mockito.mock(String.class), new MockitoMethod("testMethod2", new Class[0], new Class[0], null), new Object[]{2}, 2, new RealMethod(Mockito.mock(String.class), new MockitoMethod("testMethod2", new Class[0], new Class[0], null), new Object[]{2}));

        invocation1.markVerified();
        invocation2.markVerifiedInOrder();

        assertTrue(invocation1.isVerifiedInOrder());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToStringWithNullMatchers() {
        MockitoMethod method = new MockitoMethod(String.class, "getName", new Class[0], new Class[]{String.class}, null);
        Invocation invocation = new Invocation(Mockito.mock(String.class), method, new Object[]{null}, 1, new RealMethod(Mockito.mock(String.class), method, new Object[]{null}));

        String toString = invocation.toString(null, new PrintSettings());
    }

    @Test
    public void testToStringWithEmptyMatchers() {
        MockitoMethod method = new MockitoMethod(String.class, "getName", new Class[0], new Class[]{String.class}, null);
        Invocation invocation = new Invocation(Mockito.mock(String.class), method, new Object[]{null}, 1, new RealMethod(Mockito.mock(String.class), method, new Object[]{null}));

        List<Matcher> matchers = new ArrayList<>();

        String toString = invocation.toString(matchers, new PrintSettings());

        assertTrue(toString.isEmpty());
    }

    @Test
    public void testToStringWithLongMethodName() {
        MockitoMethod method = new MockitoMethod(String.class, "testLongMethodName", new Class[0], new Class[]{Integer.class}, null);
        Invocation invocation = new Invocation(Mockito.mock(String.class), method, new Object[]{1}, 1, new RealMethod(Mockito.mock(String.class), method, new Object[]{1}));

        List<Matcher> matchers = new ArrayList<>();
        MatchersPrinter matchersPrinter = new MatchersPrinter();
        PrintSettings printSettings = new PrintSettings();

        String expectedToString = "testLongMethodName() 1";
        String toString = invocation.toString(matchers, printSettings);

        assertEquals(expectedToString, toString);
    }

    @Test
    public void testToStringWithMultipleMatchers() {
        MockitoMethod method = new MockitoMethod(String.class, "testMethod", new Class[]{Integer.class, String[].class}, new Class[]{Integer.class, ArrayEquals.class}, null);
        Invocation invocation = new Invocation(Mockito.mock(String.class), method, new Object[]{1, new Object[]{1, 2}}, 1, new RealMethod(Mockito.mock(String.class), method, new Object[]{1, new Object[]{1, 2}}));

        List<Matcher> matchers = new ArrayList<>();
        MatchersPrinter matchersPrinter = new MatchersPrinter();
        PrintSettings printSettings = new PrintSettings();

        String expectedToString = "testMethod(1, [1, 2])";
        String toString = invocation.toString(matchers, printSettings);

        assertEquals(expectedToString, toString);
    }

    @Test
    public void testToStringWithMultilinePrintSettings() {
        MockitoMethod method = new MockitoMethod(String.class, "testMethod", new Class[]{Integer.class}, new Class[]{Integer.class}, null);
        Invocation invocation = new Invocation(Mockito.mock(String.class), method, new Object[]{1}, 1, new RealMethod(Mockito.mock(String.class), method, new Object[]{1}));

        List<Matcher> matchers = new ArrayList<>();
        MatchersPrinter matchersPrinter = new MatchersPrinter();
        PrintSettings printSettings = new PrintSettings(true, 80);

        String expectedToString = "testMethod(1)\n"
                + "arguments:\n"
                + "  1";
        String toString = invocation.toString(matchers, printSettings);

        assertEquals(expectedToString, toString);
    }
}