import org.junit.*;
import static org.junit.Assert.*;

public class ArrayByteListTest {

    private ArrayByteList arrayList;

    @Before
    public void setUp() {
        arrayList = new ArrayByteList();
    }

    @Test
    public void testConstructorNoInitialCapacity() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        ArrayByteList list = new ArrayByteList(10);
        assertEquals(0, list.size());
    }

    @Test
    public void testGet() {
        arrayList.add(5, (byte) 10);
        assertEquals(10, arrayList.get(5));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetOutOfBounds() {
        arrayList.get(10);
    }

    @Test
    public void testSize() {
        assertEquals(0, arrayList.size());
    }

    @Test
    public void testRemoveElementAt() {
        arrayList.add(0, (byte) 5);
        assertEquals(5, arrayList.removeElementAt(0));
    }

    @Test
    public void testSet() {
        arrayList.add(0, (byte) 1);
        assertEquals(1, arrayList.set(0, (byte) 2));
    }

    @Test
    public void testAdd() {
        arrayList.add(0, (byte) 1);
        assertEquals(1, arrayList.get(0));
    }

    @Test
    public void testEnsureCapacity() {
        for (int i = 0; i < 20; i++) {
            arrayList.add(i, (byte) i);
        }
        assertEquals(20, arrayList.size());
    }

    @Test
    public void testTrimToSize() {
        for (int i = 0; i < 10; i++) {
            arrayList.add(i, (byte) i);
        }
        arrayList.trimToSize();
        assertEquals(10, arrayList.size());
    }

    @Test
    public void testCheckRange() {
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            arrayList.checkRange(15);
        });
        String expectedMessage = "Should be at least 0 and less than 0, found 15";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    public void testCheckRangeIncludingEndpoint() {
        Exception exception = assertThrows(IndexOutOfBoundsException.class, () -> {
            arrayList.checkRangeIncludingEndpoint(10);
        });
        String expectedMessage = "Should be at least 0 and at most 0, found 10";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }

}