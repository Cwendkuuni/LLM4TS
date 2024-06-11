package org.apache.commons.collections.primitives;

import org.junit.Assert;
import org.junit.Test;

public class ArrayByteListTest {
    
    @Test
    public void testConstructorWithDefaultCapacity() {
        ArrayByteList byteList = new ArrayByteList();
        Assert.assertEquals(0, byteList.size());
    }
    
    @Test
    public void testConstructorWithInitialCapacity() {
        int initialCapacity = 10;
        ArrayByteList byteList = new ArrayByteList(initialCapacity);
        Assert.assertEquals(0, byteList.size());
    }
    
    @Test
    public void testConstructorWithByteCollection() {
        ByteCollection byteCollection = new ByteArrayList();
        byteCollection.add((byte)1);
        byteCollection.add((byte)2);
        
        ArrayByteList byteList = new ArrayByteList(byteCollection);
        
        Assert.assertEquals(2, byteList.size());
        Assert.assertEquals((byte)1, byteList.get(0));
        Assert.assertEquals((byte)2, byteList.get(1));
    }
    
    @Test
    public void testGet() {
        ArrayByteList byteList = new ArrayByteList();
        byteList.add((byte)1);
        byteList.add((byte)2);
        
        Assert.assertEquals((byte)1, byteList.get(0));
        Assert.assertEquals((byte)2, byteList.get(1));
    }
    
    @Test
    public void testSize() {
        ArrayByteList byteList = new ArrayByteList();
        Assert.assertEquals(0, byteList.size());
        
        byteList.add((byte)1);
        Assert.assertEquals(1, byteList.size());
        
        byteList.add((byte)2);
        Assert.assertEquals(2, byteList.size());
    }
    
    @Test
    public void testRemoveElementAt() {
        ArrayByteList byteList = new ArrayByteList();
        
        byteList.add((byte)1);
        byteList.add((byte)2);
        byteList.add((byte)3);
        
        byte removedElement = byteList.removeElementAt(1);
        
        Assert.assertEquals(2, byteList.size());
        Assert.assertEquals((byte)2, removedElement);
        Assert.assertEquals((byte)1, byteList.get(0));
        Assert.assertEquals((byte)3, byteList.get(1));
    }
    
    @Test
    public void testSet() {
        ArrayByteList byteList = new ArrayByteList();
        byteList.add((byte)1);
        
        byte oldValue = byteList.set(0, (byte)2);
        
        Assert.assertEquals((byte)1, oldValue);
        Assert.assertEquals((byte)2, byteList.get(0));
    }
    
    @Test
    public void testAdd() {
        ArrayByteList byteList = new ArrayByteList();
        byteList.add((byte)1);
        
        Assert.assertEquals(1, byteList.size());
        Assert.assertEquals((byte)1, byteList.get(0));
        
        byteList.add(0, (byte)2);
        Assert.assertEquals(2, byteList.size());
        Assert.assertEquals((byte)2, byteList.get(0));
        Assert.assertEquals((byte)1, byteList.get(1));
    }
    
    @Test
    public void testEnsureCapacity() {
        ArrayByteList byteList = new ArrayByteList();
        byteList.ensureCapacity(10);
        
        Assert.assertEquals(0, byteList.size());
    }
    
    @Test
    public void testTrimToSize() {
        ArrayByteList byteList = new ArrayByteList();
        byteList.add((byte)1);
        byteList.add((byte)2);
        
        byteList.trimToSize();
        
        Assert.assertEquals(2, byteList.size());
    }
    
    @Test
    public void testSerialization() {
        ArrayByteList byteList = new ArrayByteList();
        byteList.add((byte)1);
        byteList.add((byte)2);
        
        try {
            byte[] serializedData = serialize(byteList);
            ArrayByteList deserializedList = deserialize(serializedData);
            
            Assert.assertEquals(2, deserializedList.size());
            Assert.assertEquals((byte)1, deserializedList.get(0));
            Assert.assertEquals((byte)2, deserializedList.get(1));
        } catch (IOException | ClassNotFoundException e) {
            Assert.fail("Exception thrown: " + e.getMessage());
        }
    }
    
    private byte[] serialize(ArrayByteList byteList) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(byteList);
        oos.close();
        return baos.toByteArray();
    }
    
    private ArrayByteList deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bais);
        ArrayByteList byteList = (ArrayByteList) ois.readObject();
        ois.close();
        return byteList;
    }
}