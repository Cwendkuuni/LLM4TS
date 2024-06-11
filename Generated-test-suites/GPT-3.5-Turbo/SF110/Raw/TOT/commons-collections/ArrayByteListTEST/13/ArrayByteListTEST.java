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
    public void testConstructor1() {
        assertEquals(8, arrayByteList.size());
    }

    @Test
    public void testConstructor2() {
        ArrayByteList arrayByteList1 = new ArrayByteList(10);
        assertEquals(10, arrayByteList1.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor2WithNegativeCapacity() {
        ArrayByteList arrayByteList1 = new ArrayByteList(-1);
    }

    @Test
    public void testConstructor3() {
        ByteCollection byteCollection = new ByteCollection();
        byteCollection.add((byte) 1);
        byteCollection.add((byte) 2);
        ArrayByteList arrayByteList1 = new ArrayByteList(byteCollection);
        assertEquals(2, arrayByteList1.size());
    }

    @Test
    public void testGet() {
        arrayByteList.add(0, (byte) 1);
        assertEquals(1, arrayByteList.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithInvalidIndex() {
        arrayByteList.get(-1);
    }

    @Test
    public void testSize() {
        assertEquals(0, arrayByteList.size());
    }

    @Test
    public void testRemoveElementAt() {
        arrayByteList.add(0, (byte) 1);
        byte removedElement = arrayByteList.removeElementAt(0);
        assertEquals(1, removedElement);
        assertEquals(0, arrayByteList.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithInvalidIndex() {
        arrayByteList.removeElementAt(0);
    }

    @Test
    public void testSet() {
        arrayByteList.add(0, (byte) 1);
        byte oldElement = arrayByteList.set(0, (byte) 2);
        assertEquals(1, oldElement);
        assertEquals(2, arrayByteList.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithInvalidIndex() {
        arrayByteList.set(0, (byte) 1);
    }

    @Test
    public void testAdd() {
        arrayByteList.add(0, (byte) 1);
        assertEquals(1, arrayByteList.get(0));
        assertEquals(1, arrayByteList.size());
    }

    @Test
    public void testAddToEnd() {
        arrayByteList.add(0, (byte) 1);
        arrayByteList.add(1, (byte) 2);
        assertEquals(2, arrayByteList.get(1));
        assertEquals(2, arrayByteList.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithInvalidIndex() {
        arrayByteList.add(-1, (byte) 1);
    }

    @Test
    public void testEnsureCapacity() {
        arrayByteList.ensureCapacity(10);
        assertEquals(0, arrayByteList.size());
    }

    @Test
    public void testTrimToSize() {
        arrayByteList.add(0, (byte) 1);
        arrayByteList.trimToSize();
        assertEquals(1, arrayByteList.size());
    }

    @Test
    public void testWriteObject() {
        // Test is not possible as ObjectOutputStream is not directly used
    }

    @Test
    public void testReadObject() {
        // Test is not possible as ObjectInputStream is not directly used
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeWithInvalidIndex() {
        arrayByteList.checkRange(-1);
    }

    @Test
    public void testCheckRangeIncludingEndpoint() {
        arrayByteList.add(0, (byte) 1);
        arrayByteList.checkRangeIncludingEndpoint(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointWithInvalidIndex() {
        arrayByteList.checkRangeIncludingEndpoint(2);
    }
}