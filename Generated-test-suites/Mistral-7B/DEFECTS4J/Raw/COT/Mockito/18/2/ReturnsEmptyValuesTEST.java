package org.mockito.internal.stubbing.defaultanswers.test;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringMethod() {
        Object mock = mock(String.class);
        ReturnsEmptyValues answer = Mockito.answer(new ReturnsEmptyValues());

        InvocationOnMock invocation = Mockito.mockMethod(String.class, "toString");
        Object result = answer.answer(invocation);

        Assert.assertEquals("Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode(), result.toString());
    }

    @Test
    public void testCompareToMethod() {
        Object mockObject1 = mock(Comparable.class);
        Object mockObject2 = mock(Comparable.class);

        ReturnsEmptyValues answer = Mockito.answer(new ReturnsEmptyValues());

        InvocationOnMock invocation1 = Mockito.mockMethod(Comparable.class, "compareTo", mockObject1);
        InvocationOnMock invocation2 = Mockito.mockMethod(Comparable.class, "compareTo", mockObject2);

        answer.answer(invocation1);
        answer.answer(invocation2);

        verify(mockObject1).equals(mockObject2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testModifyCollection() {
        Object mock = mock(Collection.class);
        ReturnsEmptyValues answer = Mockito.answer(new ReturnsEmptyValues());

        List<Object> list = answer.answer(Mockito.mockMethod(List.class, "list"));
        list.add("test");
    }

    @Test
    public void testCollectionTypes() {
        Object mock = mock(Collection.class);

        for (Class<? extends Collection<?>> collectionType : new Class<?>[]{Collection.class, Set.class, HashSet.class, SortedSet.class, TreeSet.class, LinkedHashSet.class, List.class, LinkedList.class, ArrayList.class, Map.class, HashMap.class, SortedMap.class, TreeMap.class, LinkedHashMap.class}) {
            Answer answer = Mockito.answer(new ReturnsEmptyValues());
            Collection<?> returnedCollection = (Collection<?>) answer.answer(Mockito.mockMethod(collectionType, "newInstance"));

            Assert.assertNotNull(returnedCollection);
            Assert.assertTrue(returnedCollection.isEmpty());
        }
    }

    @Test
    public void testOtherTypes() {
        Object mock = mock(Object.class);

        ReturnsEmptyValues answer = Mockito.answer(new ReturnsEmptyValues());
        Assert.assertNull(answer.answer(Mockito.mockMethod(Object.class, "hashCode")));
        Assert.assertNull(answer.answer(Mockito.mockMethod(Object.class, "toString")));
        Assert.assertNull(answer.answer(Mockito.mockMethod(Object.class, "getClass")));
    }
}