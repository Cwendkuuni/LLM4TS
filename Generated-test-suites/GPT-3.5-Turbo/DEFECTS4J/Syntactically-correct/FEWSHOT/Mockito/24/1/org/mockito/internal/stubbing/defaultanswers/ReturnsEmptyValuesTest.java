import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ReturnsEmptyValuesTest {
    @Test
    public void testAnswer() {
        ReturnsEmptyValues answer = new ReturnsEmptyValues();
        
        // Test toString() method
        assertEquals("Mock for Object, hashCode: 0", answer.answer(invocationMock("toString", null, Object.class)));
        
        // Test compareTo() method
        assertEquals(1, answer.answer(invocationMock("compareTo", null, Comparable.class)));
        
        // Test primitive-returning methods
        assertEquals(0, answer.answer(invocationMock("primitiveMethod", null, int.class)));
        assertEquals(0, answer.answer(invocationMock("primitiveWrapperMethod", null, Integer.class)));
        
        // Test collection-returning methods
        assertEquals(new LinkedList<Object>(), answer.answer(invocationMock("collectionMethod", null, Collection.class)));
        assertEquals(new HashSet<Object>(), answer.answer(invocationMock("setMethod", null, Set.class)));
        assertEquals(new HashSet<Object>(), answer.answer(invocationMock("hashSetMethod", null, HashSet.class)));
        assertEquals(new TreeSet<Object>(), answer.answer(invocationMock("sortedSetMethod", null, SortedSet.class)));
        assertEquals(new TreeSet<Object>(), answer.answer(invocationMock("treeSetMethod", null, TreeSet.class)));
        assertEquals(new LinkedHashSet<Object>(), answer.answer(invocationMock("linkedHashSetMethod", null, LinkedHashSet.class)));
        assertEquals(new LinkedList<Object>(), answer.answer(invocationMock("listMethod", null, List.class)));
        assertEquals(new LinkedList<Object>(), answer.answer(invocationMock("linkedListMethod", null, LinkedList.class)));
        assertEquals(new ArrayList<Object>(), answer.answer(invocationMock("arrayListMethod", null, ArrayList.class)));
        assertEquals(new HashMap<Object, Object>(), answer.answer(invocationMock("mapMethod", null, Map.class)));
        assertEquals(new HashMap<Object, Object>(), answer.answer(invocationMock("hashMapMethod", null, HashMap.class)));
        assertEquals(new TreeMap<Object, Object>(), answer.answer(invocationMock("sortedMapMethod", null, SortedMap.class)));
        assertEquals(new TreeMap<Object, Object>(), answer.answer(invocationMock("treeMapMethod", null, TreeMap.class)));
        assertEquals(new LinkedHashMap<Object, Object>(), answer.answer(invocationMock("linkedHashMapMethod", null, LinkedHashMap.class)));
        
        // Test other non-primitive and non-collection returning methods
        assertNull(answer.answer(invocationMock("nonPrimitiveMethod", null, NonPrimitiveClass.class)));
    }
    
    private InvocationOnMock invocationMock(String methodName, Object[] args, Class<?> returnType) {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(getTestMethod(methodName, args, returnType));
        when(invocation.getMock()).thenReturn(mock(returnType));
        return invocation;
    }
    
    private Method getTestMethod(String methodName, Object[] args, Class<?> returnType) {
        try {
            return ReturnsEmptyValues.class.getDeclaredMethod(methodName, args);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
}