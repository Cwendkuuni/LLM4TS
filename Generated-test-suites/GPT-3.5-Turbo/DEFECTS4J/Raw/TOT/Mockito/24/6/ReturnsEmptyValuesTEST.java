import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer_toString() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Object.class.getMethods()[0]);
        Object result = returnsEmptyValues.answer(invocation);

        assertEquals("Mock for Object, hashCode: " + invocation.getMock().hashCode(), result);
    }

    @Test
    public void testAnswer_compareTo() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Comparable.class.getMethods()[0]);
        Object result = returnsEmptyValues.answer(invocation);

        assertEquals(1, result);
    }

    @Test
    public void testAnswer_primitiveReturnType() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethods()[0]);
        Object result = returnsEmptyValues.answer(invocation);

        assertEquals(false, result);
    }

    @Test
    public void testAnswer_CollectionReturnType() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethods()[1]);
        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof LinkedList<?>);
        assertEquals(0, ((LinkedList<?>) result).size());
    }

    @Test
    public void testAnswer_SetReturnType() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethods()[2]);
        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof HashSet<?>);
        assertEquals(0, ((HashSet<?>) result).size());
    }

    @Test
    public void testAnswer_HashSetReturnType() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethods()[3]);
        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof HashSet<?>);
        assertEquals(0, ((HashSet<?>) result).size());
    }

    @Test
    public void testAnswer_SortedSetReturnType() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethods()[4]);
        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof TreeSet<?>);
        assertEquals(0, ((TreeSet<?>) result).size());
    }

    @Test
    public void testAnswer_TreeSetReturnType() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethods()[5]);
        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof TreeSet<?>);
        assertEquals(0, ((TreeSet<?>) result).size());
    }

    @Test
    public void testAnswer_LinkedHashSetReturnType() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethods()[6]);
        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof LinkedHashSet<?>);
        assertEquals(0, ((LinkedHashSet<?>) result).size());
    }

    @Test
    public void testAnswer_ListReturnType() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethods()[7]);
        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof LinkedList<?>);
        assertEquals(0, ((LinkedList<?>) result).size());
    }

    @Test
    public void testAnswer_LinkedListReturnType() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethods()[8]);
        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof LinkedList<?>);
        assertEquals(0, ((LinkedList<?>) result).size());
    }

    @Test
    public void testAnswer_ArrayListReturnType() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethods()[9]);
        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof ArrayList<?>);
        assertEquals(0, ((ArrayList<?>) result).size());
    }

    @Test
    public void testAnswer_MapReturnType() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethods()[10]);
        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof HashMap<?, ?>);
        assertEquals(0, ((HashMap<?, ?>) result).size());
    }

    @Test
    public void testAnswer_HashMapReturnType() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethods()[11]);
        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof HashMap<?, ?>);
        assertEquals(0, ((HashMap<?, ?>) result).size());
    }

    @Test
    public void testAnswer_SortedMapReturnType() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethods()[12]);
        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof TreeMap<?, ?>);
        assertEquals(0, ((TreeMap<?, ?>) result).size());
    }

    @Test
    public void testAnswer_TreeMapReturnType() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethods()[13]);
        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof TreeMap<?, ?>);
        assertEquals(0, ((TreeMap<?, ?>) result).size());
    }

    @Test
    public void testAnswer_LinkedHashMapReturnType() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();

        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethods()[14]);
        Object result = returnsEmptyValues.answer(invocation);

        assertTrue(result instanceof LinkedHashMap<?, ?>);
        assertEquals(0, ((LinkedHashMap<?, ?>) result).size());
    }

}