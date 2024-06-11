package org.apache.commons.collections.primitives;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;

import org.junit.Before;
import org.junit.Test;

public class ArrayByteListTest {
    
    private ArrayByteList list;
    
    @Before
    public void setup() {
        list = new ArrayByteList();
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_withNegativeCapacity_shouldThrowIllegalArgumentException() {
        new ArrayByteList(-1);
    }
    
    @Test
    public void testConstructor_withValidCapacity_shouldInitializeArrayWithGivenCapacity() {
        ArrayByteList list = new ArrayByteList(10);
        assertEquals(0, list.size());
        assertEquals(10, list.toArray().length);
    }
    
    @Test
    public void testConstructor_withByteCollection_shouldInitializeArrayWithElementsFromCollection() {
        ByteCollection collection = new ArrayByteCollection();
        collection.add((byte)1);
        collection.add((byte)2);
        
        ArrayByteList list = new ArrayByteList(collection);
        assertEquals(2, list.size());
        assertArrayEquals(new byte[]{1, 2}, list.toArray());
    }
    
    @Test
    public void testGet_withValidIndex_shouldReturnElementAtIndex() {
        list.add((byte)1);
        list.add((byte)2);
        
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGet_withInvalidIndex_shouldThrowIndexOutOfBoundsException() {
        list.get(0);
    }
    
    @Test
    public void testSize_withEmptyList_shouldReturnZero() {
        assertEquals(0, list.size());
    }
    
    @Test
    public void testRemoveElementAt_withValidIndex_shouldRemoveElementAndReturnOldValue() {
        list.add((byte)1);
        list.add((byte)2);
        
        byte removed = list.removeElementAt(0);
        assertEquals(1, removed);
        assertEquals(1, list.size());
        assertEquals(2, list.get(0));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAt_withInvalidIndex_shouldThrowIndexOutOfBoundsException() {
        list.removeElementAt(0);
    }
    
    @Test
    public void testSet_withValidIndex_shouldSetElementAndReturnOldValue() {
        list.add((byte)1);
        list.add((byte)2);
        
        byte oldValue = list.set(0, (byte)3);
        assertEquals(1, oldValue);
        assertEquals(2, list.size());
        assertEquals(3, list.get(0));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSet_withInvalidIndex_shouldThrowIndexOutOfBoundsException() {
        list.set(0, (byte)1);
    }
    
    @Test
    public void testAdd_withValidIndex_shouldAddElementAtGivenIndex() {
        list.add((byte)1);
        list.add((byte)3);
        
        list.add(1, (byte)2);
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }
    
    @Test
    public void testAdd_withValidIndexAndExistingElements_shouldShiftElementsAndAddElementAtGivenIndex() {
        list.add((byte)1);
        list.add((byte)3);
        
        list.add(1, (byte)2);
        list.add(0, (byte)0);
        
        assertEquals(4, list.size());
        assertEquals(0, list.get(0));
        assertEquals(1, list.get(1));
        assertEquals(2, list.get(2));
        assertEquals(3, list.get(3));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAdd_withInvalidIndex_shouldThrowIndexOutOfBoundsException() {
        list.add(-1, (byte)1);
    }
    
    @Test
    public void testEnsureCapacity_withMinimumCapacityGreaterThanCurrentCapacity_shouldIncreaseCapacity() {
        list.ensureCapacity(10);
        
        assertEquals(10, list.toArray().length);
    }
    
    @Test
    public void testEnsureCapacity_withMinimumCapacityLessThanCurrentCapacity_shouldNotChangeCapacity() {
        list.ensureCapacity(5);
        
        assertEquals(8, list.toArray().length);
    }
    
    @Test
    public void testTrimToSize_withSizeLessThanCapacity_shouldTrimArrayToSize() {
        list.add((byte)1);
        
        list.trimToSize();
        
        assertEquals(1, list.toArray().length);
    }
    
    @Test
    public void testTrimToSize_withSizeEqualToCapacity_shouldNotChangeArraySize() {
        list.add((byte)1);
        list.add((byte)2);
        list.add((byte)3);
        
        list.trimToSize();
        
        assertEquals(3, list.toArray().length);
    }
}