import org.junit.Test;
import static org.junit.Assert.*;

public class TShortByteMapDecoratorTest {

    @Test
    public void testPutAndGet() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Short key = 1;
        Byte value = 2;
        mapDecorator.put(key, value);

        assertEquals(value, mapDecorator.get(key));
    }

    @Test
    public void testClear() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((short)1, (byte)2);

        mapDecorator.clear();

        assertTrue(mapDecorator.isEmpty());
    }

    @Test
    public void testRemove() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((short)1, (byte)2);

        Byte value = mapDecorator.remove((short)1);

        assertNull(mapDecorator.get((short)1));
        assertEquals((byte)2, value.byteValue());
    }

    @Test
    public void testContainsValue() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((short)1, (byte)2);

        assertTrue(mapDecorator.containsValue((byte)2));
        assertFalse(mapDecorator.containsValue((byte)3));
    }

    @Test
    public void testContainsKey() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((short)1, (byte)2);

        assertTrue(mapDecorator.containsKey((short)1));
        assertFalse(mapDecorator.containsKey((short)2));
    }

    @Test
    public void testSize() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        mapDecorator.put((short)1, (byte)2);

        int size = mapDecorator.size();

        assertEquals(1, size);
    }

    @Test
    public void testPutAll() {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Map<Short, Byte> map = new HashMap<>();
        map.put((short)1, (byte)2);
        map.put((short)3, (byte)4);

        mapDecorator.putAll(map);

        assertEquals(2, mapDecorator.size());
        assertEquals((byte)2, mapDecorator.get((short)1).byteValue());
        assertEquals((byte)4, mapDecorator.get((short)3).byteValue());
    }

    @Test
    public void testReadExternal() throws Exception {
        TShortByteMapDecorator mapDecorator = new TShortByteMapDecorator();
        Map<Short, Byte> map = new HashMap<>();
        map.put((short)1, (byte)2);
        map.put((short)3, (byte)4);
        mapDecorator.putAll(map);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);

        mapDecorator.writeExternal(oos);
        oos.flush();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);

        TShortByteMapDecorator newMapDecorator = new TShortByteMapDecorator();
        newMapDecorator.readExternal(ois);

        assertEquals(mapDecorator.size(), newMapDecorator.size());
        assertEquals(mapDecorator.get((short)1), newMapDecorator.get((short)1));
        assertEquals(mapDecorator.get((short)3), newMapDecorator.get((short)3));
    }

}