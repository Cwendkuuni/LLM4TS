import org.junit.Test;
import static org.junit.Assert.*;

public class TDoubleShortMapDecoratorTest {

  // Test cases for put method
  @Test
  public void testPut() {
    TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
    assertEquals(null, map.put(1.0, (short) 1));
    assertEquals((Short) 1, map.put(1.0, (short) 2));
    assertEquals(null, map.put(null, (short) 3));
    assertEquals((Short) 3, map.put(null, (short) 4));
  }

  // Test cases for get method
  @Test
  public void testGet() {
    TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
    map.put(1.0, (short) 1);
    map.put(2.0, (short) 2);
    map.put(null, (short) 3);
    assertEquals((Short) 1, map.get(1.0));
    assertEquals((Short) 2, map.get(2.0));
    assertEquals((Short) 3, map.get(null));
    assertEquals(null, map.get(3.0));
  }

  // Test case for clear method
  @Test
  public void testClear() {
    TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
    map.put(1.0, (short) 1);
    map.put(2.0, (short) 2);
    map.clear();
    assertEquals(0, map.size());
  }

  // Test cases for remove method
  @Test
  public void testRemove() {
    TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
    map.put(1.0, (short) 1);
    assertEquals((Short) 1, map.remove(1.0));
    assertEquals(0, map.size());
    assertEquals(null, map.remove(null));
  }

  // Test cases for entrySet method
  @Test
  public void testEntrySet() {
    TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
    map.put(1.0, (short) 1);
    map.put(2.0, (short) 2);
    map.put(3.0, (short) 3);
    for (Map.Entry<Double, Short> entry : map.entrySet()) {
      assertTrue(entry.getKey() instanceof Double);
      assertTrue(entry.getValue() instanceof Short);
    }
  }

  // Test cases for containsValue method
  @Test
  public void testContainsValue() {
    TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
    map.put(1.0, (short) 1);
    assertTrue(map.containsValue((short) 1));
    assertFalse(map.containsValue((short) 2));
    assertFalse(map.containsValue(null));
  }

  // Test cases for containsKey method
  @Test
  public void testContainsKey() {
    TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
    map.put(1.0, (short) 1);
    assertTrue(map.containsKey(1.0));
    assertFalse(map.containsKey(2.0));
    assertTrue(map.containsKey(null));
  }

  // Test case for size method
  @Test
  public void testSize() {
    TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
    assertEquals(0, map.size());
    map.put(1.0, (short) 1);
    assertEquals(1, map.size());
  }

  // Test case for isEmpty method
  @Test
  public void testIsEmpty() {
    TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
    assertTrue(map.isEmpty());
    map.put(1.0, (short) 1);
    assertFalse(map.isEmpty());
  }

  // Test case for putAll method
  @Test
  public void testPutAll() {
    TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
    Map<Double, Short> inputMap = new HashMap<Double, Short>();
    inputMap.put(1.0, (short) 1);
    inputMap.put(2.0, (short) 2);
    map.putAll(inputMap);
    assertEquals((Short) 1, map.get(1.0));
    assertEquals((Short) 2, map.get(2.0));
  }
}