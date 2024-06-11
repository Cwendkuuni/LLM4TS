import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ArrayByteListTest {

    private ArrayByteList arrayByteList;
    
    @Before
    public void setUp() {
        arrayByteList = new ArrayByteList();
    }
    
    @Test
    public void testArrayByteList() {
        // Verify that the initial capacity is set correctly
        assertTrue(arrayByteList._data.length == 8);
        assertTrue(arrayByteList.size() == 0);
    }
    
    @Test
    public void testArrayByteListWithInitialCapacity() {
        // Verify that the initial capacity is set correctly
        ArrayByteList arrayByteList = new ArrayByteList(10);
        assertTrue(arrayByteList._data.length == 10);
        assertTrue(arrayByteList.size() == 0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testArrayByteListWithNegativeInitialCapacity() {
        // Verify that an IllegalArgumentException is thrown when initialCapacity is negative
        ArrayByteList arrayByteList = new ArrayByteList(-10);    
    }
    
    @Test
    public void testArrayByteListWithByteCollection() {
        // Verify that the ArrayByteList is constructed with the correct size and elements from ByteCollection
        ByteCollection byteCollection = new ByteCollection();
        byteCollection.add((byte) 1);
        byteCollection.add((byte) 2);
        byteCollection.add((byte) 3);
        ArrayByteList arrayByteList = new ArrayByteList(byteCollection);
        
        assertTrue(arrayByteList._data.length == byteCollection.size());
        assertTrue(arrayByteList.get(0) == 1);
        assertTrue(arrayByteList.get(1) == 2);
        assertTrue(arrayByteList.get(2) == 3);
        assertTrue(arrayByteList.size() == byteCollection.size());
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithNegativeIndex() {
        // Verify that an IndexOutOfBoundsException is thrown when index is negative
        arrayByteList.get(-1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithIndexOutOfRange() {
        // Verify that an IndexOutOfBoundsException is thrown when index is greater than or equal to size
        arrayByteList.get(arrayByteList.size());
    }
    
    @Test
    public void testGet() {
        // Verify that the correct element is returned
        arrayByteList.add(0, (byte) 1);
        arrayByteList.add(1, (byte) 2);
        
        assertTrue(arrayByteList.get(0) == 1);
        assertTrue(arrayByteList.get(1) == 2);
    }
    
    @Test
    public void testSize() {
        // Verify that the initial size is 0 and is updated correctly when elements are added or removed
        assertTrue(arrayByteList.size() == 0);
        
        arrayByteList.add(0, (byte) 1);
        assertTrue(arrayByteList.size() == 1);
        
        arrayByteList.add(1, (byte) 2);
        assertTrue(arrayByteList.size() == 2);
        
        arrayByteList.removeElementAt(0);
        assertTrue(arrayByteList.size() == 1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithNegativeIndex() {
        // Verify that an IndexOutOfBoundsException is thrown when index is negative
        arrayByteList.removeElementAt(-1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithIndexOutOfRange() {
        // Verify that an IndexOutOfBoundsException is thrown when index is greater than or equal to size
        arrayByteList.removeElementAt(arrayByteList.size());
    }
    
    @Test
    public void testRemoveElementAt() {
        // Verify that the correct element is removed and returned
        arrayByteList.add(0, (byte) 1);
        arrayByteList.add(1, (byte) 2);
        
        byte removedElement = arrayByteList.removeElementAt(1);
        assertTrue(removedElement == 2);
        assertTrue(arrayByteList.size() == 1);
        
        removedElement = arrayByteList.removeElementAt(0);
        assertTrue(removedElement == 1);
        assertTrue(arrayByteList.size() == 0);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithNegativeIndex() {
        // Verify that an IndexOutOfBoundsException is thrown when index is negative
        arrayByteList.set(-1, (byte) 1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithIndexOutOfRange() {
        // Verify that an IndexOutOfBoundsException is thrown when index is greater than or equal to size
        arrayByteList.set(arrayByteList.size(), (byte) 1);
    }
    
    @Test
    public void testSet() {
        // Verify that the correct element is set and the old element is returned
        arrayByteList.add(0, (byte) 1);
        arrayByteList.add(1, (byte) 2);
        
        byte oldElement = arrayByteList.set(0, (byte) 3);
        assertTrue(oldElement == 1);
        assertTrue(arrayByteList.get(0) == 3);
        
        oldElement = arrayByteList.set(1, (byte) 4);
        assertTrue(oldElement == 2);
        assertTrue(arrayByteList.get(1) == 4);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithNegativeIndex() {
        // Verify that an IndexOutOfBoundsException is thrown when index is negative
        arrayByteList.add(-1, (byte) 1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithIndexOutOfRange() {
        // Verify that an IndexOutOfBoundsException is thrown when index is greater than size
        arrayByteList.add(arrayByteList.size() + 1, (byte) 1);
    }
    
    @Test
    public void testAdd() {
        // Verify that the element is added at the correct index and the size is updated correctly
        arrayByteList.add(0, (byte) 1);
        arrayByteList.add(1, (byte) 2);
        
        assertTrue(arrayByteList.get(0) == 1);
        assertTrue(arrayByteList.get(1) == 2);
        assertTrue(arrayByteList.size() == 2);
    }
    
    @Test
    public void testEnsureCapacity() {
        // Verify that the capacity is expanded correctly when the mincap is greater than the current capacity
        arrayByteList.ensureCapacity(10);
        assertTrue(arrayByteList._data.length == 10);
        
        arrayByteList.ensureCapacity(5);
        assertTrue(arrayByteList._data.length == 10);
    }
    
    @Test
    public void testTrimToSize() {
        // Verify that the capacity is trimmed to the current size
        arrayByteList.ensureCapacity(10);
        arrayByteList.trimToSize();
        assertTrue(arrayByteList._data.length == arrayByteList.size());
    }
    
    @Test
    public void testWriteObject() {
        // Verify that the object is correctly serialized and deserialized
        arrayByteList.add(0, (byte) 1);
        arrayByteList.add(1, (byte) 2);
        
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            
            arrayByteList.writeObject(objectOutputStream);
            
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            
            ArrayByteList deserializedList = new ArrayByteList();
            deserializedList.readObject(objectInputStream);
            
            assertTrue(deserializedList.size() == arrayByteList.size());
            assertTrue(deserializedList.get(0) == arrayByteList.get(0));
            assertTrue(deserializedList.get(1) == arrayByteList.get(1));
            
        } catch (IOException | ClassNotFoundException e) {
            fail("An exception occurred");
        }
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeWithNegativeIndex() {
        // Verify that an IndexOutOfBoundsException is thrown when index is negative
        arrayByteList.checkRange(-1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeWithIndexOutOfRange() {
        // Verify that an IndexOutOfBoundsException is thrown when index is greater than or equal to size
        arrayByteList.checkRange(arrayByteList.size());
    }
    
    @Test
    public void testCheckRange() {
        // Verify that no exception is thrown when index is within range
        arrayByteList.add(0, (byte) 1);
        
        try {
            arrayByteList.checkRange(0);
        } catch (IndexOutOfBoundsException e) {
            fail("An exception occurred");
        }
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointWithNegativeIndex() {
        // Verify that an IndexOutOfBoundsException is thrown when index is negative
        arrayByteList.checkRangeIncludingEndpoint(-1);
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointWithIndexOutOfRange() {
        // Verify that an IndexOutOfBoundsException is thrown when index is greater than size
        arrayByteList.checkRangeIncludingEndpoint(arrayByteList.size() + 1);
    }
    
    @Test
    public void testCheckRangeIncludingEndpoint() {
        // Verify that no exception is thrown when index is within range
        arrayByteList.add(0, (byte) 1);
        
        try {
            arrayByteList.checkRangeIncludingEndpoint(0);
        } catch (IndexOutOfBoundsException e) {
            fail("An exception occurred");
        }
    }
}