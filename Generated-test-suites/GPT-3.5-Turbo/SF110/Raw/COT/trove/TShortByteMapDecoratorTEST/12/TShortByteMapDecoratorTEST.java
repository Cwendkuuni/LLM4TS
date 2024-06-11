
import org.junit.Test;
import static org.junit.Assert.*;

public class TShortByteMapDecoratorTest {

    @Test
    public void testPutAndGet() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 2, (byte) 3);
        
        assertEquals((byte) 2, (byte) decorator.get((short) 1)); // Check if the value added with the key is retrieved correctly
        assertNull(decorator.get((short) 3)); // Check if a key that does not exist returns null
    }
    
    @Test
    public void testClear() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 2, (byte) 3);
        
        decorator.clear();
        assertTrue(decorator.isEmpty()); // Check if the map is empty after clearing
        assertNull(decorator.get((short) 1)); // Check if the values have been cleared
    }
    
    @Test
    public void testRemove() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 2);
        
        assertNull(decorator.remove((short) 2)); // Check if removing a key that does not exist returns null
        assertEquals((byte) 2, (byte) decorator.remove((short) 1)); // Check if the removed value is returned correctly
        assertNull(decorator.get((short) 1)); // Check if the value has been removed
    }
    
    @Test
    public void testContainsValue() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 2, (byte) 3);
        
        assertTrue(decorator.containsValue((byte) 2)); // Check if a value that exists is detected
        assertFalse(decorator.containsValue((byte) 4)); // Check if a value that does not exist is not detected
    }
    
    @Test
    public void testContainsKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 2, (byte) 3);
        
        assertTrue(decorator.containsKey((short) 1)); // Check if a key that exists is detected
        assertFalse(decorator.containsKey((short) 3)); // Check if a key that does not exist is not detected
    }
    
    @Test
    public void testSize() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 2, (byte) 3);
        
        assertEquals(2, decorator.size()); // Check if the correct size is returned
    }
    
    @Test
    public void testPutAll() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 2);
        decorator.put((short) 2, (byte) 3);
        
        Map<Short, Byte> map = new HashMap<>();
        map.put((short) 3, (byte) 4);
        map.put((short) 4, (byte) 5);
        
        decorator.putAll(map);
        assertEquals((byte) 4, (byte) decorator.get((short) 3)); // Check if the values are added correctly
        assertEquals((byte) 5, (byte) decorator.get((short) 4));
    }
    
    @Test
    public void testWrapAndUnwrapKey() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 2);
        
        assertEquals((short) 1, (short) decorator.unwrapKey(decorator.wrapKey((short) 1))); // Check if wrapping and unwrapping the key does not change its value
    }
    
    @Test
    public void testWrapAndUnwrapValue() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 2);
        
        assertEquals((byte) 2, (byte) decorator.unwrapValue(decorator.wrapValue((byte) 2))); // Check if wrapping and unwrapping the value does not change its value
    }
    
    @Test
    public void testReadExternalAndWriteExternal() {
        TShortByteMapDecorator decorator = new TShortByteMapDecorator();
        decorator.put((short) 1, (byte) 2);
        
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            
            decorator.writeExternal(objectOutputStream);
            
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            
            TShortByteMapDecorator deserializedDecorator = new TShortByteMapDecorator();
            deserializedDecorator.readExternal(objectInputStream);
            
            assertEquals((byte) 2, (byte) deserializedDecorator.get((short) 1)); // Check if the serialized and deserialized map are equal
        } catch (IOException | ClassNotFoundException e) {
            fail(); // Fail if an exception is thrown
        }
    }
}

