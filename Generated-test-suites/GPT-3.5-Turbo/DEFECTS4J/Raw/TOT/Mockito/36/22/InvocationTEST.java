import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.util.MockUtil;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationTest {

    private Invocation invocation;

    @Before
    public void setUp() {
        Object mock = mock(Object.class);
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object[] args = new Object[] { "arg1", "arg2" };
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);
    }

    @Test
    public void testGetMock() {
        Object mock = invocation.getMock();
        assertNotNull(mock);
    }

    @Test
    public void testGetMethod() {
        MockitoMethod method = invocation.getMethod();
        assertNotNull(method);
    }

    @Test
    public void testGetArguments() {
        Object[] arguments = invocation.getArguments();
        assertNotNull(arguments);
        assertEquals(2, arguments.length);
    }

    @Test
    public void testIsVerifiedBeforeVerification() {
        boolean verified = invocation.isVerified();
        assertFalse(verified);
    }

    @Test
    public void testGetSequenceNumber() {
        Integer sequenceNumber = invocation.getSequenceNumber();
        assertNotNull(sequenceNumber);
        assertEquals(Integer.valueOf(1), sequenceNumber);
    }

    @Test
    public void testIsVerifiedInOrderBeforeVerification() {
        boolean verifiedInOrder = invocation.isVerifiedInOrder();
        assertFalse(verifiedInOrder);
    }

    @Test
    public void testEquals() {
        Invocation otherInvocation = new Invocation(mock(Object.class), mock(MockitoMethod.class), new Object[] { "arg1", "arg2" }, 1, mock(RealMethod.class));
        boolean equals = invocation.equals(otherInvocation);
        assertTrue(equals);
    }

    @Test
    public void testHashCode() {
        try {
            invocation.hashCode();
            fail("Expected RuntimeException");
        } catch (RuntimeException e) {
            assertEquals("hashCode() is not implemented", e.getMessage());
        }
    }

    @Test
    public void testToString() {
        String toString = invocation.toString();
        String expectedToString = MockUtil.getMockName(invocation.getMock()) + "." + invocation.getMethod().getName() + new Equals("arg1").toString() + new Equals("arg2").toString();
        assertEquals(expectedToString, toString);
    }

    @Test
    public void testIsValidException() {
        Throwable throwable = new RuntimeException();
        boolean validException = invocation.isValidException(throwable);
        assertFalse(validException);
    }

    @Test
    public void testIsValidReturnType() {
        boolean validReturnType = invocation.isValidReturnType(String.class);
        assertTrue(validReturnType);
    }

    @Test
    public void testIsValidReturnTypePrimitive() {
        boolean validReturnType = invocation.isValidReturnType(int.class);
        assertTrue(validReturnType);
    }

    @Test
    public void testIsVoid() {
        boolean isVoid = invocation.isVoid();
        assertFalse(isVoid);
    }

    @Test
    public void testPrintMethodReturnType() {
        String returnType = invocation.printMethodReturnType();
        assertNotNull(returnType);
    }

    @Test
    public void testGetMethodName() {
        String methodName = invocation.getMethodName();
        assertNotNull(methodName);
    }

    @Test
    public void testReturnsPrimitive() {
        boolean returnsPrimitive = invocation.returnsPrimitive();
        assertFalse(returnsPrimitive);
    }

    @Test
    public void testGetLocation() {
        Location location = invocation.getLocation();
        assertNotNull(location);
    }

    @Test
    public void testGetArgumentsCount() {
        int argumentsCount = invocation.getArgumentsCount();
        assertEquals(2, argumentsCount);
    }

    @Test
    public void testGetRawArguments() {
        Object[] rawArguments = invocation.getRawArguments();
        assertNotNull(rawArguments);
    }

    @Test
    public void testCallRealMethod() {
        try {
            Object result = invocation.callRealMethod();
            assertNotNull(result);
        } catch (Throwable e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testToStringWithPrintSettings() {
        PrintSettings printSettings = new PrintSettings();
        printSettings.setMultiline(true);

        String toString = invocation.toString(printSettings);
        assertNotNull(toString);
    }

    @Test
    public void testMarkVerified() {
        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }

    @Test
    public void testMarkVerifiedInOrder() {
        invocation.markVerifiedInOrder();
        assertTrue(invocation.isVerified());
        assertTrue(invocation.isVerifiedInOrder());
    }

    @Test
    public void testExpandVarArgsWithVarArgsArray() {
        boolean isVarArgs = true;
        Object[] args = new Object[] { "arg1", new String[] { "arg2", "arg3" } };

        Object[] expandedArgs = Invocation.expandVarArgs(isVarArgs, args);
        assertNotNull(expandedArgs);
        assertEquals(3, expandedArgs.length);
    }

    @Test
    public void testExpandVarArgsWithNonVarArgsArray() {
        boolean isVarArgs = false;
        Object[] args = new Object[] { "arg1", new String[] { "arg2", "arg3" } };

        Object[] expandedArgs = Invocation.expandVarArgs(isVarArgs, args);
        assertNotNull(expandedArgs);
        assertEquals(2, expandedArgs.length);
    }

    @Test
    public void testToStringWithLongInvocation() {
        List<Matcher> matchers = Arrays.asList(
                new Equals("ThisIsAReallyLongArgumentThatExceedsTheMaxLengthForALineInToString"),
                new Equals("ThisIsAnotherReallyLongArgumentThatExceedsTheMaxLengthForALineInToString")
        );
        PrintSettings printSettings = new PrintSettings();

        String toString = invocation.toString(matchers, printSettings);
        assertNotNull(toString);
    }

    @Test
    public void testToStringWithEmptyMatchers() {
        List<Matcher> matchers = Arrays.asList();
        PrintSettings printSettings = new PrintSettings();

        String toString = invocation.toString(matchers, printSettings);
        assertNotNull(toString);
    }

}