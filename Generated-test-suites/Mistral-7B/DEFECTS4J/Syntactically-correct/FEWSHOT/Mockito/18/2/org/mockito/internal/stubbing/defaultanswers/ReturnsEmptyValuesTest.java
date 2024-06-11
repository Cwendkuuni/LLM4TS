package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

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
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerToStringMethod() {
        Object mock = mock(Object.class);
        assertEquals("Mock for Object, hashCode: " + mock.hashCode(), ReturnsEmptyValues.getInstance().answer(InvocationOnMock.method(Object.class, "toString()")));
    }

    @Test
    public void testAnswerCompareToMethod() {
        Object mock1 = mock(Object.class);
        Object mock2 = mock(Object.class);
        assertEquals(0, ReturnsEmptyValues.getInstance().answer(InvocationOnMock.method(Comparable.class, "compareTo", Object.class)).compareTo(mock1));
        assertEquals(1, ReturnsEmptyValues.getInstance().answer(InvocationOnMock.method(Comparable.class, "compareTo", Object.class)).compareTo(mock2));
    }

    @Test
    public void testReturnValueForPrimitiveTypes() {
        Class<?> byteType = byte.class;
        Byte byteValue = ReturnsEmptyValues.getInstance().returnValueFor(byteType);
        assertEquals((byte) 0, byteValue.byteValue());

        Class<?> intType = int.class;
        Integer intValue = ReturnsEmptyValues.getInstance().returnValueFor(intType);
        assertEquals(0, intValue.intValue());

        Class<?> longType = long.class;
        Long longValue = ReturnsEmptyValues.getInstance().returnValueFor(longType);
        assertEquals(0L, longValue.longValue());

        Class<?> floatType = float.class;
        Float floatValue = ReturnsEmptyValues.getInstance().returnValueFor(floatType);
        assertEquals(0.0f, floatValue.floatValue(), 0.001f);

        Class<?> doubleType = double.class;
        Double doubleValue = ReturnsEmptyValues.getInstance().returnValueFor(doubleType);
        assertEquals(0.0d, doubleValue.doubleValue(), 0.001d);

        Class<?> booleanType = boolean.class;
        Boolean booleanValue = ReturnsEmptyValues.getInstance().returnValueFor(booleanType);
        assertEquals(false, booleanValue.booleanValue());

        Class<?> charType = char.class;
        Character charValue = ReturnsEmptyValues.getInstance().returnValueFor(charType);
        assertEquals((char) 0, charValue.charValue());
    }

    @Test
    public void testReturnValueForCollectionTypes() {
        Class<?> collectionType = Collection.class;
        Collection<?> collectionValue = ReturnsEmptyValues.getInstance().returnValueFor(collectionType);
        assertNotNull(collectionValue);
        // The following assertion will fail due to the unmodifiable list nature of LinkedList.
        // assertFalse(collectionValue.isEmpty());
        assertEquals(0, collectionValue.size());

        Class<?> listType = List.class;
        List<?> listValue = ReturnsEmptyValues.getInstance().returnValueFor(listType);
        assertNotNull(listValue);
        // The following assertion will fail due to the unmodifiable list nature of LinkedList.
        // assertFalse(listValue.isEmpty());
        assertEquals(0, listValue.size());

        Class<?> arrayListType = ArrayList.class;
        ArrayList<?> arrayListValue = (ArrayList<?>) ReturnsEmptyValues.getInstance().returnValueFor(arrayListType);
        assertNotNull(arrayListValue);
        // The following assertion will fail due to the unmodifiable list nature of LinkedList.
        // assertFalse(arrayListValue.isEmpty());
        assertEquals(0, arrayListValue.size());

        Class<?> hashSetType = HashSet.class;
        Set<?> hashSetValue = ReturnsEmptyValues.getInstance().returnValueFor(hashSetType);
        assertNotNull(hashSetValue);
        assertTrue(hashSetValue.isEmpty());

        Class<?> linkedHashSetType = LinkedHashSet.class;
        Set<?> linkedHashSetValue = ReturnsEmptyValues.getInstance().returnValueFor(linkedHashSetType);
        assertNotNull(linkedHashSetValue);
        assertTrue(linkedHashSetValue.isEmpty());

        Class<?> treeSetType = TreeSet.class;
        SortedSet<?> treeSetValue = ReturnsEmptyValues.getInstance().returnValueFor(treeSetType);
        assertNotNull(treeSetValue);
        assertTrue(treeSetValue.isEmpty());

        Class<?> linkedTreeSetType = LinkedTreeSet.class;
        SortedSet<?> linkedTreeSetValue = ReturnsEmptyValues.getInstance().returnValueFor(linkedTreeSetType);
        assertNotNull(linkedTreeSetValue);
        assertTrue(linkedTreeSetValue.isEmpty());
    }

    @Test
    public void testReturnValueForMapTypes() {
        Class<?> mapType = Map.class;
        Map<?, ?> mapValue = ReturnsEmptyValues.getInstance().returnValueFor(mapType);
        assertNotNull(mapValue);
        assertTrue(mapValue.isEmpty());

        Class<?> hashMapType = HashMap.class;
        Map<String, Object> hashMapValue = (Map<String, Object>) ReturnsEmptyValues.getInstance().returnValueFor(hashMapType);
        assertNotNull(hashMapValue);
        assertTrue(hashMapValue.isEmpty());
    }
}