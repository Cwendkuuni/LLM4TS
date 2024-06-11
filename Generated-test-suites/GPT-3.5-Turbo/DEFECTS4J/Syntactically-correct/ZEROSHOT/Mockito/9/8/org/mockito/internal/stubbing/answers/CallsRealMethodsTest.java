import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class CallsRealMethodsTest {
    
    @Test
    public void testAnswer() throws Throwable {
        CallsRealMethods callsRealMethods = new CallsRealMethods();
        Invocation onMock = mock(InvocationOnMock.class);
        
        when(onMock.callRealMethod()).thenReturn("test");
        
        Object result = callsRealMethods.answer(onMock);
        
        assertEquals("test", result);
    }
}