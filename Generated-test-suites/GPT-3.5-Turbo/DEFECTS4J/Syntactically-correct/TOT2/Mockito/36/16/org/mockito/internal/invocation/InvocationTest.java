import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.RealMethod;

public class InvocationTest {

    @Test
    public void testConstructor() {
        Object mock = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = {1, "test"};
        int sequenceNumber = 1;
        RealMethod realMethod = mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, method, args, sequenceNumber, realMethod);

        assertEquals(mock, invocation.getMock());
        assertEquals(method, invocation.getMethod());
        assertArrayEquals(args, invocation.getArguments());
        assertFalse(invocation.isVerified());
        assertEquals(sequenceNumber, invocation.getSequenceNumber().intValue());
        assertFalse(invocation.isVerifiedInOrder());
    }

    // Test other public methods here

}