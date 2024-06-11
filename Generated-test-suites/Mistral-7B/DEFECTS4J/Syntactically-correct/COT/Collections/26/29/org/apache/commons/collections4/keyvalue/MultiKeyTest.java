import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

 private K key1;
 private K key2;
 private K key3;
 private K key4;
 private K key5;

 @Before
 public void setup() {
 key1 = (K) new Object();
 key2 = (K) new Object();
 key3 = (K) new Object();
 key4 = (K) new Object();
 key5 = (K) new Object();
 }

 @Test
 public void testMultiKeyTwoKeys() {
 MultiKey<K> multiKey = new MultiKey<>(key1, key2);
 assertNotNull(multiKey);
 assertEquals(2, multiKey.size());
 }

 @Test
 public void testMultiKeyThreeKeys() {
 MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);
 assertNotNull(multiKey);
 assertEquals(3, multiKey.size());
 }

 @Test
 public void testMultiKeyFourKeys() {
 MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4);
 assertNotNull(multiKey);
 assertEquals(4, multiKey.size());
 }

 @Test
 public void testMultiKeyFiveKeys() {
 MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
 assertNotNull(multiKey);
 assertEquals(5, multiKey.size());
 }

 @Test
 public void testMultiKeyArrayOfKeys() {
 MultiKey<K> multiKey = new MultiKey<>(new K[] {key1, key2});
 assertNotNull(multiKey);
 assertEquals(2, multiKey.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMultiKeyNullArrayOfKeys() {
 try {
 new MultiKey<>(null);
 fail();
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testGetKeys() {
 MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
 K[] keys = multiKey.getKeys();
 assertNotNull(keys);
 assertEquals(5, keys.length);
 }

 @Test
 public void testGetKey() {
 MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
 K key = multiKey.getKey(0);
 assertNotNull(key);
 assertEquals(key1, key);
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetKeyIndexOutOfBounds() {
 MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
 multiKey.getKey(5);
 fail();
 }

 @Test
 public void testEquals() {
 MultiKey<K> multiKey1 = new MultiKey<>(key1, key2, key3, key4, key5);
 MultiKey<K> multiKey2 = new MultiKey<>(key1, key2, key3, key4, key5);
 assertTrue(multiKey1.equals(multiKey2));
 assertTrue(multiKey2.equals(multiKey1));
 }

 @Test
 public void testEqualsDifferentElements() {
 MultiKey<K> multiKey1 = new MultiKey<>(key1, key2, key3, key4, key5);
 MultiKey<K> multiKey2 = new MultiKey<>(key1, key2, key3, key4, new Object());
 assertFalse(multiKey1.equals(multiKey2));
 assertFalse(multiKey2.equals(multiKey1));
 }

 @Test
 public void testHashCode() {
 MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
 int hashCode = multiKey.hashCode();
 MultiKey<K> multiKey2 = new MultiKey<>(key1, key2, key3, key4, key5);
 int hashCode2 = multiKey2.hashCode();
 assertEquals(hashCode, hashCode2);
 }

 @Test
 public void testToString() {
 MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
 String toString = multiKey.toString();
 assertTrue(toString.contains("MultiKey"));
 assertTrue(toString.contains("[Ljava.lang.Object;@"));
 assertTrue(toString.contains("[Ljava.lang.Object;:"));
 assertTrue(toString.contains(key1.toString()));
 assertTrue(toString.contains(key2.toString()));
 assertTrue(toString.contains(key3.toString()));
 assertTrue(toString.contains(key4.toString()));
 assertTrue(toString.contains(key5.toString()));
 }
}