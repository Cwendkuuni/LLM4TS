import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerToString() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        assertNotNull(emptyValues.answer(mock(InvocationOnMock.class)));
    }
    
    @Test
    public void testAnswerCompareTo() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        Object result = emptyValues.answer(mock(InvocationOnMock.class));
        assertEquals(1, result);
    }
    
    @Test
    public void testReturnValueForPrimitive() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        assertEquals(0, emptyValues.returnValueFor(int.class));
    }
    
    @Test
    public void testReturnValueForCollection() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        assertTrue(emptyValues.returnValueFor(Collection.class) instanceof LinkedList);
    }
    
    @Test
    public void testReturnValueForSet() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        assertTrue(emptyValues.returnValueFor(Set.class) instanceof HashSet);
    }
    
    @Test
    public void testReturnValueForMap() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        assertTrue(emptyValues.returnValueFor(Map.class) instanceof HashMap);
    }

}