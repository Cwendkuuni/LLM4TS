import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
    }

    @Test
    public void testEquals() {
        Object mock1 = mock(Object.class);
        MockitoMethod method1 = mock(MockitoMethod.class);
        Object[] args1 = {1, "test"};
        int sequenceNumber1 = 1;
        RealMethod realMethod1 = mock(RealMethod.class);

        Invocation invocation1 = new Invocation(mock1, method1, args1, sequenceNumber1, realMethod1);

        Object mock2 = mock(Object.class);
        MockitoMethod method2 = mock(MockitoMethod.class);
        Object[] args2 = {1, "test"};
        int sequenceNumber2 = 2;
        RealMethod realMethod2 = mock(RealMethod.class);

        Invocation invocation2 = new Invocation(mock2, method2, args2, sequenceNumber2, realMethod2);

        assertFalse(invocation1.equals(null));
        assertFalse(invocation1.equals("test"));
        assertFalse(invocation1.equals(invocation2));

        Invocation invocation3 = new Invocation(mock1, method1, args1, sequenceNumber1, realMethod1);
        assertTrue(invocation1.equals(invocation3));
    }

    // Additional test cases for other methods can be added here

}