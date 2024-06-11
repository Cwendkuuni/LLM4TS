
package org.apache.commons.collections.primitives;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
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
        assertArrayEquals(new byte[8], list.toArray());
        
        ArrayByteList listWithCapacity = new ArrayByteList(5);
        assertEquals(0, listWithCapacity.size());
        assertArrayEquals(new byte[5], listWithCapacity.toArray());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidCapacity() {
        ArrayByteList listWithNegativeCapacity = new ArrayByteList(-1);
    }
    
    @Test
    public void testConstructorWithCollection() {
        ByteCollection collection = new ArrayByteList();
        collection.add((byte)1);
        collection.add((byte)2);
        
        ArrayByteList listWithCollection = new ArrayByteList(collection);
        assertEquals(2, listWithCollection.size());
        assertArrayEquals(new byte[]{1, 2}, listWithCollection.toArray());
    }
    
    @Test
    public void testGet() {
        list.add((byte)1);
        list.add((byte)2);
        
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithNegativeIndex() {
        list.get(-1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithIndexOutOfBounds() {
        list.get(0);
    }
    
    @Test
    public void testSize() {
        assertEquals(0, list.size());
        
        list.add((byte)1);
        assertEquals(1, list.size());
        
        list.add((byte)2);
        assertEquals(2, list.size());
    }
    
    @Test
    public void testRemoveElementAt() {
        list.add((byte)1);
        list.add((byte)2);
        
        byte removed = list.removeElementAt(1);
        assertEquals(2, removed);
        assertEquals(1, list.size());
        assertEquals(1, list.get(0));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithNegativeIndex() {
        list.removeElementAt(-1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithIndexOutOfBounds() {
        list.removeElementAt(0);
    }
    
    @Test
    public void testSet() {
        list.add((byte)1);
        list.add((byte)2);
        
        byte old = list.set(1, (byte)3);
        assertEquals(2, old);
        assertEquals(2, list.size());
        assertEquals(1, list.get(0));
        assertEquals(3, list.get(1));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithNegativeIndex() {
        list.set(-1, (byte)1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithIndexOutOfBounds() {
        list.set(0, (byte)1);
    }
    
    @Test
    public void testAdd() {
        list.add((byte)1);
        list.add((byte)2);
        list.add((byte)3);
        
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithNegativeIndex() {
        list.add(-1, (byte)1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithIndexOutOfBounds() {
        list.add(1, (byte)1);
    }
    
    @Test
    public void testEnsureCapacity() {
        list.ensureCapacity(5);
        assertTrue(list.toArray().length >= 5);
        
        list.ensureCapacity(10);
        assertTrue(list.toArray().length >= 10);
    }
    
    @Test
    public void testTrimToSize() {
        list.trimToSize();
        assertEquals(0, list.toArray().length);
        
        list.add((byte)1);
        list.add((byte)2);
        list.trimToSize();
        assertEquals(2, list.toArray().length);
    }
    
    @Test
    public void testSerialization() {
        list.add((byte)1);
        list.add((byte)2);
        
        ArrayByteList deserializedList = (ArrayByteList)SerializationUtils.deserialize(SerializationUtils.serialize(list));
        
        assertEquals(list.size(), deserializedList.size());
        assertArrayEquals(list.toArray(), deserializedList.toArray());
    }
    
    @Test
    public void testCheckRange() {
        list.add((byte)1);
        
        list.get(0);  // should not throw exception
        
        try {
            list.get(-1);
            assertFalse(true);  // should not reach here
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);   // should reach here
        }
        
        try {
            list.get(1);
            assertFalse(true);  // should not reach here
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);   // should reach here
        }
    }
    
    @Test
    public void testCheckRangeIncludingEndpoint() {
        list.add((byte)1);
        
        list.add(0, (byte)2);  // should not throw exception
        
        try {
            list.add(-1, (byte)3);
            assertFalse(true);  // should not reach here
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);   // should reach here
        }
        
        try {
            list.add(2, (byte)4);
            assertFalse(true);  // should not reach here
        } catch (IndexOutOfBoundsException e) {
            assertTrue(true);   // should reach here
        }
    }
    
}
