import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.RealMethod;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class InvocationTest {

    private Object mock;
    private Invocation invocation;

    @Before
    public void setUp() {
        mock = Mockito.mock(String.class);
    }

    @Test
    public void testInvocationWithStringArgument() {
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(String.class, "testMethod", new Class[0]), new Object[]{"hello"}, 1, realMethod);

        verify(mock).testMethod("hello");
        assertEquals(1, invocation.getSequenceNumber());
        assertEquals(mock, invocation.getMock());
        assertEquals("testMethod(java.lang.String)", invocation.getMethod().getName());
        assertEquals(new Object[]{"hello"}, invocation.getArguments());
        assertArrayEquals(new Object[]{"hello"}, invocation.getRawArguments());
        assertEquals(false, invocation.isVerified());
        assertEquals(false, invocation.isVerifiedInOrder());

        invocation.markVerified();
        invocation.markVerifiedInOrder();

        assertEquals(true, invocation.isVerified());
        assertEquals(true, invocation.isVerifiedInOrder());
    }

    @Test
    public void testInvocationWithArrayArgument() {
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(String.class, "testMethod", new Class[]{String[].class}), new Object[]{"hello", new String[]{"world"}}, 2, realMethod);

        verify(mock).testMethod(ArgumentMatchers.arrayContaining(Arrays.asList("hello", "world")));
        assertEquals(2, invocation.getSequenceNumber());
        assertEquals(mock, invocation.getMock());
        assertEquals("testMethod([Ljava.lang.String;)", invocation.getMethod().getName());
        assertEquals(new Object[]{"hello", new String[]{"world"}}, invocation.getArguments());
        assertArrayEquals(new Object[]{"hello", new String[]{"world"}}, invocation.getRawArguments());
        assertEquals(false, invocation.isVerified());
        assertEquals(false, invocation.isVerifiedInOrder());

        invocation.markVerified();
        invocation.markVerifiedInOrder();

        assertEquals(true, invocation.isVerified());
        assertEquals(true, invocation.isVerifiedInOrder());
    }

    @Test(expected = AssertionError.class)
    public void testInvocationWithInvalidArgumentType() {
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(String.class, "testMethod", new Class[]{Integer.TYPE}), new Object[]{"hello"}, 1, realMethod);

        verify(mock).testMethod(ArgumentMatchers.anyInt());

        // This should fail with an AssertionError due to method signature mismatch
        invocation.callRealMethod();
    }

    @Test
    public void testInvocationWithException() {
        RealMethod realMethod = Mockito.mock(RealMethod.class);
        Invocation invocation = new Invocation(mock, new MockitoMethod(String.class, "testMethod", new Class[0]), new Object[0], 1, realMethod);

        doThrow(new RuntimeException("Test exception")).when(mock).testMethod();

        try {
            invocation.callRealMethod();
            fail("Expected exception");
        } catch (RuntimeException e) {
            assertEquals("Test exception", e.getMessage());
        }

        assertEquals(true, invocation.isValidException(new RuntimeException("Test exception")));
    }
}