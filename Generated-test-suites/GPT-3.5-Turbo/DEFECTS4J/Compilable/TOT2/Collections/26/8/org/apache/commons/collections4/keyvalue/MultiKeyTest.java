package org.apache.commons.collections4.keyvalue;
import static org.junit.Assert.*;
import org.junit.Test;

public class MultiKeyTest {
  
  @Test
  public void testConstructorTwoKeys() {
      MultiKey<Integer> multiKey = new MultiKey<>(1, 2);
      Integer[] keys = multiKey.getKeys();
      assertEquals(2, keys.length);
      assertEquals(Integer.valueOf(1), keys[0]);
      assertEquals(Integer.valueOf(2), keys[1]);
  }
  
  @Test
  public void testConstructorThreeKeys() {
      MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3);
      Integer[] keys = multiKey.getKeys();
      assertEquals(3, keys.length);
      assertEquals(Integer.valueOf(1), keys[0]);
      assertEquals(Integer.valueOf(2), keys[1]);
      assertEquals(Integer.valueOf(3), keys[2]);
  }
  
  @Test
  public void testConstructorFourKeys() {
      MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3, 4);
      Integer[] keys = multiKey.getKeys();
      assertEquals(4, keys.length);
      assertEquals(Integer.valueOf(1), keys[0]);
      assertEquals(Integer.valueOf(2), keys[1]);
      assertEquals(Integer.valueOf(3), keys[2]);
      assertEquals(Integer.valueOf(4), keys[3]);
  }
  
  @Test
  public void testConstructorFiveKeys() {
      MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3, 4, 5);
      Integer[] keys = multiKey.getKeys();
      assertEquals(5, keys.length);
      assertEquals(Integer.valueOf(1), keys[0]);
      assertEquals(Integer.valueOf(2), keys[1]);
      assertEquals(Integer.valueOf(3), keys[2]);
      assertEquals(Integer.valueOf(4), keys[3]);
      assertEquals(Integer.valueOf(5), keys[4]);
  }
  
  @Test
  public void testConstructorArrayKeys() {
      Integer[] arrayKeys = {1, 2, 3, 4, 5};
      MultiKey<Integer> multiKey = new MultiKey<>(arrayKeys);
      Integer[] keys = multiKey.getKeys();
      assertArrayEquals(arrayKeys, keys);
  }
  
  @Test
  public void testGetKey() {
      MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3, 4, 5);
      assertEquals(Integer.valueOf(2), multiKey.getKey(1));
  }
  
  @Test(expected = IndexOutOfBoundsException.class)
  public void testGetInvalidKeyIndex() {
      MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3);
      multiKey.getKey(3);
  }
  
  @Test
  public void testHashCode() {
      MultiKey<Integer> multiKey1 = new MultiKey<>(1, 2, 3);
      MultiKey<Integer> multiKey2 = new MultiKey<>(1, 2, 3);
      assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
  }
  
  @Test
  public void testEquals() {
      MultiKey<Integer> multiKey1 = new MultiKey<>(1, 2, 3);
      MultiKey<Integer> multiKey2 = new MultiKey<>(1, 2, 3);
      assertTrue(multiKey1.equals(multiKey2));
  }
  
  @Test
  public void testToString() {
      MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3);
      assertEquals("MultiKey[1, 2, 3]", multiKey.toString());
  }
}