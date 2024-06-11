import gnu.trove.map.TDoubleShortMap;
import gnu.trove.map.hash.TDoubleShortHashMap;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;
import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {
    private TDoubleShortMapDecorator decorator;

    @Before
    public void setUp() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        decorator = new TDoubleShortMapDecorator(map);
    }

    @Test
    public void testPutAndGet() {
        decorator.put(3.0, (short) 3);
        assertEquals(3, decorator.get(3.0).shortValue());
    }

    @Test
    public void testPutWithNullKey() {
        decorator.put(null, (short) 4);
        assertEquals(4, decorator.get(null).shortValue());
    }

    @Test
    public void testPutWithNullValue() {
        decorator.put(5.0, null);
        assertNull(decorator.get(5.0));
    }

    @Test
    public void testGetWithNullKey() {
        assertNull(decorator.get(null));
    }

    @Test
    public void testClear() {
        decorator.clear();
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testRemove() {
        decorator.remove(2.0);
        assertNull(decorator.get(2.0));
    }

    @Test
    public void testRemoveWithNullKey() {
        decorator.remove(null);
        assertNull(decorator.get(null));
    }

    @Test
    public void testContainsValue() {
        assertTrue(decorator.containsValue((short) 1));
        assertFalse(decorator.containsValue((short) 3));
    }

    @Test
    public void testContainsKey() {
        assertTrue(decorator.containsKey(1.0));
        assertFalse(decorator.containsKey(3.0));
    }

    @Test
    public void testSize() {
        assertEquals(2, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(decorator.isEmpty());
        decorator.clear();
        assertTrue(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        Map<Double, Short> map = Map.of(3.0, (short) 3, 4.0, (short) 4);
        decorator.putAll(map);
        assertEquals(4, decorator.get(3.0).shortValue());
        assertEquals(4, decorator.get(4.0).shortValue());
    }
}