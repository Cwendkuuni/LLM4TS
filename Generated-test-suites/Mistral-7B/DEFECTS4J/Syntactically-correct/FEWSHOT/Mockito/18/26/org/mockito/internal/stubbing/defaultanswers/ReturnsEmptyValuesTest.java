package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.mockito.ArgumentMatchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsEmptyValuesTest {

    @Test
    public void test_returnValueFor_primitiveTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        assertEquals(0, returnsEmptyValues.answer(ArgumentMatchers.method(Boolean.class, "booleanValue")).hashCode());
        assertEquals(false, (Boolean) returnsEmptyValues.answer(ArgumentMatchers.method(Boolean.class, "booleanValue")));
        assertEquals(0L, returnsEmptyValues.answer(ArgumentMatchers.method(Byte.class, "byteValue")).hashCode());
        assertEquals((byte) 0, (Byte) returnsEmptyValues.answer(ArgumentMatchers.method(Byte.class, "byteValue")));
        assertEquals(0d, returnsEmptyValues.answer(ArgumentMatchers.method(Double.class, "doubleValue")).hashCode());
        assertEquals(0.0d, (Double) returnsEmptyValues.answer(ArgumentMatchers.method(Double.class, "doubleValue")));
        assertEquals(0f, returnsEmptyValues.answer(ArgumentMatchers.method(Float.class, "floatValue")).hashCode());
        assertEquals(0.0f, (Float) returnsEmptyValues.answer(ArgumentMatchers.method(Float.class, "floatValue")));
        assertEquals(0, returnsEmptyValues.answer(ArgumentMatchers.method(Integer.class, "intValue")).hashCode());
        assertEquals(0, (Integer) returnsEmptyValues.answer(ArgumentMatchers.method(Integer.class, "intValue")));
        assertEquals(0, returnsEmptyValues.answer(ArgumentMatchers.method(Long.class, "longValue")).hashCode());
        assertEquals(0L, (Long) returnsEmptyValues.answer(ArgumentMatchers.method(Long.class, "longValue")));
        assertEquals(0, returnsEmptyValues.answer(ArgumentMatchers.method(Short.class, "shortValue")).hashCode());
        assertEquals((short) 0, (Short) returnsEmptyValues.answer(ArgumentMatchers.method(Short.class, "shortValue")));
        assertEquals(0, returnsEmptyValues.answer(ArgumentMatchers.method(Character.class, "charValue")).hashCode());
        assertEquals('\u0000', (Character) returnsEmptyValues.answer(ArgumentMatchers.method(Character.class, "charValue")));

        // Testing the wrapper classes
        assertEquals(0, returnsEmptyValues.answer(ArgumentMatchers.method(Integer.class, "valueOf", Integer.TYPE)).hashCode());
        assertEquals(0, (Integer) returnsEmptyValues.answer(ArgumentMatchers.method(Integer.class, "valueOf", Integer.TYPE)));
        assertEquals(0L, returnsEmptyValues.answer(ArgumentMatchers.method(Long.class, "valueOf", Long.TYPE)).hashCode());
        assertEquals(0L, (Long) returnsEmptyValues.answer(ArgumentMatchers.method(Long.class, "valueOf", Long.TYPE)));
        assertEquals(0d, returnsEmptyValues.answer(ArgumentMatchers.method(Double.class, "valueOf", Double.TYPE)).hashCode());
        assertEquals(0.0d, (Double) returnsEmptyValues.answer(ArgumentMatchers.method(Double.class, "valueOf", Double.TYPE)));
    }

    @Test
    public void test_returnValueFor_collectionTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // Testing empty collections
        assertEquals(new LinkedList<>(), returnsEmptyValues.returnValueFor(Collection.class));
        assertEquals(new HashSet<>(), returnsEmptyValues.returnValueFor(Set.class));
        assertEquals(new ArrayList<>(), returnsEmptyValues.returnValueFor(List.class));
        assertEquals(new HashMap<>(), returnsEmptyValues.returnValueFor(Map.class));
        assertEquals(new TreeSet<>(), returnsEmptyValues.returnValueFor(SortedSet.class));
        assertEquals(new TreeMap<>(), returnsEmptyValues.returnValueFor(SortedMap.class));
        assertEquals(new LinkedHashSet<>(), returnsEmptyValues.returnValueFor(LinkedHashSet.class));
        assertEquals(new LinkedList<>(), returnsEmptyValues.returnValueFor(LinkedList.class));

        // Testing non-empty collections
        Collection<String> nonEmptyCollection = new ArrayList<>();
        nonEmptyCollection.add("foo");

        InvocationOnMock invocation = mock(Answer.class).answer(ArgumentMatchers.any());
        when(invocation.answer(ArgumentMatchers.eq(Collection.class))).thenReturn(nonEmptyCollection);

        assertEquals(nonEmptyCollection, returnsEmptyValues.answer(invocation));
    }

    @Test
    public void test_toString() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        String mockName = "mockName";

        MockName mockNameMock = mock(MockName.class);
        when(mockNameMock.toString()).thenReturn(mockName);

        Object mock = mock(Object.class, new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) {
                return mockNameMock;
            }
        });

        assertEquals("Mock for " + MockitoTestUtils.getTypeName(mock) + ", hashCode: " + mock.hashCode(), returnsEmptyValues.answer(ArgumentMatchers.method(Object.class, "toString")).toString());
    }

    @Test
    public void test_compareTo() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Object obj1 = new Object();
        Object obj2 = obj1;

        assertEquals(0, returnsEmptyValues.answer(ArgumentMatchers.method(Comparable.class, "compareTo", Object.class)).compareTo(obj1));
        assertEquals(0, returnsEmptyValues.answer(ArgumentMatchers.method(Comparable.class, "compareTo", Object.class)).compareTo(obj2));
        assertEquals(1, returnsEmptyValues.answer(ArgumentMatchers.method(Comparable.class, "compareTo", Object.class)).compareTo(new Object()));
    }
}

class MockitoTestUtils {
    public static String getTypeName(Object object) {
        return object.getClass().getSimpleName();
    }
}