import org.junit.*;
import static org.junit.Assert.*;

public class ArrayByteListTest {

    private ArrayByteList arrayByteList;

    @Before
    public void setUp() {
        arrayByteList = new ArrayByteList();
    }

    @Test
    public void testConstructor1() {
        assertEquals(0, arrayByteList.size());
    }

    @Test
    public void testConstructor2() {
        ArrayByteList arrayByteList = new ArrayByteList(5);
        assertEquals(0, arrayByteList.size());
    }

    @Test
    public void testConstructor3() {
        ByteCollection byteCollection = new ByteCollectionImpl();
        byteCollection.add((byte) 1);
        byteCollection.add((byte) 2);
        ByteList arrayByteList = new ArrayByteList(byteCollection);
        assertEquals(2, arrayByteList.size());
        assertEquals(1, arrayByteList.get(0));
        assertEquals(2, arrayByteList.get(1));
    }

    @Test
    public void testGet() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        assertEquals(1, arrayByteList.get(0));
        assertEquals(2, arrayByteList.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBounds() {
        arrayByteList.get(0);
    }

    @Test
    public void testSize() {
        assertEquals(0, arrayByteList.size());
    }

    @Test
    public void testRemoveElementAt() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        assertEquals(2, arrayByteList.size());
        assertEquals(1, arrayByteList.removeElementAt(0));
        assertEquals(1, arrayByteList.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtOutOfBounds() {
        arrayByteList.removeElementAt(0);
    }

    @Test
    public void testSet() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        assertEquals(2, arrayByteList.size());
        assertEquals(1, arrayByteList.set(0, (byte) 3));
        assertEquals(2, arrayByteList.size());
        assertEquals(3, arrayByteList.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetOutOfBounds() {
        arrayByteList.set(0, (byte) 1);
    }

    @Test
    public void testAdd() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        assertEquals(2, arrayByteList.size());
        assertEquals(1, arrayByteList.get(0));
        assertEquals(2, arrayByteList.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddOutOfBounds() {
        arrayByteList.add(1, (byte) 1);
    }

    @Test
    public void testEnsureCapacity() {
        arrayByteList.ensureCapacity(10);
        assertEquals(0, arrayByteList.size());
    }

    @Test
    public void testTrimToSize() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        arrayByteList.trimToSize();
        assertEquals(2, arrayByteList.size());
    }

    @Test
    public void testSerialize() throws Exception {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        ArrayByteList deserializedList = (ArrayByteList) SerializationUtils.clone(arrayByteList);
        assertEquals(2, deserializedList.size());
        assertEquals(1, deserializedList.get(0));
        assertEquals(2, deserializedList.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRange1() {
        arrayByteList.checkRange(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRange2() {
        arrayByteList.checkRange(1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpoint1() {
        arrayByteList.checkRangeIncludingEndpoint(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpoint2() {
        arrayByteList.checkRangeIncludingEndpoint(1);
    }

}