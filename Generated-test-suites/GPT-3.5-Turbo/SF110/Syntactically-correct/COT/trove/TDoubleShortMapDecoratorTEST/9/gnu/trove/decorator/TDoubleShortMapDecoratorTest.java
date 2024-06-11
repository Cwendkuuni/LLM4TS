
import org.junit.Test;
import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {
    @Test
    public void testPut() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        assertNull(mapDecorator.put(1.0, (short) 1));
        assertEquals((Short) 1, mapDecorator.put(1.0, (short) 2));
    }
    
    @Test
    public void testGet() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(1.0, (short) 1);
        assertEquals((Short) 1, mapDecorator.get(1.0));
        assertNull(mapDecorator.get(2.0));
    }
    
    @Test
    public void testClear() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(1.0, (short) 1);
        mapDecorator.clear();
        assertTrue(mapDecorator.isEmpty());
    }
    
    @Test
    public void testRemove() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(1.0, (short) 1);
        assertEquals((Short) 1, mapDecorator.remove(1.0));
        assertNull(mapDecorator.remove(2.0));
    }
    
    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(1.0, (short) 1);
        mapDecorator.put(2.0, (short) 2);
        mapDecorator.put(3.0, (short) 3);
        assertEquals(3, mapDecorator.entrySet().size());
        assertTrue(mapDecorator.entrySet().contains(new AbstractMap.SimpleEntry<Double, Short>(1.0, (short) 1)));
        assertFalse(mapDecorator.entrySet().contains(new AbstractMap.SimpleEntry<Double, Short>(4.0, (short) 4)));
        mapDecorator.entrySet().remove(new AbstractMap.SimpleEntry<Double, Short>(2.0, (short) 2));
        assertNull(mapDecorator.get(2.0));
    }
    
    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(1.0, (short) 1);
        assertTrue(mapDecorator.containsValue((short) 1));
        assertFalse(mapDecorator.containsValue((short) 2));
    }
    
    @Test
    public void testContainsKey() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        mapDecorator.put(1.0, (short) 1);
        assertTrue(mapDecorator.containsKey(1.0));
        assertFalse(mapDecorator.containsKey(2.0));
        assertFalse(mapDecorator.containsKey("2.0"));
    }
    
    @Test
    public void testSize() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        assertEquals(0, mapDecorator.size());
        mapDecorator.put(1.0, (short) 1);
        assertEquals(1, mapDecorator.size());
    }
    
    @Test
    public void testIsEmpty() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        assertTrue(mapDecorator.isEmpty());
        mapDecorator.put(1.0, (short) 1);
        assertFalse(mapDecorator.isEmpty());
    }
    
    @Test
    public void testPutAll() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        Map<Double, Short> map = new HashMap<>();
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.put(3.0, (short) 3);
        mapDecorator.putAll(map);
        assertEquals((Short) 1, mapDecorator.get(1.0));
        assertEquals((Short) 2, mapDecorator.get(2.0));
        assertEquals((Short) 3, mapDecorator.get(3.0));
    }
    
    @Test
    public void testWrapKey() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        assertEquals((Double) 1.0, mapDecorator.wrapKey(1.0));
    }
    
    @Test
    public void testUnwrapKey() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        assertEquals(1.0, mapDecorator.unwrapKey(1.0), 0.001);
    }
    
    @Test
    public void testWrapValue() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        assertEquals((Short) 1, mapDecorator.wrapValue((short) 1));
    }
    
    @Test
    public void testUnwrapValue() {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        assertEquals((short) 1, mapDecorator.unwrapValue((short) 1));
    }
    
    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(mapDecorator);
        oos.flush();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        TDoubleShortMapDecorator newMapDecorator = (TDoubleShortMapDecorator) ois.readObject();
        assertEquals(mapDecorator, newMapDecorator);
    }
    
    @Test
    public void testWriteExternal() throws IOException, ClassNotFoundException {
        TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(mapDecorator);
        oos.flush();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        TDoubleShortMapDecorator newMapDecorator = (TDoubleShortMapDecorator) ois.readObject();
        assertEquals(mapDecorator, newMapDecorator);
    }
}
