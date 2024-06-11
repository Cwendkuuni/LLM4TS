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
    public void testConstructor() {
        ArrayByteList newArrayByteList = new ArrayByteList();
        assertNotNull(newArrayByteList);
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        ArrayByteList newArrayByteList = new ArrayByteList(10);
        assertNotNull(newArrayByteList);
    }

    @Test
    public void testConstructorWithByteCollection() {
        ArrayByteList anotherArrayByteList = new ArrayByteList(arrayByteList);
        assertNotNull(anotherArrayByteList);
    }

    @Test
    public void testGet() {
        arrayByteList.add(0, (byte) 10);
        assertEquals(10, arrayByteList.get(0));
    }

    @Test
    public void testSize() {
        assertEquals(0, arrayByteList.size());
    }

    @Test
    public void testRemoveElementAt() {
        arrayByteList.add(0, (byte) 10);
        byte removed = arrayByteList.removeElementAt(0);
        assertEquals(10, removed);
        assertEquals(0, arrayByteList.size());
    }

    @Test
    public void testSet() {
        arrayByteList.add(0, (byte) 10);
        byte oldVal = arrayByteList.set(0, (byte) 20);
        assertEquals(10, oldVal);
        assertEquals(20, arrayByteList.get(0));
    }

    @Test
    public void testAdd() {
        arrayByteList.add(0, (byte) 10);
        arrayByteList.add(1, (byte) 20);
        assertEquals(10, arrayByteList.get(0));
        assertEquals(20, arrayByteList.get(1));
        assertEquals(2, arrayByteList.size());
    }

    @Test
    public void testEnsureCapacity() {
        arrayByteList.ensureCapacity(20);
        assertEquals(20, arrayByteList._data.length);
    }

    @Test
    public void testTrimToSize() {
        arrayByteList.add(0, (byte) 10);
        arrayByteList.add(1, (byte) 20);
        arrayByteList.trimToSize();
        assertEquals(2, arrayByteList._data.length);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeCapacity() {
        ArrayByteList negativeArrayByteList = new ArrayByteList(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithInvalidIndex() {
        arrayByteList.get(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithInvalidIndex() {
        arrayByteList.removeElementAt(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithInvalidIndex() {
        arrayByteList.set(1, (byte) 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithInvalidIndex() {
        arrayByteList.add(1, (byte) 10);
    }
}