
package org.mockito.internal.stubbing.defaultanswers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.mock.MockName;

public class ReturnsEmptyValuesTest {
    
    private ReturnsEmptyValues emptyValues = new ReturnsEmptyValues();
    
    @Test
    public void testAnswer_ToString() {
        ObjectMethodsGuru methodsGuru = new ObjectMethodsGuru();
        MockUtil mockUtil = new MockUtil();
        
        InvocationOnMock invocation = mockUtil.createInvocation(Object.class, methodsGuru.getDeclaredMethods(Object.class).get(0), this, new Object[] {});
        assertEquals("Mock for Object, hashCode: " + this.hashCode(), emptyValues.answer(invocation));
    
        MockName name = new MockName();
        name.setDefault();
        invocation = mockUtil.createInvocation(Object.class, methodsGuru.getDeclaredMethods(Object.class).get(0), this, new Object[] {});
        assertEquals("Mock for Object, hashCode: " + this.hashCode(), emptyValues.answer(invocation));
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void testAnswer_CompareTo() {
        InvocationOnMock invocation = mockUtil.createInvocation(Comparable.class, methodsGuru.getDeclaredMethods(Comparable.class).get(0), this, new Object[] { null });
        assertEquals(1, emptyValues.answer(invocation));
        
        invocation = mockUtil.createInvocation(Comparable.class, methodsGuru.getDeclaredMethods(Comparable.class).get(0), this, new Object[] { this });
        assertEquals(0, emptyValues.answer(invocation));
    }
    
    @Test
    public void testAnswer_PrimitiveOrWrapper() {
        InvocationOnMock invocation = mockUtil.createInvocation(Object.class, methodsGuru.getDeclaredMethods(Object.class).get(0), this, new Object[] {});
        assertNull(emptyValues.answer(invocation));
    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void testAnswer_Collection() {
        InvocationOnMock invocation = mockUtil.createInvocation(Collection.class, methodsGuru.getDeclaredMethods(Collection.class).get(0), this, new Object[] {});
        assertEquals(new LinkedList<Object>(), emptyValues.answer(invocation));
    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void testAnswer_Set() {
        InvocationOnMock invocation = mockUtil.createInvocation(Set.class, methodsGuru.getDeclaredMethods(Set.class).get(0), this, new Object[] {});
        assertEquals(new HashSet<Object>(), emptyValues.answer(invocation));
    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void testAnswer_HashSet() {
        InvocationOnMock invocation = mockUtil.createInvocation(HashSet.class, methodsGuru.getDeclaredMethods(HashSet.class).get(0), this, new Object[] {});
        assertEquals(new HashSet<Object>(), emptyValues.answer(invocation));
    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void testAnswer_SortedSet() {
        InvocationOnMock invocation = mockUtil.createInvocation(SortedSet.class, methodsGuru.getDeclaredMethods(SortedSet.class).get(0), this, new Object[] {});
        assertEquals(new TreeSet<Object>(), emptyValues.answer(invocation));
    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void testAnswer_TreeSet() {
        InvocationOnMock invocation = mockUtil.createInvocation(TreeSet.class, methodsGuru.getDeclaredMethods(TreeSet.class).get(0), this, new Object[] {});
        assertEquals(new TreeSet<Object>(), emptyValues.answer(invocation));
    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void testAnswer_LinkedHashSet() {
        InvocationOnMock invocation = mockUtil.createInvocation(LinkedHashSet.class, methodsGuru.getDeclaredMethods(LinkedHashSet.class).get(0), this, new Object[] {});
        assertEquals(new LinkedHashSet<Object>(), emptyValues.answer(invocation));
    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void testAnswer_List() {
        InvocationOnMock invocation = mockUtil.createInvocation(List.class, methodsGuru.getDeclaredMethods(List.class).get(0), this, new Object[] {});
        assertEquals(new LinkedList<Object>(), emptyValues.answer(invocation));
    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void testAnswer_LinkedList() {
        InvocationOnMock invocation = mockUtil.createInvocation(LinkedList.class, methodsGuru.getDeclaredMethods(LinkedList.class).get(0), this, new Object[] {});
        assertEquals(new LinkedList<Object>(), emptyValues.answer(invocation));
    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void testAnswer_ArrayList() {
        InvocationOnMock invocation = mockUtil.createInvocation(ArrayList.class, methodsGuru.getDeclaredMethods(ArrayList.class).get(0), this, new Object[] {});
        assertEquals(new ArrayList<Object>(), emptyValues.answer(invocation));
    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void testAnswer_Map() {
        InvocationOnMock invocation = mockUtil.createInvocation(Map.class, methodsGuru.getDeclaredMethods(Map.class).get(0), this, new Object[] {});
        assertEquals(new HashMap<Object, Object>(), emptyValues.answer(invocation));
    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void testAnswer_HashMap() {
        InvocationOnMock invocation = mockUtil.createInvocation(HashMap.class, methodsGuru.getDeclaredMethods(HashMap.class).get(0), this, new Object[] {});
        assertEquals(new HashMap<Object, Object>(), emptyValues.answer(invocation));
    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void testAnswer_SortedMap() {
        InvocationOnMock invocation = mockUtil.createInvocation(SortedMap.class, methodsGuru.getDeclaredMethods(SortedMap.class).get(0), this, new Object[] {});
        assertEquals(new TreeMap<Object, Object>(), emptyValues.answer(invocation));
    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void testAnswer_TreeMap() {
        InvocationOnMock invocation = mockUtil.createInvocation(TreeMap.class, methodsGuru.getDeclaredMethods(TreeMap.class).get(0), this, new Object[] {});
        assertEquals(new TreeMap<Object, Object>(), emptyValues.answer(invocation));
    }
    
    @SuppressWarnings("rawtypes")
    @Test
    public void testAnswer_LinkedHashMap() {
        InvocationOnMock invocation = mockUtil.createInvocation(LinkedHashMap.class, methodsGuru.getDeclaredMethods(LinkedHashMap.class).get(0), this, new Object[] {});
        assertEquals(new LinkedHashMap<Object, Object>(), emptyValues.answer(invocation));
    }
    
    @Test
    public void testAnswer_Others() {
        InvocationOnMock invocation = mockUtil.createInvocation(String.class, methodsGuru.getDeclaredMethods(String.class).get(0), this, new Object[] {});
        assertNull(emptyValues.answer(invocation));
    }
}
