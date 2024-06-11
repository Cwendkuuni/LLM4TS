import gnu.trove.map.TShortByteMap;
import java.util.Map;
import org.junit.Test;
import static org.junit.Assert.*;

public class TShortByteMapDecoratorTest {

  @Test
  public void testPutAndGet() {
    TShortByteMap map = new TShortByteMap();
    TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
    
    decorator.put((short) 1, (byte) 2);
    decorator.put((short) 3, (byte) 4);
    
    assertEquals((byte) 2, decorator.get((short) 1).byteValue());
    assertEquals((byte) 4, decorator.get((short) 3).byteValue());
  }
  
  @Test
  public void testClear() {
    TShortByteMap map = new TShortByteMap();
    map.put((short) 1, (byte) 2);
    map.put((short) 3, (byte) 4);
    
    TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
    decorator.clear();
    
    assertTrue(decorator.isEmpty());
  }
  
  @Test
  public void testRemove() {
    TShortByteMap map = new TShortByteMap();
    map.put((short) 1, (byte) 2);
    map.put((short) 3, (byte) 4);
    
    TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
    decorator.remove((short) 1);
    
    assertNull(decorator.get((short) 1));
  }
  
  @Test
  public void testContainsKey() {
    TShortByteMap map = new TShortByteMap();
    map.put((short) 1, (byte) 2);
    map.put((short) 3, (byte) 4);
    
    TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
    
    assertTrue(decorator.containsKey((short) 1));
    assertFalse(decorator.containsKey((short) 2));
  }
  
  @Test
  public void testContainsValue() {
    TShortByteMap map = new TShortByteMap();
    map.put((short) 1, (byte) 2);
    map.put((short) 3, (byte) 4);
    
    TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
    
    assertTrue(decorator.containsValue((byte) 2));
    assertFalse(decorator.containsValue((byte) 3));
  }
  
  @Test
  public void testSize() {
    TShortByteMap map = new TShortByteMap();
    map.put((short) 1, (byte) 2);
    map.put((short) 3, (byte) 4);
    
    TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
    
    assertEquals(2, decorator.size());
  }
  
  @Test
  public void testPutAll() {
    TShortByteMap map = new TShortByteMap();
    
    TShortByteMapDecorator decorator = new TShortByteMapDecorator(map);
    
    Map<Short, Byte> otherMap = new java.util.HashMap<>();
    otherMap.put((short) 1, (byte) 2);
    otherMap.put((short) 3, (byte) 4);
    
    decorator.putAll(otherMap);
    
    assertEquals((byte) 2, decorator.get((short) 1).byteValue());
    assertEquals((byte) 4, decorator.get((short) 3).byteValue());
  }
  
}