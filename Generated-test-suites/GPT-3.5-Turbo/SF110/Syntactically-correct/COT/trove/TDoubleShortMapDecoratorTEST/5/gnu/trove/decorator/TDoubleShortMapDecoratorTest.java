
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import gnu.trove.map.*;
import java.util.*;

public class TDoubleShortMapDecoratorTest {

    private TDoubleShortMapDecorator mapDecorator;

    @Before
    public void setUp() {
        mapDecorator = new TDoubleShortMapDecorator();
    }

    @Test
    public void testPut() {
        mapDecorator.put(1.0, (short) 10);
        assertEquals(1, mapDecorator.size());
    }

    @Test
    public void testGet() {
        mapDecorator.put(1.0, (short) 10);
        assertEquals((short) 10, mapDecorator.get(1.0).shortValue());
    }

    @Test
    public void testClear() {
        mapDecorator.put(1.0, (short) 10);
        mapDecorator.clear();
        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testRemove() {
        mapDecorator.put(1.0, (short) 10);
        assertEquals((short) 10, mapDecorator.remove(1.0).shortValue());
        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testEntrySet() {
        mapDecorator.put(1.0, (short) 10);
        Set<Map.Entry<Double, Short>> entrySet = mapDecorator.entrySet();
        assertEquals(1, entrySet.size());
    }

    @Test
    public void testContainsValue() {
        mapDecorator.put(1.0, (short) 10);
        assertTrue(mapDecorator.containsValue((short) 10));
    }

    @Test
    public void testContainsKey() {
        mapDecorator.put(1.0, (short) 10);
        assertTrue(mapDecorator.containsKey(1.0));
    }

    @Test
    public void testSize() {
        mapDecorator.put(1.0, (short) 10);
        assertEquals(1, mapDecorator.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        Map<Double, Short> map = new HashMap<Double, Short>();
        map.put(1.0, (short) 10);
        mapDecorator.putAll(map);
        assertEquals(1, mapDecorator.size());
    }

}
