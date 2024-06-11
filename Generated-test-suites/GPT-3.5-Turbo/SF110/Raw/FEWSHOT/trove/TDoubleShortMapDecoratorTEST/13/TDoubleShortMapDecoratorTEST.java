import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class TDoubleShortMapDecoratorTest {
    private TDoubleShortMapDecorator map;
    
    @Before
    public void setUp() {
        map = new TDoubleShortMapDecorator();
    }
    
    @Test
    public void testPutAndGet() {
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.put(3.0, (short) 3);
        
        assertEquals(1, map.get(1.0).shortValue());
        assertEquals(2, map.get(2.0).shortValue());
        assertEquals(3, map.get(3.0).shortValue());
    }
    
    @Test
    public void testPutNullKey() {
        map.put(null, (short) 1);
        
        assertNull(map.get(null));
    }
    
    @Test
    public void testPutNullValue() {
        map.put(1.0, null);
        
        assertNull(map.get(1.0));
    }
    
    @Test
    public void testRemove() {
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        
        assertEquals(2, map.size());
        
        map.remove(1.0);
        
        assertEquals(1, map.size());
        assertNull(map.get(1.0));
        assertEquals(2, map.get(2.0).shortValue());
    }
    
    @Test
    public void testClear() {
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        
        assertEquals(2, map.size());
        
        map.clear();
        
        assertEquals(0, map.size());
        assertNull(map.get(1.0));
        assertNull(map.get(2.0));
    }
    
    @Test
    public void testContainsKey() {
        map.put(1.0, (short) 1);
        
        assertTrue(map.containsKey(1.0));
        assertFalse(map.containsKey(2.0));
    }
    
    @Test
    public void testContainsValue() {
        map.put(1.0, (short) 1);
        
        assertTrue(map.containsValue((short) 1));
        assertFalse(map.containsValue((short) 2));
    }
    
    @Test
    public void testSize() {
        assertEquals(0, map.size());
        
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        
        assertEquals(2, map.size());
        
        map.remove(1.0);
        
        assertEquals(1, map.size());
    }
    
    @Test
    public void testIsEmpty() {
        assertTrue(map.isEmpty());
        
        map.put(1.0, (short) 1);
        
        assertFalse(map.isEmpty());
        
        map.remove(1.0);
        
        assertTrue(map.isEmpty());
    }
    
    @Test
    public void testEntrySet() {
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        
        Set<Map.Entry<Double, Short>> entrySet = map.entrySet();
        
        assertEquals(2, entrySet.size());
        
        for (Map.Entry<Double, Short> entry : entrySet) {
            assertTrue(entry.getKey().equals(1.0) || entry.getKey().equals(2.0));
            assertTrue(entry.getValue().shortValue() == 1 || entry.getValue().shortValue() == 2);
        }
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testEntrySetAdd() {
        Set<Map.Entry<Double, Short>> entrySet = map.entrySet();
        entrySet.add(new AbstractMap.SimpleEntry<Double, Short>(1.0, (short) 1));
    }
    
    @Test
    public void testEntrySetRemove() {
        map.put(1.0, (short) 1);
        
        Set<Map.Entry<Double, Short>> entrySet = map.entrySet();
        
        assertTrue(entrySet.remove(new AbstractMap.SimpleEntry<Double, Short>(1.0, (short) 1)));
        assertFalse(entrySet.remove(new AbstractMap.SimpleEntry<Double, Short>(2.0, (short) 2)));
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testEntrySetAddAll() {
        Set<Map.Entry<Double, Short>> entrySet = map.entrySet();
        entrySet.addAll(new ArrayList<Map.Entry<Double, Short>>());
    }
    
    @Test
    public void testEntrySetClear() {
        map.put(1.0, (short) 1);
        
        Set<Map.Entry<Double, Short>> entrySet = map.entrySet();
        
        assertEquals(1, entrySet.size());
        
        entrySet.clear();
        
        assertEquals(0, entrySet.size());
        assertEquals(0, map.size());
        assertNull(map.get(1.0));
    }
    
    @Test
    public void testPutAll() {
        Map<Double, Short> testMap = new HashMap<>();
        testMap.put(1.0, (short) 1);
        testMap.put(2.0, (short) 2);
        
        map.putAll(testMap);
        
        assertEquals(2, map.size());
        assertTrue(map.get(1.0).shortValue() == 1);
        assertTrue(map.get(2.0).shortValue() == 2);
    }
    
    @Test
    public void testWrapAndUnwrapKey() {
        double key = 1.0;
        Double wrappedKey = map.wrapKey(key);
        double unwrappedKey = map.unwrapKey(wrappedKey);
        
        assertEquals(key, unwrappedKey, 0.0);
    }
    
    @Test
    public void testWrapAndUnwrapValue() {
        short value = (short) 1;
        Short wrappedValue = map.wrapValue(value);
        short unwrappedValue = map.unwrapValue(wrappedValue);
        
        assertEquals(value, unwrappedValue);
    }
    
    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TDoubleShortMapDecorator testMap = new TDoubleShortMapDecorator();
        testMap.put(1.0, (short) 1);
        testMap.put(2.0, (short) 2);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        
        testMap.writeExternal(oos);
        oos.flush();
        baos.flush();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        
        TDoubleShortMapDecorator resultMap = new TDoubleShortMapDecorator();
        resultMap.readExternal(ois);
        
        assertEquals(2, resultMap.size());
        assertTrue(resultMap.get(1.0).shortValue() == 1);
        assertTrue(resultMap.get(2.0).shortValue() == 2);
    }
    
    @Test
    public void testWriteExternal() throws IOException, ClassNotFoundException {
        TDoubleShortMapDecorator testMap = new TDoubleShortMapDecorator();
        testMap.put(1.0, (short) 1);
        testMap.put(2.0, (short) 2);
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        
        testMap.writeExternal(oos);
        oos.flush();
        baos.flush();
        
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        
        TDoubleShortMapDecorator resultMap = new TDoubleShortMapDecorator();
        resultMap.readExternal(ois);
        
        assertEquals(2, resultMap.size());
        assertTrue(resultMap.get(1.0).shortValue() == 1);
        assertTrue(resultMap.get(2.0).shortValue() == 2);
    }
}