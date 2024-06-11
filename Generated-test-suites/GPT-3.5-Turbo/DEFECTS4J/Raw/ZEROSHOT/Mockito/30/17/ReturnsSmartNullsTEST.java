import org.junit.Test;
import org.mockito.Mockito;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        // Test default return value
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object result = returnsSmartNulls.answer(invocation);
        assert result == null;

        // Test return value when delegate answer returns non-null
        Mockito.when(returnsSmartNulls.delegate.answer(Mockito.any(InvocationOnMock.class))).thenReturn("test");
        result = returnsSmartNulls.answer(invocation);
        assert result.equals("test");

        // Test return value when delegate answer returns null
        Mockito.when(returnsSmartNulls.delegate.answer(Mockito.any(InvocationOnMock.class))).thenReturn(null);

        // Test return value when return type is mockable
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(Object.class);
        result = returnsSmartNulls.answer(invocation);
        assert result.getClass().getSimpleName().equals("CGLIBProxy");

        // Test return value when return type is not mockable
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(String.class);
        result = returnsSmartNulls.answer(invocation);
        assert result == null;
    }
}