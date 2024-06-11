import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.*;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.Equals;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.mockitoSession;
import org.mockito.exceptions.PrintableInvocation;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class InvocationTest {

    private MockitoSession session;
    private Invocation invocation;

    @Before
    public void setUp() {
        session = new mockitoSession();
    }

    @Test
    public void testInvocationConstructor() {
        Object mock = mock(String.class);
        MockitoMethod method = mockitoSession().mockMethod(String.class, "length");
        Object[] arguments = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, arguments, sequenceNumber, realMethod);

        assertThat(invocation.getMock(), is(mock));
        assertThat(invocation.getMethod(), is(method));
        assertThat(invocation.getArguments(), is(arguments));
        assertThat(invocation.getSequenceNumber(), is(sequenceNumber));
        assertThat(invocation.realMethod, is(realMethod));
    }

    @Test
    public void testToString() {
        Object mock = mock(String.class);
        MockitoMethod method = mockitoSession().mockMethod(String.class, "length");
        Object[] arguments = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, arguments, sequenceNumber, realMethod);

        List<Matcher> matchers = invocation.argumentsToMatchers();
        String expectedResult = "String.length()" + new MatchersPrinter().getArgumentsLine(matchers, new PrintSettings());

        assertThat(invocation.toString(), is(expectedResult));
    }

    @Test
    public void testIsToString() {
        MockitoMethod toStringMethod = mockitoSession().mockMethod(Object.class, "toString");

        Invocation invocation = new Invocation(mock(Object.class), toStringMethod, new Object[0], 1, null);

        assertThat(Invocation.isToString(invocation), is(true));
    }

    @Test(expected = RuntimeException.class)
    public void testHashCode() {
        Object mock = mock(String.class);
        MockitoMethod method = mockitoSession().mockMethod(String.class, "length");
        Object[] arguments = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation1 = new Invocation(mock, method, arguments, sequenceNumber, realMethod);
        Invocation invocation2 = new Invocation(mock, method, arguments, sequenceNumber, realMethod);

        invocation1.hashCode();
        invocation2.hashCode();
    }

    @Test
    public void testEquals() {
        Object mock = mock(String.class);
        MockitoMethod method = mockitoSession().mockMethod(String.class, "length");
        Object[] arguments = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation1 = new Invocation(mock, method, arguments, sequenceNumber, realMethod);
        Invocation invocation2 = new Invocation(mock, method, arguments, sequenceNumber, realMethod);

        assertThat(invocation1, is(invocation2));

        Object[] differentArguments = new Object[]{1};
        Invocation invocation3 = new Invocation(mock, method, differentArguments, sequenceNumber, realMethod);

        assertThat(invocation1, is(not(invocation3)));

        Object differentMock = mock(Integer.class);
        Invocation invocation4 = new Invocation(differentMock, method, arguments, sequenceNumber, realMethod);

        assertThat(invocation1, is(not(invocation4)));
    }

    @Test
    public void testVerify() {
        Object mock = mock(String.class);
        MockitoMethod method = mockitoSession().mockMethod(String.class, "length");
        Object[] arguments = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, arguments, sequenceNumber, realMethod);

        invocation.markVerified();

        assertTrue(invocation.isVerified());
    }

    @Test
    public void testVerifyInOrder() {
        Object mock = mock(String.class);
        MockitoMethod method = mockitoSession().mockMethod(String.class, "length");
        Object[] arguments = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation1 = new Invocation(mock, method, arguments, sequenceNumber, realMethod);
        Invocation invocation2 = new Invocation(mock, method, arguments, sequenceNumber + 1, realMethod);

        invocation1.markVerified();
        invocation2.markVerifiedInOrder();

        assertTrue(invocation1.isVerified());
        assertTrue(invocation1.isVerifiedInOrder());
        assertTrue(invocation2.isVerified());
        assertTrue(invocation2.isVerifiedInOrder());
    }

    @Test
    public void testIsVerifiedInOrder() {
        Object mock = mock(String.class);
        MockitoMethod method = mockitoSession().mockMethod(String.class, "length");
        Object[] arguments = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation1 = new Invocation(mock, method, arguments, sequenceNumber, realMethod);

        assertFalse(invocation1.isVerifiedInOrder());

        invocation1.markVerifiedInOrder();

        assertTrue(invocation1.isVerifiedInOrder());
    }

    @Test
    public void testIsVerified() {
        Object mock = mock(String.class);
        MockitoMethod method = mockitoSession().mockMethod(String.class, "length");
        Object[] arguments = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, arguments, sequenceNumber, realMethod);

        assertFalse(invocation.isVerified());

        invocation.markVerified();

        assertTrue(invocation.isVerified());
    }

    @Test(expected = RuntimeException.class)
    public void testGetArgumentsInvalidAccess() {
        Object mock = mock(String.class);
        MockitoMethod method = mockitoSession().mockMethod(String.class, "length");
        Object[] arguments = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, arguments, sequenceNumber, realMethod);

        invocation.getArguments()[0] = 1;
    }

    @Test(expected = RuntimeException.class)
    public void testGetRawArgumentsInvalidAccess() {
        Object mock = mock(String.class);
        MockitoMethod method = mockitoSession().mockMethod(String.class, "length");
        Object[] arguments = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, arguments, sequenceNumber, realMethod);

        invocation.getRawArguments()[0] = 1;
    }

    @Test
    public void testGetSequenceNumber() {
        Object mock = mock(String.class);
        MockitoMethod method = mockitoSession().mockMethod(String.class, "length");
        Object[] arguments = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, arguments, sequenceNumber, realMethod);

        assertThat(invocation.getSequenceNumber(), is(sequenceNumber));
    }

    @Test
    public void testToStringWithVarargs() {
        Object mock = mock(String[].class);
        MockitoMethod method = mockitoSession().mockMethod(String[].class, "length");
        Object[] arguments = new Object[]{new String[]{"test"}};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, arguments, sequenceNumber, realMethod);

        List<Matcher> matchers = invocation.argumentsToMatchers();
        String expectedResult = "String[].length((String[]) org.mockito.mock.Mockito.anyArray())" + new MatchersPrinter().getArgumentsLine(matchers, new PrintSettings());

        assertThat(invocation.toString(), is(expectedResult));
    }

    @Test
    public void testToStringWithPrimitives() {
        Object mock = mock(Integer.TYPE.class);
        MockitoMethod method = mockitoSession().mockMethod(Integer.TYPE.class, "intValue");
        Object[] arguments = new Object[]{1};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, arguments, sequenceNumber, realMethod);

        List<Matcher> matchers = invocation.argumentsToMatchers();
        String expectedResult = "Integer.intValue((Integer) org.mockito.mock.Mockito.any())" + new MatchersPrinter().getArgumentsLine(matchers, new PrintSettings());

        assertThat(invocation.toString(), is(expectedResult));
    }

    @Test
    public void testInvokeRealMethod() throws Throwable {
        Object mock = mock(String.class);
        MockitoMethod method = mockitoSession().mockMethod(String.class, "length");
        Object[] arguments = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);
        when(realMethod.invoke(mock, arguments)).thenReturn(5);

        Invocation invocation = new Invocation(mock, method, arguments, sequenceNumber, realMethod);

        assertEquals(5, invocation.callRealMethod());
    }

    @Test(expected = Throwable.class)
    public void testInvokeRealMethodWithException() throws Throwable {
        Object mock = mock(String.class);
        MockitoMethod method = mockitoSession().mockMethod(String.class, "length");
        Object[] arguments = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);
        when(realMethod.invoke(mock, arguments)).thenThrow(new RuntimeException("Test Exception"));

        Invocation invocation = new Invocation(mock, method, arguments, sequenceNumber, realMethod);

        invocation.isValidException(new RuntimeException("Test Exception"));

        invocation.callRealMethod();
    }

    @Test
    public void testIsValidReturnType() {
        Object mock = mock(String.class);
        MockitoMethod method = mockitoSession().mockMethod(String.class, "length");
        Object[] arguments = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, arguments, sequenceNumber, realMethod);

        assertTrue(invocation.isValidReturnType(int.class));
        assertTrue(invocation.isValidReturnType(Integer.class));
        assertTrue(invocation.isValidReturnType(String.class));
        assertFalse(invocation.isValidReturnType(Double.class));
    }

    @Test
    public void testIsVoid() {
        Object mock = mock(Void.TYPE.class);
        MockitoMethod method = mockitoSession().mockMethod(Void.TYPE.class, "voidMethod");
        Object[] arguments = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, arguments, sequenceNumber, realMethod);

        assertTrue(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        Object mock = mock(String.class);
        MockitoMethod method = mockitoSession().mockMethod(String.class, "length");
        Object[] arguments = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, arguments, sequenceNumber, realMethod);

        assertEquals("int", invocation.printMethodReturnType());
    }

    @Test
    public void testToStringWithDifferentMethods() {
        Object mock = mock(String.class);
        MockitoMethod method1 = mockitoSession().mockMethod(String.class, "length");
        MockitoMethod method2 = mockitoSession().mockMethod(String.class, "substring");
        Object[] arguments = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation1 = new Invocation(mock, method1, arguments, sequenceNumber, realMethod);
        Invocation invocation2 = new Invocation(mock, method2, arguments, sequenceNumber, realMethod);

        List<Matcher> matchers1 = invocation1.argumentsToMatchers();
        List<Matcher> matchers2 = invocation2.argumentsToMatchers();

        String expectedResult1 = "String.length()" + new MatchersPrinter().getArgumentsLine(matchers1, new PrintSettings());
        String expectedResult2 = "String.substring(int, int)" + new MatchersPrinter().getArgumentsLine(matchers2, new PrintSettings());

        assertEquals(expectedResult1, invocation1.toString());
        assertEquals(expectedResult2, invocation2.toString());
    }

    @Test
    public void testToStringWithDifferentArguments() {
        Object mock = mock(String.class);
        MockitoMethod method = mockitoSession().mockMethod(String.class, "length");

        Invocation invocation1 = new Invocation(mock, method, new Object[0], 1, mock(RealMethod.class));
        Invocation invocation2 = new Invocation(mock, method, new Object[]{new Object(){}}, 1, mock(RealMethod.class));

        List<Matcher> matchers1 = invocation1.argumentsToMatchers();
        List<Matcher> matchers2 = invocation2.argumentsToMatchers();

        String expectedResult1 = "String.length()" + new MatchersPrinter().getArgumentsLine(matchers1, new PrintSettings());
        String expectedResult2 = "String.length((Object) org.mockito.mock.Mockito.any())" + new MatchersPrinter().getArgumentsLine(matchers2, new PrintSettings());

        assertEquals(expectedResult1, invocation1.toString());
        assertEquals(expectedResult2, invocation2.toString());
    }
}