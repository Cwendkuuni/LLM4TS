import org.junit.Test;
import static org.junit.Assert.*;

public class ArrayByteListTest {

    @Test
    public void testArrayByteList() {
        ArrayByteList arrayByteList = new ArrayByteList();
        assertNotNull(arrayByteList);
    }

    @Test
    public void testArrayByteListWithInitialCapacity() {
        ArrayByteList arrayByteList = new ArrayByteList(10);
        assertNotNull(arrayByteList);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testArrayByteListWithNegativeInitialCapacity() {
        ArrayByteList arrayByteList = new ArrayByteList(-10);
    }

    @Test
    public void testArrayByteListWithByteCollection() {
        ByteCollection byteCollection = new ArrayByteList();
        ArrayByteList arrayByteList = new ArrayByteList(byteCollection);
        assertNotNull(arrayByteList);
    }

    @Test
    public void testGet() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add(10);
        byte element = arrayByteList.get(0);
        assertEquals(10, element);
    }

    @Test
    public void testSize() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add(10);
        arrayByteList.add(20);
        int size = arrayByteList.size();
        assertEquals(2, size);
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add(10);
        byte element = arrayByteList.removeElementAt(0);
        assertEquals(10, element);
        assertEquals(0, arrayByteList.size());
    }

    @Test
    public void testSet() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add(10);
        byte oldval = arrayByteList.set(0, 20);
        assertEquals(10, oldval);
        assertEquals(20, arrayByteList.get(0));
    }

    @Test
    public void testAdd() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add(10);
        arrayByteList.add(20);
        assertEquals(10, arrayByteList.get(0));
        assertEquals(20, arrayByteList.get(1));
        assertEquals(2, arrayByteList.size());
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add(10);
        arrayByteList.add(20);
        arrayByteList.ensureCapacity(10);
        assertEquals(10, arrayByteList.get(0));
        assertEquals(20, arrayByteList.get(1));
        assertEquals(2, arrayByteList.size());
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add(10);
        arrayByteList.add(20);
        arrayByteList.trimToSize();
        assertEquals(10, arrayByteList.get(0));
        assertEquals(20, arrayByteList.get(1));
        assertEquals(2, arrayByteList.size());
    }
}