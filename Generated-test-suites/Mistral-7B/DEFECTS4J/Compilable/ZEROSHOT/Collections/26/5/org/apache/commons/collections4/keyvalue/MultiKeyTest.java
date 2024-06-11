package org.apache.commons.collections4.keyvalue;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertArrayEquals;

public class MultiKeyTest {

 @Test
 public void testMultiKeyConstruction() {
 MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
 MultiKey<String> multiKey2 = new MultiKey<>("key3", "key4");
 MultiKey<String> multiKey3 = new MultiKey<>("key1", "key2", "key3");
 MultiKey<String> multiKey4 = new MultiKey<>("key1", "key2", "key3", "key4");
 MultiKey<String> multiKey5 = new MultiKey(new String[]{"key1", "key2", "key3", "key4"});

 assertEquals("key1, key2", multiKey1.toString());
 assertEquals("key3, key4", multiKey2.toString());
 assertEquals("key1, key2, key3", multiKey3.toString());
 assertEquals("key1, key2, key3, key4", multiKey4.toString());
 assertEquals("key1, key2, key3, key4", multiKey5.toString());
 }

 @Test
 public void testMultiKeyGetKeys() {
 MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
 String[] keys = multiKey.getKeys();
 assertArrayEquals(new String[]{"key1", "key2"}, keys);
 }

 @Test
 public void testMultiKeyGetKey() {
 MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
 assertEquals("key1", multiKey.getKey(0));
 assertEquals("key2", multiKey.getKey(1));
 }

 @Test
 public void testMultiKeySize() {
 MultiKey<String> multiKey = new MultiKey<>("key1", "key2");
 assertEquals(2, multiKey.size());
 }

 @Test
 public void testMultiKeyEquals() {
 MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
 MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");
 MultiKey<String> multiKey3 = new MultiKey<>("key1", "key3");
 MultiKey<String> multiKey4 = new MultiKey<>("key3", "key2");
 Object obj = new Object();

 assertEquals(multiKey1, multiKey1);
 assertEquals(multiKey1, multiKey2);
 assertNotEquals(multiKey1, multiKey3);
 assertNotEquals(multiKey1, multiKey4);
 assertNotEquals(multiKey1, obj);
 }

 @Test
 public void testMultiKeyHashCode() {
 MultiKey<String> multiKey1 = new MultiKey<>("key1", "key2");
 MultiKey<String> multiKey2 = new MultiKey<>("key1", "key2");
 MultiKey<String> multiKey3 = new MultiKey<>("key1", "key3");
 MultiKey<String> multiKey4 = new MultiKey<>("key3", "key2");
 Object obj = new Object();

 assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
 assertNotEquals(multiKey1.hashCode(), multiKey3.hashCode());
 assertNotEquals(multiKey1.hashCode(), multiKey4.hashCode());
 assertNotEquals(multiKey1.hashCode(), obj.hashCode());
 }
}