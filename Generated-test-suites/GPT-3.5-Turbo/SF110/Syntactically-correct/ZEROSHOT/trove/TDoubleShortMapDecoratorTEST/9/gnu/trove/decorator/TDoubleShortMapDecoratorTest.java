import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testPut() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        assertEquals((short) 1, map.get(1.0).shortValue());
        assertEquals((short) 2, map.get(2.0).shortValue());
    }
    
    @Test
    public void testGet() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        assertEquals((short) 1, map.get(1.0).shortValue());
    }
    
    @Test
    public void testClear() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.clear();
        assertTrue(map.isEmpty());
    }
    
    @Test
    public void testRemove() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        Short removed = map.remove(1.0);
        assertEquals((short) 1, removed.shortValue());
        assertNull(map.get(1.0));
    }
    
    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        
        Set<Map.Entry<Double, Short>> entrySet = map.entrySet();
        assertEquals(2, entrySet.size());
        
        for (Map.Entry<Double, Short> entry : entrySet) {
            double key = entry.getKey();
            short value = entry.getValue();
            if (key == 1.0) {
                assertEquals((short) 1, value);
            } else if (key == 2.0) {
                assertEquals((short) 2, value);
            } else {
                fail("Unknown key: " + key);
            }
        }
    }
    
    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        assertTrue(map.containsValue((short) 1));
    }
    
    @Test
    public void testContainsKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        assertTrue(map.containsKey(1.0));
    }
    
    @Test
    public void testSize() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertEquals(0, map.size());
        map.put(1.0, (short) 1);
        assertEquals(1, map.size());
    }
    
    @Test
    public void testIsEmpty() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertTrue(map.isEmpty());
        map.put(1.0, (short) 1);
        assertFalse(map.isEmpty());
    }
    
    @Test
    public void testPutAll() {
        Map<Double, Short> sourceMap = new HashMap<>();
        sourceMap.put(1.0, (short) 1);
        sourceMap.put(2.0, (short) 2);
        
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.putAll(sourceMap);
        
        assertEquals((short) 1, map.get(1.0).shortValue());
        assertEquals((short) 2, map.get(2.0).shortValue());
    }
    
    @Test
    public void testWrapKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertEquals(1.0, map.wrapKey(1.0), 0.0001);
    }
    
    @Test
    public void testUnwrapKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertEquals(1.0, map.unwrapKey(1.0), 0.0001);
    }
    
    @Test
    public void testWrapValue() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertEquals((short) 1, map.wrapValue((short) 1).shortValue());
    }
    
    @Test
    public void testUnwrapValue() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertEquals((short) 1, map.unwrapValue((short) 1));
    }
    
}