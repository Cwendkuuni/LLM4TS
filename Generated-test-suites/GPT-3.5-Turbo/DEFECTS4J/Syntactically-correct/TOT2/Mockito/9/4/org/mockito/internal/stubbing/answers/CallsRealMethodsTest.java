import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.internal.stubbing.answers.CallsRealMethods;

public class CallsRealMethodsTest {

    @Test
    public void testAnswer() throws Throwable {
        CallsRealMethods callsRealMethods = new CallsRealMethods();
        InvocationOnMock invocation = new InvocationOnMock(null, null, null);
        
        assertEquals(null, callsRealMethods.answer(invocation));
    }
    
    @Test
    public void testAnswerWithException() throws Throwable {
        CallsRealMethods callsRealMethods = new CallsRealMethods();
        InvocationOnMock invocation = new InvocationOnMock(null, null, null) {
            @Override
            public Object callRealMethod() throws Throwable {
                throw new NullPointerException("Test exception");
            }
        };
        
        try {
            callsRealMethods.answer(invocation);
            fail("Exception not thrown");
        } catch (NullPointerException e) {
            assertEquals("Test exception", e.getMessage());
        }
    }
}