package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
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
    public void testAnswer_ReturnsEmptyList() {
        Collection<?> result = (Collection<?>) emptyValues.returnValueFor(Collection.class);
        assertNotNull(result);
        assertEquals(LinkedList.class, result.getClass());
        assertEquals(0, result.size());
    }
    
    @Test
    public void testAnswer_ReturnsEmptySet() {
        Set<?> result = (Set<?>) emptyValues.returnValueFor(Set.class);
        assertNotNull(result);
        assertEquals(HashSet.class, result.getClass());
        assertEquals(0, result.size());
    }
    
    @Test
    public void testAnswer_ReturnsEmptyHashSet() {
        HashSet<?> result = (HashSet<?>) emptyValues.returnValueFor(HashSet.class);
        assertNotNull(result);
        assertEquals(HashSet.class, result.getClass());
        assertEquals(0, result.size());
    }
    
    @Test
    public void testAnswer_ReturnsEmptySortedSet() {
        SortedSet<?> result = (SortedSet<?>) emptyValues.returnValueFor(SortedSet.class);
        assertNotNull(result);
        assertEquals(TreeSet.class, result.getClass());
        assertEquals(0, result.size());
    }
    
    @Test
    public void testAnswer_ReturnsEmptyTreeSet() {
        TreeSet<?> result = (TreeSet<?>) emptyValues.returnValueFor(TreeSet.class);
        assertNotNull(result);
        assertEquals(TreeSet.class, result.getClass());
        assertEquals(0, result.size());
    }
    
    @Test
    public void testAnswer_ReturnsEmptyLinkedHashSet() {
        LinkedHashSet<?> result = (LinkedHashSet<?>) emptyValues.returnValueFor(LinkedHashSet.class);
        assertNotNull(result);
        assertEquals(LinkedHashSet.class, result.getClass());
        assertEquals(0, result.size());
    }
    
    @Test
    public void testAnswer_ReturnsEmptyLinkedList() {
        LinkedList<?> result = (LinkedList<?>) emptyValues.returnValueFor(LinkedList.class);
        assertNotNull(result);
        assertEquals(LinkedList.class, result.getClass());
        assertEquals(0, result.size());
    }
    
    @Test
    public void testAnswer_ReturnsEmptyArrayList() {
        ArrayList<?> result = (ArrayList<?>) emptyValues.returnValueFor(ArrayList.class);
        assertNotNull(result);
        assertEquals(ArrayList.class, result.getClass());
        assertEquals(0, result.size());
    }
    
    @Test
    public void testAnswer_ReturnsEmptyMap() {
        Map<?, ?> result = (Map<?, ?>) emptyValues.returnValueFor(Map.class);
        assertNotNull(result);
        assertEquals(HashMap.class, result.getClass());
        assertEquals(0, result.size());
    }
    
    @Test
    public void testAnswer_ReturnsEmptyHashMap() {
        HashMap<?, ?> result = (HashMap<?, ?>) emptyValues.returnValueFor(HashMap.class);
        assertNotNull(result);
        assertEquals(HashMap.class, result.getClass());
        assertEquals(0, result.size());
    }
    
    @Test
    public void testAnswer_ReturnsEmptySortedMap() {
        SortedMap<?, ?> result = (SortedMap<?, ?>) emptyValues.returnValueFor(SortedMap.class);
        assertNotNull(result);
        assertEquals(TreeMap.class, result.getClass());
        assertEquals(0, result.size());
    }
    
    @Test
    public void testAnswer_ReturnsEmptyTreeMap() {
        TreeMap<?, ?> result = (TreeMap<?, ?>) emptyValues.returnValueFor(TreeMap.class);
        assertNotNull(result);
        assertEquals(TreeMap.class, result.getClass());
        assertEquals(0, result.size());
    }
    
    @Test
    public void testAnswer_ReturnsEmptyLinkedHashMap() {
        LinkedHashMap<?, ?> result = (LinkedHashMap<?, ?>) emptyValues.returnValueFor(LinkedHashMap.class);
        assertNotNull(result);
        assertEquals(LinkedHashMap.class, result.getClass());
        assertEquals(0, result.size());
    }
    
    @Test
    public void testAnswer_ReturnsNullForOtherTypes() {
        assertNull(emptyValues.returnValueFor(String.class));
        assertNull(emptyValues.returnValueFor(Integer.class));
        assertNull(emptyValues.returnValueFor(Object.class));
    }
}