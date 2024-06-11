import static org.junit.Assert.*;
import org.junit.Test;

public class ReturnsEmptyValuesTest {

    @Test
    public void testAnswer_toString() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        String result = returnsEmptyValues.answer(createInvocation("toString"));
        assertNotNull(result);
        assertTrue(result.contains("Mock for "));
        assertTrue(result.contains(", hashCode: "));
    }

    @Test
    public void testAnswer_compareTo() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        int result = (int) returnsEmptyValues.answer(createInvocation("compareTo"));
        assertEquals(1, result);
    }

    @Test
    public void testReturnValueFor_primitive() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object result = returnsEmptyValues.returnValueFor(int.class);
        assertNotNull(result);
        assertTrue(result instanceof Integer);
        assertEquals(0, result);
    }

    @Test
    public void testReturnValueFor_primitiveWrapper() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object result = returnsEmptyValues.returnValueFor(Integer.class);
        assertNotNull(result);
        assertTrue(result instanceof Integer);
        assertEquals(0, result);
    }

    @Test
    public void testReturnValueFor_Collection() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object result = returnsEmptyValues.returnValueFor(Collection.class);
        assertNotNull(result);
        assertTrue(result instanceof LinkedList);
        assertTrue(((Collection) result).isEmpty());
    }

    @Test
    public void testReturnValueFor_Set() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object result = returnsEmptyValues.returnValueFor(Set.class);
        assertNotNull(result);
        assertTrue(result instanceof HashSet);
        assertTrue(((Set) result).isEmpty());
    }

    @Test
    public void testReturnValueFor_HashSet() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object result = returnsEmptyValues.returnValueFor(HashSet.class);
        assertNotNull(result);
        assertTrue(result instanceof HashSet);
        assertTrue(((Set) result).isEmpty());
    }

    @Test
    public void testReturnValueFor_SortedSet() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object result = returnsEmptyValues.returnValueFor(SortedSet.class);
        assertNotNull(result);
        assertTrue(result instanceof TreeSet);
        assertTrue(((Set) result).isEmpty());
    }

    @Test
    public void testReturnValueFor_TreeSet() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object result = returnsEmptyValues.returnValueFor(TreeSet.class);
        assertNotNull(result);
        assertTrue(result instanceof TreeSet);
        assertTrue(((Set) result).isEmpty());
    }

    @Test
    public void testReturnValueFor_LinkedHashSet() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object result = returnsEmptyValues.returnValueFor(LinkedHashSet.class);
        assertNotNull(result);
        assertTrue(result instanceof LinkedHashSet);
        assertTrue(((Set) result).isEmpty());
    }

    @Test
    public void testReturnValueFor_List() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object result = returnsEmptyValues.returnValueFor(List.class);
        assertNotNull(result);
        assertTrue(result instanceof LinkedList);
        assertTrue(((List) result).isEmpty());
    }

    @Test
    public void testReturnValueFor_LinkedList() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object result = returnsEmptyValues.returnValueFor(LinkedList.class);
        assertNotNull(result);
        assertTrue(result instanceof LinkedList);
        assertTrue(((List) result).isEmpty());
    }

    @Test
    public void testReturnValueFor_ArrayList() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object result = returnsEmptyValues.returnValueFor(ArrayList.class);
        assertNotNull(result);
        assertTrue(result instanceof ArrayList);
        assertTrue(((List) result).isEmpty());
    }

    @Test
    public void testReturnValueFor_Map() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object result = returnsEmptyValues.returnValueFor(Map.class);
        assertNotNull(result);
        assertTrue(result instanceof HashMap);
        assertTrue(((Map) result).isEmpty());
    }

    @Test
    public void testReturnValueFor_HashMap() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object result = returnsEmptyValues.returnValueFor(HashMap.class);
        assertNotNull(result);
        assertTrue(result instanceof HashMap);
        assertTrue(((Map) result).isEmpty());
    }

    @Test
    public void testReturnValueFor_SortedMap() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object result = returnsEmptyValues.returnValueFor(SortedMap.class);
        assertNotNull(result);
        assertTrue(result instanceof TreeMap);
        assertTrue(((Map) result).isEmpty());
    }

    @Test
    public void testReturnValueFor_TreeMap() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object result = returnsEmptyValues.returnValueFor(TreeMap.class);
        assertNotNull(result);
        assertTrue(result instanceof TreeMap);
        assertTrue(((Map) result).isEmpty());
    }

    @Test
    public void testReturnValueFor_LinkedHashMap() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        Object result = returnsEmptyValues.returnValueFor(LinkedHashMap.class);
        assertNotNull(result);
        assertTrue(result instanceof LinkedHashMap);
        assertTrue(((Map) result).isEmpty());
    }

    private InvocationOnMock createInvocation(String methodName) {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(ReturnsEmptyValues.class.getMethod(methodName));
        Object mock = mock(Object.class);
        when(invocation.getMock()).thenReturn(mock);
        when(invocation.getArguments()).thenReturn(new Object[] { mock });
        return invocation;
    }

}