
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.Map;

public class TDoubleShortMapDecoratorTest {
  
  private TDoubleShortMapDecorator mapDecorator;
  
  @Before
  public void setUp() {
    mapDecorator = new TDoubleShortMapDecorator();
  }
  
  // Test put method
  
  @Test
  public void testPut() {
    assertNull(mapDecorator.put(1.0, (short) 1));
    assertEquals(1, mapDecorator.size());
    assertEquals((short) 1, mapDecorator.getMap().get(1.0), 0.0001);
    
    assertEquals(new Short((short) 1), mapDecorator.put(1.0, (short) 2));
    assertEquals(1, mapDecorator.size());
    assertEquals((short) 2, mapDecorator.getMap().get(1.0),0.0001);
  }
  
  // Test get method
  
  @Test
  public void testGet() {
    assertNull(mapDecorator.get(1.0));
    
    mapDecorator.put(1.0, (short) 1);
    assertEquals(new Short((short) 1), mapDecorator.get(1.0));
    
    mapDecorator.put(2.0, (short) 2);
    assertEquals(new Short((short) 2), mapDecorator.get(2.0));
  }
  
  // Test clear method
  
  @Test
  public void testClear() {
    mapDecorator.put(1.0, (short) 1);
    mapDecorator.put(2.0, (short) 2);
    
    assertFalse(mapDecorator.isEmpty());
    
    mapDecorator.clear();
    
    assertTrue(mapDecorator.isEmpty());
  }
  
  // Test remove method
  
  @Test
  public void testRemove() {
    assertNull(mapDecorator.remove(1.0));
    
    mapDecorator.put(1.0, (short) 1);
    assertEquals(new Short((short) 1), mapDecorator.remove(1.0));
    
    assertNull(mapDecorator.remove(1.0));
    
    mapDecorator.put(2.0, (short) 2);
    assertEquals(new Short((short) 2), mapDecorator.remove(2.0));
  }
  
  // Test entrySet method
  
  @Test
  public void testEntrySet() {
    mapDecorator.put(1.0, (short) 1);
    mapDecorator.put(2.0, (short) 2);
    
    Map.Entry<Double, Short>[] entryArray = new Map.Entry[2];
    mapDecorator.entrySet().toArray(entryArray);
    
    assertEquals(2, entryArray.length);
    assertEquals(new Double(1.0), entryArray[0].getKey());
    assertEquals(new Short((short) 1), entryArray[0].getValue());
    assertEquals(new Double(2.0), entryArray[1].getKey());
    assertEquals(new Short((short) 2), entryArray[1].getValue());
  }
  
  // Test containsValue method
  
  @Test
  public void testContainsValue() {
    assertFalse(mapDecorator.containsValue((short) 1));
    
    mapDecorator.put(1.0, (short) 1);
    assertTrue(mapDecorator.containsValue((short) 1));
    
    mapDecorator.put(2.0, (short) 2);
    assertTrue(mapDecorator.containsValue((short) 2));
  }
  
  // Test containsKey method
  
  @Test
  public void testContainsKey() {
    assertFalse(mapDecorator.containsKey(1.0));
    
    mapDecorator.put(1.0, (short) 1);
    assertTrue(mapDecorator.containsKey(1.0));
    
    mapDecorator.put(2.0, (short) 2);
    assertTrue(mapDecorator.containsKey(2.0));
  }
  
  // Test size method
  
  @Test
  public void testSize() {
    assertEquals(0, mapDecorator.size());
    
    mapDecorator.put(1.0, (short) 1);
    assertEquals(1, mapDecorator.size());
    
    mapDecorator.put(2.0, (short) 2);
    assertEquals(2, mapDecorator.size());
  }
  
  // Test isEmpty method
  
  @Test
  public void testIsEmpty() {
    assertTrue(mapDecorator.isEmpty());
    
    mapDecorator.put(1.0, (short) 1);
    assertFalse(mapDecorator.isEmpty());
    
    mapDecorator.remove(1.0);
    assertTrue(mapDecorator.isEmpty());
  }
  
  // Test putAll method
  
  @Test
  public void testPutAll() {
    Map<Double, Short> inputMap = new HashMap<>();
    inputMap.put(1.0, (short) 1);
    inputMap.put(2.0, (short) 2);
    
    mapDecorator.putAll(inputMap);
    
    assertEquals(2, mapDecorator.size());
    assertEquals(new Short((short) 1), mapDecorator.get(1.0));
    assertEquals(new Short((short) 2), mapDecorator.get(2.0));
  }
  
  // Test readExternal and writeExternal methods
  // Cannot be tested without mocking the ObjectInput and ObjectOutput classes
  
}
