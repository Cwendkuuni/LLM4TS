
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {

    @Test
    public void testConstructor() {
        ArrayByteList list = new ArrayByteList();
        assertNotNull(list);
    }

    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
    }

    @Test
    public void testAddAndGet() {
        ArrayByteList list = new ArrayByteList();
        list.add(1);
        assertEquals(1, list.size());
        assertEquals(1, list.get(0));
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add(1);
        list.add(2);
        list.add(3);
        assertEquals(3, list.size());
        assertEquals(2, list.removeElementAt(1));
        assertEquals(2, list.size());
        assertEquals(3, list.get(1));
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add(1);
        list.add(2);
        list.add(3);
        assertEquals(3, list.size());
        assertEquals(2, list.set(1, 4));
        assertEquals(3, list.size());
        assertEquals(4, list.get(1));
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList();
        list.ensureCapacity(10);
        assertEquals(0, list.size());
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList();
        list.add(1);
        list.add(2);
        list.add(3);
        list.trimToSize();
        assertEquals(3, list.size());
    }
    
    // Additional test cases for edge cases and exceptions
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeCapacity() {
        ArrayByteList list = new ArrayByteList(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.get(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithIndexOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.get(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.removeElementAt(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithIndexOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.removeElementAt(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.set(-1, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithIndexOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.set(0, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithNegativeIndex() {
        ArrayByteList list = new ArrayByteList();
        list.add(-1, 0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithIndexOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.add(1, 0);
    }

    @Test
    public void testSerialization() throws Exception {
        ArrayByteList list = new ArrayByteList();
        list.add(1);
        list.add(2);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        out.writeObject(list);
        out.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bais);
        ArrayByteList deserializedList = (ArrayByteList) in.readObject();
        in.close();

        assertEquals(list.size(), deserializedList.size());
        for (int i = 0; i < list.size(); i++) {
            assertEquals(list.get(i), deserializedList.get(i));
        }
    }

}

