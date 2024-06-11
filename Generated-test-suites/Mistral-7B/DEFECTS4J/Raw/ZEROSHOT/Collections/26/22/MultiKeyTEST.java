import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

 @Test
 public void testMultiKeyConstruction() {
 MultiKey<String> key1_1 = new MultiKey<>("key1", "locale1");
 MultiKey<String> key1_2 = new MultiKey<>("key1", "locale1");
 MultiKey<String> key2_1 = new MultiKey<>("key2", "locale2");
 MultiKey<String> key2_2 = new MultiKey<>("key2", "locale2");
 MultiKey<String> key3_1 = new MultiKey<>("key3", "locale3");
 MultiKey<String> key3_2 = new MultiKey<>("key3", "locale3");
 MultiKey<String[]> keyArray1 = new MultiKey(new String[]{"key1", "locale1"});
 MultiKey<String[]> keyArray2 = new MultiKey(new String[]{"key1", "locale1"}, false);
 MultiKey<String[]> keyArray3 = new MultiKey(new String[]{"key2", "locale2", "locale2"}, true);

 assertEquals(key1_1, key1_2);
 assertEquals(key1_1.hashCode(), key1_2.hashCode());
 assertEquals(key2_1, key2_2);
 assertEquals(key2_1.hashCode(), key2_2.hashCode());
 assertEquals(key3_1, key3_2);
 assertEquals(key3_1.hashCode(), key3_2.hashCode());
 assertEquals(keyArray1, keyArray1.getKeys());
 assertEquals(keyArray2, keyArray2.getKeys());
 assertEquals(keyArray3, keyArray3.getKeys());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMultiKeyNullKeys() {
 MultiKey<String> key1_null = new MultiKey<String>(null, "locale1");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMultiKeyNullLocale() {
 MultiKey<String> key1_nullLocale = new MultiKey<>("key1", null);
 }

 @Test
 public void testMultiKeySize() {
 MultiKey<String> multiKey1 = new MultiKey<>("key1", "locale1");
 MultiKey<String> multiKey2 = new MultiKey<>("key1", "locale1", "locale2");
 MultiKey<String> multiKey3 = new MultiKey<>("key1", "locale1", "locale2", "locale3");
 MultiKey<String> multiKey4 = new MultiKey<>("key1", "locale1", "locale2", "locale3", "locale4");

 assertEquals(multiKey1.size(), 1);
 assertEquals(multiKey2.size(), 2);
 assertEquals(multiKey3.size(), 3);
 assertEquals(multiKey4.size(), 4);
 }

 @Test
 public void testMultiKeyGetKey() {
 MultiKey<String> multiKey1 = new MultiKey<>("key1", "locale1");
 MultiKey<String> multiKey2 = new MultiKey<>("key1", "locale1", "locale2");
 MultiKey<String> multiKey3 = new MultiKey<>("key1", "locale1", "locale2", "locale3");
 MultiKey<String> multiKey4 = new MultiKey<>("key1", "locale1", "locale2", "locale3", "locale4");

 assertEquals(multiKey1.getKey(0), "key1");
 assertEquals(multiKey1.getKey(1), "locale1");
 assertEquals(multiKey2.getKey(0), "key1");
 assertEquals(multiKey2.getKey(1), "locale1");
 assertEquals(multiKey2.getKey(2), "locale2");
 assertEquals(multiKey3.getKey(0), "key1");
 assertEquals(multiKey3.getKey(1), "locale1");
 assertEquals(multiKey3.getKey(2), "locale2");
 assertEquals(multiKey3.getKey(3), "locale3");
 assertEquals(multiKey4.getKey(0), "key1");
 assertEquals(multiKey4.getKey(1), "locale1");
 assertEquals(multiKey4.getKey(2), "locale2");
 assertEquals(multiKey4.getKey(3), "locale3");
 assertEquals(multiKey4.getKey(4), "locale4");
 }

 @Test
 public void testMultiKeyHashCode() {
 MultiKey<String> multiKey1 = new MultiKey<>("key1", "locale1");
 MultiKey<String> multiKey2 = new MultiKey<>("key1", "locale1");
 MultiKey<String> multiKey3 = new MultiKey<>("key1", "locale1", "locale2");
 MultiKey<String> multiKey4 = new MultiKey<>("key1", "locale1", "locale2", "locale3");
 MultiKey<String> multiKey5 = new MultiKey<>("key1", "locale1", "locale2", "locale3", "locale4");

 assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
 assertNotEquals(multiKey1.hashCode(), multiKey3.hashCode());
 assertNotEquals(multiKey1.hashCode(), multiKey4.hashCode());
 assertNotEquals(multiKey1.hashCode(), multiKey5.hashCode());
 }

 @Test
 public void testMultiKeyEquals() {
 MultiKey<String> multiKey1 = new MultiKey<>("key1", "locale1");
 MultiKey<String> multiKey2 = new MultiKey<>("key1", "locale1");
 MultiKey<String> multiKey3 = new MultiKey<>("key1", "locale1");
 MultiKey<String> multiKey4 = new MultiKey<>("key1", "locale2");
 MultiKey<String> multiKey5 = new MultiKey<>("key1", "locale1", "locale2");
 MultiKey<String> multiKey6 = new MultiKey<>("key2", "locale1");
 MultiKey<String> multiKey7 = new MultiKey<>("key1", "locale1", new String[]{"locale2", "locale3"});

 assertTrue(multiKey1.equals(multiKey2));
 assertTrue(multiKey1.equals(multiKey3));
 assertFalse(multiKey1.equals(multiKey4));
 assertFalse(multiKey1.equals(multiKey5));
 assertFalse(multiKey1.equals(multiKey6));
 assertFalse(multiKey1.equals(multiKey7));
 }

 @Test
 public void testMultiKeyToString() {
 MultiKey<String> multiKey1 = new MultiKey<>("key1", "locale1");
 MultiKey<String> multiKey2 = new MultiKey<>("key1", "locale1", "locale2");
 MultiKey<String> multiKey3 = new MultiKey<>("key1", "locale1", "locale2", "locale3");
 MultiKey<String> multiKey4 = new MultiKey<>("key1", "locale1", "locale2", "locale3", "locale4");

 assertEquals(multiKey1.toString(), "MultiKey[key1, locale1]");
 assertEquals(multiKey2.toString(), "MultiKey[key1, locale1, locale2]");
 assertEquals(multiKey3.toString(), "MultiKey[key1, locale1, locale2, locale3]");
 assertEquals(multiKey4.toString(), "MultiKey[key1, locale1, locale2, locale3, locale4]");
 }
}