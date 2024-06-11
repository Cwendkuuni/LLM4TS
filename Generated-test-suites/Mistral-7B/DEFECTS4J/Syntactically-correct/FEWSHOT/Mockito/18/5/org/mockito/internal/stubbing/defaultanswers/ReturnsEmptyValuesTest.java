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
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.junit.Test;
import org.mockito.internal.stubbing.Answer;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.internal.util.Primitives;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerPrimitiveTypes() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
        ObjectMethodsGuru methodsGuru = new ObjectMethodsGuru();

        InvocationOnMock invocation = mock(Answer.class);
        mock(ObjectMethodsGuru.class);

        Object answer = emptyValues.answer(invocation);

        Class<?> returnType = invocation.getMethod().getReturnType();

        if (isPrimitiveOrWrapper(returnType)) {
            Object expected = defaultValueForPrimitiveOrWrapper(returnType);
            assertEquals(expected, answer);
        }
    }

    @Test
    public void testAnswerCollections() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();

        InvocationOnMock invocation = mock(Answer.class);

        Collection<?> collectionType = Collection.class;
        Set<?> setType = Set.class;
        HashSet<?> hashSetType = HashSet.class;
        LinkedHashSet<?> linkedHashSetType = LinkedHashSet.class;
        List<?> listType = List.class;
        LinkedList<?> linkedListType = LinkedList.class;
        ArrayList<?> arrayListType = ArrayList.class;
        Map<?> mapType = Map.class;
        HashMap<?> hashMapType = HashMap.class;
        SortedMap<?> sortedMapType = SortedMap.class;
        TreeMap<?> treeMapType = TreeMap.class;
        LinkedHashMap<?> linkedHashMapType = LinkedHashMap.class;

        Object answer = emptyValues.answer(invocation);

        if (collectionType.isAssignableFrom(invocation.getMethod().getReturnType())) {
            if (Answer.class.isAssignableFrom(LinkedList.class)) {
                assertEquals(new LinkedList<Object>(), answer);
            } else {
                assertNull(answer);
            }
        } else if (setType.isAssignableFrom(invocation.getMethod().getReturnType())) {
            if (Answer.class.isAssignableFrom(HashSet.class)) {
                assertEquals(new HashSet<Object>(), answer);
            } else {
                assertNull(answer);
            }
        } else if (hashSetType.isAssignableFrom(invocation.getMethod().getReturnType())) {
            assertEquals(new HashSet<Object>(), answer);
        } else if (linkedHashSetType.isAssignableFrom(invocation.getMethod().getReturnType())) {
            assertEquals(new LinkedHashSet<Object>(), answer);
        } else if (listType.isAssignableFrom(invocation.getMethod().getReturnType())) {
            if (Answer.class.isAssignableFrom(LinkedList.class)) {
                assertEquals(new LinkedList<Object>(), answer);
            } else {
                assertNull(answer);
            }
        } else if (linkedListType.isAssignableFrom(invocation.getMethod().getReturnType())) {
            assertEquals(new LinkedList<Object>(), answer);
        } else if (arrayListType.isAssignableFrom(invocation.getMethod().getReturnType())) {
            assertEquals(new ArrayList<Object>(), answer);
        } else if (mapType.isAssignableFrom(invocation.getMethod().getReturnType())) {
            if (Answer.class.isAssignableFrom(HashMap.class)) {
                assertEquals(new HashMap<Object, Object>(), answer);
            } else {
                assertNull(answer);
            }
        } else if (hashMapType.isAssignableFrom(invocation.getMethod().getReturnType())) {
            assertEquals(new HashMap<Object, Object>(), answer);
        } else if (sortedMapType.isAssignableFrom(invocation.getMethod().getReturnType())) {
            if (Answer.class.isAssignableFrom(TreeMap.class)) {
                assertEquals(new TreeMap<Object, Object>(), answer);
            } else {
                assertNull(answer);
            }
        } else if (treeMapType.isAssignableFrom(invocation.getMethod().getReturnType())) {
            assertEquals(new TreeMap<Object, Object>(), answer);
        } else if (linkedHashMapType.isAssignableFrom(invocation.getMethod().getReturnType())) {
            assertEquals(new LinkedHashMap<Object, Object>(), answer);
        }
    }

    @Test
    public void testToStringMethod() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();

        InvocationOnMock invocation = mock(Answer.class);
        Object mock = mock(Object.class);
        MockName name = mock(MockName.class);

        mockStatic(MockUtil.class);
        when(MockUtil.getMockName(mock)).thenReturn(name);

        String expected = "Mock for " + MockUtil.class.getSimpleName() + ", hashCode: " + mock.hashCode();

        String answer = (String) emptyValues.answer(invocation);

        if (methodsGuru.isToString(invocation.getMethod())) {
            assertEquals(expected, answer);
        }
    }

    @Test
    public void testCompareToMethod() {
        ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();

        Object mock1 = mock(Comparable.class);
        Object mock2 = mock(Comparable.class);

        InvocationOnMock invocation = mock(Answer.class);
        when(invocation.getMock()).thenReturn(mock1);
        when(invocation.getArguments()[0]).thenReturn(mock2);

        int answer = (Integer) emptyValues.answer(invocation);

        if (methodsGuru.isCompareToMethod(invocation.getMethod())) {
            assertEquals(mock1 == mock2 ? 0 : 1, answer);
        }
    }
}