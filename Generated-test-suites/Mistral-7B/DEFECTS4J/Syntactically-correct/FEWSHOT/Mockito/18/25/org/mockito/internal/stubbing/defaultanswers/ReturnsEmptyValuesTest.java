package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.internal.util.Primitives.isPrimitiveOrWrapper;
import static org.mockito.internal.util.Primitives.defaultValueForPrimitiveOrWrapper;
import static org.mockito.internal.util.ObjectMethodsGuru.isToString;
import static org.mockito.internal.util.ObjectMethodsGuru.isCompareToMethod;

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
import org.mockito.stubbing.Answer;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer_voidMethod() {
        // Test for a void method
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(MyVoidClass.class).invoking(1, "test");
        Object result = returnsEmptyValues.answer(invocation);
        assertNull(result);
    }

    @Test
    public void testAnswer_IntReturningMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(MyIntClass.class).invoking(1);
        Object result = returnsEmptyValues.answer(invocation);
        assertEquals(0, result.hashCode());
    }

    @Test
    public void testAnswer_IntegerReturningMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(MyIntegerClass.class).invoking(1);
        Object result = returnsEmptyValues.answer(invocation);
        assertEquals(0, result.hashCode());
    }

    @Test
    public void testAnswer_BooleanReturningMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(MyBooleanClass.class).invoking(1);
        Object result = returnsEmptyValues.answer(invocation);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testAnswer_BooleanWrapperReturningMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(MyBooleanWrapperClass.class).invoking(1);
        Object result = returnsEmptyValues.answer(invocation);
        assertEquals(false, result);
    }

    @Test
    public void testAnswer_StringReturningMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(MyStringClass.class).invoking(1);
        Object result = returnsEmptyValues.answer(invocation);
        assertNull(result);
    }

    @Test
    public void testAnswer_StringArrayReturningMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(MyStringArrayClass.class).invoking(1);
        Object result = returnsEmptyValues.answer(invocation);
        assertNull(result);
    }

    @Test
    public void testAnswer_CollectionReturningMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(MyCollectionClass.class).invoking(1);
        Object result = returnsEmptyValues.answer(invocation);
        assertEquals(new LinkedList<Object>(), result);
    }

    @Test
    public void testAnswer_SetReturningMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(MySetClass.class).invoking(1);
        Object result = returnsEmptyValues.answer(invocation);
        assertEquals(new HashSet<Object>(), result);
    }

    @Test
    public void testAnswer_HashSetReturningMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(MyHashSetClass.class).invoking(1);
        Object result = returnsEmptyValues.answer(invocation);
        assertEquals(new HashSet<Object>(), result);
    }

    @Test
    public void testAnswer_SortedSetReturningMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(MySortedSetClass.class).invoking(1);
        Object result = returnsEmptyValues.answer(invocation);
        assertEquals(new TreeSet<Object>(), result);
    }

    @Test
    public void testAnswer_TreeSetReturningMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(MyTreeSetClass.class).invoking(1);
        Object result = returnsEmptyValues.answer(invocation);
        assertEquals(new TreeSet<Object>(), result);
    }

    @Test
    public void testAnswer_LinkedHashSetReturningMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(MyLinkedHashSetClass.class).invoking(1);
        Object result = returnsEmptyValues.answer(invocation);
        assertEquals(new LinkedHashSet<Object>(), result);
    }

    @Test
    public void testAnswer_ListReturningMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(MyListClass.class).invoking(1);
        Object result = returnsEmptyValues.answer(invocation);
        assertEquals(new LinkedList<Object>(), result);
    }

    @Test
    public void testAnswer_LinkedListReturningMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(MyLinkedListClass.class).invoking(1);
        Object result = returnsEmptyValues.answer(invocation);
        assertEquals(new LinkedList<Object>(), result);
    }

    @Test
    public void testAnswer_ArrayListReturningMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(MyArrayListClass.class).invoking(1);
        Object result = returnsEmptyValues.answer(invocation);
        assertEquals(new ArrayList<Object>(), result);
    }

    @Test
    public void testAnswer_MapReturningMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(MyMapClass.class).invoking(1);
        Object result = returnsEmptyValues.answer(invocation);
        assertEquals(new HashMap<Object, Object>(), result);
    }

    @Test
    public void testAnswer_HashMapReturningMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(MyHashMapClass.class).invoking(1);
        Object result = returnsEmptyValues.answer(invocation);
        assertEquals(new HashMap<Object, Object>(), result);
    }

    @Test
    public void testAnswer_SortedMapReturningMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(MySortedMapClass.class).invoking(1);
        Object result = returnsEmptyValues.answer(invocation);
        assertEquals(new TreeMap<Object, Object>(), result);
    }

    @Test
    public void testAnswer_TreeMapReturningMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(MyTreeMapClass.class).invoking(1);
        Object result = returnsEmptyValues.answer(invocation);
        assertEquals(new TreeMap<Object, Object>(), result);
    }

    @Test
    public void testAnswer_LinkedHashMapReturningMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(MyLinkedHashMapClass.class).invoking(1);
        Object result = returnsEmptyValues.answer(invocation);
        assertEquals(new LinkedHashMap<Object, Object>(), result);
    }

    @Test
    public void testAnswer_toStringMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object mock = mock(MyClass.class);
        InvocationOnMock invocation = mock(MyClass.class).invoking(1).getMock();
        String result = (String) returnsEmptyValues.answer(invocation);
        assertEquals("Mock for MyClass, hashCode: " + mock.hashCode(), result);
    }

    @Test
    public void testAnswer_compareToMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object mock1 = mock(MyComparableClass.class);
        Object mock2 = mock(MyComparableClass.class);
        InvocationOnMock invocation1 = mock(MyComparableClass.class).invoking(1, mock1);
        InvocationOnMock invocation2 = mock(MyComparableClass.class).invoking(1, mock2);
        int result = (Integer) returnsEmptyValues.answer(invocation1);
        assertEquals(1, result.hashCode());
        int result2 = (Integer) returnsEmptyValues.answer(invocation2);
        assertEquals(0, result2.hashCode());
    }

    private static class MyVoidClass {}
    private static class MyIntClass {
        public int test() {
            return 0;
        }
    }
    private static class MyIntegerClass {
        public Integer test() {
            return 0;
        }
    }
    private static class MyBooleanClass {
        public boolean test() {
            return false;
        }
    }
    private static class MyBooleanWrapperClass {
        public Boolean test() {
            return false;
        }
    }
    private static class MyStringClass {
        public String test() {
            return "";
        }
    }
    private static class MyStringArrayClass {
        public String[] test() {
            return new String[0];
        }
    }
    private static class MyCollectionClass<T> {
        @SuppressWarnings("unchecked")
        public Collection<T> test() {
            return new LinkedList<T>();
        }
    }
    private static class MySetClass<T> extends MyCollectionClass<T> {
    }
    private static class MyHashSetClass<T> extends MySetClass<T> {
    }
    private static class MySortedSetClass<T> extends MySetClass<T> {
    }
    private static class MyTreeSetClass<T> extends MySortedSetClass<T> {
    }
    private static class MyLinkedHashSetClass<T> extends MySetClass<T> {
    }
    private static class MyListClass<T> {
        @SuppressWarnings("unchecked")
        public List<T> test() {
            return new LinkedList<T>();
        }
    }
    private static class MyLinkedListClass<T> extends MyListClass<T> {
    }
    private static class MyArrayListClass<T> extends MyListClass<T> {
    }
    private static class MyMapClass<K, V> {
        @SuppressWarnings("unchecked")
        public Map<K, V> test() {
            return new HashMap<K, V>();
        }
    }
    private static class MyHashMapClass<K, V> extends MyMapClass<K, V> {
    }
    private static class MySortedMapClass<K, V> extends MyMapClass<K, V> {
    }
    private static class MyTreeMapClass<K, V> extends MySortedMapClass<K, V> {
    }
    private static class MyLinkedHashMapClass<K, V> extends MyMapClass<K, V> {
    }
    private static class MyClass {
        public String toString() {
            return "";
        }
    }
    private static class MyComparableClass implements Comparable<MyComparableClass> {
        @Override
        public int compareTo(MyComparableClass other) {
            return 0;
        }
    }
}