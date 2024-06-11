package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

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

import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;

/**
 * Tests for the {@link ReturnsEmptyValues} class.
 */
public class ReturnsEmptyValuesTest {

	@Test
	public void testToStringMethod() {
		Object mock = mock(String.class);
		ReturnsEmptyValues answer = new ReturnsEmptyValues();
		InvocationOnMock invocation = Mockito.mockingDetails(answer.answer(Mockito.argThat(m -> m.getMethod().getName().equals("toString"))), mock);
		String expectedResult = "Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode();
		assertEquals(expectedResult, answer.answer(invocation));
	}

	@Test
	public void testCompareToMethod() {
		Object mock = mock(Comparable.class);
		ReturnsEmptyValues answer = new ReturnsEmptyValues();
		InvocationOnMock invocation = Mockito.mockingDetails(answer.answer(Mockito.argThat(m -> m.getMethod().getName().equals("compareTo"))), mock, new Object[] { mock });
		assertEquals(0, answer.answer(invocation));
		assertEquals(1, answer.answer(invocation).hashCode());
	}

	@Test
	public void testGetEmptyCollection() {
		ReturnsEmptyValues answer = new ReturnsEmptyValues();
		Class<?> collectionType = Collection.class;
		Object result = answer.returnValueFor(collectionType);
		assertEquals(LinkedList.class, result.getClass());
		assertEquals(true, result instanceof Collection);
		assertEquals(true, result instanceof List);
		assertEquals(0, ((Collection) result).size());
	}

	@Test
	public void testGetEmptySet() {
		ReturnsEmptyValues answer = new ReturnsEmptyValues();
		Class<?> setType = Set.class;
		Object result = answer.returnValueFor(setType);
		assertEquals(HashSet.class, result.getClass());
		assertEquals(true, result instanceof Set);
		assertEquals(true, result instanceof Collection);
		assertEquals(0, ((Set) result).size());
	}

	@Test
	public void testGetEmptyHashSet() {
		ReturnsEmptyValues answer = new ReturnsEmptyValues();
		Class<?> hashSetType = HashSet.class;
		Object result = answer.returnValueFor(hashSetType);
		assertEquals(LinkedHashSet.class, result.getClass());
		assertEquals(true, result instanceof Set);
		assertEquals(true, result instanceof Collection);
		assertEquals(0, ((Set) result).size());
	}

	@Test
	public void testGetEmptySortedSet() {
		ReturnsEmptyValues answer = new ReturnsEmptyValues();
		Class<?> sortedSetType = SortedSet.class;
		Object result = answer.returnValueFor(sortedSetType);
		assertEquals(TreeSet.class, result.getClass());
		assertEquals(true, result instanceof Set);
		assertEquals(true, result instanceof Collection);
		assertEquals(0, ((Set) result).size());
	}

	@Test
	public void testGetEmptyTreeSet() {
		ReturnsEmptyValues answer = new ReturnsEmptyValues();
		Class<?> treeSetType = TreeSet.class;
		Object result = answer.returnValueFor(treeSetType);
		assertEquals(TreeSet.class, result.getClass());
		assertEquals(true, result instanceof Set);
		assertEquals(true, result instanceof Collection);
		assertEquals(0, ((Set) result).size());
	}

	@Test
	public void testGetEmptyList() {
		ReturnsEmptyValues answer = new ReturnsEmptyValues();
		Class<?> listType = List.class;
		Object result = answer.returnValueFor(listType);
		assertEquals(LinkedList.class, result.getClass());
		assertEquals(true, result instanceof List);
		assertEquals(true, result instanceof Collection);
		assertEquals(0, ((List) result).size());
	}

	@Test
	public void testGetEmptyLinkedList() {
		ReturnsEmptyValues answer = new ReturnsEmptyValues();
		Class<?> linkedListType = LinkedList.class;
		Object result = answer.returnValueFor(linkedListType);
		assertEquals(LinkedList.class, result.getClass());
		assertEquals(true, result instanceof List);
		assertEquals(true, result instanceof Collection);
		assertEquals(0, ((List) result).size());
	}

	@Test
	public void testGetEmptyArrayList() {
		ReturnsEmptyValues answer = new ReturnsEmptyValues();
		Class<?> arrayListType = ArrayList.class;
		Object result = answer.returnValueFor(arrayListType);
		assertEquals(ArrayList.class, result.getClass());
		assertEquals(true, result instanceof List);
		assertEquals(true, result instanceof Collection);
		assertEquals(0, ((List) result).size());
	}

