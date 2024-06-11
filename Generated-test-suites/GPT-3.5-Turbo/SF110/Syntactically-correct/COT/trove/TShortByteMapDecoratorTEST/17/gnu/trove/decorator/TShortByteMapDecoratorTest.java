import org.junit.Test;
import static org.junit.Assert.*;

public class TShortByteMapDecoratorTest {
  
  @Test
  public void testPut() {
    TShortByteMapDecorator map = new TShortByteMapDecorator();
    assertEquals(null, map.put((short)1, (byte)10));
    assertEquals((byte)10, map.get((short)1).byteValue());
  }
  
  @Test
  public void testGet() {
    TShortByteMapDecorator map = new TShortByteMapDecorator();
    map.put((short)1, (byte)10);
    assertEquals((byte)10, map.get((short)1).byteValue());
  }
  
  @Test
  public void testClear() {
    TShortByteMapDecorator map = new TShortByteMapDecorator();
    map.put((short)1, (byte)10);
    map.put((short)2, (byte)20);
    map.clear();
    assertEquals(null, map.get((short)1));
    assertEquals(null, map.get((short)2));
  }
  
  @Test
  public void testRemove() {
    TShortByteMapDecorator map = new TShortByteMapDecorator();
    map.put((short)1, (byte)10);
    assertEquals((byte)10, map.remove((short)1).byteValue());
    assertEquals(null, map.get((short)1));
  }
  
  @Test
  public void testEntrySet() {
    TShortByteMapDecorator map = new TShortByteMapDecorator();
    map.put((short)1, (byte)10);
    map.put((short)2, (byte)20);
    assertEquals(2, map.entrySet().size());
    assertTrue(map.entrySet().contains(new Entry<Short, Byte>((short)1, (byte)10)));
    assertTrue(map.entrySet().contains(new Entry<Short, Byte>((short)2, (byte)20)));
  }
  
  @Test
  public void testContainsValue() {
    TShortByteMapDecorator map = new TShortByteMapDecorator();
    map.put((short)1, (byte)10);
    assertTrue(map.containsValue((byte)10));
    assertFalse(map.containsValue((byte)20));
  }
  
  @Test
  public void testContainsKey() {
    TShortByteMapDecorator map = new TShortByteMapDecorator();
    map.put((short)1, (byte)10);
    assertTrue(map.containsKey((short)1));
    assertFalse(map.containsKey((short)2));
  }
  
  @Test
  public void testSize() {
    TShortByteMapDecorator map = new TShortByteMapDecorator();
    assertEquals(0, map.size());
    map.put((short)1, (byte)10);
    assertEquals(1, map.size());
    map.put((short)2, (byte)20);
    assertEquals(2, map.size());
  }
  
  @Test
  public void testIsEmpty() {
    TShortByteMapDecorator map = new TShortByteMapDecorator();
    assertTrue(map.isEmpty());
    map.put((short)1, (byte)10);
    assertFalse(map.isEmpty());
    map.clear();
    assertTrue(map.isEmpty());
  }
  
  @Test
  public void testPutAll() {
    TShortByteMapDecorator map = new TShortByteMapDecorator();
    Map<Short, Byte> input = new HashMap<>();
    input.put((short)1, (byte)10);
    input.put((short)2, (byte)20);
    map.putAll(input);
    assertEquals((byte)10, map.get((short)1).byteValue());
    assertEquals((byte)20, map.get((short)2).byteValue());
  }
  
  @Test
  public void testWrapKey() {
    TShortByteMapDecorator map = new TShortByteMapDecorator();
    assertEquals(new Short((short)1), map.wrapKey((short)1));
  }
  
  @Test
  public void testUnwrapKey() {
    TShortByteMapDecorator map = new TShortByteMapDecorator();
    assertEquals((short)1, map.unwrapKey(new Short((short)1)));
  }
  
  @Test
  public void testWrapValue() {
    TShortByteMapDecorator map = new TShortByteMapDecorator();
    assertEquals(new Byte((byte)10), map.wrapValue((byte)10));
  }
  
  @Test
  public void testUnwrapValue() {
    TShortByteMapDecorator map = new TShortByteMapDecorator();
    assertEquals((byte)10, map.unwrapValue(new Byte((byte)10)));
  }
  
  @Test
  public void testReadExternal() {
    // Test externalization method
  }
  
  @Test
  public void testWriteExternal() {
    // Test externalization method
  }
  
}