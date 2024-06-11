import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class InvocationTest {

    // Test case for the equals method
    @Test
    public void testEquals() {
        Object mock1 = mock(Object.class);
        MockitoMethod method1 = mock(MockitoMethod.class);
        Object[] args1 = new Object[]{1, "test"};
        RealMethod realMethod1 = mock(RealMethod.class);

        Invocation invocation1 = new Invocation(mock1, method1, args1, 1, realMethod1);
        Invocation invocation2 = new Invocation(mock1, method1, args1, 1, realMethod1);

        assertTrue(invocation1.equals(invocation2));
    }

    // Test case for the expandVarArgs method
    @Test
    public void testExpandVarArgs() {
        Object[] args = new Object[]{1, "test", new Object[]{2, 3}};

        Object[] expandedArgs = Invocation.expandVarArgs(true, args);

        assertEquals(4, expandedArgs.length);
    }

    // Add more test cases for other methods here...

}