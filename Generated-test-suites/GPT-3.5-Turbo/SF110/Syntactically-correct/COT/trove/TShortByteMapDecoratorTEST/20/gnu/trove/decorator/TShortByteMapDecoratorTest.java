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
  public void testPut() {
    Short key = 1;
    Byte value = 10;
    Byte result = mapDecorator.put(key, value);
    assertEquals(value, result);
  }
  
  @Test
  public void testGet() {
    Short key = 1;
    Byte value = 10;
    mapDecorator.put(key, value);
    Byte result = mapDecorator.get(key);
    assertEquals(value, result);
  }
  
  @Test
  public void testClear() {
    Short key = 1;
    Byte value = 10;
    mapDecorator.put(key, value);
    mapDecorator.clear();
    assertTrue(mapDecorator.isEmpty());
  }
  
  @Test
  public void testRemove() {
    Short key = 1;
    Byte value = 10;
    mapDecorator.put(key, value);
    Byte result = mapDecorator.remove(key);
    assertEquals(value, result);
    assertNull(mapDecorator.get(key));
  }
  
  @Test
  public void testContainsValue() {
    Byte value = 10;
    mapDecorator.put((short)1, value);
    assertTrue(mapDecorator.containsValue(value));
    assertFalse(mapDecorator.containsValue((byte)20));
  }
  
  @Test
  public void testContainsKey() {
    Short key = 1;
    Byte value = 10;
    mapDecorator.put(key, value);
    assertTrue(mapDecorator.containsKey(key));
    assertFalse(mapDecorator.containsKey((short)2));
  }
  
  @Test
  public void testSize() {
    mapDecorator.put((short)1, (byte)10);
    mapDecorator.put((short)2, (byte)20);
    mapDecorator.put((short)3, (byte)30);
    assertEquals(3, mapDecorator.size());
  }
  
  @Test
  public void testIsEmpty() {
    assertTrue(mapDecorator.isEmpty());
    mapDecorator.put((short)1, (byte)10);
    assertFalse(mapDecorator.isEmpty());
  }
  
  @Test
  public void testPutAll() {
    Map<Short, Byte> map = new HashMap<>();
    map.put((short)1, (byte)10);
    map.put((short)2, (byte)20);
    map.put((short)3, (byte)30);
    mapDecorator.putAll(map);
    assertEquals(3, mapDecorator.size());
  }
  
  // Additional test cases for edge cases and exceptions
  
  @Test
  public void testPutWithNullKey() {
    Byte value = 10;
    Byte result = mapDecorator.put(null, value);
    assertNull(result);
  }
  
  @Test
  public void testPutWithNullValue() {
    Short key = 1;
    Byte result = mapDecorator.put(key, null);
    assertNull(result);
  }
  
  @Test
  public void testGetWithNullKey() {
    Byte result = mapDecorator.get(null);
    assertNull(result);
  }
  
  @Test
  public void testRemoveWithNullKey() {
    Byte result = mapDecorator.remove(null);
    assertNull(result);
  }
  
  @Test
  public void testEntrySet() {
    mapDecorator.put((short)1, (byte)10);
    mapDecorator.put((short)2, (byte)20);
    mapDecorator.put((short)3, (byte)30);
    
    Set<Map.Entry<Short, Byte>> entrySet = mapDecorator.entrySet();
    assertEquals(3, entrySet.size());
    
    for (Map.Entry<Short, Byte> entry : entrySet) {
      assertTrue(mapDecorator.containsKey(entry.getKey()));
      assertTrue(mapDecorator.containsValue(entry.getValue()));
    }
    
    // Test modification through iterator
    Iterator<Map.Entry<Short, Byte>> iterator = entrySet.iterator();
    iterator.next();
    iterator.remove();
    assertEquals(2, mapDecorator.size());
    
    assertFalse(mapDecorator.containsKey((short)1));
    assertFalse(mapDecorator.containsValue((byte)10));
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testEntrySetAdd() {
    Set<Map.Entry<Short, Byte>> entrySet = mapDecorator.entrySet();
    entrySet.add(new Map.Entry<Short, Byte>() {
      // create a temporary entry for testing add operation
      @Override
      public Short getKey() {
        return 1;
      }
      @Override
      public Byte getValue() {
        return 10;
      }
      @Override
      public Byte setValue(Byte value) {
        throw new UnsupportedOperationException();
      }
    });
  }
  
  @Test
  public void testEntrySetRemove() {
    mapDecorator.put((short)1, (byte)10);
    mapDecorator.put((short)2, (byte)20);
    mapDecorator.put((short)3, (byte)30);
    Set<Map.Entry<Short, Byte>> entrySet = mapDecorator.entrySet();
    
    assertTrue(entrySet.remove(new Map.Entry<Short, Byte>() {
      // create a temporary entry for testing remove operation
      @Override
      public Short getKey() {
        return 1;
      }
      @Override
      public Byte getValue() {
        return 10;
      }
      @Override
      public Byte setValue(Byte value) {
        throw new UnsupportedOperationException();
      }
    }));
    
    assertFalse(mapDecorator.containsKey((short)1));
    assertFalse(mapDecorator.containsValue((byte)10));
  }
  
  @Test(expected = UnsupportedOperationException.class)
  public void testEntrySetAddAll() {
    Set<Map.Entry<Short, Byte>> entrySet = mapDecorator.entrySet();
    entrySet.addAll(new ArrayList<>()); // addAll should throw UnsupportedOperationException
  }
  
  @Test
  public void testEntrySetClear() {
    mapDecorator.put((short)1, (byte)10);
    mapDecorator.put((short)2, (byte)20);
    
    Set<Map.Entry<Short, Byte>> entrySet = mapDecorator.entrySet();
    entrySet.clear();
    
    assertTrue(mapDecorator.isEmpty());
  }
  
  @Test
  public void testReadExternal() throws IOException, ClassNotFoundException {
    TShortByteMapDecorator newMap = new TShortByteMapDecorator();
    newMap.readExternal(new ObjectInputStream(new ByteArrayInputStream(mapDecorator.toByteArray())));
    
    assertEquals(mapDecorator, newMap);
  }
  
  @Test
  public void testWriteExternal() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    
    mapDecorator.writeExternal(oos);
    oos.flush();
    
    byte[] data = baos.toByteArray();
    
    ByteArrayInputStream bais = new ByteArrayInputStream(data);
    ObjectInputStream ois = new ObjectInputStream(bais);
    
    TShortByteMapDecorator newMap = new TShortByteMapDecorator();
    newMap.readExternal(ois);
    
    assertEquals(mapDecorator, newMap);
  }
}