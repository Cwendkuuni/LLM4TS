package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.Serializable;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues returnsEmptyValues;

    @Before
    public void setup() {
        returnsEmptyValues = new ReturnsEmptyValues();
    }

    @Test
    public void testToStringMethod() {
        Object mock = mock(String.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.setMethod(Mockito.method(String.class, "toString"));
        Object result = returnsEmptyValues.answer(invocation);

        String expected = "Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode();
        assertThat(result).isInstanceOf(String.class).isEqualTo(expected);
    }

    @Test
    public void testCompareToMethod() {
        Object mock1 = mock(Comparable.class);
        Object mock2 = mock(Comparable.class);
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.setMethod(Mockito.method(Comparable.class, "compareTo", Object.class));
        invocation.setArguments(mock2);
        int result = (int) returnsEmptyValues.answer(invocation);

        verify(mock1, Mockito.times(1)).compareTo(mock2);
        assertThat(result).isGreaterThan(0);
    }

    @Test
    public void testPrimitiveMethods() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Class<?> returnType = invocation.getMethod().getReturnType();

        if (Primitives.isPrimitiveOrWrapper(returnType)) {
            Object result = returnsEmptyValues.returnValueFor(returnType);
            Object expected = Primitives.defaultValueForPrimitiveOrWrapper(returnType);
            assertThat(result).isEqualTo(expected);
        }
    }

    @Test(expected = AssertionError.class)
    public void testCollectionTypes_ArrayList() {
        Class<?> returnType = Collection.class;
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.setMethod(Mockito.method(Collection.class, "size"));

        List<Object> result = (List<Object>) returnsEmptyValues.answer(invocation);
        result.add(new Object());
    }

    @Test(expected = AssertionError.class)
    public void testCollectionTypes_HashSet() {
        Class<?> returnType = Set.class;
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.setMethod(Mockito.method(Set.class, "add", Object.class));
        invocation.setArguments(new Object());

        Set<Object> result = (Set<Object>) returnsEmptyValues.answer(invocation);
        result.add(new Object());
    }

    @Test(expected = AssertionError.class)
    public void testCollectionTypes_LinkedHashSet() {
        Class<?> returnType = SortedSet.class;
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.setMethod(Mockito.method(SortedSet.class, "add", Object.class));
        invocation.setArguments(new Object());

        SortedSet<Object> result = (SortedSet<Object>) returnsEmptyValues.answer(invocation);
        result.add(new Object());
    }

    @Test(expected = AssertionError.class)
    public void testCollectionTypes_TreeSet() {
        Class<?> returnType = SortedSet.class;
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.setMethod(Mockito.method(SortedSet.class, "add", Object.class));
        invocation.setArguments(new Object());

        SortedSet<Object> result = (SortedSet<Object>) returnsEmptyValues.answer(invocation);
        result.add(new Object());
    }

    @Test(expected = AssertionError.class)
    public void testCollectionTypes_LinkedList() {
        Class<?> returnType = List.class;
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.setMethod(Mockito.method(List.class, "add", Object.class));
        invocation.setArguments(new Object());

        List<Object> result = (List<Object>) returnsEmptyValues.answer(invocation);
        result.add(new Object());
    }

    @Test(expected = AssertionError.class)
    public void testCollectionTypes_LinkedListWithGenericType() {
        Class<?> returnType = new ArrayList<Integer>().getClass();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.setMethod(Mockito.method(List.class, "add", Object.class));
        invocation.setArguments(Integer.valueOf(1));

        List<Integer> result = (List<Integer>) returnsEmptyValues.answer(invocation);
        result.add(Integer.valueOf(2));
    }

    @Test(expected = AssertionError.class)
    public void testCollectionTypes_HashMap() {
        Class<?> returnType = Map.class;
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.setMethod(Mockito.method(Map.class, "put", Object.class, Object.class));
        invocation.setArguments(new Object(), new Object());

        Map<Object, Object> result = (Map<Object, Object>) returnsEmptyValues.answer(invocation);
        result.put(new Object(), new Object());
    }

    @Test(expected = AssertionError.class)
    public void testCollectionTypes_HashMapWithGenericType() {
        Class<?> returnType = new HashMap<String, Integer>().getClass();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.setMethod(Mockito.method(Map.class, "put", Object.class, Object.class));
        invocation.setArguments(new String(), Integer.valueOf(1));

        Map<String, Integer> result = (Map<String, Integer>) returnsEmptyValues.answer(invocation);
        result.put("key", Integer.valueOf(2));
    }

    @Test(expected = AssertionError.class)
    public void testCollectionTypes_TreeMap() {
        Class<?> returnType = SortedMap.class;
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.setMethod(Mockito.method(SortedMap.class, "put", Object.class, Object.class));
        invocation.setArguments(new Object(), new Object());

        SortedMap<Object, Object> result = (SortedMap<Object, Object>) returnsEmptyValues.answer(invocation);
        result.put(new Object(), new Object());
    }

    @Test(expected = AssertionError.class)
    public void testCollectionTypes_TreeMapWithGenericType() {
        Class<?> returnType = new TreeMap<String, Integer>().getClass();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.setMethod(Mockito.method(SortedMap.class, "put", Object.class, Object.class));
        invocation.setArguments(new String(), Integer.valueOf(1));

        SortedMap<String, Integer> result = (SortedMap<String, Integer>) returnsEmptyValues.answer(invocation);
        result.put("key", Integer.valueOf(2));
    }

    @Test(expected = AssertionError.class)
    public void testCollectionTypes_LinkedHashMap() {
        Class<?> returnType = Map.class;
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.setMethod(Mockito.method(Map.class, "put", Object.class, Object.class));
        invocation.setArguments(new Object(), new Object());

        Map<Object, Object> result = (Map<Object, Object>) returnsEmptyValues.answer(invocation);
        result.put(new Object(), new Object());
    }

    @Test(expected = AssertionError.class)
    public void testCollectionTypes_LinkedHashMapWithGenericType() {
        Class<?> returnType = new LinkedHashMap<String, Integer>().getClass();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        invocation.setMethod(Mockito.method(Map.class, "put", Object.class, Object.class));
        invocation.setArguments(new String(), Integer.valueOf(1));

        Map<String, Integer> result = (Map<String, Integer>) returnsEmptyValues.answer(invocation);
        result.put("key", Integer.valueOf(2));
    }

    @Test
    public void testReturnNull() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Class<?> returnType = String.class;

        Object result = returnsEmptyValues.answer(invocation);
        assertThat(result).isNull();
    }
}