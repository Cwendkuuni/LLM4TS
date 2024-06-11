import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class TDoubleShortMapDecoratorTest {

  private TDoubleShortMapDecorator map;

  @Before
  public void setUp() {
    map = new TDoubleShortMapDecorator();
  }

  @Test
  public void putAndGetKeyShouldReturnCorrectValue() {
    map.put(1.0, (short) 1);
    assertEquals(1, map.get(1.0).shortValue());
  }

  @Test
  public void putAndGetNullKeyShouldReturnCorrectValue() {
    map.put(null, (short) 10);
    assertEquals(10, map.get(null).shortValue());
  }

  @Test
  public void putAndRemoveKeyShouldReturnCorrectValue() {
    map.put(1.0, (short) 1);
    assertEquals(1, map.remove(1.0).shortValue());
  }

  @Test
  public void putAndRemoveNullKeyShouldReturnCorrectValue() {
    map.put(null, (short) 10);
    assertEquals(10, map.remove(null).shortValue());
  }

  @Test
  public void containsKeyShouldReturnTrueForExistingKey() {
    map.put(1.0, (short) 1);
    assertTrue(map.containsKey(1.0));
  }

  @Test
  public void containsKeyShouldReturnFalseForNonExistingKey() {
    assertFalse(map.containsKey(1.0));
  }

  @Test
  public void containsKeyShouldReturnFalseForNullKeyIfNoEntryKeyIsDifferent() {
    map.put(null, (short) 10);
    assertFalse(map.containsKey(null));
  }

  @Test
  public void containsKeyShouldReturnTrueForNullKeyIfNoEntryKeyIsSame() {
    TDoubleShortMapDecorator map = new TDoubleShortMapDecorator(new TDoubleShortHashMap(Double.NaN, (short) 0));
    map.put(Double.NaN, (short) 10);
    assertTrue(map.containsKey(Double.NaN));
  }

  @Test
  public void containsValueShouldReturnTrueForExistingValue() {
    map.put(1.0, (short) 1);
    assertTrue(map.containsValue((short) 1));
  }

  @Test
  public void containsValueShouldReturnFalseForNonExistingValue() {
    assertFalse(map.containsValue((short) 1));
  }

  @Test
  public void containsValueShouldReturnFalseForNullValue() {
    assertFalse(map.containsValue(null));
  }

  @Test
  public void sizeShouldReturnCorrectSize() {
    map.put(1.0, (short) 1);
    map.put(2.0, (short) 2);
    assertEquals(2, map.size());
  }

  @Test
  public void isEmptyShouldReturnTrueForEmptyMap() {
    assertTrue(map.isEmpty());
  }

  @Test
  public void isEmptyShouldReturnFalseForNonEmptyMap() {
    map.put(1.0, (short) 1);
    assertFalse(map.isEmpty());
  }

  @Test
  public void clearShouldRemoveAllEntriesFromMap() {
    map.put(1.0, (short) 1);
    map.clear();
    assertTrue(map.isEmpty());
  }

  @Test
  public void putAllShouldAddAllEntriesFromGivenMap() {
    Map<Double, Short> otherMap = new HashMap<>();
    otherMap.put(1.0, (short) 1);
    otherMap.put(2.0, (short) 2);
    map.putAll(otherMap);
    assertEquals(2, map.size());
    assertEquals(1, map.get(1.0).shortValue());
    assertEquals(2, map.get(2.0).shortValue());
  }

  @Test
  public void entrySetShouldReturnCorrectSet() {
    map.put(1.0, (short) 1);
    map.put(2.0, (short) 2);
    Set<Map.Entry<Double, Short>> entrySet = map.entrySet();
    assertEquals(2, entrySet.size());
    assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>(1.0, (short) 1)));
    assertTrue(entrySet.contains(new AbstractMap.SimpleEntry<>(2.0, (short) 2)));
  }
}