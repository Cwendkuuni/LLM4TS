import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {

    @Test
    public void testDefaultConstructor() {
        ArrayByteList arrayByteList = new ArrayByteList();
        assertNotNull(arrayByteList);
        assertEquals(0, arrayByteList.size());
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        ArrayByteList arrayByteList = new ArrayByteList(5);
        assertNotNull(arrayByteList);
        assertEquals(0, arrayByteList.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeInitialCapacity() {
        ArrayByteList arrayByteList = new ArrayByteList(-5);
    }

    @Test
    public void testConstructorWithByteCollection() {
        ByteCollection byteCollection = new ArrayByteCollection(new byte[]{1, 2, 3});
        ArrayByteList arrayByteList = new ArrayByteList(byteCollection);
        assertNotNull(arrayByteList);
        assertEquals(3, arrayByteList.size());
        assertEquals(1, arrayByteList.get(0));
        assertEquals(2, arrayByteList.get(1));
        assertEquals(3, arrayByteList.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBounds() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.get(0);
    }

    @Test
    public void testGet() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add(1);
        arrayByteList.add(2);
        arrayByteList.add(3);
        assertEquals(1, arrayByteList.get(0));
        assertEquals(2, arrayByteList.get(1));
        assertEquals(3, arrayByteList.get(2));
    }

    @Test
    public void testSize() {
        ArrayByteList arrayByteList = new ArrayByteList();
        assertEquals(0, arrayByteList.size());
        arrayByteList.add(1);
        assertEquals(1, arrayByteList.size());
        arrayByteList.add(2);
        assertEquals(2, arrayByteList.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtOutOfBounds() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.removeElementAt(0);
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add(1);
        arrayByteList.add(2);
        arrayByteList.add(3);
        byte removedElement = arrayByteList.removeElementAt(1);
        assertEquals(2, removedElement);
        assertEquals(2, arrayByteList.size());
        assertEquals(1, arrayByteList.get(0));
        assertEquals(3, arrayByteList.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetOutOfBounds() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.set(0, 1);
    }

    @Test
    public void testSet() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add(1);
        arrayByteList.add(2);
        arrayByteList.add(3);
        byte oldValue = arrayByteList.set(1, 5);
        assertEquals(2, oldValue);
        assertEquals(3, arrayByteList.size());
        assertEquals(1, arrayByteList.get(0));
        assertEquals(5, arrayByteList.get(1));
        assertEquals(3, arrayByteList.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddOutOfBounds() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add(1, 1);
    }

    @Test
    public void testAdd() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add(0, 1);
        arrayByteList.add(1, 2);
        arrayByteList.add(2, 3);
        assertEquals(3, arrayByteList.size());
        assertEquals(1, arrayByteList.get(0));
        assertEquals(2, arrayByteList.get(1));
        assertEquals(3, arrayByteList.get(2));
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.ensureCapacity(5);
        assertEquals(0, arrayByteList.size());
        assertEquals(8, arrayByteList.getCapacity());
        arrayByteList.add(1);
        arrayByteList.ensureCapacity(10);
        assertEquals(1, arrayByteList.size());
        assertEquals(16, arrayByteList.getCapacity());
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.ensureCapacity(10);
        assertEquals(0, arrayByteList.size());
        assertEquals(16, arrayByteList.getCapacity());
        arrayByteList.add(1);
        arrayByteList.trimToSize();
        assertEquals(1, arrayByteList.size());
        assertEquals(1, arrayByteList.getCapacity());
    }

    // Test serialization/deserialization
    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add(1);
        arrayByteList.add(2);
        arrayByteList.add(3);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(arrayByteList);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        ArrayByteList deserializedList = (ArrayByteList) objectInputStream.readObject();

        assertEquals(3, deserializedList.size());
        assertEquals(1, deserializedList.get(0));
        assertEquals(2, deserializedList.get(1));
        assertEquals(3, deserializedList.get(2));
    }

    private class ArrayByteCollection implements ByteCollection {
        private byte[] data;

        public ArrayByteCollection(byte[] data) {
            this.data = data;
        }

        @Override
        public int size() {
            return data.length;
        }

        @Override
        public boolean add(byte element) {
            return false;
        }
    }
}