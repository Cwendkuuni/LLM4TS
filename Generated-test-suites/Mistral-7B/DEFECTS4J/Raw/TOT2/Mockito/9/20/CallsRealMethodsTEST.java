import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class CallsRealMethodsTest {

    @Test
    public void testCallRealMethod() {
        CallsRealMethods callRealMethods = new CallsRealMethods();
        Object originalObject = new Object();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getTarget()).thenReturn(originalObject);

        Object result = callRealMethods.answer(invocation);
        assertEquals(originalObject, result);
    }

    @Test(expected = NullPointerException.class)
    public void testNullInvocation() {
        CallsRealMethods callRealMethods = new CallsRealMethods();
        InvocationOnMock invocation = null;
        callRealMethods.answer(invocation);
    }

    @Test
    public void testThrowsException() throws Throwable {
        CallsRealMethods callRealMethods = new CallsRealMethods();
        Object originalObject = new Object();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        when(invocation.getTarget()).thenReturn(originalObject);
        when(originalObject.toString()).thenThrow(new RuntimeException("Test exception"));

        try {
            callRealMethods.answer(invocation);
            fail("Expected exception not thrown");
        } catch (Throwable e) {
            // Expected exception
            assertEquals("Test exception", e.getMessage());
        }
    }

    @Test
    public void testInvocationWithDifferentTarget() {
        CallsRealMethods callRealMethods = new CallsRealMethods();
        Object originalObject1 = new Object();
        Object originalObject2 = new Object();
        InvocationOnMock invocation1 = Mockito.mock(InvocationOnMock.class);
        InvocationOnMock invocation2 = Mockito.mock(InvocationOnMock.class);
        when(invocation1.getTarget()).thenReturn(originalObject1);
        when(invocation2.getTarget()).thenReturn(originalObject2);

        Object result1 = callRealMethods.answer(invocation1);
        assertEquals(originalObject1, result1);

        Object result2 = callRealMethods.answer(invocation2);
        assertEquals(originalObject2, result2);
    }
}