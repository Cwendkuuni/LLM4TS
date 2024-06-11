import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class MultiKeyTest {

   @Test
   public void testConstructor_twoKeys() {
      String key1 = "key1";
      String key2 = "key2";
      
      MultiKey<String> multiKey = new MultiKey<>(key1, key2);
      
      assertEquals(key1, multiKey.getKey(0));
      assertEquals(key2, multiKey.getKey(1));
      assertEquals(2, multiKey.size());
   }
   
   @Test
   public void testConstructor_threeKeys() {
      String key1 = "key1";
      String key2 = "key2";
      String key3 = "key3";
      
      MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3);
      
      assertEquals(key1, multiKey.getKey(0));
      assertEquals(key2, multiKey.getKey(1));
      assertEquals(key3, multiKey.getKey(2));
      assertEquals(3, multiKey.size());
   }
   
   @Test
   public void testConstructor_fourKeys() {
      String key1 = "key1";
      String key2 = "key2";
      String key3 = "key3";
      String key4 = "key4";
      
      MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4);
      
      assertEquals(key1, multiKey.getKey(0));
      assertEquals(key2, multiKey.getKey(1));
      assertEquals(key3, multiKey.getKey(2));
      assertEquals(key4, multiKey.getKey(3));
      assertEquals(4, multiKey.size());
   }
   
   @Test
   public void testConstructor_fiveKeys() {
      String key1 = "key1";
      String key2 = "key2";
      String key3 = "key3";
      String key4 = "key4";
      String key5 = "key5";
      
      MultiKey<String> multiKey = new MultiKey<>(key1, key2, key3, key4, key5);
      
      assertEquals(key1, multiKey.getKey(0));
      assertEquals(key2, multiKey.getKey(1));
      assertEquals(key3, multiKey.getKey(2));
      assertEquals(key4, multiKey.getKey(3));
      assertEquals(key5, multiKey.getKey(4));
      assertEquals(5, multiKey.size());
   }
   
   @Test
   public void testConstructor_arrayKeys() {
      String key1 = "key1";
      String key2 = "key2";
      String key3 = "key3";
      String key4 = "key4";
      
      String[] keys = {key1, key2, key3, key4};
      
      MultiKey<String> multiKey = new MultiKey<>(keys);
      
      assertEquals(key1, multiKey.getKey(0));
      assertEquals(key2, multiKey.getKey(1));
      assertEquals(key3, multiKey.getKey(2));
      assertEquals(key4, multiKey.getKey(3));
      assertEquals(4, multiKey.size());
   }
   
   @Test
   public void testEquals_sameObject() {
      String key1 = "key1";
      String key2 = "key2";
      
      MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
      MultiKey<String> multiKey2 = multiKey1;
      
      assertTrue(multiKey1.equals(multiKey2));
   }
   
   @Test
   public void testEquals_differentClass() {
      String key1 = "key1";
      String key2 = "key2";
      
      MultiKey<String> multiKey = new MultiKey<>(key1, key2);
      Object obj = new Object();
      
      assertFalse(multiKey.equals(obj));
   }
   
   @Test
   public void testEquals_sameKeys() {
      String key1 = "key1";
      String key2 = "key2";
      
      MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
      MultiKey<String> multiKey2 = new MultiKey<>(key1, key2);
      
      assertTrue(multiKey1.equals(multiKey2));
   }
   
   @Test
   public void testEquals_differentKeys() {
      String key1 = "key1";
      String key2 = "key2";
      
      MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
      MultiKey<String> multiKey2 = new MultiKey<>(key2, key1);
      
      assertFalse(multiKey1.equals(multiKey2));
   }
   
   @Test
   public void testHashCode_sameKeys() {
      String key1 = "key1";
      String key2 = "key2";
      
      MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
      MultiKey<String> multiKey2 = new MultiKey<>(key1, key2);
      
      assertTrue(multiKey1.hashCode() == multiKey2.hashCode());
   }
   
   @Test
   public void testHashCode_differentKeys() {
      String key1 = "key1";
      String key2 = "key2";
      
      MultiKey<String> multiKey1 = new MultiKey<>(key1, key2);
      MultiKey<String> multiKey2 = new MultiKey<>(key2, key1);
      
      assertFalse(multiKey1.hashCode() == multiKey2.hashCode());
   }
   
   @Test
   public void testToString() {
      String key1 = "key1";
      String key2 = "key2";
      
      MultiKey<String> multiKey = new MultiKey<>(key1, key2);
      
      String expected = "MultiKey[key1, key2]";
      assertEquals(expected, multiKey.toString());
   }
}