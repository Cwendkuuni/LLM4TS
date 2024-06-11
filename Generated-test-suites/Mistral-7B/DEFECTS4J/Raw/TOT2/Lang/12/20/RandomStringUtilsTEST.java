import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class RandomStringUtilsTest {

 // Test case for random method with default options
 @Test
 public void testRandomString() {
 int count = 10;
 String randomString = RandomStringUtils.random(count);
 assertNotNull(randomString);
 assertEquals(count, randomString.length());
 }

 // Test case for randomAscii method
 @Test
 public void testRandomAsciiString() {
 int count = 10;
 String randomAsciiString = RandomStringUtils.randomAscii(count);
 assertNotNull(randomAsciiString);
 assertEquals(count, randomAsciiString.length());
 }

 // Test case for randomAlphabetic method
 @Test
 public void testRandomAlphabeticString() {
 int count = 10;
 String randomAlphabeticString = RandomStringUtils.randomAlphabetic(count);
 assertNotNull(randomAlphabeticString);
 assertEquals(count, randomAlphabeticString.length());
 }

 // Test case for randomAlphanumeric method
 @Test
 public void testRandomAlphanumericString() {
 int count = 10;
 String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(count);
 assertNotNull(randomAlphanumericString);
 assertEquals(count, randomAlphanumericString.length());
 }

 // Test case for randomNumeric method
 @Test
 public void testRandomNumericString() {
 int count = 10;
 String randomNumericString = RandomStringUtils.randomNumeric(count);
 assertNotNull(randomNumericString);
 assertEquals(count, randomNumericString.length());
 }

 // Test case for random method with custom options
 @Test
 public void testRandomCustomString() {
 int count = 10;
 String randomCustomString = RandomStringUtils.random(count, true, true);
 assertNotNull(randomCustomString);
 assertEquals(count, randomCustomString.length());
 }

 // Test case for random method with start and end options
 @Test
 public void testRandomStringWithStartAndEnd() {
 int count = 10;
 int start = 97;
 int end = 122;
 String randomString = RandomStringUtils.random(count, start, end, true, false);
 assertNotNull(randomString);
 assertEquals(count, randomString.length());
 }

 // Test case for random method with empty character array
 @Test(expected = IllegalArgumentException.class)
 public void testRandomStringWithEmptyCharArray() {
 RandomStringUtils.random(5, new char[0]);
 }

 // Test case for random method with negative count
 @Test(expected = IllegalArgumentException.class)
 public void testRandomStringWithNegativeCount() {
 RandomStringUtils.random(-5);
 }

 // Test case for random method with invalid start and end
 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testRandomStringWithInvalidStartAndEnd() {
 int start = 123;
 int end = 97;
 RandomStringUtils.random(5, start, end, true, false);
 }

 // Test case for random method with invalid set of characters
 @Test(expected = IllegalArgumentException.class)
 public void testRandomStringWithInvalidCharacterSet() {
 RandomStringUtils.random(5, null);
 }

 // Test case for random method with invalid random instance
 @Test(expected = IllegalArgumentException.class)
 public void testRandomStringWithInvalidRandomInstance() {
 Random random = new Random();
 random.setSeed(1L);
 String randomString = RandomStringUtils.random(5, 0, 0, true, true, null, random);
 }
}