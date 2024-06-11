import static org.junit.Assert.*;
import org.junit.*;
import java.util.*;

public class TDoubleShortMapDecoratorTest {
    private TDoubleShortMapDecorator decorator;
    
    @Before
    public void setUp() {
        TDoubleShortMap map = new TDoubleShortMap();
        decorator = new TDoubleShortMapDecorator(map);
    }
    
    @Test
    public void testPutAndGet() {
        // Typical use case
        decorator.put(1.5, (short) 10);
        assertEquals((short) 10, decorator.get(1.5));
        
        // Edge cases
        assertNull(decorator.put(null, (short) 20));
        assertNull(decorator.get(null));
        decorator.put(0.0, (short) 0);
        assertEquals((short) 0, decorator.get(0.0));
        
        // Error scenarios
        assertNull(decorator.put(2.3, null));
        assertNull(decorator.get(2.3));
        assertNull(decorator.put("invalid key", (short) 30));
        assertNull(decorator.get("invalid key"));
    }
    
    @Test
    public void testClear() {
        decorator.put(1.0, (short) 5);
        decorator.put(2.0, (short) 10);
        decorator.clear();
        assertEquals(0, decorator.size());
    }
    
    @Test
    public void testRemove() {
        decorator.put(1.0, (short) 5);
        decorator.put(2.0, (short) 10);
        
        // Typical use case
        assertEquals((short) 5, decorator.remove(1.0));
        assertNull(decorator.get(1.0));
        
        // Edge case
        assertNull(decorator.remove(null));
        
        // Error scenario
        assertNull(decorator.remove("invalid key"));
    }
    
    @Test
    public void testEntrySet() {
        decorator.put(1.0, (short) 5);
        decorator.put(2.0, (short) 10);
        
        Set<Entry<Double, Short>> entrySet = decorator.entrySet();
        assertEquals(2, entrySet.size());
        
        // Typical use case
        Entry<Double, Short> entry = new AbstractMap.SimpleEntry<>(1.0, (short) 5);
        assertTrue(entrySet.contains(entry));
        
        // Edge case
        assertFalse(entrySet.isEmpty());
        
        // Error scenario
        Entry<Double, Short> invalidEntry = new AbstractMap.SimpleEntry<>("invalid key", (short) 10);
        assertFalse(entrySet.contains(invalidEntry));
    }
    
    @Test
    public void testContainsValue() {
        decorator.put(1.0, (short) 5);
        decorator.put(2.0, (short) 10);
        
        // Typical use case
        assertTrue(decorator.containsValue((short) 5));
        
        // Edge case
        assertFalse(decorator.containsValue(null));
        
        // Error scenario
        assertFalse(decorator.containsValue((short) 15));
        assertFalse(decorator.containsValue("invalid value"));
    }
    
    @Test
    public void testContainsKey() {
        decorator.put(1.0, (short) 5);
        decorator.put(2.0, (short) 10);
        
        // Typical use case
        assertTrue(decorator.containsKey(1.0));
        
        // Edge case
        assertFalse(decorator.containsKey(null));
        
        // Error scenario
        assertFalse(decorator.containsKey(3.0));
        assertFalse(decorator.containsKey("invalid key"));
    }
    
    @Test
    public void testSize() {
        decorator.put(1.0, (short) 5);
        decorator.put(2.0, (short) 10);
        
        assertEquals(2, decorator.size());
    }
    
    @Test
    public void testIsEmpty() {
        // Typical use case
        assertTrue(decorator.isEmpty());
        
        decorator.put(1.0, (short) 5);
        
        // Edge case
        assertFalse(decorator.isEmpty());
    }
    
    @Test
    public void testPutAll() {
        Map<Double, Short> map = new HashMap<>();
        map.put(1.0, (short) 5);
        map.put(2.0, (short) 10);
        
        // Typical use case
        decorator.putAll(map);
        assertEquals((short) 5, decorator.get(1.0));
        assertEquals((short) 10, decorator.get(2.0));
        
        // Edge case
        decorator.clear();
        decorator.putAll(Collections.emptyMap());
        assertEquals(0, decorator.size());
    }
    
    @Test
    public void testWrapAndUnwrapKey() {
        assertEquals(1.0, decorator.unwrapKey(decorator.wrapKey(1.0)), 0.001);
        assertNull(decorator.wrapKey(decorator._map.getNoEntryKey()));
    }
    
    @Test
    public void testWrapAndUnwrapValue() {
        assertEquals((short) 10, decorator.unwrapValue(decorator.wrapValue((short) 10)));
        assertEquals(decorator._map.getNoEntryValue(), decorator.wrapValue(decorator._map.getNoEntryValue()).shortValue());
    }
    
    @Test
    public void testSerialization() throws Exception {
        decorator.put(1.0, (short) 5);
        decorator.put(2.0, (short) 10);
        
        // Serialize
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        decorator.writeExternal(oos);
        oos.flush();
        byte[] serializedBytes = baos.toByteArray();
        
        // Deserialize
        ByteArrayInputStream bais = new ByteArrayInputStream(serializedBytes);
        ObjectInputStream ois = new ObjectInputStream(bais);
        TDoubleShortMapDecorator deserializedDecorator = new TDoubleShortMapDecorator();
        deserializedDecorator.readExternal(ois);
        
        // Verify
        assertEquals((short) 5, deserializedDecorator.get(1.0));
        assertEquals((short) 10, deserializedDecorator.get(2.0));
        assertEquals(decorator.size(), deserializedDecorator.size());
    }
}