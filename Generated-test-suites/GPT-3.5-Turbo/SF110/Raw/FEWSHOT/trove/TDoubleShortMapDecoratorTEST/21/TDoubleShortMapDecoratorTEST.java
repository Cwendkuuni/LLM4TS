import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.apache.commons.cli.Util;

public class TDoubleShortMapDecoratorTest {
    @Test
    public void testPutAndGet() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        
        assertEquals((short) 1, map.get(1.0).shortValue());
        assertEquals((short) 2, map.get(2.0).shortValue());
    }

    @Test
    public void testRemove() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        
        assertEquals((short) 1, map.remove(1.0).shortValue());
        assertNull(map.get(1.0));
    }
    
    @Test
    public void testSize() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        
        assertEquals(0, map.size());
        
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        
        assertEquals(2, map.size());
        
        map.remove(1.0);
        
        assertEquals(1, map.size());
        
        map.clear();
        
        assertEquals(0, map.size());
    }
    
    @Test
    public void testContainsKeyAndValue() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        
        assertEquals(true, map.containsKey(1.0));
        assertEquals(false, map.containsKey(2.0));
        
        assertEquals(true, map.containsValue((short) 1));
        assertEquals(false, map.containsValue((short) 2));
    }
    
    @Test
    public void testClear() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        
        map.clear();
        
        assertEquals(0, map.size());
        assertNull(map.get(1.0));
        assertNull(map.get(2.0));
    }
    
    @Test
    public void testWrapAndUnwrapKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        
        double key1 = 1.0;
        Double wrappedKey1 = map.wrapKey(key1);
        double unwrappedKey1 = map.unwrapKey(wrappedKey1);
        assertEquals(key1, unwrappedKey1, 0.0);
        
        double key2 = 2.0;
        Double wrappedKey2 = map.wrapKey(key2);
        double unwrappedKey2 = map.unwrapKey(wrappedKey2);
        assertEquals(key2, unwrappedKey2, 0.0);
    }
    
    @Test
    public void testWrapAndUnwrapValue() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        
        short value1 = 1;
        Short wrappedValue1 = map.wrapValue(value1);
        short unwrappedValue1 = map.unwrapValue(wrappedValue1);
        assertEquals(value1, unwrappedValue1);
        
        short value2 = 2;
        Short wrappedValue2 = map.wrapValue(value2);
        short unwrappedValue2 = map.unwrapValue(wrappedValue2);
        assertEquals(value2, unwrappedValue2);
    }
    
    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        
        Set<Entry<Double, Short>> entrySet = map.entrySet();
        
        assertEquals(2, entrySet.size());
        
        for (Entry<Double, Short> entry : entrySet) {
            double key = entry.getKey();
            short value = entry.getValue();
            
            if (key == 1.0) {
                assertEquals((short) 1, value);
            } else if (key == 2.0) {
                assertEquals((short) 2, value);
            }
        }
    }
}