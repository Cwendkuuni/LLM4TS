
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class ArrayByteListTest {

    private ArrayByteList arrayByteList;

    @Before
    public void setUp() {
        arrayByteList = new ArrayByteList();
    }

    @Test
    public void testConstructorWithNoArgument() {
        assertEquals(0, arrayByteList.size());
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        ArrayByteList list = new ArrayByteList(10);
        assertEquals(0, list.size());
    }

    @Test
    public void testConstructorWithByteCollection() {
        ArrayByteList inputList = new ArrayByteList();
        inputList.add((byte) 1);
        inputList.add((byte) 2);
        ArrayByteList list = new ArrayByteList(inputList);
        assertEquals(2, list.size());
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
    }

    @Test
    public void testGet() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        assertEquals((byte) 1, arrayByteList.get(0));
        assertEquals((byte) 2, arrayByteList.get(1));
    }

    @Test
    public void testRemoveElementAt() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        byte removedElement = arrayByteList.removeElementAt(0);
        assertEquals(1, arrayByteList.size());
        assertEquals((byte) 2, arrayByteList.get(0));
        assertEquals((byte) 1, removedElement);
    }

    @Test
    public void testSet() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        byte oldElement = arrayByteList.set(0, (byte) 3);
        assertEquals((byte) 3, arrayByteList.get(0));
        assertEquals((byte) 1, oldElement);
    }

    @Test
    public void testAdd() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        arrayByteList.add(0, (byte) 3);
        assertEquals(3, arrayByteList.size());
        assertEquals((byte) 3, arrayByteList.get(0));
        assertEquals((byte) 1, arrayByteList.get(1));
        assertEquals((byte) 2, arrayByteList.get(2));
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
    }

    @Test
    public void testCheckRangeWithValidIndex() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        arrayByteList.checkRange(1);
    }

    @Test
    public void testCheckRangeWithInvalidIndex() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        assertThrows(IndexOutOfBoundsException.class, () -> arrayByteList.checkRange(2));
    }

    @Test
    public void testCheckRangeIncludingEndpointWithValidIndex() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        arrayByteList.checkRangeIncludingEndpoint(2);
    }

    @Test
    public void testCheckRangeIncludingEndpointWithInvalidIndex() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        assertThrows(IndexOutOfBoundsException.class, () -> arrayByteList.checkRangeIncludingEndpoint(-1));
    }
}
