import static org.junit.Assert.*;
import org.junit.Test;

public class TDoubleShortMapDecoratorTest {

  // Test put() method
  @Test
  public void testPut() {
    TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
    assertEquals(null, map.put(1.5, (short)2));
    assertEquals(1, map.size());
    assertEquals(map.wrapValue((short)2), map.put(1.5, (short)3));
    assertEquals(1, map.size());
  }

  // Test get() method
  @Test
  public void testGet() {
    TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
    map.put(1.5, (short)2);
    assertEquals(map.wrapValue((short)2), map.get(1.5));
    assertEquals(null, map.get(2.5));
  }

  // Test clear() method
  @Test
  public void testClear() {
    TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
    map.put(1.5, (short)2);
    map.clear();
    assertEquals(0, map.size());
  }

  // Test remove() method
  @Test
  public void testRemove() {
    TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
    map.put(1.5, (short)2);
    assertEquals(map.wrapValue((short)2), map.remove(1.5));
    assertEquals(null, map.remove(2.5));
  }

  // Test entrySet() method
  @Test
  public void testEntrySet() {
    TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
    map.put(1.5, (short)2);
    assertEquals(1, map.entrySet().size());
    assertTrue(map.entrySet().contains(new AbstractMap.SimpleEntry<Double, Short>(1.5, (short)2)));
    assertFalse(map.entrySet().contains(new AbstractMap.SimpleEntry<Double, Short>(2.5, (short)3)));
  }

  // Test containsValue() method
  @Test
  public void testContainsValue() {
    TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
    map.put(1.5, (short)2);
    assertTrue(map.containsValue((short)2));
    assertFalse(map.containsValue((short)3));
  }

  // Test containsKey() method
  @Test
  public void testContainsKey() {
    TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
    map.put(1.5, (short)2);
    assertTrue(map.containsKey(1.5));
    assertFalse(map.containsKey(2.5));
  }

  // Test size() method
  @Test
  public void testSize() {
    TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
    assertEquals(0, map.size());
    map.put(1.5, (short)2);
    assertEquals(1, map.size());
  }

  // Test isEmpty() method
  @Test
  public void testIsEmpty() {
    TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
    assertTrue(map.isEmpty());
    map.put(1.5, (short)2);
    assertFalse(map.isEmpty());
  }

  // Test putAll() method
  @Test
  public void testPutAll() {
    TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
    TDoubleShortMapDecorator otherMap = new TDoubleShortMapDecorator();
    otherMap.put(1.5, (short)2);
    otherMap.put(2.5, (short)3);
    map.putAll(otherMap);
    assertEquals(2, map.size());
  }
  
  // Test readExternal() method
  @Test
  public void testReadExternal() {
    TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
    TDoubleShortMapDecorator otherMap = new TDoubleShortMapDecorator();
    otherMap.put(1.5, (short)2);
    otherMap.put(2.5, (short)3);
    try {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
      map.writeExternal(objectOutputStream);
      
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
      ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
      otherMap.readExternal(objectInputStream);
      
      objectOutputStream.close();
      objectInputStream.close();
      
      assertEquals(map.size(), otherMap.size());
      assertEquals(map.get(1.5), otherMap.get(1.5));
      assertEquals(map.get(2.5), otherMap.get(2.5));
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  // Test writeExternal() method
  @Test
  public void testWriteExternal() {
    TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
    map.put(1.5, (short)2);
    map.put(2.5, (short)3);
    try {
      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
      ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
      map.writeExternal(objectOutputStream);
      
      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
      ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
      TDoubleShortMapDecorator otherMap = new TDoubleShortMapDecorator();
      otherMap.readExternal(objectInputStream);
      
      objectOutputStream.close();
      objectInputStream.close();
      
      assertEquals(map.size(), otherMap.size());
      assertEquals(map.get(1.5), otherMap.get(1.5));
      assertEquals(map.get(2.5), otherMap.get(2.5));
    } catch (IOException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
}