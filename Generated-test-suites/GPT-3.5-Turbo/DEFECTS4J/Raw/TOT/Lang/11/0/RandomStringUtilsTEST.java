import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RandomStringUtilsTest {

  RandomStringUtils randomStringUtils;

  @Before
  public void setup() {
    randomStringUtils = new RandomStringUtils();
  }

  @Test
  public void testRandom() {
    // Typical use case - count=5
    String randomString = randomStringUtils.random(5);
    assertEquals(5, randomString.length());

    // Edge case - count=0
    randomString = randomStringUtils.random(0);
    assertEquals(0, randomString.length());
  }

  @Test
  public void testRandomAscii() {
    // Typical use case - count=5
    String randomString = randomStringUtils.randomAscii(5);
    assertEquals(5, randomString.length());

    // Edge case - count=0
    randomString = randomStringUtils.randomAscii(0);
    assertEquals(0, randomString.length());
  }

  @Test
  public void testRandomAlphabetic() {
    // Typical use case - count=5
    String randomString = randomStringUtils.randomAlphabetic(5);
    assertEquals(5, randomString.length());

    // Edge case - count=0
    randomString = randomStringUtils.randomAlphabetic(0);
    assertEquals(0, randomString.length());
  }

  @Test
  public void testRandomAlphanumeric() {
    // Typical use case - count=5
    String randomString = randomStringUtils.randomAlphanumeric(5);
    assertEquals(5, randomString.length());

    // Edge case - count=0
    randomString = randomStringUtils.randomAlphanumeric(0);
    assertEquals(0, randomString.length());
  }

  @Test
  public void testRandomNumeric() {
    // Typical use case - count=5
    String randomString = randomStringUtils.randomNumeric(5);
    assertEquals(5, randomString.length());

    // Edge case - count=0
    randomString = randomStringUtils.randomNumeric(0);
    assertEquals(0, randomString.length());
  }

  @Test
  public void testRandomWithLettersAndNumbers() {
    // Typical use case - count=5, letters=true, numbers=true
    String randomString = randomStringUtils.random(5, true, true);
    assertEquals(5, randomString.length());

    // Edge case - count=0, letters=true, numbers=true
    randomString = randomStringUtils.random(0, true, true);
    assertEquals(0, randomString.length());

    // Edge case - count=0, letters=false, numbers=false
    randomString = randomStringUtils.random(0, false, false);
    assertEquals(0, randomString.length());
  }

  @Test
  public void testRandomWithStartAndEnd() {
    // Typical use case - count=5, start=65, end=90 (A-Z)
    String randomString = randomStringUtils.random(5, 65, 90, true, false);
    assertEquals(5, randomString.length());

    // Edge case - count=0, start=65, end=90 (A-Z)
    randomString = randomStringUtils.random(0, 65, 90, true, false);
    assertEquals(0, randomString.length());

    // Edge case - count=5, start=0, end=127 (ASCII)
    randomString = randomStringUtils.random(5, 0, 127, false, false);
    assertEquals(5, randomString.length());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRandomWithNegativeCount() {
    // Negative count
    randomStringUtils.random(-5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testRandomWithEmptyCharsArray() {
    // Empty chars array
    randomStringUtils.random(5, new char[] {});
  }

  @Test
  public void testRandomWithStringChars() {
    // Typical use case - count=5, chars="ABC"
    String randomString = randomStringUtils.random(5, "ABC");
    assertEquals(5, randomString.length());

    // Edge case - count=0, chars="ABC"
    randomString = randomStringUtils.random(0, "ABC");
    assertEquals(0, randomString.length());
  }

}