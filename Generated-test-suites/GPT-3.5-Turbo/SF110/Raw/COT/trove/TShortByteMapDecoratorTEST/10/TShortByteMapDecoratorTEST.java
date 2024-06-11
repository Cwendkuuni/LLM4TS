import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import gnu.trove.decorator.TShortByteMapDecorator;

public class TShortByteMapDecoratorTest {

  private TShortByteMapDecorator mapDecorator;

  @Before
  public void setUp() {
    mapDecorator = new TShortByteMapDecorator();
  }

  @Test
  public void testPutAndGet() {
    Short key1 = 1;
    Byte value1 = 100;

    mapDecorator.put(key1, value1);

    assertEquals(value1, mapDecorator.get(key1));
  }

  @Test
  public void testPutNullKeyAndGet() {
    Short key1 = null;
    Byte value1 = 100;

    mapDecorator.put(key1, value1);

    assertEquals(value1, mapDecorator.get(key1));
  }

  @Test
  public void testPutAndGetNullValue() {
    Short key1 = 1;
    Byte value1 = null;

    mapDecorator.put(key1, value1);

    assertEquals(value1, mapDecorator.get(key1));
  }

  @Test
  public void testPutNullKeyAndGetNullValue() {
    Short key1 = null;
    Byte value1 = null;

    mapDecorator.put(key1, value1);

    assertEquals(value1, mapDecorator.get(key1));
  }

  @Test
  public void testRemove() {
    Short key1 = 1;
    Byte value1 = 100;

    mapDecorator.put(key1, value1);
    Byte removedValue = mapDecorator.remove(key1);

    assertNull(mapDecorator.get(key1));
    assertEquals(value1, removedValue);
  }

  @Test
  public void testRemoveNullKey() {
    Short key1 = null;
    Byte value1 = 100;

    mapDecorator.put(key1, value1);
    Byte removedValue = mapDecorator.remove(key1);

    assertNull(mapDecorator.get(key1));
    assertEquals(value1, removedValue);
  }

  @Test
  public void testClear() {
    Short key1 = 1;
    Byte value1 = 100;

    mapDecorator.put(key1, value1);
    mapDecorator.clear();

    assertNull(mapDecorator.get(key1));
    assertTrue(mapDecorator.isEmpty());
  }

  @Test
  public void testContainsValue() {
    Short key1 = 1;
    Byte value1 = 100;

    mapDecorator.put(key1, value1);

    assertTrue(mapDecorator.containsValue(value1));
  }

  @Test
  public void testDoesNotContainValue() {
    Short key1 = 1;
    Byte value1 = 100;
    Byte value2 = 200;

    mapDecorator.put(key1, value1);

    assertFalse(mapDecorator.containsValue(value2));
  }

  @Test
  public void testContainsKey() {
    Short key1 = 1;
    Byte value1 = 100;

    mapDecorator.put(key1, value1);

    assertTrue(mapDecorator.containsKey(key1));
  }

  @Test
  public void testDoesNotContainKey() {
    Short key1 = 1;

    assertFalse(mapDecorator.containsKey(key1));
  }

  @Test
  public void testSize() {
    Short key1 = 1;
    Byte value1 = 100;
    Short key2 = 2;
    Byte value2 = 200;

    mapDecorator.put(key1, value1);
    mapDecorator.put(key2, value2);

    assertEquals(2, mapDecorator.size());
  }

  @Test
  public void testIsEmpty() {
    assertTrue(mapDecorator.isEmpty());
  }

  @Test
  public void testIsNotEmpty() {
    Short key1 = 1;
    Byte value1 = 100;

    mapDecorator.put(key1, value1);

    assertFalse(mapDecorator.isEmpty());
  }

  @Test
  public void testPutAll() {
    Short key1 = 1;
    Byte value1 = 100;
    Short key2 = 2;
    Byte value2 = 200;

    TShortByteMapDecorator mapDecorator2 = new TShortByteMapDecorator();

    mapDecorator2.put(key1, value1);
    mapDecorator2.put(key2, value2);

    mapDecorator.putAll(mapDecorator2);

    assertEquals(value1, mapDecorator.get(key1));
    assertEquals(value2, mapDecorator.get(key2));
  }

  @Test
  public void testWrapKey() {
    short k = 1;

    Short wrappedKey = mapDecorator.wrapKey(k);

    assertEquals(k, (short)wrappedKey);
  }

  @Test
  public void testUnwrapKey() {
    Short key = 1;

    short unwrappedKey = mapDecorator.unwrapKey(key);

    assertEquals((short)key, unwrappedKey);
  }

  @Test
  public void testWrapValue() {
    byte k = 100;

    Byte wrappedValue = mapDecorator.wrapValue(k);

    assertEquals(k, (byte)wrappedValue);
  }

  @Test
  public void testUnwrapValue() {
    Byte value = 100;

    byte unwrappedValue = mapDecorator.unwrapValue(value);

    assertEquals((byte)value, unwrappedValue);
  }
}