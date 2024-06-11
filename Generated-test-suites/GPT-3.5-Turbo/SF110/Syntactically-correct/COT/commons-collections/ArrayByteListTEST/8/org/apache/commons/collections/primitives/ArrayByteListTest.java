import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArrayByteListTest {

    private ArrayByteList list;

    @Before
    public void setUp() {
        list = new ArrayByteList();
    }

    @Test
    public void testConstructor() {
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        list = new ArrayByteList(10);
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeInitialCapacity() {
        list = new ArrayByteList(-1);
    }

    @Test
    public void testConstructorWithByteCollection() {
        ByteCollection byteCollection = new ArrayByteList();
        list = new ArrayByteList(byteCollection);
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    public void testGetSize() {
        assertEquals(0, list.size());
    }

    @Test
    public void testAdd() {
        list.add((byte) 1);
        assertEquals(1, list.size());
    }

    @Test
    public void testGet() {
        list.add((byte) 1);
        assertEquals((byte) 1, list.get(0));
    }

    @Test
    public void testSet() {
        list.add((byte) 1);
        assertEquals((byte) 1, list.set(0, (byte) 2));
        assertEquals((byte) 2, list.get(0));
    }

    @Test
    public void testRemoveElementAt() {
        list.add((byte) 1);
        assertEquals((byte) 1, list.removeElementAt(0));
        assertEquals(0, list.size());
    }

    @Test
    public void testAddAtIndex() {
        list.add(0, (byte) 1);
        assertEquals((byte) 1, list.get(0));
    }

    @Test
    public void testEnsureCapacity() {
        list.ensureCapacity(10);
        assertEquals(0, list.size());
    }

    @Test
    public void testTrimToSize() {
        list.add((byte) 1);
        list.trimToSize();
        assertEquals(1, list.size());
    }

    // Test edge cases and exception handling scenarios

    @Test
    public void testGetOutOfBounds() {
        try {
            list.get(0);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Should be at least 0 and less than 0, found 0", e.getMessage());
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBoundsNegativeIndex() {
        list.get(-1);
    }

    @Test
    public void testSetOutOfBounds() {
        list.add((byte) 1);
        try {
            list.set(1, (byte) 2);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Should be at least 0 and less than 1, found 1", e.getMessage());
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetOutOfBoundsNegativeIndex() {
        list.set(-1, (byte) 1);
    }

    @Test
    public void testRemoveElementAtOutOfBounds() {
        try {
            list.removeElementAt(0);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            assertEquals("Should be at least 0 and less than 0, found 0", e.getMessage());
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtOutOfBoundsNegativeIndex() {
        list.removeElementAt(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddAtIndexOutOfBounds() {
        list.add(1, (byte) 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddAtIndexNegativeIndex() {
        list.add(-1, (byte) 1);
    }

    // Add more test cases as needed for edge cases and exception handling scenarios

}