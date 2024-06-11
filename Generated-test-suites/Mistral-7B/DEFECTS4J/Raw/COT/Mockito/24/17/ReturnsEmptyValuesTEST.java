import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Answers;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;

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

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class ReturnsEmptyValuesTest {

    @Test
    public void testToStringMethod() {
        // Given
        Object mock = Mockito.mock(String.class);

        // When
        String result = (String) new ReturnsEmptyValues().answer(Mockito.argThat(i -> i.getMethod().getName().equals("toString")));

        // Then
        assertEquals("Mock for " + mock.getClass().getSimpleName() + ", hashCode: " + mock.hashCode(), result);
    }

    @Test
    public void testCompareToMethod() {
        // Given
        Object mock1 = Mockito.mock(Comparable.class);
        Object mock2 = Mockito.mock(Comparable.class);

        // When
        int result = (Integer) new ReturnsEmptyValues().answer(Mockito.argThat(i -> i.getMethod().getName().equals("compareTo")));

        // Then
        when(mock1.compareTo(mock2)).thenReturn(1);
        int comparisonResult = ((Comparable) mock1).compareTo(mock2);
        verify(mock1).compareTo(mock2);
        assertEquals(1, comparisonResult);
        assertEquals(1, result);
    }

    @Test(expected = AssertionError.class)
    public void testPrimitiveTypes() {
        // Given
        Object mock = Mockito.mock(int.class);

        // When
        int result = (int) new ReturnsEmptyValues().answer(Mockito.argThat(i -> i.getMethod().getName().equals("intValue")));

        // Then
        assertEquals(0, result);
    }

    @Test
    public void testEmptyCollectionTypes() {
        // Given
        Collection<String> mockCollection = Mockito.mock(Collection.class);

        // When
        Collection<String> result = (Collection<String>) new ReturnsEmptyValues().answer(Mockito.argThat(i -> i.getMethod().getName().equals("iterator").or(i -> i.getMethod().getName().equals("values"))));

        // Then
        assertEquals(new LinkedList<String>(), result);
        assertEquals(0, mockCollection.size());
        verify(mockCollection).size();
    }

    @Test
    public void testEmptySetTypes() {
        // Given
        Set<String> mockSet = Mockito.mock(Set.class);

        // When
        Set<String> result = (Set<String>) new ReturnsEmptyValues().answer(Mockito.argThat(i -> i.getMethod().getName().equals("iterator").or(i -> i.getMethod().getName().equals("values"))));

        // Then
        assertEquals(new HashSet<String>(), result);
        assertEquals(0, mockSet.size());
        verify(mockSet).size();
    }

    @Test
    public void testEmptySortedSetTypes() {
        // Given
        SortedSet<String> mockSortedSet = Mockito.mock(SortedSet.class);

        // When
        SortedSet<String> result = (SortedSet<String>) new ReturnsEmptyValues().answer(Mockito.argThat(i -> i.getMethod().getName().equals("iterator").or(i -> i.getMethod().getName().equals("descendingIterator"))));

        // Then
        assertEquals(new TreeSet<String>(), result);
        assertEquals(0, mockSortedSet.size());
        verify(mockSortedSet).size();
    }

    @Test
    public void testEmptyListTypes() {
        // Given
        List<String> mockList = Mockito.mock(List.class);

        // When
        List<String> result = (List<String>) new ReturnsEmptyValues().answer(Mockito.argThat(i -> i.getMethod().getName().equals("iterator").or(i -> i.getMethod().getName().equals("listIterator"))));

        // Then
        assertEquals(new LinkedList<String>(), result);
        assertEquals(0, mockList.size());
        verify(mockList).size();
    }

    @Test
    public void testEmptyLinkedListTypes() {
        // Given
        List<String> mockLinkedList = Mockito.mock(LinkedList.class);

        // When
        List<String> result = (List<String>) new ReturnsEmptyValues().answer(Mockito.argThat(i -> i.getMethod().getName().equals("iterator").or(i -> i.getMethod().getName().equals("listIterator"))));

        // Then
        assertEquals(new LinkedList<String>(), result);
        assertEquals(0, mockLinkedList.size());
        verify(mockLinkedList).size();
    }

    @Test
    public void testEmptyArrayListTypes() {
        // Given
        List<String> mockArrayList = Mockito.mock(ArrayList.class);

        // When
        List<String> result = (List<String>) new ReturnsEmptyValues().answer(Mockito.argThat(i -> i.getMethod().getName().equals("iterator").or(i -> i.getMethod().getName().equals("listIterator"))));

        // Then
        assertEquals(new ArrayList<String>(), result);
        assertEquals(0, mockArrayList.size());
        verify(mockArrayList).size();
    }

    @Test
    public void testEmptyMapTypes() {
        // Given
        Map<String, String> mockMap = Mockito.mock(Map.class);

        // When
        Map<String, String> result = (Map<String, String>) new ReturnsEmptyValues().answer(Mockito.argThat(i -> i.getMethod().getName().equals("entrySet").or(i -> i.getMethod().getName().equals("values"))));

        // Then
        assertEquals(new HashMap<String, String>(), result);
        assertEquals(0, mockMap.size());
        verify(mockMap).size();
    }

    @Test
    public void testEmptyHashMapTypes() {
        // Given
        Map<String, String> mockHashMap = Mockito.mock(HashMap.class);

        // When
        Map<String, String> result = (Map<String, String>) new ReturnsEmptyValues().answer(Mockito.argThat(i -> i.getMethod().getName().equals("entrySet").or(i -> i.getMethod().getName().equals("values"))));

        // Then
        assertEquals(new HashMap<String, String>(), result);
        assertEquals(0, mockHashMap.size());
        verify(mockHashMap).size();
    }

    @Test
    public void testEmptySortedMapTypes() {
        // Given
        SortedMap<String, String> mockSortedMap = Mockito.mock(SortedMap.class);

        // When
        SortedMap<String, String> result = (SortedMap<String, String>) new ReturnsEmptyValues().answer(Mockito.argThat(i -> i.getMethod().getName().equals("entrySet").or(i -> i.getMethod().getName().equals("values"))));

        // Then
        assertEquals(new TreeMap<String, String>(), result);
        assertEquals(0, mockSortedMap.size());
        verify(mockSortedMap).size();
    }

    @Test
    public void testEmptyTreeMapTypes() {
        // Given
        SortedMap<String, String> mockTreeMap = Mockito.mock(TreeMap.class);

        // When
        SortedMap<String, String> result = (SortedMap<String, String>) new ReturnsEmptyValues().answer(Mockito.argThat(i -> i.getMethod().getName().equals("entrySet").or(i -> i.getMethod().getName().equals("values"))));

        // Then
        assertEquals(new TreeMap<String, String>(), result);
        assertEquals(0, mockTreeMap.size());
        verify(mockTreeMap).size();
    }

    @Test
    public void testEmptyLinkedHashMapTypes() {
        // Given
        Map<String, String> mockLinkedHashMap = Mockito.mock(LinkedHashMap.class);

        // When
        Map<String, String> result = (Map<String, String>) new ReturnsEmptyValues().answer(Mockito.argThat(i -> i.getMethod().getName().equals("entrySet").or(i -> i.getMethod().getName().equals("values"))));

        // Then
        assertEquals(new LinkedHashMap<String, String>(), result);
        assertEquals(0, mockLinkedHashMap.size());
        verify(mockLinkedHashMap).size();
    }

    @Test
    public void testNullReturnType() {
        // Given
        Object mock = Mockito.mock(Object.class);

        // When
        Object result = new ReturnsEmptyValues().answer(Mockito.argThat(i -> i.getMethod().getName().equals("toString")));

        // Then
        assertNull(result);
    }
}