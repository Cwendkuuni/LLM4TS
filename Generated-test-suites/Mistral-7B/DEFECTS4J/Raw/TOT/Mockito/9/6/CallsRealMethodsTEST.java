import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class CallsRealMethodsTest {
    
    private CallsRealMethods callsRealMethods;
    private Object realObject;

    @Before
    public void setup() {
        realObject = mock(Object.class);
        callsRealMethods = new CallsRealMethods();
    }

    @Test
    public void testCallRealMethod() {
        Object result = callsRealMethods.answer(Mockito.argThat(inv -> inv.getTarget() == realObject));
        verify(realObject).someMethod(); // Replace 'someMethod' with a real method of the mocked object
        assertSame(result, realObject.someMethod());
    }
    
}