
package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Test;

public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();

    @Test
    public void testReturnValueFor_PrimitiveTypes() {
        assertEquals((byte) 0, emptyValues.returnValueFor(byte.class));
        assertEquals((short) 0, emptyValues.returnValueFor(short.class));
        assertEquals(0, emptyValues.returnValueFor(int.class));
        assertEquals(0L, emptyValues.returnValueFor(long.class));
        assertEquals(0.0f, emptyValues.returnValueFor(float.class), 0.0);
        assertEquals(0.0, emptyValues.returnValueFor(double.class), 0.0);
        assertEquals('\u0000', emptyValues.returnValueFor(char.class));
        assertEquals(false, emptyValues.returnValueFor(boolean.class));

        assertNotNull(emptyValues.returnValueFor(Byte.class));
        assertNotNull(emptyValues.returnValueFor(Short.class));
        assertNotNull(emptyValues.returnValueFor(Integer.class));
        assertNotNull(emptyValues.returnValueFor(Long.class));
        assertNotNull(emptyValues.returnValueFor(Float.class));
        assertNotNull(emptyValues.returnValueFor(Double.class));
        assertNotNull(emptyValues.returnValueFor(Character.class));
        assertNotNull(emptyValues.returnValueFor(Boolean.class));
    }

    @Test
    public void testReturnValueFor_Collections() {
        assertTrue(emptyValues.returnValueFor(Collection.class) instanceof LinkedList);
        assertTrue(emptyValues.returnValueFor(Set.class) instanceof HashSet);
        assertTrue(emptyValues.returnValueFor(HashSet.class) instanceof HashSet);
        assertTrue(emptyValues.returnValueFor(SortedSet.class) instanceof TreeSet);
        assertTrue(emptyValues.returnValueFor(TreeSet.class) instanceof TreeSet);
        assertTrue(emptyValues.returnValueFor(LinkedHashSet.class) instanceof LinkedHashSet);
        assertTrue(emptyValues.returnValueFor(List.class) instanceof LinkedList);
        assertTrue(emptyValues.returnValueFor(LinkedList.class) instanceof LinkedList);
        assertTrue(emptyValues.returnValueFor(ArrayList.class) instanceof ArrayList);
        assertTrue(emptyValues.returnValueFor(Map.class) instanceof HashMap);
        assertTrue(emptyValues.returnValueFor(HashMap.class) instanceof HashMap);
        assertTrue(emptyValues.returnValueFor(SortedMap.class) instanceof TreeMap);
        assertTrue(emptyValues.returnValueFor(TreeMap.class) instanceof TreeMap);
        assertTrue(emptyValues.returnValueFor(LinkedHashMap.class) instanceof LinkedHashMap);
    }
}
