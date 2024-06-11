import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class InvocationTest {

    @Test
    public void testGetMock() {
        Object mock = mock(Object.class);
        Invocation invocation = new Invocation(mock, MockitoMethod.STUB, new Object[]{}, 1, null);

        assertEquals(mock, invocation.getMock());
    }

    @Test
    public void testGetSequenceNumber() {
        Object mock = mock(Object.class);
        Invocation invocation = new Invocation(mock, MockitoMethod.STUB, new Object[]{}, 1, null);

        assertEquals(Integer.valueOf(1), invocation.getSequenceNumber());
    }

    @Test
    public void testEquals() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);

        Invocation invocation1 = new Invocation(mock1, MockitoMethod.STUB, new Object[]{}, 1, null);
        Invocation invocation2 = new Invocation(mock1, MockitoMethod.STUB, new Object[]{}, 1, null);
        Invocation invocation3 = new Invocation(mock2, MockitoMethod.STUB, new Object[]{}, 1, null);

        assertTrue(invocation1.equals(invocation2));
        assertFalse(invocation1.equals(invocation3));
    }

    // Add more test cases for other methods
    
    @Test
    public void testMarkVerified() {
        Object mock = mock(Object.class);
        Invocation invocation = new Invocation(mock, MockitoMethod.STUB, new Object[]{}, 1, null);

        invocation.markVerified();
        assertTrue(invocation.isVerified());
    }

    @Test
    public void testMarkVerifiedInOrder() {
        Object mock = mock(Object.class);
        Invocation invocation = new Invocation(mock, MockitoMethod.STUB, new Object[]{}, 1, null);

        invocation.markVerifiedInOrder();
        assertTrue(invocation.isVerifiedInOrder());
    }
}