package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.internal.util.Primitives.isPrimitiveOrWrapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.mock.MockName;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer_toStringMethod() {
        MockName mockName = MockName.createMockName("MockName", ReturnsEmptyValues.class);
        Object mock = new Object();

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        assertEquals("Mock for Object, hashCode: " + mock.hashCode(), returnsEmptyValues.answer(mockOf(String.class, "toString", mock)));
        assertEquals(mockName.toString(), returnsEmptyValues.answer(mockOf(String.class, "toString", mockName)));
    }

    @Test
    public void testAnswer_compareToMethod() {
        Object obj1 = new Object();
        Object obj2 = new Object();

        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        assertEquals(1, (int) returnsEmptyValues.answer(mockOf(Comparable.class, "compareTo", obj1, obj2)).intValue());
    }

    @Test
    public void testReturnValueFor_primitiveTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        byte byteValue = (byte) returnsEmptyValues.returnValueFor(Byte.TYPE).byteValue();
        short shortValue = (short) returnsEmptyValues.returnValueFor(Short.TYPE).shortValue();
        int intValue = (int) returnsEmptyValues.returnValueFor(Integer.TYPE).intValue();
        long longValue = (long) returnsEmptyValues.returnValueFor(Long.TYPE).longValue();
        char charValue = (char) returnsEmptyValues.returnValueFor(Character.TYPE).charValue();
        boolean booleanValue = returnsEmptyValues.returnValueFor(Boolean.TYPE).booleanValue();
        float floatValue = (float) returnsEmptyValues.returnValueFor(Float.TYPE).floatValue();
        double doubleValue = (double) returnsEmptyValues.returnValueFor(Double.TYPE).doubleValue();

        assertEquals(0, byteValue);
        assertEquals(0, shortValue);
        assertEquals(0, intValue);
        assertEquals(0L, longValue);
        assertEquals('\u0000', charValue);
        assertEquals(false, booleanValue);
        assertEquals(0f, floatValue, 0.001f);
        assertEquals(0d, doubleValue, 0.001d);
    }

    @Test
    public void testReturnValueFor_Collection() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Collection<Object> collection = returnsEmptyValues.returnValueFor(Collection.class);

        assertNull(collection);

        List<Object> list = (List<Object>) returnsEmptyValues.returnValueFor(List.class);

        assertEquals(0, list.size());
        assertNull(list.get(0));
    }

    @Test
    public void testReturnValueFor_Set() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Set<Object> set = returnsEmptyValues.returnValueFor(Set.class);

        assertNull(set);

        SortedSet<Object> sortedSet = (SortedSet<Object>) returnsEmptyValues.returnValueFor(TreeSet.class);

        assertEquals(0, sortedSet.size());
    }

    @Test
    public void testReturnValueFor_LinkedHashSet() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Set<Object> linkedHashSet = returnsEmptyValues.returnValueFor(LinkedHashSet.class);

        assertNull(linkedHashSet);

        SortedSet<Object> linkedTreeSet = (SortedSet<Object>) returnsEmptyValues.returnValueFor(LinkedTreeSet.class); // This is not a valid class, but this test case should not fail

        assertEquals(0, linkedTreeSet.size());
    }

    @Test
    public void testReturnValueFor_Map() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Map<Object, Object> map = returnsEmptyValues.returnValueFor(Map.class);

        assertNull(map);

        SortedMap<Object, Object> sortedMap = (SortedMap<Object, Object>) returnsEmptyValues.returnValueFor(TreeMap.class);

        assertEquals(0, sortedMap.size());
    }

    @Test
    public void testReturnValueFor_LinkedHashMap() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Map<Object, Object> linkedHashMap = returnsEmptyValues.returnValueFor(LinkedHashMap.class);

        assertNull(linkedHashMap);

        SortedMap<Object, Object> linkedTreeMap = (SortedMap<Object, Object>) returnsEmptyValues.returnValueFor(LinkedTreeMap.class); // This is not a valid class, but this test case should not fail

        assertEquals(0, linkedTreeMap.size());
    }

    private InvocationOnMock mockOf(Class<?> returnType, String methodName, Object mock) {
        ObjectMethodsGuru methodsGuru = new ObjectMethodsGuru();
        InvocationOnMock invocation = mockery().on(mock).callRealMethod(methodName);
        return new InvocationOnMock(invocation, returnType, methodsGuru.getInvokedMethod(invocation));
    }

    private Mockery mockery() {
        return mockitoMockito().mockery();
    }
}