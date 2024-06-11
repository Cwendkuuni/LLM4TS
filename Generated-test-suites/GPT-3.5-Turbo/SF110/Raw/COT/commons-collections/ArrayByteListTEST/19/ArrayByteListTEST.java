import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;

public class ArrayByteListTest {

    private ArrayByteList arrayByteList;

    @Before
    public void setUp() {
        arrayByteList = new ArrayByteList();
    }

    @Test
    public void testConstructor() {
        assertNotNull(arrayByteList);
    }

    @Test
    public void testSize() {
        assertEquals(0, arrayByteList.size());

        arrayByteList.add((byte) 5);
        assertEquals(1, arrayByteList.size());

        arrayByteList.add((byte) 10);
        assertEquals(2, arrayByteList.size());
    }

    @Test
    public void testGet() {
        arrayByteList.add((byte) 5);
        arrayByteList.add((byte) 10);

        assertEquals(5, arrayByteList.get(0));
        assertEquals(10, arrayByteList.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBounds() {
        arrayByteList.get(0);
    }

    @Test
    public void testRemoveElementAt() {
        arrayByteList.add((byte) 5);
        arrayByteList.add((byte) 10);

        assertEquals(10, arrayByteList.removeElementAt(1));
        assertEquals(1, arrayByteList.size());
        assertEquals(5, arrayByteList.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtOutOfBounds() {
        arrayByteList.removeElementAt(0);
    }

    @Test
    public void testSet() {
        arrayByteList.add((byte) 5);
        arrayByteList.add((byte) 10);

        assertEquals(10, arrayByteList.set(1, (byte) 20));
        assertEquals(20, arrayByteList.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetOutOfBounds() {
        arrayByteList.set(0, (byte) 5);
    }

    @Test
    public void testAdd() {
        arrayByteList.add((byte) 5);
        arrayByteList.add((byte) 10);

        assertEquals(5, arrayByteList.get(0));
        assertEquals(10, arrayByteList.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddOutOfBounds() {
        arrayByteList.add(1, (byte) 5);
    }

    @Test
    public void testEnsureCapacity() {
        arrayByteList.ensureCapacity(5);

        assertEquals(5, arrayByteList._data.length);
    }

    @Test
    public void testTrimToSize() {
        arrayByteList.add((byte) 5);
        arrayByteList.add((byte) 10);
        arrayByteList.ensureCapacity(5);

        arrayByteList.trimToSize();

        assertEquals(2, arrayByteList._data.length);
    }

    @Test
    public void testSerialization() throws Exception {
        arrayByteList.add((byte) 5);
        arrayByteList.add((byte) 10);

        ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("test.dat"));
        oos.writeObject(arrayByteList);
        oos.close();

        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("test.dat"));
        ArrayByteList deserializedList = (ArrayByteList) ois.readObject();
        ois.close();

        assertEquals(2, deserializedList.size());
        assertEquals(5, deserializedList.get(0));
        assertEquals(10, deserializedList.get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeInitialCapacity() {
        new ArrayByteList(-1);
    }
}

