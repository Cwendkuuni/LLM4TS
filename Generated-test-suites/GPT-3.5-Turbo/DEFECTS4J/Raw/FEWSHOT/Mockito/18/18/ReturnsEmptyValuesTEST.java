package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ReturnsEmptyValuesTest {
    @Test
    public void testAnswer() {
        ReturnsEmptyValues rev = new ReturnsEmptyValues();
        
        // Test for toString() method
        assertEquals("Mock for Object, hashCode: 0", rev.answer(invocationToString()));
        
        // Test for compareTo() method
        assertEquals(0, rev.answer(invocationCompareTo()));
        assertEquals(1, rev.answer(invocationCompareToNotEqual()));
        
        // Test for primitive and wrapper classes return types
        assertEquals(false, rev.answer(invocationBoolean()));
        assertEquals(' ', rev.answer(invocationChar()));
        assertEquals((byte) 0, rev.answer(invocationByte()));
        assertEquals((short) 0, rev.answer(invocationShort()));
        assertEquals(0, rev.answer(invocationInt()));
        assertEquals(0L, rev.answer(invocationLong()));
        assertEquals(0.0f, rev.answer(invocationFloat()), 0.001);
        assertEquals(0.0, rev.answer(invocationDouble()), 0.001);
        
        // Test for collection return types
        assertEquals(new LinkedList<Object>(), rev.answer(invocationCollection()));
        assertEquals(new HashSet<Object>(), rev.answer(invocationSet()));
        assertEquals(new HashSet<Object>(), rev.answer(invocationHashSet()));
        assertEquals(new TreeSet<Object>(), rev.answer(invocationSortedSet()));
        assertEquals(new TreeSet<Object>(), rev.answer(invocationTreeSet()));
        assertEquals(new LinkedHashSet<Object>(), rev.answer(invocationLinkedHashSet()));
        assertEquals(new LinkedList<Object>(), rev.answer(invocationList()));
        assertEquals(new LinkedList<Object>(), rev.answer(invocationLinkedList()));
        assertEquals(new ArrayList<Object>(), rev.answer(invocationArrayList()));
        assertEquals(new HashMap<Object, Object>(), rev.answer(invocationMap()));
        assertEquals(new HashMap<Object, Object>(), rev.answer(invocationHashMap()));
        assertEquals(new TreeMap<Object, Object>(), rev.answer(invocationSortedMap()));
        assertEquals(new TreeMap<Object, Object>(), rev.answer(invocationTreeMap()));
        assertEquals(new LinkedHashMap<Object, Object>(), rev.answer(invocationLinkedHashMap()));
        
    }
    
    private InvocationOnMock invocationToString() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Object.class.getMethod("toString");
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Object mock = Mockito.mock(Object.class);
        invocation.getMock();
        
        return invocation;
    }
    
    private InvocationOnMock invocationCompareTo() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Comparable.class.getMethod("compareTo", Object.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Object mock = Mockito.mock(Object.class);
        Object argument = mock;
        Mockito.when(invocation.getArguments()).thenReturn(new Object[] { argument });
        
        return invocation;
    }
    
    private InvocationOnMock invocationCompareToNotEqual() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Comparable.class.getMethod("compareTo", Object.class);
        Mockito.when(invocation.getMethod()).thenReturn(method);
        Object mock = Mockito.mock(Object.class);
        Object argument = new Object();
        Mockito.when(invocation.getArguments()).thenReturn(new Object[] { argument });
        
        return invocation;
    }
    
    private InvocationOnMock invocationBoolean() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Object.class.getMethod("booleanValue");
        Mockito.when(invocation.getMethod()).thenReturn(method);
        
        return invocation;
    }
    
    private InvocationOnMock invocationChar() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Object.class.getMethod("charValue");
        Mockito.when(invocation.getMethod()).thenReturn(method);
        
        return invocation;
    }
    
    private InvocationOnMock invocationByte() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Object.class.getMethod("byteValue");
        Mockito.when(invocation.getMethod()).thenReturn(method);
        
        return invocation;
    }
    
    private InvocationOnMock invocationShort() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Object.class.getMethod("shortValue");
        Mockito.when(invocation.getMethod()).thenReturn(method);
        
        return invocation;
    }
    
    private InvocationOnMock invocationInt() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Object.class.getMethod("intValue");
        Mockito.when(invocation.getMethod()).thenReturn(method);
        
        return invocation;
    }
    
    private InvocationOnMock invocationLong() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Object.class.getMethod("longValue");
        Mockito.when(invocation.getMethod()).thenReturn(method);
        
        return invocation;
    }
    
    private InvocationOnMock invocationFloat() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Object.class.getMethod("floatValue");
        Mockito.when(invocation.getMethod()).thenReturn(method);
        
        return invocation;
    }
    
    private InvocationOnMock invocationDouble() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Object.class.getMethod("doubleValue");
        Mockito.when(invocation.getMethod()).thenReturn(method);
        
        return invocation;
    }
    
    private InvocationOnMock invocationCollection() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Object.class.getMethod("collectionValue");
        Mockito.when(invocation.getMethod()).thenReturn(method);
        
        return invocation;
    }
    
    private InvocationOnMock invocationSet() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Object.class.getMethod("setValue");
        Mockito.when(invocation.getMethod()).thenReturn(method);
        
        return invocation;
    }
    
    private InvocationOnMock invocationHashSet() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Object.class.getMethod("hashSetValue");
        Mockito.when(invocation.getMethod()).thenReturn(method);
        
        return invocation;
    }
    
    private InvocationOnMock invocationSortedSet() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Object.class.getMethod("sortedSetValue");
        Mockito.when(invocation.getMethod()).thenReturn(method);
        
        return invocation;
    }
    
    private InvocationOnMock invocationTreeSet() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Object.class.getMethod("treeSetValue");
        Mockito.when(invocation.getMethod()).thenReturn(method);
        
        return invocation;
    }
    
    private InvocationOnMock invocationLinkedHashSet() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Object.class.getMethod("linkedHashSetValue");
        Mockito.when(invocation.getMethod()).thenReturn(method);
        
        return invocation;
    }
    
    private InvocationOnMock invocationList() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Object.class.getMethod("listValue");
        Mockito.when(invocation.getMethod()).thenReturn(method);
        
        return invocation;
    }
    
    private InvocationOnMock invocationLinkedList() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Object.class.getMethod("linkedListValue");
        Mockito.when(invocation.getMethod()).thenReturn(method);
        
        return invocation;
    }
    
    private InvocationOnMock invocationArrayList() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Object.class.getMethod("arrayListValue");
        Mockito.when(invocation.getMethod()).thenReturn(method);
        
        return invocation;
    }
    
    private InvocationOnMock invocationMap() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Object.class.getMethod("mapValue");
        Mockito.when(invocation.getMethod()).thenReturn(method);
        
        return invocation;
    }
    
    private InvocationOnMock invocationHashMap() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Object.class.getMethod("hashMapValue");
        Mockito.when(invocation.getMethod()).thenReturn(method);
        
        return invocation;
    }
    
    private InvocationOnMock invocationSortedMap() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Object.class.getMethod("sortedMapValue");
        Mockito.when(invocation.getMethod()).thenReturn(method);
        
        return invocation;
    }
    
    private InvocationOnMock invocationTreeMap() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Object.class.getMethod("treeMapValue");
        Mockito.when(invocation.getMethod()).thenReturn(method);
        
        return invocation;
    }
    
    private InvocationOnMock invocationLinkedHashMap() {
        InvocationOnMock invocation = Mockito.mock(InvocationOnMock.class);
        Method method = Object.class.getMethod("linkedHashMapValue");
        Mockito.when(invocation.getMethod()).thenReturn(method);
        
        return invocation;
    }
}