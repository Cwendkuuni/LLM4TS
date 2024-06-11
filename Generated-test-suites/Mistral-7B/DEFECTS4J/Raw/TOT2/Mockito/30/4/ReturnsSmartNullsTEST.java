import org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() {
        ReturnsSmartNulls returnsSmartNulls = Mockito.spy(new ReturnsSmartNulls());
        InvocationOnMock invocation = Mockito.mock(Object.class);
        Object result = returnsSmartNulls.answer(invocation);
        assertNull(result);
    }
}