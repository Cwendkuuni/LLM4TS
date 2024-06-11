package gnu.trove.decorator;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

public class TDoubleShortMapDecoratorTest {
    @Test
    public void testPutAndGet() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        
        map.put(1.0, (short)1);
        map.put(2.0, (short)2);
        map.put(3.0, (short)3);
        
        assertEquals(3, map.size());
        assertEquals((short)1, (short)map.get(1.0));
        assertEquals((short)2, (short)map.get(2.0));
        assertEquals((short)3, (short)map.get(3.0));
    }
    
    @Test
    public void testClear() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        
        map.put(1.0, (short)1);
        map.put(2.0, (short)2);
        map.put(3.0, (short)3);
        
        map.clear();
        
        assertTrue(map.isEmpty());
    }
    
    @Test
    public void testRemove() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        
        map.put(1.0, (short)1);
        map.put(2.0, (short)2);
        map.put(3.0, (short)3);
        
        map.remove(2.0);
        
        assertNull(map.get(2.0));
        assertEquals(2, map.size());
    }
    
    @Test
    public void testContainsKey() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        
        map.put(1.0, (short)1);
        map.put(2.0, (short)2);
        map.put(3.0, (short)3);
        
        assertTrue(map.containsKey(1.0));
        assertFalse(map.containsKey(4.0));
    }
    
    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        
        map.put(1.0, (short)1);
        map.put(2.0, (short)2);
        map.put(3.0, (short)3);
        
        assertTrue(map.containsValue((short)1));
        assertFalse(map.containsValue((short)4));
    }
    
    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        
        map.put(1.0, (short)1);
        map.put(2.0, (short)2);
        map.put(3.0, (short)3);
        
        Set<Map.Entry<Double, Short>> entrySet = map.entrySet();
        
        assertEquals(3, entrySet.size());
        
        Map<Double, Short> expected = new HashMap<>();
        expected.put(1.0, (short)1);
        expected.put(2.0, (short)2);
        expected.put(3.0, (short)3);
        
        for (Map.Entry<Double, Short> entry : entrySet) {
            assertTrue(expected.containsKey(entry.getKey()));
            assertTrue(expected.containsValue(entry.getValue()));
        }
    }
    
    @Test
    public void testPutAll() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        
        Map<Double, Short> newMap = new HashMap<>();
        newMap.put(1.0, (short)1);
        newMap.put(2.0, (short)2);
        
        map.putAll(newMap);
        
        assertEquals(2, map.size());
        assertEquals((short)1, (short)map.get(1.0));
        assertEquals((short)2, (short)map.get(2.0));
    }
    
    @Test
    public void testClone() {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        
        map.put(1.0, (short)1);
        map.put(2.0, (short)2);
        
        TDoubleShortMapDecorator clonedMap = new TDoubleShortMapDecorator(map.getMap());
        
        assertEquals(2, clonedMap.size());
        assertEquals((short)1, (short)clonedMap.get(1.0));
        assertEquals((short)2, (short)clonedMap.get(2.0));
    }
    
    @Test
    public void testExternalizable() throws IOException, ClassNotFoundException {
        TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
        
        map.put(1.0, (short)1);
        map.put(2.0, (short)2);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        map.writeExternal(oos);
        oos.close();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        TDoubleShortMapDecorator deserializedMap = new TDoubleShortMapDecorator();
        deserializedMap.readExternal(ois);
        ois.close();
        
        assertEquals(2, deserializedMap.size());
        assertEquals((short)1, (short)deserializedMap.get(1.0));
        assertEquals((short)2, (short)deserializedMap.get(2.0));
    }
}