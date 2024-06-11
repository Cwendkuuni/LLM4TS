import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class ReturnsSmartNullsTest {

    private ReturnsSmartNulls returnsSmartNulls;

    @Before
    public void setup() {
        returnsSmartNulls = new ReturnsSmartNulls();
    }

    @Test
    public void testAnswer_ordinaryReturnValue() throws Throwable {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.getMethod().setReturnType(String.class);
        Mockito.when(delegate.answer(invocation)).thenReturn("test");
        
        Object result = returnsSmartNulls.answer(invocation);
        
        assertEquals("test", result);
    }

    @Test
    public void testAnswer_nullReturnValue() throws Throwable {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.getMethod().setReturnType(String.class);
        Mockito.when(delegate.answer(invocation)).thenReturn(null);
        
        Object result = returnsSmartNulls.answer(invocation);
        
        assertNull(result);
    }

    @Test
    public void testAnswer_mockableObjectReturnValue() throws Throwable {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.getMethod().setReturnType(ThrowsNullPointerException.class);
        Mockito.when(delegate.answer(invocation)).thenReturn(null);
        
        Object result = returnsSmartNulls.answer(invocation);
        
        assertNull(result);
    }
   
    @Test
    public void testAnswer_nonMockableObjectReturnValue() throws Throwable {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.getMethod().setReturnType(FinalClass.class);
        Mockito.when(delegate.answer(invocation)).thenReturn(null);
        
        Object result = returnsSmartNulls.answer(invocation);
        
        assertNull(result);
    }

    @Test
    public void testAnswer_toStringMethod() throws Throwable {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.getMethod().setReturnType(String.class);
        Method toStringMethod = Object.class.getDeclaredMethod("toString");
        Mockito.when(delegate.answer(invocation)).thenReturn(null);
        Mockito.when(new ObjectMethodsGuru().isToString(toStringMethod)).thenReturn(true);
        Mockito.when(formatMethodCall()).thenReturn("toString()");
        
        Object result = returnsSmartNulls.answer(invocation);
        
        assertEquals("SmartNull returned by unstubbed toString() method on mock", result);
    }

    @Test(expected = Reporter.smartNullPointerException.class)
    public void testAnswer_throwException() throws Throwable {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.getMethod().setReturnType(byte.class);
        Mockito.when(delegate.answer(invocation)).thenReturn(null);
        Mockito.when(location()).thenReturn(new Location());
        
        Object result = returnsSmartNulls.answer(invocation);
    }
  
}