package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer() {
        ReturnsEmptyValues rev = new ReturnsEmptyValues();

        assertTrue(rev.answer(null) instanceof Object);
        assertTrue(rev.answer("string") instanceof Object);
        assertNull(rev.answer(null));
        assertEquals("", rev.answer(""));
        assertEquals(0, rev.answer(int.class));
        assertEquals(0, rev.answer(Integer.class));
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
        assertNull(rev.answer(Object.class));
    }
}