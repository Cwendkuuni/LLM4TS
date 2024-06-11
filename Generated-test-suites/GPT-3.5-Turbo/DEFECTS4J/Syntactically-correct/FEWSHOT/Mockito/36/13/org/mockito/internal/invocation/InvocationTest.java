import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.junit.Test;
import org.mockito.internal.util.*;
import org.mockito.internal.invocation.*;
import org.mockito.invocation.InvocationOnMock;

public class InvocationTest {
    private final Object mock = mock(Object.class);
    private final MockitoMethod method = mock(MockitoMethod.class);
    private final Object[] args = { "arg1", "arg2" };
    private final int sequenceNumber = 1;
    private final RealMethod realMethod = mock(RealMethod.class);
    private final Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

    @Test
    public void testGetMock() {
        assertThat(invocation.getMock(), is(mock));
    }

    @Test
    public void testGetMethod() {
        assertThat(invocation.getMethod(), is(method));
    }

    @Test
    public void testGetArguments() {
        assertThat(invocation.getArguments(), is(args));
    }

    @Test
    public void testIsVerified() {
        assertThat(invocation.isVerified(), is(false));
    }

    @Test
    public void testGetSequenceNumber() {
        assertThat(invocation.getSequenceNumber(), is(sequenceNumber));
    }

    @Test
    public void testIsVerifiedInOrder() {
        assertThat(invocation.isVerifiedInOrder(), is(false));
    }

    @Test
    public void testEquals() {
        Invocation other = new Invocation(mock, method, args, sequenceNumber, realMethod);
        assertThat(invocation.equals(other), is(true));
    }

    @Test
    public void testHashCode() {
        assertThat(invocation.hashCode(), notNullValue());
    }

    @Test
    public void testToString() {
        assertThat(invocation.toString(), notNullValue());
    }

    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = new PrintSettings();
        assertThat(invocation.toString(printSettings), notNullValue());
    }

    @Test
    public void testIsToString() {
        InvocationOnMock invocationOnMock = mock(InvocationOnMock.class);
        when(invocationOnMock.getMethod()).thenReturn(method);
        ObjectMethodsGuru objectMethodsGuru = mock(ObjectMethodsGuru.class);
        when(objectMethodsGuru.isToString(method)).thenReturn(true);
        assertThat(Invocation.isToString(invocationOnMock), is(true));
    }

    @Test
    public void testIsValidException() {
        Throwable throwable = new RuntimeException();
        Class[] exceptionTypes = { RuntimeException.class };
        when(method.getExceptionTypes()).thenReturn(exceptionTypes);
        assertThat(invocation.isValidException(throwable), is(true));
    }

    @Test
    public void testIsValidReturnType() {
        Class<?> clazz = String.class;
        Class<?> returnType = String.class;
        when(method.getReturnType()).thenReturn(returnType);
        assertThat(invocation.isValidReturnType(clazz), is(true));
    }

    @Test
    public void testIsVoid() {
        Class<?> returnType = Void.TYPE;
        when(method.getReturnType()).thenReturn(returnType);
        assertThat(invocation.isVoid(), is(true));
    }

    @Test
    public void testPrintMethodReturnType() {
        Class<?> returnType = String.class;
        when(method.getReturnType()).thenReturn(returnType);
        assertThat(invocation.printMethodReturnType(), is("String"));
    }

    @Test
    public void testGetMethodName() {
        assertThat(invocation.getMethodName(), notNullValue());
    }

    @Test
    public void testReturnsPrimitive() {
        Class<?> returnType = int.class;
        when(method.getReturnType()).thenReturn(returnType);
        assertThat(invocation.returnsPrimitive(), is(true));
    }

    @Test
    public void testGetLocation() {
        assertThat(invocation.getLocation(), notNullValue());
    }

    @Test
    public void testGetArgumentsCount() {
        assertThat(invocation.getArgumentsCount(), is(args.length));
    }

    @Test
    public void testGetRawArguments() {
        assertThat(invocation.getRawArguments(), is(args));
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        when(realMethod.invoke(mock, args)).thenReturn("result");
        assertThat(invocation.callRealMethod(), is("result"));
    }

    @Test
    public void testToStringWithMatchers() {
        MatchersPrinter matchersPrinter = mock(MatchersPrinter.class);
        when(method.getName()).thenReturn("method");
        when(matchersPrinter.getArgumentsLine(anyList(), any(PrintSettings.class))).thenReturn("(arg1, arg2)");
        assertThat(invocation.toString(), notNullValue());
    }

    @Test
    public void testMarkVerified() {
        invocation.markVerified();
        assertThat(invocation.isVerified(), is(true));
    }

    @Test
    public void testMarkVerifiedInOrder() {
        invocation.markVerifiedInOrder();
        assertThat(invocation.isVerifiedInOrder(), is(true));
        assertThat(invocation.isVerified(), is(true));
    }
}