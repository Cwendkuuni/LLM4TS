import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class InvocationTest {

    @Test
    public void testEquals() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[]{1, "test"};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation1 = new Invocation(mock, method, args, sequenceNumber, realMethod);
        Invocation invocation2 = new Invocation(mock, method, args, sequenceNumber, realMethod);
        Invocation invocation3 = new Invocation(mock, method, new Object[]{2, "test"}, sequenceNumber, realMethod);

        assertEquals(invocation1, invocation2);
        assertEquals(invocation2, invocation1);
        assertEquals(invocation1.hashCode(), invocation2.hashCode());
        assertEquals(invocation2.hashCode(), invocation1.hashCode());
        assertEquals(false, invocation1.equals(invocation3));
        assertEquals(false, invocation2.equals(invocation3));
    }

    @Test
    public void testToString() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[]{1, "test"};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
        assertEquals("Object.method(1, test)", invocation.toString());
    }

    @Test
    public void testIsToString() {
        MockitoMethod method = mock(MockitoMethod.class);
        when(method.getName()).thenReturn("toString");

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(method);

        assertTrue(Invocation.isToString(invocation));
    }

    @Test
    public void testIsValidException() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[]{1, "test"};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
        Throwable throwable = new Exception();

        assertTrue(invocation.isValidException(throwable));
    }

    @Test
    public void testIsValidReturnType() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[]{1, "test"};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
        Class clazz = String.class;

        assertTrue(invocation.isValidReturnType(clazz));
    }

    @Test
    public void testIsVoid() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[]{1, "test"};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation1 = new Invocation(mock, method, args, sequenceNumber, realMethod);
        Invocation invocation2 = new Invocation(mock, method, args, sequenceNumber, realMethod);
        Invocation invocation3 = new Invocation(mock, method, new Object[]{2, "void"}, sequenceNumber, realMethod);

        assertFalse(invocation1.isVoid());
        assertFalse(invocation2.isVoid());
        assertTrue(invocation3.isVoid());
    }

    @Test
    public void testPrintMethodReturnType() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[]{1, "test"};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
        assertEquals("Void", invocation.printMethodReturnType());
    }

    @Test
    public void testGetMethodName() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[]{1, "test"};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
        assertEquals("method", invocation.getMethodName());
    }

    @Test
    public void testReturnsPrimitive() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[]{1, "test"};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
        assertFalse(invocation.returnsPrimitive());
    }

    @Test
    public void testMarkVerified() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[]{1, "test"};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }

    @Test
    public void testMarkVerifiedInOrder() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = new Object[]{1, "test"};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);
        invocation.markVerifiedInOrder();
        assertTrue(invocation.isVerified());
        assertTrue(invocation.isVerifiedInOrder());
    }

}