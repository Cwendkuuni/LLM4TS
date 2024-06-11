import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import gnu.trove.decorator.TDoubleShortMapDecorator;

public class TDoubleShortMapDecoratorTest {

  @Test
  public void testPutAndGet() {
    TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
    assertNull(map.put(1.0, (short)1));
    assertEquals(1, map.size());
    assertEquals((short)1, map.get(1.0).shortValue());

    assertNull(map.put(2.0, (short)2));
    assertEquals(2, map.size());
    assertEquals((short)2, map.get(2.0).shortValue());
  }

  @Test
  public void testPutNullValue() {
    TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
    assertNull(map.put(1.0, (Short)null));
    assertEquals(1, map.size());
    assertNull(map.get(1.0));

    assertNull(map.put(2.0, (Short)null));
    assertEquals(2, map.size());
    assertNull(map.get(2.0));
  }

  @Test
  public void testPutNullKey() {
    TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
    assertNull(map.put(null, (short)1));
    assertEquals(1, map.size());
    assertEquals((short)1, map.get((Double)null).shortValue());

    assertNull(map.put(null, (short)2));
    assertEquals(2, map.size());
    assertEquals((short)2, map.get((Double)null).shortValue());
  }

  @Test
  public void testGetNonExistentKey() {
    TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
    assertNull(map.get(1.0));
    assertNull(map.get(null));
  }

  @Test
  public void testRemove() {
    TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
    map.put(1.0, (short)1);
    map.put(2.0, (short)2);

    assertEquals((short)1, map.remove(1.0).shortValue());
    assertEquals(1, map.size());
    assertNull(map.get(1.0));

    assertNull(map.remove(1.0));
    assertEquals(1, map.size());

    assertEquals((short)2, map.remove(2.0).shortValue());
    assertEquals(0, map.size());
    assertNull(map.get(2.0));
  }

  @Test
  public void testRemoveNullKey() {
    TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
    map.put(null, (short)1);

    assertEquals((short)1, map.remove((Double)null).shortValue());
    assertEquals(0, map.size());
    assertNull(map.get((Double)null));
  }

  @Test
  public void testClear() {
    TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
    map.put(1.0, (short)1);
    map.put(2.0, (short)2);

    map.clear();
    assertEquals(0, map.size());
    assertNull(map.get(1.0));
    assertNull(map.get(2.0));
  }

  @Test
  public void testContainsKey() {
    TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
    map.put(1.0, (short)1);

    assertTrue(map.containsKey(1.0));
    assertFalse(map.containsKey(2.0));
    assertFalse(map.containsKey(null));
  }

  @Test
  public void testContainsValue() {
    TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
    map.put(1.0, (short)1);

    assertTrue(map.containsValue((short)1));
    assertFalse(map.containsValue((short)2));
    assertFalse(map.containsValue(null));
  }

  @Test
  public void testSize() {
    TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
    assertEquals(0, map.size());

    map.put(1.0, (short)1);
    assertEquals(1, map.size());

    map.put(2.0, (short)2);
    assertEquals(2, map.size());
  }

  @Test
  public void testIsEmpty() {
    TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
    assertTrue(map.isEmpty());

    map.put(1.0, (short)1);
    assertFalse(map.isEmpty());

    map.remove(1.0);
    assertTrue(map.isEmpty());
  }

  @Test
  public void testPutAll() {
    TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
    Map<Double, Short> source = new HashMap<>();
    source.put(1.0, (short)1);
    source.put(2.0, (short)2);

    map.putAll(source);
    assertEquals(2, map.size());
    assertEquals((short)1, map.get(1.0).shortValue());
    assertEquals((short)2, map.get(2.0).shortValue());
  }

  @Test
  public void testEntrySet() {
    TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
    map.put(1.0, (short)1);
    map.put(2.0, (short)2);

    Set<Map.Entry<Double, Short>> entrySet = map.entrySet();
    assertEquals(2, entrySet.size());
    assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<Double, Short>(1.0, (short)1)));
    assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<Double, Short>(2.0, (short)2)));

    Iterator<Map.Entry<Double, Short>> iterator = entrySet.iterator();
    assertTrue(iterator.hasNext());
    Map.Entry<Double, Short> entry = iterator.next();
    assertEquals((short)1, entry.getValue().shortValue());

    entry.setValue((short)3);
    assertEquals((short)3, map.get(1.0).shortValue());

    assertFalse(iterator.hasNext());
    iterator.remove();
    assertEquals(1, map.size());
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testEntrySetAdd() {
    TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
    map.entrySet().add(new AbstractMap.SimpleEntry<Double, Short>(1.0, (short)1));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testEntrySetRemove() {
    TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
    map.put(1.0, (short)1);
    map.entrySet().remove(new AbstractMap.SimpleEntry<Double, Short>(1.0, (short)1));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testEntrySetAddAll() {
    TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
    Set<Map.Entry<Double, Short>> set = new HashSet<>();
    set.add(new AbstractMap.SimpleEntry<Double, Short>(1.0, (short)1));
    set.add(new AbstractMap.SimpleEntry<Double, Short>(2.0, (short)2));
    map.entrySet().addAll(set);
  }

  @Test
  public void testReadExternalAndWriteExternal() throws IOException, ClassNotFoundException {
    TDoubleShortMapDecorator map = new TDoubleShortMapDecorator();
    map.put(1.0, (short)1);
    map.put(2.0, (short)2);

    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ObjectOutputStream out = new ObjectOutputStream(byteArrayOutputStream);
    map.writeExternal(out);
    out.close();

    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    ObjectInputStream in = new ObjectInputStream(byteArrayInputStream);
    TDoubleShortMapDecorator deserializedMap = new TDoubleShortMapDecorator();
    deserializedMap.readExternal(in);

    assertEquals(2, deserializedMap.size());
    assertEquals((short)1, deserializedMap.get(1.0).shortValue());
    assertEquals((short)2, deserializedMap.get(2.0).shortValue());
  }
}