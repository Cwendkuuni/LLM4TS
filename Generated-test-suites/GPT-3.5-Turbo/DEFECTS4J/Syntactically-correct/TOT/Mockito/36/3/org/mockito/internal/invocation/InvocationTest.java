import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class InvocationTest {

    private Invocation invocation;

    @Before
    public void setUp() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[] {1, 2, 3};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);
        invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
    }

    @Test
    public void testGetMock() {
        Object mock = invocation.getMock();
        assertThat(mock, instanceOf(Object.class));
    }

    @Test
    public void testGetMethod() {
        MockitoMethod method = invocation.getMethod();
        assertThat(method, instanceOf(MockitoMethod.class));
    }

    @Test
    public void testGetArguments() {
        Object[] arguments = invocation.getArguments();
        assertThat(arguments, arrayContaining(1, 2, 3));
    }

    @Test
    public void testIsVerified() {
        boolean verified = invocation.isVerified();
        assertThat(verified, is(false));
    }

    @Test
    public void testGetSequenceNumber() {
        int sequenceNumber = invocation.getSequenceNumber();
        assertThat(sequenceNumber, is(1));
    }

    @Test
    public void testIsVerifiedInOrder() {
        boolean verifiedInOrder = invocation.isVerifiedInOrder();
        assertThat(verifiedInOrder, is(false));
    }

    @Test
    public void testEquals() {
        Invocation other = new Invocation(invocation.getMock(), invocation.getMethod(), invocation.getArguments(),
                invocation.getSequenceNumber(), invocation.realMethod);

        boolean equals = invocation.equals(other);
        assertThat(equals, is(true));
    }

    @Test
    public void testEqualArguments() {
        Object[] arguments = new Object[] {1, 2, 3};
        boolean equalArguments = invocation.equalArguments(arguments);
        assertThat(equalArguments, is(true));
    }

    @Test
    public void testToString() {
        String expected = "mock.Object.method(1, 2, 3)";
        String actual = invocation.toString();
        assertThat(actual, is(expected));
    }

    @Test
    public void testToStringWithMatchBlock() {
        List<Matcher> matchers = new ArrayList<>();
        matchers.add(new Equals(1));
        matchers.add(new Equals(2));
        matchers.add(new Equals(3));

        PrintSettings printSettings = new PrintSettings();
        String expected = "mock.Object.method" +
                matchersToString(matchers, printSettings);
        String actual = invocation.toString(matchers, printSettings);
        assertThat(actual, is(expected));
    }

    private String matchersToString(List<Matcher> matchers, PrintSettings printSettings) {
        StringBuilder stringBuilder = new StringBuilder();
        MatchersPrinter matchersPrinter = new MatchersPrinter();
        if (printSettings.isMultiline() || (!matchers.isEmpty() && invocation.toString().length() > Invocation.MAX_LINE_LENGTH)) {
            stringBuilder.append(matchersPrinter.getArgumentsBlock(matchers, printSettings));
        }
        return stringBuilder.toString();
    }

    @Test
    public void testArgumentsToMatchers() {
        List<Matcher> matchers = invocation.argumentsToMatchers();
        assertThat(matchers.size(), is(3));
        assertThat(matchers.get(0), instanceOf(Equals.class));
        assertThat(matchers.get(1), instanceOf(Equals.class));
        assertThat(matchers.get(2), instanceOf(Equals.class));
    }

    @Test
    public void testIsToString() {
        MockitoMethod method = mock(MockitoMethod.class);
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(method);
        ObjectMethodsGuru objectMethodsGuru = mock(ObjectMethodsGuru.class);
        when(objectMethodsGuru.isToString(method)).thenReturn(true);

        boolean isToString = Invocation.isToString(invocation);

        assertThat(isToString, is(true));
    }

    @Test
    public void testIsValidException() {
        Throwable throwable = new RuntimeException();
        MockitoMethod method = mock(MockitoMethod.class);
        when(method.getExceptionTypes()).thenReturn(new Class<?>[] {RuntimeException.class});

        boolean isValidException = invocation.isValidException(throwable);

        assertThat(isValidException, is(true));
    }

    @Test
    public void testIsValidReturnTypeWithPrimitiveType() {
        Class<?> clazz = int.class;
        MockitoMethod method = mock(MockitoMethod.class);
        when(method.getReturnType()).thenReturn(int.class);

        boolean isValidReturnType = invocation.isValidReturnType(clazz);

        assertThat(isValidReturnType, is(true));
    }

    @Test
    public void testIsValidReturnTypeWithNonPrimitiveType() {
        Class<?> clazz = Object.class;
        MockitoMethod method = mock(MockitoMethod.class);
        when(method.getReturnType()).thenReturn(Object.class);

        boolean isValidReturnType = invocation.isValidReturnType(clazz);

        assertThat(isValidReturnType, is(true));
    }

    @Test
    public void testIsVoidWithVoidReturnType() {
        MockitoMethod method = mock(MockitoMethod.class);
        when(method.getReturnType()).thenReturn(void.class);

        boolean isVoid = invocation.isVoid();

        assertThat(isVoid, is(true));
    }

    @Test
    public void testPrintMethodReturnType() {
        String methodReturnType = invocation.printMethodReturnType();

        assertThat(methodReturnType, is("MockitoMethod"));
    }

    @Test
    public void testGetMethodName() {
        String methodName = invocation.getMethodName();

        assertThat(methodName, is("getName"));
    }

    @Test
    public void testReturnsPrimitiveWithPrimitiveReturnType() {
        MockitoMethod method = mock(MockitoMethod.class);
        when(method.getReturnType()).thenReturn(int.class);

        boolean returnsPrimitive = invocation.returnsPrimitive();

        assertThat(returnsPrimitive, is(true));
    }

    @Test
    public void testGetLocation() {
        Location location = invocation.getLocation();

        assertThat(location, instanceOf(Location.class));
    }

    @Test
    public void testGetArgumentsCount() {
        int argumentsCount = invocation.getArgumentsCount();

        assertThat(argumentsCount, is(3));
    }

    @Test
    public void testGetRawArguments() {
        Object[] rawArguments = invocation.getRawArguments();

        assertThat(rawArguments, arrayContaining(1, 2, 3));
    }

    @Test(expected = Throwable.class)
    public void testCallRealMethodWithException() throws Throwable {
        RealMethod realMethod = mock(RealMethod.class);
        when(realMethod.invoke(any(), any())).thenThrow(Throwable.class);

        invocation.callRealMethod();
    }

    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = new PrintSettings();
        printSettings.setMultiline(true);

        String expected = "mock.Object.method(1, 2, 3)";
        String actual = invocation.toString(printSettings);
        assertThat(actual, is(expected));
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
    }

}