import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.MockitoMethod;
import org.mockito.internal.invocation.realmethod.RealMethod;

public class InvocationTest {

    private Object mock;
    private MockitoMethod mockitoMethod;
    private Object[] args;
    private int sequenceNumber;
    private RealMethod realMethod;
    private Invocation invocation;

    @Before
    public void setUp() {
        mock = mock(Object.class);
        mockitoMethod = mock(MockitoMethod.class);
        args = new Object[0];
        sequenceNumber = 1;
        realMethod = mock(RealMethod.class);
        invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);
    }

    @Test
    public void testGetMock() {
        assertEquals(mock, invocation.getMock());
    }

    @Test
    public void testGetMethod() {
        assertEquals(mockitoMethod, invocation.getMethod());
    }

    @Test
    public void testGetArguments() {
        assertArrayEquals(args, invocation.getArguments());
    }

    @Test
    public void testIsVerified() {
        assertFalse(invocation.isVerified());
    }

    @Test
    public void testGetSequenceNumber() {
        assertEquals(Integer.valueOf(sequenceNumber), invocation.getSequenceNumber());
    }

    @Test
    public void testIsVerifiedInOrder() {
        assertFalse(invocation.isVerifiedInOrder());
    }

    @Test
    public void testEquals() {
        Invocation other = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);
        assertTrue(invocation.equals(other));
    }

    @Test
    public void testHashCode() {
        assertTrue(invocation.hashCode() >= 0);
    }

    @Test
    public void testToString() {
        assertNotNull(invocation.toString());
    }

    @Test
    public void testIsToString() {
        assertFalse(Invocation.isToString(invocation));
    }

    @Test
    public void testIsValidException() {
        Throwable throwable = new Exception();
        assertTrue(invocation.isValidException(throwable));
    }

    @Test
    public void testIsValidReturnType() {
        Class<?> clazz = Object.class;
        assertTrue(invocation.isValidReturnType(clazz));
    }

    @Test
    public void testIsVoid() {
        assertFalse(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        assertNotNull(invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        assertNotNull(invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        assertFalse(invocation.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        assertNotNull(invocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        assertEquals(args.length, invocation.getArgumentsCount());
    }

    @Test
    public void testGetRawArguments() {
        assertArrayEquals(args, invocation.getRawArguments());
    }

    @Test(expected = Exception.class)
    public void testCallRealMethod() throws Throwable {
        when(realMethod.invoke(any(), any())).thenThrow(Exception.class);
        invocation.callRealMethod();
    }

    @Test
    public void testToStringWithPrintSettings() {
        assertNotNull(invocation.toString(mock(PrintSettings.class)));
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
}