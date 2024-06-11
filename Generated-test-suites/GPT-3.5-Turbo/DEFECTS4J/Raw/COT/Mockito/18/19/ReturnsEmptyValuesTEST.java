
import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;

import java.util.*;

import static org.junit.Assert.*;

public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringMethod() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        Object mock = new Object();
        String result = (String) values.answer(new Invocation(mock, Object.class.getMethod("toString"), null));
        assertEquals("Mock for Object, hashCode: " + mock.hashCode(), result);
    }

    @Test
    public void testCompareToMethodWithSameReferences() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        Object mock = new Object();
        int result = (int) values.answer(new Invocation(mock, Comparable.class.getMethod("compareTo", Object.class), new Object[]{mock}));
        assertEquals(0, result);
    }

    @Test
    public void testCompareToMethodWithDifferentReferences() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        Object mock1 = new Object();
        Object mock2 = new Object();
        int result = (int) values.answer(new Invocation(mock1, Comparable.class.getMethod("compareTo", Object.class), new Object[]{mock2}));
        assertEquals(1, result);
    }

    @Test
    public void testPrimitiveReturnType() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        int result = (int) values.answer(new Invocation(null, ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class), new Object[]{int.class}));
        assertEquals(0, result);
    }

    @Test
    public void testPrimitiveWrapperReturnType() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        Integer result = (Integer) values.answer(new Invocation(null, ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class), new Object[]{Integer.class}));
        assertEquals(0, result.intValue());
    }

    @Test
    public void testCollectionReturnType() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        Collection<Object> result = (Collection<Object>) values.answer(new Invocation(null, ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class), new Object[]{Collection.class}));
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSetReturnType() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        Set<Object> result = (Set<Object>) values.answer(new Invocation(null, ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class), new Object[]{Set.class}));
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testHashSetReturnType() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        HashSet<Object> result = (HashSet<Object>) values.answer(new Invocation(null, ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class), new Object[]{HashSet.class}));
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSortedSetReturnType() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        SortedSet<Object> result = (SortedSet<Object>) values.answer(new Invocation(null, ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class), new Object[]{SortedSet.class}));
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testTreeSetReturnType() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        TreeSet<Object> result = (TreeSet<Object>) values.answer(new Invocation(null, ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class), new Object[]{TreeSet.class}));
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testLinkedHashSetReturnType() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        LinkedHashSet<Object> result = (LinkedHashSet<Object>) values.answer(new Invocation(null, ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class), new Object[]{LinkedHashSet.class}));
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testListReturnType() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        List<Object> result = (List<Object>) values.answer(new Invocation(null, ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class), new Object[]{List.class}));
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testLinkedListReturnType() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        LinkedList<Object> result = (LinkedList<Object>) values.answer(new Invocation(null, ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class), new Object[]{LinkedList.class}));
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testArrayListReturnType() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        ArrayList<Object> result = (ArrayList<Object>) values.answer(new Invocation(null, ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class), new Object[]{ArrayList.class}));
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testMapReturnType() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        Map<Object, Object> result = (Map<Object, Object>) values.answer(new Invocation(null, ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class), new Object[]{Map.class}));
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testHashMapReturnType() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        HashMap<Object, Object> result = (HashMap<Object, Object>) values.answer(new Invocation(null, ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class), new Object[]{HashMap.class}));
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testSortedMapReturnType() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        SortedMap<Object, Object> result = (SortedMap<Object, Object>) values.answer(new Invocation(null, ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class), new Object[]{SortedMap.class}));
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testTreeMapReturnType() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        TreeMap<Object, Object> result = (TreeMap<Object, Object>) values.answer(new Invocation(null, ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class), new Object[]{TreeMap.class}));
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testLinkedHashMapReturnType() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        LinkedHashMap<Object, Object> result = (LinkedHashMap<Object, Object>) values.answer(new Invocation(null, ReturnsEmptyValues.class.getMethod("returnValueFor", Class.class), new Object[]{LinkedHashMap.class}));
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

}
