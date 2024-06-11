import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationTest {

    private Invocation mockInvocation;

    @Before
    public void setUp() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod method = Mockito.method(String.class, "length");
        Object[] arguments = new Object[]{"test"};
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        int sequenceNumber = 1;

        mockInvocation = new Invocation(mock, method, arguments, sequenceNumber, realMethod);
    }

    @Test
    public void testGetMock() {
        assertSame(mockInvocation.getMock(), mock);
    }

    @Test
    public void testGetMethod() {
        assertEquals(mockInvocation.getMethod(), method);
    }

    @Test
    public void testGetArguments() {
        assertArrayEquals(mockInvocation.getArguments(), new Object[]{"test"});
    }

    @Test
    public void testIsVerified() {
        assertFalse(mockInvocation.isVerified());

        mockInvocation.markVerified();

        assertTrue(mockInvocation.isVerified());
    }

    @Test
    public void testGetSequenceNumber() {
        assertEquals(mockInvocation.getSequenceNumber(), 1);
    }

    @Test
    public void testGetVerifiedInOrder() {
        assertFalse(mockInvocation.isVerifiedInOrder());

        mockInvocation.markVerifiedInOrder();

        assertTrue(mockInvocation.isVerifiedInOrder());
    }

    @Test
    public void testEquals() {
        Object mock2 = Mockito.mock(String.class);
        MockitoMethod method2 = Mockito.method(String.class, "length");
        Object[] arguments2 = new Object[]{"test2"};

        Invocation invocation2 = new Invocation(mock2, method2, arguments2, 1, realMethod);

        assertFalse(mockInvocation.equals(invocation2));

        mockInvocation.markVerified();
        invocation2.markVerified();

        assertTrue(mockInvocation.equals(invocation2));
    }

    @Test(expected = RuntimeException.class)
    public void testHashCode() {
        mockInvocation.hashCode();
    }

    @Test
    public void testToString() {
        assertEquals("test.length(String)", mockInvocation.toString());
    }

    @Test
    public void testToStringPrintable() {
        assertEquals("Invocation [mock=String, method=length, arguments=[test], sequenceNumber=1]", mockInvocation.toString());
    }

    @Test
    public void testToStringWithMatchers() {
        when(mockInvocation.getMock()).thenReturn("test");

        assertEquals("test.length(String) -> String", mockInvocation.toString(Arrays.<Matcher>asList(ArgumentMatchers.eq("test")), new PrintSettings()));
    }

    @Test
    public void testIsToString() {
        assertTrue(Invocation.isToString(mockInvocation));
    }

    @Test
    public void testIsValidException() {
        Throwable exception = new IllegalArgumentException("Invalid argument");

        assertFalse(mockInvocation.isValidException(exception));

        Mockito.doThrow(new NullPointerException()).when(mockInvocation.getMock()).length();

        assertTrue(mockInvocation.isValidException(null));
    }

    @Test
    public void testIsValidReturnType() {
        assertTrue(mockInvocation.isValidReturnType(int.class));
        assertFalse(mockInvocation.isValidReturnType(String.class));
    }

    @Test
    public void testIsVoid() {
        assertFalse(mockInvocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        assertEquals("int", mockInvocation.printMethodReturnType());
    }

    @Test
    public void testGetLocation() {
        Location location = new Location();
        mockInvocation.setLocation(location);

        assertEquals(location, mockInvocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        assertEquals(1, mockInvocation.getArgumentsCount());
    }

    @Test
    public void testGetRawArguments() {
        assertArrayEquals(new Object[]{"test"}, mockInvocation.getRawArguments());
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        assertEquals(5, mockInvocation.callRealMethod().length());
    }
}