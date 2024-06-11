import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;

import static org.junit.Assert.*;

public class ArrayByteListTest {

    private ArrayByteList arrayByteList;

    @Before
    public void setUp() {
        arrayByteList = new ArrayByteList();
    }

    @Test
    public void testConstructor() {
        assertEquals(8, arrayByteList.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructor_InvalidInitialCapacity() {
        new ArrayByteList(-1);
    }

    @Test
    public void testConstructor_WithByteCollection() {
        ByteCollection byteCollection = new ArrayByteList();
        byteCollection.add((byte) 1);
        byteCollection.add((byte) 2);
        byteCollection.add((byte) 3);

        ArrayByteList newByteList = new ArrayByteList(byteCollection);

        assertEquals(3, newByteList.size());
        assertEquals(1, newByteList.get(0));
        assertEquals(2, newByteList.get(1));
        assertEquals(3, newByteList.get(2));
    }

    @Test
    public void testGet() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);

        assertEquals(1, arrayByteList.get(0));
        assertEquals(2, arrayByteList.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGet_InvalidIndex() {
        arrayByteList.get(-1);
    }

    @Test
    public void testSize() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);

        assertEquals(2, arrayByteList.size());
    }

    @Test
    public void testRemoveElementAt() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);

        byte removedElement = arrayByteList.removeElementAt(0);

        assertEquals(1, removedElement);
        assertEquals(1, arrayByteList.size());
        assertEquals(2, arrayByteList.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAt_InvalidIndex() {
        arrayByteList.removeElementAt(0);
    }

    @Test
    public void testSet() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);

        byte oldValue = arrayByteList.set(0, (byte) 3);

        assertEquals(1, oldValue);
        assertEquals(2, arrayByteList.size());
        assertEquals(3, arrayByteList.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSet_InvalidIndex() {
        arrayByteList.set(0, (byte) 1);
    }

    @Test
    public void testAdd() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);

        assertEquals(2, arrayByteList.size());
        assertEquals(1, arrayByteList.get(0));
        assertEquals(2, arrayByteList.get(1));
    }

    @Test
    public void testAdd_AtIndex() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);

        arrayByteList.add(1, (byte) 3);

        assertEquals(3, arrayByteList.size());
        assertEquals(1, arrayByteList.get(0));
        assertEquals(3, arrayByteList.get(1));
        assertEquals(2, arrayByteList.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAdd_InvalidIndex() {
        arrayByteList.add(1, (byte) 1);
    }

    @Test
    public void testEnsureCapacity() {
        arrayByteList.ensureCapacity(5);

        assertEquals(8, arrayByteList.size());

        arrayByteList.ensureCapacity(10);

        assertEquals(10, arrayByteList.size());
    }

    @Test
    public void testTrimToSize() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        arrayByteList.add((byte) 3);
        arrayByteList.add((byte) 4);

        arrayByteList.trimToSize();

        assertEquals(4, arrayByteList.size());
    }

    @Test
    public void testWriteObject() throws IOException {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        arrayByteList.add((byte) 3);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        objectOutputStream.writeObject(arrayByteList);

        byte[] serializedBytes = byteArrayOutputStream.toByteArray();

        // Decode serialized bytes and compare with original list
        ArrayByteList deserializedList = (ArrayByteList) new ObjectInputStream(new ByteArrayInputStream(serializedBytes)).readObject();

        assertEquals(arrayByteList.size(), deserializedList.size());
        assertEquals(arrayByteList.get(0), deserializedList.get(0));
        assertEquals(arrayByteList.get(1), deserializedList.get(1));
        assertEquals(arrayByteList.get(2), deserializedList.get(2));
    }

    @Test
    public void testReadObject() throws IOException, ClassNotFoundException {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);
        arrayByteList.add((byte) 3);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        objectOutputStream.writeObject(arrayByteList);

        byte[] serializedBytes = byteArrayOutputStream.toByteArray();

        // Decode serialized bytes and compare with original list
        ArrayByteList deserializedList = (ArrayByteList) new ObjectInputStream(new ByteArrayInputStream(serializedBytes)).readObject();

        assertEquals(arrayByteList.size(), deserializedList.size());
        assertEquals(arrayByteList.get(0), deserializedList.get(0));
        assertEquals(arrayByteList.get(1), deserializedList.get(1));
        assertEquals(arrayByteList.get(2), deserializedList.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRange_InvalidIndex() {
        arrayByteList.checkRange(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpoint_InvalidIndex() {
        arrayByteList.checkRangeIncludingEndpoint(-1);
    }

}