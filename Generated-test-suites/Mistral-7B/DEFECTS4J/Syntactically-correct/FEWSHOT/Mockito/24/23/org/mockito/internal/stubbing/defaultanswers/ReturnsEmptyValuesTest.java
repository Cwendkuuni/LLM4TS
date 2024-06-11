package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsEmptyValuesTest {

    @Test
    public void testToString() {
        Object mock = mock(String.class);
        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        String result = (String) answer.answer(mock(InvocationOnMock.class).withMock(mock).withMethodCallsTo(mock(String.class).toString()).createMock());

        assertEquals("Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode(), result);
    }

    @Test
    public void testCompareTo() {
        Object mock1 = mock(Comparable.class);
        Object mock2 = mock(Comparable.class);

        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        answer.answer(mock(InvocationOnMock.class).withMock(mock1).withMethodCallsTo(mock1.getClass().getMethod("compareTo", Object.class)).createMock());
        answer.answer(mock(InvocationOnMock.class).withMock(mock2).withMethodCallsTo(mock2.getClass().getMethod("compareTo", Object.class)).createMock());

        verify(mock1, Mockito.atLeastOnce()).compareTo(mock2);
        verify(mock2, Mockito.atLeastOnce()).compareTo(mock1);

        assertEquals(1, answer.answer(mock(InvocationOnMock.class).withMock(mock1).createMock()).getClass().cast().compareTo(answer.answer(mock(InvocationOnMock.class).withMock(mock2).createMock())));
    }

    @Test
    public void testPrimitives() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        assertEquals(Boolean.FALSE, answer.answer(mock(InvocationOnMock.class).createMock()).getClass().cast(Boolean.TYPE));
        assertEquals(Character.MIN_VALUE, answer.answer(mock(InvocationOnMock.class).withReturnType(char.class).createMock()).getClass().cast(Character.TYPE));
        assertEquals(Short.MIN_VALUE, answer.answer(mock(InvocationOnMock.class).withReturnType(short.class).createMock()).getClass().cast(Short.TYPE));
        assertEquals(Integer.MIN_VALUE, answer.answer(mock(InvocationOnMock.class).withReturnType(int.class).createMock()).getClass().cast(Integer.TYPE));
        assertEquals(Long.MIN_VALUE, answer.answer(mock(InvocationOnMock.class).withReturnType(long.class).createMock()).getClass().cast(Long.TYPE));
        assertEquals(Float.NaN, answer.answer(mock(InvocationOnMock.class).withReturnType(float.class).createMock()).getClass().cast(Float.TYPE));
        assertEquals(Double.NaN, answer.answer(mock(InvocationOnMock.class).withReturnType(double.class).createMock()).getClass().cast(Double.TYPE));
        assertEquals(Byte.MIN_VALUE, answer.answer(mock(InvocationOnMock.class).withReturnType(byte.class).createMock()).getClass().cast(Byte.TYPE));
    }

    @Test
    public void testCollections() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();

        assertEquals(new ArrayList<Object>(), answer.answer(mock(InvocationOnMock.class).withMethodCallsTo(List.class.getMethod("newInstance", int.class)).createMock()).getClass().cast(ArrayList.class));
        assertEquals(new LinkedList<Object>(), answer.answer(mock(InvocationOnMock.class).withMethodCallsTo(List.class.getMethod("newLinkedList")).createMock()).getClass().cast(LinkedList.class));
        assertEquals(new ArrayList<>(), answer.answer(mock(InvocationOnMock.class).withMethodCallsTo(Collection.class.getMethod("newInstance")).createMock()).getClass().cast(ArrayList.class));
        assertEquals(new HashSet<Object>(), answer.answer(mock(InvocationOnMock.class).withMethodCallsTo(Set.class.getMethod("newInstance")).createMock()).getClass().cast(HashSet.class));
        assertEquals(new LinkedHashSet<Object>(), answer.answer(mock(InvocationOnMock.class).withMethodCallsTo(Set.class.getMethod("newLinkedHashSet")).createMock()).getClass().cast(LinkedHashSet.class));
        assertEquals(new TreeSet<Object>(), answer.answer(mock(InvocationOnMock.class).withMethodCallsTo(SortedSet.class.getMethod("newInstance")).createMock()).getClass().cast(TreeSet.class));
        assertEquals(new LinkedHashMap<Object, Object>(), answer.answer(mock(InvocationOnMock.class).withMethodCallsTo(Map.class.getMethod("newLinkedHashMap", int.class)).createMock()).getClass().cast(LinkedHashMap.class));
        assertEquals(new HashMap<Object, Object>(), answer.answer(mock(InvocationOnMock.class).withMethodCallsTo(Map.class.getMethod("newHashMap")).createMock()).getClass().cast(HashMap.class));
        assertEquals(new TreeMap<Object, Object>(), answer.answer(mock(InvocationOnMock.class).withMethodCallsTo(SortedMap.class.getMethod("newTreeMap")).createMock()).getClass().cast(TreeMap.class));
    }
}