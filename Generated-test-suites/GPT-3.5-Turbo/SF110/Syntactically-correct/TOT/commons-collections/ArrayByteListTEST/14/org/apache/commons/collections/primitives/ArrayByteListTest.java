import org.junit.Before;
import org.junit.Test;
import org.apache.commons.collections.primitives.ArrayByteList;

import static org.junit.Assert.*;

public class ArrayByteListTest {

    private ArrayByteList arrayByteList;

    @Before
    public void setUp() {
        arrayByteList = new ArrayByteList();
    }

    // Test case for ArrayByteList constructor with no initial capacity
    @Test
    public void testConstructor() {
        assertNotNull(arrayByteList);
        assertEquals(0, arrayByteList.size());
    }

    // Test case for ArrayByteList constructor with initial capacity
    @Test
    public void testConstructorWithInitialCapacity() {
        arrayByteList = new ArrayByteList(10);
        assertNotNull(arrayByteList);
        assertEquals(0, arrayByteList.size());
    }

    // Test case for ArrayByteList constructor with ByteCollection parameter
    @Test
    public void testConstructorWithByteCollection() {
        ByteCollection byteCollection = new ArrayByteList();
        byteCollection.add((byte) 1);
        byteCollection.add((byte) 2);
        arrayByteList = new ArrayByteList(byteCollection);
        assertNotNull(arrayByteList);
        assertEquals(2, arrayByteList.size());
        assertEquals(1, arrayByteList.get(0));
        assertEquals(2, arrayByteList.get(1));
    }

    // Test case for get method with valid index
    @Test
    public void testGetWithValidIndex() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        assertEquals(1, arrayByteList.get(0));
        assertEquals(2, arrayByteList.get(1));
    }

    // Test case for get method with invalid index (out of bounds)
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetWithInvalidIndex() {
        arrayByteList.get(0);
    }

    // Test case for size method
    @Test
    public void testSize() {
        assertEquals(0, arrayByteList.size());
        arrayByteList.add((byte) 1);
        assertEquals(1, arrayByteList.size());
        arrayByteList.add((byte) 2);
        assertEquals(2, arrayByteList.size());
    }

    // Test case for removeElementAt method with valid index
    @Test
    public void testRemoveElementAtWithValidIndex() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        byte removedElement = arrayByteList.removeElementAt(0);
        assertEquals(1, removedElement);
        assertEquals(1, arrayByteList.size());
        assertEquals(2, arrayByteList.get(0));
    }

    // Test case for removeElementAt method with invalid index (out of bounds)
    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAtWithInvalidIndex() {
        arrayByteList.removeElementAt(0);
    }

    // Test case for set method with valid index
    @Test
    public void testSetWithValidIndex() {
        arrayByteList.add((byte) 1);
        byte oldElement = arrayByteList.set(0, (byte) 2);
        assertEquals(1, oldElement);
        assertEquals(1, arrayByteList.size());
        assertEquals(2, arrayByteList.get(0));
    }

    // Test case for set method with invalid index (out of bounds)
    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetWithInvalidIndex() {
        arrayByteList.set(0, (byte) 1);
    }

    // Test case for add method with valid index
    @Test
    public void testAddWithValidIndex() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 3);
        arrayByteList.add(1, (byte) 2);
        assertEquals(3, arrayByteList.size());
        assertEquals(1, arrayByteList.get(0));
        assertEquals(2, arrayByteList.get(1));
        assertEquals(3, arrayByteList.get(2));
    }

    // Test case for add method with invalid index (out of bounds)
    @Test(expected = IndexOutOfBoundsException.class)
    public void testAddWithInvalidIndex() {
        arrayByteList.add(1, (byte) 1);
    }

    // Test case for add method with increasing size of ArrayList
    @Test
    public void testEnsureCapacity() {
        arrayByteList.ensureCapacity(4);
        for (byte i = 0; i < 4; i++) {
            arrayByteList.add(i);
        }
        assertEquals(4, arrayByteList.size());
        assertEquals(0, arrayByteList.get(0));
        assertEquals(1, arrayByteList.get(1));
        assertEquals(2, arrayByteList.get(2));
        assertEquals(3, arrayByteList.get(3));
    }

    // Test case for add method with decreasing size of ArrayList
    @Test
    public void testTrimToSize() {
        arrayByteList.trimToSize();
        assertEquals(0, arrayByteList.size());
    }

    // Test case for serialization/deserialization
    @Test
    public void testSerializationDeserialization() throws IOException, ClassNotFoundException {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bos);
        out.writeObject(arrayByteList);

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream in = new ObjectInputStream(bis);
        ArrayByteList deserializedArrayByteList = (ArrayByteList) in.readObject();

        assertEquals(arrayByteList.size(), deserializedArrayByteList.size());
        for (int i = 0; i < arrayByteList.size(); i++) {
            assertEquals(arrayByteList.get(i), deserializedArrayByteList.get(i));
        }
    }

    // Test case for checkRange method with valid index
    @Test
    public void testCheckRangeWithValidIndex() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        arrayByteList.checkRange(0);
        arrayByteList.checkRange(1);
    }

    // Test case for checkRange method with invalid index (out of bounds)
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeWithInvalidIndex() {
        arrayByteList.checkRange(2);
    }

    // Test case for checkRangeIncludingEndpoint method with valid index
    @Test
    public void testCheckRangeIncludingEndpointWithValidIndex() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        arrayByteList.checkRangeIncludingEndpoint(0);
        arrayByteList.checkRangeIncludingEndpoint(1);
        arrayByteList.checkRangeIncludingEndpoint(2);
    }

    // Test case for checkRangeIncludingEndpoint method with invalid index (out of bounds)
    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpointWithInvalidIndex() {
        arrayByteList.checkRangeIncludingEndpoint(3);
    }

}