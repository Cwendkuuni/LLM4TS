import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.sameInstance;

@RunWith(MockitoJUnitRunner.class)
public class InvocationTest {

    @Test
    public void testNewInstance() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Object[] arguments = new Object[0];
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, realMethod);

        assertThat(invocation.getMock(), is(sameInstance(mock)));
        assertThat(invocation.getMethod(), is(sameInstance(mockitoMethod)));
        assertThat(invocation.getArguments(), is(notNullValue()));
        assertThat(invocation.isVerified(), is(false));
        assertThat(invocation.isVerifiedInOrder(), is(false));
        assertThat(invocation.getSequenceNumber(), is(1));
        assertThat(invocation.toString(), is(notNullValue()));
        assertThat(invocation.callRealMethod(), is(notNullValue()));
    }

    @Test
    public void testExpandVarArgs() {
        Object mock = Mockito.mock(List.class);
        MockitoMethod mockitoMethod = Mockito.method(List.class, "addAll", List.class);
        Object[] args = { ArgumentMatchers.anyList(), new Object[]{1, 2, 3} };
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, Mockito.mock(RealMethod.class));
        Object[] expectedArgs = { ArgumentMatchers.anyList(), new Object[]{1, 2, 3}, new Object[]{1, 2, 3} };

        assertThat(invocation.getArguments(), is(sameInstance(expectedArgs)));
    }

    @Test
    public void testEquals() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Object[] arguments1 = new Object[0];
        Object[] arguments2 = new Object[]{1};
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        Invocation invocation1 = new Invocation(mock, mockitoMethod, arguments1, 1, realMethod);
        Invocation invocation2 = new Invocation(mock, mockitoMethod, arguments2, 1, realMethod);
        Invocation invocation3 = new Invocation(mock, mockitoMethod, arguments1, 2, realMethod);

        assertThat(invocation1, is(not(sameInstance(invocation2))));
        assertThat(invocation1, is(not(sameInstance(invocation3))));
    }

    @Test
    public void testToString() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Object[] arguments = new Object[0];
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, realMethod);

        String expectedToString = "String.length()";
        String invocationToString = invocation.toString();

        assertThat(invocationToString, is(expectedToString));
    }

    @Test
    public void testIsToString() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "toString");
        Object[] arguments = new Object[0];
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, realMethod);

        boolean result = Invocation.isToString(invocation);

        assertThat(result, is(true));
    }

    @Test
    public void testIsValidException() {
        Object mock = Mockito.mock(Exception.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Object[] arguments = new Object[0];
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, realMethod);
        Throwable throwable = new RuntimeException();

        boolean result = invocation.isValidException(throwable);

        assertThat(result, is(false));
    }

    @Test
    public void testIsValidReturnType() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Object[] arguments = new Object[0];
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, realMethod);

        Class<Integer> integerClass = Integer.class;
        boolean result = invocation.isValidReturnType(integerClass);

        assertThat(result, is(false));
    }

    @Test
    public void testIsVoid() {
        Object mock = Mockito.mock(Void.TYPE.getClass());
        MockitoMethod mockitoMethod = Mockito.method(Void.TYPE.getClass(), "toString", (Class[]) null);
        Object[] arguments = new Object[0];
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, realMethod);

        boolean result = invocation.isVoid();

        assertThat(result, is(true));
    }

    @Test
    public void testPrintMethodReturnType() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Object[] arguments = new Object[0];
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, realMethod);

        String expectedReturnType = "int";
        String result = invocation.printMethodReturnType();

        assertThat(result, is(expectedReturnType));
    }

    @Test
    public void testMarkVerified() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "length");
        Object[] arguments = new Object[0];
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, realMethod);

        invocation.markVerified();

        boolean result = invocation.isVerified();

        assertThat(result, is(true));
    }

    @Test
    public void testMarkVerifiedInOrder() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod1 = Mockito.method(String.class, "length");
        MockitoMethod mockitoMethod2 = Mockito.method(String.class, "toUpperCase");
        Object[] arguments1 = new Object[0];
        Object[] arguments2 = new Object[0];
        RealMethod realMethod1 = Mockito.mock(RealMethod.class);
        RealMethod realMethod2 = Mockito.mock(RealMethod.class);
        Invocation invocation1 = new Invocation(mock, mockitoMethod1, arguments1, 1, realMethod1);
        Invocation invocation2 = new Invocation(mock, mockitoMethod2, arguments2, 2, realMethod2);

        invocation1.markVerifiedInOrder();

        boolean result1 = invocation1.isVerifiedInOrder();
        boolean result2 = invocation2.isVerifiedInOrder();

        assertThat(result1, is(true));
        assertThat(result2, is(false));
    }
}