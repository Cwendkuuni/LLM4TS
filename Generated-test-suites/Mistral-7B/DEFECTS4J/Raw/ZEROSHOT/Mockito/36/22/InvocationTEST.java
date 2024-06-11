package org.mockito.tests.internal.invocation;

import org.junit.Test;
import org.mockito.exceptions.base.MockitoException;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.RealMethod;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.util.Primitives;
import org.mockito.internal.util.PrintingFriendlyInvocation;
import org.mockito.invocation.InvocationOnMock;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.internal.matchers.TypeMatchers.isAssignableFrom;
import static org.mockito.internal.matchers.TypeMatchers.isInstanceOf;
import static org.mockito.internal.matchers.TypeMatchers.isPrimitiveTypeOf;

/**
 * Test class for {@link Invocation} class.
 */
public class InvocationTest {

    @Test
    public void testNewInvocation() {
        Object mock = mock(String.class);
        Method method = MockUtil.getMethodByName(String.class, "toString");
        Object[] arguments = new Object[0];
        RealMethod realMethod = new RealMethod(mock.getClass(), method, null);
        Invocation invocation = new Invocation(mock, new MockitoMethod(method), arguments, 1, realMethod);

        assertThat(invocation.getMock(), is(mock));
        assertThat(invocation.getMethod(), is(method));
        assertThat(invocation.getArguments(), is(arguments));
        assertThat(invocation.getSequenceNumber(), is(1));
        assertThat(invocation.realMethod, is(realMethod));
        assertThat(invocation.location.getFileName(), is(""));
        assertThat(invocation.location.getLineNumber(), is(0));
        assertThat(invocation.getLocation().getClassName(), is(""));
    }

    @Test
    public void testToString() throws Exception {
        Object mock = mock(String.class);
        Method method = MockUtil.getMethodByName(String.class, "toString");
        Object[] arguments = new Object[0];
        RealMethod realMethod = new RealMethod(mock.getClass(), method, null);
        Invocation invocation = new Invocation(mock, new MockitoMethod(method), arguments, 1, realMethod);

        String expected = String.format("%s.toString()", MockUtil.getMockName(mock));
        String actual = invocation.toString();

        assertThat(actual, is(expected));
    }

    @Test
    public void testToStringWithArguments() throws Exception {
        Object mock = mock(String.class);
        Method method = MockUtil.getMethodByName(String.class, "concat", String.class);
        Object[] arguments = new Object[]{"Hello"};
        RealMethod realMethod = new RealMethod(mock.getClass(), method, null);
        Invocation invocation = new Invocation(mock, new MockitoMethod(method), arguments, 1, realMethod);

        String expected = String.format("%s.concat(%s)", MockUtil.getMockName(mock), "\"Hello\"");
        String actual = invocation.toString();

        assertThat(actual, is(expected));
    }

    @Test
    public void testToStringWithMatchers() throws Exception {
        Object mock = mock(String.class);
        Method method = MockUtil.getMethodByName(String.class, "length");
        Object[] arguments = new Object[0];
        RealMethod realMethod = new RealMethod(mock.getClass(), method, null);
        Invocation invocation = new Invocation(mock, new MockitoMethod(method), arguments, 1, realMethod);

        PrintSettings printSettings = new PrintSettings();
        List<Matcher> matchers = invocation.argumentsToMatchers();

        String expected = String.format("%s.length()%n%s", MockUtil.getMockName(mock), MatchersPrinter.printArguments(matchers, printSettings));
        String actual = invocation.toString(matchers, printSettings);

        assertThat(actual, is(expected));
    }

    @Test
    public void testIsToString() throws Exception {
        Object mock = mock(String.class);
        Method method = MockUtil.getMethodByName(String.class, "toString");
        Object[] arguments = new Object[0];
        RealMethod realMethod = new RealMethod(mock.getClass(), method, null);
        Invocation invocation = new Invocation(mock, new MockitoMethod(method), arguments, 1, realMethod);

        boolean actual = Invocation.isToString(invocation);
        boolean expected = ObjectMethodsGuru.isToString(new MockitoMethod(method));

        assertThat(actual, is(expected));
    }

