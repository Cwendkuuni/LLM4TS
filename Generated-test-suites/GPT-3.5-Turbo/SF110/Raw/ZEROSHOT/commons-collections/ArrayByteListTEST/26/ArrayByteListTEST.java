package org.apache.commons.collections.primitives;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ArrayByteListTest {
    
    private ArrayByteList arrayByteList;
    
    @Before
    public void setup() {
        arrayByteList = new ArrayByteList();
    }

    @Test
    public void testGet() {
        arrayByteList.add(0, (byte) 1);
        assertEquals(1, arrayByteList.get(0));
    }
    
    @Test
    public void testSize() {
        arrayByteList.add(0, (byte) 1);
        assertEquals(1, arrayByteList.size());
    }

    @Test
    public void testRemoveElementAt() {
        arrayByteList.add(0, (byte) 1);
        byte removedElement = arrayByteList.removeElementAt(0);
        assertEquals(1, removedElement);
        assertEquals(0, arrayByteList.size());
    }

    @Test
    public void testSet() {
        arrayByteList.add(0, (byte) 1);
        byte oldval = arrayByteList.set(0, (byte) 2);
        assertEquals(1, oldval);
        assertEquals(2, arrayByteList.get(0));
    }

    @Test
    public void testAdd() {
        arrayByteList.add(0, (byte) 1);
        assertEquals(1, arrayByteList.get(0));
    }

    @Test
    public void testEnsureCapacity() {
        for (int i = 0; i < 10; i++) {
            arrayByteList.add(i, (byte) i);
        }
        assertEquals(10, arrayByteList.size());
    }

    @Test
    public void testTrimToSize() {
        for (int i = 0; i < 10; i++) {
            arrayByteList.add(i, (byte) i);
        }
        arrayByteList.trimToSize();
        assertEquals(10, arrayByteList.size());
    }

}