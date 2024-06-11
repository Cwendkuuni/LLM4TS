import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationTest {

    @Test
    public void testGetMock() {
        // Initialize Invocation object
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod("testMethod", null, null);
        Object[] args = new Object[]{"arg1", "arg2"};
        RealMethod realMethod = new RealMethod();

        Invocation invocation = new Invocation(mock, method, args, 1, realMethod);

        // Test getMock method
        assertEquals(mock, invocation.getMock());
    }

    @Test
    public void testGetArguments() {
        // Initialize Invocation object
        Object mock = new Object();
        MockitoMethod method = new MockitoMethod("testMethod", null, null);
        Object[] args = new Object[]{"arg1", "arg2"};
        RealMethod realMethod = new RealMethod();

        Invocation invocation = new Invocation(mock, method, args, 1, realMethod);

        // Test getArguments method
        assertArrayEquals(args, invocation.getArguments());
    }

    // Add more test cases as needed for the remaining methods...

}