import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class InvocationTest {

    @Test
    public void testGetMethod() {
        // Create a mock object
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object mockObject = new Object();
        Object[] args = new Object[]{1, "test"};
        RealMethod realMethod = mock(RealMethod.class);
        Invocation invocation = new Invocation(mockObject, mockitoMethod, args, 1, realMethod);

        // Test the getMethod() method
        assertEquals(mockitoMethod, invocation.getMethod());
    }

    @Test
    public void testGetArguments() {
        // Create a mock object
        MockitoMethod mockitoMethod = mock(MockitoMethod.class);
        Object mockObject = new Object();
        Object[] args = new Object[]{1, "test"};
        RealMethod realMethod = mock(RealMethod.class);
        Invocation invocation = new Invocation(mockObject, mockitoMethod, args, 1, realMethod);

        // Test the getArguments() method
        assertArrayEquals(args, invocation.getArguments());
    }

    // Add more test cases for other public methods

}