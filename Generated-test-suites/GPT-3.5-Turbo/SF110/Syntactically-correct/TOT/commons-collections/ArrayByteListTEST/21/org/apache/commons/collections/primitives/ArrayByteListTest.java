import static org.junit.Assert.*;
import org.junit.*;
import org.apache.commons.collections.primitives.*;

public class ArrayByteListTest {

    private ArrayByteList list;

    @Before
    public void setUp() {
        list = new ArrayByteList();
    }

    @Test
    public void testConstructorWithNoArgument() {
        assertEquals(0, list.size());
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        ArrayByteList list = new ArrayByteList(10);
        assertEquals(0, list.size());
        assertEquals(10, list._data.length);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeCapacity() {
        ArrayByteList list = new ArrayByteList(-10);
    }

    @Test
    public void testConstructorWithByteCollection() {
        ByteArrayList byteList = new ByteArrayList();
        byteList.add((byte) 1);
        byteList.add((byte) 2);
        byteList.add((byte) 3);
        ArrayByteList list = new ArrayByteList(byteList);
        assertEquals(3, list.size());
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 2, list.get(1));
        assertEquals((byte) 3, list.get(2));
    }

    @Test
    public void testGet() {
        list.add((byte) 1);
        assertEquals((byte) 1, list.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetIndexOutOfBounds() {
        list.get(0);
    }

    @Test
    public void testSize() {
        list.add((byte) 1);
        list.add((byte) 2);
        assertEquals(2, list.size());
    }

    @Test
    public void testRemoveElementAt() {
        list.add((byte) 1);
        list.add((byte) 2);
        byte removed = list.removeElementAt(0);
        assertEquals(1, list.size());
        assertEquals((byte) 1, removed);
        assertEquals((byte) 2, list.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtIndexOutOfBounds() {
        list.removeElementAt(0);
    }

    @Test
    public void testSet() {
        list.add((byte) 1);
        byte oldVal = list.set(0, (byte) 2);
        assertEquals(1, list.size());
        assertEquals((byte) 1, oldVal);
        assertEquals((byte) 2, list.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetIndexOutOfBounds() {
        list.set(0, (byte) 1);
    }

    @Test
    public void testAdd() {
        list.add((byte) 1);
        list.add((byte) 2);
        list.add(1, (byte) 3);
        assertEquals(3, list.size());
        assertEquals((byte) 1, list.get(0));
        assertEquals((byte) 3, list.get(1));
        assertEquals((byte) 2, list.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddIndexOutOfBounds() {
        list.add(1, (byte) 1);
    }

    @Test
    public void testEnsureCapacity() {
        list.ensureCapacity(20);
        assertEquals(20, list._data.length);
    }

    @Test
    public void testEnsureCapacityWithCurrentCapacity() {
        list.add((byte) 1);
        list.ensureCapacity(10);
        assertEquals(8, list._data.length);
    }

    @Test
    public void testTrimToSize() {
        list.ensureCapacity(20);
        list.trimToSize();
        assertEquals(0, list._data.length);
    }

    @Test
    public void testSerializationAndDeserialization() {
        list.add((byte) 1);
        list.add((byte) 2);
        ArrayByteList deserialized = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(list);
            oos.flush();
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bis);
            deserialized = (ArrayByteList) ois.readObject();
        } catch (Exception e) {
            fail("Exception occurred during serialization and deserialization: " + e.getMessage());
        }
        assertNotNull(deserialized);
        assertEquals(list.size(), deserialized.size());
        for (int i = 0; i < list.size(); i++) {
            assertEquals(list.get(i), deserialized.get(i));
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeWithNegativeIndex() {
        list.checkRange(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeWithOutOfBoundsIndex() {
        list.checkRange(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointWithNegativeIndex() {
        list.checkRangeIncludingEndpoint(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointWithOutOfBoundsIndex() {
        list.checkRangeIncludingEndpoint(2);
    }

}