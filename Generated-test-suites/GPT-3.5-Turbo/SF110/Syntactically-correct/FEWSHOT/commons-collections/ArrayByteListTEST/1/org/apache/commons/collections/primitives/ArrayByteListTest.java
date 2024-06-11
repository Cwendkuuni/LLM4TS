//Test for default constructor
import static org.junit.Assert.*;
import org.junit.Test;

public class ArrayByteListTest {

    @Test
    public void testDefaultConstructor() {
        ArrayByteList list = new ArrayByteList();
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    //Test for constructor with initial capacity
    @Test
    public void testConstructorWithInitialCapacity() {
        ArrayByteList list = new ArrayByteList(10);
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    //Test for constructor with ByteCollection parameter
    @Test
    public void testConstructorWithByteCollection() {
        ByteCollection collection = new ByteCollection();
        collection.add((byte)1);
        collection.add((byte)2);
        collection.add((byte)3);
        ArrayByteList list = new ArrayByteList(collection);
        assertNotNull(list);
        assertEquals(3, list.size());
    }

    //Test for get method
    @Test
    public void testGetMethod() {
        ArrayByteList list = new ArrayByteList();
        list.add(1);
        list.add(2);
        list.add(3);
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }

    //Test for size method
    @Test
    public void testSizeMethod() {
        ArrayByteList list = new ArrayByteList();
        assertEquals(0, list.size());
        list.add(1);
        assertEquals(1, list.size());
        list.add(2);
        assertEquals(2, list.size());
    }

    //Test for removeElementAt method
    @Test
    public void testRemoveElementAtMethod() {
        ArrayByteList list = new ArrayByteList();
        list.add(1);
        list.add(2);
        list.add(3);
        assertEquals(2, list.removeElementAt(1));
        assertEquals(1, list.size());
        assertEquals(3, list.get(1));
    }

    //Test for set method
    @Test
    public void testSetMethod() {
        ArrayByteList list = new ArrayByteList();
        list.add(1);
        list.add(2);
        list.add(3);
        assertEquals(2, list.set(1, 4));
        assertEquals(4, list.get(1));
    }

    //Test for add method
    @Test
    public void testAddMethod() {
        ArrayByteList list = new ArrayByteList();
        list.add(1);
        list.add(2);
        list.add(3);
        assertEquals(1, list.get(0));
        assertEquals(2, list.get(1));
        assertEquals(3, list.get(2));
    }

    //Test for ensureCapacity method
    @Test
    public void testEnsureCapacityMethod() {
        ArrayByteList list = new ArrayByteList();
        list.ensureCapacity(10);
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    //Test for trimToSize method
    @Test
    public void testTrimToSizeMethod() {
        ArrayByteList list = new ArrayByteList();
        list.add(1);
        list.add(2);
        list.add(3);
        list.trimToSize();
        assertNotNull(list);
        assertEquals(3, list.size());
    }
}