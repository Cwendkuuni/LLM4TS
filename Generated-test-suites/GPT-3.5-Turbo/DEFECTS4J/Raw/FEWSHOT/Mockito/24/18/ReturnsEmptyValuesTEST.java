package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ReturnsEmptyValuesTest {
    @Test
    public void testAnswer() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        assertEquals(Integer.valueOf(0), answer.returnValueFor(Integer.class));
        assertEquals(Long.valueOf(0), answer.returnValueFor(Long.class));
        assertEquals(Float.valueOf(0), answer.returnValueFor(Float.class));
        assertEquals(Double.valueOf(0), answer.returnValueFor(Double.class));
        assertEquals(Character.valueOf('\u0000'), answer.returnValueFor(Character.class));
        assertEquals(Byte.valueOf((byte) 0), answer.returnValueFor(Byte.class));
        assertEquals(Short.valueOf((short) 0), answer.returnValueFor(Short.class));
        assertEquals(Boolean.FALSE, answer.returnValueFor(Boolean.class));
        assertEquals(0, answer.returnValueFor(int.class));
        assertEquals(0L, answer.returnValueFor(long.class));
        assertEquals(0.0f, answer.returnValueFor(float.class), 0.0);
        assertEquals(0.0, answer.returnValueFor(double.class), 0.0);
        assertEquals('\u0000', answer.returnValueFor(char.class));
        assertEquals((byte) 0, answer.returnValueFor(byte.class));
        assertEquals((short) 0, answer.returnValueFor(short.class));
        assertEquals(false, answer.returnValueFor(boolean.class));
        assertNull(answer.returnValueFor(String.class));

        assertEquals(new LinkedList<Object>(), answer.returnValueFor(Collection.class));
        assertEquals(new HashSet<Object>(), answer.returnValueFor(Set.class));
        assertEquals(new HashSet<Object>(), answer.returnValueFor(HashSet.class));
        assertEquals(new TreeSet<Object>(), answer.returnValueFor(SortedSet.class));
        assertEquals(new TreeSet<Object>(), answer.returnValueFor(TreeSet.class));
        assertEquals(new LinkedHashSet<Object>(), answer.returnValueFor(LinkedHashSet.class));
        assertEquals(new LinkedList<Object>(), answer.returnValueFor(List.class));
        assertEquals(new LinkedList<Object>(), answer.returnValueFor(LinkedList.class));
        assertEquals(new ArrayList<Object>(), answer.returnValueFor(ArrayList.class));
        assertEquals(new HashMap<Object, Object>(), answer.returnValueFor(Map.class));
        assertEquals(new HashMap<Object, Object>(), answer.returnValueFor(HashMap.class));
        assertEquals(new TreeMap<Object, Object>(), answer.returnValueFor(SortedMap.class));
        assertEquals(new TreeMap<Object, Object>(), answer.returnValueFor(TreeMap.class));
        assertEquals(new LinkedHashMap<Object, Object>(), answer.returnValueFor(LinkedHashMap.class));
    }
}