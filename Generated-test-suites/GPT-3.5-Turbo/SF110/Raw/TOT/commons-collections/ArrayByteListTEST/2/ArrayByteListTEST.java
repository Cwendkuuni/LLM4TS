import org.junit.Before;
import org.junit.Test;
import org.apache.commons.collections.primitives.ArrayByteList;
import org.apache.commons.collections.primitives.ByteCollection;

import static org.junit.Assert.*;

public class ArrayByteListTest {
    
    private ArrayByteList arrayByteList;
    
    @Before
    public void setUp() {
        arrayByteList = new ArrayByteList();
    }
    
    @Test
    public void testConstructorWithInitialCapacity() {
        int initialCapacity = 10;
        arrayByteList = new ArrayByteList(initialCapacity);
        assertEquals(initialCapacity, arrayByteList.size());
    }
    
    @Test
    public void testConstructorWithByteCollection() {
        ByteCollection byteCollection = new ArrayByteList();
        byteCollection.add((byte) 1);
        byteCollection.add((byte) 2);
        arrayByteList = new ArrayByteList(byteCollection);
        assertEquals(2, arrayByteList.size());
        assertEquals((byte) 1, arrayByteList.get(0));
        assertEquals((byte) 2, arrayByteList.get(1));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeInitialCapacity() {
        int initialCapacity = -1;
        arrayByteList = new ArrayByteList(initialCapacity);
    }
    
    @Test
    public void testGetSize() {
        assertEquals(0, arrayByteList.size());
    }
    
    @Test
    public void testGetOutOfBounds() {
        arrayByteList.add((byte) 1);
        try {
            arrayByteList.get(1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Should be at least 0 and less than 1, found 1", e.getMessage());
        }
    }
    
    @Test
    public void testRemoveElementAt() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        byte removedElement = arrayByteList.removeElementAt(0);
        assertEquals(1, arrayByteList.size());
        assertEquals((byte) 1, removedElement);
    }
    
    @Test
    public void testRemoveElementAtOutOfBounds() {
        arrayByteList.add((byte) 1);
        try {
            arrayByteList.removeElementAt(1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Should be at least 0 and less than 1, found 1", e.getMessage());
        }
    }
    
    @Test
    public void testSet() {
        arrayByteList.add((byte) 1);
        byte oldElement = arrayByteList.set(0, (byte) 2);
        assertEquals(1, arrayByteList.size());
        assertEquals((byte) 2, arrayByteList.get(0));
        assertEquals((byte) 1, oldElement);
    }
    
    @Test
    public void testSetOutOfBounds() {
        try {
            arrayByteList.set(0, (byte) 1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Should be at least 0 and less than 0, found 0", e.getMessage());
        }
    }
    
    @Test
    public void testAdd() {
        arrayByteList.add(0, (byte) 1);
        arrayByteList.add(1, (byte) 3);
        arrayByteList.add(1, (byte) 2);
        assertEquals(3, arrayByteList.size());
        assertEquals((byte) 1, arrayByteList.get(0));
        assertEquals((byte) 2, arrayByteList.get(1));
        assertEquals((byte) 3, arrayByteList.get(2));
    }

    @Test
    public void testAddOutOfBounds() {
        try {
            arrayByteList.add(1, (byte) 1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Should be at least 0 and at most 0, found 1", e.getMessage());
        }
    }
    
    @Test
    public void testEnsureCapacity() {
        arrayByteList.ensureCapacity(10);
        assertEquals(0, arrayByteList.size());
    }
    
    @Test
    public void testTrimToSize() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        arrayByteList.trimToSize();
        assertEquals(2, arrayByteList.size());
        assertEquals(2, arrayByteList.toArray().length);
    }
  
    @Test
    public void testWriteObject() {
        // TODO: Implement this test
    }
    
    @Test
    public void testReadObject() {
        // TODO: Implement this test
    }
    
    @Test
    public void testCheckRange() {
        // TODO: Implement this test
    }
    
    @Test
    public void testCheckRangeIncludingEndpoint() {
        // TODO: Implement this test
    }
    
    
}