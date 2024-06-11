
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.MockitoMethod;
import org.mockito.internal.invocation.realmethod.RealMethod;
import org.mockito.internal.debugging.Location;

public class InvocationTest {

    private Invocation invocation;

    @Before
    public void setup() {
        Object mock = mock(Object.class);
        MockitoMethod mockitoMethod = new MockitoMethod(Invocation.class.getDeclaredMethods()[0]);
        Object[] args = { "arg1", "arg2" };
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);
    }

    @Test
    public void testGetMock() {
        assertNotNull(invocation.getMock());
    }

    @Test
    public void testGetMethod() {
        assertNotNull(invocation.getMethod());
    }

    @Test
    public void testGetArguments() {
        assertNotNull(invocation.getArguments());
        assertEquals(2, invocation.getArguments().length);
        assertEquals("arg1", invocation.getArguments()[0]);
        assertEquals("arg2", invocation.getArguments()[1]);
    }

    @Test
    public void testIsVerified() {
        assertFalse(invocation.isVerified());
    }

    @Test
    public void testGetSequenceNumber() {
        assertEquals(Integer.valueOf(1), invocation.getSequenceNumber());
    }

    @Test
    public void testIsVerifiedInOrder() {
        assertFalse(invocation.isVerifiedInOrder());
    }

    @Test
    public void testEquals() {
        Invocation other = new Invocation(mock(Object.class), new MockitoMethod(Invocation.class.getDeclaredMethods()[0]),
                new Object[] { "arg1", "arg2" }, 2, mock(RealMethod.class));
        assertTrue(invocation.equals(other));
    }

    @Test
    public void testHashCode() {
        assertThrows(RuntimeException.class, () -> invocation.hashCode());
    }

    @Test
    public void testToString() {
        assertThat(invocation.toString(), Matchers.containsString("Invocation@"));
    }

    @Test
    public void testArgumentsToMatchers() {
        assertNotNull(invocation.argumentsToMatchers());
        assertEquals(2, invocation.argumentsToMatchers().size());
        assertThat(invocation.argumentsToMatchers().get(0), instanceOf(Equals.class));
        assertThat(invocation.argumentsToMatchers().get(1), instanceOf(Equals.class));
    }

    @Test
    public void testIsToString() {
        InvocationOnMock invocationOnMock = mock(InvocationOnMock.class);
        when(invocationOnMock.getMethod()).thenReturn(Invocation.class.getDeclaredMethods()[0]);
        assertFalse(Invocation.isToString(invocationOnMock));
    }

    @Test
    public void testIsValidException() {
        Throwable throwable = new RuntimeException();
        assertFalse(invocation.isValidException(throwable));
    }

    @Test
    public void testIsValidReturnType() {
        assertTrue(invocation.isValidReturnType(Object.class));
    }

    @Test
    public void testIsVoid() {
        assertFalse(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        assertEquals("void", invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        assertEquals("getMock", invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        assertFalse(invocation.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        assertNotNull(invocation.getLocation());
        assertEquals(Location.class, invocation.getLocation().getClass());
    }

    @Test
    public void testGetArgumentsCount() {
        assertEquals(2, invocation.getArgumentsCount());
    }

    @Test
    public void testGetRawArguments() {
        assertNotNull(invocation.getRawArguments());
        assertEquals(2, invocation.getRawArguments().length);
        assertEquals("arg1", invocation.getRawArguments()[0]);
        assertEquals("arg2", invocation.getRawArguments()[1]);
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        assertNull(invocation.callRealMethod());
    }

    @Test
    public void testToStringWithPrintSettings() {
        assertEquals(invocation.toString(), invocation.toString(new PrintSettings()));
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
