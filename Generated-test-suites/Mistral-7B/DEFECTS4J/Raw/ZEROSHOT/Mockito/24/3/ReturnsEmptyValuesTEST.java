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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.invoke;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringMethod() {
        MockitoAnswer answer = mock(Answer.class);
        Mockito.doAnswer(answer).when(answer).answer(ArgumentMatchers.any(InvocationOnMock.class));

        Object mock = mock(Object.class);
        Answer answerForMock = ReturnsEmptyValues.getInstance();

        Mockito.doAnswer(answerForMock).when(answer).answer(ArgumentMatchers.any(InvocationOnMock.class));
        Mockito.when(answerForMock.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenCallRealMethod();

        String result = (String) invoke(answer, new InvocationOnMock(mock, null, null));

        assertThat(result).isInstanceOf(String.class);
        assertThat(result).contains("Mock for ");
    }

    @Test
    public void testCompareToMethod() {
        MockitoAnswer answer = mock(Answer.class);
        Mockito.doAnswer(answer).when(answer).answer(ArgumentMatchers.any(InvocationOnMock.class));

        Comparable mock = mock(Comparable.class);
        Answer answerForMock = ReturnsEmptyValues.getInstance();

        Mockito.doAnswer(answerForMock).when(answer).answer(ArgumentMatchers.any(InvocationOnMock.class));
        Mockito.when(answerForMock.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenCallRealMethod();

        int result = (Integer) invoke(answer, new InvocationOnMock(mock, "compareTo", new Object[]{Mockito.mock(Comparable.class)}));

        assertThat(result).isGreaterThan(0);
    }

    @Test
    public void testEmptyCollection() {
        MockitoAnswer answer = mock(Answer.class);
        Mockito.doAnswer(answer).when(answer).answer(ArgumentMatchers.any(InvocationOnMock.class));

        Collection<Object> mockCollection = mock(Collection.class);
        Answer answerForMock = ReturnsEmptyValues.getInstance();

        Mockito.doAnswer(answerForMock).when(answer).answer(ArgumentMatchers.any(InvocationOnMock.class));
        Mockito.when(answerForMock.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenCallRealMethod();

        Collection<Object> result = (Collection<Object>) invoke(answer, new InvocationOnMock(mockCollection, "size", new Object[0]));

        assertThat(result).isInstanceOf(Collection.class);
        assertThat(result).isEmpty();
    }

    @Test
    public void testEmptySet() {
        MockitoAnswer answer = mock(Answer.class);
        Mockito.doAnswer(answer).when(answer).answer(ArgumentMatchers.any(InvocationOnMock.class));

        Set<Object> mockSet = mock(Set.class);
        Answer answerForMock = ReturnsEmptyValues.getInstance();

        Mockito.doAnswer(answerForMock).when(answer).answer(ArgumentMatchers.any(InvocationOnMock.class));
        Mockito.when(answerForMock.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenCallRealMethod();

        Set<Object> result = (Set<Object>) invoke(answer, new InvocationOnMock(mockSet, "size", new Object[0]));

        assertThat(result).isInstanceOf(Set.class);
        assertThat(result).isEmpty();
    }

    @Test
    public void testEmptyHashSet() {
        MockitoAnswer answer = mock(Answer.class);
        Mockito.doAnswer(answer).when(answer).answer(ArgumentMatchers.any(InvocationOnMock.class));

        Set<Object> mockHashSet = mock(HashSet.class);
        Answer answerForMock = ReturnsEmptyValues.getInstance();

        Mockito.doAnswer(answerForMock).when(answer).answer(ArgumentMatchers.any(InvocationOnMock.class));
        Mockito.when(answerForMock.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenCallRealMethod();

        Set<Object> result = (Set<Object>) invoke(answer, new InvocationOnMock(mockHashSet, "size", new Object[0]));

        assertThat(result).isInstanceOf(Set.class);
        assertThat(result).isInstanceOf(HashSet.class);
        assertThat(result).isEmpty();
    }

    @Test
    public void testEmptyTreeSet() {
        MockitoAnswer answer = mock(Answer.class);
        Mockito.doAnswer(answer).when(answer).answer(ArgumentMatchers.any(InvocationOnMock.class));

        SortedSet<Object> mockSortedSet = mock(SortedSet.class);
        Answer answerForMock = ReturnsEmptyValues.getInstance();

        Mockito.doAnswer(answerForMock).when(answer).answer(ArgumentMatchers.any(InvocationOnMock.class));
        Mockito.when(answerForMock.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenCallRealMethod();

        SortedSet<Object> result = (SortedSet<Object>) invoke(answer, new InvocationOnMock(mockSortedSet, "size", new Object[0]));

        assertThat(result).isInstanceOf(SortedSet.class);
        assertThat(result).isInstanceOf(TreeSet.class);
        assertThat(result).isEmpty();
    }

    @Test
    public void testEmptyLinkedHashSet() {
        MockitoAnswer answer = mock(Answer.class);
        Mockito.doAnswer(answer).when(answer).answer(ArgumentMatchers.any(InvocationOnMock.class));

        Set<Object> mockLinkedHashSet = mock(LinkedHashSet.class);
        Answer answerForMock = ReturnsEmptyValues.getInstance();

        Mockito.doAnswer(answerForMock).when(answer).answer(ArgumentMatchers.any(InvocationOnMock.class));
        Mockito.when(answerForMock.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenCallRealMethod();

        Set<Object> result = (Set<Object>) invoke(answer, new InvocationOnMock(mockLinkedHashSet, "size", new Object[0]));

        assertThat(result).isInstanceOf(Set.class);
        assertThat(result).isInstanceOf(LinkedHashSet.class);
        assertThat(result).isEmpty();
    }

    @Test
    public void testEmptyList() {
        MockitoAnswer answer = mock(Answer.class);
        Mockito.doAnswer(answer).when(answer).answer(ArgumentMatchers.any(InvocationOnMock.class));

        List<Object> mockList = mock(List.class);
        Answer answerForMock = ReturnsEmptyValues.getInstance();

        Mockito.doAnswer(answerForMock).when(answer).answer(ArgumentMatchers.any(InvocationOnMock.class));
        Mockito.when(answerForMock.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenCallRealMethod();

        List<Object> result = (List<Object>) invoke(answer, new InvocationOnMock(mockList, "size", new Object[0]));

        assertThat(result).isInstanceOf(List.class);
        assertThat(result).isEmpty();
    }

    @Test
    public void testEmptyLinkedList() {
        MockitoAnswer answer = mock(Answer.class);
        Mockito.doAnswer(answer).when(answer).answer(ArgumentMatchers.any(InvocationOnMock.class));

        List<Object> mockLinkedList = mock(LinkedList.class);
        Answer answerForMock = ReturnsEmptyValues.getInstance();

        Mockito.doAnswer(answerForMock).when(answer).answer(ArgumentMatchers.any(InvocationOnMock.class));
        Mockito.when(answerForMock.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenCallRealMethod();

        List<Object> result = (List<Object>) invoke(answer, new InvocationOnMock(mockLinkedList, "size", new Object[0]));

        assertThat(result).isInstanceOf(List.class);
        assertThat(result).isInstanceOf(LinkedList.class);
        assertThat(result).isEmpty();
    }

    @Test
    public void testEmptyArrayList() {
        MockitoAnswer answer = mock(Answer.class);
        Mockito.doAnswer(answer).when(answer).answer(ArgumentMatchers.any(InvocationOnMock.class));

        List<Object> mockArrayList = mock(ArrayList.class);
        Answer answerForMock = ReturnsEmptyValues.getInstance();

        Mockito.doAnswer(answerForMock).when(answer).answer(ArgumentMatchers.any(InvocationOnMock.class));
        Mockito.when(answerForMock.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenCallRealMethod();

        List<Object> result = (List<Object>) invoke(answer, new InvocationOnMock(mockArrayList, "size", new Object[0]));

        assertThat(result).isInstanceOf(List.class);
        assertThat(result).isInstanceOf(ArrayList.class);
        assertThat(result).isEmpty();
    }

    @Test
    public void testEmptyMap() {
        MockitoAnswer answer = mock(Answer.class);
        Mockito.doAnswer(answer).when(answer).answer(ArgumentMatchers.any(InvocationOnMock.class));

        Map<Object, Object> mockMap = mock(Map.class);
        Answer answerForMock = ReturnsEmptyValues.getInstance();

        Mockito.doAnswer(answerForMock).when(answer).answer(ArgumentMatchers.any(InvocationOnMock.class));
        Mockito.when(answerForMock.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenCallRealMethod();

        Map<Object, Object> result = (Map<Object, Object>) invoke(answer, new InvocationOnMock(mockMap, "size", new Object[0]));

        assertThat(result).isInstanceOf(Map.class);
        assertThat(result).isEmpty();
    }

    @Test
    public void testEmptyHashMap() {
        MockitoAnswer answer = mock(Answer.class);
        Mockito.doAnswer(answer).when(answer).answer(ArgumentMatchers.any(InvocationOnMock.class));

        Map<Object, Object> mockHashMap = mock(HashMap.class);
        Answer answerForMock = ReturnsEmptyValues.getInstance();

        Mockito.doAnswer(answerForMock).when(answer).answer(ArgumentMatchers.any(InvocationOnMock.class));
        Mockito.when(answerForMock.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenCallRealMethod();

        Map<Object, Object> result = (Map<Object, Object>) invoke(answer, new InvocationOnMock(mockHashMap, "size", new Object[0]));

        assertThat(result).isInstanceOf(Map.class);
        assertThat(result).isInstanceOf(HashMap.class);
        assertThat(result).isEmpty();
    }

    @Test
    public void testEmptyTreeMap() {
        MockitoAnswer answer = mock(Answer.class);
        Mockito.doAnswer(answer).when(answer).answer(ArgumentMatchers.any(InvocationOnMock.class));

        SortedMap<Object, Object> mockSortedMap = mock(SortedMap.class);
        Answer answerForMock = ReturnsEmptyValues.getInstance();

        Mockito.doAnswer(answerForMock).when(answer).answer(ArgumentMatchers.any(InvocationOnMock.class));
        Mockito.when(answerForMock.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenCallRealMethod();

        SortedMap<Object, Object> result = (SortedMap<Object, Object>) invoke(answer, new InvocationOnMock(mockSortedMap, "size", new Object[0]));

        assertThat(result).isInstanceOf(SortedMap.class);
        assertThat(result).isInstanceOf(TreeMap.class);
        assertThat(result).isEmpty();
    }

    @Test
    public void testEmptyLinkedHashMap() {
        MockitoAnswer answer = mock(Answer.class);
        Mockito.doAnswer(answer).when(answer).answer(ArgumentMatchers.any(InvocationOnMock.class));

        Map<Object, Object> mockLinkedHashMap = mock(LinkedHashMap.class);
        Answer answerForMock = ReturnsEmptyValues.getInstance();

        Mockito.doAnswer(answerForMock).when(answer).answer(ArgumentMatchers.any(InvocationOnMock.class));
        Mockito.when(answerForMock.answer(ArgumentMatchers.any(InvocationOnMock.class))).thenCallRealMethod();

        Map<Object, Object> result = (Map<Object, Object>) invoke(answer, new InvocationOnMock(mockLinkedHashMap, "size", new Object[0]));

        assertThat(result).isInstanceOf(Map.class);
        assertThat(result).isInstanceOf(LinkedHashMap.class);
        assertThat(result).isEmpty();
    }
}