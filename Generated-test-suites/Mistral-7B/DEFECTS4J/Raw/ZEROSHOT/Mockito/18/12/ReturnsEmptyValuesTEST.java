package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

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
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Test;
import org.mockito.InvocationOnMock;
import org.mockito.internal.stubbing.answers.ReturnsEmptyValues;
import org.mockito.stubbing.Answer;

/**
 * Test class for ReturnsEmptyValues class
 *
 * @author <Your Name>
 */
public class ReturnsEmptyValuesTest {

	@Test
	public void testToStringMethod() {
		Object mock = mock(SomeClass.class);
		Answer<String> answer = new ReturnsEmptyValues();

		InvocationOnMock invocation = new InvocationOnMock(mock, SomeClass.class.getMethod("toString"), new Object[0]);
		String result = (String) answer.answer(invocation);

		assertEquals("Mock for SomeClass, hashCode: " + mock.hashCode(), result);
	}

	@Test
	public void testCompareToMethod() {
		Object mock1 = mock(SomeComparableClass.class);
		Object mock2 = mock(SomeComparableClass.class);
		Answer<Object> answer = new ReturnsEmptyValues();

		InvocationOnMock invocation1 = new InvocationOnMock(mock1, SomeComparableClass.class.getMethod("compareTo", SomeComparableClass.class), new Object[] { mock2 });
		int result1 = (Integer) answer.answer(invocation1);

		assertEquals(0, result1);

		InvocationOnMock invocation2 = new InvocationOnMock(mock1, SomeComparableClass.class.getMethod("compareTo", SomeComparableClass.class), new Object[] { mock1 });
		int result2 = (Integer) answer.answer(invocation2);

		assertNull(result2);
	}

	@Test
	public void testPrimitiveTypes() {
		Answer<Object> answer = new ReturnsEmptyValues();

		InvocationOnMock invocationInt = new InvocationOnMock(answer, Integer.TYPE.getMethod("valueOf", int.class), new Object[] { 1 });
		int resultInt = (Integer) answer.answer(invocationInt);

		assertEquals(1, resultInt.intValue());

		InvocationOnMock invocationBoolean = new InvocationOnMock(answer, Boolean.TYPE.getMethod("valueOf", boolean.class), new Object[] { true });
		boolean resultBoolean = (Boolean) answer.answer(invocationBoolean);

		assertTrue(resultBoolean);
	}

	@Test
	public void testEmptyCollections() {
		Answer<Object> answer = new ReturnsEmptyValues();

		Class<?> collectionType = Collection.class;
		Class<?> emptyCollectionType = HashSet.class;

		InvocationOnMock invocationCollection = new InvocationOnMock(answer, collectionType.getMethod("newInstance"), new Object[0]);
		Collection<Object> resultCollection = (Collection<Object>) answer.answer(invocationCollection);

		assertTrue(resultCollection.isEmpty());

		InvocationOnMock invocationEmptyCollection = new InvocationOnMock(answer, emptyCollectionType.getMethod("newInstance"), new Object[0]);
		Set<Object> resultEmptyCollection = (Set<Object>) answer.answer(invocationEmptyCollection);

		assertTrue(resultEmptyCollection.isEmpty());
	}

	@Test
	public void testOtherTypes() {
		Answer<Object> answer = new ReturnsEmptyValues();

		InvocationOnMock invocationMap = new InvocationOnMock(answer, Map.class.getMethod("newInstance"), new Object[0]);
		Map<Object, Object> resultMap = (Map<Object, Object>) answer.answer(invocationMap);

		assertNull(resultMap.get("key"));
	}

	private static class SomeClass {
	}

	private static class SomeComparableClass implements Comparable<SomeComparableClass> {
	}
}