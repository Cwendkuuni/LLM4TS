package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MultiKeyTest {

 @Test
 public void testMultiKeyConstruction() {
 MultiKey<String> key12 = new MultiKey<>("key1", "key2");
 MultiKey<String> key123 = new MultiKey<>("key1", "key2", "key3");
 MultiKey<String> key1234 = new MultiKey<>("key1", "key2", "key3", "key4");
 MultiKey<String> key12345 = new MultiKey<>("key1", "key2", "key3", "key4", "key5");
 MultiKey<String> keyArray = new MultiKey<>(new String[]{"key1", "key2"});
 MultiKey<String> keyArrayClone = new MultiKey<>(new String[]{"key1", "key2"}, true);

 assertEquals(2, key12.size());
 assertEquals(3, key123.size());
 assertEquals(4, key1234.size());
 assertEquals(5, key12345.size());
 assertArrayEquals(new String[]{"key1", "key2"}, key12.getKeys());
 assertArrayEquals(new String[]{"key1", "key2"}, keyArray.getKeys());
 assertArrayEquals(new String[]{"key1", "key2"}, keyArrayClone.getKeys());
 }

 @Test
 public void testGetKey() {
 MultiKey<String> key12 = new MultiKey<>("key1", "key2");
 MultiKey<String> key123 = new MultiKey<>("key1", "key2", "key3");
 MultiKey<String> key1234 = new MultiKey<>("key1", "key2", "key3", "key4");
 MultiKey<String> key12345 = new MultiKey<>("key1", "key2", "key3", "key4", "key5");

 assertEquals("key1", key12.getKey(0));
 assertEquals("key2", key12.getKey(1));
 assertEquals("key1", key123.getKey(0));
 assertEquals("key2", key123.getKey(1));
 assertEquals("key3", key123.getKey(2));
 assertEquals("key1", key1234.getKey(0));
 assertEquals("key2", key1234.getKey(1));
 assertEquals("key3", key1234.getKey(2));
 assertEquals("key4", key1234.getKey(3));
 assertEquals("key5", key12345.getKey(4));
 assertNull(key12345.getKey(5));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetKeyInvalidIndex() {
 MultiKey<String> key12 = new MultiKey<>("key1", "key2");
 MultiKey<String> key123 = new MultiKey<>("key1", "key2", "key3");
 MultiKey<String> key1234 = new MultiKey<>("key1", "key2", "key3", "key4");
 MultiKey<String> key12345 = new MultiKey<>("key1", "key2", "key3", "key4", "key5");

 key12.getKey(10);
 key123.getKey(3);
 key1234.getKey(5);
 key12345.getKey(-1);
 }

 @Test
 public void testEquals() {
 MultiKey<String> key12 = new MultiKey<>("key1", "key2");
 MultiKey<String> key12Equal = new MultiKey<>("key1", "key2");
 MultiKey<String> key12NotEqual = new MultiKey<>("key1", "key3");
 MultiKey<String> keyArray = new MultiKey<>(new String[]{"key1", "key2"});
 MultiKey<String> keyArrayDifferentSize = new MultiKey<>(new String[]{"key1", "key2", "key3"});
 MultiKey<String> keyArraySameContent = new MultiKey<>(new String[]{"key1", "key2"});
 MultiKey<String> keyArrayDifferentContent = new MultiKey<>(new String[]{"key1", "key3"});

 assertEquals(key12, key12Equal);
 assertFalse(key12.equals(key12NotEqual));
 assertFalse(key12.equals(null));
 assertFalse(key12.equals(new Object()));
 assertFalse(key12.equals(keyArray));
 assertFalse(key12.equals(keyArrayDifferentSize));
 assertTrue(key12.equals(keyArraySameContent));
 }

 @Test
 public void testHashCode() {
 MultiKey<String> key12 = new MultiKey<>("key1", "key2");
 MultiKey<String> key12Equal = new MultiKey<>("key1", "key2");
 MultiKey<String> key12NotEqual = new MultiKey<>("key1", "key3");
 MultiKey<String> keyArray = new MultiKey<>(new String[]{"key1", "key2"});
 MultiKey<String> keyArrayClone = new MultiKey<>(new String[]{"key1", "key2"}, true);

 assertEquals(key12.hashCode(), key12Equal.hashCode());
 assertNotEquals(key12.hashCode(), key12NotEqual.hashCode());
 assertNotEquals(key12.hashCode(), keyArray.hashCode());
 assertNotEquals(key12.hashCode(), keyArrayClone.hashCode());
 }

 @Test
 public void testToString() {
 MultiKey<String> key12 = new MultiKey<>("key1", "key2");
 MultiKey<String> key123 = new MultiKey<>("key1", "key2", "key3");
 MultiKey<String> key1234 = new MultiKey<>("key1", "key2", "key3", "key4");
 MultiKey<String> key12345 = new MultiKey<>("key1", "key2", "key3", "key4", "key5");

 assertEquals("MultiKey[key1, key2]", key12.toString());
 assertEquals("MultiKey[key1, key2, key3]", key123.toString());
 assertEquals("MultiKey[key1, key2, key3, key4]", key1234.toString());
 assertEquals("MultiKey[key1, key2, key3, key4, key5]", key12345.toString());
 }

}