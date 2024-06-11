
import static org.junit.Assert.*;

import java.util.AbstractMap;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import gnu.trove.map.TShortByteMap;
import gnu.trove.map.hash.TShortByteHashMap;

public class TShortByteMapDecoratorTest {

    private TShortByteMapDecorator mapDecorator;
    
    @Before
    public void setUp() throws Exception {
        TShortByteMap map = new TShortByteHashMap();
        map.put((short) 1, (byte) 10);
        map.put((short) 2, (byte) 20);
        
        mapDecorator = new TShortByteMapDecorator(map);
    }

    @After
    public void tearDown() throws Exception {
        mapDecorator.clear();
    }

    @Test
    public void testPutAndGet() {
        mapDecorator.put((short) 3, (byte) 30);
        assertEquals(30, mapDecorator.get((short) 3).byteValue());
    }
    
    @Test
    public void testPutNullKeyAndValue() {
        mapDecorator.put(null, null);
        assertNull(mapDecorator.get(null));
    }
    
    @Test
    public void testPutNullKey() {
        mapDecorator.put(null, (byte) 40);
        assertEquals(40, mapDecorator.get(null).byteValue());
    }
    
    @Test
    public void testPutNullValue() {
        mapDecorator.put((short) 4, null);
        assertNull(mapDecorator.get((short) 4));
    }
    
    @Test
    public void testGetNonexistentKey() {
        assertNull(mapDecorator.get((short) 5));
    }
    
    @Test
    public void testRemoveKey() {
        mapDecorator.remove((short) 2);
        assertNull(mapDecorator.get((short) 2));
    }
    
    @Test
    public void testRemoveNonexistentKey() {
        assertNull(mapDecorator.remove((short) 5));
    }
    
    @Test
    public void testContainsValue() {
        assertTrue(mapDecorator.containsValue((byte) 20));
        assertFalse(mapDecorator.containsValue((byte) 30));
    }
    
    @Test
    public void testContainsKey() {
        assertTrue(mapDecorator.containsKey((short) 1));
        assertFalse(mapDecorator.containsKey((short) 3));
    }
    
    @Test
    public void testSize() {
        assertEquals(2, mapDecorator.size());
        mapDecorator.put((short) 3, (byte) 30);
        assertEquals(3, mapDecorator.size());
        mapDecorator.remove((short) 2);
        assertEquals(2, mapDecorator.size());
    }
    
    @Test
    public void testIsEmpty() {
        assertFalse(mapDecorator.isEmpty());
        mapDecorator.clear();
        assertTrue(mapDecorator.isEmpty());
    }
    
    @Test
    public void testPutAll() {
        Map<Short, Byte> otherMap = new TShortByteHashMap();
        otherMap.put((short) 3, (byte) 30);
        otherMap.put((short) 4, (byte) 40);
        
        mapDecorator.putAll(otherMap);
        assertEquals(30, mapDecorator.get((short) 3).byteValue());
        assertEquals(40, mapDecorator.get((short) 4).byteValue());
    }
    
    @Test
    public void testEntrySet() {
        Set<Map.Entry<Short, Byte>> entrySet = mapDecorator.entrySet();
        Iterator<Map.Entry<Short, Byte>> iterator = entrySet.iterator();
        
        boolean found1 = false;
        boolean found2 = false;
        while (iterator.hasNext()) {
            Map.Entry<Short, Byte> entry = iterator.next();
            if (entry.getKey() == (short) 1) {
                found1 = true;
                assertEquals(10, entry.getValue().byteValue());
            } else if (entry.getKey() == (short) 2) {
                found2 = true;
                assertEquals(20, entry.getValue().byteValue());
            }
        }
        
        assertTrue(found1);
        assertTrue(found2);
    }
    
    @Test
    public void testEntrySetContains() {
        Set<Map.Entry<Short, Byte>> entrySet = mapDecorator.entrySet();
        Map.Entry<Short, Byte> entry1 = new AbstractMap.SimpleEntry<Short, Byte>((short) 1, (byte) 10);
        Map.Entry<Short, Byte> entry2 = new AbstractMap.SimpleEntry<Short, Byte>((short) 2, (byte) 20);
        assertTrue(entrySet.contains(entry1));
        assertTrue(entrySet.contains(entry2));
    }
    
    @Test
    public void testEntrySetRemove() {
        Set<Map.Entry<Short, Byte>> entrySet = mapDecorator.entrySet();
        Map.Entry<Short, Byte> entry = new AbstractMap.SimpleEntry<Short, Byte>((short) 1, (byte) 10);
        assertTrue(entrySet.remove(entry));
        assertEquals(1, mapDecorator.size());
        assertNull(mapDecorator.get((short) 1));
    }
    
