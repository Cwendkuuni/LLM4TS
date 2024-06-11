import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.matchers.ArgumentsMatcher;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.Equals;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InvocationTest {

    @Test
    public void testNewInvocation() {
        Object mock = mock(String.class);
        Method method = Mockito.method(String.class, "length");
        Object[] rawArguments = new Object[]{mock};
        Object[] arguments = new Object[]{mock};
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(method, null, null, null), arguments, 1, realMethod);

        assertThat(invocation.getMock(), is(notNullValue()));
        assertThat(invocation.getMethod(), is(notNullValue()));
        assertThat(invocation.getArguments(), is(notNullValue()));
        assertThat(invocation.getRawArguments(), is(notNullValue()));
        assertThat(invocation.getLocation(), is(notNullValue()));
        assertThat(invocation.isVerified(), is(false));
        assertThat(invocation.isVerifiedInOrder(), is(false));
        assertThat(invocation.realMethod, is(notNullValue()));
        assertThat(invocation.sequenceNumber, is(1));
    }

    @Test
    public void testToString() {
        Object mock = mock(String.class);
        Method method = Mockito.method(String.class, "length");
        Object[] rawArguments = new Object[]{mock};
        Object[] arguments = new Object[]{mock};
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(method, null, null, null), arguments, 1, realMethod);

        List<Matcher> matchers = invocation.argumentsToMatchers();
        String expected = String.format("%s.length(%s)", mock.getClass().getName(), Arrays.toString(matchers.toArray()));
        String actual = invocation.toString();

        assertThat(actual, is(expected));
    }

    @Test
    public void testIsToString() {
        Object mock = mock(String.class);
        Method method = Mockito.method(String.class, "toString");
        Invocation invocation = new Invocation(mock, new MockitoMethod(method, null, null, null), new Object[0], 1, null);

        boolean actual = Invocation.isToString(invocation);
        boolean expected = new ObjectMethodsGuru().isToString(method);

        assertThat(actual, is(expected));
    }

    @Test
    public void testIsVerified() {
        Object mock = mock(String.class);
        Method method = Mockito.method(String.class, "length");
        Object[] rawArguments = new Object[]{mock};
        Object[] arguments = new Object[]{mock};
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(method, null, null, null), arguments, 1, realMethod);

        verifyZeroInteractions(mock);

        invocation.markVerified();
        verify(mock).length();
    }

    @Test
    public void testIsVerifiedInOrder() {
        Object mock = mock(String.class);
        Method method = Mockito.method(String.class, "length");
        Object[] rawArguments = new Object[]{mock};
        Object[] arguments = new Object[]{mock};
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        Invocation invocation1 = new Invocation(mock, new MockitoMethod(method, null, null, null), arguments, 1, realMethod);
        Invocation invocation2 = new Invocation(mock, new MockitoMethod(method, null, null, null), arguments, 2, realMethod);

        verifyZeroInteractions(mock);

        invocation1.markVerifiedInOrder();
        verify(mock).length();
        invocation2.markVerifiedInOrder();
        verify(mock).length();
        verify(mock).length();
    }

    @Test(expected = RuntimeException.class)
    public void testHashCode() {
        Object mock = mock(String.class);
        Method method = Mockito.method(String.class, "length");
        Object[] rawArguments = new Object[]{mock};
        Object[] arguments = new Object[]{mock};
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        Invocation invocation1 = new Invocation(mock, new MockitoMethod(method, null, null, null), arguments, 1, realMethod);
        Invocation invocation2 = new Invocation(mock, new MockitoMethod(method, null, null, null), arguments, 1, realMethod);

        invocation1.hashCode();
        invocation2.hashCode();
    }

    @Test
    public void testEquals() {
        Object mock = mock(String.class);
        Method method = Mockito.method(String.class, "length");
        Object[] rawArguments = new Object[]{mock};
        Object[] arguments = new Object[]{mock};
        Object[] rawArguments2 = new Object[]{mock};
        Object[] arguments2 = new Object[]{mock};
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        Invocation invocation1 = new Invocation(mock, new MockitoMethod(method, null, null, null), arguments, 1, realMethod);
        Invocation invocation2 = new Invocation(mock, new MockitoMethod(method, null, null, null), arguments2, 1, realMethod);
        Invocation invocation3 = new Invocation(mock, new MockitoMethod(method, null, null, null), arguments, 2, realMethod);

        assertThat(invocation1, is(invocation1));
        assertThat(invocation1, is(not(invocation2)));
        assertThat(invocation1, is(not(invocation3)));
        assertThat(invocation1, is(not(new Object())));

        Object[] argumentsWithVarArgs = new Object[]{mock, new Object[0]};
        Invocation invocationWithVarArgs = new Invocation(mock, new MockitoMethod(method, null, null, true), argumentsWithVarArgs, 1, realMethod);

        assertThat(invocation1, is(not(invocationWithVarArgs)));
    }

    @Test
    public void testIsValidException() {
        Object mock = mock(String.class);
        Method method = Mockito.method(String.class, "length", new Class[0]);
        Object[] rawArguments = new Object[]{mock};
        Object[] arguments = new Object[]{mock};
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(method, Throwable.class, new Class[0]), arguments, 1, realMethod);

        Throwable exception = new RuntimeException();

        boolean actual = invocation.isValidException(exception);
        boolean expected = method.getExceptionTypes().length > 0;

        assertThat(actual, is(expected));
    }

    @Test
    public void testIsValidReturnType() {
        Object mock = mock(String.class);
        Method method = Mockito.method(String.class, "length");
        Object[] rawArguments = new Object[]{mock};
        Object[] arguments = new Object[]{mock};
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(method, null, null, int.class), arguments, 1, realMethod);

        boolean actual = invocation.isValidReturnType(int.class);
        boolean expected = true;

        assertThat(actual, is(expected));
    }

    @Test
    public void testIsVoid() {
        Object mock = mock(String.class);
        Method method = Mockito.method(void.class, "length");
        Object[] rawArguments = new Object[]{mock};
        Object[] arguments = new Object[]{mock};
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(method, null, null, null), arguments, 1, realMethod);

        boolean actual = invocation.isVoid();
        boolean expected = true;

        assertThat(actual, is(expected));
    }

    @Test
    public void testPrintMethodReturnType() {
        Object mock = mock(String.class);
        Method method = Mockito.method(int.class, "length");
        Object[] rawArguments = new Object[]{mock};
        Object[] arguments = new Object[]{mock};
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(method, null, null, null), arguments, 1, realMethod);

        String actual = invocation.printMethodReturnType();
        String expected = "int";

        assertThat(actual, is(expected));
    }

    @Test
    public void testGetMethodName() {
        Object mock = mock(String.class);
        Method method = Mockito.method(int.class, "length");
        Object[] rawArguments = new Object[]{mock};
        Object[] arguments = new Object[]{mock};
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(method, null, null, null), arguments, 1, realMethod);

        String actual = invocation.getMethodName();
        String expected = "length";

        assertThat(actual, is(expected));
    }

    @Test
    public void testReturnsPrimitive() {
        Object mock = mock(String.class);
        Method method = Mockito.method(boolean.class, "length");
        Object[] rawArguments = new Object[]{mock};
        Object[] arguments = new Object[]{mock};
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(method, null, null, null), arguments, 1, realMethod);

        boolean actual = invocation.returnsPrimitive();
        boolean expected = true;

        assertThat(actual, is(expected));
    }

    @Test
    public void testGetLocation() {
        Object mock = mock(String.class);
        Method method = Mockito.method(int.class, "length");
        Object[] rawArguments = new Object[]{mock};
        Object[] arguments = new Object[]{mock};
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(method, null, null, null), arguments, 1, realMethod);

        Location actual = invocation.getLocation();
        Location expected = new Location();

        assertThat(actual, is(expected));
    }

    @Test
    public void testGetArgumentsCount() {
        Object mock = mock(String.class);
        Method method = Mockito.method(int.class, "length");
        Object[] rawArguments = new Object[]{mock};
        Object[] arguments = new Object[]{mock};
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(method, null, null, null), arguments, 1, realMethod);

        int actual = invocation.getArgumentsCount();
        int expected = 0;

        assertThat(actual, is(expected));
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        Object mock = mock(String.class);
        Method method = Mockito.method(int.class, "length");
        Object[] rawArguments = new Object[]{mock};
        Object[] arguments = new Object[]{mock};
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(method, null, null, null), arguments, 1, realMethod);

        int actual = (int) invocation.callRealMethod();
        int expected = mock.length();

        assertThat(actual, is(expected));
    }
}