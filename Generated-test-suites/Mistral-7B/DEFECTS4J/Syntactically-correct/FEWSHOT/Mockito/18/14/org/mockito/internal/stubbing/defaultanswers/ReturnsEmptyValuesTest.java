package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.internal.util.Primitives.isPrimitiveOrWrapper;

import org.junit.Test;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.invocation.InvocationOnMock;

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

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer_PrimitiveTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Answer answer = returnsEmptyValues;

        Class<?> type = int.class;
        InvocationOnMock invocation = new InvocationOnMock(null, null, new Object[]{null}, type);
        Object result = answer.answer(invocation);
        assertEquals(0, result);

        type = boolean.class;
        invocation = new InvocationOnMock(null, null, new Object[]{null}, type);
        result = answer.answer(invocation);
        assertEquals(false, result);

        type = char.class;
        invocation = new InvocationOnMock(null, null, new Object[]{null}, type);
        result = answer.answer(invocation);
        assertEquals((char) 0, result);

        type = byte.class;
        invocation = new InvocationOnMock(null, null, new Object[]{null}, type);
        result = answer.answer(invocation);
        assertEquals((byte) 0, result);

        type = short.class;
        invocation = new InvocationOnMock(null, null, new Object[]{null}, type);
        result = answer.answer(invocation);
        assertEquals((short) 0, result);

        type = long.class;
        invocation = new InvocationOnMock(null, null, new Object[]{null}, type);
        result = answer.answer(invocation);
        assertEquals(0L, result);

        type = float.class;
        invocation = new InvocationOnMock(null, null, new Object[]{null}, type);
        result = answer.answer(invocation);
        assertEquals(0f, result, 0.01f);

        type = double.class;
        invocation = new InvocationOnMock(null, null, new Object[]{null}, type);
        result = answer.answer(invocation);
        assertEquals(0d, result, 0.001d);

        type = Integer.class;
        invocation = new InvocationOnMock(null, null, new Object[]{null}, type);
        result = answer.answer(invocation);
        assertEquals(0, result);

        type = Boolean.class;
        invocation = new InvocationOnMock(null, null, new Object[]{null}, type);
        result = answer.answer(invocation);
        assertEquals(false, result);

        type = Character.class;
        invocation = new InvocationOnMock(null, null, new Object[]{null}, type);
        result = answer.answer(invocation);
        assertEquals('\u0000', result);

        type = Byte.class;
        invocation = new InvocationOnMock(null, null, new Object[]{null}, type);
        result = answer.answer(invocation);
        assertEquals((byte) 0, result);

        type = Short.class;
        invocation = new InvocationOnMock(null, null, new Object[]{null}, type);
        result = answer.answer(invocation);
        assertEquals((short) 0, result);

        type = Long.class;
        invocation = new InvocationOnMock(null, null, new Object[]{null}, type);
        result = answer.answer(invocation);
        assertEquals(0L, result);

        type = Float.class;
        invocation = new InvocationOnMock(null, null, new Object[]{null}, type);
        result = answer.answer(invocation);
        assertEquals(0f, result, 0.01f);

        type = Double.class;
        invocation = new InvocationOnMock(null, null, new Object[]{null}, type);
        result = answer.answer(invocation);
        assertEquals(0d, result, 0.001d);
    }

    @Test
    public void testAnswer_CollectionTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Answer answer = returnsEmptyValues;

        Class<?> type = Collection.class;
        InvocationOnMock invocation = new InvocationOnMock(null, null, new Object[]{null}, type);
        Collection result = (Collection) answer.answer(invocation);
        assertNotNull(result);
        assertTrue(result.isEmpty());

        type = Set.class;
        invocation = new InvocationOnMock(null, null, new Object[]{null}, type);
        result = (Set) answer.answer(invocation);
        assertNotNull(result);
        assertTrue(result.isEmpty());

        type = HashSet.class;
        invocation = new InvocationOnMock(null, null, new Object[]{null}, type);
        result = (Set) answer.answer(invocation);
        assertNotNull(result);
        assertTrue(result.isEmpty());

        type = SortedSet.class;
        invocation = new InvocationOnMock(null, null, new Object[]{null}, type);
        result = (SortedSet) answer.answer(invocation);
        assertNotNull(result);
        assertTrue(result.isEmpty());

        type = TreeSet.class;
        invocation = new InvocationOnMock(null, null, new Object[]{null}, type);
        result = (SortedSet) answer.answer(invocation);
        assertNotNull(result);
        assertTrue(result.isEmpty());

        type = LinkedHashSet.class;
        invocation = new InvocationOnMock(null, null, new Object[]{null}, type);
        result = (Set) answer.answer(invocation);
        assertNotNull(result);
        assertTrue(result.isEmpty());

        type = List.class;
        invocation = new InvocationOnMock(null, null, new Object[]{null}, type);
        List resultList = (List) answer.answer(invocation);
        assertNotNull(resultList);
        assertTrue(resultList.isEmpty());

        type = LinkedList.class;
        invocation = new InvocationOnMock(null, null, new Object[]{null}, type);
        resultList = (List) answer.answer(invocation);
        assertNotNull(resultList);
        assertTrue(resultList.isEmpty());

        type = ArrayList.class;
        invocation = new InvocationOnMock(null, null, new Object[]{null}, type);
        resultList = (List) answer.answer(invocation);
        assertNotNull(resultList);
        assertTrue(resultList.isEmpty());
    }

    @Test
    public void testAnswer_MapTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Answer answer = returnsEmptyValues;

        Class<?> type = Map.class;
        InvocationOnMock invocation = new InvocationOnMock(null, null, new Object[]{null}, type);
        Map<Object, Object> result = (Map<Object, Object>) answer.answer(invocation);
        assertNotNull(result);
        assertTrue(result.isEmpty());

        type = HashMap.class;
        invocation = new InvocationOnMock(null, null, new Object[]{null}, type);
        result = (Map<Object, Object>) answer.answer(invocation);
        assertNotNull(result);
        assertTrue(result.isEmpty());

        type = SortedMap.class;
        invocation = new InvocationOnMock(null, null, new Object[]{null}, type);
        result = (SortedMap<Object, Object>) answer.answer(invocation);
        assertNotNull(result);
        assertTrue(result.isEmpty());

        type = TreeMap.class;
        invocation = new InvocationOnMock(null, null, new Object[]{null}, type);
        result = (SortedMap<Object, Object>) answer.answer(invocation);
        assertNotNull(result);
        assertTrue(result.isEmpty());

        type = LinkedHashMap.class;
        invocation = new InvocationOnMock(null, null, new Object[]{null}, type);
        result = (Map<Object, Object>) answer.answer(invocation);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testIsToStringMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Answer answer = returnsEmptyValues;

        InvocationOnMock invocation = new InvocationOnMock(null, null, new Object[]{null}, String.class);
        Object mock = new Object() {};
        assertEquals("Mock for [class org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues$Test$1@1b327a52]", answer.answer(invocation));

        MockName mockName = MockUtil.getMockName(mock);
        assertEquals(mockName.toString(), answer.answer(invocation));
    }

    @Test
    public void testIsCompareToMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Answer answer = returnsEmptyValues;

        InvocationOnMock invocation = new InvocationOnMock(null, null, new Object[]{new Object() {}}, Comparable.class);
        assertEquals(0, answer.answer(invocation), 0);

        // different objects, should return some other value
        Object otherObject = new Object() {};
        assertEquals(1, answer.answer(invocation), 1);
    }

    @Test
    public void testReturnValueFor_ArrayTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Class<?>[] types = {Collection.class, Set.class, HashSet.class, SortedSet.class, TreeSet.class, LinkedHashSet.class,
                List.class, LinkedList.class, ArrayList.class, Map.class, HashMap.class, SortedMap.class, TreeMap.class,
                LinkedHashMap.class};

        for (Class<?> type : types) {
            if (isPrimitiveOrWrapper(type)) continue;

            InvocationOnMock invocation = new InvocationOnMock(null, null, new Object[]{null}, type);
            Object result = returnsEmptyValues.returnValueFor(type);
            assertNotNull(result);

            if (Collection.class.isAssignableFrom(type)) {
                Collection collection = (Collection) result;
                assertTrue(collection.isEmpty());
            } else if (Set.class.isAssignableFrom(type)) {
                Set set = (Set) result;
                assertTrue(set.isEmpty());
            } else if (Map.class.isAssignableFrom(type)) {
                Map<Object, Object> map = (Map<Object, Object>) result;
                assertTrue(map.isEmpty());
            }
        }
    }
}