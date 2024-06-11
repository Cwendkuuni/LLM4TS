import static org.junit.Assert.*;
import org.junit.Test;

public class TDoubleShortMapDecoratorTest {

  @Test
  public void testPut() {
    TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
    mapDecorator.put(1.0, (short) 1);
    assertTrue(mapDecorator.get(1.0) == 1);
    
    mapDecorator.put(2.0, (short) 2);
    assertTrue(mapDecorator.get(2.0) == 2);
    
    mapDecorator.put(3.0, (short) 3);
    assertTrue(mapDecorator.get(3.0) == 3);
    
    mapDecorator.put(null, (short) 4);
    assertTrue(mapDecorator.get(null) == 4);
  }

  @Test
  public void testGet() {
    TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
    mapDecorator.put(1.0, (short) 1);
    assertTrue(mapDecorator.get(1.0) == 1);
    
    mapDecorator.put(2.0, (short) 2);
    assertTrue(mapDecorator.get(2.0) == 2);
    
    mapDecorator.put(3.0, (short) 3);
    assertTrue(mapDecorator.get(3.0) == 3);
    
    mapDecorator.put(null, (short) 4);
    assertTrue(mapDecorator.get(null) == 4);

    assertTrue(mapDecorator.get(4.0) == null);
  }

  @Test
  public void testClear() {
    TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
    mapDecorator.put(1.0, (short) 1);
    mapDecorator.put(2.0, (short) 2);
    mapDecorator.put(3.0, (short) 3);
    mapDecorator.put(null, (short) 4);

    mapDecorator.clear();
    assertTrue(mapDecorator.size() == 0);
  }

  @Test
  public void testRemove() {
    TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
    mapDecorator.put(1.0, (short) 1);
    mapDecorator.put(2.0, (short) 2);
    mapDecorator.put(3.0, (short) 3);
    mapDecorator.put(null, (short) 4);

    assertTrue(mapDecorator.remove(1.0) == 1);
    assertTrue(mapDecorator.remove(2.0) == 2);
    assertTrue(mapDecorator.remove(3.0) == 3);
    assertTrue(mapDecorator.remove(null) == 4);
    assertTrue(mapDecorator.remove(4.0) == null);
  }

  @Test
  public void testContainsValue() {
    TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
    mapDecorator.put(1.0, (short) 1);
    mapDecorator.put(2.0, (short) 2);
    mapDecorator.put(3.0, (short) 3);
    mapDecorator.put(null, (short) 4);

    assertTrue(mapDecorator.containsValue((short) 1));
    assertTrue(mapDecorator.containsValue((short) 2));
    assertTrue(mapDecorator.containsValue((short) 3));
    assertTrue(mapDecorator.containsValue((short) 4));
    assertFalse(mapDecorator.containsValue((short) 5));
  }

  @Test
  public void testContainsKey() {
    TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
    mapDecorator.put(1.0, (short) 1);
    mapDecorator.put(2.0, (short) 2);
    mapDecorator.put(3.0, (short) 3);
    mapDecorator.put(null, (short) 4);

    assertTrue(mapDecorator.containsKey(1.0));
    assertTrue(mapDecorator.containsKey(2.0));
    assertTrue(mapDecorator.containsKey(3.0));
    assertTrue(mapDecorator.containsKey(null));
    assertFalse(mapDecorator.containsKey(4.0));
  }

  @Test
  public void testSize() {
    TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
    mapDecorator.put(1.0, (short) 1);
    mapDecorator.put(2.0, (short) 2);
    mapDecorator.put(3.0, (short) 3);
    mapDecorator.put(null, (short) 4);

    assertTrue(mapDecorator.size() == 4);
    mapDecorator.remove(1.0);
    assertTrue(mapDecorator.size() == 3);
    mapDecorator.clear();
    assertTrue(mapDecorator.size() == 0);
  }

  @Test
  public void testIsEmpty() {
    TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
    assertTrue(mapDecorator.isEmpty());
    mapDecorator.put(1.0, (short) 1);
    assertFalse(mapDecorator.isEmpty());
    mapDecorator.remove(1.0);
    assertTrue(mapDecorator.isEmpty());
  }

  @Test
  public void testPutAll() {
    Map<Double, Short> testMap = new HashMap<>();
    testMap.put(1.0, (short) 1);
    testMap.put(2.0, (short) 2);
    testMap.put(3.0, (short) 3);
    testMap.put(null, (short) 4);

    TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
    mapDecorator.putAll(testMap);

    assertTrue(mapDecorator.get(1.0) == 1);
    assertTrue(mapDecorator.get(2.0) == 2);
    assertTrue(mapDecorator.get(3.0) == 3);
    assertTrue(mapDecorator.get(null) == 4);
  }

  @Test
  public void testWrapKey() {
    TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
    assertEquals(1.0, mapDecorator.wrapKey(1.0), 0);
    assertEquals(2.0, mapDecorator.wrapKey(2.0), 0);
    assertEquals(3.0, mapDecorator.wrapKey(3.0), 0);
  }

  @Test
  public void testUnwrapKey() {
    TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
    assertEquals(1.0, mapDecorator.unwrapKey(1.0), 0);
    assertEquals(2.0, mapDecorator.unwrapKey(2.0), 0);
    assertEquals(3.0, mapDecorator.unwrapKey(3.0), 0);
  }

  @Test
  public void testWrapValue() {
    TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
    assertTrue(mapDecorator.wrapValue((short) 1) == 1);
    assertTrue(mapDecorator.wrapValue((short) 2) == 2);
    assertTrue(mapDecorator.wrapValue((short) 3) == 3);
  }

  @Test
  public void testUnwrapValue() {
    TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
    assertTrue(mapDecorator.unwrapValue((short) 1) == 1);
    assertTrue(mapDecorator.unwrapValue((short) 2) == 2);
    assertTrue(mapDecorator.unwrapValue((short) 3) == 3);
  }

  @Test
  public void testReadExternal() {
    TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
    mapDecorator.put(1.0, (short) 1);
    mapDecorator.put(2.0, (short) 2);
    mapDecorator.put(3.0, (short) 3);
    mapDecorator.put(null, (short) 4);

    // write to external storage
    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("test.bin"))) {
      mapDecorator.writeExternal(out);
    } catch (Exception e) {
      fail(e.getMessage());
    }

    // read from external storage
    TDoubleShortMapDecorator newMapDecorator = new TDoubleShortMapDecorator();
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream("test.bin"))) {
      newMapDecorator.readExternal(in);
    } catch (Exception e) {
      fail(e.getMessage());
    }

    // check if the maps are equal
    assertTrue(newMapDecorator.get(1.0) == 1);
    assertTrue(newMapDecorator.get(2.0) == 2);
    assertTrue(newMapDecorator.get(3.0) == 3);
    assertTrue(newMapDecorator.get(null) == 4);
  }

  @Test
  public void testWriteExternal() {
    TDoubleShortMapDecorator mapDecorator = new TDoubleShortMapDecorator();
    mapDecorator.put(1.0, (short) 1);
    mapDecorator.put(2.0, (short) 2);
    mapDecorator.put(3.0, (short) 3);
    mapDecorator.put(null, (short) 4);

    // write to external storage
    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("test.bin"))) {
      mapDecorator.writeExternal(out);
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }
}