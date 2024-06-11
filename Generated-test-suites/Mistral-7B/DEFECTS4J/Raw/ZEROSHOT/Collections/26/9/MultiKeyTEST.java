import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

 @Test
 public void testMultiKeyConstruction() {
 MultiKey<String> key1_1 = new MultiKey("key1", "locale1");
 MultiKey<String> key1_2 = new MultiKey("key1", "locale2");
 MultiKey<String> key2_1 = new MultiKey("key2", "locale1");
 MultiKey<String> key2_2 = new MultiKey("key2", "locale2");
 MultiKey<String> key3 = new MultiKey(new String[]{"key3", "locale1"});
 MultiKey<String> key4 = new MultiKey(new String[]{"key4", "locale1"}, false);

 assertEquals("key1,locale1", key1_1.toString());
 assertEquals("key1,locale2", key1_2.toString());
 assertEquals("key2,locale1", key2_1.toString());
 assertEquals("key2,locale2", key2_2.toString());
 assertEquals("key3,locale1", key3.toString());
 assertEquals("key4,locale1", key4.toString());
 }

 @Test
 public void testMultiKeyGetters() {
 MultiKey<String> key = new MultiKey("key1", "locale1");

 assertEquals(new String[]{"key1", "locale1"}, key.getKeys());
 assertEquals("locale1", key.getKey(0));
 assertEquals("key1", key.getKey(0));
 assertEquals(2, key.size());
 }

 @Test
 public void testMultiKeyEqualsHashCode() {
 MultiKey<String> key1 = new MultiKey("key1", "locale1");
 MultiKey<String> key2 = new MultiKey("key1", "locale1");
 MultiKey<String> key3 = new MultiKey("key2", "locale2");

 assertEquals(key1, key1);
 assertEquals(key1, key1);
 assertEquals(key1, key1);

 assertEquals(key1, key2);
 assertNotEquals(key1, key3);

 assertTrue(key1.equals(key1));
 assertTrue(key1.equals(key2));
 assertFalse(key1.equals(null));
 assertFalse(key1.equals(""));
 assertFalse(key1.equals("test"));

 assertEquals(key1.hashCode(), key2.hashCode());
 assertNotEquals(key1.hashCode(), key3.hashCode());
 }
}