package gnu.trove.decorator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import gnu.trove.iterator.TDoubleShortIterator;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.map.hash.TDoubleShortHashMap;
import org.junit.Before;
import org.junit.Test;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class TDoubleShortMapDecoratorTest {

    private TDoubleShortMapDecorator decorator;

    @Before
    public void setup() {
        decorator = new TDoubleShortMapDecorator(new TDoubleShortHashMap());
    }
    
    @Test
    public void testPutAndGet() {
        Double key = 1.0;
        Short value = 2;
        assertNull(decorator.put(key, value));
        assertTrue(decorator.containsKey(key));
        assertTrue(decorator.containsValue(value));
        assertEquals(value, decorator.get(key));
    }

    @Test
    public void testClear() {
        Double key = 1.0;
        Short value = 2;
        decorator.put(key, value);
        decorator.clear();
        assertTrue(decorator.isEmpty());
        assertNull(decorator.get(key));
    }

    @Test
    public void testRemove() {
        Double key = 1.0;
        Short value = 2;
        decorator.put(key, value);
        assertEquals(value, decorator.remove(key));
        assertTrue(decorator.isEmpty());
        assertNull(decorator.get(key));
    }

    @Test
    public void testEntrySet() {
        Double key1 = 1.0;
        Short value1 = 2;
        Double key2 = 3.0;
        Short value2 = 4;

        decorator.put(key1, value1);
        decorator.put(key2, value2);

        Set<Map.Entry<Double, Short>> entrySet = decorator.entrySet();
        assertEquals(2, entrySet.size());

        Iterator<Map.Entry<Double, Short>> iterator = entrySet.iterator();
        assertTrue(iterator.hasNext());

        Map.Entry<Double, Short> entry1 = iterator.next();
        assertTrue(entry1.getKey().equals(key1) && entry1.getValue().equals(value1));

        Map.Entry<Double, Short> entry2 = iterator.next();
        assertTrue(entry2.getKey().equals(key2) && entry2.getValue().equals(value2));
    }

    @Test
    public void testContainsKey() {
        Double key = 1.0;
        Short value = 2;
        decorator.put(key, value);
        assertTrue(decorator.containsKey(key));
        assertFalse(decorator.containsKey(2.0));
    }

    @Test
    public void testContainsValue() {
        Double key = 1.0;
        Short value = 2;
        decorator.put(key, value);
        assertTrue(decorator.containsValue(value));
        assertFalse(decorator.containsValue(3));
    }

    @Test
    public void testSize() {
        assertEquals(0, decorator.size());
        decorator.put(1.0, 2);
        decorator.put(3.0, 4);
        assertEquals(2, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(decorator.isEmpty());
        decorator.put(1.0, 2);
        assertFalse(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        Map<Double, Short> map = new HashMap<>();
        map.put(1.0, 2);
        map.put(3.0, 4);
        decorator.putAll(map);
        assertEquals(2, decorator.size());
        assertTrue(decorator.containsKey(1.0));
        assertTrue(decorator.containsKey(3.0));
    }

    @Test
    public void testWrapKey() {
        double k = 1.0;
        Double wrappedKey = decorator.wrapKey(k);
        assertTrue(wrappedKey == k);
    }

    @Test
    public void testUnwrapKey() {
        Double key = 1.0;
        double unwrappedKey = decorator.unwrapKey(key);
        assertTrue(unwrappedKey == key);
    }

    @Test
    public void testWrapValue() {
        short k = 2;
        Short wrappedValue = decorator.wrapValue(k);
        assertTrue(wrappedValue == k);
    }

    @Test
    public void testUnwrapValue() {
        Short value = 2;
        short unwrappedValue = decorator.unwrapValue(value);
        assertTrue(unwrappedValue == value);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddEntry() {
        decorator.entrySet().add(new AbstractMap.SimpleEntry<>(1.0, 2));
    }

    @Test
    public void testRemoveEntry() {
        Double key = 1.0;
        Short value = 2;
        decorator.put(key, value);
        Map.Entry<Double, Short> entry = new AbstractMap.SimpleEntry<>(key, value);
        assertTrue(decorator.entrySet().remove(entry));
        assertTrue(decorator.isEmpty());
        assertNull(decorator.get(key));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testAddAll() {
        decorator.entrySet().addAll(new ArrayList<>());
    }

    @Test
    public void testEntryHashCode() {
        Double key1 = 1.0;
        Double key2 = 1.0;
        Short value1 = 2;
        Short value2 = 2;

        Map.Entry<Double, Short> entry1 = new AbstractMap.SimpleEntry<>(key1, value1);
        Map.Entry<Double, Short> entry2 = new AbstractMap.SimpleEntry<>(key2, value2);

        assertTrue(entry1.hashCode() == entry2.hashCode());
    }

    @Test
    public void testEntryEquals() {
        Double key1 = 1.0;
        Double key2 = 1.0;
        Short value1 = 2;
        Short value2 = 2;

        Map.Entry<Double, Short> entry1 = new AbstractMap.SimpleEntry<>(key1, value1);
        Map.Entry<Double, Short> entry2 = new AbstractMap.SimpleEntry<>(key2, value2);

        assertTrue(entry1.equals(entry2));
    }

    @Test
    public void testEntrySetValue() {
        Double key = 1.0;
        Short value1 = 2;
        Short value2 = 4;

        decorator.put(key, value1);

        Iterator<Map.Entry<Double, Short>> iterator = decorator.entrySet().iterator();
        assertTrue(iterator.hasNext());
        Map.Entry<Double, Short> entry = iterator.next();

        entry.setValue(value2);
        assertTrue(decorator.containsKey(key));
        assertTrue(decorator.containsValue(value2));
        assertEquals(value2, decorator.get(key));
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TDoubleShortMap map = new TDoubleShortHashMap();
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.getMap().put(1.0, (short) 2);
        decorator.getMap().put(3.0, (short) 4);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        decorator.writeExternal(objectOutputStream);
        objectOutputStream.flush();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        decorator.readExternal(objectInputStream);

        assertEquals(2, decorator.size());
        assertTrue(decorator.containsKey(1.0));
        assertTrue(decorator.containsKey(3.0));
    }

}