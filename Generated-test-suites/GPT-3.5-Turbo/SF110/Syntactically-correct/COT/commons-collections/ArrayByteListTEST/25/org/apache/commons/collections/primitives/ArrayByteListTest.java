import org.junit.*;
import static org.junit.Assert.*;

public class ArrayByteListTest {

    ArrayByteList arrayList;

    @Before
    public void setUp() {
        arrayList = new ArrayByteList();
    }

    @Test
    public void testConstructor() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());

        ArrayByteList listWithCapacity = new ArrayByteList(10);
        assertEquals(0, listWithCapacity.size());
    }

    @Test
    public void testSize() {
        assertEquals(0, arrayList.size());
    }

    @Test
    public void testGet() {
        arrayList.add(50);
        assertEquals(50, arrayList.get(0));
    }

    @Test
    public void testRemoveElementAt() {
        arrayList.add(1);
        byte removedElement = arrayList.removeElementAt(0);
        assertEquals(1, removedElement);
        assertEquals(0, arrayList.size());
    }

    @Test
    public void testSet() {
        arrayList.add(10);
        byte oldElement = arrayList.set(0, 20);
        assertEquals(10, oldElement);
        assertEquals(20, arrayList.get(0));
    }

    @Test
    public void testAdd() {
        arrayList.add(0, 100);
        assertEquals(1, arrayList.size());
        assertEquals(100, arrayList.get(0));
    }

    @Test
    public void testEnsureCapacity() {
        arrayList.ensureCapacity(10);
        assertEquals(0, arrayList.size());
    }

    @Test
    public void testTrimToSize() {
        arrayList.add(1);
        arrayList.add(2);
        arrayList.trimToSize();
        assertEquals(2, arrayList.size());
        assertEquals(1, arrayList.get(0));
        assertEquals(2, arrayList.get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidInitialCapacity() {
        ArrayByteList list = new ArrayByteList(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithInvalidIndex() {
        arrayList.get(10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithInvalidIndex() {
        arrayList.removeElementAt(10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithInvalidIndex() {
        arrayList.set(10, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithInvalidIndex() {
        arrayList.add(10, 10);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeWithInvalidIndex() {
        ArrayList.checkRange(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointWithInvalidIndex() {
        ArrayList.checkRangeIncludingEndpoint(-1);
    }
}