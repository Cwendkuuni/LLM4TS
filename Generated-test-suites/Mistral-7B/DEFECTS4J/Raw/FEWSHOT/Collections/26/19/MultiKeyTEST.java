package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MultiKeyTest {

 @Test
 public void testMultiKeyConstruction() {
 MultiKey<String> key1Key2 = new MultiKey<>("key1", "key2");
 MultiKey<String> key1Key2Key3 = new MultiKey<>("key1", "key2", "key3");
 MultiKey<String> key1Key2Key3Key4 = new MultiKey<>("key1", "key2", "key3", "key4");
 MultiKey<String> keysArray = new MultiKey<>(new String[]{"key1", "key2", "key3", "key4"});
 MultiKey<String> keysArrayCloned = new MultiKey<>(new String[]{"key1", "key2", "key3", "key4"}, true);

 assertEquals(2, key1Key2.size());
 assertEquals(3, key1Key2Key3.size());
 assertEquals(4, key1Key2Key3Key4.size());
 assertArrayEquals(new String[]{"key1", "key2"}, key1Key2.getKeys());
 assertArrayEquals(new String[]{"key1", "key2", "key3"}, key1Key2Key3.getKeys());
 assertArrayEquals(new String[]{"key1", "key2", "key3", "key4"}, keysArray.getKeys());
 assertArrayEquals(new String[]{"key1", "key2", "key3", "key4"}, keysArrayCloned.getKeys());
 }

 @Test
 public void testMultiKeyGetKey() {
 MultiKey<String> key1Key2 = new MultiKey<>("key1", "key2");
 MultiKey<String> key1Key2Key3 = new MultiKey<>("key1", "key2", "key3");
 MultiKey<String> keysArray = new MultiKey<>(new String[]{"key1", "key2", "key3", "key4"});

 assertEquals("key1", key1Key2.getKey(0));
 assertEquals("key2", key1Key2.getKey(1));
 assertEquals("key1", keysArray.getKey(0));
 assertEquals("key2", keysArray.getKey(1));
 assertEquals("key3", keysArray.getKey(2));
 }

 @Test
 public void testMultiKeyHashCode() {
 MultiKey<String> key1Key2 = new MultiKey<>("key1", "key2");
 MultiKey<String> key1Key2Cloned = new MultiKey<>(key1Key2.getKeys().clone(), true);
 MultiKey<String> key1Key2DifferentOrder = new MultiKey<>(new String[]{"key2", "key1"});

 assertEquals(key1Key2.hashCode(), key1Key2Cloned.hashCode());
 assertNotEquals(key1Key2.hashCode(), key1Key2DifferentOrder.hashCode());
 }

 @Test
 public void testMultiKeyEquals() {
 MultiKey<String> key1Key2 = new MultiKey<>("key1", "key2");
 MultiKey<String> key1Key2Cloned = new MultiKey<>(key1Key2.getKeys().clone(), true);
 MultiKey<String> key1Key2DifferentOrder = new MultiKey<>(new String[]{"key2", "key1"});
 MultiKey<String> key1Key3 = new MultiKey<>("key1", "key3");
 Map<MultiKey<String>, String> map = new HashMap<>();

 map.put(key1Key2, "value1");
 map.put(key1Key2Cloned, "value2");
 map.put(key1Key2DifferentOrder, "value3");
 map.put(key1Key3, "value4");

 assertEquals("value1", map.get(key1Key2));
 assertNull(map.get(null));
 assertNull(map.get(new MultiKey<String>(new String[]{"key1", "key4"})));
 assertEquals("value2", map.get(key1Key2Cloned));
 assertEquals("value3", map.get(key1Key2DifferentOrder));
 assertEquals("value4", map.get(key1Key3));
 }

 @Test
 public void testMultiKeyToString() {
 MultiKey<String> key1Key2 = new MultiKey<>("key1", "key2");
 MultiKey<String> key1Key2Key3 = new MultiKey<>("key1", "key2", "key3");
 MultiKey<String> keysArray = new MultiKey<>(new String[]{"key1", "key2", "key3", "key4"});

 assertEquals("MultiKey[key1, key2]", key1Key2.toString());
 assertEquals("MultiKey[key1, key2, key3]", key1Key2Key3.toString());
 assertEquals("MultiKey[key1, key2, key3, key4]", keysArray.toString());
 }
}