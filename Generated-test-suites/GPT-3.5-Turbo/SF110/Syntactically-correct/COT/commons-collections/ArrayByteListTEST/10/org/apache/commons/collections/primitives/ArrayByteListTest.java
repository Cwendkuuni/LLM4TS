
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {

    @Test
    public void testDefaultConstructor() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        ArrayByteList list = new ArrayByteList(10);
        assertEquals(0, list.size());
    }

    @Test
    public void testConstructorWithByteCollection() {
        ByteCollection collection = new ArrayByteList();
        ArrayByteList list = new ArrayByteList(collection);
        assertEquals(0, list.size());
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 10);
        assertEquals(10, list.get(0));
    }

    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        list.add((byte) 10);
        assertEquals(1, list.size());
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 10);
        assertEquals(10, list.removeElementAt(0));
        assertEquals(0, list.size());
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 10);
        assertEquals(10, list.set(0, (byte) 20));
        assertEquals(20, list.get(0));
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 10);
        assertEquals(10, list.get(0));
        list.add(1, (byte) 20);
        assertEquals(20, list.get(1));
        assertEquals(2, list.size());
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();
        list.ensureCapacity(10);
        // No assertion, as it will resize the internal array if needed
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 10);
        list.trimToSize();
        assertEquals(1, list.size());
    }

    // Additional JUnit tests for edge cases and exception handling:

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalCapacityConstructor() {
        new ArrayByteList(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfRangeLower() {
        ArrayByteList list = new ArrayByteList();
        list.get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfRangeUpper() {
        ArrayByteList list = new ArrayByteList();
        list.get(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtOutOfRangeLower() {
        ArrayByteList list = new ArrayByteList();
        list.removeElementAt(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtOutOfRangeUpper() {
        ArrayByteList list = new ArrayByteList();
        list.removeElementAt(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetOutOfRangeLower() {
        ArrayByteList list = new ArrayByteList();
        list.set(-1, (byte) 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetOutOfRangeUpper() {
        ArrayByteList list = new ArrayByteList();
        list.set(0, (byte) 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddOutOfRangeLower() {
        ArrayByteList list = new ArrayByteList();
        list.add(-1, (byte) 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddOutOfRangeUpper() {
        ArrayByteList list = new ArrayByteList();
        list.add(1, (byte) 10);
    }
    
    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 10);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(list);
        oos.close();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        ArrayByteList deserializedList = (ArrayByteList) ois.readObject();
        ois.close();
        
        assertEquals(1, deserializedList.size());
        assertEquals(10, deserializedList.get(0));
    }
}

