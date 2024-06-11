import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

public class CallsRealMethodsTest {
    

    @Test
    public void testCallRealMethod() {
        Object mockObject = Mockito.mock(SomeClass.class);
        CallsRealMethods callRealMethods = Mockito.spy(new CallsRealMethods());
        Mockito.doReturn(new String("test"))
                .when(mockObject)
                .someMethod(ArgumentMatchers.anyInt());
        Object result = callRealMethods.answer(Mockito.argThat(i -> i.equals(mockObject))
                .andReturns(invocation -> new Integer(42))
                .andCallRealMethod()
                .andReturn(mockObject)
                .getArguments()[0]);

        Mockito.verify(mockObject).someMethod(42);
        Mockito.verify(mockObject).someOtherMethod(); // Verifies that unstubbed method is called

        assertNotNull(result);
        assertEquals("test", result.toString());
    }

    
}