import org.junit.Test;
import static org.junit.Assert.*;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer_toString() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        String result = emptyValues.answer(null);
        assertNotNull(result);
        assertTrue(result.startsWith("Mock for"));
        assertTrue(result.contains("hashCode"));
    }

    @Test
    public void testAnswer_compareTo() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        Object mock = new Object();
        int result1 = emptyValues.answer(null);
        int result2 = emptyValues.answer(invocation);
        assertEquals(0, result1);
        assertNotEquals(0, result2);
    }

    @Test
    public void testReturnValueFor_primitiveType() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        Object result1 = emptyValues.returnValueFor(int.class);
        Object result2 = emptyValues.returnValueFor(Integer.class);
        assertEquals(0, result1);
        assertEquals(0, result2);
    }

    @Test
    public void testReturnValueFor_CollectionTypes() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        Object result1 = emptyValues.returnValueFor(Collection.class);
        Object result2 = emptyValues.returnValueFor(Set.class);
        Object result3 = emptyValues.returnValueFor(HashSet.class);
        Object result4 = emptyValues.returnValueFor(SortedSet.class);
        Object result5 = emptyValues.returnValueFor(TreeSet.class);
        Object result6 = emptyValues.returnValueFor(LinkedHashSet.class);
        Object result7 = emptyValues.returnValueFor(List.class);
        Object result8 = emptyValues.returnValueFor(LinkedList.class);
        Object result9 = emptyValues.returnValueFor(ArrayList.class);

        assertTrue(result1 instanceof LinkedList);
        assertTrue(result2 instanceof HashSet);
        assertTrue(result3 instanceof HashSet);
        assertTrue(result4 instanceof TreeSet);
        assertTrue(result5 instanceof TreeSet);
        assertTrue(result6 instanceof LinkedHashSet);
        assertTrue(result7 instanceof LinkedList);
        assertTrue(result8 instanceof LinkedList);
        assertTrue(result9 instanceof ArrayList);
    }

    @Test
    public void testReturnValueFor_MapTypes() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        Object result1 = emptyValues.returnValueFor(Map.class);
        Object result2 = emptyValues.returnValueFor(HashMap.class);
        Object result3 = emptyValues.returnValueFor(SortedMap.class);
        Object result4 = emptyValues.returnValueFor(TreeMap.class);
        Object result5 = emptyValues.returnValueFor(LinkedHashMap.class);

        assertTrue(result1 instanceof HashMap);
        assertTrue(result2 instanceof HashMap);
        assertTrue(result3 instanceof TreeMap);
        assertTrue(result4 instanceof TreeMap);
        assertTrue(result5 instanceof LinkedHashMap);
    }
}