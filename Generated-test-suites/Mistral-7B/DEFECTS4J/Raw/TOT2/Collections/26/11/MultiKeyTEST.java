import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.collections4.keyvalue.MultiKeytestWithMocks;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

 @Test
 public void testMultiKeyConstructorWithTwoKeys() {
 MultiKeyKey1Key2 = new MultiKey<>("key1", "key2");
 MultiKeyTestWithMocks.assertMultiKeyEquals("key1", "key2", MultiKeyKey1Key2);
 }

 @Test
 public void testMultiKeyConstructorWithThreeKeys() {
 MultiKeyKey1Key2Key3 = new MultiKey<>("key1", "key2", "key3");
 MultiKeyTestWithMocks.assertMultiKeyEquals("key1", "key2", "key3", MultiKeyKey1Key2Key3);
 }

 @Test
 public void testMultiKeyConstructorWithFourKeys() {
 MultiKeyKey1Key2Key3Key4 = new MultiKey<>("key1", "key2", "key3", "key4");
 MultiKeyTestWithMocks.assertMultiKeyEquals("key1", "key2", "key3", "key4", MultiKeyKey1Key2Key3Key4);
 }

 @Test
 public void testMultiKeyConstructorWithFiveKeys() {
 MultiKeyKey1Key2Key3Key4Key5 = new MultiKey<>("key1", "key2", "key3", "key4", "key5");
 MultiKeyTestWithMocks.assertMultiKeyEquals("key1", "key2", "key3", "key4", "key5", MultiKeyKey1Key2Key3Key4Key5);
 }

 @Test
 public void testMultiKeyConstructorWithArrayOfKeys() {
 MultiKey keyArray = new MultiKey(new String[]{"key1", "key2"});
 MultiKeyTestWithMocks.assertMultiKeyEquals("key1", "key2", keyArray.getKeys());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMultiKeyNullKeysArray() {
 try {
 new MultiKey(null);
 fail("IllegalArgumentException should be thrown");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMultiKeyCloneArray() {
 MultiKey keyArray = new MultiKey(new String[]{"key1", "key2"}).getKeys();
 try {
 new MultiKey(keyArray, false);
 fail("IllegalArgumentException should be thrown for non-cloned array");
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testMultiKeyGetKeys() {
 MultiKey keyArray = new MultiKey(new String[]{"key1", "key2"});
 String[] keys = keyArray.getKeys();
 assertArrayEquals(new String[]{"key1", "key2"}, keys);
 }

 @Test
 public void testMultiKeyGetKey() {
 MultiKey keyArray = new MultiKey(new String[]{"key1", "key2"});
 assertEquals("key1", keyArray.getKey(0));
 assertEquals("key2", keyArray.getKey(1));
 }

 @Test
 public void testMultiKeySize() {
 MultiKey keyArray = new MultiKey(new String[]{"key1", "key2"});
 assertEquals(2, keyArray.size());
 }

 @Test
 public void testMultiKeyEquals() {
 MultiKey keyArray1 = new MultiKey(new String[]{"key1", "key2"});
 MultiKey keyArray2 = new MultiKey(new String[]{"key1", "key2"});
 assertTrue(keyArray1.equals(keyArray2));
 }

 @Test
 public void testMultiKeyHashCode() {
 MultiKey keyArray1 = new MultiKey(new String[]{"key1", "key2"});
 MultiKey keyArray2 = new MultiKey(new String[]{"key1", "key2"});
 assertEquals(keyArray1.hashCode(), keyArray2.hashCode());
 }

 @Test
 public void testMultiKeyToString() {
 MultiKey keyArray = new MultiKey(new String[]{"key1", "key2"});
 assertEquals("MultiKey[key1, key2]", keyArray.toString());
 }
}

class MultiKeyTestWithMocks {
 static <K> void assertMultiKeyEquals(K... keys) {
 MultiKey expected = new MultiKey<K>(keys);
 assertEquals(expected.hashCode(), new MultiKey<K>(keys).hashCode());
 assertEquals(expected.toString(), new MultiKey<K>(keys).toString());
 assertEquals(expected, new MultiKey<K>(keys));
 }

 static void assertArrayEquals(Object[] expected, Object[] actual) {
 assertEquals(expected.length, actual.length);
 for (int i = 0; i < expected.length; i++) {
 assertEquals(expected[i], actual[i]);
 }
 }
}