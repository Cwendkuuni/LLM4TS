import org.apache.commons.collections4.keyvalue.MultiKey;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

 private K key1;
 private K key2;
 private K key3;
 private K key4;
 private K key5;

 @Before
 public void setup() {
 // Replace this with the actual initialization of your test keys
 key1 = "key1";
 key2 = "key2";
 key3 = "key3";
 key4 = "key4";
 key5 = "key5";
 }

 @Test
 public void testMultiKeyWithTwoKeys() {
 MultiKey<K> multiKey = new MultiKey<>(key1, key2);
 assertEquals(2, multiKey.size());
 assertEquals(key1, multiKey.getKey(0));
 assertEquals(key2, multiKey.getKey(1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullKeyArray() {
 try {
 new MultiKey<>(null);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyKeyArray() {
 try {
 new MultiKey<>(new K[0]);
 fail("Expected IllegalArgumentException");
 } catch (IllegalArgumentException e) {
 // pass
 }
 }

 @Test
 public void testMultiKeyWithThreeKeys() {
 MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);
 assertEquals(3, multiKey.size());
 assertEquals(key1, multiKey.getKey(0));
 assertEquals(key2, multiKey.getKey(1));
 assertEquals(key3, multiKey.getKey(2));
 }

 @Test
 public void testMultiKeyWithFourKeys() {
 MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4);
 assertEquals(4, multiKey.size());
 assertEquals(key1, multiKey.getKey(0));
 assertEquals(key2, multiKey.getKey(1));
 assertEquals(key3, multiKey.getKey(2));
 assertEquals(key4, multiKey.getKey(3));
 }

 @Test
 public void testMultiKeyWithFiveKeys() {
 MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
 assertEquals(5, multiKey.size());
 assertEquals(key1, multiKey.getKey(0));
 assertEquals(key2, multiKey.getKey(1));
 assertEquals(key3, multiKey.getKey(2));
 assertEquals(key4, multiKey.getKey(3));
 assertEquals(key5, multiKey.getKey(4));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetKeyOutOfBounds() {
 MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);
 try {
 multiKey.getKey(-1);
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // pass
 }
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetKeyWithSize() {
 MultiKey<K> multiKey = new MultiKey<>(key1, key2, key3);
 try {
 multiKey.getKey(multiKey.size());
 fail("Expected IndexOutOfBoundsException");
 } catch (IndexOutOfBoundsException e) {
 // pass
 }
 }
}