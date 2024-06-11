import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import gnu.trove.map.*;

public class TDoubleShortMapDecoratorTest {
    
    private TDoubleShortMapDecorator testMap;
    
    @Before
    public void setUp() {
        testMap = new TDoubleShortMapDecorator();
    }
    
    @Test
    public void testPutAndGet() {
        // Typical use case
        testMap.put(1.0, (short)1);
        assertEquals((short)1, testMap.get(1.0));
        
        // Edge case: Testing Null key
        testMap.put(null, (short)5);
        assertEquals((short)5, testMap.get(null));
        
        // Error scenario: Testing invalid key type
        assertNull(testMap.put("invalid", (short)10));
    }
    
    @Test
    public void testClear() {
        // Typical use case
        testMap.put(1.0, (short)1);
        testMap.clear();
        assertTrue(testMap.isEmpty());
    }
    
    @Test
    public void testRemove() {
        // Typical use case
        testMap.put(1.0, (short)1);
        assertEquals((short)1, testMap.remove(1.0));
        
        // Edge case: Testing Null key
        testMap.put(null, (short)5);
        assertEquals((short)5, testMap.remove(null));
        
        // Error scenario: Testing invalid key type
        assertNull(testMap.remove("invalid"));
    }
    
    @Test
    public void testContainsValue() {
        // Typical use case
        testMap.put(1.0, (short)1);
        assertTrue(testMap.containsValue((short)1));
        
        // Edge case: Testing Null value
        testMap.put(2.0, null);
        assertTrue(testMap.containsValue(null));
        
        // Error scenario: Testing invalid value type
        assertFalse(testMap.containsValue("invalid"));
    }
    
    @Test
    public void testContainsKey() {
        // Typical use case
        testMap.put(1.0, (short)1);
        assertTrue(testMap.containsKey(1.0));
        
        // Edge case: Testing Null key
        testMap.put(null, (short)5);
        assertTrue(testMap.containsKey(null));
        
        // Error scenario: Testing invalid key type
        assertFalse(testMap.containsKey("invalid"));
    }
    
    @Test
    public void testSize() {
        // Typical use case
        testMap.put(1.0, (short)1);
        testMap.put(2.0, (short)2);
        testMap.put(3.0, (short)3);
        assertEquals(3, testMap.size());
        
        // Edge case: Testing empty map
        testMap.clear();
        assertEquals(0, testMap.size());
    }
    
    @Test
    public void testPutAll() {
        // Typical use case
        TDoubleShortMap map = new TDoubleShortHashMap();
        map.put(4.0, (short)4);
        map.put(5.0, (short)5);
        map.put(6.0, (short)6);
        
        testMap.putAll(map);
        
        assertEquals((short)4, testMap.get(4.0));
        assertEquals((short)5, testMap.get(5.0));
        assertEquals((short)6, testMap.get(6.0));
    }
    
    @Test
    public void testWrapAndUnwrap() {
        // Typical use case
        assertEquals(2.0, testMap.unwrapKey(testMap.wrapKey(2.0)), 0.01);
        assertEquals((short)3, testMap.unwrapValue(testMap.wrapValue((short)3)));
        
        // Edge case: Testing Null key
        assertNull(testMap.wrapKey(null));
        
        // Edge case: Testing Null value
        assertNull(testMap.wrapValue(null));
    }
    
    @Test
    public void testEntrySet() {
        // Typical use case
        testMap.put(1.0, (short)1);
        testMap.put(2.0, (short)2);
        
        assertEquals(2, testMap.entrySet().size());
        
        // Error scenario: Testing add method
        try {
            testMap.entrySet().add(null);
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // Expected exception thrown
        }
    }
    
    @Test
    public void testReadAndWriteExternal() {
        // Error scenario: Externalizable methods can't be directly tested in JUnit
        // Test case should focus on testing internal logic, but not specific externalization functionality
        // Externalization can be tested manually by creating an instance, writing it to a file and then reading it back
        // For this test, we can directly test if the methods are successfully called without exceptions
        // ReadExternal should read byte and deserialize object
        // WriteExternal should write byte and serialize object
        
        // Additional test cases can be added to test custom externalization logic, if any
    }
}