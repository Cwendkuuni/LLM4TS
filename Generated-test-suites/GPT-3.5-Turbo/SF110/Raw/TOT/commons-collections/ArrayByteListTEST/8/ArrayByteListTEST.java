import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {

    private ArrayByteList list;

    @Test
    public void testConstructor() {
        list = new ArrayByteList();
        assertEquals(0, list.size());
    }

    @Test
    public void testConstructorWithCapacity() {
        list = new ArrayByteList(10);
        assertEquals(0, list.size());
        assertEquals(10, list._data.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeCapacity() {
        list = new ArrayByteList(-1);
    }

    @Test
    public void testConstructorWithByteCollection() {
        ByteCollection byteList = new ArrayByteCollection();
        byteList.add((byte) 1);
        byteList.add((byte) 2);
        list = new ArrayByteList(byteList);
        assertEquals(2, list.size());
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfRange() {
        list = new ArrayByteList();
        list.get(0);
    }

    @Test
    public void testGet() {
        list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
    }

    @Test
    public void testSize() {
        list = new ArrayByteList();
        assertEquals(0, list.size());
        list.add((byte) 1);
        assertEquals(1, list.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtOutOfRange() {
        list = new ArrayByteList();
        list.removeElementAt(0);
    }

    @Test
    public void testRemoveElementAt() {
        list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        assertEquals(2, list.size());
        assertEquals((byte) 1, list.removeElementAt(0));
        assertEquals(1, list.size());
        assertEquals((byte) 2, list.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetOutOfRange() {
        list = new ArrayByteList();
        list.set(0, (byte) 1);
    }

    @Test
    public void testSet() {
        list = new ArrayByteList();
        list.add((byte) 1);
        assertEquals((byte) 1, list.set(0, (byte) 2));
        assertEquals((byte) 2, list.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddOutOfRange() {
        list = new ArrayByteList();
        list.add(-1, (byte) 1);
    }

    @Test
    public void testAdd() {
        list = new ArrayByteList();
        list.add((byte) 1);
        list.add(0, (byte) 2);
        assertEquals(2, list.size());
        assertEquals((byte) 2, list.get(0));
        assertEquals((byte) 1, list.get(1));
    }

    @Test
    public void testEnsureCapacity() {
        list = new ArrayByteList();
        list.ensureCapacity(10);
        assertEquals(10, list._data.length);
        list.add((byte) 1);
        list.ensureCapacity(1);
        assertEquals(10, list._data.length);
    }

    @Test
    public void testTrimToSize() {
        list = new ArrayByteList();
        list.ensureCapacity(10);
        list.trimToSize();
        assertEquals(0, list._data.length);
    }

    // Additional test cases for private methods

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeWithNegativeIndex() {
        list = new ArrayByteList();
        list.checkRange(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeWithOutOfRangeIndex() {
        list = new ArrayByteList();
        list.checkRange(0);
    }

    @Test
    public void testCheckRange() {
        list = new ArrayByteList();
        list.add((byte) 1);
        list.checkRange(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointWithNegativeIndex() {
        list = new ArrayByteList();
        list.checkRangeIncludingEndpoint(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointWithOutOfRangeIndex() {
        list = new ArrayByteList();
        list.checkRangeIncludingEndpoint(1);
    }

    @Test
    public void testCheckRangeIncludingEndpoint() {
        list = new ArrayByteList();
        list.checkRangeIncludingEndpoint(0);
        list.checkRangeIncludingEndpoint(1);
    }

    // Serialization tests

    @Test
    public void testSerialization() {
        list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(list);
            oos.close();

            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(baos.toByteArray()));
            ArrayByteList serializedList = (ArrayByteList) ois.readObject();
            ois.close();

            assertEquals(2, serializedList.size());
            assertEquals((byte) 1, serializedList.get(0));
            assertEquals((byte) 2, serializedList.get(1));
        } catch (IOException | ClassNotFoundException e) {
            fail("Serialization test failed: " + e.getMessage());
        }
    }
    
}