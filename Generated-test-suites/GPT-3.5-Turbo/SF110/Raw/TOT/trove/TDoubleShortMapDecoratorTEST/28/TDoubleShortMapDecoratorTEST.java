import gnu.trove.decorator.TDoubleShortMapDecorator;
import gnu.trove.map.TDoubleShortMap;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

public class TDoubleShortMapDecoratorTest {

  private TDoubleShortMapDecorator mapDecorator;
  private TDoubleShortMap map;

  @Before
  public void setUp() {
    map = new TDoubleShortMap();
    mapDecorator = new TDoubleShortMapDecorator(map);
  }

  @Test
  public void testPut_NormalInput_SuccessfullyPutsElement() {
    Double key = 1.0;
    Short value = 10;

    Short result = mapDecorator.put(key, value);

    Assert.assertEquals(value, result);
    Assert.assertTrue(map.containsValue(value));
  }

  @Test(expected = NullPointerException.class)
  public void testPut_NullKey_ThrowsException() {
    mapDecorator.put(null, (short) 10);
  }

  @Test(expected = NullPointerException.class)
  public void testPut_NullValue_ThrowsException() {
    mapDecorator.put(1.0, null);
  }

  @Test
  public void testGet_ExistingKey_ReturnsValue() {
    Double key = 1.0;
    Short value = 10;
    mapDecorator.put(key, value);

    Short result = mapDecorator.get(key);

    Assert.assertEquals(value, result);
  }

  @Test
  public void testGet_NonExistingKey_ReturnsNull() {
    Double key = 1.0;

    Short result = mapDecorator.get(key);

    Assert.assertNull(result);
  }

  @Test
  public void testClear_EmptyMap_NoEffect() {
    mapDecorator.clear();

    Assert.assertTrue(map.isEmpty());
  }

  @Test
  public void testRemove_ExistingKey_ReturnsValueAndRemovesEntry() {
    Double key = 1.0;
    Short value = 10;
    mapDecorator.put(key, value);

    Short result = mapDecorator.remove(key);

    Assert.assertEquals(value, result);
    Assert.assertFalse(map.containsKey(key));
  }

  @Test
  public void testRemove_NonExistingKey_ReturnsNull() {
    Double key = 1.0;

    Short result = mapDecorator.remove(key);

    Assert.assertNull(result);
  }

  @Test
  public void testEntrySet_EmptyMap_ReturnsEmptySet() {
    Map.Entry<Double, Short> entry = mapDecorator.entrySet().iterator().next();

    Assert.assertFalse(entrySet.iterator().hasNext());
  }

  @Test
  public void testEntrySet_ExistingEntries_ReturnsAllEntries() {
    Double key1 = 1.0;
    Short value1 = 10;
    mapDecorator.put(key1, value1);
    Double key2 = 2.0;
    Short value2 = 20;
    mapDecorator.put(key2, value2);

    Map.Entry<Double, Short> entry1 = mapDecorator.entrySet().iterator().next();
    Map.Entry<Double, Short> entry2 = mapDecorator.entrySet().iterator().next();

    Assert.assertEquals(key1, entry1.getKey());
    Assert.assertEquals(value1, entry1.getValue());
    Assert.assertEquals(key2, entry2.getKey());
    Assert.assertEquals(value2, entry2.getValue());
  }

  @Test
  public void testContainsValue_ExistingValue_ReturnsTrue() {
    Short value = 10;
    mapDecorator.put(1.0, value);

    boolean result = mapDecorator.containsValue(value);

    Assert.assertTrue(result);
  }

  @Test
  public void testContainsValue_NonExistingValue_ReturnsFalse() {
    boolean result = mapDecorator.containsValue((short) 100);

    Assert.assertFalse(result);
  }

  @Test
  public void testContainsKey_ExistingKey_ReturnsTrue() {
    Double key = 1.0;
    mapDecorator.put(key, 10);

    boolean result = mapDecorator.containsKey(key);

    Assert.assertTrue(result);
  }

  @Test
  public void testContainsKey_NonExistingKey_ReturnsFalse() {
    boolean result = mapDecorator.containsKey(2.0);

    Assert.assertFalse(result);
  }

  @Test
  public void testSize_EmptyMap_ReturnsZero() {
    int result = mapDecorator.size();

    Assert.assertEquals(0, result);
  }

  @Test
  public void testSize_NonEmptyMap_ReturnsCorrectSize() {
    mapDecorator.put(1.0, 10);
    mapDecorator.put(2.0, 20);

    int result = mapDecorator.size();

    Assert.assertEquals(2, result);
  }

  @Test
  public void testIsEmpty_EmptyMap_ReturnsTrue() {
    boolean result = mapDecorator.isEmpty();

    Assert.assertTrue(result);
  }

  @Test
  public void testIsEmpty_NonEmptyMap_ReturnsFalse() {
    mapDecorator.put(1.0, 10);

    boolean result = mapDecorator.isEmpty();

    Assert.assertFalse(result);
  }

  @Test
  public void testPutAll_EmptyMap_PutsAllEntries() {
    Map<Double, Short> otherMap = new HashMap<>();
    otherMap.put(1.0, 10);
    otherMap.put(2.0, 20);

    mapDecorator.putAll(otherMap);

    Assert.assertEquals(otherMap, map);
  }

  @Test
  public void testPutAll_NonEmptyMap_PutsAllEntries() {
    mapDecorator.put(1.0, 10);
    Map<Double, Short> otherMap = new HashMap<>();
    otherMap.put(2.0, 20);
    otherMap.put(3.0, 30);

    mapDecorator.putAll(otherMap);

    Assert.assertEquals(3, map.size());
    Assert.assertTrue(map.containsKey(1.0));
    Assert.assertTrue(map.containsKey(2.0));
    Assert.assertTrue(map.containsKey(3.0));
    Assert.assertTrue(map.containsValue((short) 10));
    Assert.assertTrue(map.containsValue((short) 20));
    Assert.assertTrue(map.containsValue((short) 30));
  }

  @Test
  public void testWrapKey_ValidDouble_ReturnsDouble() {
    double key = 1.0;

    Double result = mapDecorator.wrapKey(key);

    Assert.assertEquals(key, result, 0.0);
  }

  @Test
  public void testUnwrapKey_ValidKey_ReturnsDouble() {
    Double key = 1.0;

    double result = mapDecorator.unwrapKey(key);

    Assert.assertEquals(key, result, 0.0);
  }

  @Test
  public void testWrapValue_ValidShort_ReturnsShort() {
    short value = 10;

    Short result = mapDecorator.wrapValue(value);

    Assert.assertEquals(value, result.shortValue());
  }

  @Test
  public void testUnwrapValue_ValidValue_ReturnsShort() {
    Short value = 10;

    short result = mapDecorator.unwrapValue(value);

    Assert.assertEquals(value, result);
  }

  @Test
  public void testReadExternal_ValidObjectInput_SuccessfullyReads() throws IOException, ClassNotFoundException {
    ByteArrayInputStream bais = new ByteArrayInputStream(new byte[] {0});
    ObjectInputStream ois = new ObjectInputStream(bais);

    mapDecorator.readExternal(ois);
  }

  @Test
  public void testWriteExternal_ValidObjectOutput_SuccessfullyWrites() throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);

    mapDecorator.writeExternal(oos);
    byte[] result = baos.toByteArray();

    Assert.assertEquals(1, result.length);
    Assert.assertEquals(0, result[0]);
  }
}