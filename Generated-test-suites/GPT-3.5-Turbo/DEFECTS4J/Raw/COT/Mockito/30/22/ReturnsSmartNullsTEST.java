import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class ReturnsSmartNullsTest {
    
    @Test
    public void testAnswer() {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        try {
            returnsSmartNulls.answer(invocation);
        } catch (Throwable e) {
            fail("Exception should not be thrown");
        }
    }
}