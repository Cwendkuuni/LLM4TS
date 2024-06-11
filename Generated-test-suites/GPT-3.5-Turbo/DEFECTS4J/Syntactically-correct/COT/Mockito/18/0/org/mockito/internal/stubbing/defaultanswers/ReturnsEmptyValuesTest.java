
import org.junit.Test;
import static org.junit.Assert.*;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerToString() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        String result = (String) emptyValues.answer(new InvocationOnMock());
        
        String expected = "Mock for Object, hashCode: 0";
        assertEquals(expected, result);
    }

    @Test
    public void testAnswerCompareTo() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        int result1 = (int) emptyValues.answer(new InvocationOnMock());
        int result2 = (int) emptyValues.answer(new InvocationOnMock());
        
        assertEquals(1, result1);
        assertEquals(0, result2);
    }
    
    @Test
    public void testAnswerPrimitiveReturn() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        int result1 = (int) emptyValues.answer(new InvocationOnMock());
        double result2 = (double) emptyValues.answer(new InvocationOnMock());
        
        assertEquals(0, result1);
        assertEquals(0.0, result2, 0.0001);
    }
    
    @Test
    public void testAnswerCollectionReturn() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        Collection<Object> result = (Collection<Object>) emptyValues.answer(new InvocationOnMock());
        
        assertTrue(result instanceof LinkedList);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testAnswerSetReturn() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        Set<Object> result1 = (Set<Object>) emptyValues.answer(new InvocationOnMock());
        Set<Object> result2 = (Set<Object>) emptyValues.answer(new InvocationOnMock());
        
        assertTrue(result1 instanceof HashSet);
        assertTrue(result2 instanceof HashSet);
        assertTrue(result1.isEmpty());
        assertTrue(result2.isEmpty());
    }
    
    @Test
    public void testAnswerSortedSetReturn() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        SortedSet<Object> result1 = (SortedSet<Object>) emptyValues.answer(new InvocationOnMock());
        SortedSet<Object> result2 = (SortedSet<Object>) emptyValues.answer(new InvocationOnMock());
        
        assertTrue(result1 instanceof TreeSet);
        assertTrue(result2 instanceof TreeSet);
        assertTrue(result1.isEmpty());
        assertTrue(result2.isEmpty());
    }
    
    @Test
    public void testAnswerLinkedHashSetReturn() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        LinkedHashSet<Object> result = (LinkedHashSet<Object>) emptyValues.answer(new InvocationOnMock());
        
        assertTrue(result instanceof LinkedHashSet);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testAnswerListReturn() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        List<Object> result = (List<Object>) emptyValues.answer(new InvocationOnMock());
        
        assertTrue(result instanceof LinkedList);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testAnswerLinkedListReturn() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        LinkedList<Object> result = (LinkedList<Object>) emptyValues.answer(new InvocationOnMock());
        
        assertTrue(result instanceof LinkedList);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testAnswerArrayListReturn() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        ArrayList<Object> result = (ArrayList<Object>) emptyValues.answer(new InvocationOnMock());
        
        assertTrue(result instanceof ArrayList);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testAnswerMapReturn() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        Map<Object, Object> result = (Map<Object, Object>) emptyValues.answer(new InvocationOnMock());
        
        assertTrue(result instanceof HashMap);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testAnswerHashMapReturn() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        HashMap<Object, Object> result = (HashMap<Object, Object>) emptyValues.answer(new InvocationOnMock());
        
        assertTrue(result instanceof HashMap);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testAnswerSortedMapReturn() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        SortedMap<Object, Object> result = (SortedMap<Object, Object>) emptyValues.answer(new InvocationOnMock());
        
        assertTrue(result instanceof TreeMap);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testAnswerTreeMapReturn() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        TreeMap<Object, Object> result = (TreeMap<Object, Object>) emptyValues.answer(new InvocationOnMock());
        
        assertTrue(result instanceof TreeMap);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testAnswerLinkedHashMapReturn() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        LinkedHashMap<Object, Object> result = (LinkedHashMap<Object, Object>) emptyValues.answer(new InvocationOnMock());
        
        assertTrue(result instanceof LinkedHashMap);
        assertTrue(result.isEmpty());
    }
    
}
