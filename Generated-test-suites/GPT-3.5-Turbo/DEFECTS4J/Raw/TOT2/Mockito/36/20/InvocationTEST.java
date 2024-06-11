import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InvocationTest {

    @Test
    public void testGetMock() {
        // Test case for getting the mock object
        Object mockObject = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = { "arg1", "arg2" };

        Invocation invocation = new Invocation(mockObject, method, args, 1, null);

        assertSame(mockObject, invocation.getMock());
    }

    @Test
    public void testGetArguments() {
        // Test case for getting the arguments of the invocation
        Object mockObject = mock(Object.class);
        MockitoMethod method = mock(MockitoMethod.class);
        Object[] args = { "arg1", "arg2" };

        Invocation invocation = new Invocation(mockObject, method, args, 1, null);

        assertArrayEquals(args, invocation.getArguments());
    }

    @Test
    public void testEquals() {
        // Test case for checking equality of invocations
        Object mockObject1 = mock(Object.class);
        MockitoMethod method1 = mock(MockitoMethod.class);
        Object[] args1 = { "arg1", "arg2" };

        Object mockObject2 = mock(Object.class);
        MockitoMethod method2 = mock(MockitoMethod.class);
        Object[] args2 = { "arg1", "arg2" };

        Invocation invocation1 = new Invocation(mockObject1, method1, args1, 1, null);
        Invocation invocation2 = new Invocation(mockObject2, method2, args2, 1, null);

        assertTrue(invocation1.equals(invocation2));
    }

    // Add more test cases for other methods of the Invocation class

}