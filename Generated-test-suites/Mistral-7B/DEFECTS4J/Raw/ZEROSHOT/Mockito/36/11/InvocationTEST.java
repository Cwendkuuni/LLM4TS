package org.mockito.tests.internal.invocation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.matchers.Arguments;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.util.Primitives;
import org.mockito.internal.util.PrintingFriendlyInvocation;
import org.mockito.internal.util.MockitoMethod;
import org.mockito.internal.util.MockitoTypeUtils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(org.mockito.runners.JUnit4.class)
public class InvocationTest {

    @Test
    public void testInvocationCreation() {
        Object mock = mock(String.class);
        Method method = String.class.getMethod("length");
        Object[] arguments = new Object[]{mock};
        Invocation invocation = new Invocation(mock, new MockitoMethod(method, null, null), arguments, 1, new RealMethod(method));

        assertThat(invocation.getMock(), is(mock));
        assertThat(invocation.getMethod(), is(method));
        assertThat(invocation.getArguments(), is(arguments));
        assertThat(invocation.getSequenceNumber(), is(1));
        assertThat(invocation.isVerified(), is(false));
        assertThat(invocation.isVerifiedInOrder(), is(false));
        assertThat(invocation.toString(), is("mock.length()"));
    }

    @Test
    public void testExpandVarArgs() {
        Object mock = mock(String[].class);
        Object[] arguments = new Object[]{mock, new Object[]{new String[]{"a", "b"}}};
        Object[] expandedArguments = Invocation.expandVarArgs(true, arguments);

        assertThat(expandedArguments, is(new Object[]{mock, "a", "b"}));
    }

    @Test
    public void testGetMockName() {
        Object mock = mock(String.class);
        String mockName = new MockUtil().getMockName(mock);

        assertThat(mockName, is("mock"));
    }

    @Test
    public void testIsToString() {
        Object mock = mock(String.class);
        Method toStringMethod = String.class.getMethod("toString");
        Invocation invocation = new Invocation(mock, new MockitoMethod(toStringMethod, null, null), new Object[0], 1, new RealMethod(toStringMethod));

        assertThat(Invocation.isToString(invocation), is(true));
    }

    @Test
    public void testIsValidException() {
        Object mock = mock(Exception.class);
        Method method = Exception.class.getMethod("getMessage");
        Invocation invocation = new Invocation(mock, new MockitoMethod(method, null, null), new Object[0], 1, new RealMethod(method));

        Throwable exception = new RuntimeException();
        assertThat(invocation.isValidException(exception), is(false));

        Class<? extends Exception> exceptionClass = Exception.class;
        assertThat(invocation.isValidException(exception), is(true));

        assertThat(invocation.isValidException(new Exception("test")), is(true));
        assertThat(invocation.isValidException(new IllegalArgumentException()), is(true));
        assertThat(invocation.isValidException(new IllegalStateException()), is(true));
    }

    @Test
    public void testIsValidReturnType() {
        Object mock = mock(String.class);
        Method method = String.class.getMethod("length");
        Invocation invocation = new Invocation(mock, new MockitoMethod(method, int.class, null), new Object[0], 1, new RealMethod(method));

        assertThat(invocation.isValidReturnType(int.class), is(true));
        assertThat(invocation.isValidReturnType(String.class), is(true));
        assertThat(invocation.isValidReturnType(Double.class), is(false));
    }

    @Test
    public void testIsVoid() {
        Object mock = mock(Void.TYPE);
        Method method = Void.TYPE.getMethod("toString");
        Invocation invocation = new Invocation(mock, new MockitoMethod(method, null, null), new Object[0], 1, new RealMethod(method));

        assertThat(invocation.isVoid(), is(true));
    }

    @Test
    public void testPrintMethodReturnType() {
        Object mock = mock(String.class);
        Method method = String.class.getMethod("length");
        Invocation invocation = new Invocation(mock, new MockitoMethod(method, int.class, null), new Object[0], 1, new RealMethod(method));

        assertThat(invocation.printMethodReturnType(), is("int"));
    }

