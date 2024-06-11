import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

public class CallsRealMethodsTest {
    

    @Test
    public void testCallsRealMethod() {
        Object realObject = new Object() {
            void methodToTest() {
                System.out.println("Real method called.");
            }
        };

        CallsRealMethods answer = Mockito.answer(new CallsRealMethods());
        Mockito.doAnswer(answer).when(realObject).methodToTest();

        realObject.methodToTest();

        Mockito.verify(realObject, Mockito.times(1)).methodToTest();
        Mockito.verifyNoMoreInteractions(realObject);
        System.out.println("Real method was called.");
    }

    
}