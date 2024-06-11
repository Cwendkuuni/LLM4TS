import static org.mockito.Mockito.*;

import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.exceptions.stacktrace.StackTraceFilter;
import org.mockito.invocation.InvocationOnMock;

import java.util.Arrays;
import java.util.List;

public class InvocationTest {
    private Invocation invocation;

    @Before
    public void setUp() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = {"test"};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);
        invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
    }

    @Test
    public void testGetMock() {
        Object expected = mock(Object.class);

        Object result = invocation.getMock();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetMethod() {
        MockitoMethod expected = mock(MockitoMethod.class);

        MockitoMethod result = invocation.getMethod();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetArguments() {
        Object[] expected = {"test"};

        Object[] result = invocation.getArguments();

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testIsVerified() {
        boolean expected = false;

        boolean result = invocation.isVerified();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetSequenceNumber() {
        Integer expected = 1;

        Integer result = invocation.getSequenceNumber();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsVerifiedInOrder() {
        boolean expected = false;

        boolean result = invocation.isVerifiedInOrder();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testEquals() {
        Invocation other = new Invocation(invocation.getMock(), invocation.getMethod(), invocation.getArguments(), invocation.getSequenceNumber(), invocation.realMethod);
        boolean expected = true;

        boolean result = invocation.equals(other);

        Assert.assertEquals(expected, result);
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
        String expected = "Mock for Object, hashCode: " + invocation.getMock().hashCode() + ".getName()";

        String result = invocation.toString();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = mock(PrintSettings.class);
        String expected = "Mock for Object, hashCode: " + invocation.getMock().hashCode() + ".getName()";

        String result = invocation.toString(printSettings);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testArgumentsToMatchers() {
        List<Matcher> expected = Arrays.asList(new Equals("test"));

        List<Matcher> result = invocation.argumentsToMatchers();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsToString() {
        InvocationOnMock invocationOnMock = mock(InvocationOnMock.class);
        MockitoMethod method = mock(MockitoMethod.class);
        when(invocationOnMock.getMethod()).thenReturn(method);

        boolean result = Invocation.isToString(invocationOnMock);

        Assert.assertEquals(false, result);
    }

    @Test
    public void testIsValidException() {
        Throwable throwable = new Exception();
        boolean expected = false;

        boolean result = invocation.isValidException(throwable);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsValidReturnType() {
        Class clazz = Object.class;
        boolean expected = true;

        boolean result = invocation.isValidReturnType(clazz);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsVoid() {
        boolean expected = false;

        boolean result = invocation.isVoid();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testPrintMethodReturnType() {
        String expected = "Object";

        String result = invocation.printMethodReturnType();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetMethodName() {
        String expected = "getName";

        String result = invocation.getMethodName();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testReturnsPrimitive() {
        boolean expected = false;

        boolean result = invocation.returnsPrimitive();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetLocation() {
        Location expected = new Location();

        Location result = invocation.getLocation();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetArgumentsCount() {
        int expected = 1;

        int result = invocation.getArgumentsCount();

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetRawArguments() {
        Object[] expected = {"test"};

        Object[] result = invocation.getRawArguments();

        Assert.assertArrayEquals(expected, result);
    }

    @Test
    public void testCallRealMethod() {
        try {
            invocation.callRealMethod();
        } catch (Throwable e) {
            Assert.fail();
        }
    }

    @Test
    public void testMarkVerified() {
        invocation.markVerified();

        boolean result = invocation.isVerified();

        Assert.assertTrue(result);
    }

    @Test
    public void testMarkVerifiedInOrder() {
        invocation.markVerifiedInOrder();

        boolean result = invocation.isVerifiedInOrder();

        Assert.assertTrue(result);
    }
}