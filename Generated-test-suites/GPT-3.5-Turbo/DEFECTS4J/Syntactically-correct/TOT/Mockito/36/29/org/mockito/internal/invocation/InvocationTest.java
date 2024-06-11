import org.junit.Test;
import org.mockito.internal.debugging.Invocation;
import org.mockito.internal.invocation.MockitoMethod;
import org.mockito.internal.invocation.realmethod.RealMethod;
import org.mockito.invocation.InvocationOnMock;

import static org.junit.Assert.*;

public class InvocationTest {

    @Test
    public void testGetMock() {
        Object mock = new Object();
        MockitoMethod method = MockitoMethod.of("methodName", null);
        Object[] args = {1, "test"};
        int sequenceNumber = 1;
        RealMethod realMethod = RealMethod.of("methodName");
        
        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
        
        assertEquals(mock, invocation.getMock());
    }

    @Test
    public void testGetMethod() {
        Object mock = new Object();
        MockitoMethod method = MockitoMethod.of("methodName", null);
        Object[] args = {1, "test"};
        int sequenceNumber = 1;
        RealMethod realMethod = RealMethod.of("methodName");

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertEquals(method, invocation.getMethod());
    }

    @Test
    public void testGetArguments() {
        Object mock = new Object();
        MockitoMethod method = MockitoMethod.of("methodName", null);
        Object[] args = {1, "test"};
        int sequenceNumber = 1;
        RealMethod realMethod = RealMethod.of("methodName");

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertArrayEquals(args, invocation.getArguments());
    }

    @Test
    public void testIsVerified() {
        Object mock = new Object();
        MockitoMethod method = MockitoMethod.of("methodName", null);
        Object[] args = {1, "test"};
        int sequenceNumber = 1;
        RealMethod realMethod = RealMethod.of("methodName");

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertFalse(invocation.isVerified());

        invocation.markVerified();

        assertTrue(invocation.isVerified());
    }

    @Test
    public void testGetSequenceNumber() {
        Object mock = new Object();
        MockitoMethod method = MockitoMethod.of("methodName", null);
        Object[] args = {1, "test"};
        int sequenceNumber = 1;
        RealMethod realMethod = RealMethod.of("methodName");

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertEquals(Integer.valueOf(sequenceNumber), invocation.getSequenceNumber());
    }

    @Test
    public void testIsVerifiedInOrder() {
        Object mock = new Object();
        MockitoMethod method = MockitoMethod.of("methodName", null);
        Object[] args = {1, "test"};
        int sequenceNumber = 1;
        RealMethod realMethod = RealMethod.of("methodName");

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertFalse(invocation.isVerifiedInOrder());

        invocation.markVerifiedInOrder();

        assertTrue(invocation.isVerifiedInOrder());
    }

    @Test
    public void testEquals() {
        Object mock = new Object();
        MockitoMethod method1 = MockitoMethod.of("methodName", null);
        Object[] args1 = {1, "test"};
        int sequenceNumber1 = 1;
        RealMethod realMethod1 = RealMethod.of("methodName");

        MockitoMethod method2 = MockitoMethod.of("methodName", null);
        Object[] args2 = {1, "test"};
        int sequenceNumber2 = 1;
        RealMethod realMethod2 = RealMethod.of("methodName");

        Invocation invocation1 = new Invocation(mock, method1, args1, sequenceNumber1, realMethod1);
        Invocation invocation2 = new Invocation(mock, method2, args2, sequenceNumber2, realMethod2);

        assertTrue(invocation1.equals(invocation1));
        assertTrue(invocation1.equals(invocation2));
    }

    @Test
    public void testHashCode() {
        Object mock = new Object();
        MockitoMethod method = MockitoMethod.of("methodName", null);
        Object[] args = {1, "test"};
        int sequenceNumber = 1;
        RealMethod realMethod = RealMethod.of("methodName");

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertThrows(RuntimeException.class, invocation::hashCode);
    }

    @Test
    public void testToString() {
        Object mock = new Object();
        MockitoMethod method = MockitoMethod.of("methodName", null);
        Object[] args = {1, "test"};
        int sequenceNumber = 1;
        RealMethod realMethod = RealMethod.of("methodName");

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        String expectedToString = "MockitoMock.methodName(1, \"test\")";

        assertEquals(expectedToString, invocation.toString());
    }

    @Test
    public void testIsValidException() {
        Object mock = new Object();
        MockitoMethod method = MockitoMethod.of("methodName", null);
        Object[] args = {1, "test"};
        int sequenceNumber = 1;
        RealMethod realMethod = RealMethod.of("methodName");

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        Throwable throwable = new Throwable();

        assertFalse(invocation.isValidException(throwable));
    }

