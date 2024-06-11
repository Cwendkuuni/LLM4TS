import static org.junit.Assert.*;
import org.junit.Test;

public class ArrayByteListTest {

    @Test
    public void testArrayByteListConstructor() {
        ArrayByteList arrayByteList = new ArrayByteList();
        assertNotNull(arrayByteList);
        assertEquals(0, arrayByteList.size());
    }

    @Test
    public void testArrayByteListWithInitialCapacityConstructor() {
        ArrayByteList arrayByteList = new ArrayByteList(10);
        assertNotNull(arrayByteList);
        assertEquals(0, arrayByteList.size());
    }

    @Test
    public void testArrayByteListFromCollectionConstructor() {
        ArrayByteList arrayByteList = new ArrayByteList(new ArrayByteList());
        assertNotNull(arrayByteList);
        assertEquals(0, arrayByteList.size());
    }

    @Test
    public void testGet() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add(0, (byte) 50);
        assertEquals(50, arrayByteList.get(0));
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add(0, (byte) 50);
        byte removedElement = arrayByteList.removeElementAt(0);
        assertEquals(50, removedElement);
        assertEquals(0, arrayByteList.size());
    }

    @Test
    public void testSet() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add(0, (byte) 50);
        byte oldElement = arrayByteList.set(0, (byte) 100);
        assertEquals(50, oldElement);
        assertEquals(100, arrayByteList.get(0));
    }

    @Test
    public void testAdd() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add(0, (byte) 50);
        arrayByteList.add(1, (byte) 100);
        assertEquals(2, arrayByteList.size());
        assertEquals(100, arrayByteList.get(1));
    }

    @Test
    public void testEnsureCapacity() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.ensureCapacity(100);
        // No assertion required, test for ensuring capacity does not change existing data
    }

    @Test
    public void testTrimToSize() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.add(0, (byte) 50);
        arrayByteList.add(1, (byte) 100);
        arrayByteList.trimToSize();
        assertEquals(2, arrayByteList.size());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeWithInvalidIndex() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.checkRange(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointWithInvalidIndex() {
        ArrayByteList arrayByteList = new ArrayByteList();
        arrayByteList.checkRangeIncludingEndpoint(1);
    }
}