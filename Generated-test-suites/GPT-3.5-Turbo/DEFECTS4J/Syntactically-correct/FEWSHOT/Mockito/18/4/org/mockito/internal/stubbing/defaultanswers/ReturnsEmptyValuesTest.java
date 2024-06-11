package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
import org.mockito.invocation.InvocationOnMock;

public class ReturnsEmptyValuesTest {
    private ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

    @Test
    public void testAnswer() {
        // Test toString() method
        InvocationOnMock toStringMethodInvocation = MockUtil.createInvocation("toString", Object.class, null);
        assertEquals("Mock for Object, hashCode: " + returnsEmptyValues.hashCode(), returnsEmptyValues.answer(toStringMethodInvocation));

        // Test compareTo() method
        InvocationOnMock compareToMethodInvocation = MockUtil.createInvocation("compareTo", Comparable.class, null, returnsEmptyValues);
        assertEquals(0, returnsEmptyValues.answer(compareToMethodInvocation));
        InvocationOnMock compareToMethodInvocation2 = MockUtil.createInvocation("compareTo", Comparable.class, null, new ReturnsEmptyValues());
        assertEquals(1, returnsEmptyValues.answer(compareToMethodInvocation2));

        // Test primitive-returning methods
        InvocationOnMock primitiveMethodInvocation = MockUtil.createInvocation("primitiveMethod", int.class, null);
        assertEquals(0, returnsEmptyValues.answer(primitiveMethodInvocation));

        // Test wrapper class-returning methods
        InvocationOnMock wrapperMethodInvocation = MockUtil.createInvocation("wrapperMethod", Integer.class, null);
        assertEquals(0, returnsEmptyValues.answer(wrapperMethodInvocation));

        // Test collection-returning methods
        InvocationOnMock collectionMethodInvocation = MockUtil.createInvocation("collectionMethod", Collection.class, null);
        assertTrue(returnsEmptyValues.answer(collectionMethodInvocation) instanceof LinkedList);

        // Test Set-returning methods
        InvocationOnMock setMethodInvocation = MockUtil.createInvocation("setMethod", Set.class, null);
        assertTrue(returnsEmptyValues.answer(setMethodInvocation) instanceof HashSet);

        // Test HashSet-returning methods
        InvocationOnMock hashSetMethodInvocation = MockUtil.createInvocation("hashSetMethod", HashSet.class, null);
        assertTrue(returnsEmptyValues.answer(hashSetMethodInvocation) instanceof HashSet);

        // Test SortedSet-returning methods
        InvocationOnMock sortedSetMethodInvocation = MockUtil.createInvocation("sortedSetMethod", SortedSet.class, null);
        assertTrue(returnsEmptyValues.answer(sortedSetMethodInvocation) instanceof TreeSet);

        // Test TreeSet-returning methods
        InvocationOnMock treeSetMethodInvocation = MockUtil.createInvocation("treeSetMethod", TreeSet.class, null);
        assertTrue(returnsEmptyValues.answer(treeSetMethodInvocation) instanceof TreeSet);

        // Test LinkedHashSet-returning methods
        InvocationOnMock linkedHashSetMethodInvocation = MockUtil.createInvocation("linkedHashSetMethod", LinkedHashSet.class, null);
        assertTrue(returnsEmptyValues.answer(linkedHashSetMethodInvocation) instanceof LinkedHashSet);

        // Test List-returning methods
        InvocationOnMock listMethodInvocation = MockUtil.createInvocation("listMethod", List.class, null);
        assertTrue(returnsEmptyValues.answer(listMethodInvocation) instanceof LinkedList);

        // Test LinkedList-returning methods
        InvocationOnMock linkedListMethodInvocation = MockUtil.createInvocation("linkedListMethod", LinkedList.class, null);
        assertTrue(returnsEmptyValues.answer(linkedListMethodInvocation) instanceof LinkedList);

        // Test ArrayList-returning methods
        InvocationOnMock arrayListMethodInvocation = MockUtil.createInvocation("arrayListMethod", ArrayList.class, null);
        assertTrue(returnsEmptyValues.answer(arrayListMethodInvocation) instanceof ArrayList);

        // Test Map-returning methods
        InvocationOnMock mapMethodInvocation = MockUtil.createInvocation("mapMethod", Map.class, null);
        assertTrue(returnsEmptyValues.answer(mapMethodInvocation) instanceof HashMap);

        // Test HashMap-returning methods
        InvocationOnMock hashMapMethodInvocation = MockUtil.createInvocation("hashMapMethod", HashMap.class, null);
        assertTrue(returnsEmptyValues.answer(hashMapMethodInvocation) instanceof HashMap);

        // Test SortedMap-returning methods
        InvocationOnMock sortedMapMethodInvocation = MockUtil.createInvocation("sortedMapMethod", SortedMap.class, null);
        assertTrue(returnsEmptyValues.answer(sortedMapMethodInvocation) instanceof TreeMap);

        // Test TreeMap-returning methods
        InvocationOnMock treeMapMethodInvocation = MockUtil.createInvocation("treeMapMethod", TreeMap.class, null);
        assertTrue(returnsEmptyValues.answer(treeMapMethodInvocation) instanceof TreeMap);

        // Test LinkedHashMap-returning methods
        InvocationOnMock linkedHashMapMethodInvocation = MockUtil.createInvocation("linkedHashMapMethod", LinkedHashMap.class, null);
        assertTrue(returnsEmptyValues.answer(linkedHashMapMethodInvocation) instanceof LinkedHashMap);
    }
}