    @Test
    public void testIsValidReturnType() {
        Object mock = new Object();
        MockitoMethod method = MockitoMethod.of("methodName", null);
        Object[] args = {1, "test"};
        int sequenceNumber = 1;
        RealMethod realMethod = RealMethod.of("methodName");

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        Class<?> clazz = String.class;

        assertTrue(invocation.isValidReturnType(clazz));
    }

    @Test
    public void testIsVoid() {
        Object mock = new Object();
        MockitoMethod method = MockitoMethod.of("methodName", null);
        Object[] args = {1, "test"};
        int sequenceNumber = 1;
        RealMethod realMethod = RealMethod.of("methodName");

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertFalse(invocation.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        Object mock = new Object();
        MockitoMethod method = MockitoMethod.of("methodName", null);
        Object[] args = {1, "test"};
        int sequenceNumber = 1;
        RealMethod realMethod = RealMethod.of("methodName");

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        String expectedReturnType = "void";

        assertEquals(expectedReturnType, invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        Object mock = new Object();
        MockitoMethod method = MockitoMethod.of("methodName", null);
        Object[] args = {1, "test"};
        int sequenceNumber = 1;
        RealMethod realMethod = RealMethod.of("methodName");

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        String expectedMethodName = "methodName";

        assertEquals(expectedMethodName, invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        Object mock = new Object();
        MockitoMethod method = MockitoMethod.of("methodName", null);
        Object[] args = {1, "test"};
        int sequenceNumber = 1;
        RealMethod realMethod = RealMethod.of("methodName");

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertFalse(invocation.returnsPrimitive());
    }

    @Test
    public void testGetLocation() {
        Object mock = new Object();
        MockitoMethod method = MockitoMethod.of("methodName", null);
        Object[] args = {1, "test"};
        int sequenceNumber = 1;
        RealMethod realMethod = RealMethod.of("methodName");

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertNotNull(invocation.getLocation());
    }

    @Test
    public void testGetArgumentsCount() {
        Object mock = new Object();
        MockitoMethod method = MockitoMethod.of("methodName", null);
        Object[] args = {1, "test"};
        int sequenceNumber = 1;
        RealMethod realMethod = RealMethod.of("methodName");

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        int expectedArgumentsCount = 2;

        assertEquals(expectedArgumentsCount, invocation.getArgumentsCount());
    }

    @Test
    public void testGetRawArguments() {
        Object mock = new Object();
        MockitoMethod method = MockitoMethod.of("methodName", null);
        Object[] args = {1, "test"};
        int sequenceNumber = 1;
        RealMethod realMethod = RealMethod.of("methodName");

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertArrayEquals(args, invocation.getRawArguments());
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        Object mock = new Object();
        MockitoMethod method = MockitoMethod.of("methodName", null);
        Object[] args = {1, "test"};
        int sequenceNumber = 1;
        RealMethod realMethod = RealMethod.of("methodName");

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertThrows(UnsupportedOperationException.class, invocation::callRealMethod);
    }

    @Test
    public void testToStringWithSettings() {
        Object mock = new Object();
        MockitoMethod method = MockitoMethod.of("methodName", null);
        Object[] args = {1, "test"};
        int sequenceNumber = 1;
        RealMethod realMethod = RealMethod.of("methodName");

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
        
        PrintSettings printSettings = new PrintSettings();
        
        String expectedToString = "MockitoMock.methodName(\n" +
                                  "    1,\n" +
                                  "    \"test\"\n" +
                                  ")";

        assertEquals(expectedToString, invocation.toString(printSettings));
    }

    @Test
    public void testMarkVerified() {
        Object mock = new Object();
        MockitoMethod method = MockitoMethod.of("methodName", null);
        Object[] args = {1, "test"};
        int sequenceNumber = 1;
        RealMethod realMethod = RealMethod.of("methodName");

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertFalse(invocation.isVerified());
        
        invocation.markVerified();
        
        assertTrue(invocation.isVerified());
    }

    @Test
    public void testMarkVerifiedInOrder() {
        Object mock = new Object();
        MockitoMethod method = MockitoMethod.of("methodName", null);
        Object[] args = {1, "test"};
        int sequenceNumber = 1;
        RealMethod realMethod = RealMethod.of("methodName");

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertFalse(invocation.isVerifiedInOrder());

        invocation.markVerifiedInOrder();

        assertTrue(invocation.isVerifiedInOrder());
    }
}