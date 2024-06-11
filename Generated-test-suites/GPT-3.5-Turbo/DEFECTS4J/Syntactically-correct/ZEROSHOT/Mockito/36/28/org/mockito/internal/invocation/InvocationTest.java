import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.invocation.InvocationOnMock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class InvocationTest {

    @Test
    public void testGetMock() {
        // Arrange
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = {};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        // Act
        Object result = invocation.getMock();

        // Assert
        assertSame(mock, result);
    }

    @Test
    public void testGetMethod() {
        // Arrange
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = {};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        // Act
        MockitoMethod result = invocation.getMethod();

        // Assert
        assertSame(method, result);
    }

    @Test
    public void testGetArguments() {
        // Arrange
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = {1, "test"};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        // Act
        Object[] result = invocation.getArguments();

        // Assert
        assertArrayEquals(args, result);
    }

    @Test
    public void testIsVerified_false() {
        // Arrange
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = {};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        // Act
        boolean result = invocation.isVerified();

        // Assert
        assertFalse(result);
    }

    @Test
    public void testGetSequenceNumber() {
        // Arrange
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = {};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        // Act
        Integer result = invocation.getSequenceNumber();

        // Assert
        assertEquals(Integer.valueOf(sequenceNumber), result);
    }

    @Test
    public void testIsVerifiedInOrder_false() {
        // Arrange
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = {};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        // Act
        boolean result = invocation.isVerifiedInOrder();

        // Assert
        assertFalse(result);
    }

    @Test
    public void testEquals_true() {
        // Arrange
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args1 = {1, "test"};
        Object[] args2 = {1, "test"};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();
        Invocation invocation1 = new Invocation(mock, method, args1, sequenceNumber, realMethod);
        Invocation invocation2 = new Invocation(mock, method, args2, sequenceNumber, realMethod);

        // Act
        boolean result = invocation1.equals(invocation2);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testEquals_false() {
        // Arrange
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args1 = {1, "test"};
        Object[] args2 = {1, "different"};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();
        Invocation invocation1 = new Invocation(mock, method, args1, sequenceNumber, realMethod);
        Invocation invocation2 = new Invocation(mock, method, args2, sequenceNumber, realMethod);

        // Act
        boolean result = invocation1.equals(invocation2);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHashCode() {
        // Arrange
        Invocation invocation = new Invocation();

        try {
            // Act
            invocation.hashCode();
            fail("Expected RuntimeException to be thrown");
        } catch (RuntimeException e) {
            // Assert
            assertEquals("hashCode() is not implemented", e.getMessage());
        }
    }

    @Test
    public void testToString() {
        // Arrange
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = {1, "test"};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        // Act
        String result = invocation.toString();

        // Assert
        assertEquals("mock.toString(1, \"test\")", result);
    }

    @Test
    public void testToString_multiline() {
        // Arrange
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = {1, "test"};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        // Act
        String result = invocation.toString();

        // Assert
        assertEquals("mock.toString(\n" +
                "  1,\n" +
                "  \"test\"\n" +
                ")", result);
    }

    @Test
    public void testIsToString_true() {
        // Arrange
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod("toString");
        Object[] args = {};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        // Act
        boolean result = Invocation.isToString(invocation);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsToString_false() {
        // Arrange
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod("otherMethod");
        Object[] args = {};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        // Act
        boolean result = Invocation.isToString(invocation);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsValidException_true() {
        // Arrange
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = {};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
        Throwable throwable = new Exception();

        // Act
        boolean result = invocation.isValidException(throwable);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsValidException_false() {
        // Arrange
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = {};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
        Throwable throwable = new IllegalArgumentException();

        // Act
        boolean result = invocation.isValidException(throwable);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsValidReturnType_true() {
        // Arrange
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = {};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
        Class clazz = Object.class;

        // Act
        boolean result = invocation.isValidReturnType(clazz);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsValidReturnType_false() {
        // Arrange
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = {};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
        Class clazz = Integer.class;

        // Act
        boolean result = invocation.isValidReturnType(clazz);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testIsVoid_true() {
        // Arrange
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod("voidMethod");
        Object[] args = {};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        // Act
        boolean result = invocation.isVoid();

        // Assert
        assertTrue(result);
    }

    @Test
    public void testIsVoid_false() {
        // Arrange
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod("nonVoidMethod");
        Object[] args = {};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        // Act
        boolean result = invocation.isVoid();

        // Assert
        assertFalse(result);
    }

    @Test
    public void testPrintMethodReturnType() {
        // Arrange
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod("intMethod");
        Object[] args = {};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        // Act
        String result = invocation.printMethodReturnType();

        // Assert
        assertEquals("int", result);
    }

    @Test
    public void testGetMethodName() {
        // Arrange
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod("testMethod");
        Object[] args = {};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        // Act
        String result = invocation.getMethodName();

        // Assert
        assertEquals("testMethod", result);
    }

    @Test
    public void testReturnsPrimitive_true() {
        // Arrange
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod("intMethod");
        Object[] args = {};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        // Act
        boolean result = invocation.returnsPrimitive();

        // Assert
        assertTrue(result);
    }

    @Test
    public void testReturnsPrimitive_false() {
        // Arrange
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod("nonPrimitiveMethod");
        Object[] args = {};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        // Act
        boolean result = invocation.returnsPrimitive();

        // Assert
        assertFalse(result);
    }

}