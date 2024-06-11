package org.apache.commons.collections4.keyvalue;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiKeyTest {

 @Test
 public void testConstructorTwoKeys() {
   MultiKey<Integer> multiKey = new MultiKey<>(1, 2);
   Integer[] expectedKeys = { 1, 2 };
   assertArrayEquals(expectedKeys, multiKey.getKeys());
 }

 @Test
 public void testConstructorThreeKeys() {
   MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3);
   Integer[] expectedKeys = { 1, 2, 3 };
   assertArrayEquals(expectedKeys, multiKey.getKeys());
 }

 @Test
 public void testConstructorFourKeys() {
   MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3, 4);
   Integer[] expectedKeys = { 1, 2, 3, 4 };
   assertArrayEquals(expectedKeys, multiKey.getKeys());
 }

 @Test
 public void testConstructorFiveKeys() {
   MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3, 4, 5);
   Integer[] expectedKeys = { 1, 2, 3, 4, 5 };
   assertArrayEquals(expectedKeys, multiKey.getKeys());
 }

 @Test
 public void testConstructorArrayKeys() {
   Integer[] keys = { 1, 2, 3 };
   MultiKey<Integer> multiKey = new MultiKey<>(keys);
   Integer[] expectedKeys = { 1, 2, 3 };
   assertArrayEquals(expectedKeys, multiKey.getKeys());
 }

 @Test
 public void testGetKey() {
   MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3);
   assertEquals(Integer.valueOf(1), multiKey.getKey(0));
   assertEquals(Integer.valueOf(2), multiKey.getKey(1));
   assertEquals(Integer.valueOf(3), multiKey.getKey(2));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetKeyOutOfBounds() {
   MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3);
   multiKey.getKey(3);
 }

 @Test
 public void testSize() {
   MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3);
   assertEquals(3, multiKey.size());
 }

 @Test
 public void testEqualsSameObject() {
   MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3);
   assertTrue(multiKey.equals(multiKey));
 }

 @Test
 public void testEqualsDifferentClass() {
   MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3);
   assertFalse(multiKey.equals("Hello"));
 }

 @Test
 public void testEqualsSameKeys() {
   MultiKey<Integer> multiKey1 = new MultiKey<>(1, 2, 3);
   MultiKey<Integer> multiKey2 = new MultiKey<>(1, 2, 3);
   assertTrue(multiKey1.equals(multiKey2));
 }

 @Test
 public void testEqualsDifferentKeys() {
   MultiKey<Integer> multiKey1 = new MultiKey<>(1, 2, 3);
   MultiKey<Integer> multiKey2 = new MultiKey<>(2, 3, 4);
   assertFalse(multiKey1.equals(multiKey2));
 }

 @Test
 public void testHashCode() {
   MultiKey<Integer> multiKey1 = new MultiKey<>(1, 2, 3);
   MultiKey<Integer> multiKey2 = new MultiKey<>(1, 2, 3);
   assertEquals(multiKey1.hashCode(), multiKey2.hashCode());
 }

 @Test
 public void testToString() {
   MultiKey<Integer> multiKey = new MultiKey<>(1, 2, 3);
   assertEquals("MultiKey[1, 2, 3]", multiKey.toString());
 }

}