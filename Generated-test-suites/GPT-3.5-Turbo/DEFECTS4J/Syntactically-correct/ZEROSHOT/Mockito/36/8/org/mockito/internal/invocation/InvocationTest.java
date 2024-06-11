import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.realmethod.RealMethod;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.matchers.MatchersPrinter;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class InvocationTest {

    private Invocation invocation;

    @Before
    public void setUp() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod();
        Object[] args = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();

        invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);
    }

    @Test
    public void testGetMock() {
        assertThat(invocation.getMock(), is(notNullValue()));
    }

    @Test
    public void testGetMethod() {
        assertThat(invocation.getMethod(), is(notNullValue()));
    }

    @Test
    public void testGetArguments() {
        assertThat(invocation.getArguments(), is(notNullValue()));
    }

    @Test
    public void testIsVerified() {
        assertThat(invocation.isVerified(), is(false));
    }

    @Test
    public void testGetSequenceNumber() {
        assertThat(invocation.getSequenceNumber(), is(notNullValue()));
    }

    @Test
    public void testIsVerifiedInOrder() {
        assertThat(invocation.isVerifiedInOrder(), is(false));
    }

    @Test
    public void testEquals() {
        Invocation other = new Invocation(new Object(), new MockitoMethod(), new Object[0], 1, new RealMethod());
        assertThat(invocation.equals(other), is(false));

        Invocation same = newInvocation();
        assertThat(invocation.equals(same), is(true));
    }

    @Test
    public void testHashCode() {
        // TODO: Implement test case
    }

    @Test
    public void testToString() {
        String expected = invocation.getMethod().getName();
        assertThat(invocation.toString(), containsString(expected));
    }

    @Test
    public void testEqualsArguments() {
        Invocation other = newInvocation();
        assertThat(invocation.equalsArguments(other.getArguments()), is(true));
    }

    @Test
    public void testToStringWithMatchers() {
        String expected = invocation.getMethod().getName();
        assertThat(invocation.toStringWithMatchers(), containsString(expected));
    }

    @Test
    public void testIsToString() {
        InvocationOnMock invocationOnMock = new Invocation(null, null, null, null, null, null, null, false);
        assertThat(Invocation.isToString(invocationOnMock), is(false));
    }

    @Test
    public void testIsValidException() {
        Throwable throwable = new Throwable();
        assertThat(invocation.isValidException(throwable), is(false));
    }

    @Test
    public void testIsValidReturnType() {
        Class clazz = Object.class;
        assertThat(invocation.isValidReturnType(clazz), is(true));
    }

    @Test
    public void testIsVoid() {
        assertThat(invocation.isVoid(), is(false));
    }

    @Test
    public void testPrintMethodReturnType() {
        assertThat(invocation.printMethodReturnType(), is(notNullValue()));
    }

    @Test
    public void testGetMethodName() {
        assertThat(invocation.getMethodName(), is(notNullValue()));
    }

    @Test
    public void testReturnsPrimitive() {
        assertThat(invocation.returnsPrimitive(), is(false));
    }

    @Test
    public void testGetLocation() {
        assertThat(invocation.getLocation(), is(notNullValue()));
    }

    @Test
    public void testGetArgumentsCount() {
        assertThat(invocation.getArgumentsCount(), is(0));
    }

    @Test
    public void testGetRawArguments() {
        assertThat(invocation.getRawArguments(), is(notNullValue()));
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        assertThat(invocation.callRealMethod(), is(notNullValue()));
    }

    private Invocation newInvocation() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod();
        Object[] args = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();

        return new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);
    }
}