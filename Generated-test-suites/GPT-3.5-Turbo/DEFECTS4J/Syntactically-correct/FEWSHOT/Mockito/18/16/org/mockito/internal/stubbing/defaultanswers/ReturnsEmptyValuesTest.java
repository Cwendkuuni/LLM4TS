
package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
import org.mockito.internal.util.MockUtil;
import org.mockito.internal.util.ObjectMethodsGuru;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;
import org.mockito.stubbing.Answer;

public class ReturnsEmptyValuesTest {
    
    private ReturnsEmptyValues returnsEmptyValues = new ReturnsEmptyValues();
    private ObjectMethodsGuru methodsGuru = new ObjectMethodsGuru();
    private MockUtil mockUtil = new MockUtil();

    @Test
    public void testAnswer() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(methodsGuru.toStringMethod());
        when(invocation.getMock()).thenReturn(new Object());
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertEquals("Mock for Object, hashCode: " + invocation.getMock().hashCode(), result);
    }
    
    @Test
    public void testAnswerCompareTo() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(methodsGuru.compareToMethod());
        when(invocation.getMock()).thenReturn(new Object());
        when(invocation.getArguments()).thenReturn(new Object[] { new Object() });
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertEquals(1, result);
    }
    
    @Test
    public void testAnswerPrimitiveReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(getMethodWithPrimitiveReturnType());
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertEquals(getExpectedPrimitiveDefaultValue(), result);
    }
    
    @Test
    public void testAnswerWrapperReturnType() {
        InvocationOnMock invocation = mock(InvocationOnMock.class);
        when(invocation.getMethod()).thenReturn(getMethodWithWrapperReturnType());
        
        Object result = returnsEmptyValues.answer(invocation);
        
        assertEquals(getExpectedWrapperDefaultValue(), result);
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
    
    @Test
    public void testReturnValueForOtherType() {
        Object result = returnsEmptyValues.returnValueFor(Object.class);
        
        assertNull(result);
    }
    
    private java.lang.reflect.Method getMethodWithPrimitiveReturnType() {
        try {
            return ReturnsEmptyValues.class.getDeclaredMethod("returnValueFor", Class.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private Object getExpectedPrimitiveDefaultValue() {
        return Primitives.defaultValueForPrimitiveOrWrapper(getMethodWithPrimitiveReturnType().getReturnType());
    }
    
    private java.lang.reflect.Method getMethodWithWrapperReturnType() {
        try {
            return ReturnsEmptyValues.class.getDeclaredMethod("returnValueFor", Class.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    private Object getExpectedWrapperDefaultValue() {
        return Primitives.defaultValueForPrimitiveOrWrapper(getMethodWithWrapperReturnType().getReturnType());
    }
}
