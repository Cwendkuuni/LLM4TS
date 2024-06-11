package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.internal.util.Primitives.isPrimitiveOrWrapper;

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
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer_toStringMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Object mock = new Object();
        InvocationOnMock invocation = Mockito.mockingDetails(ReturnsEmptyValues.class.getMethod("answer", InvocationOnMock.class)).getInvocation();
        invocation.setMock(mock);

        assertEquals("Mock for Object, hashCode: " + mock.hashCode(), returnsEmptyValues.answer(invocation));
    }

    @Test
    public void testAnswer_compareToMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Object mock1 = new Object();
        Object mock2 = new Object();

        InvocationOnMock invocation = Mockito.mockingDetails(ReturnsEmptyValues.class.getMethod("answer", InvocationOnMock.class)).getInvocation();
        invocation.setMock(mock1);
        invocation.setArguments(new Object[]{mock2});

        // mocks should return 0 if references are equal
        assertEquals(0, returnsEmptyValues.answer(invocation).intValue());

        // mocks should return non-zero value if references are not equal
        assertNotSame(mock1, mock2);
        assertEquals(1, returnsEmptyValues.answer(invocation).intValue());
    }

    @Test
    public void testReturnValueFor_primitiveTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        for (Class<?> type : new Class<?>[]{boolean.class, byte.class, short.class, int.class, long.class, float.class, double.class, char.class}) {
            assertEquals(isPrimitiveOrWrapper(type) ? Primitives.getDefaultValue(type) : Primitives.wrap(Primitives.getDefaultValue(type.getComponentType())),
                    returnsEmptyValues.returnValueFor(type));
        }
    }

    @Test
    public void testReturnValueFor_Collection() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Collection<Object> expected = new LinkedList<Object>();
        Collection<Object> actual = (Collection<Object>) returnsEmptyValues.returnValueFor(Collection.class);

        assertEquals(expected.getClass(), actual.getClass());
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.isEmpty(), actual.isEmpty());
    }

    @Test
    public void testReturnValueFor_Set() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Set<Object> expected = new HashSet<Object>();
        Set<Object> actual = (Set<Object>) returnsEmptyValues.returnValueFor(Set.class);

        assertEquals(expected.getClass(), actual.getClass());
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.isEmpty(), actual.isEmpty());
    }

    @Test
    public void testReturnValueFor_HashSet() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Set<Object> expected = new HashSet<Object>();
        Set<Object> actual = (Set<Object>) returnsEmptyValues.returnValueFor(HashSet.class);

        assertEquals(expected.getClass(), actual.getClass());
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.isEmpty(), actual.isEmpty());
    }

    @Test
    public void testReturnValueFor_SortedSet() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        SortedSet<Object> expected = new TreeSet<Object>();
        SortedSet<Object> actual = (SortedSet<Object>) returnsEmptyValues.returnValueFor(SortedSet.class);

        assertEquals(expected.getClass(), actual.getClass());
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.isEmpty(), actual.isEmpty());
    }

    @Test
    public void testReturnValueFor_TreeSet() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        SortedSet<Object> expected = new TreeSet<Object>();
        SortedSet<Object> actual = (SortedSet<Object>) returnsEmptyValues.returnValueFor(TreeSet.class);

        assertEquals(expected.getClass(), actual.getClass());
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.isEmpty(), actual.isEmpty());
    }

    @Test
    public void testReturnValueFor_LinkedHashSet() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Set<Object> expected = new LinkedHashSet<Object>();
        Set<Object> actual = (Set<Object>) returnsEmptyValues.returnValueFor(LinkedHashSet.class);

        assertEquals(expected.getClass(), actual.getClass());
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.isEmpty(), actual.isEmpty());
    }

    @Test
    public void testReturnValueFor_List() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        List<Object> expected = new LinkedList<Object>();
        List<Object> actual = (List<Object>) returnsEmptyValues.returnValueFor(List.class);

        assertEquals(expected.getClass(), actual.getClass());
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.isEmpty(), actual.isEmpty());
    }

    @Test
    public void testReturnValueFor_LinkedList() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        List<Object> expected = new LinkedList<Object>();
        List<Object> actual = (List<Object>) returnsEmptyValues.returnValueFor(LinkedList.class);

        assertEquals(expected.getClass(), actual.getClass());
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.isEmpty(), actual.isEmpty());
    }

    @Test
    public void testReturnValueFor_ArrayList() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        List<Object> expected = new ArrayList<Object>();
        List<Object> actual = (List<Object>) returnsEmptyValues.returnValueFor(ArrayList.class);

        assertEquals(expected.getClass(), actual.getClass());
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.isEmpty(), actual.isEmpty());
    }

    @Test
    public void testReturnValueFor_Map() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Map<Object, Object> expected = new HashMap<Object, Object>();
        Map<Object, Object> actual = (Map<Object, Object>) returnsEmptyValues.returnValueFor(Map.class);

        assertEquals(expected.getClass(), actual.getClass());
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.isEmpty(), actual.isEmpty());
    }

    @Test
    public void testReturnValueFor_HashMap() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Map<Object, Object> expected = new HashMap<Object, Object>();
        Map<Object, Object> actual = (Map<Object, Object>) returnsEmptyValues.returnValueFor(HashMap.class);

        assertEquals(expected.getClass(), actual.getClass());
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.isEmpty(), actual.isEmpty());
    }

    @Test
    public void testReturnValueFor_SortedMap() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        SortedMap<Object, Object> expected = new TreeMap<Object, Object>();
        SortedMap<Object, Object> actual = (SortedMap<Object, Object>) returnsEmptyValues.returnValueFor(SortedMap.class);

        assertEquals(expected.getClass(), actual.getClass());
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.isEmpty(), actual.isEmpty());
    }

    @Test
    public void testReturnValueFor_TreeMap() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        SortedMap<Object, Object> expected = new TreeMap<Object, Object>();
        SortedMap<Object, Object> actual = (SortedMap<Object, Object>) returnsEmptyValues.returnValueFor(TreeMap.class);

        assertEquals(expected.getClass(), actual.getClass());
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.isEmpty(), actual.isEmpty());
    }

    @Test
    public void testReturnValueFor_LinkedHashMap() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Map<Object, Object> expected = new LinkedHashMap<Object, Object>();
        Map<Object, Object> actual = (Map<Object, Object>) returnsEmptyValues.returnValueFor(LinkedHashMap.class);

        assertEquals(expected.getClass(), actual.getClass());
        assertEquals(expected.size(), actual.size());
        assertEquals(expected.isEmpty(), actual.isEmpty());
    }
}