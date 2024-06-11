import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class RandomStringUtilsTest {

 @Test
 public void testRandomString() {
 String randomString = RandomStringUtils.random(5);
 assertNotNull(randomString);
 }

 @Test
 public void testRandomAsciiString() {
 String randomAsciiString = RandomStringUtils.randomAscii(5);
 assertNotNull(randomAsciiString);
 }

 @Test
 public void testRandomAlphabeticString() {
 String randomAlphabeticString = RandomStringUtils.randomAlphabetic(5);
 assertNotNull(randomAlphabeticString);
 }

 @Test
 public void testRandomAlphanumericString() {
 String randomAlphanumericString = RandomStringUtils.randomAlphanumeric(5);
 assertNotNull(randomAlphanumericString);
 }

 @Test
 public void testRandomNumericString() {
 String randomNumericString = RandomStringUtils.randomNumeric(5);
 assertNotNull(randomNumericString);
 }

 @Test
 public void testRandomStringWithRange() {
 String randomStringWithRange = RandomStringUtils.random(5, 1, 10);
 assertNotNull(randomStringWithRange);
 }

 @Test
 public void testRandomAlphabeticStringWithRange() {
 String randomAlphabeticStringWithRange = RandomStringUtils.randomAlphabetic(5, 'a', 'z');
 assertNotNull(randomAlphabeticStringWithRange);
 }

 @Test
 public void testRandomAlphanumericStringWithRange() {
 char[] chars = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
 String randomAlphanumericStringWithRange = RandomStringUtils.random(5, 0, chars.length, true, true, null, RANDOM);
 assertNotNull(randomAlphanumericStringWithRange);
 }

 @Test
 public void testRandomStringWithCustomCharacterSet() {
 char[] customChars = new char[]{'A', 'B', 'C'};
 String randomStringWithCustomCharacterSet = RandomStringUtils.random(5, customChars);
 assertNotNull(randomStringWithCustomCharacterSet);
 }

 @Test
 public void testRandomStringWithLengthZero() {
 String randomStringWithLengthZero = RandomStringUtils.random(0);
 assertNotNull(randomStringWithLengthZero);
 assertEquals("", randomStringWithLengthZero);
 }
}