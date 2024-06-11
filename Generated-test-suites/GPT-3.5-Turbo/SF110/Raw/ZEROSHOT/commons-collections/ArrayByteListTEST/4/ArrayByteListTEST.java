package org.apache.commons.collections.primitives;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ArrayByteListTest {
    
    private ArrayByteList arrayByteList;
    
    @Before
    public void setup() {
        arrayByteList = new ArrayByteList();
    }
    
    @Test
    public void testEmptyArrayByteListSize() {
        Assert.assertEquals(0, arrayByteList.size());
    }
    
    @Test
    public void testArrayByteListSize() {
        arrayByteList.add(10);
        arrayByteList.add(20);
        arrayByteList.add(30);
        
        Assert.assertEquals(3, arrayByteList.size());
    }
    
    @Test
    public void testArrayByteListGet() {
        arrayByteList.add(10);
        arrayByteList.add(20);
        arrayByteList.add(30);
        
        Assert.assertEquals(20, arrayByteList.get(1));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testArrayByteListGetInvalidIndex() {
        arrayByteList.add(10);
        arrayByteList.add(20);
        arrayByteList.add(30);
        
        arrayByteList.get(3);
    }
    
    @Test
    public void testArrayByteListRemoveElementAt() {
        arrayByteList.add(10);
        arrayByteList.add(20);
        arrayByteList.add(30);
        
        byte removedElement = arrayByteList.removeElementAt(1);
        
        Assert.assertEquals(20, removedElement);
        Assert.assertEquals(2, arrayByteList.size());
        Assert.assertEquals(10, arrayByteList.get(0));
        Assert.assertEquals(30, arrayByteList.get(1));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testArrayByteListRemoveElementAtInvalidIndex() {
        arrayByteList.add(10);
        arrayByteList.add(20);
        arrayByteList.add(30);
        
        arrayByteList.removeElementAt(3);
    }
    
    @Test
    public void testArrayByteListSet() {
        arrayByteList.add(10);
        arrayByteList.add(20);
        arrayByteList.add(30);
        
        byte oldElement = arrayByteList.set(1, (byte) 25);
        
        Assert.assertEquals(20, oldElement);
        Assert.assertEquals(3, arrayByteList.size());
        Assert.assertEquals(10, arrayByteList.get(0));
        Assert.assertEquals(25, arrayByteList.get(1));
        Assert.assertEquals(30, arrayByteList.get(2));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testArrayByteListSetInvalidIndex() {
        arrayByteList.add(10);
        arrayByteList.add(20);
        arrayByteList.add(30);
        
        arrayByteList.set(3, (byte) 25);
    }
    
    @Test
    public void testArrayByteListAdd() {
        arrayByteList.add(10);
        arrayByteList.add(20);
        arrayByteList.add(30);
        
        Assert.assertEquals(3, arrayByteList.size());
        
        arrayByteList.add(1, (byte) 25);
        
        Assert.assertEquals(4, arrayByteList.size());
        Assert.assertEquals(10, arrayByteList.get(0));
        Assert.assertEquals(25, arrayByteList.get(1));
        Assert.assertEquals(20, arrayByteList.get(2));
        Assert.assertEquals(30, arrayByteList.get(3));
    }
    
    @Test(expected = IndexOutOfBoundsException.class)
    public void testArrayByteListAddInvalidIndex() {
        arrayByteList.add(10);
        arrayByteList.add(20);
        arrayByteList.add(30);
        
        arrayByteList.add(4, (byte) 25);
    }
    
    @Test
    public void testArrayByteListEnsureCapacity() {
        arrayByteList.ensureCapacity(5);
        
        Assert.assertEquals(0, arrayByteList.size());
        
        arrayByteList.add(10);
        arrayByteList.add(20);
        arrayByteList.add(30);
        
        arrayByteList.ensureCapacity(10);
        
        Assert.assertEquals(3, arrayByteList.size());
    }
    
    @Test
    public void testArrayByteListTrimToSize() {
        arrayByteList.add(10);
        arrayByteList.add(20);
        arrayByteList.add(30);
        
        arrayByteList.trimToSize();
        
        Assert.assertEquals(3, arrayByteList.size());
    }
    
    @Test
    public void testArrayByteListSerialization() throws IOException, ClassNotFoundException {
        arrayByteList.add(10);
        arrayByteList.add(20);
        arrayByteList.add(30);
        
        ObjectOutputStream out = new ObjectOutputStream(new ByteArrayOutputStream());
        out.writeObject(arrayByteList);
        
        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(out.toByteArray()));
        ArrayByteList deserializedList = (ArrayByteList) in.readObject();
        
        Assert.assertEquals(3, deserializedList.size());
        Assert.assertEquals(10, deserializedList.get(0));
        Assert.assertEquals(20, deserializedList.get(1));
        Assert.assertEquals(30, deserializedList.get(2));
    }
}