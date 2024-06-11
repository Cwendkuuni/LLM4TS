import org.junit.Test;
import org.junit.Assert;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();
        
        // Test invocation with default return value
        InvocationOnMock invocationWithDefaultReturn = Mockito.mock(InvocationOnMock.class);
        Object defaultReturnValue = returnsSmartNulls.answer(invocationWithDefaultReturn);
        Assert.assertNull("Default return value should be null", defaultReturnValue);
        
        // Test invocation with mockable return type
        InvocationOnMock invocationWithMockableType = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocationWithMockableType.getMethod().getReturnType()).thenReturn(Object.class);
        Object returnValueWithMockableType = returnsSmartNulls.answer(invocationWithMockableType);
        Assert.assertNotNull("Return value with mockable type should not be null", returnValueWithMockableType);
        
        // Test invocation with non-mockable return type
        InvocationOnMock invocationWithNonMockableType = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocationWithNonMockableType.getMethod().getReturnType()).thenReturn(String.class);
        Object returnValueWithNonMockableType = returnsSmartNulls.answer(invocationWithNonMockableType);
        Assert.assertNull("Return value with non-mockable type should be null", returnValueWithNonMockableType);
    }
}