import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.collections.primitives.ArrayByteList;

public class ArrayByteListTest {

    @Test
    public void testConstructorNoArg() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        ArrayByteList list = new ArrayByteList(10);
        assertEquals(0, list.size());
    }

    @Test
    public void testConstructorByteCollection() {
        ArrayByteList list = new ArrayByteList(new ArrayByteList());
        assertEquals(0, list.size());
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 10);
        assertEquals(10, list.get(0));
    }

    @Test
    public void testSize() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 10);
        assertEquals(1, list.size());
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 10);
        assertEquals(10, list.removeElementAt(0));
        assertEquals(0, list.size());
    }

    @Test
    public void testSet() {
        ArrayByteList list = new ArrayByteList();
        list.add(0, (byte) 10);
        assertEquals(10, list.set(0, (byte) 20));
        assertEquals(20, list.get(0));
    }

    // Add more test cases for other methods and edge cases

}