import org.junit.Test;
import org.junit.Assert;
import org.mockito.internal.stubbing.defaultanswers.ReturnsSmartNulls;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() throws Throwable {
        // Initialize the class under test
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        // Verify that the answer returns null for unstubbed methods
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Object result = returnsSmartNulls.answer(invocation);
        Assert.assertNull(result);
    }
    
    @Test
    public void testAnswer_withDefaultReturnValue() throws Throwable {
        // Initialize the class under test
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        
        // Create a mock InvocationOnMock with a default return value
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Answer<Object> defaultAnswer = Mockito.mock(Answer.class);
        Mockito.when(defaultAnswer.answer(invocation)).thenReturn("default");
        
        // Verify that the answer uses the default return value when available
        Object result = returnsSmartNulls.answer(invocation);
        Assert.assertEquals("default", result);
    }
    
    @Test
    public void testAnswer_withFinalReturnType() throws Throwable {
        // Initialize the class under test
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        
        // Create a mock InvocationOnMock with a final return type
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Answer<Object> defaultAnswer = Mockito.mock(Answer.class);
        Mockito.when(defaultAnswer.answer(invocation)).thenReturn(null);
        Mockito.when(invocation.getMethod().getReturnType()).thenReturn(Integer.class);
        
        // Verify that the answer returns null for final return types
        Object result = returnsSmartNulls.answer(invocation);
        Assert.assertNull(result);
    }
    
    @Test
    public void testAnswer_withToStringMethod() throws Throwable {
        // Initialize the class under test
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        
        // Create a mock InvocationOnMock with a toString method
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Answer<Object> defaultAnswer = Mockito.mock(Answer.class);
        Mockito.when(defaultAnswer.answer(invocation)).thenReturn(null);
        Mockito.when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));
        
        // Verify that the answer returns a SmartNull for the toString method
        Object result = returnsSmartNulls.answer(invocation);
        Assert.assertEquals("SmartNull returned by unstubbed toString() method on mock", result);
    }

}