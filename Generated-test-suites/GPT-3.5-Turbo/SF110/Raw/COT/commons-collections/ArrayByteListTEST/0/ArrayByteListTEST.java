import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;



public class ArrayByteListTest {

    private ArrayByteList arrayByteList;

    @Before
    public void setUp() {
        arrayByteList = new ArrayByteList();
    }

    @Test
    public void testConstructor() {
        assertEquals(0, arrayByteList.size());
    }

    @Test
    public void testAdd() {
        arrayByteList.add((byte) 5);
        assertEquals(1, arrayByteList.size());
        assertEquals(5, arrayByteList.get(0));
    }

    @Test
    public void testGet() {
        arrayByteList.add((byte) 5);
        arrayByteList.add((byte) 10);
        assertEquals(2, arrayByteList.size());
        assertEquals(5, arrayByteList.get(0));
        assertEquals(10, arrayByteList.get(1));
    }

    @Test
    public void testSet() {
        arrayByteList.add((byte) 5);
        arrayByteList.add((byte) 10);
        arrayByteList.set(1, (byte) 15);
        assertEquals(2, arrayByteList.size());
        assertEquals(5, arrayByteList.get(0));
        assertEquals(15, arrayByteList.get(1));
    }

    @Test
    public void testRemoveElementAt() {
        arrayByteList.add((byte) 5);
        arrayByteList.add((byte) 10);
        arrayByteList.removeElementAt(0);
        assertEquals(1, arrayByteList.size());
        assertEquals(10, arrayByteList.get(0));
    }

    @Test
    public void testEnsureCapacity() {
        arrayByteList.ensureCapacity(10);
        assertEquals(0, arrayByteList.size());
    }

    @Test
    public void testTrimToSize() {
        arrayByteList.add((byte) 5);
        arrayByteList.add((byte) 10);
        arrayByteList.trimToSize();
        assertEquals(2, arrayByteList.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeCapacity() {
        new ArrayByteList(-5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithInvalidIndex() {
        arrayByteList.get(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithInvalidIndex() {
        arrayByteList.set(0, (byte) 5);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithInvalidIndex() {
        arrayByteList.add((byte) 5);
        arrayByteList.removeElementAt(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithInvalidIndex() {
        arrayByteList.add(1, (byte) 5);
    }

}