    @Test
    public void testToString() {
        Object mock = mock(String.class);
        Method method = String.class.getMethod("length");
        Object[] arguments = new Object[]{mock};
        Invocation invocation = new Invocation(mock, new MockitoMethod(method, int.class, null), arguments, 1, new RealMethod(method));

        PrintSettings printSettings = new PrintSettings();
        printSettings.setMultiline(true);

        String expectedToString = "mock.length()\n" +
                "arguments:\n" +
                "  [mock]\n";

        String actualToString = invocation.toString(printSettings);

        assertThat(actualToString, is(expectedToString));

        printSettings.setMultiline(false);

        expectedToString = "mock.length()";

        actualToString = invocation.toString();

        assertThat(actualToString, is(expectedToString));
    }

    @Test
    public void testMarkVerified() {
        Object mock = mock(String.class);
        Method method = String.class.getMethod("length");
        Object[] arguments = new Object[]{mock};
        Invocation invocation = new Invocation(mock, new MockitoMethod(method, int.class, null), arguments, 1, new RealMethod(method));

        invocation.markVerified();

        assertThat(invocation.isVerified(), is(true));
    }

    @Test
    public void testMarkVerifiedInOrder() {
        Object mock = mock(String.class);
        Method method = String.class.getMethod("length");
        Object[] arguments = new Object[]{mock};
        Invocation invocation1 = new Invocation(mock, new MockitoMethod(method, int.class, null), arguments, 1, new RealMethod(method));
        Invocation invocation2 = new Invocation(mock, new MockitoMethod(method, int.class, null), arguments, 2, new RealMethod(method));

        invocation1.markVerifiedInOrder();

        assertThat(invocation1.isVerifiedInOrder(), is(true));
        assertThat(invocation2.isVerifiedInOrder(), is(false));

        verify(mock, Mockito.inOrder(invocation1, invocation2)).length();
    }

    @Test
    public void testEquals() {
        Object mock = mock(String.class);
        Method method = String.class.getMethod("length");
        Object[] arguments = new Object[]{mock};
        Invocation invocation1 = new Invocation(mock, new MockitoMethod(method, int.class, null), arguments, 1, new RealMethod(method));
        Invocation invocation2 = new Invocation(mock, new MockitoMethod(method, int.class, null), arguments, 1, new RealMethod(method));

        assertThat(invocation1, is(invocation2));
    }

    @Test(expected = RuntimeException.class)
    public void testHashCode() {
        Object mock = mock(String.class);
        Method method = String.class.getMethod("length");
        Object[] arguments = new Object[]{mock};
        Invocation invocation = new Invocation(mock, new MockitoMethod(method, int.class, null), arguments, 1, new RealMethod(method));

        invocation.hashCode();
    }

    @Test
    public void testToStringWithMatchers() {
        Object mock = mock(String.class);
        Method method = String.class.getMethod("length");
        Object[] arguments = new Object[]{mock};
        Invocation invocation = new Invocation(mock, new MockitoMethod(method, int.class, null), arguments, 1, new RealMethod(method));

        PrintSettings printSettings = new PrintSettings();
        printSettings.setMultiline(true);

        List<Matcher> matchers = Arrays.asList(ArgumentMatchers.eq(mock), ArgumentMatchers.isA(String.class));
        String expectedToString = "mock.length()\n" +
                "arguments:\n" +
                "  [<mock>, java.lang.String]\n";

        String actualToString = invocation.toString(matchers, printSettings);

        verify(mock).length();

        assertThat(actualToString, is(expectedToString));
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        Object mock = mock(String.class);
        Method method = String.class.getMethod("length");
        Invocation invocation = new Invocation(mock, new MockitoMethod(method, int.class, null), new Object[0], 1, new RealMethod(method));

        int expectedLength = 5;
        when(mock.toString()).thenReturn("mockString");
        int actualLength = (Integer) invocation.callRealMethod();

        assertThat(actualLength, is(expectedLength));
        assertThat(mock.toString(), is("mockString"));
    }
}