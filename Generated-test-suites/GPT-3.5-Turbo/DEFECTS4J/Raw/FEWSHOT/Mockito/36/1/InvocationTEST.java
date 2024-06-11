package org.mockito.internal.invocation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.realmethod.RealMethod;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.Equals;
import org.mockito.internal.util.ArrayEquals;
import org.mockito.internal.util.Equals;
import org.mockito.invocation.InvocationOnMock;

@SuppressWarnings("unchecked")
public class InvocationTest {

    private Invocation invocation;

    @Before
    public void setUp() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[] { "arg1", "arg2" };
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();

        invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
    }

    @Test
    public void testGetMock() {
        Object mock = invocation.getMock();
        assertEquals(mock, new Object());
    }

    @Test
    public void testGetMethod() {
        MockitoMethod method = invocation.getMethod();
        assertEquals(method, new MockitoMethod());
    }

    @Test
    public void testGetArguments() {
        Object[] arguments = invocation.getArguments();
        assertEquals(arguments.length, 2);
        assertEquals(arguments[0], "arg1");
        assertEquals(arguments[1], "arg2");
    }

    @Test
    public void testIsVerified() {
        assertFalse(invocation.isVerified());
    }

    @Test
    public void testGetSequenceNumber() {
        Integer sequenceNumber = invocation.getSequenceNumber();
        assertEquals(sequenceNumber, (Integer) 1);
    }

    @Test
    public void testIsVerifiedInOrder() {
        assertFalse(invocation.isVerifiedInOrder());
    }

    @Test
    public void testEquals() {
        // Arrange
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[] { "arg1", "arg2" };
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();

        Invocation other = new Invocation(mock, method, args, sequenceNumber, realMethod);

        // Act
        boolean result = invocation.equals(other);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testHashCode() {
        // Arrange
        boolean exceptionThrown = false;

        // Act
        try {
            invocation.hashCode();
        } catch (RuntimeException e) {
            exceptionThrown = true;
        }

        // Assert
        assertTrue(exceptionThrown);
    }

    @Test
    public void testToString() {
        // Arrange
        List<Matcher> matchers = invocation.argumentsToMatchers();

        // Act
        String result = invocation.toString(matchers, new PrintSettings());

        // Assert
        assertEquals(result, "MockitoMethod.arg1, arg2");
    }

    @Test
    public void testToStringWithMultilinePrintSetting() {
        // Arrange
        PrintSettings printSettings = new PrintSettings();
        printSettings.setMultiline(true);

        // Act
        String result = invocation.toString(printSettings);

        // Assert
        assertEquals(result, "MockitoMethod.arg1,\narg2");
    }

    @Test
    public void testToStringWithLongInvocation() {
        // Arrange
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[] { "arg1", "arg2", "arg3", "arg4", "arg5", "arg6" };

        Invocation invocation = new Invocation(new Object(), method, args, 1, new RealMethod());

        // Act
        String result = invocation.toString();

        // Assert
        assertEquals(result, "MockitoMethod.arg1, arg2, arg3,\narg4, arg5, arg6");
    }

    @Test
    public void testIsToString() {
        // Arrange
        MockitoMethod method = new MockitoMethod();
        InvocationOnMock invocation = new InvocationOnMock();

        invocation.setMethod(method);

        // Act
        boolean result = Invocation.isToString(invocation);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsValidExceptionWithValidException() {
        // Arrange
        Throwable throwable = new Throwable();
        Throwable throwable2 = new Throwable();
        MockitoMethod method = new MockitoMethod();
        method.setExceptionTypes(new Class[] { Throwable.class });

        Invocation invocation = new Invocation(new Object(), method, new Object[0], 1, new RealMethod());

        // Act
        boolean result = invocation.isValidException(throwable);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsValidExceptionWithInvalidException() {
        // Arrange
        Throwable throwable = new Throwable();
        MockitoMethod method = new MockitoMethod();
        method.setExceptionTypes(new Class[] { IOException.class });

        Invocation invocation = new Invocation(new Object(), method, new Object[0], 1, new RealMethod());

        // Act
        boolean result = invocation.isValidException(throwable);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsValidReturnTypeWithValidReturnType() {
        // Arrange
        Class clazz = String.class;
        MockitoMethod method = new MockitoMethod();
        method.setReturnType(String.class);

        Invocation invocation = new Invocation(new Object(), method, new Object[0], 1, new RealMethod());

        // Act
        boolean result = invocation.isValidReturnType(clazz);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsValidReturnTypeWithInvalidReturnType() {
        // Arrange
        Class clazz = Integer.class;
        MockitoMethod method = new MockitoMethod();
        method.setReturnType(String.class);

        Invocation invocation = new Invocation(new Object(), method, new Object[0], 1, new RealMethod());

        // Act
        boolean result = invocation.isValidReturnType(clazz);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsVoidWithVoidReturnType() {
        // Arrange
        MockitoMethod method = new MockitoMethod();
        method.setReturnType(Void.TYPE);

        Invocation invocation = new Invocation(new Object(), method, new Object[0], 1, new RealMethod());

        // Act
        boolean result = invocation.isVoid();

        // Assert
        assertTrue(result);
    }

    @Test
    public void testPrintMethodReturnType() {
        // Arrange
        MockitoMethod method = new MockitoMethod();
        method.setReturnType(String.class);

        Invocation invocation = new Invocation(new Object(), method, new Object[0], 1, new RealMethod());

        // Act
        String result = invocation.printMethodReturnType();

        // Assert
        assertEquals(result, "String");
    }

    @Test
    public void testGetMethodName() {
        // Arrange
        MockitoMethod method = new MockitoMethod();
        method.setName("methodName");

        Invocation invocation = new Invocation(new Object(), method, new Object[0], 1, new RealMethod());

        // Act
        String result = invocation.getMethodName();

        // Assert
        assertEquals(result, "methodName");
    }

    @Test
    public void testReturnsPrimitiveWithPrimitiveReturnType() {
        // Arrange
        MockitoMethod method = new MockitoMethod();
        method.setReturnType(Integer.TYPE);

        Invocation invocation = new Invocation(new Object(), method, new Object[0], 1, new RealMethod());

        // Act
        boolean result = invocation.returnsPrimitive();

        // Assert
        assertTrue(result);
    }

    @Test
    public void testReturnsPrimitiveWithNonPrimitiveReturnType() {
        // Arrange
        MockitoMethod method = new MockitoMethod();
        method.setReturnType(String.class);

        Invocation invocation = new Invocation(new Object(), method, new Object[0], 1, new RealMethod());

        // Act
        boolean result = invocation.returnsPrimitive();

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetLocation() {
        // Arrange
        Location location = new Location();

        // Act
        invocationmarkVerified();
        Location result = invocation.getLocation();

        // Assert
        assertEquals(result, location);
    }

    @Test
    public void testGetArgumentsCount() {
        // Act
        int result = invocation.getArgumentsCount();

        // Assert
        assertEquals(result, 2);
    }

    @Test
    public void testGetRawArguments() {
        // Act
        Object[] result = invocation.getRawArguments();

        // Assert
        assertEquals(result.length, 2);
        assertEquals(result[0], "arg1");
        assertEquals(result[1], "arg2");
    }

    @Test
    public void testCallRealMethod() {
        // Arrange
        Object expectedResult = new Object();

        RealMethod realMethod = new RealMethod();
        realMethod.setReturnValue(expectedResult);

        Invocation invocation = new Invocation(new Object(), new MockitoMethod(), new Object[0], 1, realMethod);

        // Act
        Object result = invocation.callRealMethod();

        // Assert
        assertEquals(result, expectedResult);
    }

    @Test
    public void testToStringWithPrintSettings() {
        // Arrange
        PrintSettings printSettings = new PrintSettings();

        // Act
        String result = invocation.toString(printSettings);

        // Assert
        assertEquals(result, "MockitoMethod.arg1, arg2");
    }

    @Test
    public void testMarkVerified() {
        // Act
        invocation.markVerified();
        boolean result = invocation.isVerified();

        // Assert
        assertTrue(result);
    }

    @Test
    public void testMarkVerifiedInOrder() {
        // Act
        invocation.markVerifiedInOrder();
        boolean result = invocation.isVerifiedInOrder();

        // Assert
        assertTrue(result);
    }
}