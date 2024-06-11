
import org.junit.Test;
import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testPut() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertNull(map.put(1.0, (short) 1));
        assertEquals(1, map.size());
        
        assertNull(map.put(null, null));
        assertEquals(2, map.size());
    }

    @Test
    public void testGet() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        
        Short value = map.get(1.0);
        assertNotNull(value);
        assertEquals(1, value.shortValue());
        
        value = map.get(null);
        assertNull(value);
    }

    @Test
    public void testClear() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        
        assertFalse(map.isEmpty());
        map.clear();
        assertTrue(map.isEmpty());
    }

    @Test
    public void testRemove() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        
        Short value = map.remove(1.0);
        assertNotNull(value);
        assertEquals(1, value.shortValue());
        
        value = map.remove(null);
        assertNull(value);
    }

    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        
        assertEquals(2, map.entrySet().size());
        assertTrue(map.entrySet().contains(new AbstractMap.SimpleEntry<Double, Short>(1.0, (short) 1)));
        
        map.entrySet().remove(new AbstractMap.SimpleEntry<Double, Short>(1.0, (short) 1));
        assertFalse(map.entrySet().contains(new AbstractMap.SimpleEntry<Double, Short>(1.0, (short) 1)));
    }

    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        
        assertTrue(map.containsValue((short) 1));
        assertFalse(map.containsValue((short) 3));
    }

    @Test
    public void testContainsKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        
        assertTrue(map.containsKey(1.0));
        assertFalse(map.containsKey(3.0));
    }

    @Test
    public void testSize() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        assertEquals(0, map.size());
        
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        assertEquals(2, map.size());
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
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        
        Map<Double, Short> otherMap = new HashMap<>();
        otherMap.put(1.0, (short) 1);
        otherMap.put(2.0, (short) 2);

        map.putAll(otherMap);

        assertEquals(2, map.size());
        assertTrue(map.containsKey(1.0));
        assertTrue(map.containsValue((short) 1));
    }
}

