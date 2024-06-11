package org.apache.commons.collections4.keyvalue;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert. assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class MultiKeyTest {

 @Test
 public void testMultiKeyCreation() {
 MultiKey<String> multiKey1 = new MultiKey<>("key1", "locale1");
 MultiKey<String> multiKey2 = new MultiKey<>("key2", "locale2");
 MultiKey<String> multiKey3 = new MultiKey<>("key3", "locale3", "locale4");
 MultiKey<String> multiKey4 = new MultiKey<>(new String[]{"key4", "locale4", "locale5"});

 assertNotNull(multiKey1);
 assertNotNull(multiKey2);
 assertNotNull(multiKey3);
 assertNotNull(multiKey4);
 }

 @Test
 public void testMultiKeyGetters() {
 MultiKey<String> multiKey = new MultiKey<>("key1", "locale1", "locale2");

 assertArrayEquals(new String[]{"key1", "locale1", "locale2"}, multiKey.getKeys());
 assertTrue(multiKey.size() == 3);
 assertNotNull(multiKey.getKey(0));
 assertNotNull(multiKey.getKey(1));
 assertNotNull(multiKey.getKey(2));
 }

 @Test
 public void testMultiKeyEquals() {
 MultiKey<String> multiKey1 = new MultiKey<>("key1", "locale1");
 MultiKey<String> multiKey2 = new MultiKey<>("key1", "locale1");
 MultiKey<String> multiKey3 = new MultiKey<>("key1", "locale2");

 assertTrue(multiKey1.equals(multiKey1));
 assertTrue(multiKey1.equals(multiKey2));
 assertFalse(multiKey1.equals(multiKey3));
 }

 @Test
 public void testMultiKeyHashCode() {
 MultiKey<String> multiKey1 = new MultiKey<>("key1", "locale1");
 MultiKey<String> multiKey2 = new MultiKey<>("key1", "locale1");
 MultiKey<String> multiKey3 = new MultiKey<>("key1", "locale2");

 assertTrue(multiKey1.hashCode() == multiKey2.hashCode());
 assertFalse(multiKey1.hashCode() == multiKey3.hashCode());
 }

 @Test
 public void testMultiKeyToString() {
 MultiKey<String> multiKey = new MultiKey<>("key1", "locale1", "locale2");

 assertTrue(multiKey.toString().contains("MultiKey[key1, locale1, locale2]"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMultiKeyNullKeys() {
 MultiKey<Object> multiKey = new MultiKey<Object>(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMultiKeyNullArray() {
 MultiKey<Object> multiKey = new MultiKey<Object>(null);
 }
}