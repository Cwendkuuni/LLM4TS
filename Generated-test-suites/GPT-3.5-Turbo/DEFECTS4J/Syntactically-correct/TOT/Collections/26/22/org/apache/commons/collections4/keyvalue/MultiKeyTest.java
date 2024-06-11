import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class MultiKeyTest {
    
  private final String key1 = "key1";
  private final String key2 = "key2";
  private final Integer key3 = Integer.valueOf(3);
  private final Integer key4 = Integer.valueOf(4);
  private final Long key5 = Long.valueOf(5);
    
  @Test
  public void testConstructorWithTwoKeys() {
    MultiKey<String> multiKey = new MultiKey<>(key1, key2);
    assertNotNull(multiKey);
    assertEquals(2, multiKey.size());
    assertEquals(key1, multiKey.getKey(0));
    assertEquals(key2, multiKey.getKey(1));
  }
    
  @Test
  public void testConstructorWithThreeKeys() {
    MultiKey<Integer> multiKey = new MultiKey<>(key3, key4, key5);
    assertNotNull(multiKey);
    assertEquals(3, multiKey.size());
    assertEquals(key3, multiKey.getKey(0));
    assertEquals(key4, multiKey.getKey(1));
    assertEquals(key5, multiKey.getKey(2));
  }
    
  @Test
  public void testConstructorWithFourKeys() {
    MultiKey<Integer> multiKey = new MultiKey<>(key1, key2, key3, key4);
    assertNotNull(multiKey);
    assertEquals(4, multiKey.size());
    assertEquals(key1, multiKey.getKey(0));
    assertEquals(key2, multiKey.getKey(1));
    assertEquals(key3, multiKey.getKey(2));
    assertEquals(key4, multiKey.getKey(3));
  }
    
  @Test
  public void testConstructorWithFiveKeys() {
    MultiKey<Long> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
    assertNotNull(multiKey);
    assertEquals(5, multiKey.size());
    assertEquals(key1, multiKey.getKey(0));
    assertEquals(key2, multiKey.getKey(1));
    assertEquals(key3, multiKey.getKey(2));
    assertEquals(key4, multiKey.getKey(3));
    assertEquals(key5, multiKey.getKey(4));
  }
    
  @Test
  public void testConstructorWithArray() {
    Integer[] keys = { key3, key4, key5 };
    MultiKey<Integer> multiKey = new MultiKey<>(keys);
    assertNotNull(multiKey);
    assertEquals(3, multiKey.size());
    assertEquals(key3, multiKey.getKey(0));
    assertEquals(key4, multiKey.getKey(1));
    assertEquals(key5, multiKey.getKey(2));
  }
    
  @Test(expected = IllegalArgumentException.class)
  public void testConstructorWithNullArray() {
    Integer[] keys = null;
    MultiKey<Integer> multiKey = new MultiKey<>(keys);
  }
    
  @Test
  public void testGetKeys() {
    MultiKey<String> multiKey = new MultiKey<>(key1, key2);
    String[] keys = multiKey.getKeys();
    assertNotNull(keys);
    assertEquals(2, keys.length);
    assertEquals(key1, keys[0]);
    assertEquals(key2, keys[1]);
  }
    
  @Test
  public void testEqualsWithSameInstance() {
    MultiKey<String> multiKey = new MultiKey<>(key1, key2);
    assertTrue(multiKey.equals(multiKey));
  }
    
  @Test
  public void testEqualsWithNull() {
    MultiKey<String> multiKey = new MultiKey<>(key1, key2);
    assertNotEquals(multiKey, null);
  }
    
  @Test
  public void testEqualsWithDifferentClass() {
    MultiKey<String> multiKey = new MultiKey<>(key1, key2);
    String str = "test";
    assertNotEquals(multiKey, str);
  }
    
  @Test
  public void testEqualsWithDifferentKeys() {
    MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
    MultiKey<String> multiKey2 = new MultiKey<>(key2, key1);
    assertNotEquals(multiKey1, multiKey2);
  }
    
  @Test
  public void testEqualsWithSameKeys() {
    MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
    MultiKey<String> multiKey2 = new MultiKey<>(key1, key2);
    assertEquals(multiKey1, multiKey2);
    assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
  }
    
  @Test
  public void testHashCodeWithSameKeys() {
    MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
    MultiKey<String> multiKey2 = new MultiKey<>(key1, key2);
    assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
  }
    
  @Test
  public void testHashCodeWithDifferentKeys() {
    MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
    MultiKey<String> multiKey2 = new MultiKey<>(key2, key1);
    assertNotEquals(multiKey1.hashCode(), multiKey2.hashCode());
  }
    
  @Test
  public void testToString() {
    MultiKey<String> multiKey = new MultiKey<>(key1, key2, key2);
    String expected = "MultiKey[" + key1 + ", " + key2 + ", " + key2 + "]";
    assertEquals(expected, multiKey.toString());
  }
  
}