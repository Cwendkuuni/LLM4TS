import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.invocation.InvocationOnMock;

public class InvocationTest {

    @Test
    public void testGetMock() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[] { "arg1", "arg2" };
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertEquals(mock, invocation.getMock());
    }

    @Test
    public void testGetMethod() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[] { "arg1", "arg2" };
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertEquals(method, invocation.getMethod());
    }

    @Test
    public void testGetArguments() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[] { "arg1", "arg2" };
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertArrayEquals(args, invocation.getArguments());
    }

    @Test
    public void testIsVerified() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[] { "arg1", "arg2" };
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertFalse(invocation.isVerified());

        invocation.markVerified();

        assertTrue(invocation.isVerified());
    }

    @Test
    public void testGetSequenceNumber() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[] { "arg1", "arg2" };
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertEquals(new Integer(sequenceNumber), invocation.getSequenceNumber());
    }

    @Test
    public void testIsVerifiedInOrder() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[] { "arg1", "arg2" };
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertFalse(invocation.isVerifiedInOrder());

        invocation.markVerifiedInOrder();

        assertTrue(invocation.isVerifiedInOrder());
    }

    @Test
    public void testEquals() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[] { "arg1", "arg2" };
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation1 = new Invocation(mock, method, args, sequenceNumber, realMethod);
        Invocation invocation2 = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertTrue(invocation1.equals(invocation2));
    }

    @Test
    public void testHashCode() {
        // TODO: Implement this test according to the class documentation
    }

    @Test
    public void testToString() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[] { "arg1", "arg2" };
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        String expected = "mockObject.methodName()";
        assertEquals(expected, invocation.toString());
    }

    @Test
    public void testToStringWithMatchers() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[] { "arg1", "arg2" };
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        String expected = "mockObject.methodName(arg1, arg2)";
        assertEquals(expected, invocation.toString(new PrintSettings()));
    }

    @Test
    public void testArgumentsToMatchers() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[] { "arg1", "arg2" };
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        List<Matcher> expectedMatchers = Arrays.asList(new Equals("arg1"), new Equals("arg2"));
        assertEquals(expectedMatchers, invocation.argumentsToMatchers());
    }

    @Test
    public void testIsToString() {
        // TODO: Implement this test according to the class documentation
    }

    @Test
    public void testIsValidException() {
        // TODO: Implement this test according to the class documentation
    }

    @Test
    public void testIsValidReturnType() {
        // TODO: Implement this test according to the class documentation
    }

    @Test
    public void testIsVoid() {
        // TODO: Implement this test according to the class documentation
    }

    @Test
    public void testPrintMethodReturnType() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[] { "arg1", "arg2" };
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertEquals("ReturnType", invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[] { "arg1", "arg2" };
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertEquals("methodName", invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[] { "arg1", "arg2" };
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertTrue(invocation.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        // TODO: Implement this test according to the class documentation
    }

    @Test
    public void testGetArgumentsCount() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[] { "arg1", "arg2" };
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertEquals(args.length, invocation.getArgumentsCount());
    }

    @Test
    public void testGetRawArguments() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[] { "arg1", "arg2" };
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertArrayEquals(args, invocation.getRawArguments());
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        // TODO: Implement this test according to the class documentation
    }

    @Test
    public void testToStringWithPrintSettings() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[] { "arg1", "arg2" };
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
        PrintSettings printSettings = mock(PrintSettings.class);

        String expected = "mockObject.methodName(arg1, arg2)";
        assertEquals(expected, invocation.toString(printSettings));
    }

    @Test
    public void testMarkVerified() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[] { "arg1", "arg2" };
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertFalse(invocation.isVerified());

        invocation.markVerified();

        assertTrue(invocation.isVerified());
    }

    @Test
    public void testMarkVerifiedInOrder() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[] { "arg1", "arg2" };
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertFalse(invocation.isVerified());
        assertFalse(invocation.isVerifiedInOrder());

        invocation.markVerifiedInOrder();

        assertTrue(invocation.isVerified());
        assertTrue(invocation.isVerifiedInOrder());
    }
}