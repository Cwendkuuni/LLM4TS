
import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.invocation.InvocationOnMock;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer_ToString() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Object.class.getMethod("toString"));
        
        Object result = answer.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof String);
    }

    @Test
    public void testAnswer_CompareTo() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Comparable.class.getMethod("compareTo", Object.class));
        
        Object result = answer.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof Integer);
    }

    @Test
    public void testAnswer_PrimitiveOrWrapper() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(IntFunction.class.getMethod("apply", int.class));
        
        Object result = answer.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof Integer);
    }

    @Test
    public void testAnswer_Collection() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Collection.class.getMethod("isEmpty"));
        
        Object result = answer.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof LinkedList);
    }

    @Test
    public void testAnswer_Set() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Set.class.getMethod("isEmpty"));
        
        Object result = answer.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof HashSet);
    }

    @Test
    public void testAnswer_HashSet() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(HashSet.class.getMethod("isEmpty"));
        
        Object result = answer.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof HashSet);
    }

    @Test
    public void testAnswer_SortedSet() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(SortedSet.class.getMethod("isEmpty"));
        
        Object result = answer.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof TreeSet);
    }

    @Test
    public void testAnswer_TreeSet() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(TreeSet.class.getMethod("isEmpty"));
        
        Object result = answer.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof TreeSet);
    }

    @Test
    public void testAnswer_LinkedHashSet() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(LinkedHashSet.class.getMethod("isEmpty"));
        
        Object result = answer.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof LinkedHashSet);
    }

    @Test
    public void testAnswer_List() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(List.class.getMethod("isEmpty"));
        
        Object result = answer.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof LinkedList);
    }

    @Test
    public void testAnswer_LinkedList() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(LinkedList.class.getMethod("isEmpty"));
        
        Object result = answer.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof LinkedList);
    }

    @Test
    public void testAnswer_ArrayList() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(ArrayList.class.getMethod("isEmpty"));
        
        Object result = answer.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof ArrayList);
    }

    @Test
    public void testAnswer_Map() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(Map.class.getMethod("isEmpty"));
        
        Object result = answer.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof HashMap);
    }

    @Test
    public void testAnswer_HashMap() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(HashMap.class.getMethod("isEmpty"));
        
        Object result = answer.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof HashMap);
    }

    @Test
    public void testAnswer_SortedMap() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(SortedMap.class.getMethod("isEmpty"));
        
        Object result = answer.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof TreeMap);
    }

    @Test
    public void testAnswer_TreeMap() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(TreeMap.class.getMethod("isEmpty"));
        
        Object result = answer.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof TreeMap);
    }

    @Test
    public void testAnswer_LinkedHashMap() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Mockito.when(invocation.getMethod()).thenReturn(LinkedHashMap.class.getMethod("isEmpty"));
        
        Object result = answer.answer(invocation);
        
        assertNotNull(result);
        assertTrue(result instanceof LinkedHashMap);
    }
}

