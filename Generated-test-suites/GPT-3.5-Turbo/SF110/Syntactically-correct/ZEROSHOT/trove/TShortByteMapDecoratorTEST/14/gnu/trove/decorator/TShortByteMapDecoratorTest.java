import gnu.trove.decorator.TShortByteMapDecorator;
import gnu.trove.map.TShortByteMap;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;
import java.util.Set;
import static org.junit.Assert.*;

public class TShortByteMapDecoratorTest {
    private TShortByteMapDecorator decorator;

    @Before
    public void setUp() {
        decorator = new TShortByteMapDecorator();
    }

    @Test
    public void testPut() {
        decorator.put((short) 1, (byte) 1);
        assertEquals((byte) 1, decorator.getMap().get((short) 1));
    }

    @Test
    public void testGet() {
        decorator.getMap().put((short) 1, (byte) 1);
        assertEquals((byte) 1, decorator.get((short) 1));
    }

    @Test
    public void testClear() {
        decorator.getMap().put((short) 1, (byte) 1);
        decorator.clear();
        assertTrue(decorator.isEmpty());
        assertEquals(0, decorator.size());
    }

    @Test
    public void testRemove() {
        decorator.getMap().put((short) 1, (byte) 1);
        Byte removedValue = decorator.remove((short) 1);
        assertEquals((byte) 1, removedValue.byteValue());
        assertFalse(decorator.containsKey((short) 1));
    }

    @Test
    public void testEntrySet() {
        decorator.getMap().put((short) 1, (byte) 1);
        decorator.getMap().put((short) 2, (byte) 2);
        Set<Map.Entry<Short, Byte>> entrySet = decorator.entrySet();
        assertEquals(2, entrySet.size());
        assertTrue(entrySet.contains(new Map.Entry<Short, Byte>() {
            @Override
            public Short getKey() {
                return (short) 1;
            }

            @Override
            public Byte getValue() {
                return (byte) 1;
            }

            @Override
            public Byte setValue(Byte value) {
                return null;
            }
        }));
    }

    @Test
    public void testContainsValue() {
        decorator.getMap().put((short) 1, (byte) 1);
        assertTrue(decorator.containsValue((byte) 1));
    }

    @Test
    public void testContainsKey() {
        decorator.getMap().put((short) 1, (byte) 1);
        assertTrue(decorator.containsKey((short) 1));
    }

    @Test
    public void testSize() {
        decorator.getMap().put((short) 1, (byte) 1);
        decorator.getMap().put((short) 2, (byte) 2);
        assertEquals(2, decorator.size());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(decorator.isEmpty());
        decorator.getMap().put((short) 1, (byte) 1);
        assertFalse(decorator.isEmpty());
    }

    @Test
    public void testPutAll() {
        TShortByteMap map = new TShortByteMap();
        map.put((short) 1, (byte) 1);
        map.put((short) 2, (byte) 2);
        decorator.putAll(map);
        assertEquals(2, decorator.size());
        assertEquals((byte) 1, decorator.get((short) 1).byteValue());
        assertEquals((byte) 2, decorator.get((short) 2).byteValue());
    }

    @Test
    public void testWrapKey() {
        assertEquals((short) 1, decorator.wrapKey((short) 1).shortValue());
    }

    @Test
    public void testUnwrapKey() {
        assertEquals((short) 1, decorator.unwrapKey((short) 1));
    }

    @Test
    public void testWrapValue() {
        assertEquals((byte) 1, decorator.wrapValue((byte) 1).byteValue());
    }

    @Test
    public void testUnwrapValue() {
        assertEquals((byte) 1, decorator.unwrapValue((byte) 1));
    }
}