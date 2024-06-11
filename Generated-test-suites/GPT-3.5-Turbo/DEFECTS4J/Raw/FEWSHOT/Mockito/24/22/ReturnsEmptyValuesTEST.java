
package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ReturnsEmptyValuesTest {
    @Test
    public void testAnswer() {
        ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
        
        // Test toString() method
        assertEquals("Mock for SomeClass, hashCode: 123", returnsEmptyValues.answer(invocationWithMethodName("toString")));
        
        // Test compareTo() method
        assertEquals(1, returnsEmptyValues.answer(invocationWithMethodName("compareTo")));
        
        // Test primitive and wrapper class return types
        assertEquals(0, returnsEmptyValues.answer(invocationWithReturnType(int.class)));
        assertEquals(0, returnsEmptyValues.answer(invocationWithReturnType(Integer.class)));
        
        // Test Collection return type
        assertEquals(new LinkedList<Object>(), returnsEmptyValues.answer(invocationWithReturnType(Collection.class)));
        
        // Test Set return types
        assertEquals(new HashSet<Object>(), returnsEmptyValues.answer(invocationWithReturnType(Set.class)));
        assertEquals(new HashSet<Object>(), returnsEmptyValues.answer(invocationWithReturnType(HashSet.class)));
        assertEquals(new TreeSet<Object>(), returnsEmptyValues.answer(invocationWithReturnType(SortedSet.class)));
        assertEquals(new TreeSet<Object>(), returnsEmptyValues.answer(invocationWithReturnType(TreeSet.class)));
        assertEquals(new LinkedHashSet<Object>(), returnsEmptyValues.answer(invocationWithReturnType(LinkedHashSet.class)));
        
        // Test List return types
        assertEquals(new LinkedList<Object>(), returnsEmptyValues.answer(invocationWithReturnType(List.class)));
        assertEquals(new LinkedList<Object>(), returnsEmptyValues.answer(invocationWithReturnType(LinkedList.class)));
        assertEquals(new ArrayList<Object>(), returnsEmptyValues.answer(invocationWithReturnType(ArrayList.class)));
        
        // Test Map return types
        assertEquals(new HashMap<Object, Object>(), returnsEmptyValues.answer(invocationWithReturnType(Map.class)));
        assertEquals(new HashMap<Object, Object>(), returnsEmptyValues.answer(invocationWithReturnType(HashMap.class)));
        assertEquals(new TreeMap<Object, Object>(), returnsEmptyValues.answer(invocationWithReturnType(SortedMap.class)));
        assertEquals(new TreeMap<Object, Object>(), returnsEmptyValues.answer(invocationWithReturnType(TreeMap.class)));
        assertEquals(new LinkedHashMap<Object, Object>(), returnsEmptyValues.answer(invocationWithReturnType(LinkedHashMap.class)));
        
        // Test remaining return types
        assertNull(returnsEmptyValues.answer(invocationWithReturnType(Object.class)));
    }
    
    private InvocationOnMock invocationWithMethodName(String methodName) {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getName()).thenReturn(methodName);
        return invocation;
    }
    
    private InvocationOnMock invocationWithReturnType(Class<?> returnType) {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod().getReturnType()).thenReturn(returnType);
        return invocation;
    }
}
