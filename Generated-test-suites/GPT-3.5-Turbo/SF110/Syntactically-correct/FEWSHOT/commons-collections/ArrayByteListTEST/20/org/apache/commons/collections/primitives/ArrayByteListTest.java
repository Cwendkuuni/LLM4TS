package org.apache.commons.collections.primitives;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        assertEquals(8, list._data.length);
        assertEquals(0, list._size);
    }
    
    @Test
    public void testConstructorWithInitialCapacity() {
        ArrayByteList list = new ArrayByteList(10);
        assertEquals(10, list._data.length);
        assertEquals(0, list._size);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeInitialCapacity() {
        new ArrayByteList(-1);
    }
    
    @Test
    public void testConstructorWithByteCollection() {
        ByteCollection collection = new ByteHashSet();
        collection.add((byte)1);
        collection.add((byte)2);
        collection.add((byte)3);
        
        ArrayByteList list = new ArrayByteList(collection);
        assertEquals(3, list._data.length);
        assertEquals(3, list._size);
        assertEquals((byte)1, list._data[0]);
        assertEquals((byte)2, list._data[1]);
        assertEquals((byte)3, list._data[2]);
    }
    
    @Test
    public void testGet() {
        list._data = new byte[] {1, 2, 3};
        list._size = 3;
        
        assertEquals((byte)1, list.get(0));
        assertEquals((byte)2, list.get(1));
        assertEquals((byte)3, list.get(2));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetIndexOutOfBounds() {
        list._data = new byte[3];
        list._size = 3;
        
        list.get(3);
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
        
        assertEquals((byte)2, removedElement);
        assertEquals(2, list._size);
        assertEquals((byte)1, list._data[0]);
        assertEquals((byte)3, list._data[1]);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtIndexOutOfBounds() {
        list._data = new byte[3];
        list._size = 3;
        
        list.removeElementAt(3);
    }
    
    @Test
    public void testSet() {
        list._data = new byte[] {1, 2, 3};
        list._size = 3;
        
        byte oldElement = list.set(1, (byte)5);
        
        assertEquals((byte)2, oldElement);
        assertEquals((byte)5, list._data[1]);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetIndexOutOfBounds() {
        list._data = new byte[3];
        list._size = 3;
        
        list.set(3, (byte)5);
    }
    
    @Test
    public void testAdd() {
        list._data = new byte[3];
        list._size = 2;
        
        list.add(1, (byte)5);
        
        assertEquals(3, list._data.length);
        assertEquals(3, list._size);
        assertEquals((byte)0, list._data[0]);
        assertEquals((byte)5, list._data[1]);
        assertEquals((byte)0, list._data[2]);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddIndexOutOfBounds() {
        list._data = new byte[3];
        list._size = 3;
        
        list.add(4, (byte)5);
    }
    
    @Test
    public void testEnsureCapacity() {
        list._data = new byte[3];
        list._size = 2;
        
        list.ensureCapacity(4);
        
        assertTrue(list._data.length >= 4);
    }
    
    @Test
    public void testTrimToSize() {
        list._data = new byte[5];
        list._size = 2;
        
        list.trimToSize();
        
        assertEquals(2, list._data.length);
    }
    
    @Test
    public void testPrivateCheckRange() {
        list._size = 5;
        
        list.checkRange(3);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testPrivateCheckRangeIndexOutOfBounds() {
        list._size = 5;
        
        list.checkRange(6);
    }
    
    @Test
    public void testPrivateCheckRangeIncludingEndpoint() {
        list._size = 5;
        
        list.checkRangeIncludingEndpoint(5);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testPrivateCheckRangeIncludingEndpointIndexOutOfBounds() {
        list._size = 5;
        
        list.checkRangeIncludingEndpoint(6);
    }
}