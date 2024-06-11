import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.invocation.InvocationOnMock;
import static org.mockito.Mockito.*;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer_ToString() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof String);
        assertTrue(((String) result).contains("Mock for"));
    }

    @Test
    public void testAnswer_CompareToMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getName()).thenReturn("compareTo");
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof Integer);
        assertEquals(1, (int) result);
    }

    @Test
    public void testAnswer_PrimitiveReturnType() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(int.class);
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof Integer);
        assertEquals(0, (int) result);
    }

    @Test
    public void testReturnValueFor_CollectionType() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        
        Object result = returnsEmptyValues.returnValueFor(Collection.class);
        
        assertNotNull(result);
        assertTrue(result instanceof LinkedList);
        assertTrue(((LinkedList) result).isEmpty());
    }

    @Test
    public void testReturnValueFor_SetType() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        
        Object result = returnsEmptyValues.returnValueFor(Set.class);
        
        assertNotNull(result);
        assertTrue(result instanceof HashSet);
        assertTrue(((HashSet) result).isEmpty());
    }

    // Add more test cases for other public methods and edge cases

}