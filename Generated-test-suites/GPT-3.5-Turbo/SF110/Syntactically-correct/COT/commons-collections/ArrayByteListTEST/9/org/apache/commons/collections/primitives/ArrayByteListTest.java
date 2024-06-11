
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ArrayByteListTest {
    
    @Test
    public void testConstructor1() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
    }

    @Test
    public void testConstructor2() {
        ArrayByteList list = new ArrayByteList(10);
        assertEquals(0, list.size());
    }

    @Test
    public void testConstructor3() {
        ArrayByteList list1 = new ArrayByteList();
        list1.add((byte) 10);
        list1.add((byte) 20);

        ArrayByteList list2 = new ArrayByteList(list1);
        
        assertEquals(2, list2.size());
        assertEquals(10, list2.get(0));
        assertEquals(20, list2.get(1));
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 10);
        assertEquals(10, list.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.get(0);
    }

    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 10);
        list.add((byte) 20);
        assertEquals(2, list.size());
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 10);
        list.add((byte) 20);
        list.add((byte) 30);
        assertEquals(20, list.removeElementAt(1));
        assertEquals(2, list.size());
        assertEquals(10, list.get(0));
        assertEquals(30, list.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.removeElementAt(0);
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 10);
        list.add((byte) 20);
        assertEquals(20, list.set(1, (byte) 30));
        assertEquals(2, list.size());
        assertEquals(10, list.get(0));
        assertEquals(30, list.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.set(0, (byte) 10);
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 10);
        list.add((byte) 20);
        
        assertEquals(2, list.size());
        assertEquals(10, list.get(0));
        assertEquals(20, list.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.add(1, (byte) 10);
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 10);
        list.add((byte) 20);
        list.ensureCapacity(8);

        assertEquals(2, list.size());
        assertEquals(10, list.get(0));
        assertEquals(20, list.get(1));
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList(10);
        list.add((byte) 10);
        list.add((byte) 20);
        list.trimToSize();

        assertEquals(2, list.size());
        assertEquals(10, list.get(0));
        assertEquals(20, list.get(1));
    }

    @Test
    public void testSerialization() {
        ArrayByteList list1 = new ArrayByteList();
        list1.add((byte) 10);
        list1.add((byte) 20);

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);

            oos.writeObject(list1);

            ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
            ObjectInputStream ois = new ObjectInputStream(bais);

            ArrayByteList list2 = (ArrayByteList) ois.readObject();

            assertEquals(2, list2.size());
            assertEquals(10, list2.get(0));
            assertEquals(20, list2.get(1));
        } catch(IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRange() {
        ArrayByteList list = new ArrayByteList();
        list.checkRange(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpoint() {
        ArrayByteList list = new ArrayByteList();
        list.checkRangeIncludingEndpoint(10);
    }
}
