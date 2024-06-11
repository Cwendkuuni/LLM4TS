import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

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
        ArrayByteList listWithCapacity = new ArrayByteList(10);
        assertNotNull(listWithCapacity);
        assertEquals(0, listWithCapacity.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeInitialCapacity() {
        ArrayByteList listWithNegativeCapacity = new ArrayByteList(-1);
    }

    @Test
    public void testConstructorWithByteCollection() {
        ByteCollection collection = new ArrayByteList(5);
        collection.add((byte) 1);
        collection.add((byte) 2);

        ArrayByteList listWithCollection = new ArrayByteList(collection);
        assertNotNull(listWithCollection);
        assertEquals(2, listWithCollection.size());
    }

    @Test
    public void testGet() {
        list.add((byte) 1);
        list.add((byte) 2);

        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithInvalidIndex() {
        list.get(0);
    }

    @Test
    public void testSize() {
        assertEquals(0, list.size());

        list.add((byte) 1);
        list.add((byte) 2);
        
        assertEquals(2, list.size());

        list.removeElementAt(1);

        assertEquals(1, list.size());
    }

    @Test
    public void testRemoveElementAt() {
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        byte removed = list.removeElementAt(1);
        assertEquals((byte) 2, removed);
        assertEquals(2, list.size());
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 3, list.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithInvalidIndex() {
        list.removeElementAt(0);
    }

    @Test
    public void testSet() {
        list.add((byte) 1);
        list.add((byte) 2);

        byte old = list.set(1, (byte) 3);
        assertEquals((byte) 2, old);
        assertEquals((byte) 3, list.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithInvalidIndex() {
        list.set(0, (byte) 1);
    }

    @Test
    public void testAdd() {
        list.add(0, (byte) 1);
        list.add(1, (byte) 2);
        list.add(0, (byte) 3);
        list.add(2, (byte) 4);

        assertEquals(4, list.size());
        assertEquals((byte) 3, list.get(0));
        assertEquals((byte) 1, list.get(1));
        assertEquals((byte) 4, list.get(2));
        assertEquals((byte) 2, list.get(3));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithInvalidIndex() {
        list.add(1, (byte) 1);
    }

    @Test
    public void testEnsureCapacity() {
        list.ensureCapacity(5);
        assertTrue(list.size() == 0);
        assertEquals(8, list._data.length);

        list.ensureCapacity(10);
        assertTrue(list.size() == 0);
        assertEquals(15, list._data.length);
    }

    @Test
    public void testTrimToSize() {
        list.ensureCapacity(10);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);
        
        assertEquals(10, list._data.length);

        list.trimToSize();

        assertEquals(3, list.size());
        assertEquals(3, list._data.length);
    }

    @Test
    public void testWriteObject() {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream;
        byte[] expected = {1, 2, 3};

        try {
            objectStream = new ObjectOutputStream(byteStream);
            objectStream.writeObject(list);

            objectStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        assertArrayEquals(expected, byteStream.toByteArray());
    }

    @Test
    public void testReadObject() {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream;
        byte[] expectedBytes = {1, 2, 3};

        try {
            objectStream = new ObjectOutputStream(byteStream);
            objectStream.writeObject(expectedBytes);
            objectStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayInputStream input = new ByteArrayInputStream(byteStream.toByteArray());
        ObjectInputStream objectInput;

        try {
            objectInput = new ObjectInputStream(input);
            byte[] actualBytes = (byte[]) objectInput.readObject();

            assertArrayEquals(expectedBytes, actualBytes);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCheckRange() {
        list.add((byte) 1);
        list.add((byte) 2);

        list.checkRange(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeWithLowerOutOfBounds() {
        list.add((byte) 1);
        list.add((byte) 2);

        list.checkRange(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeWithUpperOutOfBounds() {
        list.add((byte) 1);
        list.add((byte) 2);

        list.checkRange(2);
    }
}