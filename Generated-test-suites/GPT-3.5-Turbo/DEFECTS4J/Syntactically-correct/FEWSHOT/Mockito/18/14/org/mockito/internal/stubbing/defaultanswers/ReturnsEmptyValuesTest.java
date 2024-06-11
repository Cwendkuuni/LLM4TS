package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ReturnsEmptyValuesTest {
    @Test
    public void testAnswer() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();

        assertEquals(new LinkedList<Object>(), values.returnValueFor(Collection.class));
        assertEquals(new HashSet<Object>(), values.returnValueFor(Set.class));
        assertEquals(new HashSet<Object>(), values.returnValueFor(HashSet.class));
        assertEquals(new TreeSet<Object>(), values.returnValueFor(SortedSet.class));
        assertEquals(new TreeSet<Object>(), values.returnValueFor(TreeSet.class));
        assertEquals(new LinkedHashSet<Object>(), values.returnValueFor(LinkedHashSet.class));
        assertEquals(new LinkedList<Object>(), values.returnValueFor(List.class));
        assertEquals(new LinkedList<Object>(), values.returnValueFor(LinkedList.class));
        assertEquals(new ArrayList<Object>(), values.returnValueFor(ArrayList.class));
        assertEquals(new HashMap<Object, Object>(), values.returnValueFor(Map.class));
        assertEquals(new HashMap<Object, Object>(), values.returnValueFor(HashMap.class));
        assertEquals(new TreeMap<Object, Object>(), values.returnValueFor(SortedMap.class));
        assertEquals(new TreeMap<Object, Object>(), values.returnValueFor(TreeMap.class));
        assertEquals(new LinkedHashMap<Object, Object>(), values.returnValueFor(LinkedHashMap.class));
        assertNull(values.returnValueFor(String.class));
    }
}