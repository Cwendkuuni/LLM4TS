import org.junit.*;
import org.mockito.*;
import org.mockito.internal.invocation.*;
import org.mockito.internal.invocation.realmethod.RealMethod;
import org.mockito.internal.matchers.*;
import org.mockito.internal.util.*;
import org.mockito.invocation.InvocationOnMock;
import static org.junit.Assert.*;

public class InvocationTest {

    @Test
    public void testGetMock() {
        // Create a mock object
        Object mock = Mockito.mock(Object.class);
        // Create an instance of Invocation
        Invocation invocation = new Invocation(mock, null, null, 0, null);
        // Verify that getMock returns the same mock object
        assertSame(mock, invocation.getMock());
    }

    @Test
    public void testGetMethod() {
        // Create a mock object
        MockitoMethod mockitoMethod = Mockito.mock(MockitoMethod.class);
        // Create an instance of Invocation
        Invocation invocation = new Invocation(null, mockitoMethod, null, 0, null);
        // Verify that getMethod returns the same MockitoMethod
        assertSame(mockitoMethod, invocation.getMethod());
    }

    @Test
    public void testGetArguments() {
        // Create an array of arguments
        Object[] args = new Object[] { "arg1", 2, null };
        // Create an instance of Invocation
        Invocation invocation = new Invocation(null, null, args, 0, null);
        // Verify that getArguments returns the same array of arguments
        assertSame(args, invocation.getArguments());
    }

    @Test
    public void testIsVerified() {
        // Create an instance of Invocation
        Invocation invocation = new Invocation(null, null, null, 0, null);
        // Verify that isVerified returns false initially
        assertFalse(invocation.isVerified());
        // Mark the invocation as verified
        invocation.markVerified();
        // Verify that isVerified returns true after marking as verified
        assertTrue(invocation.isVerified());
    }

    @Test
    public void testGetSequenceNumber() {
        // Create an instance of Invocation with sequence number 5
        Invocation invocation = new Invocation(null, null, null, 5, null);
        // Verify that getSequenceNumber returns 5
        assertEquals(5, (int) invocation.getSequenceNumber());
    }

    @Test
    public void testIsVerifiedInOrder() {
        // Create an instance of Invocation
        Invocation invocation = new Invocation(null, null, null, 0, null);
        // Verify that isVerifiedInOrder returns false initially
        assertFalse(invocation.isVerifiedInOrder());
        // Mark the invocation as verified in order
        invocation.markVerifiedInOrder();
        // Verify that isVerifiedInOrder returns true after marking as verified in order
        assertTrue(invocation.isVerifiedInOrder());
    }

    @Test
    public void testEquals() {
        // Create a mock object
        Object mock1 = Mockito.mock(Object.class);
        Object mock2 = Mockito.mock(Object.class);
        // Create two instances of MethodInvocation with the same mock and method but different arguments
        Invocation invocation1 = new Invocation(mock1, null, new Object[] { "arg1" }, 0, null);
        Invocation invocation2 = new Invocation(mock1, null, new Object[] { "arg2" }, 0, null);
        // Verify that invocation1 is equal to itself
        assertEquals(invocation1, invocation1);
        // Verify that invocation1 is equal to invocation2
        assertEquals(invocation1, invocation2);
        // Verify that invocation1 is not equal to null
        assertNotEquals(invocation1, null);
        // Verify that invocation1 is not equal to a different mock
        Invocation invocation3 = new Invocation(mock2, null, new Object[] { "arg1" }, 0, null);
        assertNotEquals(invocation1, invocation3);
    }

    @Test
    public void testHashCode() {
        // Create an instance of Invocation
        Invocation invocation = new Invocation(null, null, null, 0, null);
        // Verify that hashCode throws a RuntimeException
        assertThrows(RuntimeException.class, invocation::hashCode);
    }

    @Test
    public void testToString() {
        // Create an instance of Invocation with mock name "mock1" and method name "method1"
        Object mock = Mockito.mock(Object.class);
        Mockito.when(new MockUtil().getMockName(mock)).thenReturn("mock1");
        MockitoMethod method = Mockito.mock(MockitoMethod.class);
        Mockito.when(method.getName()).thenReturn("method1");
        Invocation invocation = new Invocation(mock, method, null, 0, null);
        // Verify that toString returns "mock1.method1"
        assertEquals("mock1.method1", invocation.toString());
    }

