import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.realmethod.RealMethod;

public class InvocationTest {

    private Invocation invocation;
    private Object mock;
    private MockitoMethod method;
    private Object[] args;
    private int sequenceNumber;
    private RealMethod realMethod;

    @Before
    public void setup() {
        mock = mock(Object.class);
        method = mock(MockitoMethod.class);
        args = new Object[] { "arg1", "arg2" };
        sequenceNumber = 1;
        realMethod = mock(RealMethod.class);

        invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
    }

    @Test
    public void testGetMock() {
        assertEquals(mock, invocation.getMock());
    }

    @Test
    public void testGetMethod() {
        assertEquals(method, invocation.getMethod());
    }

    @Test
    public void testGetArguments() {
        assertArrayEquals(args, invocation.getArguments());
    }

    @Test
    public void testIsVerified() {
        assertFalse(invocation.isVerified());
    }

    @Test
    public void testGetSequenceNumber() {
        assertEquals(sequenceNumber, invocation.getSequenceNumber().intValue());
    }

    @Test
    public void testIsVerifiedInOrder() {
        assertFalse(invocation.isVerifiedInOrder());
    }

    @Test
    public void testEquals() {
        Invocation other = new Invocation(mock, method, args, sequenceNumber, realMethod);
        assertTrue(invocation.equals(other));
    }

    @Test
    public void testHashCode() {
        assertThrows(RuntimeException.class, () -> invocation.hashCode());
    }

    @Test
    public void testToString() {
        String expected = "mock.Object.method(arg1, arg2)";
        assertEquals(expected, invocation.toString());
    }

    @Test
    public void testToStringWithMatchers() {
        MatchersPrinter matchersPrinter = mock(MatchersPrinter.class);
        PrintSettings printSettings = mock(PrintSettings.class);
        List<Matcher> matchers = new ArrayList<Matcher>();
        String expected = "mock.Object.method(matchers)";

        when(matchersPrinter.getArgumentsLine(matchers, printSettings)).thenReturn("(matchers)");
        when(matchersPrinter.getArgumentsBlock(matchers, printSettings)).thenReturn("(matchers)");

        String result = invocation.toString(matchers, printSettings);
        assertEquals(expected, result);
    }

    @Test
    public void testToStringWithMultilineSettings() {
        MatchersPrinter matchersPrinter = mock(MatchersPrinter.class);
        PrintSettings printSettings = mock(PrintSettings.class);
        List<Matcher> matchers = new ArrayList<Matcher>();
        String expected = "mock.Object.method(\n    matchers)";

        when(printSettings.isMultiline()).thenReturn(true);
        when(matchersPrinter.getArgumentsBlock(matchers, printSettings)).thenReturn("    matchers");

        String result = invocation.toString(matchers, printSettings);
        assertEquals(expected, result);
    }

    @Test
    public void testToStringWithLongInvocation() {
        MatchersPrinter matchersPrinter = mock(MatchersPrinter.class);
        PrintSettings printSettings = mock(PrintSettings.class);
        List<Matcher> matchers = new ArrayList<Matcher>();
        String longArgument = "a".repeat(50);
        String expected = "mock.Object.method(\n    matchers)";

        matchers.add(new Equals(longArgument));

        when(matchersPrinter.getArgumentsBlock(matchers, printSettings)).thenReturn("    matchers");

        String result = invocation.toString(matchers, printSettings);
        assertEquals(expected, result);
    }

    @Test
    public void testIsToString() {
        MockitoMethod toStringMethod = mock(MockitoMethod.class);
        InvocationOnMock invocationOnMock = mock(InvocationOnMock.class);
        when(toStringMethod.getName()).thenReturn("toString");
        when(invocationOnMock.getMethod()).thenReturn(toStringMethod);

        assertTrue(Invocation.isToString(invocationOnMock));
    }

    @Test
    public void testIsValidException() {
        Throwable throwable = new RuntimeException();
        MockitoMethod exceptionMethod = mock(MockitoMethod.class);
        Class<?> exceptionType = RuntimeException.class;
        Class<?>[] exceptionTypes = new Class<?>[] { exceptionType };

        when(exceptionMethod.getExceptionTypes()).thenReturn(exceptionTypes);
        when(invocation.getMethod()).thenReturn(exceptionMethod);

        assertTrue(invocation.isValidException(throwable));
    }

    @Test
    public void testIsValidReturnType() {
        Class<?> returnType = String.class;
        Class<?> clazz = String.class;

        MockitoMethod returnTypeMethod = mock(MockitoMethod.class);
        when(returnTypeMethod.getReturnType()).thenReturn(returnType);
        when(invocation.getMethod()).thenReturn(returnTypeMethod);

        assertTrue(invocation.isValidReturnType(clazz));
    }

    @Test
    public void testIsVoid() {
        Class<?> voidType = Void.TYPE;

        MockitoMethod voidMethod = mock(MockitoMethod.class);
        when(voidMethod.getReturnType()).thenReturn(voidType);
        when(invocation.getMethod()).thenReturn(voidMethod);

        assertTrue(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        Class<?> returnType = String.class;

        MockitoMethod returnTypeMethod = mock(MockitoMethod.class);
        when(returnTypeMethod.getReturnType()).thenReturn(returnType);
        when(invocation.getMethod()).thenReturn(returnTypeMethod);

        String expected = "String";
        assertEquals(expected, invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        String methodName = "method";

        MockitoMethod method = mock(MockitoMethod.class);
        when(method.getName()).thenReturn(methodName);
        when(invocation.getMethod()).thenReturn(method);

        assertEquals(methodName, invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        Class<?> primitiveType = int.class;

        MockitoMethod primitiveMethod = mock(MockitoMethod.class);
        when(primitiveMethod.getReturnType()).thenReturn(primitiveType);
        when(invocation.getMethod()).thenReturn(primitiveMethod);

        assertTrue(invocation.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        Location location = new Location();
        Location expectedLocation;

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
        expectedLocation = invocation.getLocation();

        assertEquals(expectedLocation, location);
    }

    @Test
    public void testGetArgumentsCount() {
        int expectedCount = args.length;
        assertEquals(expectedCount, invocation.getArgumentsCount());
    }

    @Test
    public void testGetRawArguments() {
        assertArrayEquals(args, invocation.getRawArguments());
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        Object result = new Object();
        when(realMethod.invoke(mock, args)).thenReturn(result);

        Object actualResult = invocation.callRealMethod();
        assertEquals(result, actualResult);
    }

    @Test
    public void testToStringWithPrintSettings() {
        MatchersPrinter matchersPrinter = mock(MatchersPrinter.class);
        PrintSettings printSettings = mock(PrintSettings.class);
        List<Matcher> matchers = new ArrayList<Matcher>();
        String expected = "mock.Object.method(*varargs)";

        matchers.add(new ArrayEquals(args));

        when(matchersPrinter.getArgumentsLine(matchers, printSettings)).thenReturn("(*varargs)");

        String result = invocation.toString(matchers, printSettings);
        assertEquals(expected, result);
    }

    @Test
    public void testMarkVerified() {
        assertFalse(invocation.isVerified());

        invocation.markVerified();

        assertTrue(invocation.isVerified());
    }

    @Test
    public void testMarkVerifiedInOrder() {
        assertFalse(invocation.isVerifiedInOrder());

        invocation.markVerifiedInOrder();

        assertTrue(invocation.isVerifiedInOrder());
        assertTrue(invocation.isVerified());
    }

}