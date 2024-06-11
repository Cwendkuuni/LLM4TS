import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {
    
    private ArrayByteList arrayByteList;
    
    @Before
    public void setUp() {
        arrayByteList = new ArrayByteList();
    }
    
    @Test
    public void testGet() {
        // Test with valid index
        arrayByteList.add(0, (byte) 1);
        arrayByteList.add(1, (byte) 2);
        arrayByteList.add(2, (byte) 3);
        assertEquals(1, arrayByteList.get(0));
        assertEquals(2, arrayByteList.get(1));
        assertEquals(3, arrayByteList.get(2));
        
        // Test with invalid index
        try {
            arrayByteList.get(-1);
            fail("Exception not thrown for negative index");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Should be at least 0 and less than " + arrayByteList.size() + ", found -1", e.getMessage());
        }
        try {
            arrayByteList.get(arrayByteList.size());
            fail("Exception not thrown for index greater than size");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Should be at least 0 and less than " + arrayByteList.size() + ", found " + arrayByteList.size(), e.getMessage());
        }
    }
    
    @Test
    public void testSize() {
        // Test with empty list
        assertEquals(0, arrayByteList.size());
        
        // Test with non-empty list
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        arrayByteList.add((byte) 3);
        assertEquals(3, arrayByteList.size());
    }
    
    @Test
    public void testRemoveElementAt() {
        // Test with valid index
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        arrayByteList.add((byte) 3);
        assertEquals(2, arrayByteList.removeElementAt(1));
        assertEquals(2, arrayByteList.size());
        
        // Test with invalid index
        try {
            arrayByteList.removeElementAt(-1);
            fail("Exception not thrown for negative index");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Should be at least 0 and less than " + arrayByteList.size() + ", found -1", e.getMessage());
        }
        try {
            arrayByteList.removeElementAt(arrayByteList.size());
            fail("Exception not thrown for index greater than size");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Should be at least 0 and less than " + arrayByteList.size() + ", found " + arrayByteList.size(), e.getMessage());
        }
    }
    
    @Test
    public void testSet() {
        // Test with valid index
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        arrayByteList.add((byte) 3);
        assertEquals(2, arrayByteList.set(1, (byte) 4));
        assertEquals(4, arrayByteList.get(1));
        
        // Test with invalid index
        try {
            arrayByteList.set(-1, (byte) 1);
            fail("Exception not thrown for negative index");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Should be at least 0 and less than " + arrayByteList.size() + ", found -1", e.getMessage());
        }
        try {
            arrayByteList.set(arrayByteList.size(), (byte) 1);
            fail("Exception not thrown for index greater than size");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Should be at least 0 and less than " + arrayByteList.size() + ", found " + arrayByteList.size(), e.getMessage());
        }
    }
    
    @Test
    public void testAdd() {
        // Test with valid index
        arrayByteList.add(0, (byte) 1);
        arrayByteList.add(1, (byte) 2);
        arrayByteList.add(2, (byte) 3);
        assertEquals(1, arrayByteList.get(0));
        assertEquals(2, arrayByteList.get(1));
        assertEquals(3, arrayByteList.get(2));
        
        // Test with invalid index
        try {
            arrayByteList.add(-1, (byte) 1);
            fail("Exception not thrown for negative index");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Should be at least 0 and at most " + arrayByteList.size() + ", found -1", e.getMessage());
        }
        try {
            arrayByteList.add(arrayByteList.size() + 1, (byte) 1);
            fail("Exception not thrown for index greater than size");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Should be at least 0 and at most " + arrayByteList.size() + ", found " + (arrayByteList.size() + 1), e.getMessage());
        }
        
        // Test increasing size after adding
        int initialSize = arrayByteList.size();
        arrayByteList.add(1, (byte) 4);
        assertEquals(initialSize + 1, arrayByteList.size());
    }
    
    @Test
    public void testEnsureCapacity() {
        // Test with mincap less than current capacity
        arrayByteList.ensureCapacity(5);
        assertEquals(8, arrayByteList._data.length);
        
        // Test with mincap greater than current capacity
        arrayByteList.ensureCapacity(10);
        assertTrue(arrayByteList._data.length >= 10);
    }
    
    @Test
    public void testTrimToSize() {
        // Test with size equal to capacity
        arrayByteList.ensureCapacity(10);
        assertEquals(10, arrayByteList._data.length);
        arrayByteList.trimToSize();
        assertEquals(0, arrayByteList._data.length);
        
        // Test with size less than capacity
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        arrayByteList.ensureCapacity(10);
        assertEquals(10, arrayByteList._data.length);
        arrayByteList.trimToSize();
        assertEquals(2, arrayByteList._data.length);
    }
}