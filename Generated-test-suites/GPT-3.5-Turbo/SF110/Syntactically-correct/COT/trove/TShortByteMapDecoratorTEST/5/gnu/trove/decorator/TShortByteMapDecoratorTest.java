
import org.junit.*;
import static org.junit.Assert.*;
import gnu.trove.map.*;
import java.util.*;

public class TShortByteMapDecoratorTest {
  
  @Test
  public void testPutAndGet() {
    TShortByteMapDecorator map = new TShortByteMapDecorator();
    Short key1 = 1;
    Byte value1 = 10;
    Byte value2 = 20;
    
    // Test put()
    assertNull(map.put(key1, value1));
    assertTrue(map.containsKey(key1));
    assertEquals(value1, map.get(key1));
    
    // Test put() with null key
    assertNull(map.put(null, value2));
    assertTrue(map.containsKey(null));
    assertEquals(value2, map.get(null));
    
    // Test put() with null value
    assertNull(map.put(key1, null));
    assertTrue(map.containsKey(key1));
    assertNull(map.get(key1));
  }
  
  @Test
  public void testGetWithNonExistentKey() {
    TShortByteMapDecorator map = new TShortByteMapDecorator();
    Short key = 1;
    
    // Test get() with non-existent key
    assertNull(map.get(key));
  }
  
  @Test
  public void testRemove() {
    TShortByteMapDecorator map = new TShortByteMapDecorator();
    Short key = 1;
    Byte value = 10;
    
    // Test remove()
    assertNull(map.put(key, value));
    assertTrue(map.containsKey(key));
    assertEquals(value, map.remove(key));
    assertFalse(map.containsKey(key));
    assertNull(map.get(key));
  }
  
  @Test
  public void testClear() {
    TShortByteMapDecorator map = new TShortByteMapDecorator();
    Short key = 1;
    Byte value = 10;
    
    // Test clear()
    assertNull(map.put(key, value));
    assertFalse(map.isEmpty());
    map.clear();
    assertTrue(map.isEmpty());
    assertNull(map.get(key));
  }
  
  @Test
  public void testEntrySet() {
    TShortByteMapDecorator map = new TShortByteMapDecorator();
    Short key1 = 1;
    Byte value1 = 10;
    Short key2 = 2;
    Byte value2 = 20;
    
    // Test entrySet()
    assertTrue(map.entrySet().isEmpty());
    
    // Add entries
    assertNull(map.put(key1, value1));
    assertNull(map.put(key2, value2));
    
    Set<Map.Entry<Short, Byte>> entrySet = map.entrySet();
    assertEquals(2, entrySet.size());
    
    // Test iteration
    for (Map.Entry<Short, Byte> entry : entrySet) {
      Short key = entry.getKey();
      Byte value = entry.getValue();
      assertTrue(map.containsKey(key));
      assertEquals(value, map.get(key));
    }
    
    // Test modification through entry
    Map.Entry<Short, Byte> entry1 = entrySet.iterator().next();
    Short key = entry1.getKey();
    Byte value = entry1.getValue();
    entry1.setValue((byte)30);
    
    assertEquals((byte)30, map.get(key));
    assertEquals((byte)30, value);
  }
  
  @Test
  public void testContainsValue() {
    TShortByteMapDecorator map = new TShortByteMapDecorator();
    Byte value1 = 10;
    Byte value2 = 20;
    
    // Test containsValue() with non-existent value
    assertFalse(map.containsValue(value1));
    
    // Add entries
    map.put((short)1, value1);
    map.put((short)2, value2);
    
    // Test containsValue() with existent value
    assertTrue(map.containsValue(value1));
    assertTrue(map.containsValue(value2));
  }
  
  @Test
  public void testContainsKey() {
    TShortByteMapDecorator map = new TShortByteMapDecorator();
    Short key1 = 1;
    Short key2 = 2;
    
    // Test containsKey() with non-existent key
    assertFalse(map.containsKey(key1));
    assertFalse(map.containsKey(key2));
    
    // Add entries
    map.put(key1, (byte)10);
    map.put(key2, (byte)20);
    
    // Test containsKey() with existent key
    assertTrue(map.containsKey(key1));
    assertTrue(map.containsKey(key2));
  }
  
  @Test
  public void testSizeAndIsEmpty() {
    TShortByteMapDecorator map = new TShortByteMapDecorator();
    Short key1 = 1;
    Byte value1 = 10;
    Short key2 = 2;
    Byte value2 = 20;
    
    // Test size() and isEmpty() on an empty map
    assertEquals(0, map.size());
    assertTrue(map.isEmpty());
    
    // Add entries
    map.put(key1, value1);
    
    // Test size() and isEmpty() on a non-empty map
    assertEquals(1, map.size());
    assertFalse(map.isEmpty());
    
    map.put(key2, value2);
    
    // Test size() and isEmpty() on a non-empty map with multiple entries
    assertEquals(2, map.size());
    assertFalse(map.isEmpty());
  }
  
  @Test
  public void testPutAll() {
    TShortByteMapDecorator map = new TShortByteMapDecorator();
    Map<Short, Byte> otherMap = new HashMap<>();
    otherMap.put((short)1, (byte)10);
    otherMap.put((short)2, (byte)20);
    
    // Test putAll()
    map.putAll(otherMap);
    
    assertEquals(2, map.size());
    assertTrue(map.containsKey((short)1));
    assertTrue(map.containsKey((short)2));
    assertEquals((byte)10, map.get((short)1));
    assertEquals((byte)20, map.get((short)2));
  }
}
