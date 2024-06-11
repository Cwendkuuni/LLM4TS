import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ArrayByteListTest {

    private ArrayByteList arrayByteList;

    @Before
    public void setUp() {
        arrayByteList = new ArrayByteList();
    }

    @Test
    public void testConstructorWithNoArguments() {
        assertEquals(0, arrayByteList.size());
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        ArrayByteList arrayByteList = new ArrayByteList(5);
        assertEquals(0, arrayByteList.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeCapacity() {
        ArrayByteList arrayByteList = new ArrayByteList(-1);
    }

    @Test
    public void testConstructorWithByteCollection() {
        ByteCollection byteCollection = new ByteCollectionImpl();
        byteCollection.add((byte) 1);
        byteCollection.add((byte) 2);

        ArrayByteList arrayByteList = new ArrayByteList(byteCollection);

        assertEquals(2, arrayByteList.size());
        assertEquals((byte) 1, arrayByteList.get(0));
        assertEquals((byte) 2, arrayByteList.get(1));
    }

    @Test
    public void testGetElementAt() {
        arrayByteList.add(0, (byte) 1);
        arrayByteList.add(1, (byte) 2);

        assertEquals((byte) 1, arrayByteList.get(0));
        assertEquals((byte) 2, arrayByteList.get(1));
    }

    @Test
    public void testSize() {
        arrayByteList.add(0, (byte) 1);
        arrayByteList.add(1, (byte) 2);

        assertEquals(2, arrayByteList.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtOutOfRange() {
        arrayByteList.removeElementAt(0);
    }

    @Test
    public void testRemoveElementAt() {
        arrayByteList.add(0, (byte) 1);
        arrayByteList.add(1, (byte) 2);

        byte element = arrayByteList.removeElementAt(1);

        assertEquals(1, arrayByteList.size());
        assertEquals((byte) 2, element);
        assertEquals((byte) 1, arrayByteList.get(0));
    }

    @Test
    public void testSetElementAt() {
        arrayByteList.add(0, (byte) 1);
        arrayByteList.add(1, (byte) 2);

        byte element = arrayByteList.set(1, (byte) 3);

        assertEquals(2, arrayByteList.size());
        assertEquals((byte) 2, element);
        assertEquals((byte) 3, arrayByteList.get(1));
    }

    @Test
    public void testAddElementAt() {
        arrayByteList.add(0, (byte) 1);
        arrayByteList.add(1, (byte) 2);

        arrayByteList.add(1, (byte) 3);

        assertEquals(3, arrayByteList.size());
        assertEquals((byte) 1, arrayByteList.get(0));
        assertEquals((byte) 3, arrayByteList.get(1));
        assertEquals((byte) 2, arrayByteList.get(2));
    }

    @Test
    public void testEnsureCapacity() {
        arrayByteList.ensureCapacity(10);

        assertEquals(0, arrayByteList.size());
    }

    @Test
    public void testTrimToSize() {
        arrayByteList.add(0, (byte) 1);
        arrayByteList.add(1, (byte) 2);
        arrayByteList.add(2, (byte) 3);

        arrayByteList.trimToSize();

        assertEquals(3, arrayByteList.size());
        assertEquals((byte) 1, arrayByteList.get(0));
        assertEquals((byte) 2, arrayByteList.get(1));
        assertEquals((byte) 3, arrayByteList.get(2));
    }

    @Test
    public void testSerialization() throws Exception {
        arrayByteList.add(0, (byte) 1);
        arrayByteList.add(1, (byte) 2);
        
        ArrayByteList arrayByteList2 = TestUtils.serializeAndDeserialize(arrayByteList);
        
        assertEquals(2, arrayByteList2.size());
        assertEquals((byte) 1, arrayByteList2.get(0));
        assertEquals((byte) 2, arrayByteList2.get(1));
    }

    private class ByteCollectionImpl implements ByteCollection {
        private byte[] data;
        private int size;

        public ByteCollectionImpl() {
            data = new byte[8];
            size = 0;
        }

        @Override
        public void add(byte element) {
            data[size++] = element;
        }

        @Override
        public int size() {
            return size;
        }
    }
}