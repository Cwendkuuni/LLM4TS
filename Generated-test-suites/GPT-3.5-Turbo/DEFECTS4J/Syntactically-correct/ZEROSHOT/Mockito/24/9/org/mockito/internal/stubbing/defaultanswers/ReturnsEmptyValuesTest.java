import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;
import org.mockito.stubbing.Answer;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

    @Test
    public void testToString() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));
        when(invocation.getMock()).thenReturn(null);

        Object result = returnsEmptyValues.answer(invocation);

        assertEquals("Mock for Object, hashCode: 0", result);
    }

    @Test
    public void testCompareToMethod() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Comparable.class.getMethod("compareTo", Object.class));

        Object result = returnsEmptyValues.answer(invocation);

        assertEquals(1, result);
    }

    @Test
    public void testReturnValueForPrimitive() {
        Class<?> type = int.class;

        Object result = returnsEmptyValues.returnValueFor(type);

        assertEquals(0, result);
    }

    @Test
    public void testReturnValueForPrimitiveWrapper() {
        Class<?> type = Integer.class;

        Object result = returnsEmptyValues.returnValueFor(type);

        assertEquals(0, result);
    }

    @Test
    public void testReturnValueForCollection() {
        Class<?> type = Collection.class;

        Object result = returnsEmptyValues.returnValueFor(type);

        assertNotNull(result);
        assertTrue(result instanceof LinkedList);
    }

    @Test
    public void testReturnValueForSet() {
        Class<?> type = Set.class;

        Object result = returnsEmptyValues.returnValueFor(type);

        assertNotNull(result);
        assertTrue(result instanceof HashSet);
    }

    @Test
    public void testReturnValueForHashSet() {
        Class<?> type = HashSet.class;

        Object result = returnsEmptyValues.returnValueFor(type);

        assertNotNull(result);
        assertTrue(result instanceof HashSet);
    }

    @Test
    public void testReturnValueForSortedSet() {
        Class<?> type = SortedSet.class;

        Object result = returnsEmptyValues.returnValueFor(type);

        assertNotNull(result);
        assertTrue(result instanceof TreeSet);
    }

    @Test
    public void testReturnValueForTreeSet() {
        Class<?> type = TreeSet.class;

        Object result = returnsEmptyValues.returnValueFor(type);

        assertNotNull(result);
        assertTrue(result instanceof TreeSet);
    }

    @Test
    public void testReturnValueForLinkedHashSet() {
        Class<?> type = LinkedHashSet.class;

        Object result = returnsEmptyValues.returnValueFor(type);

        assertNotNull(result);
        assertTrue(result instanceof LinkedHashSet);
    }

    @Test
    public void testReturnValueForList() {
        Class<?> type = List.class;

        Object result = returnsEmptyValues.returnValueFor(type);

        assertNotNull(result);
        assertTrue(result instanceof LinkedList);
    }

    @Test
    public void testReturnValueForLinkedList() {
        Class<?> type = LinkedList.class;

        Object result = returnsEmptyValues.returnValueFor(type);

        assertNotNull(result);
        assertTrue(result instanceof LinkedList);
    }

    @Test
    public void testReturnValueForArrayList() {
        Class<?> type = ArrayList.class;

        Object result = returnsEmptyValues.returnValueFor(type);

        assertNotNull(result);
        assertTrue(result instanceof ArrayList);
    }

    @Test
    public void testReturnValueForMap() {
        Class<?> type = Map.class;

        Object result = returnsEmptyValues.returnValueFor(type);

        assertNotNull(result);
        assertTrue(result instanceof HashMap);
    }

    @Test
    public void testReturnValueForHashMap() {
        Class<?> type = HashMap.class;

        Object result = returnsEmptyValues.returnValueFor(type);

        assertNotNull(result);
        assertTrue(result instanceof HashMap);
    }

    @Test
    public void testReturnValueForSortedMap() {
        Class<?> type = SortedMap.class;

        Object result = returnsEmptyValues.returnValueFor(type);

        assertNotNull(result);
        assertTrue(result instanceof TreeMap);
    }

    @Test
    public void testReturnValueForTreeMap() {
        Class<?> type = TreeMap.class;

        Object result = returnsEmptyValues.returnValueFor(type);

        assertNotNull(result);
        assertTrue(result instanceof TreeMap);
    }

    @Test
    public void testReturnValueForLinkedHashMap() {
        Class<?> type = LinkedHashMap.class;

        Object result = returnsEmptyValues.returnValueFor(type);

        assertNotNull(result);
        assertTrue(result instanceof LinkedHashMap);
    }

}