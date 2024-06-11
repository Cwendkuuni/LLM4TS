import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerToString() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        
        assertTrue(answer.answer(invocation) instanceof String);
    }
    
    @Test
    public void testAnswerCompareTo() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        
        assertTrue(answer.answer(invocation) instanceof Integer);
        assertEquals(1, (int) answer.answer(invocation));
    }
    
    @Test
    public void testAnswerReturnValueForPrimitive() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        
        assertEquals(0, (int) answer.returnValueFor(int.class));
        assertEquals(0L, (long) answer.returnValueFor(long.class));
        assertEquals(0.0, (double) answer.returnValueFor(double.class), 0.001);
        assertEquals(0.0f, (float) answer.returnValueFor(float.class), 0.001);
        assertEquals(false, (boolean) answer.returnValueFor(boolean.class));
        assertEquals('\u0000', (char) answer.returnValueFor(char.class));
        
        assertEquals(Integer.valueOf(0), answer.returnValueFor(Integer.class));
        assertEquals(Long.valueOf(0L), answer.returnValueFor(Long.class));
        assertEquals(Double.valueOf(0.0), answer.returnValueFor(Double.class));
        assertEquals(Float.valueOf(0.0f), answer.returnValueFor(Float.class));
        assertEquals(Boolean.FALSE, answer.returnValueFor(Boolean.class));
        assertEquals(Character.valueOf('\u0000'), answer.returnValueFor(Character.class));
    }
    
    @Test
    public void testAnswerReturnValueForCollection() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        
        assertTrue(answer.returnValueFor(Collection.class) instanceof LinkedList);
        assertTrue(answer.returnValueFor(Set.class) instanceof HashSet);
        assertTrue(answer.returnValueFor(HashSet.class) instanceof HashSet);
        assertTrue(answer.returnValueFor(SortedSet.class) instanceof TreeSet);
        assertTrue(answer.returnValueFor(TreeSet.class) instanceof TreeSet);
        assertTrue(answer.returnValueFor(LinkedHashSet.class) instanceof LinkedHashSet);
        assertTrue(answer.returnValueFor(List.class) instanceof LinkedList);
        assertTrue(answer.returnValueFor(LinkedList.class) instanceof LinkedList);
        assertTrue(answer.returnValueFor(ArrayList.class) instanceof ArrayList);
        assertTrue(answer.returnValueFor(Map.class) instanceof HashMap);
        assertTrue(answer.returnValueFor(HashMap.class) instanceof HashMap);
        assertTrue(answer.returnValueFor(SortedMap.class) instanceof TreeMap);
        assertTrue(answer.returnValueFor(TreeMap.class) instanceof TreeMap);
        assertTrue(answer.returnValueFor(LinkedHashMap.class) instanceof LinkedHashMap);
    }
    
    @Test
    public void testAnswerReturnValueForOtherTypes() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        
        assertNull(answer.returnValueFor(Object.class));
        assertNull(answer.returnValueFor(String.class));
        assertNull(answer.returnValueFor(Exception.class));
    }
    
}