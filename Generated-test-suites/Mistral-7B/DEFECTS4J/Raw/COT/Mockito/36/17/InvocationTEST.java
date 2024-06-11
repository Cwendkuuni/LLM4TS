import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.RealMethod;
import org.mockito.internal.matchers.ArrayEquals;
import org.mockito.internal.matchers.Equals;
import org.mockito.invocation.InvocationOnMock;

public class InvocationTest {

    @Test
    public void testConstructorWithMockAndMethodAndArguments() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{}, "testMethod", new Class[]{String.class, Integer.class});
        Object[] arguments = new Object[]{"hello", 1};
        RealMethod realMethod = new RealMethod(mockitoMethod, new Object[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, realMethod);

        assertEquals(mock, invocation.getMock());
        assertEquals(mockitoMethod, invocation.getMethod());
        assertArrayEquals(arguments, invocation.getArguments());
        assertEquals(1, invocation.getSequenceNumber());
    }

    @Test
    public void testConstructorWithVarArgs() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{String[].class}, "testMethod", new Class[0]);
        Object[] arguments = new Object[]{new Object[]{"a", "b"}};
        RealMethod realMethod = new RealMethod(mockitoMethod, new Object[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, realMethod);

        assertEquals(mock, invocation.getMock());
        assertEquals(mockitoMethod, invocation.getMethod());
        assertArrayEquals(arguments, invocation.getArguments());
        assertEquals(1, invocation.getSequenceNumber());
    }

    @Test
    public void testIsVerified() {
        Invocation invocation = new Invocation(new Object(), new MockitoMethod(new Class[]{}, "testMethod", new Class[0]), new Object[0], 1, new RealMethod(new MockitoMethod(new Class[]{}, "testMethod", new Class[0]), new Object[0]));
        assertFalse(invocation.isVerified());

        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }

    @Test
    public void testToString() {
        Object mock = new Object();
        MockitoMethod mockitoMethod = new MockitoMethod(new Class[]{String.class}, "testMethod", new Class[]{String.class});
        Object[] arguments = new Object[]{"hello"};
        RealMethod realMethod = new RealMethod(mockitoMethod, new Object[0]);
        Invocation invocation = new Invocation(mock, mockitoMethod, arguments, 1, realMethod);

        assertEquals("testMethod(java.lang.String) arguments: [hello]", invocation.toString());
    }

    @Test(expected = RuntimeException.class)
    public void testHashCode() {
        Invocation invocation = new Invocation(new Object(), new MockitoMethod(new Class[]{}, "testMethod", new Class[0]), new Object[0], 1, new RealMethod(new MockitoMethod(new Class[]{}, "testMethod", new Class[0]), new Object[0]));
        invocation.hashCode();
    }

    @Test
    public void testIsToString() {
        Invocation invocationOnMock = mock(InvocationOnMock.class);
        assertTrue(Invocation.isToString(invocationOnMock.mockingDetails()));
    }

    @Test
    public void testIsValidException() {
        Invocation invocation = new Invocation(new Object(), new MockitoMethod(new Class[]{String.class}, "testMethod", new Class[]{String.class}), new Object[0], 1, new RealMethod(new MockitoMethod(new Class[]{String.class}, "testMethod", new Class[]{String.class}), new Object[0]));

        assertFalse(invocation.isValidException(new IllegalArgumentException("error")));
        assertTrue(invocation.isValidException(new RuntimeException("error")));
    }

    @Test
    public void testIsValidReturnType() {
        Invocation invocation = new Invocation(new Object(), new MockitoMethod(new Class[]{String.class}, "testMethod", new Class[]{String.class}), new Object[0], 1, new RealMethod(new MockitoMethod(new Class[]{String.class}, "testMethod", new Class[]{String.class}), new Object[0]));

        assertTrue(invocation.isValidReturnType(String.class));
        assertFalse(invocation.isValidReturnType(Integer.class));
    }

    @Test
    public void testIsVoid() {
        Invocation invocation = new Invocation(new Object(), new MockitoMethod(new Class[]{String.class}, "testMethod", new Class[0]), new Object[0], 1, new RealMethod(new MockitoMethod(new Class[]{String.class}, "testMethod", new Class[0]), new Object[0]));

        assertFalse(invocation.isVoid());
    }
}