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
 MultiKey<String> key1_3 = new MultiKey<>("key1", "locale1", "locale3");
 MultiKey<String> key2_3 = new MultiKey<>("key2", "locale2", "locale3");
 MultiKey<String[]> keyArray = new MultiKey<String[]>(new String[]{"key1", "locale1"});

 assertEquals(key1_1, key1_2);
 assertNotSame(key1_1, key2_1);
 assertNotSame(key1_1, key1_3);
 assertNotSame(key1_1, keyArray);
 assertArrayEquals(new String[]{"key1", "locale1"}, key1_1.getKeys());
 assertArrayEquals(new String[]{"key2", "locale2"}, key2_1.getKeys());
 assertArrayEquals(new String[]{"key1", "locale1", "locale3"}, key1_3.getKeys());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMultiKeyNullKeys() {
 new MultiKey<String>(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testMultiKeyNullArray() {
 new MultiKey<String>(null);
 }

 @Test
 public void testMultiKeySize() {
 MultiKey<String> key1 = new MultiKey<>("key1", "locale1");
 assertEquals(1, key1.size());
 MultiKey<String> key2 = new MultiKey<>("key2", "locale2");
 assertEquals(1, key2.size());
 MultiKey<String> key3 = new MultiKey<>("key1", "locale1", "locale3");
 assertEquals(2, key3.size());
 }

 @Test
 public void testMultiKeyToString() {
 MultiKey<String> key1 = new MultiKey<>("key1", "locale1");
 assertEquals("MultiKey[key1, locale1]", key1.toString());
 }

}