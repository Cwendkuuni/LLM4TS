package org.apache.commons.lang3;
import org.junit.Test;

import static org.junit.Assert.*;

public class RandomStringUtilsTest {

   @Test
   public void testRandom() {
       String result = RandomStringUtils.random(10);
       assertNotNull(result);
       assertEquals(10, result.length());
       assertNotEquals("", result);
   }

   @Test
   public void testRandomAscii() {
       String result = RandomStringUtils.randomAscii(10);
       assertNotNull(result);
       assertEquals(10, result.length());
       assertNotEquals("", result);
       // Check that all characters are within the ASCII range
       for(int i = 0; i < result.length(); i++) {
           char ch = result.charAt(i);
           assertTrue(ch >= 32 && ch <= 127);
       }
   }
   
   @Test
   public void testRandomAlphabetic() {
       String result = RandomStringUtils.randomAlphabetic(10);
       assertNotNull(result);
       assertEquals(10, result.length());
       assertNotEquals("", result);
       // Check that all characters are alphabetic
       for(int i = 0; i < result.length(); i++) {
           char ch = result.charAt(i);
           assertTrue(Character.isLetter(ch));
       }
   }
   
   @Test
   public void testRandomAlphanumeric() {
       String result = RandomStringUtils.randomAlphanumeric(10);
       assertNotNull(result);
       assertEquals(10, result.length());
       assertNotEquals("", result);
       // Check that all characters are either alphabetic or numeric
       for(int i = 0; i < result.length(); i++) {
           char ch = result.charAt(i);
           assertTrue(Character.isLetterOrDigit(ch));
       }
   }
   
   @Test
   public void testRandomNumeric() {
       String result = RandomStringUtils.randomNumeric(10);
       assertNotNull(result);
       assertEquals(10, result.length());
       assertNotEquals("", result);
       // Check that all characters are numeric
       for(int i = 0; i < result.length(); i++) {
           char ch = result.charAt(i);
           assertTrue(Character.isDigit(ch));
       }
   }

   @Test
   public void testRandomWithNegativeCount() {
       try {
           String result = RandomStringUtils.random(-5);
           fail("Expected IllegalArgumentException to be thrown");
       } catch (IllegalArgumentException e) {
           // Exception expected, do nothing
       }
   }

   @Test
   public void testRandomWithEmptyCharsArray() {
       try {
           char[] chars = new char[0];
           String result = RandomStringUtils.random(10, chars);
           fail("Expected IllegalArgumentException to be thrown");
       } catch (IllegalArgumentException e) {
           // Exception expected, do nothing
       }
   }

   @Test
   public void testRandomWithStartAndEndOutOfRange() {
       try {
           String result = RandomStringUtils.random(10, 100, 200, false, true);
           fail("Expected ArrayIndexOutOfBoundsException to be thrown");
       } catch (ArrayIndexOutOfBoundsException e) {
           // Exception expected, do nothing
       }
   }

   @Test
   public void testRandomWithInvalidChars() {
       char[] chars = {'a', '1', '$', '!', '@'};
       String result = RandomStringUtils.random(10, chars);
       assertNotNull(result);
       assertEquals(10, result.length());
       assertNotEquals("", result);
       // Check that all characters are contained in the specified chars array
       for(int i = 0; i < result.length(); i++) {
           char ch = result.charAt(i);
           assertTrue(charsContains(chars, ch));
       }
   }

   private boolean charsContains(char[] chars, char ch) {
       for(int i = 0; i < chars.length; i++) {
           if(chars[i] == ch) {
               return true;
           }
       }
       return false;
   }
}