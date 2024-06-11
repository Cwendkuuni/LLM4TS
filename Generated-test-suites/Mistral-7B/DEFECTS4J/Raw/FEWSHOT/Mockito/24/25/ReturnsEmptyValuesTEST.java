package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.internal.util.Primitives.isPrimitiveOrWrapper;
import static org.mockito.internal.util.Primitives.defaultValueForPrimitiveOrWrapper;

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
    public void testAnswerForPrimitives() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        byte b = returnsEmptyValues.returnValueFor((byte.class));
        short s = returnsEmptyValues.returnValueFor((short.class));
        int i = returnsEmptyValues.returnValueFor((int.class));
        long l = returnsEmptyValues.returnValueFor((long.class));
        float f = returnsEmptyValues.returnValueFor((float.class));
        double d = returnsEmptyValues.returnValueFor((double.class));
        boolean bool = returnsEmptyValues.returnValueFor((boolean.class));

        assertEquals(0, (byte) b);
        assertEquals(0, s);
        assertEquals(0, i);
        assertEquals(0L, l);
        assertEquals(0.0f, f, 0.001f);
        assertEquals(0.0, d, 0.001);
        assertEquals(false, bool);
    }

    @Test
    public void testAnswerForCollectionTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Collection<String> collection = returnsEmptyValues.returnValueFor(Collection.class);
        List<String> list = returnsEmptyValues.returnValueFor(List.class);
        Set<String> set = returnsEmptyValues.returnValueFor(Set.class);
        HashSet<String> hashSet = returnsEmptyValues.returnValueFor(HashSet.class);
        LinkedHashSet<String> linkedHashSet = returnsEmptyValues.returnValueFor(LinkedHashSet.class);
        SortedSet<String> sortedSet = returnsEmptyValues.returnValueFor(SortedSet.class);
        TreeSet<String> treeSet = returnsEmptyValues.returnValueFor(TreeSet.class);

        assertEquals(0, collection.size());
        assertEquals(0, list.size());
        assertEquals(0, set.size());
        assertEquals(0, hashSet.size());
        assertEquals(0, linkedHashSet.size());
        assertEquals(0, sortedSet.size());
        assertEquals(0, treeSet.size());
    }

    @Test
    public void testAnswerForOtherTypes() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Answer<Object> answer = returnsEmptyValues;

        InvocationOnMock invocation = mockitoTestUtils.mock(Object.class);
        Object result = answer.answer(invocation);

        assertNull(result);
    }

    @Test
    public void testToStringMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Object mock = mockitoTestUtils.mock(Object.class);
        String result = returnsEmptyValues.answer(mockitoTestUtils.mockingDetails(mock).getInvocation("toString()"));

        String mockName = "Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode();
        assertEquals(mockName, result);
    }

    @Test
    public void testCompareToMethod() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        Object obj1 = mockitoTestUtils.mock(Comparable.class);
        Object obj2 = mockitoTestUtils.mock(Comparable.class);

        int result = returnsEmptyValues.answer(mockitoTestUtils.mockingDetails(obj1).getInvocation("compareTo(Ljava/lang/Object;)I", obj2));

        assertEquals(1, result);
    }
}