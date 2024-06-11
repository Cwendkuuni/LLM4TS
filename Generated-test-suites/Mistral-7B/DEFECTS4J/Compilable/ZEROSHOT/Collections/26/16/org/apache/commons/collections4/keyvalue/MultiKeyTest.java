package org.apache.commons.collections4.keyvalue;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

 @Test
 public void testMultiKeyConstructorWithTwoKeys() {
 MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
 String[] expectedKeys = new String[]{"key1", "key2"};
 assertArrayEquals(expectedKeys, multiKey.getKeys());
 }

 @Test
 public void testMultiKeyConstructorWithThreeKeys() {
 MultiKey<String> multiKey = new MultiKey<>("key1", "key2", "key3");
 String[] expectedKeys = new String[]{"key1", "key2", "key3"};
 assertArrayEquals(expectedKeys, multiKey.getKeys());
 }

 @Test
 public void testMultiKeyGetKey() {
 MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
 String key1 = multiKey.getKey(0);
 String key2 = multiKey.getKey(1);
 assertEquals("key1", key1);
 assertEquals("key2", key2);
 }

 @Test
 public void testMultiKeySize() {
 MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
 int expectedSize = 2;
 assertEquals(expectedSize, multiKey.size());
 }

 @Test
 public void testMultiKeyEquals() {
 MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
 MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");
 MultiKey<String> multiKey3 = new MultiKey<>("key1", "key3");
 assertTrue(multiKey1.equals(multiKey1));
 assertTrue(multiKey1.equals(multiKey2));
 assertFalse(multiKey1.equals(multiKey3));
 }

 @Test
 public void testMultiKeyHashCode() {
 MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
 int expectedHashCode = 32 * "key1".hashCode() ^ "key2".hashCode();
 assertEquals(expectedHashCode, multiKey.hashCode());
 }

 @Test
 public void testMultiKeyToString() {
 MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
 String expectedToString = "MultiKey[key1, key2]";
 assertEquals(expectedToString, multiKey.toString());
 }
}