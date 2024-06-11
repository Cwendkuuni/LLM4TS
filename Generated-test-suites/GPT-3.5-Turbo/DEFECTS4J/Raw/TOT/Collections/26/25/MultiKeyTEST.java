import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

   private MultiKey<String> multiKey;

   @Test
   public void testConstructorTwoKeys() {
       String key1 = "key1";
       String key2 = "key2";
       multiKey = new MultiKey<>(key1, key2);
       assertEquals(key1, multiKey.getKey(0));
       assertEquals(key2, multiKey.getKey(1));
   }

   @Test
   public void testConstructorThreeKeys() {
       String key1 = "key1";
       String key2 = "key2";
       String key3 = "key3";
       multiKey = new MultiKey<>(key1, key2, key3);
       assertEquals(key1, multiKey.getKey(0));
       assertEquals(key2, multiKey.getKey(1));
       assertEquals(key3, multiKey.getKey(2));
   }

   @Test
   public void testConstructorFourKeys() {
       String key1 = "key1";
       String key2 = "key2";
       String key3 = "key3";
       String key4 = "key4";
       multiKey = new MultiKey<>(key1, key2, key3, key4);
       assertEquals(key1, multiKey.getKey(0));
       assertEquals(key2, multiKey.getKey(1));
       assertEquals(key3, multiKey.getKey(2));
       assertEquals(key4, multiKey.getKey(3));
   }

   @Test
   public void testConstructorFiveKeys() {
       String key1 = "key1";
       String key2 = "key2";
       String key3 = "key3";
       String key4 = "key4";
       String key5 = "key5";
       multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
       assertEquals(key1, multiKey.getKey(0));
       assertEquals(key2, multiKey.getKey(1));
       assertEquals(key3, multiKey.getKey(2));
       assertEquals(key4, multiKey.getKey(3));
       assertEquals(key5, multiKey.getKey(4));
   }

   @Test
   public void testConstructorKeyArray() {
       String key1 = "key1";
       String key2 = "key2";
       String key3 = "key3";
       String[] keys = {key1, key2, key3};
       multiKey = new MultiKey<>(keys);
       assertEquals(key1, multiKey.getKey(0));
       assertEquals(key2, multiKey.getKey(1));
       assertEquals(key3, multiKey.getKey(2));
   }

   @Test(expected = IllegalArgumentException.class)
   public void testConstructorNullKeys() {
       String[] keys = null;
       multiKey = new MultiKey<>(keys);
   }

   @Test
   public void testGetKeys() {
       String key1 = "key1";
       String key2 = "key2";
       String key3 = "key3";
       multiKey = new MultiKey<>(key1, key2, key3);
       String[] result = multiKey.getKeys();
       assertEquals(key1, result[0]);
       assertEquals(key2, result[1]);
       assertEquals(key3, result[2]);
   }

   @Test
   public void testGetSize() {
       String key1 = "key1";
       String key2 = "key2";
       String key3 = "key3";
       String key4 = "key4";
       multiKey = new MultiKey<>(key1, key2, key3, key4);
       int size = multiKey.size();
       assertEquals(4, size);
   }

   @Test
   public void testEqualsSameObject() {
       String key1 = "key1";
       String key2 = "key2";
       multiKey = new MultiKey<>(key1, key2);

       assertTrue(multiKey.equals(multiKey));
   }

   @Test
   public void testEqualsDifferentClass() {
       String key1 = "key1";
       String key2 = "key2";
       multiKey = new MultiKey<>(key1, key2);
       Object obj = new Object();

       assertFalse(multiKey.equals(obj));
   }

   @Test
   public void testEqualsDifferentKeys() {
       String key1 = "key1";
       String key2 = "key2";
       multiKey = new MultiKey<>(key1, key2);
       MultiKey<String> otherMultiKey = new MultiKey<>(key2, key1);

       assertFalse(multiKey.equals(otherMultiKey));
   }

   @Test
   public void testEqualsSameKeys() {
       String key1 = "key1";
       String key2 = "key2";
       multiKey = new MultiKey<>(key1, key2);
       MultiKey<String> otherMultiKey = new MultiKey<>(key1, key2);

       assertTrue(multiKey.equals(otherMultiKey));
   }

   @Test
   public void testHashCode() {
       String key1 = "key1";
       String key2 = "key2";
       multiKey = new MultiKey<>(key1, key2);
       int expectedHashCode = key1.hashCode() ^ key2.hashCode();

       assertEquals(expectedHashCode, multiKey.hashCode());
   }

   @Test
   public void testToString() {
       String key1 = "key1";
       String key2 = "key2";
       String key3 = "key3";
       multiKey = new MultiKey<>(key1, key2, key3);
       String expectedString = "MultiKey[key1, key2, key3]";

       assertEquals(expectedString, multiKey.toString());
   }

}