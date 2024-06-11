package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.internal.util.Primitives.isPrimitiveOrWrapper;
import static org.mockito.internal.util.Primitives.defaultValueForPrimitiveOrWrapper;

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
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerInstance_Primitives() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);

        assertEquals(defaultValueForPrimitiveOrWrapper(int.class), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(int.class, "someMethod"))));
        assertEquals(defaultValueForPrimitiveOrWrapper(double.class), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(double.class, "someMethod"))));
        assertEquals(defaultValueForPrimitiveOrWrapper(boolean.class), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(boolean.class, "someMethod"))));
        assertEquals(defaultValueForPrimitiveOrWrapper(char.class), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(char.class, "someMethod"))));
        assertEquals(defaultValueForPrimitiveOrWrapper(byte.class), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(byte.class, "someMethod"))));
        assertEquals(defaultValueForPrimitiveOrWrapper(short.class), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(short.class, "someMethod"))));
        assertEquals(defaultValueForPrimitiveOrWrapper(long.class), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(long.class, "someMethod"))));
        assertEquals(defaultValueForPrimitiveOrWrapper(float.class), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(float.class, "someMethod"))));
        assertEquals(defaultValueForPrimitiveOrWrapper(boolean[].class), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(boolean[].class, "someMethod"))));
        assertEquals(defaultValueForPrimitiveOrWrapper(byte[].class), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(byte[].class, "someMethod"))));
        assertEquals(defaultValueForPrimitiveOrWrapper(char[].class), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(char[].class, "someMethod"))));
        assertEquals(defaultValueForPrimitiveOrWrapper(short[].class), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(short[].class, "someMethod"))));
        assertEquals(defaultValueForPrimitiveOrWrapper(int[].class), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(int[].class, "someMethod"))));
        assertEquals(defaultValueForPrimitiveOrWrapper(long[].class), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(long[].class, "someMethod"))));
    }

    @Test
    public void testAnswerInstance_Collections_EmptyArrayList() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);

        assertEquals(new ArrayList<>(), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(Collection.class, "someMethod"))));
    }

    @Test
    public void testAnswerInstance_Collections_EmptyLinkedList() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);

        assertEquals(new LinkedList<>(), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(List.class, "someMethod"))));
    }

    @Test
    public void testAnswerInstance_Collections_EmptyLinkedHashSet() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);

        assertEquals(new LinkedHashSet<>(), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(Set.class, "someMethod"))));
    }

    @Test
    public void testAnswerInstance_Collections_EmptyHashSet() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);

        assertEquals(new HashSet<>(), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(Set.class, "someMethod"))));
    }

    @Test
    public void testAnswerInstance_Collections_EmptyTreeSet() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);

        assertEquals(new TreeSet<>(), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(SortedSet.class, "someMethod"))));
    }

    @Test
    public void testAnswerInstance_Collections_EmptyTreeMap() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);

        assertEquals(new TreeMap<>(), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(SortedMap.class, "someMethod"))));
    }

    @Test
    public void testAnswerInstance_Collections_EmptyHashMap() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);

        assertEquals(new HashMap<>(), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(Map.class, "someMethod"))));
    }

    @Test
    public void testAnswerInstance_Collections_EmptyLinkedHashMap() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);

        assertEquals(new LinkedHashMap<>(), returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(Map.class, "someMethod"))));
    }

    @Test
    public void testAnswerInstance_Collections_Null() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);

        assertNull(returnsEmptyValues.answer(Mockito.mock(InvocationOnMock.class).withMethod(Mockito.method(Collection.class, "someMethod"))));
    }

    @Test
    public void testCompareToMethod_ReturnsOne() {
        ReturnsEmptyValues returnsEmptyValues = Mockito.mock(ReturnsEmptyValues.class);

        assertEquals(1, ((Comparable<Object>) returnsEmptyValues).compareTo(Mockito.mock(Object.class)));
    }
}