    @Test
    public void testIsVoid() throws Exception {
        Object mock = mock(String.class);
        Method method = MockUtil.getMethodByName(String.class, "toString");
        Object[] arguments = new Object[0];
        RealMethod realMethod = new RealMethod(mock.Class, method, null);
        Invocation invocation = new Invocation(mock, new MockitoMethod(method), arguments, 1, realMethod);

        boolean actual = invocation.isVoid();
        boolean expected = false;

        assertThat(actual, is(expected));
    }

    @Test
    public void testIsValidException() throws Exception {
        Object mock = mock(String.class);
        Method method = MockUtil.getMethodByName(String.class, "toString");
        Object[] arguments = new Object[0];
        RealMethod realMethod = new RealMethod(mock.getClass(), method, null);
        Invocation invocation = new Invocation(mock, new MockitoMethod(method), arguments, 1, realMethod);

        Class<?> expectedExceptionType = IllegalArgumentException.class;
        Throwable expectedException = new IllegalArgumentException("Test exception");
        Throwable actualException = new IllegalArgumentException("Test exception");

        boolean actual = invocation.isValidException(expectedException);
        boolean expected = expectedExceptionType.isAssignableFrom(expectedException.getClass());

        assertThat(actual, is(expected));
    }

    @Test
    public void testIsValidReturnType() throws Exception {
        Object mock = mock(String.class);
        Method method = MockUtil.getMethodByName(String.class, "toString");
        Object[] arguments = new Object[0];
        RealMethod realMethod = new RealMethod(mock.getClass(), method, null);
        Invocation invocation = new Invocation(mock, new MockitoMethod(method), arguments, 1, realMethod);

        Class<?> expectedReturnType = String.class;
        Class<?> actualReturnType = Void.TYPE;

        boolean actual = invocation.isValidReturnType(actualReturnType);
        boolean expected = false;

        assertThat(actual, is(expected));
    }

    @Test(expected = MockitoException.class)
    public void testCallRealMethodWithInvalidArguments() throws Exception {
        Object mock = mock(String.class);
        Method method = MockUtil.getMethodByName(String.class, "toString");
        Object[] arguments = new Object[2];
        RealMethod realMethod = new RealMethod(mock.getClass(), method, null);
        Invocation invocation = new Invocation(mock, new MockitoMethod(method), arguments, 1, realMethod);

        invocation.callRealMethod();
    }

    @Test
    public void testCallRealMethod() throws Exception {
        Object mock = mock(String.class);
        Method method = MockUtil.getMethodByName(String.class, "toString");
        Object[] arguments = new Object[0];
        RealMethod realMethod = new RealMethod(mock.getClass(), method, null);
        Invocation invocation = new Invocation(mock, new MockitoMethod(method), arguments, 1, realMethod);

        String expected = "Mockito\n    at org.mockito.internal.invocation.Invocation.toString(Invocation.java:100)\n";
        String actual = (String) invocation.callRealMethod();

        assertThat(actual, is(expected));
    }

    @Test
    public void testIsToStringWithSpy() throws Exception {
        Object mock = mock(String.class);
        Object spy = spy(mock);
        Method method = MockUtil.getMethodByName(String.class, "toString");
        Object[] arguments = new Object[0];
        RealMethod realMethod = new RealMethod(mock.getClass(), method, null);
        Invocation invocation = new Invocation(spy, new MockitoMethod(method), arguments, 1, realMethod);

        boolean actual = Invocation.isToString(invocation);
        boolean expected = ObjectMethodsGuru.isToString(new MockitoMethod(method));

        assertThat(actual, is(expected));
    }
}

class MockitoMethod {
    private Method method;

    public MockitoMethod(Method method) {
        this.method = method;
    }

    public Method getMethod() {
        return method;
    }

    public String getName() {
        return method.getName();
    }

    public Class<?> getReturnType() {
        return method.getReturnType();
    }

    public Class<?>[] getExceptionTypes() {
        return method.getExceptionTypes();
    }

    public boolean isVarArgs() {
        return method.isVarArgs();
    }

    public boolean isStatic() {
        return method.isStatic();
    }
}