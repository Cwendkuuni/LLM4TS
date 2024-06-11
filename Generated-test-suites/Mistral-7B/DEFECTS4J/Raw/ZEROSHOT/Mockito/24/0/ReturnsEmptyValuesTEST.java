package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.Serializable;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.invocation;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringAnswer() {
        // Given
        MockName mockName = Mockito.mock(MockName.class);
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object mock = mock(Object.class);

        // When
        String answer = (String) returnsEmptyValues.answer(invocationWith(mock, "toString()"));

        // Then
        assertEquals("Mock for [class java.lang.Object, hashCode: " + mock.hashCode() + "]", answer);
    }

    @Test
    public void testCompareToAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        int answer = (int) returnsEmptyValues.answer(invocationWith(mock(Comparable.class), "compareTo", null));

        // Then
        assertEquals(1, answer);
    }

    @Test
    public void testEmptyCollectionAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        Collection<Object> answer = (Collection<Object>) returnsEmptyValues.answer(invocationWith(mock(Collection.class), "newInstance()"));

        // Then
        assertEquals(new LinkedList<>(), answer);
        assertEquals(0, answer.size());
        verify(answers(returnsEmptyValues)).returns(answer.getClass());
    }

    @Test
    public void testEmptySetAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        Set<Object> answer = (Set<Object>) returnsEmptyValues.answer(invocationWith(mock(Set.class), "newInstance()"));

        // Then
        assertEquals(new HashSet<>(), answer);
        assertEquals(0, answer.size());
        verify(answers(returnsEmptyValues)).returns(answer.getClass());
    }

    @Test
    public void testEmptyHashSetAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        Set<Object> answer = (Set<Object>) returnsEmptyValues.answer(invocationWith(mock(HashSet.class), "newInstance()"));

        // Then
        assertEquals(new HashSet<>(), answer);
        assertEquals(0, answer.size());
        verify(answers(returnsEmptyValues)).returns(answer.getClass());
    }

    @Test
    public void testEmptyTreeSetAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        SortedSet<Object> answer = (SortedSet<Object>) returnsEmptyValues.answer(invocationWith(mock(SortedSet.class), "newInstance()"));

        // Then
        assertEquals(new TreeSet<>(), answer);
        assertEquals(0, answer.size());
        verify(answers(returnsEmptyValues)).returns(answer.getClass());
    }

    @Test
    public void testEmptyTreeSetAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        SortedSet<Object> answer = (SortedSet<Object>) returnsEmptyValues.answer(invocationWith(mock(TreeSet.class), "newInstance()"));

        // Then
        assertEquals(new TreeSet<>(), answer);
        assertEquals(0, answer.size());
        verify(answers(returnsEmptyValues)).returns(answer.getClass());
    }

    @Test
    public void testEmptyLinkedHashSetAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        Set<Object> answer = (Set<Object>) returnsEmptyValues.answer(invocationWith(mock(LinkedHashSet.class), "newInstance()"));

        // Then
        assertEquals(new LinkedHashSet<>(), answer);
        assertEquals(0, answer.size());
        verify(answers(returnsEmptyValues)).returns(answer.getClass());
    }

    @Test
    public void testEmptyListAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        List<Object> answer = (List<Object>) returnsEmptyValues.answer(invocationWith(mock(List.class), "newInstance()"));

        // Then
        assertEquals(new LinkedList<>(), answer);
        assertEquals(0, answer.size());
        verify(answers(returnsEmptyValues)).returns(answer.getClass());
    }

    @Test
    public void testEmptyLinkedListAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        List<Object> answer = (List<Object>) returnsEmptyValues.answer(invocationWith(mock(LinkedList.class), "newInstance()"));

        // Then
        assertEquals(new LinkedList<>(), answer);
        assertEquals(0, answer.size());
        verify(answers(returnsEmptyValues)).returns(answer.getClass());
    }

    @Test
    public void testEmptyArrayListAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        List<Object> answer = (List<Object>) returnsEmptyValues.answer(invocationWith(mock(ArrayList.class), "newInstance()"));

        // Then
        assertEquals(new ArrayList<>(), answer);
        assertEquals(0, answer.size());
        verify(answers(returnsEmptyValues)).returns(answer.getClass());
    }

    @Test
    public void testEmptyMapAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        Map<Object, Object> answer = (Map<Object, Object>) returnsEmptyValues.answer(invocationWith(mock(Map.class), "newInstance()"));

        // Then
        assertEquals(new HashMap<>(), answer);
        assertEquals(0, answer.size());
        verify(answers(returnsEmptyValues)).returns(answer.getClass());
    }

    @Test
    public void testEmptyHashMapAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        Map<Object, Object> answer = (Map<Object, Object>) returnsEmptyValues.answer(invocationWith(mock(HashMap.class), "newInstance()"));

        // Then
        assertEquals(new HashMap<>(), answer);
        assertEquals(0, answer.size());
        verify(answers(returnsEmptyValues)).returns(answer.getClass());
    }

    @Test
    public void testEmptyTreeMapAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        SortedMap<Object, Object> answer = (SortedMap<Object, Object>) returnsEmptyValues.answer(invocationWith(mock(TreeMap.class), "newInstance()"));

        // Then
        assertEquals(new TreeMap<>(), answer);
        assertEquals(0, answer.size());
        verify(answers(returnsEmptyValues)).returns(answer.getClass());
    }

    @Test
    public void testEmptyTreeMapAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        SortedMap<Object, Object> answer = (SortedMap<Object, Object>) returnsEmptyValues.answer(invocationWith(mock(TreeMap.class), "newInstance()"));

        // Then
        assertEquals(new TreeMap<>(), answer);
        assertEquals(0, answer.size());
        verify(answers(returnsEmptyValues)).returns(answer.getClass());
    }

    @Test
    public void testEmptyLinkedHashMapAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        // When
        Map<Object, Object> answer = (Map<Object, Object>) returnsEmptyValues.answer(invocationWith(mock(LinkedHashMap.class), "newInstance()"));

        // Then
        assertEquals(new LinkedHashMap<>(), answer);
        assertEquals(0, answer.size());
        verify(answers(returnsEmptyValues)).returns(answer.getClass());
    }

    private InvocationOnMock invocationWith(Object mock, String methodName, Object... arguments) {
        return new InvocationOnMock(mock, Mockito.method(methodName, arguments));
    }

    private InvocationOnMock invocationWith(Class<?> clazz, String methodName, Object... arguments) {
        return invocation(clazz, methodName, arguments);
    }

    private Answer<?> answers(Answer<?> answer) {
        return answer;
    }
}