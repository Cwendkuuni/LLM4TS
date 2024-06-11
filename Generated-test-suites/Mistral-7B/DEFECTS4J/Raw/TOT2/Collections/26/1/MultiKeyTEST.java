import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.keyvalue.MultiKeytest;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

 @Test
 public void testMultiKeyTwoKeys() {
 KeyType key1 = new KeyType("Key1");
 KeyType key2 = new KeyType("Key2");
 MultiKey<KeyType> multiKey = new MultiKey<>(key1, key2);
 assertEquals(key1, multiKey.getKey(0));
 assertEquals(key2, multiKey.getKey(1));
 assertEquals(2, multiKey.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMultiKeyNullKeys() {
 MultiKey<Object> multiKey = new MultiKey<>(new Object[]{null}, false);
 fail("Should throw IllegalArgumentException for null keys");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMultiKeyDifferentTypes() {
 KeyType key1 = new KeyType("Key1");
 Number key2 = 1;
 MultiKey<KeyType> multiKey = new MultiKey<>(key1, key2);
 fail("Should throw IllegalArgumentException for different types");
 }

 @Test
 public void testMultiKeyThreeKeys() {
 KeyType key1 = new KeyType("Key1");
 KeyType key2 = new KeyType("Key2");
 KeyType key3 = new KeyType("Key3");
 MultiKey<KeyType> multiKey = new MultiKey<>(key1, key2, key3);
 assertEquals(key1, multiKey.getKey(0));
 assertEquals(key2, multiKey.getKey(1));
 assertEquals(key3, multiKey.getKey(2));
 assertEquals(3, multiKey.size());
 }

 @Test
 public void testMultiKeyFourKeys() {
 KeyType key1 = new KeyType("Key1");
 KeyType key2 = new KeyType("Key2");
 KeyType key3 = new KeyType("Key3");
 KeyType key4 = new KeyType("Key4");
 MultiKey<KeyType> multiKey = new MultiKey<>(key1, key2, key3, key4);
 assertEquals(key1, multiKey.getKey(0));
 assertEquals(key2, multiKey.getKey(1));
 assertEquals(key3, multiKey.getKey(2));
 assertEquals(key4, multiKey.getKey(3));
 assertEquals(4, multiKey.size());
 }

 @Test
 public void testMultiKeyFiveKeys() {
 KeyType key1 = new KeyType("Key1");
 KeyType key2 = new KeyType("Key2");
 KeyType key3 = new KeyType("Key3");
 KeyType key4 = new KeyType("Key4");
 KeyType key5 = new KeyType("Key5");
 MultiKey<KeyType> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
 assertEquals(key1, multiKey.getKey(0));
 assertEquals(key2, multiKey.getKey(1));
 assertEquals(key3, multiKey.getKey(2));
 assertEquals(key4, multiKey.getKey(3));
 assertEquals(key5, multiKey.getKey(4));
 assertEquals(5, multiKey.size());
 }

 @Test
 public void testMultiKeyArray() {
 KeyType[] keys = new KeyType[5];
 for (int i = 0; i < keys.length; i++) {
 keys[i] = new KeyType("Key" + i);
 }
 MultiKey<KeyType> multiKey = new MultiKey<>(keys);
 for (int i = 0; i < keys.length; i++) {
 assertEquals(keys[i], multiKey.getKey(i));
 }
 assertEquals(5, multiKey.size());
 }

 @Test
 public void testMultiKeyCloneArray() {
 KeyType[] keys = new KeyType[5];
 for (int i = 0; i < keys.length; i++) {
 keys[i] = new KeyType("Key" + i);
 }
 MultiKey<KeyType> multiKey = new MultiKey<>(keys.clone());
 for (int i = 0; i < keys.length; i++) {
 assertEquals(keys[i], multiKey.getKey(i));
 }
 assertEquals(5, multiKey.size());
 }

 @Test
 public void testMultiKeyHashCode() {
 KeyType key1 = new KeyType("Key1");
 KeyType key2 = new KeyType("Key2");
 MultiKey<KeyType> multiKey1 = new MultiKey<>(key1, key2);
 MultiKey<KeyType> multiKey2 = new MultiKey<>(key2, key1);
 assertNotEquals(multiKey1.hashCode(), multiKey2.hashCode());
 }

 @Test
 public void testMultiKeyEquals() {
 KeyType key1 = new KeyType("Key1");
 KeyType key2 = new KeyType("Key2");
 MultiKey<KeyType> multiKey1 = new MultiKey<>(key1, key2);
 MultiKey<KeyType> multiKey2 = new MultiKey<>(key1, key2);
 MultiKey<KeyType> multiKey3 = new MultiKey<>(new KeyType("Key3"), key2);
 MultiKey<KeyType> multiKey4 = new MultiKey<>(key1, new KeyType("Key4"));
 assertTrue(multiKey1.equals(multiKey2));
 assertFalse(multiKey1.equals(multiKey3));
 assertFalse(multiKey1.equals(multiKey4));
 }

}