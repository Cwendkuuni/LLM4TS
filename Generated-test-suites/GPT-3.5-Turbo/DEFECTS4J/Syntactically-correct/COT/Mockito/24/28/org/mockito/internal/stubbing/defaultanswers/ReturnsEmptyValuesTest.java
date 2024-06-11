
import org.junit.Test;
import static org.junit.Assert.*;

public class ReturnsEmptyValuesTest {

    @Test
    public void testToString() {
        ReturnsEmptyValues obj = new ReturnsEmptyValues();
        assertEquals("Mock for ReturnsEmptyValues, hashCode: " + obj.hashCode(), obj.answer(invocation));
    }

    @Test
    public void testCompareTo() {
        ReturnsEmptyValues obj = new ReturnsEmptyValues();
        assertEquals(1, obj.answer(invocation));
    }

    @Test
    public void testReturnValueFor_primitives() {
        ReturnsEmptyValues obj = new ReturnsEmptyValues();
        assertEquals(0, obj.returnValueFor(int.class));
        assertEquals(0L, obj.returnValueFor(long.class));
        assertEquals(0.0F, obj.returnValueFor(float.class), 0.0);
        assertEquals(0.0, obj.returnValueFor(double.class), 0.0);
        assertEquals(false, obj.returnValueFor(boolean.class));
        assertEquals('\u0000', obj.returnValueFor(char.class));
        assertNull(obj.returnValueFor(void.class));
    }

    @Test
    public void testReturnValueFor_wrappers() {
        ReturnsEmptyValues obj = new ReturnsEmptyValues();
        assertEquals(0, obj.returnValueFor(Integer.class));
        assertEquals(0L, obj.returnValueFor(Long.class));
        assertEquals(0.0F, obj.returnValueFor(Float.class), 0.0);
        assertEquals(0.0, obj.returnValueFor(Double.class), 0.0);
        assertEquals(false, obj.returnValueFor(Boolean.class));
        assertEquals('\u0000', obj.returnValueFor(Character.class));
    }

    @Test
    public void testReturnValueFor_collection_types() {
        ReturnsEmptyValues obj = new ReturnsEmptyValues();
        assertEquals(new LinkedList<Object>(), obj.returnValueFor(Collection.class));
        assertEquals(new HashSet<Object>(), obj.returnValueFor(Set.class));
        assertEquals(new HashSet<Object>(), obj.returnValueFor(HashSet.class));
        assertEquals(new TreeSet<Object>(), obj.returnValueFor(SortedSet.class));
        assertEquals(new TreeSet<Object>(), obj.returnValueFor(TreeSet.class));
        assertEquals(new LinkedHashSet<Object>(), obj.returnValueFor(LinkedHashSet.class));
        assertEquals(new LinkedList<Object>(), obj.returnValueFor(List.class));
        assertEquals(new LinkedList<Object>(), obj.returnValueFor(LinkedList.class));
        assertEquals(new ArrayList<Object>(), obj.returnValueFor(ArrayList.class));
        assertEquals(new HashMap<Object, Object>(), obj.returnValueFor(Map.class));
        assertEquals(new HashMap<Object, Object>(), obj.returnValueFor(HashMap.class));
        assertEquals(new TreeMap<Object, Object>(), obj.returnValueFor(SortedMap.class));
        assertEquals(new TreeMap<Object, Object>(), obj.returnValueFor(TreeMap.class));
        assertEquals(new LinkedHashMap<Object, Object>(), obj.returnValueFor(LinkedHashMap.class));
    }
}
