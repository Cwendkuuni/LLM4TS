import static org.junit.Assert.*;
import org.junit.Test;

public class ReturnsEmptyValuesTest {
    
    @Test
    public void testAnswer_forToStringMethod() {
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        when(invocation.getMethod()).thenReturn(ObjectMethodsGuru.class.getMethod("toString"));
        
        Object result = returnValue.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof String);
    }
    
    @Test
    public void testAnswer_forCompareToMethod() {
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        when(invocation.getMethod()).thenReturn(ObjectMethodsGuru.class.getMethod("compareTo"));
        
        Object result = returnValue.answer(invocation);
        
        assertNotNull(result);
        assertEquals(1, (int) result);
    }
    
    @Test
    public void testAnswer_forCollectionReturnType() {
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class));
        
        Object result = returnValue.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof LinkedList);
    }
    
    // Add more test cases for edge cases and exception handling scenarios
    
}