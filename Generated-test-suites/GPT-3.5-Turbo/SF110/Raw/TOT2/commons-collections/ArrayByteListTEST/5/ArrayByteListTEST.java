import org.junit.*;
import static org.junit.Assert.*;

import org.apache.commons.collections.primitives.ArrayByteList;

public class ArrayByteListTest {

    private ArrayByteList array;

    @Before
    public void setUp() {
        array = new ArrayByteList();
    }

    @Test
    public void testConstructor() {
        assertEquals(0, array.size());
    }

    @Test
    public void testGet() {
        array.add(0, (byte) 10);
        assertEquals((byte) 10, array.get(0));
    }

    @Test
    public void testSize() {
        array.add(0, (byte) 5);
        assertEquals(1, array.size());
    }

    @Test
    public void testRemoveElementAt() {
        array.add(0, (byte) 5);
        assertEquals((byte) 5, array.removeElementAt(0));
        assertEquals(0, array.size());
    }

    @Test
    public void testSet() {
        array.add(0, (byte) 3);
        assertEquals((byte) 3, array.set(0, (byte) 10));
    }

    @Test
    public void testAdd() {
        array.add(0, (byte) 5);
        array.add(1, (byte) 10);
        assertEquals(2, array.size());
        assertEquals((byte) 10, array.get(1));
    }

    @Test
    public void testEnsureCapacity() {
        array.ensureCapacity(10);
        assertEquals(10, array._data.length);
    }

    @Test
    public void testTrimToSize() {
        array.add(0, (byte) 5);
        array.add(1, (byte) 10);
        array.trimToSize();
        assertEquals(2, array._data.length);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRange() {
        array.add(0, (byte) 5);
        array.get(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpoint() {
        array.add(0, (byte) 5);
        array.add(1, (byte) 10);
        array.get(3);
    }
}