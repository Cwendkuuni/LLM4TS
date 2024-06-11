import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.util.*;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

    // Test for answer() method

    // Test if invocation method is toString()
    @Test
    public void testAnswerToString() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Object.class.getDeclaredMethods()[0]);

        Object result = returnsEmptyValues.answer(invocation);

        assertEquals("Mock for Object, hashCode: " + InvocationOnMock.class.hashCode(), result);
    }

    // Test if invocation method is compareTo()
    @Test
    public void testAnswerCompareTo() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Comparable.class.getDeclaredMethods()[0]);
        when(invocation.getMock()).thenReturn(new Object());
        when(invocation.getArguments()).thenReturn(new Object[] { new Object() });

        Object result = returnsEmptyValues.answer(invocation);

        assertEquals(1, result);
    }

    // Test if invocation method is a primitive or wrapper class type
    @Test
    public void testAnswerPrimitiveOrWrapper() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Integer.class.getDeclaredMethods()[0]);

        Object result = returnsEmptyValues.answer(invocation);

        assertEquals(0, result);
    }

    // Test if invocation method is a collection class type
    @Test
    public void testAnswerCollection() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Collection.class.getDeclaredMethods()[0]);

        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof LinkedList);
        assertTrue(((LinkedList) result).isEmpty());
    }

    // Test if invocation method is a set class type (HashSet)
    @Test
    public void testAnswerSetHashSet() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(HashSet.class.getDeclaredMethods()[0]);

        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof HashSet);
        assertTrue(((HashSet) result).isEmpty());
    }

    // Test if invocation method is a set class type (SortedSet)
    @Test
    public void testAnswerSetSortedSet() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(SortedSet.class.getDeclaredMethods()[0]);

        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof TreeSet);
        assertTrue(((TreeSet) result).isEmpty());
    }

    // Test if invocation method is a set class type (LinkedHashSet)
    @Test
    public void testAnswerSetLinkedHashSet() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(LinkedHashSet.class.getDeclaredMethods()[0]);

        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof LinkedHashSet);
        assertTrue(((LinkedHashSet) result).isEmpty());
    }

    // Test if invocation method is a list class type (List)
    @Test
    public void testAnswerListList() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(List.class.getDeclaredMethods()[0]);

        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof LinkedList);
        assertTrue(((LinkedList) result).isEmpty());
    }

    // Test if invocation method is a list class type (LinkedList)
    @Test
    public void testAnswerListLinkedList() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(LinkedList.class.getDeclaredMethods()[0]);

        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof LinkedList);
        assertTrue(((LinkedList) result).isEmpty());
    }

    // Test if invocation method is a list class type (ArrayList)
    @Test
    public void testAnswerListArrayList() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ArrayList.class.getDeclaredMethods()[0]);

        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof ArrayList);
        assertTrue(((ArrayList) result).isEmpty());
    }

    // Test if invocation method is a map class type (Map)
    @Test
    public void testAnswerMap() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Map.class.getDeclaredMethods()[0]);

        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof HashMap);
        assertTrue(((HashMap) result).isEmpty());
    }

    // Test if invocation method is a map class type (HashMap)
    @Test
    public void testAnswerHashMap() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(HashMap.class.getDeclaredMethods()[0]);

        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof HashMap);
        assertTrue(((HashMap) result).isEmpty());
    }

    // Test if invocation method is a map class type (SortedMap)
    @Test
    public void testAnswerSortedMap() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(SortedMap.class.getDeclaredMethods()[0]);

        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof TreeMap);
        assertTrue(((TreeMap) result).isEmpty());
    }

    // Test if invocation method is a map class type (TreeMap)
    @Test
    public void testAnswerTreeMap() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TreeMap.class.getDeclaredMethods()[0]);

        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof TreeMap);
        assertTrue(((TreeMap) result).isEmpty());
    }

    // Test if invocation method is a map class type (LinkedHashMap)
    @Test
    public void testAnswerLinkedHashMap() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(LinkedHashMap.class.getDeclaredMethods()[0]);

        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof LinkedHashMap);
        assertTrue(((LinkedHashMap) result).isEmpty());
    }

    // Test for returnValueFor() method

    // Test if type is a primitive or wrapper class type (Integer)
    @Test
    public void testReturnValueForPrimitiveOrWrapperInteger() {
        Object result = returnsEmptyValues.returnValueFor(Integer.class);

        assertEquals(0, result);
    }

    // Test if type is a collection class type
    @Test
    public void testReturnValueForCollection() {
        Object result = returnsEmptyValues.returnValueFor(Collection.class);

        assertTrue(result instanceof LinkedList);
        assertTrue(((LinkedList) result).isEmpty());
    }

    // Test if type is a set class type (HashSet)
    @Test
    public void testReturnValueForSetHashSet() {
        Object result = returnsEmptyValues.returnValueFor(HashSet.class);

        assertTrue(result instanceof HashSet);
        assertTrue(((HashSet) result).isEmpty());
    }

    // Test if type is a set class type (SortedSet)
    @Test
    public void testReturnValueForSetSortedSet() {
        Object result = returnsEmptyValues.returnValueFor(SortedSet.class);

        assertTrue(result instanceof TreeSet);
        assertTrue(((TreeSet) result).isEmpty());
    }

    // Test if type is a set class type (LinkedHashSet)
    @Test
    public void testReturnValueForSetLinkedHashSet() {
        Object result = returnsEmptyValues.returnValueFor(LinkedHashSet.class);

        assertTrue(result instanceof LinkedHashSet);
        assertTrue(((LinkedHashSet) result).isEmpty());
    }

    // Test if type is a list class type (List)
    @Test
    public void testReturnValueForListList() {
        Object result = returnsEmptyValues.returnValueFor(List.class);

        assertTrue(result instanceof LinkedList);
        assertTrue(((LinkedList) result).isEmpty());
    }

    // Test if type is a list class type (LinkedList)
    @Test
    public void testReturnValueForListLinkedList() {
        Object result = returnsEmptyValues.returnValueFor(LinkedList.class);

        assertTrue(result instanceof LinkedList);
        assertTrue(((LinkedList) result).isEmpty());
    }

    // Test if type is a list class type (ArrayList)
    @Test
    public void testReturnValueForListArrayList() {
        Object result = returnsEmptyValues.returnValueFor(ArrayList.class);

        assertTrue(result instanceof ArrayList);
        assertTrue(((ArrayList) result).isEmpty());
    }

    // Test if type is a map class type (Map)
    @Test
    public void testReturnValueForMap() {
        Object result = returnsEmptyValues.returnValueFor(Map.class);

        assertTrue(result instanceof HashMap);
        assertTrue(((HashMap) result).isEmpty());
    }

    // Test if type is a map class type (HashMap)
    @Test
    public void testReturnValueForHashMap() {
        Object result = returnsEmptyValues.returnValueFor(HashMap.class);

        assertTrue(result instanceof HashMap);
        assertTrue(((HashMap) result).isEmpty());
    }

    // Test if type is a map class type (SortedMap)
    @Test
    public void testReturnValueForSortedMap() {
        Object result = returnsEmptyValues.returnValueFor(SortedMap.class);

        assertTrue(result instanceof TreeMap);
        assertTrue(((TreeMap) result).isEmpty());
    }

    // Test if type is a map class type (TreeMap)
    @Test
    public void testReturnValueForTreeMap() {
        Object result = returnsEmptyValues.returnValueFor(TreeMap.class);

        assertTrue(result instanceof TreeMap);
        assertTrue(((TreeMap) result).isEmpty());
    }

    // Test if type is a map class type (LinkedHashMap)
    @Test
    public void testReturnValueForLinkedHashMap() {
        Object result = returnsEmptyValues.returnValueFor(LinkedHashMap.class);

        assertTrue(result instanceof LinkedHashMap);
        assertTrue(((LinkedHashMap) result).isEmpty());
    }

}