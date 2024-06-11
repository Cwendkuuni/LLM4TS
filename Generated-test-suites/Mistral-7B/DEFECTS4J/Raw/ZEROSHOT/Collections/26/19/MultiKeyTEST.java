import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

 @Test
 public void testMultiKeyConstructionWithTwoKeys() {
 MultiKey<String> multiKey1 = new MultiKey<>("key1", "locale1");
 MultiKey<String> multiKey2 = new MultiKey<>("key1", "locale2");
 assertEquals(multiKey1.getKeys(), multiKey2.getKeys());
 }

 @Test
 public void testMultiKeyConstructionWithThreeKeys() {
 MultiKey<String> multiKey1 = new MultiKey<>("key1", "locale1", "locale2");
 MultiKey<String> multiKey2 = new MultiKey<>("key1", "locale1", "locale3");
 assertNotEquals(multiKey1.getKeys(), multiKey2.getKeys());
 }

 @Test
 public void testMultiKeyConstructionWithFourKeys() {
 MultiKey<String> multiKey1 = new MultiKey<>("key1", "locale1", "locale2", "locale3");
 MultiKey<String> multiKey2 = new MultiKey<>("key1", "locale1", "locale2", "locale4");
 assertNotEquals(multiKey1.getKeys(), multiKey2.getKeys());
 }

 @Test
 public void testMultiKeyConstructionWithFiveKeys() {
 MultiKey<String> multiKey1 = new MultiKey<>("key1", "locale1", "locale2", "locale3", "locale4");
 MultiKey<String> multiKey2 = new MultiKey<>("key1", "locale1", "locale2", "locale5", "locale6");
 assertNotEquals(multiKey1.getKeys(), multiKey2.getKeys());
 }

 @Test
 public void testMultiKeyConstructionWithArrayOfKeys() {
 String[] keys = new String[]{"key1", "locale1", "locale2"};
 MultiKey<String> multiKey1 = new MultiKey<>(keys);
 MultiKey<String> multiKey2 = new MultiKey<>(keys.clone());
 assertEquals(multiKey1.getKeys(), multiKey2.getKeys());
 }

 @Test
 public void testMultiKeyEquals() {
 MultiKey<String> multiKey1 = new MultiKey<>("key1", "locale1", "locale2");
 MultiKey<String> multiKey2 = new MultiKey<>("key1", "locale1", "locale2");
 MultiKey<String> multiKey3 = new MultiKey<>("key1", "locale1", "locale3");
 MultiKey<String> multiKey4 = new MultiKey<>("key1", "locale2", "locale2");
 assertTrue(multiKey1.equals(multiKey1));
 assertTrue(multiKey1.equals(multiKey2));
 assertFalse(multiKey1.equals(multiKey3));
 assertFalse(multiKey1.equals(multiKey4));
 assertFalse(multiKey1.equals(new Object()));
 }

 @Test
 public void testMultiKeyHashCode() {
 MultiKey<String> multiKey1 = new MultiKey<>("key1", "locale1", "locale2");
 MultiKey<String> multiKey2 = new MultiKey<>("key1", "locale1", "locale2");
 MultiKey<String> multiKey3 = new MultiKey<>("key1", "locale1", "locale3");
 assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
 assertNotEquals(multiKey1.hashCode(), multiKey3.hashCode());
 }

 @Test
 public void testMultiKeyToString() {
 MultiKey<String> multiKey = new MultiKey<>("key1", "locale1", "locale2");
 assertEquals("MultiKey[key1, locale1, locale2]", multiKey.toString());
 }
}