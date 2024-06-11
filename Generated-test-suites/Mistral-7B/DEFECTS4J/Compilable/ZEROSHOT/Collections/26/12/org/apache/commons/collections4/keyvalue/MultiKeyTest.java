package org.apache.commons.collections4.keyvalue;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

 @Test
 public void testMultiKeyConstruction() {
 MultiKey<String> multiKey1 = new MultiKey<>("key1", "locale1");
 MultiKey<String> multiKey2 = new MultiKey<>("key2", "locale2");
 MultiKey<String> multiKey3 = new MultiKey<>("key3", "locale3", "locale4");
 MultiKey<String> multiKey4 = new MultiKey<>(new String[]{"key1", "locale1", "locale2"});
 MultiKey<String> multiKey5 = new MultiKey<>(new String[]{"key1", "locale1", "locale2"}, false);

 assertEquals("key1,locale1", multiKey1.toString());
 assertEquals("key2,locale2", multiKey2.toString());
 assertEquals("key3,locale3,locale4", multiKey3.toString());
 assertEquals("key1,locale1,locale2", multiKey4.toString());
 assertEquals("key1,locale1,locale2", multiKey5.toString());
 }

 @Test
 public void testMultiKeyGetKeys() {
 MultiKey<String> multiKey = new MultiKey<>("key1", "locale1");
 String[] keys = multiKey.getKeys();
 assertEquals(2, keys.length);
 assertEquals("key1", keys[0]);
 assertEquals("locale1", keys[1]);
 }

 @Test
 public void testMultiKeyGetKey() {
 MultiKey<String> multiKey = new MultiKey<>("key1", "locale1");
 assertEquals("key1", multiKey.getKey(0));
 assertEquals("locale1", multiKey.getKey(1));
 }

 @Test
 public void testMultiKeySize() {
 MultiKey<String> multiKey = new MultiKey<>("key1", "locale1");
 assertEquals(2, multiKey.size());
 }

 @Test
 public void testMultiKeyEquals() {
 MultiKey<String> multiKey1 = new MultiKey<>("key1", "locale1");
 MultiKey<String> multiKey2 = new MultiKey<>("key1", "locale1");
 MultiKey<String> multiKey3 = new MultiKey<>("key2", "locale2");

 assertTrue(multiKey1.equals(multiKey1));
 assertTrue(multiKey1.equals(multiKey2));
 assertFalse(multiKey1.equals(multiKey3));
 }

 @Test
 public void testMultiKeyHashCode() {
 MultiKey<String> multiKey1 = new MultiKey<>("key1", "locale1");
 MultiKey<String> multiKey2 = new MultiKey<>("key1", "locale1");
 MultiKey<String> multiKey3 = new MultiKey<>("key2", "locale2");

 assertEquals(multiKey1.hashCode(), multiKey1.hashCode());
 assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
 assertNotSame(multiKey1.hashCode(), multiKey3.hashCode());
 }
}