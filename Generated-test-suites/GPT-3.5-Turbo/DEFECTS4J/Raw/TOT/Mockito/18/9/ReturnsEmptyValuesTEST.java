import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerToString() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        String result = (String)returnsEmptyValues.answer(InvocationOnMock);
        assertNotNull(result);
        assertTrue(result.contains("Mock for"));
        assertTrue(result.contains("hashCode"));
    }
    
    @Test
    public void testAnswerCompareTo() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        int result = (int)returnsEmptyValues.answer(InvocationOnMock);
        assertTrue(result == 0 || result == 1);
    }
    
    @Test
    public void testAnswerPrimitiveReturn() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Integer result = (Integer)returnsEmptyValues.answer(InvocationOnMock);
        assertNotNull(result);
        assertEquals(0, result.intValue());
    }
    
    @Test
    public void testAnswerWrapperReturn() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        int result = (int)returnsEmptyValues.answer(InvocationOnMock);
        assertEquals(0, result);
    }
    
    @Test
    public void testAnswerCollectionReturn() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Collection<Object> result = (Collection<Object>)returnsEmptyValues.answer(InvocationOnMock);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testAnswerSetReturn() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Set<Object> result = (Set<Object>)returnsEmptyValues.answer(InvocationOnMock);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testAnswerHashSetReturn() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        HashSet<Object> result = (HashSet<Object>)returnsEmptyValues.answer(InvocationOnMock);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testAnswerSortedSetReturn() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        SortedSet<Object> result = (SortedSet<Object>)returnsEmptyValues.answer(InvocationOnMock);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testAnswerTreeSetReturn() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        TreeSet<Object> result = (TreeSet<Object>)returnsEmptyValues.answer(InvocationOnMock);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testAnswerLinkedHashSetReturn() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        LinkedHashSet<Object> result = (LinkedHashSet<Object>)returnsEmptyValues.answer(InvocationOnMock);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testAnswerListReturn() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        List<Object> result = (List<Object>)returnsEmptyValues.answer(InvocationOnMock);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testAnswerLinkedListReturn() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        LinkedList<Object> result = (LinkedList<Object>)returnsEmptyValues.answer(InvocationOnMock);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testAnswerArrayListReturn() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        ArrayList<Object> result = (ArrayList<Object>)returnsEmptyValues.answer(InvocationOnMock);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testAnswerMapReturn() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Map<Object, Object> result = (Map<Object, Object>)returnsEmptyValues.answer(InvocationOnMock);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testAnswerHashMapReturn() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        HashMap<Object, Object> result = (HashMap<Object, Object>)returnsEmptyValues.answer(InvocationOnMock);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testAnswerSortedMapReturn() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        SortedMap<Object, Object> result = (SortedMap<Object, Object>)returnsEmptyValues.answer(InvocationOnMock);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testAnswerTreeMapReturn() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        TreeMap<Object, Object> result = (TreeMap<Object, Object>)returnsEmptyValues.answer(InvocationOnMock);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testAnswerLinkedHashMapReturn() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        LinkedHashMap<Object, Object> result = (LinkedHashMap<Object, Object>)returnsEmptyValues.answer(InvocationOnMock);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
    
    @Test
    public void testAnswerOtherReturn() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object result = returnsEmptyValues.answer(InvocationOnMock);
        assertNull(result);
    }
}