import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import org.junit.Assert.*;

public class MultiKeyTest {

 // Test constructor with two keys
 @Test
 public void testMultiKeyTwoKeys() {
 K key1 = "key1";
 K key2 = "key2";
 MultiKey<K> multiKey = new MultiKey<>(key1, key2);
 K[] expectedKeys = new K[]{key1, key2};
 assertArrayEquals(expectedKeys, multiKey.getKeys());
 }

 // Test constructor with three keys
 @Test
 public void testMultiKeyThreeKeys() {
 K key1 = "key1";
 K key2 = "key2";
 K key3 = "key3";
 MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);
 K[] expectedKeys = new K[]{key1, key2, key3};
 assertArrayEquals(expectedKeys, multiKey.getKeys());
 }

 // Test constructor with four keys
 @Test
 public void testMultiKeyFourKeys() {
 K key1 = "key1";
 K key2 = "key2";
 K key3 = "key3";
 K key4 = "key4";
 MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4);
 K[] expectedKeys = new K[]{key1, key2, key3, key4};
 assertArrayEquals(expectedKeys, multiKey.getKeys());
 }

 // Test constructor with five keys
 @Test
 public void testMultiKeyFiveKeys() {
 K key1 = "key1";
 K key2 = "key2";
 K key3 = "key3";
 K key4 = "key4";
 K key5 = "key5";
 MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
 K[] expectedKeys = new K[]{key1, key2, key3, key4, key5};
 assertArrayEquals(expectedKeys, multiKey.getKeys());
 }

 // Test constructor with an array of keys
 @Test
 public void testMultiKeyArrayKeys() {
 K[] keys = new K[]{"key1", "key2", "key3"};
 MultiKey<K> multiKey = new MultiKey<>(keys);
 assertArrayEquals(keys, multiKey.getKeys());
 }

 // Test getKey method
 @Test
 public void testGetKey() {
 K key1 = "key1";
 K key2 = "key2";
 MultiKey<K> multiKey = new MultiKey<>(key1, key2);
 assertEquals(key1, multiKey.getKey(0));
 assertEquals(key2, multiKey.getKey(1));
 }

 // Test size method
 @Test
 public void testSize() {
 K key1 = "key1";
 K key2 = "key2";
 MultiKey<K> multiKey = new MultiKey<>(key1, key2);
 assertEquals(2, multiKey.size());
 }

 // Test equals method
 @Test
 public void testEquals() {
 K key1 = "key1";
 K key2 = "key2";
 MultiKey<K> multiKey1 = new MultiKey<>(key1, key2);
 MultiKey<K> multiKey2 = new MultiKey<>(key1, key2);
 MultiKey<K> multiKey3 = new MultiKey<>(key1, new K() {}); // Different instance of key2
 MultiKey<String> multiKey4 = new MultiKey<>("key1", "key2"); // Different type
 K[] keys = new K[]{"key1", "key2"};
 MultiKey<K> multiKey5 = new MultiKey<>(keys);
 MultiKey<K> multiKey6 = new MultiKey<>(keys.clone());
 MultiKey<K> multiKey7 = new MultiKey<>(keys); // Same instance of keys
 Assert.assertTrue(multiKey1.equals(multiKey2));
 Assert.assertFalse(multiKey1.equals(multiKey3));
 Assert.assertFalse(multiKey1.equals(multiKey4));
 Assert.assertTrue(multiKey1.equals(multiKey5));
 Assert.assertTrue(multiKey1.equals(multiKey6));
 Assert.assertTrue(multiKey1.equals(multiKey7));
 }

 // Test hashCode method
 @Test
 public void testHashCode() {
 K key1 = "key1";
 K key2 = "key2";
 MultiKey<K> multiKey = new MultiKey<>(key1, key2);
 K[] keys = new K[]{"key1", "key2"};
 MultiKey<K> multiKeyClone = new MultiKey<>(keys.clone());
 int expectedHashCode = 41 * 31 + key1.hashCode() + 31 * 31 + key2.hashCode();
 int actualHashCode = multiKey.hashCode();
 int actualHashCodeClone = multiKeyClone.hashCode();
 Assert.assertEquals(expectedHashCode, actualHashCode);
 Assert.assertEquals(expectedHashCode, actualHashCodeClone);
 }

 // Test toString method
 @Test
 public void testToString() {
 K key1 = "key1";
 K key2 = "key2";
 MultiKey<K> multiKey = new MultiKey<>(key1, key2);
 String expectedString = "MultiKey[MultiKey[key1, key2]]";
 String actualString = multiKey.toString();
 Assert.assertEquals(expectedString, actualString);
 }

 // Edge cases and error scenarios

 // Test constructor with null key array
 @Test(expected = IllegalArgumentException.class)
 public void testMultiKeyNullKeys() {
 MultiKey<K> multiKey = new MultiKey<>(null);
 }

 // Test getKey with negative index
 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetKeyNegativeIndex() {
 MultiKey<K> multiKey = new MultiKey<>(new K[]{"key1", "key2"});
 multiKey.getKey(-1);
 }

 // Test getKey with index greater than the size of the MultiKey
 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetKeyIndexOutOfBounds() {
 MultiKey<K> multiKey = new MultiKey<>(new K[]{"key1", "key2"});
 multiKey.getKey(2);
 }

 // Test equals method with null object
 @Test
 public void testEqualsNullObject() {
 MultiKey<K> multiKey = new MultiKey<>(new K[]{"key1", "key2"});
 Assert.assertFalse(multiKey.equals(null));
 }
}