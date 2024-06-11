import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

 // Test constructor with two keys
 @Test
 public void testTwoKeysConstructor() {
 MultiKey<String, String> multiKey = new MultiKey<String, String>("key1", "key2");
 assertEquals(2, multiKey.size());
 assertArrayEquals(new String[]{"key1", "key2"}, multiKey.getKeys());
 }

 // Test constructor with three keys
 @Test
 public void testThreeKeysConstructor() {
 MultiKey<String, String> multiKey = new MultiKey<String, String>("key1", "key2", "key3");
 assertEquals(3, multiKey.size());
 assertArrayEquals(new String[]{"key1", "key2", "key3"}, multiKey.getKeys());
 }

 // Test constructor with an array of keys
 @Test
 public void testArrayConstructor() {
 MultiKey<String, String> multiKey = new MultiKey<String, String>(new String[]{"key1", "key2"});
 assertEquals(2, multiKey.size());
 assertArrayEquals(new String[]{"key1", "key2"}, multiKey.getKeys());
 }

 // Test equals method
 @Test
 public void testEquals() {
 MultiKey<String, String> multiKey1 = new MultiKey<String, String>("key1", "key2");
 MultiKey<String, String> multiKey2 = new MultiKey<String, String>("key1", "key2");
 MultiKey<String, String> multiKey3 = new MultiKey<String, String>("key1", "key3");
 assertTrue(multiKey1.equals(multiKey2));
 assertFalse(multiKey1.equals(multiKey3));
 }

 // Test getKey method
 @Test
 public void testGetKey() {
 MultiKey<String, String> multiKey = new MultiKey<String, String>("key1", "key2");
 assertEquals("key1", multiKey.getKey(0));
 assertEquals("key2", multiKey.getKey(1));
 }

 // Test hashCode method
 @Test
 public void testHashCode() {
 MultiKey<String, String> multiKey1 = new MultiKey<String, String>("key1", "key2");
 MultiKey<String, String> multiKey2 = new MultiKey<String, String>("key1", "key2");
 MultiKey<String, String> multiKey3 = new MultiKey<String, String>("key1", "key3");
 assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
 assertNotEquals(multiKey1.hashCode(), multiKey3.hashCode());
 }

 // Test size method
 @Test
 public void testSize() {
 MultiKey<String, String> multiKey = new MultiKey<String, String>("key1", "key2");
 assertEquals(2, multiKey.size());
 }

 // Test toString method
 @Test
 public void testToString() {
 MultiKey<String, String> multiKey = new MultiKey<String, String>("key1", "key2");
 assertEquals("MultiKey[key1, key2]", multiKey.toString());
 }

 // Edge case: Test null keys
 @Test(expected = IllegalArgumentException.class)
 public void testNullKeys() {
 new MultiKey<String, String>(null);
 }

 // Error scenario: Test changing keys after creation
 @Test
 public void testChangeKeysAfterCreation() {
 MultiKey<String, String> multiKey = new MultiKey<String, String>("key1", "key2");
 multiKey.keys[0] = "newKey";
 assertFalse(multiKey.equals(new MultiKey<String, String>("key1", "key2")));
 }

}