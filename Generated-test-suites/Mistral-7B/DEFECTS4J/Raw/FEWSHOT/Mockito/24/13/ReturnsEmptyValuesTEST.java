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
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsEmptyValuesTest {

	@Test
	public void testAnswer_toStringMethod() {
		Object mock = mock(Object.class);
		ReturnsEmptyValues answer = new ReturnsEmptyValues();
		String expected = "Mock for Object, hashCode: " + mock.hashCode();
		assertEquals(expected, answer.answer(mock(InvocationOnMock.class, "toString()")));
	}

	@Test
	public void testAnswer_compareToMethod() {
		Object mock1 = mock(Comparable.class);
		Object mock2 = mock(Comparable.class);
		ReturnsEmptyValues answer = new ReturnsEmptyValues();
		verify(mock1).compareTo(mock2);
		assertEquals(1, answer.answer(mock(InvocationOnMock.class, "compareTo(Object other)")));
	}

	@Test
	public void testReturnValueFor_primitiveTypes() {
		ReturnsEmptyValues answer = new ReturnsEmptyValues();
		Class<?> types[] = { byte.class, short.class, int.class, long.class, float.class, double.class, boolean.class, char.class };
		for (Class<?> type : types) {
			Object result = answer.returnValueFor(type);
			assertNotNull(result);
			assertEquals(Primitives.defaultValueForPrimitiveOrWrapper(type), result);
		}
	}

	@Test
	public void testReturnValueFor_Collection() {
		ReturnsEmptyValues answer = new ReturnsEmptyValues();
		Collection<Object> expected = new LinkedList<Object>();
		assertEquals(expected, answer.returnValueFor(Collection.class));
	}

	@Test
	public void testReturnValueFor_Set() {
		ReturnsEmptyValues answer = new ReturnsEmptyValues();
		Set<Object> expected = new HashSet<Object>();
		assertEquals(expected, answer.returnValueFor(Set.class));
		assertEquals(expected, answer.returnValueFor(HashSet.class));
		assertEquals(expected, answer.returnValueFor(SortedSet.class));
		assertEquals(expected, answer.returnValueFor(TreeSet.class));
		assertEquals(expected, answer.returnValueFor(LinkedHashSet.class));
	}

	@Test
	public void testReturnValueFor_List() {
		ReturnsEmptyValues answer = new ReturnsEmptyValues();
		List<Object> expected = new LinkedList<Object>();
		assertEquals(expected, answer.returnValueFor(List.class));
		assertEquals(expected, answer.returnValueFor(LinkedList.class));
		assertEquals(expected, answer.returnValueFor(ArrayList.class));
	}

	@Test
	public void testReturnValueFor_Map() {
		ReturnsEmptyValues answer = new ReturnsEmptyValues();
		Map<Object, Object> expected = new HashMap<Object, Object>();
		assertEquals(expected, answer.returnValueFor(Map.class));
		assertEquals(expected, answer.returnValueFor(HashMap.class));
		assertEquals(expected, answer.returnValueFor(SortedMap.class));
		assertEquals(expected, answer.returnValueFor(TreeMap.class));
		assertEquals(expected, answer.returnValueFor(LinkedHashMap.class));
	}
}