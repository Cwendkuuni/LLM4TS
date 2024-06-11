import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

 // Test constructor with two keys
 @Test
 public void testMultiKeyTwoKeys() {
 K key1 = "key1";
 K key2 = "key2";
 MultiKey<K> multiKey = new MultiKey<K>(key1, key2);
 K[] expectedKeys = new K[2];
 expectedKeys[0] = key1;
 expectedKeys[1] = key2;
 assertArrayEquals(expectedKeys, multiKey.getKeys());
 }

 // Test constructor with three keys
 @Test
 public void testMultiKeyThreeKeys() {
 K key1 = "key1";
 K key2 = "key2";
 K key3 = "key3";
 MultiKey<K> multiKey = new MultiKey<K>(key1, key2, key3);
 K[] expectedKeys = new K[3];
 expectedKeys[0] = key1;
 expectedKeys[1] = key2;
 expectedKeys[2] = key3;
 assertArrayEquals(expectedKeys, multiKey.getKeys());
 }

 // Test constructor with four keys
 @Test
 public void testMultiKeyFourKeys() {
 K key1 = "key1";
 K key2 = "key2";
 K key3 = "key3";
 K key4 = "key4";
 MultiKey<K> multiKey = new MultiKey<K>(key1, key2, key3, key4);
 K[] expectedKeys = new K[4];
 expectedKeys[0] = key1;
 expectedKeys[1] = key2;
 expectedKeys[2] = key3;
 expectedKeys[3] = key4;
 assertArrayEquals(expectedKeys, multiKey.getKeys());
 }

 // Test getKey method
 @Test
 public void testGetKey() {
 K key1 = "key1";
 K key2 = "key2";
 MultiKey<K> multiKey = new MultiKey<K>(key1, key2);
 assertEquals(key1, multiKey.getKey(0));
 assertEquals(key2, multiKey.getKey(1));
 }

 // Test size method
 @Test
 public void testSize() {
 K key1 = "key1";
 K key2 = "key2";
 MultiKey<K> multiKey = new MultiKey<K>(key1, key2);
 assertEquals(2, multiKey.size());
 }

 // Test equals method with identical MultiKey
 @Test
 public void testEqualsIdenticalMultiKey() {
 K key1 = "key1";
 K key2 = "key2";
 MultiKey<K> multiKey1 = new MultiKey<K>(key1, key2);
 MultiKey<K> multiKey2 = new MultiKey<K>(key1, key2);
 assertTrue(multiKey1.equals(multiKey2));
 }

 // Test equals method with different number of keys
 @Test
 public void testEqualsDifferentNumberOfKeys() {
 K key1 = "key1";
 K key2 = "key2";
 MultiKey<K> multiKey1 = new MultiKey<K>(key1, key2);
 MultiKey<K> multiKey2 = new MultiKey<K>(key1);
 assertFalse(multiKey1.equals(multiKey2));
 }

 // Test equals method with different keys
 @Test
 public void testEqualsDifferentKeys() {
 K key1 = "key1";
 K key2 = "key2";
 K key3 = "key3";
 MultiKey<K> multiKey1 = new MultiKey<K>(key1, key2);
 MultiKey<K> multiKey2 = new MultiKey<K>(key1, key3);
 assertFalse(multiKey1.equals(multiKey2));
 }

 // Test hashCode method with identical MultiKey
 @Test
 public void testHashCodeIdenticalMultiKey() {
 K key1 = "key1";
 K key2 = "key2";
 MultiKey<K> multiKey1 = new MultiKey<K>(key1, key2);
 MultiKey<K> multiKey2 = new MultiKey<K>(key1, key2);
 assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
 }

 // Test hashCode method with different MultiKey
 @Test
 public void testHashCodeDifferentMultiKey() {
 K key1 = "key1";
 K key2 = "key2";
 K key3 = "key3";
 MultiKey<K> multiKey1 = new MultiKey<K>(key1, key2);
 MultiKey<K> multiKey2 = new MultiKey<K>(key1, key3);
 assertNotSame(multiKey1.hashCode(), multiKey2.hashCode());
 }

 // Test toString method
 @Test
 public void testToString() {
 K key1 = "key1";
 K key2 = "key2";
 MultiKey<K> multiKey = new MultiKey<K>(key1, key2);
 assertEquals("MultiKey[key1, key2]", multiKey.toString());
 }
}