package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.Serializable;
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

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;
import org.mockito.stubbing.Answer;

public class ReturnsEmptyValuesTest {
    private ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
    
    @Test
    public void testAnswer_toString() throws Exception {
        // Setup
        InvocationOnMock invocation = new InvocationOnMockImpl("toString");
        
        // Run
        Object result = returnsEmptyValues.answer(invocation);
        
        // Verify
        assertNotNull(result);
        assertEquals(String.class, result.getClass());
    }
    
    @Test
    public void testAnswer_compareTo() throws Exception {
        // Setup
        InvocationOnMock invocation = new InvocationOnMockImpl("compareTo");
        
        // Run
        Object result = returnsEmptyValues.answer(invocation);
        
        // Verify
        assertNotNull(result);
        assertEquals(Integer.class, result.getClass());
    }
    
    @Test
    public void testAnswer_primitiveType() throws Exception {
        // Setup
        InvocationOnMock invocation = new InvocationOnMockImpl("primitiveType");
        
        // Run
        Object result = returnsEmptyValues.answer(invocation);
        
        // Verify
        assertNotNull(result);
        assertEquals(returnsEmptyValues.returnValueFor(int.class), result);
    }
    
    @Test
    public void testAnswer_primitiveWrapperType() throws Exception {
        // Setup
        InvocationOnMock invocation = new InvocationOnMockImpl("primitiveWrapperType");
        
        // Run
        Object result = returnsEmptyValues.answer(invocation);
        
        // Verify
        assertNotNull(result);
        assertEquals(returnsEmptyValues.returnValueFor(Integer.class), result);
    }
    
    @Test
    public void testAnswer_collectionType() throws Exception {
        // Setup
        InvocationOnMock invocation = new InvocationOnMockImpl("collectionType");
        
        // Run
        Object result = returnsEmptyValues.answer(invocation);
        
        // Verify
        assertNotNull(result);
        assertEquals(LinkedList.class, result.getClass());
    }
    
    @Test
    public void testAnswer_setType() throws Exception {
        // Setup
        InvocationOnMock invocation = new InvocationOnMockImpl("setType");
        
        // Run
        Object result = returnsEmptyValues.answer(invocation);
        
        // Verify
        assertNotNull(result);
        assertEquals(HashSet.class, result.getClass());
    }
    
    @Test
    public void testAnswer_hashSetType() throws Exception {
        // Setup
        InvocationOnMock invocation = new InvocationOnMockImpl("hashSetType");
        
        // Run
        Object result = returnsEmptyValues.answer(invocation);
        
        // Verify
        assertNotNull(result);
        assertEquals(HashSet.class, result.getClass());
    }
    
    @Test
    public void testAnswer_sortedSetType() throws Exception {
        // Setup
        InvocationOnMock invocation = new InvocationOnMockImpl("sortedSetType");
        
        // Run
        Object result = returnsEmptyValues.answer(invocation);
        
        // Verify
        assertNotNull(result);
        assertEquals(TreeSet.class, result.getClass());
    }
    
    @Test
    public void testAnswer_treeSetType() throws Exception {
        // Setup
        InvocationOnMock invocation = new InvocationOnMockImpl("treeSetType");
        
        // Run
        Object result = returnsEmptyValues.answer(invocation);
        
        // Verify
        assertNotNull(result);
        assertEquals(TreeSet.class, result.getClass());
    }
    
    @Test
    public void testAnswer_linkedHashSetType() throws Exception {
        // Setup
        InvocationOnMock invocation = new InvocationOnMockImpl("linkedHashSetType");
        
        // Run
        Object result = returnsEmptyValues.answer(invocation);
        
        // Verify
        assertNotNull(result);
        assertEquals(LinkedHashSet.class, result.getClass());
    }
    
    @Test
    public void testAnswer_listType() throws Exception {
        // Setup
        InvocationOnMock invocation = new InvocationOnMockImpl("listType");
        
        // Run
        Object result = returnsEmptyValues.answer(invocation);
        
        // Verify
        assertNotNull(result);
        assertEquals(LinkedList.class, result.getClass());
    }
    
    @Test
    public void testAnswer_linkedListType() throws Exception {
        // Setup
        InvocationOnMock invocation = new InvocationOnMockImpl("linkedListType");
        
        // Run
        Object result = returnsEmptyValues.answer(invocation);
        
        // Verify
        assertNotNull(result);
        assertEquals(LinkedList.class, result.getClass());
    }
    
    @Test
    public void testAnswer_arrayListType() throws Exception {
        // Setup
        InvocationOnMock invocation = new InvocationOnMockImpl("arrayListType");
        
        // Run
        Object result = returnsEmptyValues.answer(invocation);
        
        // Verify
        assertNotNull(result);
        assertEquals(ArrayList.class, result.getClass());
    }
    
    @Test
    public void testAnswer_mapType() throws Exception {
        // Setup
        InvocationOnMock invocation = new InvocationOnMockImpl("mapType");
        
        // Run
        Object result = returnsEmptyValues.answer(invocation);
        
        // Verify
        assertNotNull(result);
        assertEquals(HashMap.class, result.getClass());
    }
    
    @Test
    public void testAnswer_hashMapType() throws Exception {
        // Setup
        InvocationOnMock invocation = new InvocationOnMockImpl("hashMapType");
        
        // Run
        Object result = returnsEmptyValues.answer(invocation);
        
        // Verify
        assertNotNull(result);
        assertEquals(HashMap.class, result.getClass());
    }
    
    @Test
    public void testAnswer_sortedMapType() throws Exception {
        // Setup
        InvocationOnMock invocation = new InvocationOnMockImpl("sortedMapType");
        
        // Run
        Object result = returnsEmptyValues.answer(invocation);
        
        // Verify
        assertNotNull(result);
        assertEquals(TreeMap.class, result.getClass());
    }
    
    @Test
    public void testAnswer_treeMapType() throws Exception {
        // Setup
        InvocationOnMock invocation = new InvocationOnMockImpl("treeMapType");
        
        // Run
        Object result = returnsEmptyValues.answer(invocation);
        
        // Verify
        assertNotNull(result);
        assertEquals(TreeMap.class, result.getClass());
    }
    
    @Test
    public void testAnswer_linkedHashMapType() throws Exception {
        // Setup
        InvocationOnMock invocation = new InvocationOnMockImpl("linkedHashMapType");
        
        // Run
        Object result = returnsEmptyValues.answer(invocation);
        
        // Verify
        assertNotNull(result);
        assertEquals(LinkedHashMap.class, result.getClass());
    }
    
    private class InvocationOnMockImpl implements InvocationOnMock {
        private String methodName;
        
        public InvocationOnMockImpl(String methodName) {
            this.methodName = methodName;
        }

        @Override
        public Object getMock() {
            return null;
        }

        @Override
        public Object[] getArguments() {
            return null;
        }

        @Override
        public void markVerified() {
        }

        @Override
        public boolean isVerified() {
            return false;
        }

        @Override
        public String toString() {
            return methodName;
        }
    }
}