package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer_toString() {
        Object mock = Mockito.mock(String.class);
        InvocationOnMock invocation = Mockito.mockinvocation(mock, "toString()");

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object result = returnsEmptyValues.answer(invocation);

        String expected = String.format("Mock for %s, hashCode: %s", Mockito.class.getSimpleName(), mock.hashCode());
        assertEquals(expected, result);
    }

    @Test
    public void testAnswer_compareTo() {
        Object mock1 = Mockito.mock(Comparable.class);
        Object mock2 = Mockito.mock(Comparable.class);

        InvocationOnMock invocation1 = Mockito.mockinvocation(mock1, "compareTo(Ljava/lang/Object;)I", mock2);
        InvocationOnMock invocation2 = Mockito.mockinvocation(mock2, "compareTo(Ljava/lang/Object;)I", mock1);

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        int result1 = returnsEmptyValues.answer(invocation1).getClass().cast(invocation1.getArguments()[1]).hashCode();
        int result2 = returnsEmptyValues.answer(invocation2).getClass().cast(invocation2.getArguments()[1]).hashCode();

        assertTrue(mock1.equals(mock2) ? result1 == 0 : result1 != 0 && result2 != 0);
    }

    @Test
    public void testReturnValueFor_primitive_int() {
        Class<Integer> intType = Integer.TYPE;

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Integer result = (Integer) returnsEmptyValues.returnValueFor(intType);

        assertEquals(0, result.intValue());
    }

    @Test
    public void testReturnValueFor_primitive_boolean() {
        Class<Boolean> booleanType = Boolean.TYPE;

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Boolean result = (Boolean) returnsEmptyValues.returnValueFor(booleanType);

        assertFalse(result);
    }

    @Test
    public void testReturnValueFor_Collection() {
        Class<Collection<?>> collectionType = Collection.class;

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Collection<Object> result = (Collection<Object>) returnsEmptyValues.returnValueFor(collectionType);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueFor_Set() {
        Class<Set<?>> setType = Set.class;

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Set<Object> result = (Set<Object>) returnsEmptyValues.returnValueFor(setType);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueFor_HashSet() {
        Class<HashSet<?>> hashSetType = HashSet.class;

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Set<Object> result = (Set<Object>) returnsEmptyValues.returnValueFor(hashSetType);

        assertTrue(result instanceof HashSet);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueFor_SortedSet() {
        Class<SortedSet<?>> sortedSetType = SortedSet.class;

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        SortedSet<Object> result = (SortedSet<Object>) returnsEmptyValues.returnValueFor(sortedSetType);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueFor_TreeSet() {
        Class<TreeSet<?>> treeSetType = TreeSet.class;

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        SortedSet<Object> result = (SortedSet<Object>) returnsEmptyValues.returnValueFor(treeSetType);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueFor_LinkedHashSet() {
        Class<LinkedHashSet<?>> linkedHashSetType = LinkedHashSet.class;

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Set<Object> result = (Set<Object>) returnsEmptyValues.returnValueFor(linkedHashSetType);

        assertTrue(result instanceof LinkedHashSet);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueFor_List() {
        Class<List<?>> listType = List.class;

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        List<Object> result = (List<Object>) returnsEmptyValues.returnValueFor(listType);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueFor_LinkedList() {
        Class<LinkedList<?>> linkedListType = LinkedList.class;

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        List<Object> result = (List<Object>) returnsEmptyValues.returnValueFor(linkedListType);

        assertTrue(result instanceof LinkedList);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueFor_ArrayList() {
        Class<ArrayList<?>> arrayListType = ArrayList.class;

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        List<Object> result = (List<Object>) returnsEmptyValues.returnValueFor(arrayListType);

        assertTrue(result instanceof ArrayList);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueFor_Map() {
        Class<Map<?, ?>> mapType = Map.class;

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Map<Object, Object> result = (Map<Object, Object>) returnsEmptyValues.returnValueFor(mapType);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueFor_HashMap() {
        Class<HashMap<?, ?>> hashMapType = HashMap.class;

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Map<Object, Object> result = (Map<Object, Object>) returnsEmptyValues.returnValueFor(hashMapType);

        assertTrue(result instanceof HashMap);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueFor_SortedMap() {
        Class<SortedMap<?, ?>> sortedMapType = SortedMap.class;

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        SortedMap<Object, Object> result = (SortedMap<Object, Object>) returnsEmptyValues.returnValueFor(sortedMapType);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueFor_TreeMap() {
        Class<TreeMap<?, ?>> treeMapType = TreeMap.class;

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        SortedMap<Object, Object> result = (SortedMap<Object, Object>) returnsEmptyValues.returnValueFor(treeMapType);

        assertTrue(result.isEmpty());
    }

    @Test
    public void testReturnValueFor_LinkedHashMap() {
        Class<LinkedHashMap<?, ?>> linkedHashMapType = LinkedHashMap.class;

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Map<Object, Object> result = (Map<Object, Object>) returnsEmptyValues.returnValueFor(linkedHashMapType);

        assertTrue(result instanceof LinkedHashMap);
        assertTrue(result.isEmpty());
    }
}