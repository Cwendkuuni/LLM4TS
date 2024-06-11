package org.apache.commons.lang3;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

   @Test
   public void testRandom() {
      // Test when count is 0
      assertEquals("", RandomStringUtils.random(0));

      // Test when count is negative
      try {
         RandomStringUtils.random(-5);
         fail("Expected an IllegalArgumentException to be thrown");
      } catch (IllegalArgumentException e) {
         assertEquals("Requested random string length -5 is less than 0.", e.getMessage());
      }

      // Test randomly generated string length
      int count = 10;
      String randomString = RandomStringUtils.random(count);
      assertEquals(count, randomString.length());
   }

   @Test
   public void testRandomAscii() {
      // Test when count is 0
      assertEquals("", RandomStringUtils.randomAscii(0));

      // Test randomly generated string length
      int count = 10;
      String randomString = RandomStringUtils.randomAscii(count);
      assertEquals(count, randomString.length());

      // Test characters in the generated string are within ASCII range
      for (char c : randomString.toCharArray()) {
         assertTrue((int) c >= 32 && (int) c <= 127);
      }
   }

   @Test
   public void testRandomAlphabetic() {
      // Test when count is 0
      assertEquals("", RandomStringUtils.randomAlphabetic(0));

      // Test randomly generated string length
      int count = 10;
      String randomString = RandomStringUtils.randomAlphabetic(count);
      assertEquals(count, randomString.length());

      // Test characters in the generated string are alphabetic
      assertTrue(randomString.matches("[a-zA-Z]+"));
   }

   @Test
   public void testRandomAlphanumeric() {
      // Test when count is 0
      assertEquals("", RandomStringUtils.randomAlphanumeric(0));

      // Test randomly generated string length
      int count = 10;
      String randomString = RandomStringUtils.randomAlphanumeric(count);
      assertEquals(count, randomString.length());

      // Test characters in the generated string are alphanumeric
      assertTrue(randomString.matches("[a-zA-Z0-9]+"));
   }

   @Test
   public void testRandomNumeric() {
      // Test when count is 0
      assertEquals("", RandomStringUtils.randomNumeric(0));

      // Test randomly generated string length
      int count = 10;
      String randomString = RandomStringUtils.randomNumeric(count);
      assertEquals(count, randomString.length());

      // Test characters in the generated string are numeric
      assertTrue(randomString.matches("[0-9]+"));
   }

   @Test
   public void testRandomWithLettersAndNumbers() {
      // Test when count is 0
      assertEquals("", RandomStringUtils.random(0, true, true));

      // Test randomly generated string length
      int count = 10;
      String randomString = RandomStringUtils.random(count, true, true);
      assertEquals(count, randomString.length());

      // Test characters in the generated string are alphanumeric
      assertTrue(randomString.matches("[a-zA-Z0-9]+"));
   }

   @Test
   public void testRandomWithStartAndEnd() {
      // Test when count is 0
      assertEquals("", RandomStringUtils.random(0, 65, 90, true, false));

      // Test randomly generated string length
      int count = 10;
      String randomString = RandomStringUtils.random(count, 65, 90, true, false);
      assertEquals(count, randomString.length());

      // Test characters in the generated string are within the specified range
      for (char c : randomString.toCharArray()) {
         assertTrue((int) c >= 65 && (int) c <= 90);
      }
   }

   @Test
   public void testRandomWithProvidedChars() {
      // Test when count is 0
      assertEquals("", RandomStringUtils.random(0, new char[]{'a', 'b', 'c'}));

      // Test randomly generated string length
      int count = 10;
      String randomString = RandomStringUtils.random(count, new char[]{'a', 'b', 'c'});
      assertEquals(count, randomString.length());

      // Test characters in the generated string are within the provided chars array
      for (char c : randomString.toCharArray()) {
         assertTrue(c == 'a' || c == 'b' || c == 'c');
      }
   }
}