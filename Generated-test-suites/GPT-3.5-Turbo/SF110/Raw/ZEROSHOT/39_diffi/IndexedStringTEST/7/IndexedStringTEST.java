package de.beiri22.stringincrementor.helper;

import org.junit.Assert;
import org.junit.Test;

public class IndexedStringTest {

   @Test
   public void testCount() {
      IndexedString indexedString = new IndexedString("hello");
      int result = indexedString.count('l');
      Assert.assertEquals(2, result);
   }

   @Test
   public void testCountNoMatch() {
      IndexedString indexedString = new IndexedString("hello");
      int result = indexedString.count('z');
      Assert.assertEquals(0, result);
   }

   @Test
   public void testCountEmptyString() {
      IndexedString indexedString = new IndexedString("");
      int result = indexedString.count('a');
      Assert.assertEquals(0, result);
   }

   @Test
   public void testCountRepeatedCharacter() {
      IndexedString indexedString = new IndexedString("hello");
      int result = indexedString.count('o');
      Assert.assertEquals(1, result);
   }

   @Test
   public void testIndexOf() {
      IndexedString indexedString = new IndexedString("hello");
      int result = indexedString.indexOf(new char[]{'l', 'o'});
      Assert.assertEquals(2, result);
   }

   @Test
   public void testIndexOfNoMatch() {
      IndexedString indexedString = new IndexedString("hello");
      int result = indexedString.indexOf(new char[]{'z', 'o'});
      Assert.assertEquals(-1, result);
   }

   @Test
   public void testIndexOfEmptyString() {
      IndexedString indexedString = new IndexedString("");
      int result = indexedString.indexOf(new char[]{'a'});
      Assert.assertEquals(-1, result);
   }

   @Test
   public void testIndexOfMultipleMatches() {
      IndexedString indexedString = new IndexedString("hello");
      int result = indexedString.indexOf(new char[]{'l'});
      Assert.assertEquals(2, result);
   }
}