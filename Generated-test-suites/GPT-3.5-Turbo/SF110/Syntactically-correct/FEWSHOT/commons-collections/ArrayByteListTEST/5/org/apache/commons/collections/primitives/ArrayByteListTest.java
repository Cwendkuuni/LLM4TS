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
    public void testDefaultConstructor() {
        assertEquals(0, arrayByteList.size());
    }
    
    @Test
    public void testConstructorWithCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        assertEquals(0, list.size());
    }
    
    @Test
    public void testConstructorWithByteCollection() {
        ByteCollection byteCollection = new ByteCollection();
        byteCollection.add((byte)1);
        byteCollection.add((byte)2);
        
        ArrayByteList list = new ArrayByteList(byteCollection);
        assertEquals(2, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
    }

    @Test
    public void testGet() {
        arrayByteList.add(5);
        assertEquals(5, arrayByteList.get(0));
    }
    
    @Test
    public void testGetOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> arrayByteList.get(0));
    }
    
    @Test
    public void testSize() {
        arrayByteList.add(1);
        arrayByteList.add(2);
        assertEquals(2, arrayByteList.size());
    }

    @Test
    public void testRemoveElementAt() {
        arrayByteList.add(1);
        byte removed = arrayByteList.removeElementAt(0);
        assertEquals(1, removed);
        assertEquals(0, arrayByteList.size());
    }
    
    @Test
    public void testRemoveElementAtOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> arrayByteList.removeElementAt(0));
    }

    @Test
    public void testSet() {
        arrayByteList.add(5);
        byte oldValue = arrayByteList.set(0, 10);
        assertEquals(5, oldValue);
        assertEquals(10, arrayByteList.get(0));
    }
    
    @Test
    public void testSetOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> arrayByteList.set(0, 10));
    }

    @Test
    public void testAdd() {
        arrayByteList.add(0, 5);
        assertEquals(5, arrayByteList.get(0));
        assertEquals(1, arrayByteList.size());
    }
    
    @Test
    public void testAddOutOfBounds() {
        assertThrows(IndexOutOfBoundsException.class, () -> arrayByteList.add(1, 5));
    }

    @Test
    public void testEnsureCapacity() {
        arrayByteList.ensureCapacity(10);
        assertEquals(10, arrayByteList._data.length);
    }

    @Test
    public void testTrimToSize() {
        arrayByteList.ensureCapacity(10);
        arrayByteList.trimToSize();
        assertEquals(0, arrayByteList._data.length);
    }

    @Test
    public void testWriteObject() {
        // Write object to file and validate
    }

    @Test
    public void testReadObject() {
        // Read object from file and validate
    }

    // including additional test cases if needed
}