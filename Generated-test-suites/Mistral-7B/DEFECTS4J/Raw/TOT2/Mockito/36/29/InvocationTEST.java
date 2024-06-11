import static org.mockito.Mockito.*;
import org.mockito.Mockito;
import org.junit.Test;
import org.mockito.internal.invocation.Invocation;
import org.mockito.internal.invocation.RealMethod;

public class InvocationTest {

    @Test
    public void testNewInvocation() {
        Object mock = Mockito.mock(String.class);
        MockitoMethod mockitoMethod = Mockito.method(String.class, "toString");
        Object[] args = new Object[0];
        int sequenceNumber = 1;
        RealMethod realMethod = Mockito.mock(RealMethod.class);

        Invocation invocation = new Invocation(mock, mockitoMethod, args, sequenceNumber, realMethod);

        // Basic functionality test
        assertNotNull(invocation);
        assertEquals(mock, invocation.getMock());
        assertEquals(mockitoMethod, invocation.getMethod());
        assertArrayEquals(args, invocation.getArguments());
        assertEquals(sequenceNumber, invocation.getSequenceNumber());
        assertEquals(realMethod, invocation.realMethod);

        // Edge case: null arguments
        Object[] nullArgs = null;
        Invocation invocationNullArgs = new Invocation(mock, mockitoMethod, nullArgs, sequenceNumber, realMethod);
        assertNull(invocationNullArgs.getRawArguments());

        // Exception handling: invalid method signature
        try {
            Invocation invocationInvalidMethod = new Invocation(mock, null, args, sequenceNumber, realMethod);
            fail();
        } catch (NullPointerException e) {
            // Expected
        }

        // Exception handling: invalid mock object
        try {
            Invocation invocationInvalidMock = new Invocation(null, mockitoMethod, args, sequenceNumber, realMethod);
            fail();
        } catch (NullPointerException e) {
            // Expected
        }
    }
}