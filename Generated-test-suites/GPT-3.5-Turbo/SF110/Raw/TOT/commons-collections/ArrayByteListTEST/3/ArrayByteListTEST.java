
package org.apache.commons.collections.primitives;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ArrayByteListTest {
    
    private ArrayByteList list;
    
    @Before
    public void setup() {
        list = new ArrayByteList();
    }
    
    @Test
    public void testConstructor() {
        assertEquals(8, list._data.length);
        assertEquals(0, list._size);
    }
    
    @Test
    public void testConstructorWithInitialCapacity() {
        ArrayByteList listWithCapacity = new ArrayByteList(16);
        assertEquals(16, listWithCapacity._data.length);
        assertEquals(0, listWithCapacity._size);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidInitialCapacity() {
        new ArrayByteList(-1);
    }
    
    @Test
    public void testConstructorWithByteCollection() {
        ByteCollectionMock byteCollection = new ByteCollectionMock();
        byteCollection.add((byte) 1);
        byteCollection.add((byte) 2);
        byteCollection.add((byte) 3);
        
        ArrayByteList listWithCollection = new ArrayByteList(byteCollection);
        assertEquals(3, listWithCollection._size);
        assertEquals(8, listWithCollection._data.length);
        assertEquals((byte) 1, listWithCollection._data[0]);
        assertEquals((byte) 2, listWithCollection._data[1]);
        assertEquals((byte) 3, listWithCollection._data[2]);
    }
    
    @Test
    public void testGet() {
        list._data = new byte[] {1, 2, 3};
        list._size = 3;
        
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 3, list.get(2));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithNegativeIndex() {
        list.get(-1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithInvalidIndex() {
        list.get(0);
    }
    
    @Test
    public void testSize() {
        list._size = 5;
        assertEquals(5, list.size());
    }
    
    @Test
    public void testRemoveElementAt() {
        list._data = new byte[] {1, 2, 3};
        list._size = 3;
        
        byte removedElement = list.removeElementAt(1);
        
        assertEquals(2, list._size);
        assertEquals((byte) 2, removedElement);
        assertEquals((byte) 1, list._data[0]);
        assertEquals((byte) 3, list._data[1]);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithNegativeIndex() {
        list.removeElementAt(-1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithInvalidIndex() {
        list.removeElementAt(0);
    }
    
    @Test
    public void testSet() {
        list._data = new byte[] {1, 2, 3};
        list._size = 3;
        
        byte oldElement = list.set(1, (byte) 4);
        
        assertEquals(3, list._size);
        assertEquals((byte) 2, oldElement);
        assertEquals((byte) 1, list._data[0]);
        assertEquals((byte) 4, list._data[1]);
        assertEquals((byte) 3, list._data[2]);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithNegativeIndex() {
        list.set(-1, (byte) 5);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithInvalidIndex() {
        list.set(0, (byte) 5);
    }
    
    @Test
    public void testAdd() {
        list._data = new byte[] {1, 2, 3};
        list._size = 3;
        
        list.add(1, (byte) 4);
        
        assertEquals(4, list._size);
        assertEquals((byte) 1, list._data[0]);
        assertEquals((byte) 4, list._data[1]);
        assertEquals((byte) 2, list._data[2]);
        assertEquals((byte) 3, list._data[3]);
    }
    
    @Test
    public void testAddWithEmptyList() {
        list._data = new byte[0];
        list._size = 0;
        
        list.add(0, (byte) 1);
        
        assertEquals(1, list._size);
        assertEquals((byte) 1, list._data[0]);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithNegativeIndex() {
        list.add(-1, (byte) 1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithInvalidIndex() {
        list._data = new byte[] {1, 2};
        list._size = 2;
        
        list.add(3, (byte) 3);
    }
    
    @Test
    public void testEnsureCapacity() {
        list._data = new byte[] {1, 2, 3};
        list._size = 3;
        
        list.ensureCapacity(5);
        
        assertEquals(3, list._size);
        assertTrue(list._data.length >= 5);
    }
    
    @Test
    public void testEnsureCapacityWithNoData() {
        list._data = new byte[0];
        list._size = 0;
        
        list.ensureCapacity(5);
        
        assertEquals(0, list._size);
        assertTrue(list._data.length >= 5);
    }
    
    @Test
    public void testTrimToSize() {
        list._data = new byte[] {1, 2, 3};
        list._size = 3;
        
        list.trimToSize();
        
        assertEquals(3, list._size);
        assertEquals(3, list._data.length);
    }
    
    @Test
    public void testTrimToSizeWithNoData() {
        list._data = new byte[0];
        list._size = 0;
        
        list.trimToSize();
        
        assertEquals(0, list._size);
        assertEquals(0, list._data.length);
    }
}
