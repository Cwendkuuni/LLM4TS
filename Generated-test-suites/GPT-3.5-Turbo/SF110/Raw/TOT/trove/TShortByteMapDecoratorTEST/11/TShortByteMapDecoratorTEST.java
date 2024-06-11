import static org.junit.Assert.*;
import org.junit.Test;
import gnu.trove.map.TShortByteMap;

public class TShortByteMapDecoratorTest {

  @Test
  public void testPut() {
    TShortByteMapDecorator decorator = new TShortByteMapDecorator();
    decorator.put((short)1, (byte)2);
    assertTrue(decorator.containsKey((short)1));
    assertTrue(decorator.containsValue((byte)2));
  }

  @Test
  public void testGet() {
    TShortByteMapDecorator decorator = new TShortByteMapDecorator();
    decorator.put((short)1, (byte)2);
    Byte value = decorator.get((short)1);
    assertEquals(value, (byte)2);
  }

  @Test
  public void testClear() {
    TShortByteMapDecorator decorator = new TShortByteMapDecorator();
    decorator.put((short)1, (byte)2);
    decorator.clear();
    assertTrue(decorator.isEmpty());
  }

  @Test
  public void testRemove() {
    TShortByteMapDecorator decorator = new TShortByteMapDecorator();
    decorator.put((short)1, (byte)2);
    Byte value = decorator.remove((short)1);
    assertEquals(value, (byte)2);
    assertFalse(decorator.containsKey((short)1));
  }

  @Test
  public void testEntrySet() {
    TShortByteMapDecorator decorator = new TShortByteMapDecorator();
    decorator.put((short)1, (byte)2);
    decorator.put((short)3, (byte)4);
    decorator.put((short)5, (byte)6);
    decorator.put((short)7, (byte)8);
    assertEquals(decorator.entrySet().size(), 4);
  }

  @Test
  public void testContainsValue() {
    TShortByteMapDecorator decorator = new TShortByteMapDecorator();
    decorator.put((short)1, (byte)2);
    assertTrue(decorator.containsValue((byte)2));
    assertFalse(decorator.containsValue((byte)3));
  }

  @Test
  public void testContainsKey() {
    TShortByteMapDecorator decorator = new TShortByteMapDecorator();
    decorator.put((short)1, (byte)2);
    assertTrue(decorator.containsKey((short)1));
    assertFalse(decorator.containsKey((short)2));
  }

  @Test
  public void testSize() {
    TShortByteMapDecorator decorator = new TShortByteMapDecorator();
    decorator.put((short)1, (byte)2);
    decorator.put((short)3, (byte)4);
    assertEquals(decorator.size(), 2);
  }

  @Test
  public void testIsEmpty() {
    TShortByteMapDecorator decorator = new TShortByteMapDecorator();
    assertTrue(decorator.isEmpty());
    decorator.put((short)1, (byte)2);
    assertFalse(decorator.isEmpty());
  }

  @Test
  public void testPutAll() {
    TShortByteMapDecorator decorator = new TShortByteMapDecorator();

    TShortByteMap map = new TShortByteMap();
    map.put((short)1, (byte)2);
    map.put((short)3, (byte)4);
    map.put((short)5, (byte)6);

    decorator.putAll(map);
    
    assertEquals(decorator.size(), 3);
    assertTrue(decorator.containsKey((short)1));
    assertTrue(decorator.containsKey((short)3));
    assertTrue(decorator.containsKey((short)5));
  }
  
  @Test
  public void testWrapKey() {
    TShortByteMapDecorator decorator = new TShortByteMapDecorator();
    Short key = decorator.wrapKey((short)10);
    assertEquals(key, (short)10);
  }

  @Test
  public void testUnwrapKey() {
    TShortByteMapDecorator decorator = new TShortByteMapDecorator();
    short key = decorator.unwrapKey((short)10);
    assertEquals(key, (short)10);
  }

  @Test
  public void testWrapValue() {
    TShortByteMapDecorator decorator = new TShortByteMapDecorator();
    Byte value = decorator.wrapValue((byte)10);
    assertEquals(value, (byte)10);
  }

  @Test
  public void testUnwrapValue() {
    TShortByteMapDecorator decorator = new TShortByteMapDecorator();
    byte value = decorator.unwrapValue((byte)10);
    assertEquals(value, (byte)10);
  }
}