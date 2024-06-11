import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.TreeSet;

import org.junit.Test;

public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringReturnsDescriptionOfMock() {
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        String mockDescription = (String) returnValue.returnValueFor(String.class);
        assertNotNull(mockDescription);
        assertTrue(mockDescription.startsWith("Mock for "));
    }

    @Test
    public void testCompareToMethodReturnsZeroOrOne() {
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        int compareResult = (int) returnValue.answer(mock(InvocationOnMock.class));
        assertTrue(compareResult == 0 || compareResult == 1);
    }

    @Test
    public void testPrimitiveReturnTypeReturnsDefaultValue() {
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        int defaultInt = (int) returnValue.returnValueFor(int.class);
        assertEquals(0, defaultInt);
    }

    @Test
    public void testCollectionReturnTypeReturnsEmptyLinkedList() {
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        Collection<Object> collection = (Collection<Object>) returnValue.returnValueFor(Collection.class);
        assertTrue(collection instanceof LinkedList);
        assertTrue(collection.isEmpty());
    }

    @Test
    public void testSetReturnTypeReturnsEmptyHashSet() {
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        Set<Object> set = (Set<Object>) returnValue.returnValueFor(Set.class);
        assertTrue(set instanceof HashSet);
        assertTrue(set.isEmpty());
    }

    @Test
    public void testMapReturnTypeReturnsEmptyHashMap() {
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        Map<Object, Object> map = (Map<Object, Object>) returnValue.returnValueFor(Map.class);
        assertTrue(map instanceof HashMap);
        assertTrue(map.isEmpty());
    }

    @Test
    public void testTreeMapReturnTypeReturnsEmptyTreeMap() {
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        Map<Object, Object> treeMap = (Map<Object, Object>) returnValue.returnValueFor(TreeMap.class);
        assertTrue(treeMap instanceof TreeMap);
        assertTrue(treeMap.isEmpty());
    }

    @Test
    public void testLinkedHashMapReturnTypeReturnsEmptyLinkedHashMap() {
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        Map<Object, Object> linkedHashMap = (Map<Object, Object>) returnValue.returnValueFor(LinkedHashMap.class);
        assertTrue(linkedHashMap instanceof LinkedHashMap);
        assertTrue(linkedHashMap.isEmpty());
    }

    @Test
    public void testLinkedListReturnTypeReturnsEmptyLinkedList() {
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        Collection<Object> linkedList = (Collection<Object>) returnValue.returnValueFor(LinkedList.class);
        assertTrue(linkedList instanceof LinkedList);
        assertTrue(linkedList.isEmpty());
    }

    @Test
    public void testArrayListReturnTypeReturnsEmptyArrayList() {
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        Collection<Object> arrayList = (Collection<Object>) returnValue.returnValueFor(ArrayList.class);
        assertTrue(arrayList instanceof ArrayList);
        assertTrue(arrayList.isEmpty());
    }

    @Test
    public void testHashSetReturnTypeReturnsEmptyHashSet() {
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        Set<Object> hashSet = (Set<Object>) returnValue.returnValueFor(HashSet.class);
        assertTrue(hashSet instanceof HashSet);
        assertTrue(hashSet.isEmpty());
    }

    @Test
    public void testTreeSetReturnTypeReturnsEmptyTreeSet() {
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        Set<Object> treeSet = (Set<Object>) returnValue.returnValueFor(TreeSet.class);
        assertTrue(treeSet instanceof TreeSet);
        assertTrue(treeSet.isEmpty());
    }

    @Test
    public void testLinkedHashSetReturnTypeReturnsEmptyLinkedHashSet() {
        ReturnsEmptyValues returnValue = new ReturnsEmptyValues();
        Set<Object> linkedHashSet = (Set<Object>) returnValue.returnValueFor(LinkedHashSet.class);
        assertTrue(linkedHashSet instanceof LinkedHashSet);
        assertTrue(linkedHashSet.isEmpty());
    }

}