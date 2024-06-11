import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ReturnsEmptyValuesTest {
    @Test
    public void testAnswer() {
        ReturnsEmptyValues rev = new ReturnsEmptyValues();
        
        assertEquals(Integer.valueOf(0), rev.answer());
        assertEquals(Long.valueOf(0), rev.answer());
        assertEquals(Float.valueOf(0), rev.answer());
        assertEquals(Double.valueOf(0), rev.answer());
        assertEquals(Character.valueOf('\u0000'), rev.answer());
        assertEquals(Boolean.FALSE, rev.answer());
        assertNull(rev.answer());
        
        assertEquals(new LinkedList<Object>(), rev.answer(Collection.class));
        assertEquals(new HashSet<Object>(), rev.answer(Set.class));
        assertEquals(new HashSet<Object>(), rev.answer(HashSet.class));
        assertEquals(new TreeSet<Object>(), rev.answer(SortedSet.class));
        assertEquals(new TreeSet<Object>(), rev.answer(TreeSet.class));
        assertEquals(new LinkedHashSet<Object>(), rev.answer(LinkedHashSet.class));
        assertEquals(new LinkedList<Object>(), rev.answer(List.class));
        assertEquals(new LinkedList<Object>(), rev.answer(LinkedList.class));
        assertEquals(new ArrayList<Object>(), rev.answer(ArrayList.class));
        assertEquals(new HashMap<Object, Object>(), rev.answer(Map.class));
        assertEquals(new HashMap<Object, Object>(), rev.answer(HashMap.class));
        assertEquals(new TreeMap<Object, Object>(), rev.answer(SortedMap.class));
        assertEquals(new TreeMap<Object, Object>(), rev.answer(TreeMap.class));
        assertEquals(new LinkedHashMap<Object, Object>(), rev.answer(LinkedHashMap.class));
    }
}