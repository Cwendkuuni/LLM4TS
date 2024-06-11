package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.mockito.invocation.InvocationOnMock;
import org.junit.Test;

public class ReturnsEmptyValuesTest {
   
    @Test
    public void testAnswer() {
        ReturnsEmptyValues rev = new ReturnsEmptyValues();
        InvocationOnMock invocation;
        
        // Test toString() method
        invocation = createInvocation("toString", String.class);
        assertEquals("Mock for Object, hashCode: 0", rev.answer(invocation));
        
        // Test compareTo() method
        invocation = createInvocation("compareTo", int.class, Object.class);
        assertEquals(1, rev.answer(invocation));
        
        // Test primitive-returning methods
        invocation = createInvocation("doSomething", int.class);
        assertEquals(0, rev.answer(invocation));
        
        // Test Integer-returning methods
        invocation = createInvocation("getSomeInteger", Integer.class);
        assertEquals(0, rev.answer(invocation));
        
        // Test Collection-returning methods
        invocation = createInvocation("getCollection", Collection.class);
        assertEquals(new LinkedList<Object>(), rev.answer(invocation));
        
        // Test Set-returning methods
        invocation = createInvocation("getSet", Set.class);
        assertEquals(new HashSet<Object>(), rev.answer(invocation));
        
        // Test HashSet-returning methods
        invocation = createInvocation("getHashSet", HashSet.class);
        assertEquals(new HashSet<Object>(), rev.answer(invocation));
        
        // Test SortedSet-returning methods
        invocation = createInvocation("getSortedSet", SortedSet.class);
        assertEquals(new TreeSet<Object>(), rev.answer(invocation));
        
        // Test TreeSet-returning methods
        invocation = createInvocation("getTreeSet", TreeSet.class);
        assertEquals(new TreeSet<Object>(), rev.answer(invocation));
        
        // Test LinkedHashSet-returning methods
        invocation = createInvocation("getLinkedHashSet", LinkedHashSet.class);
        assertEquals(new LinkedHashSet<Object>(), rev.answer(invocation));
        
        // Test List-returning methods
        invocation = createInvocation("getList", List.class);
        assertEquals(new LinkedList<Object>(), rev.answer(invocation));
        
        // Test LinkedList-returning methods
        invocation = createInvocation("getLinkedList", LinkedList.class);
        assertEquals(new LinkedList<Object>(), rev.answer(invocation));
        
        // Test ArrayList-returning methods
        invocation = createInvocation("getArrayList", ArrayList.class);
        assertEquals(new ArrayList<Object>(), rev.answer(invocation));
        
        // Test Map-returning methods
        invocation = createInvocation("getMap", Map.class);
        assertEquals(new HashMap<Object, Object>(), rev.answer(invocation));
        
        // Test HashMap-returning methods
        invocation = createInvocation("getHashMap", HashMap.class);
        assertEquals(new HashMap<Object, Object>(), rev.answer(invocation));
        
        // Test SortedMap-returning methods
        invocation = createInvocation("getSortedMap", SortedMap.class);
        assertEquals(new TreeMap<Object, Object>(), rev.answer(invocation));
        
        // Test TreeMap-returning methods
        invocation = createInvocation("getTreeMap", TreeMap.class);
        assertEquals(new TreeMap<Object, Object>(), rev.answer(invocation));
        
        // Test LinkedHashMap-returning methods
        invocation = createInvocation("getLinkedHashMap", LinkedHashMap.class);
        assertEquals(new LinkedHashMap<Object, Object>(), rev.answer(invocation));
    }
    
    private InvocationOnMock createInvocation(String methodName, Class<?> returnType, Class<?>... parameterTypes) {
        InvocationOnMock invocation = org.mockito.Mockito.mock(InvocationOnMock.class);
        org.mockito.Mockito.when(invocation.getMethod()).thenReturn(getMethod(methodName, returnType, parameterTypes));
        return invocation;
    }
    
    private java.lang.reflect.Method getMethod(String methodName, Class<?> returnType, Class<?>... parameterTypes) {
        try {
            return getClass().getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
    
    // Test methods for mocking
    public String toString() {
        return "toString";
    }
    
    public int compareTo(Object obj) {
        return 0;
    }
    
    public int doSomething() {
        return 0;
    }
    
    public Integer getSomeInteger() {
        return 0;
    }
    
    public Collection<Object> getCollection() {
        return null;
    }
    
    public Set<Object> getSet() {
        return null;
    }
    
    public HashSet<Object> getHashSet() {
        return null;
    }
    
    public SortedSet<Object> getSortedSet() {
        return null;
    }
    
    public TreeSet<Object> getTreeSet() {
        return null;
    }
    
    public LinkedHashSet<Object> getLinkedHashSet() {
        return null;
    }
    
    public List<Object> getList() {
        return null;
    }
    
    public LinkedList<Object> getLinkedList() {
        return null;
    }
    
    public ArrayList<Object> getArrayList() {
        return null;
    }
    
    public Map<Object, Object> getMap() {
        return null;
    }
    
    public HashMap<Object, Object> getHashMap() {
        return null;
    }
    
    public SortedMap<Object, Object> getSortedMap() {
        return null;
    }
    
    public TreeMap<Object, Object> getTreeMap() {
        return null;
    }
    
    public LinkedHashMap<Object, Object> getLinkedHashMap() {
        return null;
    }
}