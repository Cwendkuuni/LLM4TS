package org.apache.commons.collections.primitives;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ArrayByteListTest {
    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add(1);
        list.add(2);
        list.add(3);
        
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGet_IndexOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.get(0);
    }
    
    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        list.add(1);
        list.add(2);
        list.add(3);
        
        assertEquals(3, list.size());
    }
    
    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add(1);
        list.add(2);
        list.add(3);
        
        assertEquals(2, list.removeElementAt(1));
        assertEquals(1, list.get(0));
        assertEquals(3, list.get(1));
        assertEquals(2, list.size());
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAt_IndexOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.removeElementAt(0);
    }
    
    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add(1);
        list.add(2);
        list.add(3);
        
        assertEquals(2, list.set(1, 5));
        assertEquals(1, list.get(0));
        assertEquals(5, list.get(1));
        assertEquals(3, list.get(2));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSet_IndexOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.set(0, 1);
    }
    
    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list.add(1);
        list.add(2);
        list.add(3);
        
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
        assertEquals(3, list.size());
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAdd_IndexOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.add(-1, 1);
    }
    
    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();
        list.add(1);
        list.ensureCapacity(5);
        
        assertEquals(1, list.get(0));
    }
    
    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList();
        list.add(1);
        list.trimToSize();
        
        assertEquals(1, list.get(0));
        assertEquals(1, list.size());
    }
}