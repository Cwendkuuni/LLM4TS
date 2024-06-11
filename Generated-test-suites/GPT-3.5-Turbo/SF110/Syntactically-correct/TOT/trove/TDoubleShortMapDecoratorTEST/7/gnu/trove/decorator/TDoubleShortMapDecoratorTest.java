import gnu.trove.map.hash.TDoubleShortHashMap;
import org.junit.*;
import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {

    // Create an instance of TDoubleShortHashMap for testing
    TDoubleShortHashMap map = new TDoubleShortHashMap();
    
    @Before
    public void setUp() {
        // Set up test data
        map.put(1.0, (short) 1);
        map.put(2.0, (short) 2);
        map.put(3.0, (short) 3);
    }
    
    @After
    public void tearDown() {
        // Clean up the test data
        map.clear();
    }
    
    // Test case for put() method
    @Test
    public void testPut() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        
        // Test typical use cases
        assertNull(decorator.put(4.0, (short) 4));
        assertEquals((short) 4, decorator.put(4.0, (short) 0));
        
        // Test edge case
        assertNull(decorator.put(0.0, (short) 0));
        
        // Test error scenario
        assertNull(decorator.put(null, (short) 0));
    }
    
    // Test case for get() method
    @Test
    public void testGet() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        
        // Test typical use cases
        assertEquals((short) 1, decorator.get(1.0));
        assertEquals((short) 2, decorator.get(2.0));
        
        // Test edge case
        assertNull(decorator.get(0.0));
        
        // Test error scenario
        assertNull(decorator.get(null));
    }
    
    // Test case for clear() method
    @Test
    public void testClear() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        
        // Test typical use case
        decorator.clear();
        assertTrue(decorator.isEmpty());
        
        // Test edge case
        decorator.clear();
        assertTrue(decorator.isEmpty());
    }
    
    // Test case for remove() method
    @Test
    public void testRemove() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        
        // Test typical use case
        assertEquals((short) 1, decorator.remove(1.0));
        assertNull(decorator.remove(1.0));
        
        // Test edge case
        assertNull(decorator.remove(0.0));
        
        // Test error scenario
        assertNull(decorator.remove(null));
    }
    
    // Test case for entrySet() method
    @Test
    public void testEntrySet() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        
        // Test typical use case
        assertEquals(map.entrySet().size(), decorator.entrySet().size());
        
        // Test edge case
        decorator.clear();
        assertEquals(0, decorator.entrySet().size());
    }
    
    // Test case for containsValue() method
    @Test
    public void testContainsValue() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        
        // Test typical use cases
        assertTrue(decorator.containsValue((short) 1));
        assertTrue(decorator.containsValue((short) 2));
        
        // Test edge case
        assertFalse(decorator.containsValue((short) 0));
        
        // Test error scenario
        assertFalse(decorator.containsValue(null));
    }
    
    // Test case for containsKey() method
    @Test
    public void testContainsKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        
        // Test typical use cases
        assertTrue(decorator.containsKey(1.0));
        assertTrue(decorator.containsKey(2.0));
        
        // Test edge case
        assertFalse(decorator.containsKey(0.0));
        
        // Test error scenario
        assertFalse(decorator.containsKey(null));
    }
    
    // Test case for size() method
    @Test
    public void testSize() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        
        // Test typical use case
        assertEquals(map.size(), decorator.size());
        
        // Test edge case
        decorator.clear();
        assertEquals(0, decorator.size());
    }
    
    // Test case for isEmpty() method
    @Test
    public void testIsEmpty() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        
        // Test typical use case
        assertFalse(decorator.isEmpty());
        
        // Test edge case
        decorator.clear();
        assertTrue(decorator.isEmpty());
    }
    
    // Test case for putAll() method
    @Test
    public void testPutAll() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        decorator.putAll(map);
        
        // Test typical use case
        assertEquals(map.size(), decorator.size());
        
        // Test edge case
        decorator.clear();
        decorator.putAll(map);
        assertEquals(map.size(), decorator.size());
    }
    
    // Test case for wrapKey() method
    @Test
    public void testWrapKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        
        // Test typical use case
        assertEquals(1.0, decorator.wrapKey(1.0), 0);
        assertEquals(2.0, decorator.wrapKey(2.0), 0);
        
        // Test edge case
        assertEquals(0.0, decorator.wrapKey(0.0), 0);
    }
    
    // Test case for unwrapKey() method
    @Test
    public void testUnwrapKey() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        
        // Test typical use case
        assertEquals(1.0, decorator.unwrapKey(1.0), 0);
        assertEquals(2.0, decorator.unwrapKey(2.0), 0);
        
        // Test edge case
        assertEquals(0.0, decorator.unwrapKey(0.0), 0);
    }
    
    // Test case for wrapValue() method
    @Test
    public void testWrapValue() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        
        // Test typical use case
        assertEquals((short) 1, decorator.wrapValue((short) 1));
        assertEquals((short) 2, decorator.wrapValue((short) 2));
        
        // Test edge case
        assertEquals((short) 0, decorator.wrapValue((short) 0));
    }
    
    // Test case for unwrapValue() method
    @Test
    public void testUnwrapValue() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator(map);
        
        // Test typical use case
        assertEquals((short) 1, decorator.unwrapValue((short) 1));
        assertEquals((short) 2, decorator.unwrapValue((short) 2));
        
        // Test edge case
        assertEquals((short) 0, decorator.unwrapValue((short) 0));
    }
    
    // Test case for readExternal() method
    @Test
    public void testReadExternal() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        
        // Test typical use case
        try {
            decorator.readExternal(null);
        } catch (Exception e) {
            // Do nothing
        }
        
        // Test edge case
        try {
            decorator.readExternal(null);
        } catch (Exception e) {
            // Do nothing
        }
    }
    
    // Test case for writeExternal() method
    @Test
    public void testWriteExternal() {
        TDoubleShortMapDecorator decorator = new TDoubleShortMapDecorator();
        
        // Test typical use case
        try {
            decorator.writeExternal(null);
        } catch (Exception e) {
            // Do nothing
        }
        
        // Test edge case
        try {
            decorator.writeExternal(null);
        } catch (Exception e) {
            // Do nothing
        }
    }
}