	@Test
	public void testGetEmptyMap() {
		ReturnsEmptyValues answer = new ReturnsEmptyValues();
		Class<?> mapType = Map.class;
		Object result = answer.returnValueFor(mapType);
		assertEquals(HashMap.class, result.getClass());
		assertEquals(true, result instanceof Map);
		assertEquals(true, result instanceof Collection);
		assertEquals(0, ((Map) result).size());
	}

	@Test
	public void testGetEmptyHashMap() {
		ReturnsEmptyValues answer = new ReturnsEmptyValues();
		Class<?> hashMapType = HashMap.class;
		Object result = answer.returnValueFor(hashMapType);
		assertEquals(HashMap.class, result.getClass());
		assertEquals(true, result instanceof Map);
		assertEquals(true, result instanceof Collection);
		assertEquals(0, ((Map) result).size());
	}

	@Test
	public void testGetEmptySortedMap() {
		ReturnsEmptyValues answer = new ReturnsEmptyValues();
		Class<?> sortedMapType = SortedMap.class;
		Object result = answer.returnValueFor(sortedMapType);
		assertEquals(TreeMap.class, result.getClass());
		assertEquals(true, result instanceof Map);
		assertEquals(true, result instanceof Collection);
		assertEquals(0, ((Map) result).size());
	}

	@Test
	public void testGetEmptyTreeMap() {
		ReturnsEmptyValues answer = new ReturnsEmptyValues();
		Class<?> treeMapType = TreeMap.class;
		Object result = answer.returnValueFor(treeMapType);
		assertEquals(TreeMap.class, result.getClass());
		assertEquals(true, result instanceof Map);
		assertEquals(true, result instanceof Collection);
		assertEquals(0, ((Map) result).size());
	}

	@Test
	public void testGetEmptyLinkedHashMap() {
		ReturnsEmptyValues answer = new ReturnsEmptyValues();
		Class<?> linkedHashMapType = LinkedHashMap.class;
		Object result = answer.returnValueFor(linkedHashMapType);
		assertEquals(LinkedHashMap.class, result.getClass());
		assertEquals(true, result instanceof Map);
		assertEquals(true, result instanceof Collection);
		assertEquals(0, ((Map) result).size());
	}

	@Test
	public void testGetDefaultValueForString() {
		ReturnsEmptyValues answer = new ReturnsEmptyValues();
		Class<?> stringType = String.class;
		Object result = answer.returnValueFor(stringType);
		assertEquals(null, result);
	}

	@Test
	public void testGetDefaultValueForInteger() {
		ReturnsEmptyValues answer = new ReturnsEmptyValues();
		Class<?> intType = int.class;
		Object result = answer.returnValueFor(intType);
		assertEquals(Integer.MIN_VALUE, result);
	}

	@Test
	public void testGetDefaultValueForBoolean() {
		ReturnsEmptyValues answer = new ReturnsEmptyValues();
		Class<?> booleanType = boolean.class;
		Object result = answer.returnValueFor(booleanType);
		assertEquals(false, result);
	}

	@Test
	public void testGetDefaultValueForByte() {
		ReturnsEmptyValues answer = new ReturnsEmptyValues();
		Class<?> byteType = byte.class;
		Object result = answer.returnValueFor(byteType);
		assertEquals((byte) 0x00, result);
	}

	@Test
	public void testGetDefaultValueForShort() {
		ReturnsEmptyValues answer = new ReturnsEmptyValues();
		Class<?> shortType = short.class;
		Object result = answer.returnValueFor(shortType);
		assertEquals((short) 0x0000, result);
	}

	@Test
	public void testGetDefaultValueForChar() {
		ReturnsEmptyValues answer = new ReturnsEmptyValues();
		Class<?> charType = char.class;
		Object result = answer.returnValueFor(charType);
		assertEquals('\u0000', result);
	}

	@Test
	public void testGetDefaultValueForLong() {
		ReturnsEmptyValues answer = new ReturnsEmptyValues();
		Class<?> longType = long.class;
		Object result = answer.returnValueFor(longType);
		assertEquals(Long.MIN_VALUE, result);
	}

	@Test
	public void testGetDefaultValueForFloat() {
		ReturnsEmptyValues answer = new ReturnsEmptyValues();
		Class<?> floatType = float.class;
		Object result = answer.returnValueFor(floatType);
		assertEquals(0f, result, 0.0);
	}

	@Test
	public void testGetDefaultValueForDouble() {
		ReturnsEmptyValues answer = new ReturnsEmptyValues();
		Class<?> doubleType = double.class;
		Object result = answer.returnValueFor(doubleType);
		assertEquals(0.0d, result, 0.0);
	}

}