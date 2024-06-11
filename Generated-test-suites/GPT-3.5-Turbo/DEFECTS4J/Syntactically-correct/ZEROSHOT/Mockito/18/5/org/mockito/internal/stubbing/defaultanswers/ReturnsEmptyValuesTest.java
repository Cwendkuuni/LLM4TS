import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;

public class ReturnsEmptyValuesTest {

    ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

    @Test
    public void testAnswer_toStringMethod() {
        Object mock = new Object();
        assertNotNull(mock.toString());
    }

    @Test
    public void testAnswer_compareToMethod() {
        Object mock = new Object();
        assertEquals(1, mock.compareTo(mock));
    }

    @Test
    public void testAnswer_primitiveReturnType() {
        int defaultValue = (int) returnsEmptyValues.returnValueFor(int.class);
        assertEquals(0, defaultValue);
    }

    @Test
    public void testAnswer_primitiveWrapperReturnType() {
        Integer defaultValue = (Integer) returnsEmptyValues.returnValueFor(Integer.class);
        assertEquals(0, defaultValue.intValue());
    }

    @Test
    public void testAnswer_collectionReturnType() {
        Object defaultValue = returnsEmptyValues.returnValueFor(Collection.class);
        assertTrue(defaultValue instanceof LinkedList);
        assertTrue(((LinkedList) defaultValue).isEmpty());
    }

    @Test
    public void testAnswer_setReturnType() {
        Object defaultValue = returnsEmptyValues.returnValueFor(Set.class);
        assertTrue(defaultValue instanceof HashSet);
        assertTrue(((HashSet) defaultValue).isEmpty());
    }

    @Test
    public void testAnswer_hashSetReturnType() {
        Object defaultValue = returnsEmptyValues.returnValueFor(HashSet.class);
        assertTrue(defaultValue instanceof HashSet);
        assertTrue(((HashSet) defaultValue).isEmpty());
    }

    @Test
    public void testAnswer_sortedSetReturnType() {
        Object defaultValue = returnsEmptyValues.returnValueFor(SortedSet.class);
        assertTrue(defaultValue instanceof TreeSet);
        assertTrue(((TreeSet) defaultValue).isEmpty());
    }

    @Test
    public void testAnswer_treeSetReturnType() {
        Object defaultValue = returnsEmptyValues.returnValueFor(TreeSet.class);
        assertTrue(defaultValue instanceof TreeSet);
        assertTrue(((TreeSet) defaultValue).isEmpty());
    }

    @Test
    public void testAnswer_linkedHashSetReturnType() {
        Object defaultValue = returnsEmptyValues.returnValueFor(LinkedHashSet.class);
        assertTrue(defaultValue instanceof LinkedHashSet);
        assertTrue(((LinkedHashSet) defaultValue).isEmpty());
    }

    @Test
    public void testAnswer_listReturnType() {
        Object defaultValue = returnsEmptyValues.returnValueFor(List.class);
        assertTrue(defaultValue instanceof LinkedList);
        assertTrue(((LinkedList) defaultValue).isEmpty());
    }

    @Test
    public void testAnswer_linkedListReturnType() {
        Object defaultValue = returnsEmptyValues.returnValueFor(LinkedList.class);
        assertTrue(defaultValue instanceof LinkedList);
        assertTrue(((LinkedList) defaultValue).isEmpty());
    }

    @Test
    public void testAnswer_arrayListReturnType() {
        Object defaultValue = returnsEmptyValues.returnValueFor(ArrayList.class);
        assertTrue(defaultValue instanceof ArrayList);
        assertTrue(((ArrayList) defaultValue).isEmpty());
    }

    @Test
    public void testAnswer_mapReturnType() {
        Object defaultValue = returnsEmptyValues.returnValueFor(Map.class);
        assertTrue(defaultValue instanceof HashMap);
        assertTrue(((HashMap) defaultValue).isEmpty());
    }

    @Test
    public void testAnswer_hashMapReturnType() {
        Object defaultValue = returnsEmptyValues.returnValueFor(HashMap.class);
        assertTrue(defaultValue instanceof HashMap);
        assertTrue(((HashMap) defaultValue).isEmpty());
    }

    @Test
    public void testAnswer_sortedMapReturnType() {
        Object defaultValue = returnsEmptyValues.returnValueFor(SortedMap.class);
        assertTrue(defaultValue instanceof TreeMap);
        assertTrue(((TreeMap) defaultValue).isEmpty());
    }

    @Test
    public void testAnswer_treeMapReturnType() {
        Object defaultValue = returnsEmptyValues.returnValueFor(TreeMap.class);
        assertTrue(defaultValue instanceof TreeMap);
        assertTrue(((TreeMap) defaultValue).isEmpty());
    }

    @Test
    public void testAnswer_linkedHashMapReturnType() {
        Object defaultValue = returnsEmptyValues.returnValueFor(LinkedHashMap.class);
        assertTrue(defaultValue instanceof LinkedHashMap);
        assertTrue(((LinkedHashMap) defaultValue).isEmpty());
    }

    @Test
    public void testAnswer_otherReturnType() {
        Object defaultValue = returnsEmptyValues.returnValueFor(String.class);
        assertNull(defaultValue);
    }
}