import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

public class ReturnsEmptyValuesTest {

    private ReturnsEmptyValues returnsEmptyValues;

    @Before
    public void setUp() {
        returnsEmptyValues = new ReturnsEmptyValues();
    }

    @Test
    public void testAnswerToString() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(String.class.getDeclaredMethods()[0]); // Just selecting the first method as it is irrelevant for this test
        when(invocation.getMock()).thenReturn(mock(Object.class));
        
        Object result = returnsEmptyValues.answer(invocation);
        
        String expected = "Mock for Object, hashCode: " + invocation.getMock().hashCode();
        assertEquals(expected, result);
    }

    @Test
    public void testAnswerCompareTo() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(Comparable.class.getDeclaredMethods()[0]); // Just selecting the first method as it is irrelevant for this test
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertEquals(1, result);
    }

    @Test
    public void testReturnValueForPrimitive() {
        Object result = returnsEmptyValues.returnValueFor(int.class);
        
        assertEquals(0, result);
    }

    @Test
    public void testReturnValueForPrimitiveWrapper() {
        Object result = returnsEmptyValues.returnValueFor(Integer.class);
        
        assertEquals(0, result);
    }

    @Test
    public void testReturnValueForCollection() {
        Object result = returnsEmptyValues.returnValueFor(Collection.class);
        
        assertEquals(new LinkedList<Object>(), result);
    }

    @Test
    public void testReturnValueForSet() {
        Object result = returnsEmptyValues.returnValueFor(Set.class);
        
        assertEquals(new HashSet<Object>(), result);
    }

    @Test
    public void testReturnValueForHashSet() {
        Object result = returnsEmptyValues.returnValueFor(HashSet.class);
        
        assertEquals(new HashSet<Object>(), result);
    }

    @Test
    public void testReturnValueForSortedSet() {
        Object result = returnsEmptyValues.returnValueFor(SortedSet.class);
        
        assertEquals(new TreeSet<Object>(), result);
    }

    @Test
    public void testReturnValueForTreeSet() {
        Object result = returnsEmptyValues.returnValueFor(TreeSet.class);
        
        assertEquals(new TreeSet<Object>(), result);
    }

    @Test
    public void testReturnValueForLinkedHashSet() {
        Object result = returnsEmptyValues.returnValueFor(LinkedHashSet.class);
        
        assertEquals(new LinkedHashSet<Object>(), result);
    }

    @Test
    public void testReturnValueForList() {
        Object result = returnsEmptyValues.returnValueFor(List.class);
        
        assertEquals(new LinkedList<Object>(), result);
    }

    @Test
    public void testReturnValueForLinkedList() {
        Object result = returnsEmptyValues.returnValueFor(LinkedList.class);
        
        assertEquals(new LinkedList<Object>(), result);
    }

    @Test
    public void testReturnValueForArrayList() {
        Object result = returnsEmptyValues.returnValueFor(ArrayList.class);
        
        assertEquals(new ArrayList<Object>(), result);
    }

    @Test
    public void testReturnValueForMap() {
        Object result = returnsEmptyValues.returnValueFor(Map.class);
        
        assertEquals(new HashMap<Object, Object>(), result);
    }

    @Test
    public void testReturnValueForHashMap() {
        Object result = returnsEmptyValues.returnValueFor(HashMap.class);
        
        assertEquals(new HashMap<Object, Object>(), result);
    }

    @Test
    public void testReturnValueForSortedMap() {
        Object result = returnsEmptyValues.returnValueFor(SortedMap.class);
        
        assertEquals(new TreeMap<Object, Object>(), result);
    }

    @Test
    public void testReturnValueForTreeMap() {
        Object result = returnsEmptyValues.returnValueFor(TreeMap.class);
        
        assertEquals(new TreeMap<Object, Object>(), result);
    }

    @Test
    public void testReturnValueForLinkedHashMap() {
        Object result = returnsEmptyValues.returnValueFor(LinkedHashMap.class);
        
        assertEquals(new LinkedHashMap<Object, Object>(), result);
    }

    // TODO - add tests for empty Iterable

}