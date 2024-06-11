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
import org.mockito.invocation.InvocationOnMock;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerPrimitiveTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = MockitoTestUtils.createInvocationOnMock("testMethod", new Integer(0));
        assertEquals(0, returnsEmptyValues.answer(invocation).intValue());
        assertEquals(0, returnsEmptyValues.answer(invocation).byteValue());
        assertEquals(0, returnsEmptyValues.answer(invocation).shortValue());
        assertEquals(0, returnsEmptyValues.answer(invocation).charValue());
        assertEquals(0L, returnsEmptyValues.answer(invocation).longValue());
        assertEquals(0, returnsEmptyValues.answer(invocation).floatValue());
        assertEquals(0.0, returnsEmptyValues.answer(invocation).doubleValue());
        assertEquals(false, returnsEmptyValues.answer(invocation).booleanValue());
    }

    @Test
    public void testAnswerCollectionTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = MockitoTestUtils.createInvocationOnMock("testMethod", new Class[0]);
        Collection<Object> collection = (Collection<Object>) returnsEmptyValues.answer(invocation);
        assertNotNull(collection);
        assertEquals(0, collection.size());
    }

    @Test
    public void testAnswerSetTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = MockitoTestUtils.createInvocationOnMock("testMethod", new Class[0]);
        Set<Object> set = (Set<Object>) returnsEmptyValues.answer(invocation);
        assertNotNull(set);
        assertEquals(0, set.size());
    }

    @Test
    public void testAnswerMapTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = MockitoTestUtils.createInvocationOnMock("testMethod", new Class[0]);
        Map<Object, Object> map = (Map<Object, Object>) returnsEmptyValues.answer(invocation);
        assertNotNull(map);
        assertEquals(0, map.size());
    }

    @Test
    public void testToString() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = MockitoTestUtils.createInvocationOnMock("testToString", new Object());
        assertEquals("Mock for <class java.lang.Object> hashCode: " + System.identityHashCode(invocation.getMock()) + "\n", returnsEmptyValues.answer(invocation).toString());
    }

    @Test
    public void testCompareTo() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object mockObject1 = new Object();
        Object mockObject2 = new Object();
        InvocationOnMock invocation1 = MockitoTestUtils.createInvocationOnMock("compareTo", mockObject1, new Object());
        InvocationOnMock invocation2 = MockitoTestUtils.createInvocationOnMock("compareTo", mockObject2, new Object());
        assertEquals(0, returnsEmptyValues.answer(invocation1).intValue());
        assertEquals(1, returnsEmptyValues.answer(invocation2).intValue());
    }

    @Test
    public void testOtherReturnTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        InvocationOnMock invocation = MockitoTestUtils.createInvocationOnMock("testOtherReturnTypes", new Object());
        assertNull(returnsEmptyValues.answer(invocation));
    }

    @Test
    public void testReturnValueForPrimitiveWrapperTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Class<?> intClass = int.class;
        Class<?> integerClass = Integer.class;
        assertEquals(0, returnsEmptyValues.returnValueFor(intClass).intValue());
        assertEquals(0, returnsEmptyValues.returnValueFor(integerClass).intValue());
    }

    @Test
    public void testReturnValueForCollectionTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Class<?> collectionClass = Collection.class;
        List<Object> list = (List<Object>) returnsEmptyValues.returnValueFor(collectionClass);
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    public void testReturnValueForSetTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Class<?> setClass = Set.class;
        Set<Object> set = (Set<Object>) returnsEmptyValues.returnValueFor(setClass);
        assertNotNull(set);
        assertEquals(0, set.size());
    }

    @Test
    public void testReturnValueForMapTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Class<?> mapClass = Map.class;
        Map<Object, Object> map = (Map<Object, Object>) returnsEmptyValues.returnValueFor(mapClass);
        assertNotNull(map);
        assertEquals(0, map.size());
    }

}

class MockitoTestUtils {
    public static InvocationOnMock createInvocationOnMock(String methodName, Object... arguments) {
        Class<?> mockType = Object.class;
        try {
            mockType = Class.forName("org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues");
        } catch (Exception e) {
            // Mockito is not in the classpath, no need to fail test
        }
        return Mockito.mock(mockType).invokeMethod(methodName, arguments);
    }
}