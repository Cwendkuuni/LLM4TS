
import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.realmethod.RealMethod;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.matchers.MatchersPrinter;
import org.mockito.internal.reporting.PrintSettings;
import org.mockito.internal.reporting.PrintingFriendlyInvocation;
import org.mockito.internal.util.MockUtil;
import org.mockito.invocation.InvocationOnMock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InvocationTest {

    private Invocation invocation;

    @Before
    public void setup() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod();
        Object[] args = new Object[0];
        int sequenceNumber = 0;
        RealMethod realMethod = new RealMethod();

        invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);
    }

    @Test
    public void testGetMock() {
        Object mock = invocation.getMock();

        Assert.assertNotNull(mock);
    }

    @Test
    public void testGetMethod() {
        MockitoMethod method = invocation.getMethod();

        Assert.assertNotNull(method);
    }

    @Test
    public void testGetArguments() {
        Object[] arguments = invocation.getArguments();

        Assert.assertNotNull(arguments);
    }

    @Test
    public void testIsVerified() {
        boolean verified = invocation.isVerified();

        Assert.assertFalse(verified);
    }

    @Test
    public void testGetSequenceNumber() {
        Integer sequenceNumber = invocation.getSequenceNumber();

        Assert.assertNotNull(sequenceNumber);
    }

    @Test
    public void testIsVerifiedInOrder() {
        boolean verifiedInOrder = invocation.isVerifiedInOrder();

        Assert.assertFalse(verifiedInOrder);
    }

    @Test
    public void testEquals() {
        Object o = new Object();
        boolean equals = invocation.equals(o);

        Assert.assertFalse(equals);
    }

    @Test
    public void testHashCode() {
        try {
            invocation.hashCode();
        } catch (RuntimeException e) {
            Assert.assertEquals("hashCode() is not implemented", e.getMessage());
        }
    }

    @Test
    public void testToString() {
        String toString = invocation.toString();

        Assert.assertNotNull(toString);
    }

    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = new PrintSettings();
        String toString = invocation.toString(printSettings);

        Assert.assertNotNull(toString);
    }

    @Test
    public void testToStringWithMatchersAndPrintSettings() {
        List<Matcher> matchers = Arrays.asList(new Equals("test"));
        PrintSettings printSettings = new PrintSettings();
        String toString = invocation.toString(matchers, printSettings);

        Assert.assertNotNull(toString);
    }

    @Test
    public void testToStringWithBlockMatchersAndPrintSettings() {
        List<Matcher> matchers = Arrays.asList(new Equals("test"));
        PrintSettings printSettings = new PrintSettings();
        printSettings.setMultiline(true);
        String toString = invocation.toString(matchers, printSettings);

        Assert.assertNotNull(toString);
    }

    @Test
    public void testArgumentsToMatchers() {
        List<Matcher> matchers = invocation.argumentsToMatchers();

        Assert.assertNotNull(matchers);
    }

    @Test
    public void testIsToString() {
        InvocationOnMock invocationOnMock = new InvocationOnMock();

        boolean isToString = Invocation.isToString(invocationOnMock);

        Assert.assertFalse(isToString);
    }

    @Test
    public void testIsValidException() {
        Throwable throwable = new Throwable();

        boolean isValidException = invocation.isValidException(throwable);

        Assert.assertFalse(isValidException);
    }

    @Test
    public void testIsValidReturnType() {
        Class clazz = Object.class;

        boolean isValidReturnType = invocation.isValidReturnType(clazz);

        Assert.assertTrue(isValidReturnType);
    }

    @Test
    public void testIsVoid() {
        boolean isVoid = invocation.isVoid();

        Assert.assertFalse(isVoid);
    }

    @Test
    public void testPrintMethodReturnType() {
        String methodReturnType = invocation.printMethodReturnType();

        Assert.assertNotNull(methodReturnType);
    }

    @Test
    public void testGetMethodName() {
        String methodName = invocation.getMethodName();

        Assert.assertNotNull(methodName);
    }

    @Test
    public void testReturnsPrimitive() {
        boolean returnsPrimitive = invocation.returnsPrimitive();

        Assert.assertFalse(returnsPrimitive);
    }

    @Test
    public void testGetLocation() {
        Location location = invocation.getLocation();

        Assert.assertNotNull(location);
    }

    @Test
    public void testGetArgumentsCount() {
        int argumentsCount = invocation.getArgumentsCount();

        Assert.assertEquals(0, argumentsCount);
    }

    @Test
    public void testGetRawArguments() {
        Object[] rawArguments = invocation.getRawArguments();

        Assert.assertNotNull(rawArguments);
    }

    @Test
    public void testCallRealMethod() {
        try {
            invocation.callRealMethod();
        } catch (Throwable throwable) {
            Assert.fail("Exception thrown: " + throwable.getMessage());
        }
    }

    @Test
    public void testMarkVerified() {
        invocation.markVerified();

        Assert.assertTrue(invocation.isVerified());
    }

    @Test
    public void testMarkVerifiedInOrder() {
        invocation.markVerifiedInOrder();

        Assert.assertTrue(invocation.isVerified());
        Assert.assertTrue(invocation.isVerifiedInOrder());
    }
}
