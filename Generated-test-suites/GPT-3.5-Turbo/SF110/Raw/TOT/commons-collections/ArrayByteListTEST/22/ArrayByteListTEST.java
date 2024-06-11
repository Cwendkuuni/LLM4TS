import static org.junit.Assert.*;
import org.junit.*;

public class ArrayByteListTest {

    private ArrayByteList byteList;

    @Before
    public void setUp() {
        byteList = new ArrayByteList();
    }

    @Test
    public void testArrayByteList() {
        assertNotNull(byteList);
        assertEquals(8, byteList._data.length);
        assertEquals(0, byteList._size);
    }

    @Test
    public void testArrayByteListWithInitialCapacity() {
        ArrayByteList byteList = new ArrayByteList(10);
        assertNotNull(byteList);
        assertEquals(10, byteList._data.length);
        assertEquals(0, byteList._size);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArrayByteListWithNegativeInitialCapacity() {
        new ArrayByteList(-1);
    }

    @Test
    public void testArrayByteListWithByteCollection() {
        ByteCollection byteCollection = new ArrayByteList();
        byteCollection.add((byte) 1);
        byteCollection.add((byte) 2);
        byteCollection.add((byte) 3);
        ArrayByteList byteList = new ArrayByteList(byteCollection);
        assertNotNull(byteList);
        assertEquals(3, byteList._data.length);
        assertEquals(3, byteList._size);
        assertEquals((byte) 1, byteList.get(0));
        assertEquals((byte) 2, byteList.get(1));
        assertEquals((byte) 3, byteList.get(2));
    }

    @Test
    public void testGet() {
        byteList.add((byte) 5);
        assertEquals((byte) 5, byteList.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithInvalidIndex() {
        byteList.get(0);
    }

    @Test
    public void testSize() {
        assertEquals(0, byteList.size());
        byteList.add((byte) 1);
        assertEquals(1, byteList.size());
    }

    @Test
    public void testRemoveElementAt() {
        byteList.add((byte) 5);
        byteList.add((byte) 6);
        assertEquals((byte) 5, byteList.removeElementAt(0));
        assertEquals(1, byteList.size());
        assertEquals((byte) 6, byteList.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithInvalidIndex() {
        byteList.removeElementAt(0);
    }

    @Test
    public void testSet() {
        byteList.add((byte) 5);
        assertEquals((byte) 5, byteList.set(0, (byte) 10));
        assertEquals((byte) 10, byteList.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithInvalidIndex() {
        byteList.set(0, (byte) 10);
    }

    @Test
    public void testAdd() {
        byteList.add((byte) 5);
        assertEquals(1, byteList.size());
        assertEquals((byte) 5, byteList.get(0));
    }

    @Test
    public void testAddWithIndex() {
        byteList.add((byte) 5);
        byteList.add((byte) 6);
        byteList.add(0, (byte) 10);
        assertEquals(3, byteList.size());
        assertEquals((byte) 10, byteList.get(0));
        assertEquals((byte) 5, byteList.get(1));
        assertEquals((byte) 6, byteList.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithInvalidIndex() {
        byteList.add(1, (byte) 10);
    }

    @Test
    public void testEnsureCapacity() {
        byteList.ensureCapacity(10);
        assertEquals(8, byteList._data.length);
        byteList.ensureCapacity(15);
        assertEquals(23, byteList._data.length);
    }

    @Test
    public void testTrimToSize() {
        byteList.ensureCapacity(23);
        byteList.add((byte) 5);
        byteList.trimToSize();
        assertEquals(1, byteList._data.length);
        assertEquals((byte) 5, byteList.get(0));
    }

    // Additional tests for private methods and serialization/deserialization can be added here

}