package org.mockito.internal.stubbing.defaultanswers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InvocationOnMock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMockArguments;
import org.mockito.runner.JUnit4;

import java.io.Serializable;
import java.util.*;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for the ReturnsEmptyValues class.
 */
@RunWith(JUnit4.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object mock = Mockito.mock(String.class);

        // When
        String answer = (String) returnsEmptyValues.answer(new InvocationOnMock(mock, null, "toString()", new Object[0], new Object[0], null));

        // Then
        verify(mock).toString();
        assertEquals("Mock for String, hashCode: " + mock.hashCode(), answer);
    }

    @Test
    public void testCompareToAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object mock = Mockito.mock(Number.class);
        Object anotherObject = Mockito.mock(Number.class);

        // When
        when(mock.compareTo(anotherObject)).thenAnswer(returnsEmptyValues);

        // Then
        int result = (Integer) mock.compareTo(anotherObject);

        // Verify that compareTo method was called
        verify(mock).compareTo(anotherObject);

        // Check that the answer is not zero since mocks by default should return 1 for compareTo method
        assertNotEquals(0, result);
    }

    @Test
    public void testEmptyCollectionAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object mock = Mockito.mock(Collection.class);

        // When
        Collection answer = (Collection) returnsEmptyValues.answer(new InvocationOnMock(mock, null, null, new Object[0], new Object[0], null));

        // Then
        assertTrue(answer instanceof ArrayList);
        assertTrue(answer.isEmpty());
    }

    @Test
    public void testEmptySetAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object mock = Mockito.mock(Set.class);

        // When
        Set answer = (Set) returnsEmptyValues.answer(new InvocationOnMock(mock, null, null, new Object[0], new Object[0], null));

        // Then
        assertTrue(answer instanceof HashSet);
        assertTrue(answer.isEmpty());
    }

    @Test
    public void testEmptySortedSetAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object mock = Mockito.mock(SortedSet.class);

        // When
        SortedSet answer = (SortedSet) returnsEmptyValues.answer(new InvocationOnMock(mock, null, null, new Object[0], new Object[0], null));

        // Then
        assertTrue(answer instanceof TreeSet);
        assertTrue(answer.isEmpty());
    }

    @Test
    public void testEmptyListAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object mock = Mockito.mock(List.class);

        // When
        List answer = (List) returnsEmptyValues.answer(new InvocationOnMock(mock, null, null, new Object[0], new Object[0], null));

        // Then
        assertTrue(answer instanceof LinkedList);
        assertTrue(answer.isEmpty());
    }

    @Test
    public void testEmptyLinkedListAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object mock = Mockito.mock(LinkedList.class);

        // When
        List answer = (List) returnsEmptyValues.answer(new InvocationOnMock(mock, null, null, new Object[0], new Object[0], null));

        // Then
        assertTrue(answer instanceof LinkedList);
        assertTrue(answer.isEmpty());
    }

    @Test
    public void testEmptyArrayListAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object mock = Mockito.mock(ArrayList.class);

        // When
        List answer = (List) returnsEmptyValues.answer(new InvocationOnMock(mock, null, null, new Object[0], new Object[0], null));

        // Then
        assertTrue(answer instanceof ArrayList);
        assertTrue(answer.isEmpty());
    }

    @Test
    public void testEmptyMapAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object mock = Mockito.mock(Map.class);

        // When
        Map answer = (Map) returnsEmptyValues.answer(new InvocationOnMock(mock, null, null, new Object[0], new Object[0], null));

        // Then
        assertTrue(answer instanceof HashMap);
        assertTrue(answer.isEmpty());
    }

    @Test
    public void testEmptyHashMapAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object mock = Mockito.mock(HashMap.class);

        // When
        Map answer = (Map) returnsEmptyValues.answer(new InvocationOnMock(mock, null, null, new Object[0], new Object[0], null));

        // Then
        assertTrue(answer instanceof HashMap);
        assertTrue(answer.isEmpty());
    }

    @Test
    public void testEmptySortedMapAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object mock = Mockito.mock(SortedMap.class);

        // When
        SortedMap answer = (SortedMap) returnsEmptyValues.answer(new InvocationOnMock(mock, null, null, new Object[0], new Object[0], null));

        // Then
        assertTrue(answer instanceof TreeMap);
        assertTrue(answer.isEmpty());
    }

    @Test
    public void testEmptyTreeMapAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object mock = Mockito.mock(TreeMap.class);

        // When
        SortedMap answer = (SortedMap) returnsEmptyValues.answer(new InvocationOnMock(mock, null, null, new Object[0], new Object[0], null));

        // Then
        assertTrue(answer instanceof TreeMap);
        assertTrue(answer.isEmpty());
    }

    @Test
    public void testEmptyLinkedHashMapAnswer() {
        // Given
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object mock = Mockito.mock(LinkedHashMap.class);

        // When
        Map answer = (Map) returnsEmptyValues.answer(new InvocationOnMock(mock, null, null, new Object[0], new Object[0], null));

        // Then
        assertTrue(answer instanceof LinkedHashMap);
        assertTrue(answer.isEmpty());
    }
}