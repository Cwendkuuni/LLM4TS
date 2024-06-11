import org.junit.Test;
import org.junit.Before;

public class ArrayByteListTest {

    private ArrayByteList arrayByteList;

    @Before
    public void setUp() {
        arrayByteList = new ArrayByteList();
    }

    // Test case for the constructor with no parameters
    @Test
    public void testArrayByteListNoParam() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
    }

    // Test case for the constructor with initial capacity
    @Test
    public void testArrayByteListWithParam() {
        ArrayByteList list = new ArrayByteList(10);
        assertEquals(0, list.size());
        // test if the initial capacity is set correctly
        assertTrue(list._data.length >= 10);
    }

    // Test case for the constructor with ByteCollection
    @Test
    public void testArrayByteListWithCollection() {
        ByteCollection collection = new ArrayByteList();
        collection.add(1);
        collection.add(2);

        ArrayByteList list = new ArrayByteList(collection);
        assertEquals(2, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
    }

    // Test case for get()
    @Test
    public void testGet() {
        arrayByteList.add(1);
        arrayByteList.add(2);
        arrayByteList.add(3);
        // test getting element at index 0
        assertEquals(1, arrayByteList.get(0));
        // test getting element at index 1
        assertEquals(2, arrayByteList.get(1));
        // test getting element at index 2
        assertEquals(3, arrayByteList.get(2));
    }

    // Test case for size()
    @Test
    public void testSize() {
        assertEquals(0, arrayByteList.size());
        arrayByteList.add(1);
        arrayByteList.add(2);
        assertEquals(2, arrayByteList.size());
        arrayByteList.removeElementAt(0);
        assertEquals(1, arrayByteList.size());
    }

    // Test case for removeElementAt()
    @Test
    public void testRemoveElementAt() {
        arrayByteList.add(1);
        arrayByteList.add(2);
        arrayByteList.add(3);
        // test removing element at index 1
        assertEquals(2, arrayByteList.removeElementAt(1));
        // test if element at index 1 is successfully removed
        assertEquals(1, arrayByteList.size());
        assertEquals(1, arrayByteList.get(0));
        assertEquals(3, arrayByteList.get(1));
    }

    // Test case for set()
    @Test
    public void testSet() {
        arrayByteList.add(1);
        arrayByteList.add(2);
        arrayByteList.add(3);
        // test setting element at index 1
        assertEquals(2, arrayByteList.set(1, 4));
        // test if element at index 1 is successfully set
        assertEquals(3, arrayByteList.size());
        assertEquals(1, arrayByteList.get(0));
        assertEquals(4, arrayByteList.get(1));
        assertEquals(3, arrayByteList.get(2));
    }

    // Test case for add()
    @Test
    public void testAdd() {
        arrayByteList.add(1);
        arrayByteList.add(2);
        arrayByteList.add(3);
        // test adding element at index 1
        arrayByteList.add(1, 4);
        // test if element is successfully added at index 1
        assertEquals(4, arrayByteList.size());
        assertEquals(1, arrayByteList.get(0));
        assertEquals(4, arrayByteList.get(1));
        assertEquals(2, arrayByteList.get(2));
        assertEquals(3, arrayByteList.get(3));
    }

    // Test case for ensureCapacity()
    @Test
    public void testEnsureCapacity() {
        arrayByteList.ensureCapacity(10);
        // test if the capacity is increased to at least 10
        assertTrue(arrayByteList._data.length >= 10);
    }

    // Test case for trimToSize()
    @Test
    public void testTrimToSize() {
        arrayByteList.ensureCapacity(10);
        arrayByteList.trimToSize();
        // test if the capacity is trimmed to the current size
        assertEquals(arrayByteList._size, arrayByteList._data.length);
    }

    // Test case for serialization
    @Test
    public void testSerialization() {
        arrayByteList.add(1);
        arrayByteList.add(2);
        arrayByteList.add(3);

        // test serialization
        ArrayByteList deserializedList = serializeAndDeserialize(arrayByteList);
        assertEquals(arrayByteList.size(), deserializedList.size());
        assertEquals(arrayByteList.get(0), deserializedList.get(0));
        assertEquals(arrayByteList.get(1), deserializedList.get(1));
        assertEquals(arrayByteList.get(2), deserializedList.get(2));
    }

    // Helper method to serialize and deserialize an object
    private ArrayByteList serializeAndDeserialize(ArrayByteList obj) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(obj);
            out.flush();
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream in = new ObjectInputStream(bis);
            return (ArrayByteList) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}