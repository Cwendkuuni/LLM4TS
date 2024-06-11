import gnu.trove.map.TDoubleShortMap;
import gnu.trove.map.hash.TDoubleShortHashMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class TDoubleShortMapDecoratorTest {
    private TDoubleShortMapDecorator mapDecorator;

    @Before
    public void setUp() {
        TDoubleShortMap map = new TDoubleShortHashMap();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        mapDecorator = new TDoubleShortMapDecorator(map);
    }

    @Test
    public void testPutAndGet() {
        mapDecorator.put(3.0, (short) 3);

        Assert.assertEquals((short) 3, mapDecorator.get(3.0).shortValue());
    }

    @Test
    public void testPutNullKey() {
        mapDecorator.put(null, (short) 1);

        Assert.assertEquals((short) 1, mapDecorator.get(null).shortValue());
    }

    @Test
    public void testPutNullValue() {
        mapDecorator.put(4.0, null);

        Assert.assertNull(mapDecorator.get(4.0));
    }

    @Test
    public void testRemove() {
        mapDecorator.remove(1.0);

        Assert.assertNull(mapDecorator.get(1.0));
    }

    @Test
    public void testRemoveNullKey() {
        mapDecorator.remove(null);

        Assert.assertNull(mapDecorator.get(null));
    }

    @Test
    public void testContainsKey() {
        Assert.assertTrue(mapDecorator.containsKey(1.0));
        Assert.assertFalse(mapDecorator.containsKey(3.0));
    }

    @Test
    public void testContainsValue() {
        Assert.assertTrue(mapDecorator.containsValue((short) 1));
        Assert.assertFalse(mapDecorator.containsValue((short) 3));
    }

    @Test
    public void testSize() {
        Assert.assertEquals(2, mapDecorator.size());

        mapDecorator.put(3.0, (short) 3);

        Assert.assertEquals(3, mapDecorator.size());
    }

    @Test
    public void testIsEmpty() {
        Assert.assertFalse(mapDecorator.isEmpty());

        mapDecorator.clear();

        Assert.assertTrue(mapDecorator.isEmpty());
    }
    
    @Test
    public void testClear() {
        mapDecorator.clear();
        
        Assert.assertEquals(0, mapDecorator.size());
    }
    
    @Test
    public void testPutAll() {
        Map<Double, Short> otherMap = new HashMap<>();
        otherMap.put(3.0, (short) 3);
        otherMap.put(4.0, (short) 4);
        
        mapDecorator.putAll(otherMap);
        
        Assert.assertEquals(4, mapDecorator.size());
        Assert.assertEquals((short) 3, mapDecorator.get(3.0).shortValue());
        Assert.assertEquals((short) 4, mapDecorator.get(4.0).shortValue());
    }
}