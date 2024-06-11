import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {

    private ArrayByteList arrayByteList;

    @Before
    public void setUp() {
        arrayByteList = new ArrayByteList();
        arrayByteList.add(0, (byte) 1);
        arrayByteList.add(1, (byte) 2);
        arrayByteList.add(2, (byte) 3);
    }

    @Test
    public void testGet() {
        byte result = arrayByteList.get(1);
        assertEquals((byte) 2, result);

        try {
            arrayByteList.get(3);
            fail("IndexOutOfBoundsException should have been thrown");
        } catch (IndexOutOfBoundsException e) {
            // Exception thrown as expected
        }
    }

    @Test
    public void testSize() {
        int size = arrayByteList.size();
        assertEquals(3, size);

        arrayByteList.add(3, (byte) 4);
        size = arrayByteList.size();
        assertEquals(4, size);
    }

    @Test
    public void testRemoveElementAt() {
        byte result = arrayByteList.removeElementAt(1);
        assertEquals((byte) 2, result);
        assertEquals(2, arrayByteList.size());

        try {
            arrayByteList.removeElementAt(3);
            fail("IndexOutOfBoundsException should have been thrown");
        } catch (IndexOutOfBoundsException e) {
            // Exception thrown as expected
        }
    }

    @Test
    public void testSet() {
        byte result = arrayByteList.set(1, (byte) 5);
        assertEquals((byte) 2, result);
        assertEquals((byte) 5, arrayByteList.get(1));

        try {
            arrayByteList.set(3, (byte) 6);
            fail("IndexOutOfBoundsException should have been thrown");
        } catch (IndexOutOfBoundsException e) {
            // Exception thrown as expected
        }
    }

    @Test
    public void testAdd() {
        arrayByteList.add(1, (byte) 4);
        assertEquals((byte) 4, arrayByteList.get(1));
        assertEquals(4, arrayByteList.size());

        try {
            arrayByteList.add(5, (byte) 6);
            fail("IndexOutOfBoundsException should have been thrown");
        } catch (IndexOutOfBoundsException e) {
            // Exception thrown as expected
        }
    }

    @Test
    public void testEnsureCapacity() {
        arrayByteList.ensureCapacity(6);
        assertEquals(8, arrayByteList._data.length);
        
        arrayByteList.add(4, (byte) 6);
        arrayByteList.ensureCapacity(5);
        assertEquals(8, arrayByteList._data.length);
    }

    @Test
    public void testTrimToSize() {
        arrayByteList.trimToSize();
        assertEquals(3, arrayByteList._data.length);
        
        arrayByteList.add(3, (byte) 7);
        arrayByteList.trimToSize();
        assertEquals(4, arrayByteList._data.length);
    }

    // Test cases for exception handling methods and edge cases should also be added

}