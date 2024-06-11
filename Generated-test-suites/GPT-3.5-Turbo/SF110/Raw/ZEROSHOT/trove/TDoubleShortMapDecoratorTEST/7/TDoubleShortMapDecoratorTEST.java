import gnu.trove.decorator.TDoubleShortMapDecorator;
import gnu.trove.list.array.TDoubleArrayList;
import gnu.trove.list.array.TShortArrayList;
import gnu.trove.map.TDoubleShortMap;
import gnu.trove.map.hash.TDoubleShortHashMap;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.*;

public class TDoubleShortMapDecoratorTest {
  
  private TDoubleShortMapDecorator mapDecorator;
  
  @Before
  public void setUp() {
    TDoubleShortMap map = new TDoubleShortHashMap();
    map.put(1.0, (short) 10);
    map.put(2.0, (short) 20);
    map.put(3.0, (short) 30);
    mapDecorator = new TDoubleShortMapDecorator(map);
  }
  
  @After
  public void tearDown() {
    mapDecorator.clear();
  }
  
  @Test
  public void testPut() {
    assertEquals((Short) 10, mapDecorator.put(1.0, (short) 11));
    assertEquals(3, mapDecorator.size());
  }
  
  @Test
  public void testGet() {
    assertEquals((Short) 10, mapDecorator.get(1.0));
    assertNull(mapDecorator.get(4.0));
  }
  
  @Test
  public void testClear() {
    mapDecorator.clear();
    assertTrue(mapDecorator.isEmpty());
  }
  
  @Test
  public void testRemove() {
    assertEquals((Short) 10, mapDecorator.remove(1.0));
    assertNull(mapDecorator.remove(4.0));
    assertEquals(2, mapDecorator.size());
  }
  
  @Test
  public void testEntrySet() {
    Set<Map.Entry<Double, Short>> entrySet = mapDecorator.entrySet();
    assertEquals(3, entrySet.size());
    assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>(1.0, (short) 10)));
    assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>(2.0, (short) 20)));
    assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>(3.0, (short) 30)));
  }
  
  @Test
  public void testContainsValue() {
    assertTrue(mapDecorator.containsValue((short) 10));
    assertFalse(mapDecorator.containsValue((short) 40));
  }
  
  @Test
  public void testContainsKey() {
    assertTrue(mapDecorator.containsKey(1.0));
    assertFalse(mapDecorator.containsKey(4.0));
  }
  
  @Test
  public void testSize() {
    assertEquals(3, mapDecorator.size());
  }
  
  @Test
  public void testIsEmpty() {
    assertFalse(mapDecorator.isEmpty());
    mapDecorator.clear();
    assertTrue(mapDecorator.isEmpty());
  }
  
  @Test
  public void testPutAll() {
    Map<Double, Short> otherMap = new HashMap<>();
    otherMap.put(4.0, (short) 40);
    otherMap.put(5.0, (short) 50);
    
    mapDecorator.putAll(otherMap);
    
    assertEquals((Short) 40, mapDecorator.get(4.0));
    assertEquals((Short) 50, mapDecorator.get(5.0));
    assertEquals(5, mapDecorator.size());
  }
  
  @Test
  public void testWrapKey() {
    double key = 2.5;
    assertEquals(key, mapDecorator.wrapKey(key), 0.001);
  }
  
  @Test
  public void testUnwrapKey() {
    Object key = 2.5;
    assertEquals((double) key, mapDecorator.unwrapKey(key), 0.001);
  }
  
  @Test
  public void testWrapValue() {
    short value = 25;
    assertEquals(value, (short) mapDecorator.wrapValue(value));
  }
  
  @Test
  public void testUnwrapValue() {
    Object value = 25;
    assertEquals((short) value, mapDecorator.unwrapValue(value));
  }
  
  @Test
  public void testReadExternal() {
    TDoubleShortMapDecorator newMapDecorator = new TDoubleShortMapDecorator();
    
    try {
      newMapDecorator.readExternal(null);
      fail("Expected IOException to be thrown");
    } catch (IOException e) {
      // Expected IOException to be thrown
    } catch (ClassNotFoundException e) {
      fail("ClassNotFoundException should not be thrown");
    }
  }
  
  @Test
  public void testWriteExternal() {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    TDoubleShortMapDecorator newMapDecorator = new TDoubleShortMapDecorator();
    
    try {
      newMapDecorator.writeExternal(null);
      fail("Expected IOException to be thrown");
    } catch (IOException e) {
      // Expected IOException to be thrown
    }
  }
}