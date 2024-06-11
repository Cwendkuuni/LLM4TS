import org.junit.*;
import static org.junit.Assert.*;

public class ArrayByteListTest {

    @Test
    public void testConstructor() {
        ArrayByteList arrayByteList = new ArrayByteList();
        assertNotNull(arrayByteList);
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        int initialCapacity = 10;
        ArrayByteList arrayByteList = new ArrayByteList(initialCapacity);
        assertNotNull(arrayByteList);
    }

    @Test
    public void testConstructorWithByteCollection() {
        ByteCollection byteCollection = new ByteArrayList();
        ArrayByteList arrayByteList = new ArrayByteList(byteCollection);
        assertNotNull(arrayByteList);
    }

    @Test
    public void testGet() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add((byte) 1);
        byte element = arrayByteList.get(0);
        assertEquals((byte) 1, element);
    }

    @Test
    public void testSize() {
        ArrayByteList arrayByteList = new ArrayByteList();
        int size = arrayByteList.size();
        assertEquals(0, size);
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add((byte) 1);
        byte removedElement = arrayByteList.removeElementAt(0);
        assertEquals((byte) 1, removedElement);
        assertEquals(0, arrayByteList.size());
    }

    @Test
    public void testSet() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add((byte) 1);
        byte oldElement = arrayByteList.set(0, (byte) 2);
        assertEquals((byte) 1, oldElement);
        assertEquals((byte) 2, arrayByteList.get(0));
    }

    @Test
    public void testAdd() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add(0, (byte) 1);
        assertEquals((byte) 1, arrayByteList.get(0));
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.ensureCapacity(10);
        assertEquals(10, arrayByteList.getCapacity());
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.ensureCapacity(10);
        arrayByteList.trimToSize();
        assertEquals(0, arrayByteList.getCapacity());
    }

    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(arrayByteList);
        out.close();

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bis);
        ArrayByteList serializedList = (ArrayByteList) in.readObject();
        in.close();

        assertEquals(arrayByteList.size(), serializedList.size());
        assertEquals(arrayByteList.get(0), serializedList.get(0));
        assertEquals(arrayByteList.get(1), serializedList.get(1));
    }

    private ArrayByteList createTestList() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        arrayByteList.add((byte) 3);
        arrayByteList.add((byte) 4);
        arrayByteList.add((byte) 5);
        return arrayByteList;
    }
}