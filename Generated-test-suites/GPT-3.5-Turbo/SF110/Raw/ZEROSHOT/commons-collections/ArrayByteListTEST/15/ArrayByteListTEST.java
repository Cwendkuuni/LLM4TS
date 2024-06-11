import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {

    private ArrayByteList list;

    @Before
    public void setUp() {
        list = new ArrayByteList();
    }

    @Test
    public void testConstructorWithoutCapacity() {
        assertEquals(0, list.size());
    }

    @Test
    public void testConstructorWithCapacity() {
        ArrayByteList list = new ArrayByteList(10);
        assertEquals(0, list.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeCapacity() {
        new ArrayByteList(-10);
    }

    @Test
    public void testConstructorWithByteCollection() {
        ByteCollection collection = new ArrayByteList();
        collection.add((byte) 1);
        collection.add((byte) 2);
        collection.add((byte) 3);

        ArrayByteList list = new ArrayByteList(collection);

        assertEquals(3, list.size());
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 3, list.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetIndexOutOfBounds() {
        list.get(0);
    }

    @Test
    public void testGet() {
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 3, list.get(2));
    }

    @Test
    public void testSize() {
        assertEquals(0, list.size());

        list.add((byte) 1);
        list.add((byte) 2);

        assertEquals(2, list.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtIndexOutOfBounds() {
        list.removeElementAt(0);
    }

    @Test
    public void testRemoveElementAt() {
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        assertEquals((byte) 2, list.removeElementAt(1));
        assertEquals(2, list.size());
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 3, list.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetIndexOutOfBounds() {
        list.set(0, (byte) 1);
    }

    @Test
    public void testSet() {
        list.add((byte) 1);
        list.add((byte) 2);

        assertEquals((byte) 1, list.set(0, (byte) 3));
        assertEquals(2, list.size());
        assertEquals((byte) 3, list.get(0));
        assertEquals((byte) 2, list.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddIndexOutOfBounds() {
        list.add(1, (byte) 1);
    }

    @Test
    public void testAdd() {
        list.add((byte) 1);
        list.add((byte) 3);

        list.add(1, (byte) 2);
        assertEquals(3, list.size());
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 3, list.get(2));
    }

    @Test
    public void testEnsureCapacity() {
        list.ensureCapacity(10);
        assertEquals(0, list.size());
    }

    @Test
    public void testTrimToSize() {
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        list.trimToSize();
        assertEquals(3, list.size());
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 3, list.get(2));
    }

    @Test
    public void testSerialization() throws Exception {
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        ArrayByteList deserializedList = (ArrayByteList) SerializationUtils.deserialize(SerializationUtils.serialize(list));

        assertEquals(list.size(), deserializedList.size());

        for (int i = 0; i < list.size(); i++) {
            assertEquals(list.get(i), deserializedList.get(i));
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIndexOutOfBounds() {
        list.checkRange(0);
    }

    @Test
    public void testCheckRange() {
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        list.checkRange(2);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointIndexOutOfBounds() {
        list.checkRangeIncludingEndpoint(3);
    }

    @Test
    public void testCheckRangeIncludingEndpoint() {
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        list.checkRangeIncludingEndpoint(3);
    }
}