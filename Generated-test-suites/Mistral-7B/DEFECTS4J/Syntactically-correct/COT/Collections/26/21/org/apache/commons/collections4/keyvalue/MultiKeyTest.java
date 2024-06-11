import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

 private K key1, key2, key3, key4, key5;

 @Before
 public void setUp() {
 // Replace these with actual instances of your K type
 key1 = (K) "key1";
 key2 = (K) "key2";
 key3 = (K) "key3";
 key4 = (K) "key4";
 key5 = (K) "key5";
 }

 @Test
 public void testMultiKeyWithTwoKeys() {
 MultiKey<K> multiKey = new MultiKey<>(key1, key2);
 assertArrayEquals(new K[] { key1, key2 }, multiKey.getKeys());
 }

 @Test
 public void testMultiKeyWithThreeKeys() {
 MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);
 assertArrayEquals(new K[] { key1, key2, key3 }, multiKey.getKeys());
 }

 @Test
 public void testMultiKeyWithFourKeys() {
 MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4);
 assertArrayEquals(new K[] { key1, key2, key3, key4 }, multiKey.getKeys());
 }

 @Test
 public void testMultiKeyWithFiveKeys() {
 MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
 assertArrayEquals(new K[] { key1, key2, key3, key4, key5 }, multiKey.getKeys());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullKeysArray() {
 MultiKey<K> multiKey = new MultiKey<>(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testKeysArrayWithNullKey() {
 MultiKey<K> multiKey = new MultiKey<>(key1, null);
 }

 @Test
 public void testSize() {
 MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
 assertEquals(5, multiKey.size());
 }

 @Test
 public void testEquals() {
 MultiKey<K> multiKey1 = new MultiKey<>(key1, key2, key3, key4, key5);
 MultiKey<K> multiKey2 = new MultiKey<>(key1, key2, key3, key4, key5);
 MultiKey<K> multiKey3 = new MultiKey<>(key1, key2, key3, key4);
 MultiKey<K> multiKey4 = new MultiKey<>(key1, key2, key3, key5);
 assertTrue(multiKey1.equals(multiKey1));
 assertTrue(multiKey1.equals(multiKey2));
 assertFalse(multiKey1.equals(multiKey3));
 assertFalse(multiKey1.equals(multiKey4));
 }

 @Test
 public void testHashCode() {
 MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
 MultiKey<K> multiKeyCopy = new MultiKey<>(multiKey.getKeys().clone());
 assertEquals(multiKey.hashCode(), multiKeyCopy.hashCode());
 }
}