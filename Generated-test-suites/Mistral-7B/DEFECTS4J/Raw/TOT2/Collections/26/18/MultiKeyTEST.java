import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

 // Test constructor with two keys
 @Test
 public void testMultiKeyTwoKeys() {
 MultiKey<String, String> multiKey = new MultiKey<>("key1", "locale1");
 assertNotNull(multiKey);
 assertEquals(2, multiKey.size());
 assertEquals("key1", multiKey.getKey(0));
 assertEquals("locale1", multiKey.getKey(1));
 }

 // Test constructor with three keys
 @Test
 public void testMultiKeyThreeKeys() {
 MultiKey<String, String> multiKey = new MultiKey<>("key1", "locale1", "locale2");
 assertNotNull(multiKey);
 assertEquals(3, multiKey.size());
 assertEquals("key1", multiKey.getKey(0));
 assertEquals("locale1", multiKey.getKey(1));
 assertEquals("locale2", multiKey.getKey(2));
 }

 // Test constructor with four keys
 @Test
 public void testMultiKeyFourKeys() {
 MultiKey<String, String> multiKey = new MultiKey<>("key1", "locale1", "locale2", "locale3");
 assertNotNull(multiKey);
 assertEquals(4, multiKey.size());
 assertEquals("key1", multiKey.getKey(0));
 assertEquals("locale1", multiKey.getKey(1));
 assertEquals("locale2", multiKey.getKey(2));
 assertEquals("locale3", multiKey.getKey(3));
 }

 // Test constructor with an array of keys
 @Test
 public void testMultiKeyArrayKeys() {
 MultiKey<String, String> multiKey = new MultiKey(new String[]{"key1", "locale1", "locale2"});
 assertNotNull(multiKey);
 assertEquals(3, multiKey.size());
 assertEquals("key1", multiKey.getKey(0));
 assertEquals("locale1", multiKey.getKey(1));
 assertEquals("locale2", multiKey.getKey(2));
 }

 // Test getKeys method
 @Test
 public void testGetKeys() {
 MultiKey<String, String> multiKey = new MultiKey<>("key1", "locale1", "locale2");
 K[] keys = multiKey.getKeys();
 assertNotNull(keys);
 assertEquals(3, keys.length);
 assertEquals("key1", keys[0]);
 assertEquals("locale1", keys[1]);
 assertEquals("locale2", keys[2]);
 }

 // Test getKey method
 @Test
 public void testGetKey() {
 MultiKey<String, String> multiKey = new MultiKey<>("key1", "locale1", "locale2");
 assertNotNull(multiKey.getKey(0));
 assertNotNull(multiKey.getKey(1));
 assertNotNull(multiKey.getKey(2));
 assertEquals("key1", multiKey.getKey(0));
 assertEquals("locale1", multiKey.getKey(1));
 assertEquals("locale2", multiKey.getKey(2));
 }

 // Test equals method
 @Test
 public void testEquals() {
 MultiKey<String, String> multiKey1 = new MultiKey<>("key1", "locale1", "locale2");
 MultiKey<String, String> multiKey2 = new MultiKey<>("key1", "locale1", "locale2");
 MultiKey<String, String> multiKey3 = new MultiKey<>("key1", "locale1", "locale3");
 MultiKey<Integer, Integer> multiKey4 = new MultiKey<>("key1", 1, "locale2");
 MultiKey<String, String> multiKey5 = new MultiKey<>("key2", "locale1", "locale2");

 assertTrue(multiKey1.equals(multiKey1));
 assertTrue(multiKey1.equals(multiKey2));
 assertFalse(multiKey1.equals(multiKey3));
 assertFalse(multiKey1.equals(multiKey4));
 assertFalse(multiKey1.equals(multiKey5));
 }

 // Test hashCode method
 @Test
 public void testHashCode() {
 MultiKey<String, String> multiKey1 = new MultiKey<>("key1", "locale1", "locale2");
 MultiKey<String, String> multiKey2 = new MultiKey<>("key1", "locale1", "locale2");
 MultiKey<String, String> multiKey3 = new MultiKey<>("key1", "locale1", "locale3");
 MultiKey<String, String> multiKey4 = new MultiKey<>("key2", "locale1", "locale2");
 MultiKey<Integer, Integer> multiKey5 = new MultiKey<>("key1", 1, "locale2");

 assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
 assertNotEquals(multiKey1.hashCode(), multiKey3.hashCode());
 assertNotEquals(multiKey1.hashCode(), multiKey4.hashCode());
 assertNotEquals(multiKey1.hashCode(), multiKey5.hashCode());
 }

 // Test toString method
 @Test
 public void testToString() {
 MultiKey<String, String> multiKey = new MultiKey<>("key1", "locale1", "locale2");
 assertEquals("MultiKey[key1, locale1, locale2]", multiKey.toString());
 }
}