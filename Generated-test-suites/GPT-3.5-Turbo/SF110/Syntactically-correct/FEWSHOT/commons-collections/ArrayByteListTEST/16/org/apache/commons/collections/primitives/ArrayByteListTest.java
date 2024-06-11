package org.apache.commons.collections.primitives;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
        ArrayByteList emptyList = new ArrayByteList();
        assertEquals(0, emptyList.size());
        
        ArrayByteList listWithCapacity = new ArrayByteList(10);
        assertEquals(0, listWithCapacity.size());
        
        try {
            ArrayByteList negativeCapacityList = new ArrayByteList(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testSize() {
        assertEquals(0, arrayByteList.size());
        
        arrayByteList.add(1);
        assertEquals(1, arrayByteList.size());
        
        arrayByteList.add(2);
        assertEquals(2, arrayByteList.size());
        
        arrayByteList.removeElementAt(1);
        assertEquals(1, arrayByteList.size());
    }
    
    @Test
    public void testGet() {
        arrayByteList.add(1);
        arrayByteList.add(2);
        arrayByteList.add(3);
        
        assertEquals(1, arrayByteList.get(0));
        assertEquals(2, arrayByteList.get(1));
        assertEquals(3, arrayByteList.get(2));
        
        try {
            arrayByteList.get(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
        
        try {
            arrayByteList.get(3);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testRemoveElementAt() {
        arrayByteList.add(1);
        arrayByteList.add(2);
        arrayByteList.add(3);
        
        assertEquals(2, arrayByteList.removeElementAt(1));
        assertEquals(2, arrayByteList.size());
        assertEquals(1, arrayByteList.get(0));
        assertEquals(3, arrayByteList.get(1));
        
        try {
            arrayByteList.removeElementAt(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
        
        try {
            arrayByteList.removeElementAt(2);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testSet() {
        arrayByteList.add(1);
        arrayByteList.add(2);
        arrayByteList.add(3);
        
        assertEquals(2, arrayByteList.set(1, 4));
        assertEquals(3, arrayByteList.size());
        assertEquals(1, arrayByteList.get(0));
        assertEquals(4, arrayByteList.get(1));
        assertEquals(3, arrayByteList.get(2));
        
        try {
            arrayByteList.set(-1, 5);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
        
        try {
            arrayByteList.set(3, 5);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testAdd() {
        arrayByteList.add(1);
        arrayByteList.add(2);
        
        assertEquals(2, arrayByteList.size());
        assertEquals(1, arrayByteList.get(0));
        assertEquals(2, arrayByteList.get(1));
        
        arrayByteList.add(1, 3);
        
        assertEquals(3, arrayByteList.size());
        assertEquals(1, arrayByteList.get(0));
        assertEquals(3, arrayByteList.get(1));
        assertEquals(2, arrayByteList.get(2));
        
        try {
            arrayByteList.add(-1, 4);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
        
        try {
            arrayByteList.add(4, 4);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testEnsureCapacity() {
        arrayByteList.ensureCapacity(4);
        assertEquals(0, arrayByteList.size());
        
        arrayByteList.add(1);
        arrayByteList.add(2);
        arrayByteList.ensureCapacity(4);
        
        assertEquals(2, arrayByteList.size());
        assertEquals(1, arrayByteList.get(0));
        assertEquals(2, arrayByteList.get(1));
        
        arrayByteList.ensureCapacity(1);
        assertEquals(2, arrayByteList.size());
        assertEquals(1, arrayByteList.get(0));
        assertEquals(2, arrayByteList.get(1));
    }
    
    @Test
    public void testTrimToSize() {
        arrayByteList.trimToSize();
        assertEquals(0, arrayByteList.size());
        
        arrayByteList.add(1);
        arrayByteList.add(2);
        arrayByteList.trimToSize();
        
        assertEquals(2, arrayByteList.size());
        assertEquals(1, arrayByteList.get(0));
        assertEquals(2, arrayByteList.get(1));
    }
}