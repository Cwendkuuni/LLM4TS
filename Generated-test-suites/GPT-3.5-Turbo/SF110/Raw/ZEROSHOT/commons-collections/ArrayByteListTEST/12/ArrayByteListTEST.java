
package org.apache.commons.collections.primitives;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.Arrays;

public class ArrayByteListTest {

    private ArrayByteList arrayByteList;

    @Before
    public void setUp() {
        arrayByteList = new ArrayByteList();
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        ArrayByteList list = new ArrayByteList(10);
        Assert.assertEquals(0, list.size());
        Assert.assertEquals(10, list._data.length);
    }

    @Test
    public void testConstructorWithByteCollection() {
        ByteCollection byteCollection = new ArrayByteList();
        byteCollection.add((byte) 1);
        byteCollection.add((byte) 2);
        byteCollection.add((byte) 3);

        ArrayByteList list = new ArrayByteList(byteCollection);
        Assert.assertEquals(3, list.size());
        Assert.assertEquals((byte) 1, list.get(0));
        Assert.assertEquals((byte) 2, list.get(1));
        Assert.assertEquals((byte) 3, list.get(2));
    }

    @Test
    public void testGet() {
        arrayByteList.add((byte) 1);
        Assert.assertEquals((byte) 1, arrayByteList.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGet_OutOfBounds() {
        arrayByteList.get(0);
    }

    @Test
    public void testSize() {
        Assert.assertEquals(0, arrayByteList.size());

        arrayByteList.add((byte) 1);
        Assert.assertEquals(1, arrayByteList.size());
    }

    @Test
    public void testRemoveElementAt() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);

        byte removed = arrayByteList.removeElementAt(0);
        Assert.assertEquals(1, arrayByteList.size());
        Assert.assertEquals((byte) 1, removed);
        Assert.assertEquals((byte) 2, arrayByteList.get(0));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testRemoveElementAt_OutOfBounds() {
        arrayByteList.removeElementAt(0);
    }

    @Test
    public void testSet() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);

        byte replaced = arrayByteList.set(1, (byte) 3);
        Assert.assertEquals(2, arrayByteList.size());
        Assert.assertEquals((byte) 2, replaced);
        Assert.assertEquals((byte) 1, arrayByteList.get(0));
        Assert.assertEquals((byte) 3, arrayByteList.get(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSet_OutOfBounds() {
        arrayByteList.set(0, (byte) 1);
    }

    @Test
    public void testAdd() {
        arrayByteList.add((byte) 1);
        Assert.assertEquals(1, arrayByteList.size());
        Assert.assertEquals((byte) 1, arrayByteList.get(0));
    }

    @Test
    public void testAdd_AtIndex() {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);

        arrayByteList.add(1, (byte) 3);
        Assert.assertEquals(3, arrayByteList.size());
        Assert.assertEquals((byte) 1, arrayByteList.get(0));
        Assert.assertEquals((byte) 3, arrayByteList.get(1));
        Assert.assertEquals((byte) 2, arrayByteList.get(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testAdd_OutOfBounds() {
        arrayByteList.add(1, (byte) 1);
    }

    @Test
    public void testEnsureCapacity() {
        arrayByteList.ensureCapacity(10);
        Assert.assertEquals(0, arrayByteList.size());
        Assert.assertEquals(10, arrayByteList._data.length);

        arrayByteList.add((byte) 1);
        arrayByteList.ensureCapacity(5);
        Assert.assertEquals(1, arrayByteList.size());
        Assert.assertEquals(10, arrayByteList._data.length);

        arrayByteList.ensureCapacity(15);
        Assert.assertEquals(1, arrayByteList.size());
        Assert.assertEquals(15, arrayByteList._data.length);
    }

    @Test
    public void testTrimToSize() {
        arrayByteList.ensureCapacity(10);
        Assert.assertEquals(0, arrayByteList.size());
        Assert.assertEquals(10, arrayByteList._data.length);

        arrayByteList.add((byte) 1);
        arrayByteList.trimToSize();
        Assert.assertEquals(1, arrayByteList.size());
        Assert.assertEquals(1, arrayByteList._data.length);

        arrayByteList.add((byte) 2);
        arrayByteList.trimToSize();
        Assert.assertEquals(2, arrayByteList.size());
        Assert.assertEquals(2, arrayByteList._data.length);
    }

    @Test
    public void testWriteObject() throws IOException {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        arrayByteList.writeObject(objectOutputStream);
        objectOutputStream.flush();
        byte[] bytes = byteArrayOutputStream.toByteArray();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

        ArrayByteList deserialized = new ArrayByteList();
        deserialized.readObject(objectInputStream);

        Assert.assertEquals(2, deserialized.size());
        Assert.assertEquals(Arrays.toString(arrayByteList._data), Arrays.toString(deserialized._data));
    }

    @Test
    public void testReadObject() throws IOException, ClassNotFoundException {
        arrayByteList.add((byte) 1);
        arrayByteList.add((byte) 2);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        arrayByteList.writeObject(objectOutputStream);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

        ArrayByteList deserialized = new ArrayByteList();
        deserialized.readObject(objectInputStream);

        Assert.assertEquals(2, deserialized.size());
        Assert.assertEquals(Arrays.toString(arrayByteList._data), Arrays.toString(deserialized._data));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRange_OutOfBounds() {
        arrayByteList.checkRange(0);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testCheckRangeIncludingEndpoint_OutOfBounds() {
        arrayByteList.checkRangeIncludingEndpoint(0);
    }
}
