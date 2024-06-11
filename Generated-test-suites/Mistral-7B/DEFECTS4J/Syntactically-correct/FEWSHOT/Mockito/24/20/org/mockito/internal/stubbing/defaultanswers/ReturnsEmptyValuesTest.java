package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertInstanceOf;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;

/**
 * Test class for the ReturnsEmptyValues class.
 */
public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerForPrimitiveTypes() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        ObjectMethodsGuru methodsGuru = new ObjectMethodsGuru();
        MockUtil mockUtil = new MockUtil();

        InvocationOnMock invocation = MockUtil.createMockInvocation(int[].class, new Object() {}, null);

        assertEquals(0, emptyValues.answer(invocation).hashCode());
        assertEquals(0, ((int[]) emptyValues.answer(invocation))[0]);
    }

    @Test
    public void testAnswerForCollectionTypes() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();

        for (Class<?> collectionType : new Class<?>[] {Collection.class, Set.class, HashSet.class, SortedSet.class, TreeSet.class, LinkedHashSet.class, List.class, LinkedList.class, ArrayList.class, Map.class, HashMap.class, SortedMap.class, TreeMap.class, LinkedHashMap.class}) {
            InvocationOnMock invocation = MockUtil.createMockInvocation(collectionType, new Object() {}, null);
            Collection<?> collection = (Collection<?>) emptyValues.answer(invocation);

            assertTrue(collection.isEmpty());
        }
    }

    @Test
    public void testAnswerForOtherTypes() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();

        InvocationOnMock invocation = MockUtil.createMockInvocation(String[].class, new Object() {}, null);
        assertNull(emptyValues.answer(invocation));

        invocation = MockUtil.createMockInvocation(String.class, new Object() {}, null);
        assertNull(emptyValues.answer(invocation));

        invocation = MockUtil.createMockInvocation(Object.class, new Object() {}, null);
        assertNull(emptyValues.answer(invocation));
    }

    @Test
    public void testToStringMethod() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        MockUtil mockUtil = new MockUtil();

        Object mock = new Object();
        MockName name = new MockName("mockName");
        InvocationOnMock invocation = MockUtil.createMockInvocation(String.class, mock, null);

        String description = emptyValues.answer(invocation).toString();
        assertTrue(description.contains("Mock for"));
        assertTrue(description.contains(mockUtil.getMockSettings(mock).getTypeToMock().getSimpleName()));
    }

    @Test
    public void testCompareToMethod() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();

        InvocationOnMock invocation = MockUtil.createMockInvocation(Number.class, new Object() {}, null);
        assertEquals(1, emptyValues.answer(invocation).getClass().getMethod("compareTo", Object.class).invoke(emptyValues.answer(invocation), new Object() {}));
    }
}