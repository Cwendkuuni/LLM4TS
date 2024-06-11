import gnu.trove.decorator.TShortByteMapDecorator;
import gnu.trove.map.TShortByteMap;
import org.junit.Assert;
import org.junit.Test;

import java.util.NoSuchElementException;

public class TShortByteMapDecoratorTest {
    
    // Extracted public method: getMap()
    @Test
    public void testGetMap() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        Assert.assertEquals(map, decorator.getMap());
    }
    
    // Extracted public method: put(Short key, Byte value)
    @Test
    public void testPut() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        // Testing basic functionality
        Assert.assertNull(decorator.put((short) 1, (byte) 1));
        Assert.assertEquals(1, decorator.size());
        
        // Testing replacing value with new value
        Assert.assertEquals((byte) 1, (byte) decorator.put((short) 1, (byte) 2));
        Assert.assertEquals(1, decorator.size());
        
        // Testing replacing value with null value
        Assert.assertEquals((byte) 2, (byte) decorator.put((short) 1, null));
        Assert.assertEquals(1, decorator.size());
        
        // Testing replacing null key with non-null key
        Assert.assertEquals((byte) 2, (byte) decorator.put(null, (byte) 3));
        Assert.assertEquals(2, decorator.size());
        
        // Testing replacing null key with null key
        Assert.assertEquals((byte) 3, (byte) decorator.put(null, null));
        Assert.assertEquals(2, decorator.size());
    }
    
    // Extracted public method: get(Object key)
    @Test
    public void testGet() {
        TShortByteMap map = new TShortByteMap();
        map.put((short) 1, (byte) 2);
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        // Testing existing key
        Assert.assertEquals((byte) 2, (byte) decorator.get((short) 1));
        
        // Testing non-existing key
        Assert.assertNull(decorator.get((short) 2));
        
        // Testing null key
        Assert.assertNull(decorator.get(null));
    }
    
    // Extracted public method: clear()
    @Test
    public void testClear() {
        TShortByteMap map = new TShortByteMap();
        map.put((short) 1, (byte) 2);
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        decorator.clear();
        
        Assert.assertTrue(decorator.isEmpty());
    }
    
    // Extracted public method: remove(Object key)
    @Test
    public void testRemove() {
        TShortByteMap map = new TShortByteMap();
        map.put((short) 1, (byte) 2);
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        // Testing removing existing key
        Assert.assertEquals((byte) 2, (byte) decorator.remove((short) 1));
        Assert.assertEquals(0, decorator.size());
        
        // Testing removing non-existing key
        Assert.assertNull(decorator.remove((short) 2));
        
        // Testing removing null key
        Assert.assertNull(decorator.remove(null));
    }
    
    // Extracted public method: entrySet()
    @Test
    public void testEntrySet() {
        TShortByteMap map = new TShortByteMap();
        
        map.put((short) 1, (byte) 2);
        map.put((short) 2, (byte) 3);
        
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        // Testing size
        Assert.assertEquals(2, decorator.entrySet().size());
        
        // Testing isEmpty
        Assert.assertFalse(decorator.entrySet().isEmpty());
        
        // Testing contains with existing entry
        Assert.assertTrue(decorator.entrySet().contains(decorator.entrySet().iterator().next()));
        
        // Testing contains with non-existing entry
        Assert.assertFalse(decorator.entrySet().contains(null));
        
        // Testing iterator
        Assert.assertNotNull(decorator.entrySet().iterator().next());
        
        // Testing remove with existing entry
        Assert.assertTrue(decorator.entrySet().remove(decorator.entrySet().iterator().next()));
        Assert.assertEquals(1, decorator.size());
        
        // Testing remove with non-existing entry
        Assert.assertFalse(decorator.entrySet().remove(null));
        
        // Testing add
        try {
            decorator.entrySet().add(null);
            Assert.fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            // Expected UnsupportedOperationException
        }
        
        // Testing addAll
        try {
            decorator.entrySet().addAll(null);
            Assert.fail("UnsupportedOperationException not thrown");
        } catch (UnsupportedOperationException e) {
            // Expected UnsupportedOperationException
        }
        
        // Testing clear
        decorator.entrySet().clear();
        Assert.assertTrue(decorator.isEmpty());
    }
    
    // Extracted public method: containsValue(Object val)
    @Test
    public void testContainsValue() {
        TShortByteMap map = new TShortByteMap();
        
        map.put((short) 1, (byte) 2);
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        // Testing containsValue with existing value
        Assert.assertTrue(decorator.containsValue((byte) 2));
        
        // Testing containsValue with non-existing value
        Assert.assertFalse(decorator.containsValue((byte) 3));
    }
    
    // Extracted public method: containsKey(Object key)
    @Test
    public void testContainsKey() {
        TShortByteMap map = new TShortByteMap();
        
        map.put((short) 1, (byte) 2);
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        // Testing containsKey with existing key
        Assert.assertTrue(decorator.containsKey((short) 1));
        
        // Testing containsKey with non-existing key
        Assert.assertFalse(decorator.containsKey((short) 2));
        
        // Testing containsKey with null key
        Assert.assertTrue(decorator.containsKey(null));
    }
    
    // Extracted public method: size()
    @Test
    public void testSize() {
        TShortByteMap map = new TShortByteMap();
        map.put((short) 1, (byte) 2);
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        Assert.assertEquals(1, decorator.size());
    }
    
    // Extracted public method: isEmpty()
    @Test
    public void testIsEmpty() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        Assert.assertTrue(decorator.isEmpty());
    }
    
    // Extracted public method: putAll(Map<? extends Short, ? extends Byte> map)
    @Test
    public void testPutAll() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        Map<Short, Byte> sourceMap = new HashMap<>();
        sourceMap.put((short) 1, (byte) 2);
        sourceMap.put((short) 2, (byte) 3);
        
        decorator.putAll(sourceMap);
        
        Assert.assertEquals(2, decorator.size());
    }
    
    // Extracted protected method: wrapKey(short k)
    @Test
    public void testWrapKey() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        Assert.assertEquals(1, (short) decorator.wrapKey((short) 1));
    }
    
    // Extracted protected method: unwrapKey(Object key)
    @Test
    public void testUnwrapKey() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        Assert.assertEquals((short) 1, decorator.unwrapKey((short) 1));
    }
    
    // Extracted protected method: wrapValue(byte k)
    @Test
    public void testWrapValue() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        Assert.assertEquals((byte) 2, (byte) decorator.wrapValue((byte) 2));
    }
    
    // Extracted protected method: unwrapValue(Object value)
    @Test
    public void testUnwrapValue() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        Assert.assertEquals((byte) 2, decorator.unwrapValue((byte) 2));
    }
    
    // Extracted public method: readExternal(ObjectInput in)
    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(decorator);
        objectOutputStream.close();
        
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        decorator.readExternal(objectInputStream);
        objectInputStream.close();
        
        Assert.assertEquals(0, decorator.size());
    }
    
    // Extracted public method: writeExternal(ObjectOutput out)
    @Test
    public void testWriteExternal() throws IOException {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        decorator.writeExternal(objectOutputStream);
        objectOutputStream.close();
        
        Assert.assertEquals(1, byteArrayOutputStream.toByteArray().length);
    }

}