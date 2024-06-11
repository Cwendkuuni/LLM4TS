
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {
    private ArrayByteList arrayByteList;

    @Before
    public void setUp() {
        arrayByteList = new ArrayByteList();
    }

    @Test
    public void testAddAndGet() {
        arrayByteList.add(0, (byte) 1);
        arrayByteList.add(1, (byte) 2);
        assertEquals(1, arrayByteList.get(0));
        assertEquals(2, arrayByteList.get(1));
    }

    @Test
    public void testRemoveElementAt() {
        arrayByteList.add(0, (byte) 1);
        arrayByteList.add(1, (byte) 2);
        byte removed = arrayByteList.removeElementAt(0);
        assertEquals(1, removed);
        assertEquals(2, arrayByteList.get(0));
    }

    @Test
    public void testSet() {
        arrayByteList.add(0, (byte) 1);
        arrayByteList.add(1, (byte) 2);
        byte oldValue = arrayByteList.set(0, (byte) 3);
        assertEquals(1, oldValue);
        assertEquals(3, arrayByteList.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfRange() {
        arrayByteList.get(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtOutOfRange() {
        arrayByteList.removeElementAt(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetOutOfRange() {
        arrayByteList.set(0, (byte) 1);
    }
}
