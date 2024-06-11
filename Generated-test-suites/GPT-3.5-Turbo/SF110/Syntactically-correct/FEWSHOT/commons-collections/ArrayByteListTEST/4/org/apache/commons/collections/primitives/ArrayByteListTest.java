package org.apache.commons.collections.primitives;

import static org.junit.Assert.*;
import org.junit.Test;

public class ArrayByteListTest {
    @Test
    public void testConstructor() {
        ArrayByteList list = new ArrayByteList();
        assertNotNull(list);
        assertEquals(0, list.size());
        
        ArrayByteList list2 = new ArrayByteList(10);
        assertNotNull(list2);
        assertEquals(0, list2.size());
        
        try {
            ArrayByteList list3 = new ArrayByteList(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
    
    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list.add(10);
        assertEquals(1, list.size());
        assertEquals(10, list.get(0));
        
        list.add(0, 20);
        assertEquals(20, list.get(0));
        assertEquals(10, list.get(1));
    }
    
    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add(10);
        list.add(20);
        list.add(30);
        
        assertEquals(10, list.get(0));
        assertEquals(20, list.get(1));
        assertEquals(30, list.get(2));
        
        try {
            list.get(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
        
        try {
            list.get(3);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }
    
    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add(10);
        list.add(20);
        list.add(30);
        
        byte removed = list.removeElementAt(1);
        assertEquals(20, removed);
        assertEquals(2, list.size());
        assertEquals(10, list.get(0));
        assertEquals(30, list.get(1));
        
        try {
            list.removeElementAt(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
        
        try {
            list.removeElementAt(2);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }
    
    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add(10);
        list.add(20);
        list.add(30);
        
        byte old = list.set(1, 25);
        assertEquals(20, old);
        assertEquals(3, list.size());
        assertEquals(10, list.get(0));
        assertEquals(25, list.get(1));
        assertEquals(30, list.get(2));
        
        try {
            list.set(-1, 5);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
        
        try {
            list.set(3, 5);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }
    
    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();
        list.ensureCapacity(10);
        assertEquals(0, list.size());
        
        list.add(1);
        list.ensureCapacity(10);
        assertEquals(1, list.size());
        
        list.ensureCapacity(0);
        assertEquals(1, list.size());
    }
    
    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList();
        list.trimToSize();
        assertEquals(0, list.size());
        
        list.add(1);
        list.trimToSize();
        assertEquals(1, list.size());
    }
}