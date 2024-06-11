import static org.junit.Assert.*;
import org.junit.Test;

public class ArrayByteListTest {

    @Test
    public void testArrayByteList() {
        ArrayByteList list = new ArrayByteList();
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    public void testArrayByteListWithInitialCapacity() {
        ArrayByteList list = new ArrayByteList(10);
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    public void testArrayByteListWithByteCollection() {
        ByteArrayList byteList = new ByteArrayList();
        byteList.add((byte) 1);
        byteList.add((byte) 2);

        ArrayByteList list = new ArrayByteList(byteList);
        assertNotNull(list);
        assertEquals(2, list.size());
    }

    @Test
    public void testGet() {
        ArrayByteList list = new ArrayByteList();
        byte element = 5;
        list.add(element);
        assertEquals(element, list.get(0));

        // Test for IndexOutOfBoundsException
        try {
            list.get(1);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException e) {
            // Exception expected
        }
    }

    @Test
    public void testRemoveElementAt() {
        ArrayByteList list = new ArrayByteList();
        list.add((byte) 1);
        list.add((byte) 2);
        assertEquals((byte) 1, list.removeElementAt(0));
        assertEquals(1, list.size());

        // Test for IndexOutOfBoundsException
        try {
            list.removeElementAt(1);
            fail("IndexOutOfBoundsException expected");
        } catch (IndexOutOfBoundsException e) {
            // Exception expected
        }
    }

    // Additional test cases for other methods can be added similarly

}