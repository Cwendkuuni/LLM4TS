package gnu.trove.decorator;

import gnu.trove.map.*;
import org.junit.Test;
import java.util.*;

import static org.junit.Assert.*;

public class TShortByteMapDecoratorTest {

  @Test
  public void testPutAndGet() {
    TShortByteMap map = new TShortByteHashMap();
    TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
    
    decorator.put((short) 1, (byte) 10);
    decorator.put((short) 2, (byte) 20);
    decorator.put((short) 3, (byte) 30);

    assertEquals((byte) 10, (byte) decorator.get((short) 1));
    assertEquals((byte) 20, (byte) decorator.get((short) 2));
    assertEquals((byte) 30, (byte) decorator.get((short) 3));
  }

  @Test
  public void testClear() {
    TShortByteMap map = new TShortByteHashMap();
    TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
    
    decorator.put((short) 1, (byte) 10);
    decorator.put((short) 2, (byte) 20);
    decorator.put((short) 3, (byte) 30);

    decorator.clear();

    assertEquals(0, decorator.size());
    assertTrue(decorator.isEmpty());
  }

  @Test
  public void testRemove() {
    TShortByteMap map = new TShortByteHashMap();
    TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
    
    decorator.put((short) 1, (byte) 10);
    decorator.put((short) 2, (byte) 20);
    decorator.put((short) 3, (byte) 30);

    decorator.remove((short) 2);

    assertNull(decorator.get((short) 2));
    assertEquals(2, decorator.size());
  }

  @Test
  public void testContainsKey() {
    TShortByteMap map = new TShortByteHashMap();
    TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
    
    decorator.put((short) 1, (byte) 10);
    decorator.put((short) 2, (byte) 20);
    decorator.put((short) 3, (byte) 30);

    assertTrue(decorator.containsKey((short) 1));
    assertFalse(decorator.containsKey((short) 4));
  }

  @Test
  public void testContainsValue() {
    TShortByteMap map = new TShortByteHashMap();
    TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
    
    decorator.put((short) 1, (byte) 10);
    decorator.put((short) 2, (byte) 20);
    decorator.put((short) 3, (byte) 30);

    assertTrue(decorator.containsValue((byte) 10));
    assertFalse(decorator.containsValue((byte) 40));
  }

  @Test
  public void testSize() {
    TShortByteMap map = new TShortByteHashMap();
    TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
    
    decorator.put((short) 1, (byte) 10);
    decorator.put((short) 2, (byte) 20);
    decorator.put((short) 3, (byte) 30);

    assertEquals(3, decorator.size());
  }

  @Test
  public void testIsEmpty() {
    TShortByteMap map = new TShortByteHashMap();
    TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
    
    assertTrue(decorator.isEmpty());

    decorator.put((short) 1, (byte) 10);
    decorator.put((short) 2, (byte) 20);
    decorator.put((short) 3, (byte) 30);

    assertFalse(decorator.isEmpty());
  }

  @Test
  public void testPutAll() {
    TShortByteMap map = new TShortByteHashMap();
    TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
    
    Map<Short, Byte> testMap = new HashMap<>();
    testMap.put((short) 1, (byte) 10);
    testMap.put((short) 2, (byte) 20);
    testMap.put((short) 3, (byte) 30);

    decorator.putAll(testMap);

    assertEquals((byte) 10, (byte) decorator.get((short) 1));
    assertEquals((byte) 20, (byte) decorator.get((short) 2));
    assertEquals((byte) 30, (byte) decorator.get((short) 3));
  }

  @Test
  public void testEntrySet() {
    TShortByteMap map = new TShortByteHashMap();
    TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
    
    decorator.put((short) 1, (byte) 10);
    decorator.put((short) 2, (byte) 20);
    decorator.put((short) 3, (byte) 30);

    Set<Map.Entry<Short, Byte>> entrySet = decorator.entrySet();

    assertEquals(decorator.size(), entrySet.size());
    assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>((short) 1, (byte) 10)));
    assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>((short) 2, (byte) 20)));
    assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>((short) 3, (byte) 30)));
  }
}