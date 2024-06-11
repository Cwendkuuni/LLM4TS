import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.*;
import java.util.*;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testPutAndGet() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);
        
        assertEquals((short) 1, (short) decorator.get(1.0));
        assertEquals((short) 2, (short) decorator.get(2.0));
    }

    @Test
    public void testClear() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        decorator.clear();

        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testRemove() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        decorator.remove(1.0);

        assertNull(decorator.get(1.0));
    }

    @Test
    public void testContainsKey() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        assertTrue(decorator.containsKey(1.0));
        assertFalse(decorator.containsKey(3.0));
    }

    @Test
    public void testContainsValue() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        assertTrue(decorator.containsValue((short) 1));
        assertFalse(decorator.containsValue((short) 3));
    }

    @Test
    public void testSize() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        assertEquals(2, decorator.size());
    }

    @Test
    public void testPutAll() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        Map<Double, Short> inputMap = new HashMap<>();
        inputMap.put(1.0, (short) 1);
        inputMap.put(2.0, (short) 2);

        decorator.putAll(inputMap);

        assertEquals((short) 1, (short) decorator.get(1.0));
        assertEquals((short) 2, (short) decorator.get(2.0));
    }

    @Test
    public void testEntrySet() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        Set<Map.Entry<Double, Short>> entrySet = decorator.entrySet();

        assertEquals(2, entrySet.size());

        for (Map.Entry<Double, Short> entry : entrySet) {
            double key = entry.getKey();
            short value = entry.getValue();

            assertTrue(key == 1.0 || key == 2.0);
            assertTrue(value == (short) 1 || value == (short) 2);
        }
    }

    @Test
    public void testIterator() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);

        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        Iterator<Map.Entry<Double, Short>> iterator = decorator.entrySet().iterator();

        assertTrue(iterator.hasNext());

        while (iterator.hasNext()) {
            Map.Entry<Double, Short> entry = iterator.next();
            double key = entry.getKey();
            short value = entry.getValue();

            assertTrue(key == 1.0 || key == 2.0);
            assertTrue(value == (short) 1 || value == (short) 2);
        }
    }
}