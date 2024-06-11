package org.apache.commons.collections.primitives;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Before;
import org.junit.Test;

public class ArrayByteListTest {

    private ArrayByteList arrayByteList;
    
    @Before
    public void setUp() {
        arrayByteList = new ArrayByteList();
    }
    
    @Test
    public void testConstructor() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(8, list.size());
        
        list = new ArrayByteList(10);
        assertEquals(0, list.size());
        
        assertThrows(IllegalArgumentException.class, () -> new ArrayByteList(-1));
    }
    
    @Test
    public void testGet() {
        arrayByteList.add(1, (byte) 10);
        arrayByteList.add(2, (byte) 20);
        arrayByteList.add(3, (byte) 30);
        
        assertEquals(10, arrayByteList.get(1));
        assertEquals(20, arrayByteList.get(2));
        assertEquals(30, arrayByteList.get(3));
        
        assertThrows(IndexOutOfBoundsException.class, () -> arrayByteList.get(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> arrayByteList.get(4));
    }
    
    @Test
    public void testSize() {
        assertEquals(0, arrayByteList.size());
        
        arrayByteList.add(1, (byte) 10);
        arrayByteList.add(2, (byte) 20);
        arrayByteList.add(3, (byte) 30);
        
        assertEquals(3, arrayByteList.size());
    }
    
    @Test
    public void testRemoveElementAt() {
        arrayByteList.add(1, (byte) 10);
        arrayByteList.add(2, (byte) 20);
        arrayByteList.add(3, (byte) 30);
        
        byte removedElement = arrayByteList.removeElementAt(2);
        
        assertEquals(20, removedElement);
        assertEquals(2, arrayByteList.size());
        
        removedElement = arrayByteList.removeElementAt(0);
        
        assertEquals(10, removedElement);
        assertEquals(1, arrayByteList.size());
        
        assertThrows(IndexOutOfBoundsException.class, () -> arrayByteList.removeElementAt(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> arrayByteList.removeElementAt(1));
    }
    
    @Test
    public void testSet() {
        arrayByteList.add(1, (byte) 10);
        arrayByteList.add(2, (byte) 20);
        arrayByteList.add(3, (byte) 30);
        
        byte oldValue = arrayByteList.set(2, (byte) 25);
        
        assertEquals(20, oldValue);
        assertEquals(25, arrayByteList.get(2));
        
        oldValue = arrayByteList.set(0, (byte) 15);
        
        assertEquals(10, oldValue);
        assertEquals(15, arrayByteList.get(0));
        
        assertThrows(IndexOutOfBoundsException.class, () -> arrayByteList.set(-1, (byte) 5));
        assertThrows(IndexOutOfBoundsException.class, () -> arrayByteList.set(3, (byte) 5));
    }
    
    @Test
    public void testAdd() {
        arrayByteList.add(1, (byte) 10);
        arrayByteList.add(2, (byte) 20);
        
        assertEquals(10, arrayByteList.get(1));
        assertEquals(20, arrayByteList.get(2));
        assertEquals(2, arrayByteList.size());
        
        arrayByteList.add(1, (byte) 15);
        
        assertEquals(10, arrayByteList.get(2));
        assertEquals(15, arrayByteList.get(1));
        assertEquals(20, arrayByteList.get(3));
        assertEquals(3, arrayByteList.size());
        
        assertThrows(IndexOutOfBoundsException.class, () -> arrayByteList.add(-1, (byte) 5));
        assertThrows(IndexOutOfBoundsException.class, () -> arrayByteList.add(4, (byte) 5));
    }
    
    @Test
    public void testEnsureCapacity() {
        arrayByteList.ensureCapacity(5);
        
        assertEquals(8, arrayByteList.size());
        
        arrayByteList.ensureCapacity(10);
        
        assertEquals(10, arrayByteList.size());
    }
    
    @Test
    public void testTrimToSize() {
        arrayByteList.add(1, (byte) 10);
        arrayByteList.add(2, (byte) 20);
        
        arrayByteList.trimToSize();
        
        assertEquals(2, arrayByteList.size());
    }
}