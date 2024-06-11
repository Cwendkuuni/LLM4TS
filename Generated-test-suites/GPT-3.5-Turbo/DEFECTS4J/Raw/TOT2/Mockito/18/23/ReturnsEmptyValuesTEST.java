import static org.junit.Assert.assertEquals;
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

    @Test
    public void testAnswerToString() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        InvocationOnMock invocation = new InvocationOnMock();
        assertEquals(values.answer(invocation), "Mock for Object, hashCode: 0");
    }

    @Test
    public void testAnswerCompareTo() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        InvocationOnMock invocation = new InvocationOnMock();
        invocation.setMock(new Object());
        invocation.setArguments(new Object[] {new Object()});
        assertEquals(values.answer(invocation), 1);
    }

    @Test
    public void testAnswerReturnValueForPrimitive() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        assertEquals(values.returnValueFor(int.class), 0);
    }

    @Test
    public void testAnswerReturnValueForCollection() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        Collection<Object> collection = (Collection<Object>) values.returnValueFor(Collection.class);
        assertTrue(collection instanceof LinkedList);
    }

    @Test
    public void testAnswerReturnValueForSet() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        Set<Object> set = (Set<Object>) values.returnValueFor(Set.class);
        assertTrue(set instanceof HashSet);
    }

    @Test
    public void testAnswerReturnValueForMap() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        Map<Object, Object> map = (Map<Object, Object>) values.returnValueFor(Map.class);
        assertTrue(map instanceof HashMap);
    }

    @Test
    public void testAnswerReturnValueForSortedSet() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        SortedSet<Object> sortedSet = (SortedSet<Object>) values.returnValueFor(SortedSet.class);
        assertTrue(sortedSet instanceof TreeSet);
    }

    @Test
    public void testAnswerReturnValueForSortedMap() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        SortedMap<Object, Object> sortedMap = (SortedMap<Object, Object>) values.returnValueFor(SortedMap.class);
        assertTrue(sortedMap instanceof TreeMap);
    }

    @Test
    public void testAnswerReturnValueForList() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        List<Object> list = (List<Object>) values.returnValueFor(List.class);
        assertTrue(list instanceof LinkedList);
    }

    @Test
    public void testAnswerReturnValueForHashMap() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        HashMap<Object, Object> hashMap = (HashMap<Object, Object>) values.returnValueFor(HashMap.class);
        assertTrue(hashMap instanceof HashMap);
    }

    @Test
    public void testAnswerReturnValueForTreeMap() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        TreeMap<Object, Object> treeMap = (TreeMap<Object, Object>) values.returnValueFor(TreeMap.class);
        assertTrue(treeMap instanceof TreeMap);
    }

    @Test
    public void testAnswerReturnValueForLinkedHashMap() {
        ReturnsEmptyValues values = new ReturnsEmptyValues();
        LinkedHashMap<Object, Object> linkedHashMap = (LinkedHashMap<Object, Object>) values.returnValueFor(LinkedHashMap.class);
        assertTrue(linkedHashMap instanceof LinkedHashMap);
    }

}