import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {

    //Test case for ArrayByteList()
    @Test
    public void testArrayByteList() {
        ArrayByteList byteList = new ArrayByteList();
        assertEquals(0, byteList.size());
        byteList.add((byte) 1);
        assertEquals(1, byteList.size());
        assertEquals((byte) 1, byteList.get(0));
    }

    //Test case for ArrayByteList(int initialCapacity)
    @Test
    public void testArrayByteListWithInitialCapacity() {
        ArrayByteList byteList = new ArrayByteList(5);
        assertEquals(0, byteList.size());
        byteList.add((byte) 1);
        assertEquals(1, byteList.size());
        assertEquals((byte) 1, byteList.get(0));
    }

    //Test case for ArrayByteList(ByteCollection that)
    @Test
    public void testArrayByteListWithByteCollection() {
        // Test with empty collection
        ByteCollection emptyCollection = new ArrayByteList();
        ArrayByteList byteList1 = new ArrayByteList(emptyCollection);
        assertEquals(0, byteList1.size());

        // Test with non-empty collection
        ByteCollection collection = new ArrayByteList();
        collection.add((byte) 1);
        ArrayByteList byteList2 = new ArrayByteList(collection);
        assertEquals(1, byteList2.size());
        assertEquals((byte) 1, byteList2.get(0));
    }

    //Test case for get(int index)
    @Test
    public void testGet() {
        ArrayByteList byteList = new ArrayByteList();
        byteList.add((byte) 1);
        assertEquals((byte) 1, byteList.get(0));

        // Test with invalid index
        try {
            byteList.get(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // pass
        }

        try {
            byteList.get(1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // pass
        }
    }

    //Test case for size()
    @Test
    public void testSize() {
        ArrayByteList byteList = new ArrayByteList();
        assertEquals(0, byteList.size());

        byteList.add((byte) 1);
        assertEquals(1, byteList.size());

        byteList.removeElementAt(0);
        assertEquals(0, byteList.size());
    }

    //Test case for removeElementAt(int index)
    @Test
    public void testRemoveElementAt() {
        ArrayByteList byteList = new ArrayByteList();
        byteList.add((byte) 1);
        assertEquals((byte) 1, byteList.removeElementAt(0));
        assertEquals(0, byteList.size());

        // Test with invalid index
        byteList.add((byte) 1);
        try {
            byteList.removeElementAt(-1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // pass
        }

        try {
            byteList.removeElementAt(1);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // pass
        }
    }

    //Test case for set(int index, byte element)
    @Test
    public void testSet() {
        ArrayByteList byteList = new ArrayByteList();
        byteList.add((byte) 1);
        assertEquals((byte) 1, byteList.set(0, (byte) 2));
        assertEquals((byte) 2, byteList.get(0));

        // Test with invalid index
        try {
            byteList.set(-1, (byte) 3);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // pass
        }

        try {
            byteList.set(1, (byte) 3);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // pass
        }
    }

    //Test case for add(int index, byte element)
    @Test
    public void testAdd() {
        ArrayByteList byteList = new ArrayByteList();
        byteList.add(0, (byte) 1);
        assertEquals(1, byteList.size());
        assertEquals((byte) 1, byteList.get(0));

        // Test adding at specific index
        byteList.add(0, (byte) 2);
        assertEquals(2, byteList.size());
        assertEquals((byte) 2, byteList.get(0));
        assertEquals((byte) 1, byteList.get(1));

        // Test adding at the end
        byteList.add(byteList.size(), (byte) 3);
        assertEquals(3, byteList.size());
        assertEquals((byte) 2, byteList.get(0));
        assertEquals((byte) 1, byteList.get(1));
        assertEquals((byte) 3, byteList.get(2));

        // Test with invalid index
        try {
            byteList.add(-1, (byte) 4);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // pass
        }

        try {
            byteList.add(4, (byte) 4);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // pass
        }
    }

    //Test case for ensureCapacity(int mincap)
    @Test
    public void testEnsureCapacity() {
        ArrayByteList byteList = new ArrayByteList();
        byteList.add((byte) 1);
        assertEquals(8, byteList._data.length);

        byteList.ensureCapacity(10);
        assertEquals(8, byteList._data.length);

        byteList.ensureCapacity(20);
        assertEquals(30, byteList._data.length);
    }

    //Test case for trimToSize()
    @Test
    public void testTrimToSize() {
        ArrayByteList byteList = new ArrayByteList();
        byteList.add((byte) 1);
        byteList.trimToSize();
        assertEquals(1, byteList._data.length);
        assertEquals((byte) 1, byteList.get(0));

        byteList.ensureCapacity(10);
        byteList.trimToSize();
        assertEquals(1, byteList._data.length);
        assertEquals((byte) 1, byteList.get(0));
    }

    //Test case for serialize and deserialize
    @Test
    public void testSerialization() {
        ArrayByteList byteList = new ArrayByteList();
        byteList.add((byte) 1);
        byteList.add((byte) 2);
        byteList.add((byte) 3);

        ArrayByteList deserializedList = (ArrayByteList) serializeAndDeserialize(byteList);
        assertEquals(3, deserializedList.size());
        assertEquals((byte) 1, deserializedList.get(0));
        assertEquals((byte) 2, deserializedList.get(1));
        assertEquals((byte) 3, deserializedList.get(2));

    }

    // Helper method to serialize and deserialize an object
    private Object serializeAndDeserialize(Object object) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(object);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}