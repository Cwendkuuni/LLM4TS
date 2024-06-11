
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsSmartNullsTest {

    @Test
    public void testAnswer() throws Throwable {
        ReturnsSmartNulls returnsSmartNulls = new ReturnsSmartNulls();

        // Test 1: Basic functionality test
        InvocationOnMock invocation1 = Mockito.mock(InvocationOnMock.class);
        Object returnValue1 = returnsSmartNulls.answer(invocation1);
        Assert.assertNull(returnValue1);

        // Test 2: Test that delegate answer is called first
        Answer<Object> delegate = Mockito.mock(Answer.class);
        Mockito.when(delegate.answer(Mockito.any(InvocationOnMock.class))).thenReturn("test");
        ReturnsSmartNulls returnsSmartNulls2 = new ReturnsSmartNulls(delegate);
        InvocationOnMock invocation2 = Mockito.mock(InvocationOnMock.class);
        Object returnValue2 = returnsSmartNulls2.answer(invocation2);
        Assert.assertEquals("test", returnValue2);

        // Test 3: Test that default return value is returned if delegate answer returns null
        Answer<Object> delegate2 = Mockito.mock(Answer.class);
        Mockito.when(delegate2.answer(Mockito.any(InvocationOnMock.class))).thenReturn(null);
        ReturnsSmartNulls returnsSmartNulls3 = new ReturnsSmartNulls(delegate2);
        InvocationOnMock invocation3 = Mockito.mock(InvocationOnMock.class);
        Object returnValue3 = returnsSmartNulls3.answer(invocation3);
        Assert.assertNull(returnValue3);

        // Test 4: Test that mockito can create a SmartNull for mockable return type
        InvocationOnMock invocation4 = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation4.getMethod().getReturnType()).thenReturn(String.class);
        Object returnValue4 = returnsSmartNulls.answer(invocation4);
        Assert.assertNotNull(returnValue4);
        Assert.assertTrue(returnValue4 instanceof String);
    }
}
