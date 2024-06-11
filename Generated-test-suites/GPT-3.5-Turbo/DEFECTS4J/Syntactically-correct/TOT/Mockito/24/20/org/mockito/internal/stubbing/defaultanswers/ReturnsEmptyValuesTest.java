import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.*;

public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues returnsEmptyValues;
    private InvocationOnMock toStringInvocation;
    private InvocationOnMock compareToInvocation;
    private InvocationOnMock returnValueInvocation;

    @Before
    public void setUp() {
        returnsEmptyValues = new ReturnsEmptyValues();
        toStringInvocation = mock(InvocationOnMock.class);
        compareToInvocation = mock(InvocationOnMock.class);
        returnValueInvocation = mock(InvocationOnMock.class);
    }

    @Test
    public void answer_toString_shouldReturnMockDescription() {
        // Arrange
        Object mock = mock(Object.class);
        when(toStringInvocation.getMock()).thenReturn(mock);

        // Act
        Object actual = returnsEmptyValues.answer(toStringInvocation);

        // Assert
        String expected = "Mock for Object, hashCode: " + mock.hashCode();
        assertEquals(expected, actual);
    }

    @Test
    public void answer_compareTo_shouldReturnNonZeroValue() {
        // Act
        Object actual = returnsEmptyValues.answer(compareToInvocation);

        // Assert
        int nonZero = 1;
        assertEquals(nonZero, actual);
    }

    @Test
    public void answer_primitiveWrapperType_shouldReturnConsistentValue() {
        // Arrange
        when(returnValueInvocation.getMethod().getReturnType()).thenReturn(Integer.class);

        // Act
        Object actual = returnsEmptyValues.answer(returnValueInvocation);

        // Assert
        int expected = 0;
        assertEquals(expected, actual);
    }

    @Test
    public void answer_primitiveType_shouldReturnDefaultValue() {
        // Arrange
        when(returnValueInvocation.getMethod().getReturnType()).thenReturn(int.class);

        // Act
        Object actual = returnsEmptyValues.answer(returnValueInvocation);

        // Assert
        int expected = 0;
        assertEquals(expected, actual);
    }

    @Test
    public void answer_CollectionType_shouldReturnEmptyLinkedList() {
        // Arrange
        when(returnValueInvocation.getMethod().getReturnType()).thenReturn(Collection.class);

        // Act
        Object actual = returnsEmptyValues.answer(returnValueInvocation);

        // Assert
        assertTrue(actual instanceof LinkedList);
        assertTrue(((LinkedList) actual).isEmpty());
    }

    @Test
    public void answer_SetType_shouldReturnEmptyHashSet() {
        // Arrange
        when(returnValueInvocation.getMethod().getReturnType()).thenReturn(Set.class);

        // Act
        Object actual = returnsEmptyValues.answer(returnValueInvocation);

        // Assert
        assertTrue(actual instanceof HashSet);
        assertTrue(((HashSet) actual).isEmpty());
    }

    @Test
    public void answer_HashSetType_shouldReturnEmptyHashSet() {
        // Arrange
        when(returnValueInvocation.getMethod().getReturnType()).thenReturn(HashSet.class);

        // Act
        Object actual = returnsEmptyValues.answer(returnValueInvocation);

        // Assert
        assertTrue(actual instanceof HashSet);
        assertTrue(((HashSet) actual).isEmpty());
    }

    @Test
    public void answer_SortedSetType_shouldReturnEmptyTreeSet() {
        // Arrange
        when(returnValueInvocation.getMethod().getReturnType()).thenReturn(SortedSet.class);

        // Act
        Object actual = returnsEmptyValues.answer(returnValueInvocation);

        // Assert
        assertTrue(actual instanceof TreeSet);
        assertTrue(((TreeSet) actual).isEmpty());
    }

    @Test
    public void answer_TreeSetType_shouldReturnEmptyTreeSet() {
        // Arrange
        when(returnValueInvocation.getMethod().getReturnType()).thenReturn(TreeSet.class);

        // Act
        Object actual = returnsEmptyValues.answer(returnValueInvocation);

        // Assert
        assertTrue(actual instanceof TreeSet);
        assertTrue(((TreeSet) actual).isEmpty());
    }

    @Test
    public void answer_LinkedHashSetType_shouldReturnEmptyLinkedHashSet() {
        // Arrange
        when(returnValueInvocation.getMethod().getReturnType()).thenReturn(LinkedHashSet.class);

        // Act
        Object actual = returnsEmptyValues.answer(returnValueInvocation);

        // Assert
        assertTrue(actual instanceof LinkedHashSet);
        assertTrue(((LinkedHashSet) actual).isEmpty());
    }

    @Test
    public void answer_ListType_shouldReturnEmptyLinkedList() {
        // Arrange
        when(returnValueInvocation.getMethod().getReturnType()).thenReturn(List.class);

        // Act
        Object actual = returnsEmptyValues.answer(returnValueInvocation);

        // Assert
        assertTrue(actual instanceof LinkedList);
        assertTrue(((LinkedList) actual).isEmpty());
    }

    @Test
    public void answer_LinkedListType_shouldReturnEmptyLinkedList() {
        // Arrange
        when(returnValueInvocation.getMethod().getReturnType()).thenReturn(LinkedList.class);

        // Act
        Object actual = returnsEmptyValues.answer(returnValueInvocation);

        // Assert
        assertTrue(actual instanceof LinkedList);
        assertTrue(((LinkedList) actual).isEmpty());
    }

    @Test
    public void answer_ArrayListType_shouldReturnEmptyArrayList() {
        // Arrange
        when(returnValueInvocation.getMethod().getReturnType()).thenReturn(ArrayList.class);

        // Act
        Object actual = returnsEmptyValues.answer(returnValueInvocation);

        // Assert
        assertTrue(actual instanceof ArrayList);
        assertTrue(((ArrayList) actual).isEmpty());
    }

    @Test
    public void answer_MapType_shouldReturnEmptyHashMap() {
        // Arrange
        when(returnValueInvocation.getMethod().getReturnType()).thenReturn(Map.class);

        // Act
        Object actual = returnsEmptyValues.answer(returnValueInvocation);

        // Assert
        assertTrue(actual instanceof HashMap);
        assertTrue(((HashMap) actual).isEmpty());
    }

    @Test
    public void answer_HashMapType_shouldReturnEmptyHashMap() {
        // Arrange
        when(returnValueInvocation.getMethod().getReturnType()).thenReturn(HashMap.class);

        // Act
        Object actual = returnsEmptyValues.answer(returnValueInvocation);

        // Assert
        assertTrue(actual instanceof HashMap);
        assertTrue(((HashMap) actual).isEmpty());
    }

    @Test
    public void answer_SortedMapType_shouldReturnEmptyTreeMap() {
        // Arrange
        when(returnValueInvocation.getMethod().getReturnType()).thenReturn(SortedMap.class);

        // Act
        Object actual = returnsEmptyValues.answer(returnValueInvocation);

        // Assert
        assertTrue(actual instanceof TreeMap);
        assertTrue(((TreeMap) actual).isEmpty());
    }

    @Test
    public void answer_TreeMapType_shouldReturnEmptyTreeMap() {
        // Arrange
        when(returnValueInvocation.getMethod().getReturnType()).thenReturn(TreeMap.class);

        // Act
        Object actual = returnsEmptyValues.answer(returnValueInvocation);

        // Assert
        assertTrue(actual instanceof TreeMap);
        assertTrue(((TreeMap) actual).isEmpty());
    }

    @Test
    public void answer_LinkedHashMapType_shouldReturnEmptyLinkedHashMap() {
        // Arrange
        when(returnValueInvocation.getMethod().getReturnType()).thenReturn(LinkedHashMap.class);

        // Act
        Object actual = returnsEmptyValues.answer(returnValueInvocation);

        // Assert
        assertTrue(actual instanceof LinkedHashMap);
        assertTrue(((LinkedHashMap) actual).isEmpty());
    }

    @Test
    public void answer_otherReturnTypes_shouldReturnNull() {
        // Arrange
        when(returnValueInvocation.getMethod().getReturnType()).thenReturn(Object.class);

        // Act
        Object actual = returnsEmptyValues.answer(returnValueInvocation);

        // Assert
        assertNull(actual);
    }
}