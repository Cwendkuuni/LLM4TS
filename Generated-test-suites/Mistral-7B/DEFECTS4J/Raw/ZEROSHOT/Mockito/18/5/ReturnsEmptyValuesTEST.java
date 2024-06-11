package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswerToStringMethod() {
        Mockito<String> mockString = Mockito.mock(String.class);
        Answer answer = new ReturnsEmptyValues();

        String mockName = Mockito.mockUtil.getMockName(mockString);
        String expectedToString = "Mock for " + mockString.getClass().getSimpleName() + ", hashCode: " + mockString.hashCode();

        Mockito.doAnswer(answer).when(mockString).toString();

        assertEquals(expectedToString, mockString.toString());

        String actualToString = answer.answer(Mockito.argument(0, ArgumentMatchers.instanceOf(InvocationOnMock.class)));
        assertNotNull(actualToString);
        assertEquals(expectedToString, actualToString);
    }

    @Test
    public void testAnswerCompareToMethod() {
        Object mock1 = Mockito.mock(Comparable.class);
        Object mock2 = Mockito.mock(Comparable.class);

        Answer answer = new ReturnsEmptyValues();

        Mockito.doAnswer(answer).when(mock1).toString();
        Mockito.doAnswer(answer).when(mock2).toString();

        int comparisonResult = answer.answer(new InvocationOnMock(mock1, Mockito.method(Mockito.type(Comparable.class), "compareTo", Mockito.type(Object.class))))
                .compareTo(mock2);

        assertEquals(1, comparisonResult);
    }

    @Test
    public void testAnswerEmptyCollection() {
        Collection<Object> mockCollection = Mockito.mock(Collection.class);
        Answer answer = new ReturnsEmptyValues();

        Mockito.doAnswer(answer).when(mockCollection).toString();

        Collection<Object> actualCollection = answer.answer(new InvocationOnMock(mockCollection, Mockito.method(Mockito.type(Collection.class), "size", Mockito.any())));

        assertEquals(0, actualCollection.size());
        assertEquals(0, actualCollection.isEmpty());
    }

    @Test
    public void testAnswerEmptySet() {
        Set<Object> mockSet = Mockito.mock(Set.class);
        Answer answer = new ReturnsEmptyValues();

        Mockito.doAnswer(answer).when(mockSet).toString();

        Set<Object> actualSet = answer.answer(new InvocationOnMock(mockSet, Mockito.method(Mockito.type(Set.class), "size", Mockito.any())));

        assertEquals(0, actualSet.size());
        assertEquals(true, actualSet.isEmpty());
    }

    @Test
    public void testAnswerEmptyHashSet() {
        Set<Object> mockHashSet = Mockito.mock(HashSet.class);
        Answer answer = new ReturnsEmptyValues();

        Mockito.doAnswer(answer).when(mockHashSet).toString();

        Set<Object> actualHashSet = answer.answer(new InvocationOnMock(mockHashSet, Mockito.method(Mockito.type(HashSet.class), "size", Mockito.any())));

        assertEquals(0, actualHashSet.size());
        assertEquals(true, actualHashSet.isEmpty());
    }

    @Test
    public void testAnswerEmptyTreeSet() {
        SortedSet<Object> mockTreeSet = Mockito.mock(TreeSet.class);
        Answer answer = new ReturnsEmptyValues();

        Mockito.doAnswer(answer).when(mockTreeSet).toString();

        SortedSet<Object> actualTreeSet = answer.answer(new InvocationOnMock(mockTreeSet, Mockito.method(Mockito.type(SortedSet.class), "size", Mockito.any())));

        assertEquals(0, actualTreeSet.size());
        assertEquals(true, actualTreeSet.isEmpty());
    }

    @Test
    public void testAnswerEmptyLinkedHashSet() {
        Set<Object> mockLinkedHashSet = Mockito.mock(LinkedHashSet.class);
        Answer answer = new ReturnsEmptyValues();

        Mockito.doAnswer(answer).when(mockLinkedHashSet).toString();

        Set<Object> actualLinkedHashSet = answer.answer(new InvocationOnMock(mockLinkedHashSet, Mockito.method(Mockito.type(LinkedHashSet.class), "size", Mockito.any())));

        assertEquals(0, actualLinkedHashSet.size());
        assertEquals(true, actualLinkedHashSet.isEmpty());
    }

    @Test
    public void testAnswerEmptyList() {
        List<Object> mockList = Mockito.mock(List.class);
        Answer answer = new ReturnsEmptyValues();

        Mockito.doAnswer(answer).when(mockList).toString();

        List<Object> actualList = answer.answer(new InvocationOnMock(mockList, Mockito.method(Mockito.type(List.class), "size", Mockito.any())));

        assertEquals(0, actualList.size());
        assertEquals(true, actualList.isEmpty());
    }

    @Test
    public void testAnswerEmptyLinkedList() {
        List<Object> mockLinkedList = Mockito.mock(LinkedList.class);
        Answer answer = new ReturnsEmptyValues();

        Mockito.doAnswer(answer).when(mockLinkedList).toString();

        List<Object> actualLinkedList = answer.answer(new InvocationOnMock(mockLinkedList, Mockito.method(Mockito.type(LinkedList.class), "size", Mockito.any())));

        assertEquals(0, actualLinkedList.size());
        assertEquals(true, actualLinkedList.isEmpty());
    }

    @Test
    public void testAnswerEmptyArrayList() {
        List<Object> mockArrayList = Mockito.mock(ArrayList.class);
        Answer answer = new ReturnsEmptyValues();

        Mockito.doAnswer(answer).when(mockArrayList).toString();

        List<Object> actualArrayList = answer.answer(new InvocationOnMock(mockArrayList, Mockito.method(Mockito.type(ArrayList.class), "size", Mockito.any())));

        assertEquals(0, actualArrayList.size());
        assertEquals(true, actualArrayList.isEmpty());
    }

    @Test
    public void testAnswerEmptyMap() {
        Map<Object, Object> mockMap = Mockito.mock(Map.class);
        Answer answer = new ReturnsEmptyValues();

        Mockito.doAnswer(answer).when(mockMap).toString();

        Map<Object, Object> actualMap = answer.answer(new InvocationOnMock(mockMap, Mockito.method(Mockito.type(Map.class), "size", Mockito.any())));

        assertEquals(0, actualMap.size());
        assertEquals(true, actualMap.isEmpty());
    }

    @Test
    public void testAnswerEmptyHashMap() {
        Map<Object, Object> mockHashMap = Mockito.mock(HashMap.class);
        Answer answer = new ReturnsEmptyValues();

        Mockito.doAnswer(answer).when(mockHashMap).toString();

        Map<Object, Object> actualHashMap = answer.answer(new InvocationOnMock(mockHashMap, Mockito.method(Mockito.type(HashMap.class), "size", Mockito.any())));

        assertEquals(0, actualHashMap.size());
        assertEquals(true, actualHashMap.isEmpty());
    }

    @Test
    public void testAnswerEmptyTreeMap() {
        SortedMap<Object, Object> mockTreeMap = Mockito.mock(TreeMap.class);
        Answer answer = new ReturnsEmptyValues();

        Mockito.doAnswer(answer).when(mockTreeMap).toString();

        SortedMap<Object, Object> actualTreeMap = answer.answer(new InvocationOnMock(mockTreeMap, Mockito.method(Mockito.type(TreeMap.class), "size", Mockito.any())));

        assertEquals(0, actualTreeMap.size());
        assertEquals(true, actualTreeMap.isEmpty());
    }

    @Test
    public void testAnswerEmptyLinkedHashMap() {
        Map<Object, Object> mockLinkedHashMap = Mockito.mock(LinkedHashMap.class);
        Answer answer = new ReturnsEmptyValues();

        Mockito.doAnswer(answer).when(mockLinkedHashMap).toString();

        Map<Object, Object> actualLinkedHashMap = answer.answer(new InvocationOnMock(mockLinkedHashMap, Mockito.method(Mockito.type(LinkedHashMap.class), "size", Mockito.any())));

        assertEquals(0, actualLinkedHashMap.size());
        assertEquals(true, actualLinkedHashMap.isEmpty());
    }
}