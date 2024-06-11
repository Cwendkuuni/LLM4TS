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

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // test toString() method
        Object mock = new Object();
        InvocationOnMock invocation = MockitoTestUtils.createInvocationOnMock(null, null, "toString", mock);
        String result = (String) returnsEmptyValues.answer(invocation);
        assertEquals("Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode(), result);

        // test compareTo() method
        Object other = new Object();
        InvocationOnMock invocationCompareTo = MockitoTestUtils.createInvocationOnMock(null, null, "compareTo", returnsEmptyValues, other);
        Long resultCompareTo = (Long) returnsEmptyValues.answer(invocationCompareTo);
        assertEquals(1, resultCompareTo.longValue());

        // test primitive types
        Class<?> intType = int.class;
        Object intAnswer = returnsEmptyValues.answer(MockitoTestUtils.createInvocationOnMock(null, null, "intValue", null, intType));
        assertEquals(0, intAnswer);

        Class<?> charType = char.class;
        Object charAnswer = returnsEmptyValues.answer(MockitoTestUtils.createInvocationOnMock(null, null, "charValue", null, charType));
        assertEquals('\u0000', charAnswer);

        Class<?> booleanType = boolean.class;
        Object booleanAnswer = returnsEmptyValues.answer(MockitoTestUtils.createInvocationOnMock(null, null, "booleanValue", null, booleanType));
        assertEquals(false, booleanAnswer);

        // test wrapper classes
        Class<?> intWrapperType = Integer.class;
        Object intWrapperAnswer = returnsEmptyValues.answer(MockitoTestUtils.createInvocationOnMock(null, null, "intValue", null, intWrapperType));
        assertEquals(0, intWrapperAnswer.intValue());

        Class<?> charWrapperType = Character.class;
        Object charWrapperAnswer = returnsEmptyValues.answer(MockitoTestUtils.createInvocationOnMock(null, null, "charValue", null, charWrapperType));
        assertEquals('\u0000', charWrapperAnswer.charValue());

        Class<?> booleanWrapperType = Boolean.class;
        Object booleanWrapperAnswer = returnsEmptyValues.answer(MockitoTestUtils.createInvocationOnMock(null, null, "booleanValue", null, booleanWrapperType));
        assertEquals(false, booleanWrapperAnswer.booleanValue());

        // test Collections
        Class<?> collectionType = Collection.class;
        Object collectionAnswer = returnsEmptyValues.answer(MockitoTestUtils.createInvocationOnMock(null, null, null, null, collectionType));
        assertNotNull(collectionAnswer);
        assertTrue(collectionAnswer instanceof Collection);
        assertTrue(collectionAnswer.getClass().isInstance(new LinkedList<>()));
        assertEquals(0, ((Collection<?>) collectionAnswer).size());

        Class<?> listType = List.class;
        Object listAnswer = returnsEmptyValues.answer(MockitoTestUtils.createInvocationOnMock(null, null, null, null, listType));
        assertNotNull(listAnswer);
        assertTrue(listAnswer instanceof List);
        assertTrue(listAnswer.getClass().isInstance(new LinkedList<>()));
        assertEquals(0, ((List<?>) listAnswer).size());

        Class<?> setType = Set.class;
        Object setAnswer = returnsEmptyValues.answer(MockitoTestUtils.createInvocationOnMock(null, null, null, null, setType));
        assertNotNull(setAnswer);
        assertTrue(setAnswer instanceof Set);
        assertTrue(setAnswer.getClass().isInstance(new HashSet<>()));
        assertEquals(0, ((Set<?>) setAnswer).size());

        Class<?> hashSetType = HashSet.class;
        Object hashSetAnswer = returnsEmptyValues.answer(MockitoTestUtils.createInvocationOnMock(null, null, null, null, hashSetType));
        assertNotNull(hashSetAnswer);
        assertTrue(hashSetAnswer instanceof Set);
        assertTrue(hashSetAnswer.getClass().isInstance(new HashSet<>()));
        assertEquals(0, ((Set<?>) hashSetAnswer).size());

        Class<?> linkedHashSetType = LinkedHashSet.class;
        Object linkedHashSetAnswer = returnsEmptyValues.answer(MockitoTestUtils.createInvocationOnMock(null, null, null, null, linkedHashSetType));
        assertNotNull(linkedHashSetAnswer);
        assertTrue(linkedHashSetAnswer instanceof Set);
        assertTrue(linkedHashSetAnswer.getClass().isInstance(new LinkedHashSet<>()));
        assertEquals(0, ((Set<?>) linkedHashSetAnswer).size());

        Class<?> mapType = Map.class;
        Object mapAnswer = returnsEmptyValues.answer(MockitoTestUtils.createInvocationOnMock(null, null, null, null, mapType));
        assertNotNull(mapAnswer);
        assertTrue(mapAnswer instanceof Map);
        assertTrue(mapAnswer.getClass().isInstance(new HashMap<>()));
        assertEquals(0, ((Map<?, ?>) mapAnswer).size());

        Class<?> hashMapType = HashMap.class;
        Object hashMapAnswer = returnsEmptyValues.answer(MockitoTestUtils.createInvocationOnMock(null, null, null, null, hashMapType));
        assertNotNull(hashMapAnswer);
        assertTrue(hashMapAnswer instanceof Map);
        assertTrue(hashMapAnswer.getClass().isInstance(new HashMap<>()));
        assertEquals(0, ((Map<?, ?>) hashMapAnswer).size());

        Class<?> sortedMapType = SortedMap.class;
        Object sortedMapAnswer = returnsEmptyValues.answer(MockitoTestUtils.createInvocationOnMock(null, null, null, null, sortedMapType));
        assertNotNull(sortedMapAnswer);
        assertTrue(sortedMapAnswer instanceof SortedMap);
        assertTrue(sortedMapAnswer.getClass().isInstance(new TreeMap<>()));
        assertEquals(0, ((SortedMap<?, ?>) sortedMapAnswer).size());

        Class<?> treeMapType = TreeMap.class;
        Object treeMapAnswer = returnsEmptyValues.answer(MockitoTestUtils.createInvocationOnMock(null, null, null, null, treeMapType));
        assertNotNull(treeMapAnswer);
        assertTrue(treeMapAnswer instanceof SortedMap);
        assertTrue(treeMapAnswer.getClass().isInstance(new TreeMap<>()));
        assertEquals(0, ((SortedMap<?, ?>) treeMapAnswer).size());

        Class<?> linkedHashMapType = LinkedHashMap.class;
        Object linkedHashMapAnswer = returnsEmptyValues.answer(MockitoTestUtils.createInvocationOnMock(null, null, null, null, linkedHashMapType));
        assertNotNull(linkedHashMapAnswer);
        assertTrue(linkedHashMapAnswer instanceof Map);
        assertTrue(linkedHashMapAnswer.getClass().isInstance(new LinkedHashMap<>()));
        assertEquals(0, ((Map<?, ?>) linkedHashMapAnswer).size());

        // test other types
        Class<?> stringType = String.class;
        Object stringAnswer = returnsEmptyValues.answer(MockitoTestUtils.createInvocationOnMock(null, null, null, null, stringType));
        assertNull(stringAnswer);

        Class<?> byteType = byte.class;
        Object byteAnswer = returnsEmptyValues.answer(MockitoTestUtils.createInvocationOnMock(null, null, "byteValue", null, byteType));
        assertEquals((byte) 0, byteAnswer);

        Class<?> shortType = short.class;
        Object shortAnswer = returnsEmptyValues.answer(MockitoTestUtils.createInvocationOnMock(null, null, "shortValue", null, shortType));
        assertEquals((short) 0, shortAnswer);

        Class<?> longType = long.class;
        Object longAnswer = returnsEmptyValues.answer(MockitoTestUtils.createInvocationOnMock(null, null, "longValue", null, longType));
        assertEquals(0L, longAnswer);

        Class<?> floatType = float.class;
        Object floatAnswer = returnsEmptyValues.answer(MockitoTestUtils.createInvocationOnMock(null, null, "floatValue", null, floatType));
        assertEquals(0f, floatAnswer, 0.001f);

        Class<?> doubleType = double.class;
        Object doubleAnswer = returnsEmptyValues.answer(MockitoTestUtils.createInvocationOnMock(null, null, "doubleValue", null, doubleType));
        assertEquals(0d, doubleAnswer, 0.001d);

        Class<?> byteArrayType = byte[].class;
        Object byteArrayAnswer = returnsEmptyValues.answer(MockitoTestUtils.createInvocationOnMock(null, null, null, null, byteArrayType));
        assertNull(byteArrayAnswer);

        Class<?> charArrayType = char[].class;
        Object charArrayAnswer = returnsEmptyValues.answer(MockitoTestUtils.createInvocationOnMock(null, null, null, null, charArrayType));
        assertNull(charArrayAnswer);

        Class<?> booleanArrayType = boolean[].class;
        Object booleanArrayAnswer = returnsEmptyValues.answer(MockitoTestUtils.createInvocationOnMock(null, null, null, null, booleanArrayType));
        assertNull(booleanArrayAnswer);

        Class<?> intArrayType = int[].class;
        Object intArrayAnswer = returnsEmptyValues.answer(MockitoTestUtils.createInvocationOnMock(null, null, null, null, intArrayType));
        assertNull(intArrayAnswer);

        Class<?> intArrayWrapperType = Integer[].class;
        Object intArrayWrapperAnswer = returnsEmptyValues.answer(MockitoTestUtils.createInvocationOnMock(null, null, null, null, intArrayWrapperType));
        assertNull(intArrayWrapperAnswer);

        Class<?> stringArrayType = String[].class;
        Object stringArrayAnswer = returnsEmptyValues.answer(MockitoTestUtils.createInvocationOnMock(null, null, null, null, stringArrayType));
        assertNull(stringArrayAnswer);
    }

    private static InvocationOnMock createInvocationOnMock(Object mock, Object[] args, String methodName, Object... expectedArguments) {
        try {
            return new InvocationOnMock(mock, methodName, args == null ? new Class[0] : args);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Cannot find method " + methodName + " in class " + mock.getClass(), e);
        }
    }
}

class MockitoTestUtils {
    public static InvocationOnMock createInvocationOnMock(Object mock, Object[] args, String methodName, Object... expectedArguments) {
        try {
            return new InvocationOnMock(mock, methodName, args == null ? new Class[0] : args);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Cannot find method " + methodName + " in class " + mock.getClass(), e);
        }
    }
}