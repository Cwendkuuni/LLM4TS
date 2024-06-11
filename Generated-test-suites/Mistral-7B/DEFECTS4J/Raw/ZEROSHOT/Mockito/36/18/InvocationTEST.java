package org.mockito.tests.internal.invocation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.util.Primitives;
import org.mockito.internal.util.MockitoMethod;
import org.mockito.internal.invocation.realmethod.RealMethod;
import org.mockito.exceptions.PrintableInvocation;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.internal.util.ArrayEquals.createObjectArray;
import static org.mockito.internal.util.MockUtil.getMockName;
import static org.mockito.internal.util.Primitives.primitiveTypeOf;
import static org.mockito.junit.MockitoJUnitRunner.mock;

@RunWith(MockitoJUnitRunner.class)
public class InvocationTest {

    @Test
    public void testNewInstance() {
        // given
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = new MockitoMethod(String.class, "length", new Class[0]);
        Object[] args = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        // when
        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);

        // then
        assertThat(invocation.getMock(), is(mock));
        assertThat(invocation.getMethod(), is(mockitoMethod));
        assertThat(invocation.getArguments(), is(args));
        assertThat(invocation.getSequenceNumber(), is(sequenceNumber));
        assertThat(invocation.isVerified(), is(false));
        assertThat(invocation.isVerifiedInOrder(), is(false));
        assertThat(invocation.realMethod, is(realMethod));
    }

    @Test
    public void testExpandVarArgs() {
        // given
        Object mock = mock(Object[].class);
        MockitoMethod mockitoMethod = new MockitoMethod(Object[].class, "toString", new Class[0]);
        Object[] args = new Object[] { mock };
        boolean isVarArgs = true;

        // when
        Object[] expandedArgs = Invocation.expandVarArgs(isVarArgs, args);

        // then
        Object[] expectedExpandedArgs = new Object[] { mock, null };
        assertThat(expandedArgs, is(expectedExpandedArgs));
    }

    @Test
    public void testToString() {
        // given
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = new MockitoMethod(String.class, "length", new Class[0]);
        Object[] args = new Object[0];
        int sequenceNumber = 1;

        // when
        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, null);
        String printSettingsString = "printSettings";
        PrintSettings printSettings = new PrintSettings(printSettingsString);

        // then
        String expectedInvocation = "String.length()";
        String invocationString = invocation.toString(printSettings);
        assertThat(invocationString, is(expectedInvocation));
    }

    @Test
    public void testToStringWithArguments() {
        // given
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = new MockitoMethod(String.class, "length", new Class[0]);
        Object[] args = new Object[] { "hello" };
        int sequenceNumber = 1;

        // when
        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, null);
        String printSettingsString = "printSettings";
        PrintSettings printSettings = new PrintSettings(printSettingsString);

        // then
        List<Matcher> matchers = invocation.argumentsToMatchers();
        String expectedInvocation = "String.length(\"hello\")";
        String invocationString = invocation.toString(matchers, printSettings);
        assertThat(invocationString, is(expectedInvocation));
    }

    @Test
    public void testToStringMultiline() {
        // given
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = new MockitoMethod(String.class, "substring", new Class[] { int.class, int.class });
        Object[] args = new Object[] { mock, 1, 2 };
        int sequenceNumber = 1;

        // when
        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, null);
        String printSettingsString = "printSettings";
        PrintSettings printSettings = new PrintSettings(printSettingsString);

        // then
        List<Matcher> matchers = invocation.argumentsToMatchers();
        String expectedInvocation = "String \"%s\".substring(1, 2)";
        String invocationString = invocation.toString(matchers, printSettings);
        assertThat(invocationString, is(expectedInvocation));
    }

    @Test
    public void testEquals() {
        // given
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod1 = new MockitoMethod(String.class, "length", new Class[0]);
        MockitoMethod mockitoMethod2 = new MockitoMethod(String.class, "substring", new Class[] { int.class, int.class });
        Object[] args1 = new Object[0];
        Object[] args2 = new Object[] { 1, 2 };
        int sequenceNumber1 = 1;
        int sequenceNumber2 = 1;

        // when
        Invocation invocation1 = new Invocation(mock, mockitoMethod1, args1, sequenceNumber1, null);
        Invocation invocation2 = new Invocation(mock, mockitoMethod2, args2, sequenceNumber2, null);

        // then
        assertThat(invocation1, is(invocation1));
        assertThat(invocation1, is(not(invocation2)));
    }

    @Test
    public void testIsToString() {
        // given
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = new MockitoMethod(String.class, "toString", new Class[0]);

        // when
        boolean isToString = Invocation.isToString(new InvocationOnMock(mockitoMethod, mock));

        // then
        assertThat(isToString, is(true));
    }

    @Test
    public void testIsValidException() {
        // given
        Object mock = mock(Exception.class);
        MockitoMethod mockitoMethod = new MockitoMethod(String.class, "length", new Class[0]);
        Object[] args = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        // when
        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);
        Throwable throwable = new Exception();

        // then
        boolean isValidException = invocation.isValidException(throwable);
        assertThat(isValidException, is(true));
    }

    @Test
    public void testIsValidReturnType() {
        // given
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = new MockitoMethod(String.class, "length", new Class[0]);
        Object[] args = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        // when
        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);

        // then
        Class expectedReturnType = int.class;
        boolean isValidReturnType = invocation.isValidReturnType(expectedReturnType);
        assertThat(isValidReturnType, is(true));
    }

    @Test
    public void testIsVoid() {
        // given
        Object mock = mock(Void.TYPE);
        MockitoMethod mockitoMethod = new MockitoMethod(Void.TYPE, "toString", new Class[0]);
        Object[] args = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        // when
        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);

        // then
        boolean isVoid = invocation.isVoid();
        assertThat(isVoid, is(true));
    }

    @Test
    public void testPrintMethodReturnType() {
        // given
        Object mock = mock(Integer.class);
        MockitoMethod mockitoMethod = new MockitoMethod(Integer.class, "intValue", new Class[0]);
        Object[] args = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        // when
        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);

        // then
        String expectedReturnType = "int";
        String printMethodReturnType = invocation.printMethodReturnType();
        assertThat(printMethodReturnType, is(expectedReturnType));
    }

    @Test
    public void testGetMethodName() {
        // given
        Object mock = mock(Integer.class);
        MockitoMethod mockitoMethod = new MockitoMethod(Integer.class, "intValue", new Class[0]);
        Object[] args = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        // when
        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);

        // then
        String expectedMethodName = "intValue";
        String getMethodName = invocation.getMethodName();
        assertThat(getMethodName, is(expectedMethodName));
    }

    @Test
    public void testReturnsPrimitive() {
        // given
        Object mock = mock(boolean.class);
        MockitoMethod mockitoMethod = new MockitoMethod(boolean.class, "booleanValue", new Class[0]);
        Object[] args = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        // when
        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);

        // then
        boolean returnsPrimitive = invocation.returnsPrimitive();
        assertThat(returnsPrimitive, is(true));
    }

    @Test
    public void testGetLocation() {
        // given
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = new MockitoMethod(String.class, "length", new Class[0]);
        Object[] args = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        // when
        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);

        // then
        Location expectedLocation = new Location();
        Location getLocation = invocation.getLocation();
        assertThat(getLocation, is(expectedLocation));
    }

    @Test
    public void testGetArgumentsCount() {
        // given
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = new MockitoMethod(String.class, "length", new Class[0]);
        Object[] args = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        // when
        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);

        // then
        int expectedArgumentsCount = 0;
        int getArgumentsCount = invocation.getArgumentsCount();
        assertThat(getArgumentsCount, is(expectedArgumentsCount));
    }

    @Test
    public void testCallRealMethod() throws Exception {
        // given
        Object mock = mock(Integer.class);
        MockitoMethod mockitoMethod = new MockitoMethod(Integer.class, "intValue", new Class[0]);
        Object[] args = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        // when
        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);
        int result = invocation.callRealMethod();

        // then
        int expectedResult = 0;
        assertThat(result, is(expectedResult));
    }

    @Test(expected = RuntimeException.class)
    public void testHashCode() {
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = new MockitoMethod(String.class, "length", new Class[0]);
        Object[] args = new Object[0];
        int sequenceNumber = 1;

        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, null);
        invocation.hashCode();
    }

    @Test
    public void testMarkVerified() {
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = new MockitoMethod(String.class, "length", new Class[0]);
        Object[] args = new Object[0];
        int sequenceNumber = 1;

        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, null);

        // when
        invocation.markVerified();

        // then
        boolean expectedVerified = true;
        boolean isVerified = invocation.isVerified();
        assertThat(isVerified, is(expectedVerified));
    }

    @Test
    public void testMarkVerifiedInOrder() {
        Object mock = mock(String.class);
        MockitoMethod mockitoMethod = new MockitoMethod(String.class, "length", new Class[0]);
        Object[] args = new Object[0];
        int sequenceNumber = 1;

        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, null);

        // when
        invocation.markVerifiedInOrder();

        // then
        boolean expectedVerified = true;
        boolean isVerified = invocation.isVerified();
        boolean expectedVerifiedInOrder = true;
        boolean isVerifiedInOrder = invocation.isVerifiedInOrder();
        assertThat(isVerified, is(expectedVerified));
        assertThat(isVerifiedInOrder, is(expectedVerifiedInOrder));
    }
}