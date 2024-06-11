
package org.apache.commons.collections.primitives;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Before;
import org.junit.Test;

public class ArrayByteListTest {
    
    private ArrayByteList list;
    
    @Before
    public void setUp() {
        list = new ArrayByteList();
    }
    
    @Test
    public void testConstructor() {
        assertEquals(0, list.size());
    }
    
    @Test
    public void testConstructor_WithInitialCapacity() {
        assertEquals(0, list.size());
        
        list = new ArrayByteList(5);
        assertEquals(0, list.size());
    }
    
    @Test
    public void testConstructor_WithNegativeInitialCapacity() {
        assertThrows(IllegalArgumentException.class, () -> new ArrayByteList(-5));
    }
    
    @Test
    public void testConstructor_WithByteCollection() {
        ByteList byteList = new ArrayByteList();
        byteList.add((byte) 2);
        byteList.add((byte) 4);
        byteList.add((byte) 6);
        
        list = new ArrayByteList(byteList);
        assertEquals(3, list.size());
        assertEquals(2, list.get(0));
        assertEquals(4, list.get(1));
        assertEquals(6, list.get(2));
    }
    
    @Test
    public void testGet() {
        list.add((byte) 3);
        list.add((byte) 7);
        
        assertEquals(3, list.get(0));
        assertEquals(7, list.get(1));
    }
    
    @Test
    public void testGet_WithIndexOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.get(0));
    }
    
    @Test
    public void testSize() {
        assertEquals(0, list.size());
        
        list.add((byte) 2);
        assertEquals(1, list.size());
        
        list.add((byte) 4);
        assertEquals(2, list.size());
    }
    
    @Test
    public void testRemoveElementAt() {
        list.add((byte) 2);
        list.add((byte) 4);
        list.add((byte) 6);
        
        byte removedElement = list.removeElementAt(1);
        assertEquals(4, removedElement);
        assertEquals(2, list.size());
        assertEquals(2, list.get(0));
        assertEquals(6, list.get(1));
    }
    
    @Test
    public void testRemoveElementAt_WithIndexOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.removeElementAt(0));
    }
    
    @Test
    public void testSet() {
        list.add((byte) 2);
        list.add((byte) 4);
        
        byte replacedElement = list.set(1, (byte) 6);
        assertEquals(4, replacedElement);
        assertEquals(2, list.size());
        assertEquals(2, list.get(0));
        assertEquals(6, list.get(1));
    }
    
    @Test
    public void testSet_WithIndexOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.set(0, (byte) 1));
    }
    
    @Test
    public void testAdd() {
        list.add((byte) 2);
        list.add((byte) 6);
        
        assertEquals(2, list.size());
        assertEquals(2, list.get(0));
        assertEquals(6, list.get(1));
    }
    
    @Test
    public void testAdd_WithIndex() {
        list.add((byte) 2);
        list.add((byte) 6);
        
        list.add(1, (byte) 4);
        assertEquals(3, list.size());
        assertEquals(2, list.get(0));
        assertEquals(4, list.get(1));
        assertEquals(6, list.get(2));
    }
    
    @Test
    public void testAdd_WithInvalidIndex() {
        assertThrows(IndexOutOfBoundsException.class, () -> list.add(1, (byte) 3));
    }
    
    @Test
    public void testEnsureCapacity() {
        list = new ArrayByteList(2);
        
        list.add((byte) 2);
        list.ensureCapacity(5);
        
        assertEquals(2, list.size());
        assertEquals(2, list.get(0));
        
        list.add((byte) 6);
        assertEquals(3, list.size());
        assertEquals(6, list.get(2));
    }
    
    @Test
    public void testTrimToSize() {
        list = new ArrayByteList(5);
        
        list.add((byte) 2);
        list.add((byte) 4);
        
        list.trimToSize();
        
        assertEquals(2, list.size());
        assertEquals(2, list.get(0));
        assertEquals(4, list.get(1));
    }
}
