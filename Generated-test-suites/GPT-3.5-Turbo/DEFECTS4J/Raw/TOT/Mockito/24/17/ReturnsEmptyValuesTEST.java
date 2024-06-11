import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.stubbing.defaultanswers.ReturnsEmptyValues;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;
import org.mockito.stubbing.Answer;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues returnsEmptyValues;

    @Before
    public void setUp() {
        returnsEmptyValues = new ReturnsEmptyValues();
    }

    @Test
    public void testAnswer_toString() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]);

        assertEquals("Mock for Object, hashCode: " + invocation.getMock().hashCode(), returnsEmptyValues.answer(invocation));
    }

    @Test
    public void testAnswer_compareTo() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Comparable.class.getDeclaredMethods()[0]);

        assertEquals(1, returnsEmptyValues.answer(invocation));
    }

    @Test
    public void testAnswer_primitiveReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(int.class.getDeclaredMethods()[0]);

        assertEquals(0, returnsEmptyValues.answer(invocation));
    }

    @Test
    public void testAnswer_primitiveWrapperReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Integer.class.getDeclaredMethods()[0]);

        assertEquals(0, returnsEmptyValues.answer(invocation));
    }

    @Test
    public void testAnswer_collectionReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Collection.class.getDeclaredMethods()[0]);

        assertTrue(returnsEmptyValues.answer(invocation) instanceof LinkedList);
        assertTrue(((LinkedList) returnsEmptyValues.answer(invocation)).isEmpty());
    }

    @Test
    public void testAnswer_setReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Set.class.getDeclaredMethods()[0]);

        assertTrue(returnsEmptyValues.answer(invocation) instanceof HashSet);
        assertTrue(((HashSet) returnsEmptyValues.answer(invocation)).isEmpty());
    }

    @Test
    public void testAnswer_hashSetReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(HashSet.class.getDeclaredMethods()[0]);

        assertTrue(returnsEmptyValues.answer(invocation) instanceof HashSet);
        assertTrue(((HashSet) returnsEmptyValues.answer(invocation)).isEmpty());
    }

    @Test
    public void testAnswer_sortedSetReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(SortedSet.class.getDeclaredMethods()[0]);

        assertTrue(returnsEmptyValues.answer(invocation) instanceof TreeSet);
        assertTrue(((TreeSet) returnsEmptyValues.answer(invocation)).isEmpty());
    }

    @Test
    public void testAnswer_treeSetReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TreeSet.class.getDeclaredMethods()[0]);

        assertTrue(returnsEmptyValues.answer(invocation) instanceof TreeSet);
        assertTrue(((TreeSet) returnsEmptyValues.answer(invocation)).isEmpty());
    }

    @Test
    public void testAnswer_linkedHashSetReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(LinkedHashSet.class.getDeclaredMethods()[0]);

        assertTrue(returnsEmptyValues.answer(invocation) instanceof LinkedHashSet);
        assertTrue(((LinkedHashSet) returnsEmptyValues.answer(invocation)).isEmpty());
    }

    @Test
    public void testAnswer_listReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(List.class.getDeclaredMethods()[0]);

        assertTrue(returnsEmptyValues.answer(invocation) instanceof LinkedList);
        assertTrue(((LinkedList) returnsEmptyValues.answer(invocation)).isEmpty());
    }

    @Test
    public void testAnswer_linkedListReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(LinkedList.class.getDeclaredMethods()[0]);

        assertTrue(returnsEmptyValues.answer(invocation) instanceof LinkedList);
        assertTrue(((LinkedList) returnsEmptyValues.answer(invocation)).isEmpty());
    }

    @Test
    public void testAnswer_arrayListReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ArrayList.class.getDeclaredMethods()[0]);

        assertTrue(returnsEmptyValues.answer(invocation) instanceof ArrayList);
        assertTrue(((ArrayList) returnsEmptyValues.answer(invocation)).isEmpty());
    }

    @Test
    public void testAnswer_mapReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Map.class.getDeclaredMethods()[0]);

        assertTrue(returnsEmptyValues.answer(invocation) instanceof HashMap);
        assertTrue(((HashMap) returnsEmptyValues.answer(invocation)).isEmpty());
    }

    @Test
    public void testAnswer_hashMapReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(HashMap.class.getDeclaredMethods()[0]);

        assertTrue(returnsEmptyValues.answer(invocation) instanceof HashMap);
        assertTrue(((HashMap) returnsEmptyValues.answer(invocation)).isEmpty());
    }

    @Test
    public void testAnswer_sortedMapReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(SortedMap.class.getDeclaredMethods()[0]);

        assertTrue(returnsEmptyValues.answer(invocation) instanceof TreeMap);
        assertTrue(((TreeMap) returnsEmptyValues.answer(invocation)).isEmpty());
    }

    @Test
    public void testAnswer_treeMapReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TreeMap.class.getDeclaredMethods()[0]);

        assertTrue(returnsEmptyValues.answer(invocation) instanceof TreeMap);
        assertTrue(((TreeMap) returnsEmptyValues.answer(invocation)).isEmpty());
    }

    @Test
    public void testAnswer_linkedHashMapReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(LinkedHashMap.class.getDeclaredMethods()[0]);

        assertTrue(returnsEmptyValues.answer(invocation) instanceof LinkedHashMap);
        assertTrue(((LinkedHashMap) returnsEmptyValues.answer(invocation)).isEmpty());
    }

    // TODO Add test for Iterable

}