    @Test
    public void testToStringWithMatchers() {
        // Create an instance of Invocation with mock name "mock1" and method name "method1" and arguments ["arg1", 2, null]
        Object mock = Mockito.mock(Object.class);
        Mockito.when(new MockUtil().getMockName(mock)).thenReturn("mock1");
        MockitoMethod method = Mockito.mock(MockitoMethod.class);
        Mockito.when(method.getName()).thenReturn("method1");
        Invocation invocation = new Invocation(mock, method, new Object[] { "arg1", 2, null }, 0, null);
        // Verify that toString returns "mock1.method1(arg1, 2, null)"
        assertEquals("mock1.method1(arg1, 2, null)", invocation.toString());
    }

    @Test
    public void testIsToString() {
        // Create a mock object
        MockitoMethod method = Mockito.mock(MockitoMethod.class);
        Mockito.when(new ObjectMethodsGuru().isToString(method)).thenReturn(true);
        // Create an instance of Invocation with the mockitoMethod that returns true for isToString
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);
        // Verify that isToString returns true for the Invocation
        assertTrue(Invocation.isToString(invocation));
    }

    @Test
    public void testIsValidException() {
        // Create a mock object
        MockitoMethod method = Mockito.mock(MockitoMethod.class);
        // Create an instance of Invocation with the mockitoMethod
        Invocation invocation = new Invocation(null, method, null, 0, null);
        // Create a throwable object of type Exception
        Exception exception = new Exception();
        // Verify that isValidException returns true for the Exception
        assertTrue(invocation.isValidException(exception));
    }

    @Test
    public void testIsValidReturnType() {
        // Create a mock object
        MockitoMethod method = Mockito.mock(MockitoMethod.class);
        // Create an instance of Invocation with the mockitoMethod
        Invocation invocation = new Invocation(null, method, null, 0, null);
        // Verify that isValidReturnType returns true for the class String (assuming the return type of the method is String)
        assertTrue(invocation.isValidReturnType(String.class));
    }

    @Test
    public void testIsVoid() {
        // Create a mock object
        MockitoMethod method = Mockito.mock(MockitoMethod.class);
        Mockito.when(method.getReturnType()).thenReturn(Void.TYPE);
        // Create an instance of Invocation with the mockitoMethod that returns Void.TYPE for getReturnType
        Invocation invocation = new Invocation(null, method, null, 0, null);
        // Verify that isVoid returns true for the Invocation
        assertTrue(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        // Create a mock object
        MockitoMethod method = Mockito.mock(MockitoMethod.class);
        Mockito.when(method.getReturnType()).thenReturn(String.class);
        // Create an instance of Invocation with the mockitoMethod that returns String.class for getReturnType
        Invocation invocation = new Invocation(null, method, null, 0, null);
        // Verify that printMethodReturnType returns "String"
        assertEquals("String", invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        // Create a mock object
        MockitoMethod method = Mockito.mock(MockitoMethod.class);
        Mockito.when(method.getName()).thenReturn("method1");
        // Create an instance of Invocation with the mockitoMethod that returns "method1" for getName
        Invocation invocation = new Invocation(null, method, null, 0, null);
        // Verify that getMethodName returns "method1"
        assertEquals("method1", invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        // Create a mock object
        MockitoMethod method = Mockito.mock(MockitoMethod.class);
        Mockito.when(method.getReturnType()).thenReturn(int.class);
        // Create an instance of Invocation with the mockitoMethod that returns int.class for getReturnType
        Invocation invocation = new Invocation(null, method, null, 0, null);
        // Verify that returnsPrimitive returns true for the Invocation
        assertTrue(invocation.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        // Create an instance of Invocation
        Invocation invocation = new Invocation(null, null, null, 0, null);
        // Verify that getLocation returns a non-null Location object
        assertNotNull(invocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        // Create an instance of Invocation with 3 arguments
        Invocation invocation = new Invocation(null, null, new Object[] { "arg1", 2, null }, 0, null);
        // Verify that getArgumentsCount returns 3
        assertEquals(3, invocation.getArgumentsCount());
    }

    @Test
    public void testGetRawArguments() {
        // Create an array of arguments
        Object[] args = new Object[] { "arg1", 2, null };
        // Create an instance of Invocation
        Invocation invocation = new Invocation(null, null, args, 0, null);
        // Verify that getRawArguments returns the same array of arguments
        assertSame(args, invocation.getRawArguments());
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        // Create a mock object
        Object mock = Mockito.mock(Object.class);
        // Create a RealMethod object
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        // Create an instance of Invocation
        Invocation invocation = new Invocation(mock, null, null, 0, realMethod);
        // Create an array of raw arguments
        Object[] rawArguments = new Object[] { "arg1", 2, null };
        Mockito.when(realMethod.invoke(mock, rawArguments)).thenReturn("result");
        // Verify that callRealMethod returns "result"
        assertEquals("result", invocation.callRealMethod());
    }
}