
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testPut() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        assertNull(decorator.put(1.0, (short) 1));
        assertEquals(1, decorator.size());
        assertEquals((short) 1, (short) decorator.put(2.0, (short) 2));
        assertEquals(2, decorator.size());
    }

    @Test
    public void testGet() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        assertNull(decorator.get(3.0));
        assertEquals((short) 1, (short) decorator.get(1.0));
        assertEquals((short) 2, (short) decorator.get(2.0));
    }

    @Test
    public void testClear() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        decorator.clear();
        assertEquals(0, decorator.size());
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testRemove() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        assertNull(decorator.remove(3.0));
        assertEquals((short) 1, (short) decorator.remove(1.0));
        assertEquals(1, decorator.size());
    }

    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        Set<Map.Entry<Double, Short>> entrySet = decorator.entrySet();
        assertEquals(2, entrySet.size());
        assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>(1.0, (short) 1)));
        assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>(2.0, (short) 2)));
    }

    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        assertTrue(decorator.containsValue((short) 1));
        assertTrue(decorator.containsValue((short) 2));
        assertFalse(decorator.containsValue((short) 3));
    }

    @Test
    public void testContainsKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        assertTrue(decorator.containsKey(1.0));
        assertTrue(decorator.containsKey(2.0));
        assertFalse(decorator.containsKey(3.0));
    }

    @Test
    public void testSize() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        assertEquals(0, decorator.size());
        decorator.put(1.0, (short) 1);
        assertEquals(1, decorator.size());
        decorator.put(2.0, (short) 2);
        assertEquals(2, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        assertTrue(decorator.isEmpty());
        decorator.put(1.0, (short) 1);
        assertFalse(decorator.isEmpty());
        decorator.clear();
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        Map<Double, Short> map = new HashMap<>();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        decorator.putAll(map);
        assertEquals(2, decorator.size());
        assertTrue(decorator.containsKey(1.0));
        assertTrue(decorator.containsKey(2.0));
    }

    @Test
    public void testWrapKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        assertEquals(1.0, decorator.wrapKey(1.0), 0.0);
        assertEquals(2.0, decorator.wrapKey(2.0), 0.0);
    }

    @Test
    public void testUnwrapKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        assertEquals(1.0, decorator.unwrapKey(1.0), 0.0);
        assertEquals(2.0, decorator.unwrapKey(2.0), 0.0);
    }

    @Test
    public void testWrapValue() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        assertEquals((short) 1, (short) decorator.wrapValue((short) 1));
        assertEquals((short) 2, (short) decorator.wrapValue((short) 2));
    }

    @Test
    public void testUnwrapValue() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        assertEquals((short) 1, (short) decorator.unwrapValue((short) 1));
        assertEquals((short) 2, (short) decorator.unwrapValue((short) 2));
    }
    
    @Test
    public void testReadExternal() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        try {
            decorator.readExternal(null);
            fail("Should throw IOException");
        } catch (IOException e) {
            // success
        } catch (ClassNotFoundException e) {
            fail("Should not throw ClassNotFoundException");
        }
    }
    
    @Test
    public void testWriteExternal() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        try {
            decorator.writeExternal(null);
            fail("Should throw IOException");
        } catch (IOException e) {
            // success
        }
    }
}

