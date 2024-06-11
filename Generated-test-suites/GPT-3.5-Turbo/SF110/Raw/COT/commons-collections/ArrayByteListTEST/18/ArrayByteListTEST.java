
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.collections.primitives.*;

import static org.junit.Assert.*;

public class ArrayByteListTest {

    private ArrayByteList arrayByteList;

    @Before
    public void setUp() {
        arrayByteList = new ArrayByteList();
    }

    @Test
    public void testConstructorWithNoArguments() {
        assertEquals(0, arrayByteList.size());
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
        assertEquals(1, arrayByteList.get(0));
        assertEquals(2, arrayByteList.get(1));
    }

    @Test
    public void testGet() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        assertEquals(2, arrayByteList.get(1));
    }

    @Test
    public void testSize() {
        assertEquals(0, arrayByteList.size());
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        assertEquals(2, arrayByteList.size());
    }

    @Test
    public void testRemoveElementAt() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        arrayByteList.add((byte) 3);
        byte removedElement = arrayByteList.removeElementAt(1);
        assertEquals(2, removedElement);
        assertEquals(2, arrayByteList.size());
        assertEquals(1, arrayByteList.get(0));
        assertEquals(3, arrayByteList.get(1));
    }

    @Test
    public void testSet() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        byte previousElement = arrayByteList.set(1, (byte) 3);
        assertEquals(2, previousElement);
        assertEquals(2, arrayByteList.size());
        assertEquals(1, arrayByteList.get(0));
        assertEquals(3, arrayByteList.get(1));
    }

    @Test
    public void testAdd() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 3);
        arrayByteList.add(1, (byte) 2);
        assertEquals(3, arrayByteList.size());
        assertEquals(1, arrayByteList.get(0));
        assertEquals(2, arrayByteList.get(1));
        assertEquals(3, arrayByteList.get(2));
    }

    @Test
    public void testEnsureCapacity() {
        arrayByteList.ensureCapacity(10);
        assertEquals(0, arrayByteList.size());
        assertEquals(10, arrayByteList._data.length);
    }

    @Test
    public void testTrimToSize() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        arrayByteList.trimToSize();
        assertEquals(2, arrayByteList.size());
        assertEquals(2, arrayByteList._data.length);
    }

    // Test for writeObject and readObject are not included since these methods are for serialization

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeWithLowerBound() {
        arrayByteList.checkRange(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeWithUpperBound() {
        arrayByteList.add((byte) 1);
        arrayByteList.checkRange(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointWithLowerBound() {
        arrayByteList.checkRangeIncludingEndpoint(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointWithUpperBound() {
        arrayByteList.add((byte) 1);
        arrayByteList.checkRangeIncludingEndpoint(2);
    }
}

