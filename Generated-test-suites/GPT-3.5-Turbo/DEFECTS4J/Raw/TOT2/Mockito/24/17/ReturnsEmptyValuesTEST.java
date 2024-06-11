import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.Collection;
import java.util.HashSet;
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
    public void testAnswer_toString() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));
        Object result = answer.answer(invocation);
        assertTrue(result instanceof String);
    }

    @Test
    public void testAnswer_compareTo() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Comparable.class.getMethod("compareTo"));
        Object result = answer.answer(invocation);
        assertEquals(1, result);
    }

    @Test
    public void testAnswer_primitive() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("hashCode"));
        Object result = answer.answer(invocation);
        assertEquals(0, result);
    }

    @Test
    public void testReturnValueFor_collection() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        Collection<Object> result = (Collection<Object>) answer.returnValueFor(Collection.class);
        assertTrue(result instanceof LinkedList);
    }

    @Test
    public void testReturnValueFor_set() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        Set<Object> result = (Set<Object>) answer.returnValueFor(Set.class);
        assertTrue(result instanceof HashSet);
    }

    @Test
    public void testReturnValueFor_sortedSet() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        SortedSet<Object> result = (SortedSet<Object>) answer.returnValueFor(SortedSet.class);
        assertTrue(result instanceof TreeSet);
    }

    @Test
    public void testReturnValueFor_list() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        List<Object> result = (List<Object>) answer.returnValueFor(List.class);
        assertTrue(result instanceof LinkedList);
    }

    @Test
    public void testReturnValueFor_map() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        Map<Object, Object> result = (Map<Object, Object>) answer.returnValueFor(Map.class);
        assertTrue(result instanceof HashMap);
    }

    @Test
    public void testReturnValueFor_sortedMap() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        SortedMap<Object, Object> result = (SortedMap<Object, Object>) answer.returnValueFor(SortedMap.class);
        assertTrue(result instanceof TreeMap);
    }

}