import org.junit.jupiter.api.Test;
import org.mockito.internal.invocation.Invocation;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InvocationTest {

    @Test
    public void testInvocationConstructor() {
        Object mock = mock(Object.class);
        MockitoMethod mockitoMethod = mockitoMethod(String.class, "testMethod");
        Object[] args = new Object[]{1, "test"};
        Invocation invocation = new Invocation(mock, mockitoMethod, args, 1, mockitoMethod.getRealMethod());

        assertEquals(mock, invocation.getMock());
        assertEquals(mockitoMethod, invocation.getMethod());
        assertEquals(args, invocation.getArguments());
        assertEquals(1, invocation.getSequenceNumber());
        assertEquals(mockitoMethod.getRealMethod(), invocation.realMethod);
    }
}