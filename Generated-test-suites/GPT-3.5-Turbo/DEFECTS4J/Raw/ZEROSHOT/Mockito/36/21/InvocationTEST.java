import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

public class InvocationTest {

    private Object mock;
    private MockitoMethod mockitoMethod;
    private Object[] args;
    private int sequenceNumber;
    private RealMethod realMethod;
    private Invocation invocation;
    private List<Matcher> matchers;
    private PrintSettings printSettings;

    @Before
    public void setUp() {
        mock = mock(Object.class);
        mockitoMethod = mock(MockitoMethod.class);
        args = new Object[]{"arg1", "arg2"};
        sequenceNumber = 1;
        realMethod = mock(RealMethod.class);
        invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);
        matchers = new ArrayList<>();
        printSettings = mock(PrintSettings.class);
    }

    @Test
    public void testGetMock() {
        assertThat(invocation.getMock(), is(sameInstance(mock)));
    }

    @Test
    public void testGetMethod() {
        assertThat(invocation.getMethod(), is(sameInstance(mockitoMethod)));
    }

    @Test
    public void testGetArguments() {
        assertThat(invocation.getArguments(), is(sameInstance(args)));
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
        Invocation otherInvocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);
        assertThat(invocation.equals(otherInvocation), is(true));
    }

    @Test
    public void testHashCode() {
        try {
            invocation.hashCode();
        } catch (RuntimeException e) {
            assertThat(e.getMessage(), is("hashCode() is not implemented"));
        }
    }

    @Test
    public void testToString() {
        List<Matcher> expectedMatchers = new ArrayList<>();
        expectedMatchers.add(new Equals("arg1"));
        expectedMatchers.add(new Equals("arg2"));

        MatchersPrinter matchersPrinter = new MatchersPrinter();
        String expectedInvocation = invocation.getMethod().getName() + matchersPrinter.getArgumentsLine(expectedMatchers, printSettings);
        
        assertThat(invocation.toString(), is(expectedInvocation));
    }

    @Test
    public void testToStringWithMultiline() {
        List<Matcher> expectedMatchers = new ArrayList<>();
        expectedMatchers.add(new Equals("arg1"));
        expectedMatchers.add(new Equals("arg2"));

        MatchersPrinter matchersPrinter = new MatchersPrinter();
        String expectedInvocation = invocation.getMethod().getName() + matchersPrinter.getArgumentsBlock(expectedMatchers, printSettings);

        when(printSettings.isMultiline()).thenReturn(true);
        
        assertThat(invocation.toString(printSettings), is(expectedInvocation));
    }

    @Test
    public void testArgumentsToMatchers() {
        List<Matcher> expectedMatchers = new ArrayList<>();
        expectedMatchers.add(new Equals("arg1"));
        expectedMatchers.add(new Equals("arg2"));

        assertThat(invocation.argumentsToMatchers(), is(expectedMatchers));
    }

    @Test
    public void testIsToString() {
        MockitoMethod toStringMethod = mock(MockitoMethod.class);
        when(toStringMethod.getName()).thenReturn("toString");

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(toStringMethod);

        assertThat(Invocation.isToString(invocation), is(true));
    }

    @Test
    public void testIsValidException() {
        Throwable throwable = new Throwable();
        MockitoMethod exceptionMethod = mock(MockitoMethod.class);
        Class<?> throwableClass = throwable.getClass();
        Class<?>[] exceptionTypes = {throwableClass};
        when(exceptionMethod.getExceptionTypes()).thenReturn(exceptionTypes);

        Invocation invocation = new Invocation(mock, exceptionMethod, args, sequenceNumber, realMethod);

        assertThat(invocation.isValidException(throwable), is(true));
    }

    @Test
    public void testIsValidReturnTypeWithPrimitive() {
        MockitoMethod primitiveMethod = mock(MockitoMethod.class);
        Class<?> returnType = int.class;
        when(primitiveMethod.getReturnType()).thenReturn(returnType);

        Invocation invocation = new Invocation(mock, primitiveMethod, args, sequenceNumber, realMethod);

        assertThat(invocation.isValidReturnType(int.class), is(true));
    }

    @Test
    public void testIsValidReturnTypeWithNonPrimitive() {
        MockitoMethod nonPrimitiveMethod = mock(MockitoMethod.class);
        Class<?> returnType = Object.class;
        when(nonPrimitiveMethod.getReturnType()).thenReturn(returnType);

        Invocation invocation = new Invocation(mock, nonPrimitiveMethod, args, sequenceNumber, realMethod);

        assertThat(invocation.isValidReturnType(Object.class), is(true));
    }

    @Test
    public void testIsVoid() {
        MockitoMethod voidMethod = mock(MockitoMethod.class);
        Class<?> returnType = void.class;
        when(voidMethod.getReturnType()).thenReturn(returnType);

        Invocation invocation = new Invocation(mock, voidMethod, args, sequenceNumber, realMethod);

        assertThat(invocation.isVoid(), is(true));
    }

    @Test
    public void testPrintMethodReturnType() {
        MockitoMethod returnTypeMethod = mock(MockitoMethod.class);
        Class<?> returnType = Object.class;
        when(returnTypeMethod.getReturnType()).thenReturn(returnType);

        Invocation invocation = new Invocation(mock, returnTypeMethod, args, sequenceNumber, realMethod);

        assertThat(invocation.printMethodReturnType(), is(returnType.getSimpleName()));
    }

    @Test
    public void testGetMethodName() {
        MockitoMethod methodNameMethod = mock(MockitoMethod.class);
        String methodName = "testMethod";
        when(methodNameMethod.getName()).thenReturn(methodName);

        Invocation invocation = new Invocation(mock, methodNameMethod, args, sequenceNumber, realMethod);

        assertThat(invocation.getMethodName(), is(methodName));
    }

    @Test
    public void testReturnsPrimitive() {
        MockitoMethod primitiveReturnMethod = mock(MockitoMethod.class);
        Class<?> returnType = int.class;
        when(primitiveReturnMethod.getReturnType()).thenReturn(returnType);

        Invocation invocation = new Invocation(mock, primitiveReturnMethod, args, sequenceNumber, realMethod);

        assertThat(invocation.returnsPrimitive(), is(true));
    }

    @Test
    public void testGetLocation() {
        assertThat(invocation.getLocation(), is(notNullValue()));
    }

    @Test
    public void testGetArgumentsCount() {
        assertThat(invocation.getArgumentsCount(), is(args.length));
    }

    @Test
    public void testGetRawArguments() {
        assertThat(invocation.getRawArguments(), is(sameInstance(args)));
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        Object returnValue = new Object();
        when(realMethod.invoke(mock, args)).thenReturn(returnValue);

        assertThat(invocation.callRealMethod(), is(sameInstance(returnValue)));
    }

    @Test
    public void testToStringWithPrintSettings() {
        List<Matcher> expectedMatchers = new ArrayList<>();
        expectedMatchers.add(new Equals("arg1"));
        expectedMatchers.add(new Equals("arg2"));

        MatchersPrinter matchersPrinter = new MatchersPrinter();
        String expectedInvocation = invocation.getMethod().getName() + matchersPrinter.getArgumentsLine(expectedMatchers, printSettings);

        assertThat(invocation.toString(printSettings), is(expectedInvocation));
    }

    @Test
    public void testMarkVerified() {
        invocation.markVerified();

        assertThat(invocation.isVerified(), is(true));
    }

    @Test
    public void testMarkVerifiedInOrder() {
        invocation.markVerifiedInOrder();

        assertThat(invocation.isVerified(), is(true));
        assertThat(invocation.isVerifiedInOrder(), is(true));
    }
}