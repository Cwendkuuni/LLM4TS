import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TShortByteMapDecoratorTest {
  
  private TShortByteMapDecorator mapDecorator;
  
  @Before
  public void setUp() {
    TShortByteMap map = new TShortByteHashMap();
    map.put((short)1, (byte)10);
    map.put((short)2, (byte)20);
    map.put((short)3, (byte)30);
    
    mapDecorator = new TShortByteMapDecorator(map);
  }
  
  @Test
  public void testPut() {
    assertEquals(null, mapDecorator.put((short)4, (byte)40));
    assertEquals(Byte.valueOf((byte)40), mapDecorator.put((short)4, (byte)50));
  }
  
  @Test
  public void testGet() {
    assertEquals(Byte.valueOf((byte)10), mapDecorator.get((short)1));
    assertEquals(Byte.valueOf((byte)20), mapDecorator.get((short)2));
    assertEquals(Byte.valueOf((byte)30), mapDecorator.get((short)3));
    assertEquals(null, mapDecorator.get((short)4));
  }
  
  @Test
  public void testClear() {
    mapDecorator.clear();
    assertEquals(0, mapDecorator.size());
    assertEquals(true, mapDecorator.isEmpty());
  }
  
  @Test
  public void testRemove() {
    assertEquals(Byte.valueOf((byte)10), mapDecorator.remove((short)1));
    assertEquals(null, mapDecorator.remove((short)1));
  }
  
  @Test
  public void testContainsKey() {
    assertEquals(true, mapDecorator.containsKey((short)1));
    assertEquals(false, mapDecorator.containsKey((short)4));
  }
  
  @Test
  public void testContainsValue() {
    assertEquals(true, mapDecorator.containsValue((byte)10));
    assertEquals(false, mapDecorator.containsValue((byte)40));
  }
  
  @Test
  public void testSize() {
    assertEquals(3, mapDecorator.size());
  }
  
  @Test
  public void testIsEmpty() {
    assertEquals(false, mapDecorator.isEmpty());
    mapDecorator.clear();
    assertEquals(true, mapDecorator.isEmpty());
  }
  
  @Test
  public void testPutAll() {
    TShortByteMap toAdd = new TShortByteHashMap();
    toAdd.put((short)4, (byte)40);
    toAdd.put((short)5, (byte)50);
    
    mapDecorator.putAll(toAdd);
    
    assertEquals(Byte.valueOf((byte)40), mapDecorator.get((short)4));
    assertEquals(Byte.valueOf((byte)50), mapDecorator.get((short)5));
  }
  
  // Add more tests for entrySet, wrapKey, unwrapKey, wrapValue, unwrapValue, readExternal, writeExternal

}