import org.junit.Test;
import org.mockito.internal.exceptions.PrintableInvocation;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.MockitoMethod;
import org.mockito.internal.invocation.realmethod.RealMethod;
import org.mockito.internal.util.ArrayEquals;
import org.mockito.invocation.InvocationOnMock;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationTest {

    // Test getMock()
    @Test
    public void testGetMock() {
        Object mock = new Object();
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[]{};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertSame(mock, invocation.getMock());
    }

    // Test getMethod()
    @Test
    public void testGetMethod() {
        Object mock = new Object();
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[]{};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertSame(method, invocation.getMethod());
    }

    // Test getArguments()
    @Test
    public void testGetArguments() {
        Object mock = new Object();
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[]{"arg1", "arg2"};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertArrayEquals(args, invocation.getArguments());
    }

    // Test isVerified()
    @Test
    public void testIsVerified() {
        Object mock = new Object();
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[]{};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertFalse(invocation.isVerified());
    }

    // Test getSequenceNumber()
    @Test
    public void testGetSequenceNumber() {
        Object mock = new Object();
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[]{};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertEquals(Integer.valueOf(sequenceNumber), invocation.getSequenceNumber());
    }

    // Test isVerifiedInOrder()
    @Test
    public void testIsVerifiedInOrder() {
        Object mock = new Object();
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[]{};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertFalse(invocation.isVerifiedInOrder());
    }

    // Test equals()
    @Test
    public void testEquals() {
        Object mock = new Object();
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args1 = new Object[]{"arg1", "arg2"};
        Object[] args2 = new Object[]{"arg1", "arg2"};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation1 = new Invocation(mock, method, args1, sequenceNumber, realMethod);
        Invocation invocation2 = new Invocation(mock, method, args2, sequenceNumber, realMethod);

        assertTrue(invocation1.equals(invocation2));
    }

    // Test hashCode()
    @Test(expected = RuntimeException.class)
    public void testHashCode() {
        Object mock = new Object();
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[]{"arg1", "arg2"};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        invocation.hashCode();
    }

    // Test toString()
    @Test
    public void testToString() {
        Object mock = new Object();
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[]{"arg1", "arg2"};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertEquals(mock.getClass().getName() + "$MockitoMethod." + method.getName() + "[arg1, arg2]", invocation.toString());
    }

    // Test argumentsToMatchers()
    @Test
    public void testArgumentsToMatchers() {
        Object mock = new Object();
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[]{"arg1", "arg2"};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertEquals(Arrays.asList(new ArrayEquals("arg1"), new ArrayEquals("arg2")), invocation.argumentsToMatchers());
    }

    // Test isToString()
    @Test
    public void testIsToString() {
        MockitoMethod method = mock(MockitoMethod.class);
        when(method.isToString()).thenReturn(true);

        InvocationOnMock invocation = mock(InvocationOnMock.class);

        assertTrue(Invocation.isToString(invocation));
    }

    // Test isValidException()
    @Test
    public void testIsValidException() {
        Object mock = new Object();
        MockitoMethod method = mock(MockitoMethod.class);
        when(method.getExceptionTypes()).thenReturn(new Class[]{Exception.class});

        Invocation invocation = new Invocation(mock, method, new Object[]{}, 1, mock(RealMethod.class));

        assertTrue(invocation.isValidException(new Exception()));
    }

    // Test isValidReturnType()
    @Test
    public void testIsValidReturnType() {
        Object mock = new Object();
        MockitoMethod method = mock(MockitoMethod.class);
        when(method.getReturnType()).thenReturn(String.class);

        Invocation invocation = new Invocation(mock, method, new Object[]{}, 1, mock(RealMethod.class));

        assertTrue(invocation.isValidReturnType(String.class));
    }

    // Test isVoid()
    @Test
    public void testIsVoid() {
        Object mock = new Object();
        MockitoMethod method = mock(MockitoMethod.class);
        when(method.getReturnType()).thenReturn(void.class);

        Invocation invocation = new Invocation(mock, method, new Object[]{}, 1, mock(RealMethod.class));

        assertTrue(invocation.isVoid());
    }

    // Test printMethodReturnType()
    @Test
    public void testPrintMethodReturnType() {
        Object mock = new Object();
        MockitoMethod method = mock(MockitoMethod.class);
        when(method.getReturnType()).thenReturn(String.class);

        Invocation invocation = new Invocation(mock, method, new Object[]{}, 1, mock(RealMethod.class));

        assertEquals("String", invocation.printMethodReturnType());
    }

    // Test getMethodName()
    @Test
    public void testGetMethodName() {
        Object mock = new Object();
        MockitoMethod method = mock(MockitoMethod.class);
        when(method.getName()).thenReturn("methodName");

        Invocation invocation = new Invocation(mock, method, new Object[]{}, 1, mock(RealMethod.class));

        assertEquals("methodName", invocation.getMethodName());
    }

    // Test returnsPrimitive()
    @Test
    public void testReturnsPrimitive() {
        Object mock = new Object();
        MockitoMethod method = mock(MockitoMethod.class);
        when(method.getReturnType()).thenReturn(int.class);

        Invocation invocation = new Invocation(mock, method, new Object[]{}, 1, mock(RealMethod.class));

        assertTrue(invocation.returnsPrimitive());
    }

    // Test getLocation()
    @Test
    public void testGetLocation() {
        Object mock = new Object();
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[]{};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertNotNull(invocation.getLocation());
    }

    // Test getArgumentsCount()
    @Test
    public void testGetArgumentsCount() {
        Object mock = new Object();
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[]{"arg1", "arg2"};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertEquals(args.length, invocation.getArgumentsCount());
    }

    // Test getRawArguments()
    @Test
    public void testGetRawArguments() {
        Object mock = new Object();
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[]{"arg1", "arg2"};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertArrayEquals(args, invocation.getRawArguments());
    }

    // Test callRealMethod()
    @Test(expected = Throwable.class)
    public void testCallRealMethod() throws Throwable {
        Object mock = new Object();
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[]{};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        when(realMethod.invoke(mock, args)).thenThrow(new Throwable());

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        invocation.callRealMethod();
    }

    // Test toString(PrintSettings)
    @Test
    public void testToStringWithPrintSettings() {
        Object mock = new Object();
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[]{"arg1", "arg2"};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
        PrintableInvocation printableInvocation = invocation;

        assertEquals(mock.getClass().getName() + "$MockitoMethod." + method.getName() + "[arg1, arg2]", printableInvocation.toString());
    }

    // Test markVerified()
    @Test
    public void testMarkVerified() {
        Object mock = new Object();
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[]{};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
        assertFalse(invocation.isVerified());

        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }

    // Test markVerifiedInOrder()
    @Test
    public void testMarkVerifiedInOrder() {
        Object mock = new Object();
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[]{};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
        assertFalse(invocation.isVerifiedInOrder());

        invocation.markVerifiedInOrder();
        assertTrue(invocation.isVerified());
        assertTrue(invocation.isVerifiedInOrder());
    }

}