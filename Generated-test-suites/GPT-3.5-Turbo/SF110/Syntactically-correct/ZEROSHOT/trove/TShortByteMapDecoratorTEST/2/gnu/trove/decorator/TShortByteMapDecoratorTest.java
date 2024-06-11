import org.junit.Test;
import static org.junit.Assert.assertEquals;
import gnu.trove.decorator.TShortByteMapDecorator;
import gnu.trove.map.TShortByteMap;

public class TShortByteMapDecoratorTest {

    @Test
    public void testPut() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        decorator.put((short)1, (byte)2);
        
        byte value = decorator.getMap().get((short)1);
        assertEquals(2, value);
    }
    
    @Test
    public void testGet() {
        TShortByteMap map = new TShortByteMap();
        map.put((short)1, (byte)2);
        
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        Byte value = decorator.get((short)1);
        assertEquals(2, value.byteValue());
    }
    
    @Test
    public void testClear() {
        TShortByteMap map = new TShortByteMap();
        map.put((short)1, (byte)2);
        
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        decorator.clear();
        
        assertEquals(0, decorator.getMap().size());
    }
    
    @Test
    public void testRemove() {
        TShortByteMap map = new TShortByteMap();
        map.put((short)1, (byte)2);
        
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        decorator.remove((short)1);
        
        assertEquals(0, decorator.getMap().size());
    }
    
    @Test
    public void testContainsValue() {
        TShortByteMap map = new TShortByteMap();
        map.put((short)1, (byte)2);
        
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        boolean containsValue = decorator.containsValue((byte)2);
        
        assertEquals(true, containsValue);
    }
    
    @Test
    public void testContainsKey() {
        TShortByteMap map = new TShortByteMap();
        map.put((short)1, (byte)2);
        
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        boolean containsKey = decorator.containsKey((short)1);
        
        assertEquals(true, containsKey);
    }
    
    @Test
    public void testSize() {
        TShortByteMap map = new TShortByteMap();
        map.put((short)1, (byte)2);
        
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        int size = decorator.size();
        
        assertEquals(1, size);
    }
    
    @Test
    public void testIsEmpty() {
        TShortByteMap map = new TShortByteMap();
        
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        boolean isEmpty = decorator.isEmpty();
        
        assertEquals(true, isEmpty);
    }
    
    @Test
    public void testPutAll() {
        TShortByteMap map = new TShortByteMap();
        map.put((short)1, (byte)2);
        
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        TShortByteMap newMap = new TShortByteMap();
        newMap.put((short)2, (byte)3);
        
        decorator.putAll(newMap);
        
        Byte value1 = decorator.get((short)1);
        Byte value2 = decorator.get((short)2);
        
        assertEquals(2, value1.byteValue());
        assertEquals(3, value2.byteValue());
    }
    
    @Test
    public void testWrapKey() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        Short wrappedKey = decorator.wrapKey((short)1);
        
        assertEquals((short)1, wrappedKey.shortValue());
    }
    
    @Test
    public void testUnwrapKey() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        short unwrappedKey = decorator.unwrapKey((short)1);
        
        assertEquals((short)1, unwrappedKey);
    }
    
    @Test
    public void testWrapValue() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        Byte wrappedValue = decorator.wrapValue((byte)2);
        
        assertEquals((byte)2, wrappedValue.byteValue());
    }
    
    @Test
    public void testUnwrapValue() {
        TShortByteMap map = new TShortByteMap();
        TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
        
        byte unwrappedValue = decorator.unwrapValue((byte)2);
        
        assertEquals((byte)2, unwrappedValue);
    }
}