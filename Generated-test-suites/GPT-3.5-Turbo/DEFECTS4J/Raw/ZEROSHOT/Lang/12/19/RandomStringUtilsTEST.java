import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

   @Test
   public void testRandom() {
      String result = RandomStringUtils.random(5);
      assertNotNull(result);
      assertEquals(5, result.length());
   }

   @Test
   public void testRandomAscii() {
      String result = RandomStringUtils.randomAscii(5);
      assertNotNull(result);
      assertEquals(5, result.length());
   }

   @Test
   public void testRandomAlphabetic() {
      String result = RandomStringUtils.randomAlphabetic(5);
      assertNotNull(result);
      assertEquals(5, result.length());
   }

   @Test
   public void testRandomAlphanumeric() {
      String result = RandomStringUtils.randomAlphanumeric(5);
      assertNotNull(result);
      assertEquals(5, result.length());
   }

   @Test
   public void testRandomNumeric() {
      String result = RandomStringUtils.randomNumeric(5);
      assertNotNull(result);
      assertEquals(5, result.length());
   }

   @Test
   public void testRandomWithLettersAndNumbers() {
      String result = RandomStringUtils.random(5, true, true);
      assertNotNull(result);
      assertEquals(5, result.length());
   }

   @Test
   public void testRandomWithOnlyLetters() {
      String result = RandomStringUtils.random(5, true, false);
      assertNotNull(result);
      assertEquals(5, result.length());
   }

   @Test
   public void testRandomWithOnlyNumbers() {
      String result = RandomStringUtils.random(5, false, true);
      assertNotNull(result);
      assertEquals(5, result.length());
   }

   @Test
   public void testRandomWithChars() {
      String result = RandomStringUtils.random(5, new char[] {'a', 'b', 'c'});
      assertNotNull(result);
      assertEquals(5, result.length());
   }

   @Test
   public void testRandomWithEmptyChars() {
      String result = RandomStringUtils.random(5, new char[0]);
      assertNotNull(result);
      assertEquals(5, result.length());
   }

   @Test(expected = IllegalArgumentException.class)
   public void testRandomNegativeCount() {
      RandomStringUtils.random(-5);
   }

   @Test
   public void testRandomWithStartAndEnd() {
      String result = RandomStringUtils.random(5, 32, 127, false, false);
      assertNotNull(result);
      assertEquals(5, result.length());
   }

   @Test
   public void testRandomWithStartAndEndWithLettersAndNumbers() {
      String result = RandomStringUtils.random(5, 32, 127, true, true);
      assertNotNull(result);
      assertEquals(5, result.length());
   }

   @Test
   public void testRandomWithStartAndEndWithOnlyLetters() {
      String result = RandomStringUtils.random(5, 32, 127, true, false);
      assertNotNull(result);
      assertEquals(5, result.length());
   }

   @Test
   public void testRandomWithStartAndEndWithOnlyNumbers() {
      String result = RandomStringUtils.random(5, 32, 127, false, true);
      assertNotNull(result);
      assertEquals(5, result.length());
   }

   @Test
   public void testRandomWithStartAndEndWithChars() {
      String result = RandomStringUtils.random(5, 32, 127, new char[] {'a', 'b', 'c'});
      assertNotNull(result);
      assertEquals(5, result.length());
   }

   @Test
   public void testRandomWithStartAndEndWithEmptyChars() {
      String result = RandomStringUtils.random(5, 32, 127, new char[0]);
      assertNotNull(result);
      assertEquals(5, result.length());
   }

   @Test(expected = IllegalArgumentException.class)
   public void testRandomWithStartAndEndNegativeCount() {
      RandomStringUtils.random(-5, 32, 127, false, false);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testRandomWithEmptyCharArray() {
      RandomStringUtils.random(5, new char[0], null);
   }

   @Test(expected = ArrayIndexOutOfBoundsException.class)
   public void testRandomWithInvalidChars() {
      RandomStringUtils.random(5, 1000, 2000, false, false, new char[] {'a', 'b', 'c'}, null);
   }

   @Test
   public void testRandomWithStringChars() {
      String result = RandomStringUtils.random(5, "abc");
      assertNotNull(result);
      assertEquals(5, result.length());
   }

   @Test
   public void testRandomWithEmptyStringChars() {
      String result = RandomStringUtils.random(5, "");
      assertNotNull(result);
      assertEquals(5, result.length());
   }

   @Test(expected = IllegalArgumentException.class)
   public void testRandomWithNegativeCountAndStringChars() {
      RandomStringUtils.random(-5, "abc");
   }

}