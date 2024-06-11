import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.exceptions.PrintableInvocation;
import org.mockito.internal.debugging.Location;
import org.mockito.internal.invocation.realmethod.RealMethod;
import org.mockito.invocation.InvocationOnMock;

public class InvocationTest {

    private Invocation invocation;
    private Object mock;
    private MockitoMethod method;
    private Object[] arguments;
    private int sequenceNumber;
    private RealMethod realMethod;

    @Before
    public void setUp() {
        mock = mock(Object.class);
        method = mock(MockitoMethod.class);
        arguments = new Object[] { "arg1", "arg2" };
        sequenceNumber = 1;
        realMethod = mock(RealMethod.class);
        invocation = new Invocation(mock, method, arguments, sequenceNumber, realMethod);
    }

    @Test
    public void testGetMock() {
        assertEquals(mock, invocation.getMock());
    }

    @Test
    public void testGetMethod() {
        assertEquals(method, invocation.getMethod());
    }

    @Test
    public void testGetArguments() {
        assertArrayEquals(arguments, invocation.getArguments());
    }

    @Test
    public void testIsVerifiedFalse() {
        assertFalse(invocation.isVerified());
    }

    @Test
    public void testIsVerifiedInOrderFalse() {
        assertFalse(invocation.isVerifiedInOrder());
    }

    @Test
    public void testGetSequenceNumber() {
        assertEquals(Integer.valueOf(sequenceNumber), invocation.getSequenceNumber());
    }

    @Test
    public void testEqualsSameObject() {
        assertTrue(invocation.equals(invocation));
    }

    @Test
    public void testEqualsNullObject() {
        assertFalse(invocation.equals(null));
    }

    @Test
    public void testEqualsDifferentClass() {
        assertFalse(invocation.equals("test"));
    }

    // additional test cases for equals()
    
    @Test
    public void testEqualsDifferentMock() {
        Invocation other = new Invocation(mock(Object.class), method, arguments, sequenceNumber, realMethod);
        assertFalse(invocation.equals(other));
    }
    
    @Test
    public void testEqualsDifferentMethod() {
        MockitoMethod otherMethod = mock(MockitoMethod.class);
        when(otherMethod.getName()).thenReturn("otherMethod");
        Invocation other = new Invocation(mock, otherMethod, arguments, sequenceNumber, realMethod);
        assertFalse(invocation.equals(other));
    }
    
    @Test
    public void testEqualsDifferentArguments() {
        Object[] otherArgs = new Object[] { "arg1", "arg3" };
        Invocation other = new Invocation(mock, method, otherArgs, sequenceNumber, realMethod);
        assertFalse(invocation.equals(other));
    }
    
    @Test
    public void testHashCode() {
        assertThrows(RuntimeException.class, () -> {
            invocation.hashCode();
        });
    }

    @Test
    public void testToStringWithoutMatchers() {
        String expected = "org.mockito.internal.invocation.Invocation@";
        expected += Integer.toHexString(invocation.hashCode());
        assertEquals(expected, invocation.toString());
    }

    // additional test case for toString()
    // to be added if we add Matchers to the arguments
    
    @Test
    public void testIsToStringTrue() {
        MockitoMethod toStringMethod = mock(MockitoMethod.class);
        when(toStringMethod.getName()).thenReturn("toString");
        InvocationOnMock invocationMock = mock(InvocationOnMock.class);
        when(invocationMock.getMethod()).thenReturn(toStringMethod);
        assertTrue(Invocation.isToString(invocationMock));
    }

    // additional test cases for isValidException()
    
    @Test
    public void testIsValidExceptionTrue() {
        Throwable exception = new IllegalArgumentException();
        when(method.getExceptionTypes()).thenReturn(new Class[]{IllegalArgumentException.class});
        assertTrue(invocation.isValidException(exception));
    }
    
    @Test
    public void testIsValidExceptionFalse() {
        Throwable exception = new NullPointerException();
        when(method.getExceptionTypes()).thenReturn(new Class[]{IllegalArgumentException.class});
        assertFalse(invocation.isValidException(exception));
    }
    
    // additional test cases for isValidReturnType()
    
    @Test
    public void testIsValidReturnTypePrimitiveTrue() {
        when(method.getReturnType()).thenReturn(int.class);
        assertTrue(invocation.isValidReturnType(int.class));
    }
    
    @Test
    public void testIsValidReturnTypePrimitiveFalse() {
        when(method.getReturnType()).thenReturn(int.class);
        assertFalse(invocation.isValidReturnType(String.class));
    }
    
    @Test
    public void testIsValidReturnTypeNonPrimitiveTrue() {
        when(method.getReturnType()).thenReturn(String.class);
        assertTrue(invocation.isValidReturnType(String.class));
    }
    
    @Test
    public void testIsValidReturnTypeNonPrimitiveFalse() {
        when(method.getReturnType()).thenReturn(String.class);
        assertFalse(invocation.isValidReturnType(Integer.class));
    }

    @Test
    public void testIsVoidTrue() {
        when(method.getReturnType()).thenReturn(void.class);
        assertTrue(invocation.isVoid());
    }

    @Test
    public void testIsVoidFalse() {
        when(method.getReturnType()).thenReturn(int.class);
        assertFalse(invocation.isVoid());
    }

    // additional test cases for printMethodReturnType()
    
    @Test
    public void testPrintMethodReturnType() {
        when(method.getReturnType()).thenReturn(String.class);
        assertEquals("String", invocation.printMethodReturnType());
    }
    
    // additional test cases for getMethodName()
    
    @Test
    public void testGetMethodName() {
        when(method.getName()).thenReturn("testMethod");
        assertEquals("testMethod", invocation.getMethodName());
    }
    
    // additional test cases for returnsPrimitive()
    
    @Test
    public void testReturnsPrimitiveTrue() {
        when(method.getReturnType()).thenReturn(int.class);
        assertTrue(invocation.returnsPrimitive());
    }
    
    @Test
    public void testReturnsPrimitiveFalse() {
        when(method.getReturnType()).thenReturn(String.class);
        assertFalse(invocation.returnsPrimitive());
    }

    // additional test cases for getLocation()
    
    @Test
    public void testGetLocation() {
        Location expected = new Location();
        assertEquals(expected, invocation.getLocation());
    }
    
    // additional test cases for getArgumentsCount()
    
    @Test
    public void testGetArgumentsCount() {
        assertEquals(arguments.length, invocation.getArgumentsCount());
    }
    
    // additional test cases for getRawArguments()
    
    // to be added if necessary
    
    // additional test cases for callRealMethod()
    
    // to be added if necessary

    // additional test cases for toString(PrintSettings printSettings)
    
    // to be added if we add Matchers to the arguments
    
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
}