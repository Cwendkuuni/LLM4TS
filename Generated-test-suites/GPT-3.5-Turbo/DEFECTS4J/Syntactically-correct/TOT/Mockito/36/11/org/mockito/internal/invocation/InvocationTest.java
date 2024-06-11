import static org.junit.Assert.*;
import org.junit.Test;

public class InvocationTest {

    @Test
    public void testGetMock() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[] {1, 2, 3};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertSame(mock, invocation.getMock());
    }

    @Test
    public void testGetMethod() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[] {1, 2, 3};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertSame(method, invocation.getMethod());
    }

    @Test
    public void testGetArguments() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[] {1, 2, 3};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertArrayEquals(args, invocation.getArguments());
    }

    @Test
    public void testIsVerified() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[] {1, 2, 3};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertFalse(invocation.isVerified());
    }

    @Test
    public void testGetSequenceNumber() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[] {1, 2, 3};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertEquals(Integer.valueOf(sequenceNumber), invocation.getSequenceNumber());
    }

    @Test
    public void testIsVerifiedInOrder() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[] {1, 2, 3};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertFalse(invocation.isVerifiedInOrder());
    }

    @Test
    public void testEquals() {
        Object mock1 = new Object();
        Object mock2 = new Object();
        MockitoMethod method1 = new MockitoMethod();
        MockitoMethod method2 = new MockitoMethod();
        Object[] args1 = new Object[] {1, 2, 3};
        Object[] args2 = new Object[] {4, 5, 6};
        int sequenceNumber1 = 1;
        int sequenceNumber2 = 2;
        RealMethod realMethod = new RealMethod();
        Invocation invocation1 = new Invocation(mock1, method1, args1, sequenceNumber1, realMethod);
        Invocation invocation2 = new Invocation(mock1, method1, args1, sequenceNumber1, realMethod);
        Invocation invocation3 = new Invocation(mock1, method2, args1, sequenceNumber1, realMethod);
        Invocation invocation4 = new Invocation(mock2, method1, args1, sequenceNumber1, realMethod);
        Invocation invocation5 = new Invocation(mock1, method1, args2, sequenceNumber1, realMethod);
        Invocation invocation6 = new Invocation(mock1, method1, args1, sequenceNumber2, realMethod);

        assertTrue(invocation1.equals(invocation1));
        assertTrue(invocation1.equals(invocation2));
        assertFalse(invocation1.equals(invocation3));
        assertFalse(invocation1.equals(invocation4));
        assertFalse(invocation1.equals(invocation5));
        assertFalse(invocation1.equals(invocation6));
    }

    @Test
    public void testHashCode() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[] {1, 2, 3};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        try {
            invocation.hashCode();
            fail("hashCode() should not be implemented");
        } catch (RuntimeException e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testToString() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[] {1, 2, 3};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertNotNull(invocation.toString());
    }

    @Test
    public void testToStringWithPrintSettings() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[] {1, 2, 3};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
        PrintSettings printSettings = new PrintSettings();

        assertNotNull(invocation.toString(printSettings));
    }

    @Test
    public void testGetLocation() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[] {1, 2, 3};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertNotNull(invocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[] {1, 2, 3};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertEquals(args.length, invocation.getArgumentsCount());
    }

    @Test
    public void testGetRawArguments() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[] {1, 2, 3};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertArrayEquals(args, invocation.getRawArguments());
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[] {1, 2, 3};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertNull(invocation.callRealMethod());
    }

    @Test
    public void testMarkVerified() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[] {1, 2, 3};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        invocation.markVerified();

        assertTrue(invocation.isVerified());
    }

    @Test
    public void testMarkVerifiedInOrder() {
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[] {1, 2, 3};
        int sequenceNumber = 1;
        RealMethod realMethod = new RealMethod();
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        invocation.markVerifiedInOrder();

        assertTrue(invocation.isVerified());
        assertTrue(invocation.isVerifiedInOrder());
    }
}