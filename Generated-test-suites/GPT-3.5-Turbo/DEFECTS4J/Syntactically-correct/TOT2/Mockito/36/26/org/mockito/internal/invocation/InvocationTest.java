import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class InvocationTest {

    @Test
    public void testGetMock() {
        // Arrange
        Object mockObject = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[] { "arg1", "arg2" };
        Invocation invocation = new Invocation(mockObject, method, args, 1, new RealMethod());

        // Act
        Object result = invocation.getMock();

        // Assert
        assertEquals(mockObject, result);
    }

    @Test
    public void testGetSequenceNumber() {
        // Arrange
        Object mockObject = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[] { "arg1", "arg2" };
        Invocation invocation = new Invocation(mockObject, method, args, 1, new RealMethod());

        // Act
        Integer result = invocation.getSequenceNumber();

        // Assert
        assertEquals(Integer.valueOf(1), result);
    }

    @Test
    public void testEquals() {
        // Arrange
        Object mockObject1 = new Object();
        Object mockObject2 = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args1 = new Object[] { "arg1", "arg2" };
        Object[] args2 = new Object[] { "arg1", "arg2" };
        Invocation invocation1 = new Invocation(mockObject1, method, args1, 1, new RealMethod());
        Invocation invocation2 = new Invocation(mockObject2, method, args2, 1, new RealMethod());

        // Act
        boolean result = invocation1.equals(invocation2);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testCallRealMethod() throws Throwable {
        // Arrange
        Object mockObject = new Object();
        MockitoMethod method = new MockitoMethod();
        Object[] args = new Object[] { "arg1", "arg2" };
        Invocation invocation = new Invocation(mockObject, method, args, 1, new RealMethod());

        // Assert
        assertThrows(Throwable.class, () -> invocation.callRealMethod());
    }
}