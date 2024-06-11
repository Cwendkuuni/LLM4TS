import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer_WhenReturnTypeIsMockable_ShouldReturnNull() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ExampleClass.class.getMethod("mockableMethod"));
        Mockito.when(invocation.getArguments()).thenReturn(new Object[0]);

        Object result = returnsSmartNulls.answer(invocation);

        assertNull(result);
    }

    @Test
    public void testAnswer_WhenReturnTypeIsNotMockable_ShouldReturnSmartNull() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ExampleClass.class.getMethod("notMockableMethod"));
        Mockito.when(invocation.getArguments()).thenReturn(new Object[0]);

        Object result = returnsSmartNulls.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof ExampleClass.ThrowingInterceptor);
    }

    @Test
    public void testAnswer_WhenToStringMethod_ShouldReturnSmartNull() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));
        Mockito.when(invocation.getArguments()).thenReturn(new Object[0]);

        Object result = returnsSmartNulls.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof String);
        assertEquals("SmartNull returned by unstubbed toString method on mock", result.toString());
    }
}

class ExampleClass {

    public void mockableMethod() {
    }

    public final void notMockableMethod() {
    }
}