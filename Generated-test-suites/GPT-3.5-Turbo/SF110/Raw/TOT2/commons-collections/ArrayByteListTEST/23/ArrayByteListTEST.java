import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {

    private ArrayByteList list;

    @Before
    public void setUp() {
        list = new ArrayByteList();
    }

    @Test
    public void testAdd() {
        list.add(0, (byte) 1);
        assertEquals(1, list.size());
        assertEquals(1, list.get(0));
    }

    @Test
    public void testGet() {
        list.add(0, (byte) 1);
        assertEquals(1, list.get(0));
    }

    @Test
    public void testRemoveElementAt() {
        list.add(0, (byte) 1);
        assertEquals(1, list.removeElementAt(0));
        assertEquals(0, list.size());
    }

    @Test
    public void testSet() {
        list.add(0, (byte) 1);
        assertEquals(1, list.set(0, (byte) 2));
        assertEquals(2, list.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddIndexOutOfBounds() {
        list.add(1, (byte) 1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetIndexOutOfBounds() {
        list.get(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtIndexOutOfBounds() {
        list.removeElementAt(0);
    }

    @Test
    public void testEnsureCapacity() {
        list.ensureCapacity(10);
        assertEquals(10, list._data.length);
    }

    @Test
    public void testTrimToSize() {
        list.add(0, (byte) 1);
        list.add(1, (byte) 2);
        list.trimToSize();
        assertEquals(2, list._data.length);
    }

}