    @Test
    public void testEntrySetSize() {
        Set<Map.Entry<Short, Byte>> entrySet = mapDecorator.entrySet();
        assertEquals(2, entrySet.size());
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testEntrySetAdd() {
        Set<Map.Entry<Short, Byte>> entrySet = mapDecorator.entrySet();
        Map.Entry<Short, Byte> entry = new AbstractMap.SimpleEntry<Short, Byte>((short) 3, (byte) 30);
        entrySet.add(entry);
    }
    
    @Test
    public void testEntrySetAddAll() {
        Set<Map.Entry<Short, Byte>> entrySet = mapDecorator.entrySet();
        Collection<Map.Entry<Short, Byte>> entries = new ArrayList<>();
        entries.add(new AbstractMap.SimpleEntry<Short, Byte>((short) 3, (byte) 30));
        entries.add(new AbstractMap.SimpleEntry<Short, Byte>((short) 4, (byte) 40));
        
        assertFalse(entrySet.addAll(entries));
        assertEquals(2, mapDecorator.size());
        
        entries.add(new AbstractMap.SimpleEntry<Short, Byte>((short) 5, (byte) 50));
        assertTrue(entrySet.addAll(entries));
        assertEquals(5, mapDecorator.size());
    }
    
    @Test
    public void testEntrySetContainsAll() {
        Set<Map.Entry<Short, Byte>> entrySet = mapDecorator.entrySet();
        Collection<Map.Entry<Short, Byte>> entries = new ArrayList<>();
        entries.add(new AbstractMap.SimpleEntry<Short, Byte>((short) 1, (byte) 10));
        entries.add(new AbstractMap.SimpleEntry<Short, Byte>((short) 2, (byte) 20));
        
        assertTrue(entrySet.containsAll(entries));
        
        entries.add(new AbstractMap.SimpleEntry<Short, Byte>((short) 3, (byte) 30));
        assertFalse(entrySet.containsAll(entries));
    }
    
    @Test
    public void testEntrySetRemoveAll() {
        Set<Map.Entry<Short, Byte>> entrySet = mapDecorator.entrySet();
        Collection<Map.Entry<Short, Byte>> entries = new ArrayList<>();
        entries.add(new AbstractMap.SimpleEntry<Short, Byte>((short) 1, (byte) 10));
        entries.add(new AbstractMap.SimpleEntry<Short, Byte>((short) 2, (byte) 20));
        
        assertTrue(entrySet.removeAll(entries));
        assertTrue(entrySet.isEmpty());
        assertEquals(0, mapDecorator.size());
    }
    
    @Test
    public void testEntrySetRetainAll() {
        Set<Map.Entry<Short, Byte>> entrySet = mapDecorator.entrySet();
        Collection<Map.Entry<Short, Byte>> entries = new ArrayList<>();
        entries.add(new AbstractMap.SimpleEntry<Short, Byte>((short) 1, (byte) 10));
        entries.add(new AbstractMap.SimpleEntry<Short, Byte>((short) 2, (byte) 20));
        
        assertFalse(entrySet.retainAll(entries));
        assertEquals(2, entrySet.size());
        
        entries.remove(0);
        assertTrue(entrySet.retainAll(entries));
        assertEquals(1, entrySet.size());
    }
    
    @Test
    public void testEntrySetClear() {
        Set<Map.Entry<Short, Byte>> entrySet = mapDecorator.entrySet();
        entrySet.clear();
        assertEquals(0, entrySet.size());
        assertEquals(0, mapDecorator.size());
    }

    @Test
    public void testReadExternalAndWriteExternal() throws Exception {
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        ObjectOutputStream objectOutput = new ObjectOutputStream(byteOutput);
        
        mapDecorator.writeExternal(objectOutput);
        objectOutput.flush();
        
        byte[] byteArray = byteOutput.toByteArray();
        objectOutput.close();
        byteOutput.close();
        
        ByteArrayInputStream byteInput = new ByteArrayInputStream(byteArray);
        ObjectInputStream objectInput = new ObjectInputStream(byteInput);
        
        TShortByteMapDecorator mapDecoratorClone = new TShortByteMapDecorator();
        mapDecoratorClone.readExternal(objectInput);
        
        objectInput.close();
        byteInput.close();
        
        assertEquals(mapDecorator.size(), mapDecoratorClone.size());
        assertEquals(mapDecorator.get((short) 1), mapDecoratorClone.get((short) 1));
        assertEquals(mapDecorator.get((short) 2), mapDecoratorClone.get((short) 2));
    }

}
