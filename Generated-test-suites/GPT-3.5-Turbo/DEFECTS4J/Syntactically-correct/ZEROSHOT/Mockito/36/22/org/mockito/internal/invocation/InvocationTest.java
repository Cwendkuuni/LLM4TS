import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InvocationTest {

    private Invocation invocation;

    @Before
    public void setUp() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[]{};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
    }

    @Test
    public void testGetMock() {
        Object expected = mock(Object.class);
        Object actual = invocation.getMock();

        assertEquals(expected, actual);
    }

    @Test
    public void testGeMethod() {
        MockitoMethod expected = mock(MockitoMethod.class);
        MockitoMethod actual = invocation.getMethod();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetArguments() {
        Object[] expected = new Object[]{};
        Object[] actual = invocation.getArguments();

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testIsVerified() {
        boolean expected = false;
        boolean actual = invocation.isVerified();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetSequenceNumber() {
        Integer expected = 1;
        Integer actual = invocation.getSequenceNumber();

        assertEquals(expected, actual);
    }

    @Test
    public void testIsVerifiedInOrder() {
        boolean expected = false;
        boolean actual = invocation.isVerifiedInOrder();

        assertEquals(expected, actual);
    }

    @Test
    public void testEquals() {
        Invocation other = new Invocation(mock(Object.class), mock(MockitoMethod.class), new Object[]{}, 1, mock(RealMethod.class));

        boolean expected = true;
        boolean actual = invocation.equals(other);

        assertEquals(expected, actual);
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
        String expected = invocation.qualifiedMethodName();
        String actual = invocation.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testToStringPrintSettings() {
        PrintSettings printSettings = mock(PrintSettings.class);

        String expected = invocation.qualifiedMethodName();
        String actual = invocation.toString(printSettings);

        assertEquals(expected, actual);
    }

    @Test
    public void testArgumentsToMatchers() {
        List<Matcher> expected = new ArrayList<>();
        Object[] arguments = invocation.getArguments();

        for (Object arg : arguments) {
            if (arg != null && arg.getClass().isArray()) {
                expected.add(new ArrayEquals(arg));
            } else {
                expected.add(new Equals(arg));
            }
        }

        List<Matcher> actual = invocation.argumentsToMatchers();

        assertEquals(expected, actual);
    }

    @Test
    public void testIsToString() {
        MockitoMethod method = mock(MockitoMethod.class);
        when(method.getName()).thenReturn("toString");

        InvocationOnMock invocationOnMock = mock(InvocationOnMock.class);
        when(invocationOnMock.getMethod()).thenReturn(method);

        boolean expected = true;
        boolean actual = Invocation.isToString(invocationOnMock);

        assertEquals(expected, actual);
    }

    @Test
    public void testIsValidException() {
        Throwable throwable = new Throwable();

        MockitoMethod method = mock(MockitoMethod.class);
        when(method.getExceptionTypes()).thenReturn(new Class[]{Throwable.class});

        Invocation invocation = new Invocation(mock(Object.class), method, new Object[]{}, 1, mock(RealMethod.class));

        boolean expected = true;
        boolean actual = invocation.isValidException(throwable);

        assertEquals(expected, actual);
    }

    @Test
    public void testIsValidReturnType() {
        Class clazz = Object.class;

        MockitoMethod method = mock(MockitoMethod.class);
        when(method.getReturnType()).thenReturn(Object.class);

        Invocation invocation = new Invocation(mock(Object.class), method, new Object[]{}, 1, mock(RealMethod.class));

        boolean expected = true;
        boolean actual = invocation.isValidReturnType(clazz);

        assertEquals(expected, actual);
    }

    @Test
    public void testIsVoid() {
        MockitoMethod method = mock(MockitoMethod.class);
        when(method.getReturnType()).thenReturn(Void.TYPE);

        Invocation invocation = new Invocation(mock(Object.class), method, new Object[]{}, 1, mock(RealMethod.class));

        boolean expected = true;
        boolean actual = invocation.isVoid();

        assertEquals(expected, actual);
    }

    @Test
    public void testPrintMethodReturnType() {
        MockitoMethod method = mock(MockitoMethod.class);
        when(method.getReturnType()).thenReturn(Object.class);

        Invocation invocation = new Invocation(mock(Object.class), method, new Object[]{}, 1, mock(RealMethod.class));

        String expected = "Object";
        String actual = invocation.printMethodReturnType();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetMethodName() {
        MockitoMethod method = mock(MockitoMethod.class);
        when(method.getName()).thenReturn("method");

        Invocation invocation = new Invocation(mock(Object.class), method, new Object[]{}, 1, mock(RealMethod.class));

        String expected = "method";
        String actual = invocation.getMethodName();

        assertEquals(expected, actual);
    }

    @Test
    public void testReturnsPrimitive() {
        MockitoMethod method = mock(MockitoMethod.class);
        when(method.getReturnType()).thenReturn(int.class);

        Invocation invocation = new Invocation(mock(Object.class), method, new Object[]{}, 1, mock(RealMethod.class));

        boolean expected = true;
        boolean actual = invocation.returnsPrimitive();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetLocation() {
        Location expected = new Location();
        Location actual = invocation.getLocation();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetArgumentsCount() {
        int expected = invocation.getArguments().length;
        int actual = invocation.getArgumentsCount();

        assertEquals(expected, actual);
    }

    @Test
    public void testGetRawArguments() {
        Object[] expected = invocation.getArguments();
        Object[] actual = invocation.getRawArguments();

        assertArrayEquals(expected, actual);
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        Object expected = new Object();

        RealMethod realMethod = mock(RealMethod.class);
        when(realMethod.invoke(any(), any())).thenReturn(expected);

        Object actual = invocation.callRealMethod();

        assertEquals(expected, actual);
    }

    @Test
    public void testMarkVerified() {
        invocation.markVerified();

        boolean expected = true;
        boolean actual = invocation.isVerified();

        assertEquals(expected, actual);
    }

    @Test
    public void testMarkVerifiedInOrder() {
        invocation.markVerifiedInOrder();

        boolean expectedVerified = true;
        boolean actualVerified = invocation.isVerified();

        boolean expectedVerifiedInOrder = true;
        boolean actualVerifiedInOrder = invocation.isVerifiedInOrder();

        assertEquals(expectedVerified, actualVerified);
        assertEquals(expectedVerifiedInOrder, actualVerifiedInOrder);
    }
}