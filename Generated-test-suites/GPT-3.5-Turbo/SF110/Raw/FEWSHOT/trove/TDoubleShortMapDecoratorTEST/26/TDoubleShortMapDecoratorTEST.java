package gnu.trove.decorator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.map.TDoubleShortMap;

import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class TDoubleShortMapDecoratorTest {

    @Test
    public void testPutAndGet() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();

        assertNull(decorator.put(null, (short) 1));
        assertEquals((short) 1, decorator.get(null).shortValue());

        assertNull(decorator.put(1.0, null));
        assertNull(decorator.get(1.0));

        assertNull(decorator.put(2.0, (short) 2));
        assertEquals((short) 2, decorator.get(2.0).shortValue());

        decorator.clear();
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testRemove() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);

        assertEquals((short) 1, decorator.remove(1.0).shortValue());
        assertNull(decorator.remove(1.0));
        assertNull(decorator.remove(null));
    }

    @Test
    public void testContains() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        assertTrue(decorator.containsKey(1.0));
        assertTrue(decorator.containsKey(2.0));
        assertTrue(decorator.containsKey(null));
        assertTrue(decorator.containsValue((short) 1));
        assertTrue(decorator.containsValue((short) 2));
        assertTrue(decorator.containsValue(null));
    }

    @Test
    public void testSizeAndIsEmpty() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();

        assertEquals(0, decorator.size());
        assertTrue(decorator.isEmpty());

        decorator.put(1.0, (short) 1);

        assertEquals(1, decorator.size());
        assertFalse(decorator.isEmpty());

        decorator.remove(1.0);

        assertEquals(0, decorator.size());
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        Map<Double, Short> map = new HashMap<>();
        map.put(3.0, (short) 3);
        map.put(4.0, (short) 4);

        decorator.putAll(map);

        assertEquals(4, decorator.size());
        assertTrue(decorator.containsKey(1.0));
        assertTrue(decorator.containsKey(2.0));
        assertTrue(decorator.containsKey(3.0));
        assertTrue(decorator.containsKey(4.0));
        assertTrue(decorator.containsValue((short) 1));
        assertTrue(decorator.containsValue((short) 2));
        assertTrue(decorator.containsValue((short) 3));
        assertTrue(decorator.containsValue((short) 4));
    }

    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        Set<Entry<Double, Short>> entrySet = decorator.entrySet();
        assertEquals(2, entrySet.size());

        Iterator<Entry<Double, Short>> iterator = entrySet.iterator();
        assertTrue(iterator.hasNext());

        Entry<Double, Short> entry = iterator.next();
        assertEquals(1.0, entry.getKey().doubleValue(), 0.0);
        assertEquals((short) 1, entry.getValue().shortValue());

        assertTrue(iterator.hasNext());

        entry = iterator.next();
        assertEquals(2.0, entry.getKey().doubleValue(), 0.0);
        assertEquals((short) 2, entry.getValue().shortValue());

        assertFalse(iterator.hasNext());
        iterator.remove();

        assertNull(decorator.get(2.0));
    }

    @Test
    public void testContainsEntry() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        Set<Entry<Double, Short>> entrySet = decorator.entrySet();

        Entry<Double, Short> entry = new AbstractMap.SimpleEntry<>(1.0, (short) 1);
        assertTrue(entrySet.contains(entry));

        entry = new AbstractMap.SimpleEntry<>(2.0, (short) 3);
        assertFalse(entrySet.contains(entry));
    }

    @Test
    public void testIterator() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.put(1.0, (short) 1);
        decorator.put(2.0, (short) 2);

        Set<Entry<Double, Short>> entrySet = decorator.entrySet();

        Iterator<Entry<Double, Short>> iterator = entrySet.iterator();
        assertTrue(iterator.hasNext());

        Entry<Double, Short> entry = iterator.next();
        assertEquals(1.0, entry.getKey().doubleValue(), 0.0);
        assertEquals((short) 1, entry.getValue().shortValue());

        assertTrue(iterator.hasNext());

        entry = iterator.next();
        assertEquals(2.0, entry.getKey().doubleValue(), 0.0);
        assertEquals((short) 2, entry.getValue().shortValue());

        assertFalse(iterator.hasNext());
        iterator.remove();

        assertNull(decorator.get(2.0));
    }

}