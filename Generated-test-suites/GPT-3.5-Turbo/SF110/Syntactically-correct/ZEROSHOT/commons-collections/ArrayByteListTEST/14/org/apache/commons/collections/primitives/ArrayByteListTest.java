import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertArrayEquals;

public class ArrayByteListTest {

    @Test
    public void testConstructor1() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
    }

    @Test
    public void testConstructor2() {
        ArrayByteList list = new ArrayByteList(8);
        assertEquals(0, list.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor2WithNegativeCapacity() {
        ArrayByteList list = new ArrayByteList(-1);
    }

    @Test
    public void testConstructor3() {
        ByteCollection collection = new ArrayByteList();
        collection.add((byte) 1);
        collection.add((byte) 2);
        collection.add((byte) 3);

        ArrayByteList list = new ArrayByteList(collection);

        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.get(0);
    }

    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());

        list.add((byte) 1);
        assertEquals(1, list.size());

        list.add((byte) 2);
        assertEquals(2, list.size());
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        byte removed = list.removeElementAt(1);

        assertEquals(2, removed);
        assertEquals(2, list.size());
        assertEquals(1, list.get(0));
        assertEquals(3, list.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.removeElementAt(0);
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        byte old = list.set(1, (byte) 5);

        assertEquals(2, old);
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(5, list.get(1));
        assertEquals(3, list.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.set(0, (byte) 1);
    }

    @Test
    public void testAdd() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 1);

        assertEquals(1, list.size());
        assertEquals(1, list.get(0));

        list.add(1, (byte) 2);
        assertEquals(2, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));

        list.add(1, (byte) 3);
        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(3, list.get(1));
        assertEquals(2, list.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddOutOfBounds() {
        ArrayByteList list = new ArrayByteList();
        list.add(1, (byte) 1);
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList list = new ArrayByteList(2);
        list.add((byte) 1);
        list.add((byte) 2);

        list.ensureCapacity(5);

        assertEquals(2, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList list = new ArrayByteList(5);
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        list.trimToSize();

        assertEquals(3, list.size());
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        ArrayByteList list1 = new ArrayByteList();
        list1.add((byte) 1);
        list1.add((byte) 2);
        list1.add((byte) 3);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        out.writeObject(list1);
        out.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bais);
        ArrayByteList list2 = (ArrayByteList) in.readObject();
        in.close();

        assertEquals(list1.size(), list2.size());
        for (int i = 0; i < list1.size(); i++) {
            assertEquals(list1.get(i), list2.get(i));
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRange() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        list.checkRange(-1);
        list.checkRange(list.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpoint() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        list.add((byte) 3);

        list.checkRangeIncludingEndpoint(-1);
        list.checkRangeIncludingEndpoint(list.size() + 1);
    }

}