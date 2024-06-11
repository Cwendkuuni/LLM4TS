import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer_toString() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));

        Object result = answer.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof String);
        // Test the format of mock string representation
        assertTrue(result.toString().contains("Mock for "));
        assertTrue(result.toString().contains(", hashCode: "));
    }

    @Test
    public void testAnswer_compareTo_sameReference() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object[] arguments = new Object[] {invocation.getMock()};
        when(invocation.getMethod()).thenReturn(Comparable.class.getMethod("compareTo", Object.class));
        when(invocation.getArguments()).thenReturn(arguments);

        Object result = answer.answer(invocation);

        assertNotNull(result);
        assertEquals(0, result);
        assertTrue(result instanceof Integer);
    }
    
    @Test
    public void testAnswer_compareTo_differentReference() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        Object[] arguments = new Object[] {new Object()};
        when(invocation.getMethod()).thenReturn(Comparable.class.getMethod("compareTo", Object.class));
        when(invocation.getArguments()).thenReturn(arguments);

        Object result = answer.answer(invocation);

        assertNotNull(result);
        assertNotEquals(0, result);
        assertTrue(result instanceof Integer);
    }

    @Test
    public void testAnswer_primitiveWrapper() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Integer.class.getMethod("intValue"));

        Object result = answer.answer(invocation);

        assertNotNull(result);
        assertEquals(0, result);
        assertTrue(result instanceof Integer);
    }
    
    @Test
    public void testAnswer_primitive() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(int.class.getMethod("intValue"));

        Object result = answer.answer(invocation);

        assertNotNull(result);
        assertEquals(0, result);
        assertTrue(result instanceof Integer);
    }

    @Test
    public void testAnswer_collection() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Collection.class.getMethod("isEmpty"));

        Object result = answer.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof LinkedList<?>);
        assertTrue(((LinkedList<?>) result).isEmpty());
    }

    @Test
    public void testAnswer_set() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Set.class.getMethod("isEmpty"));

        Object result = answer.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof HashSet<?>);
        assertTrue(((HashSet<?>) result).isEmpty());
    }

    @Test
    public void testAnswer_hashSet() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(HashSet.class.getMethod("isEmpty"));

        Object result = answer.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof HashSet<?>);
        assertTrue(((HashSet<?>) result).isEmpty());
    }

    @Test
    public void testAnswer_sortedSet() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(SortedSet.class.getMethod("isEmpty"));

        Object result = answer.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof TreeSet<?>);
        assertTrue(((TreeSet<?>) result).isEmpty());
    }

    @Test
    public void testAnswer_treeSet() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TreeSet.class.getMethod("isEmpty"));

        Object result = answer.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof TreeSet<?>);
        assertTrue(((TreeSet<?>) result).isEmpty());
    }

    @Test
    public void testAnswer_linkedHashSet() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(LinkedHashSet.class.getMethod("isEmpty"));

        Object result = answer.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof LinkedHashSet<?>);
        assertTrue(((LinkedHashSet<?>) result).isEmpty());
    }

    @Test
    public void testAnswer_list() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(List.class.getMethod("isEmpty"));

        Object result = answer.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof LinkedList<?>);
        assertTrue(((LinkedList<?>) result).isEmpty());
    }

    @Test
    public void testAnswer_linkedList() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(LinkedList.class.getMethod("isEmpty"));

        Object result = answer.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof LinkedList<?>);
        assertTrue(((LinkedList<?>) result).isEmpty());
    }

    @Test
    public void testAnswer_arrayList() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ArrayList.class.getMethod("isEmpty"));

        Object result = answer.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof ArrayList<?>);
        assertTrue(((ArrayList<?>) result).isEmpty());
    }

    @Test
    public void testAnswer_map() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Map.class.getMethod("isEmpty"));

        Object result = answer.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof HashMap<?, ?>);
        assertTrue(((HashMap<?, ?>) result).isEmpty());
    }

    @Test
    public void testAnswer_hashMap() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(HashMap.class.getMethod("isEmpty"));

        Object result = answer.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof HashMap<?, ?>);
        assertTrue(((HashMap<?, ?>) result).isEmpty());
    }

    @Test
    public void testAnswer_sortedMap() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(SortedMap.class.getMethod("isEmpty"));

        Object result = answer.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof TreeMap<?, ?>);
        assertTrue(((TreeMap<?, ?>) result).isEmpty());
    }

    @Test
    public void testAnswer_treeMap() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(TreeMap.class.getMethod("isEmpty"));

        Object result = answer.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof TreeMap<?, ?>);
        assertTrue(((TreeMap<?, ?>) result).isEmpty());
    }

    @Test
    public void testAnswer_linkedHashMap() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(LinkedHashMap.class.getMethod("isEmpty"));

        Object result = answer.answer(invocation);

        assertNotNull(result);
        assertTrue(result instanceof LinkedHashMap<?, ?>);
        assertTrue(((LinkedHashMap<?, ?>) result).isEmpty());
    }
}