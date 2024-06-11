import org.junit.Assert;
import org.junit.Test;

public class RandomStringUtilsTest {
  
  @Test
  public void testRandom() {
    Assert.assertNotNull(RandomStringUtils.random(5));
  }
  
  @Test
  public void testRandomAscii() {
    Assert.assertNotNull(RandomStringUtils.randomAscii(5));
  }
  
  @Test
  public void testRandomAlphabetic() {
    Assert.assertNotNull(RandomStringUtils.randomAlphabetic(5));
  }
  
  @Test
  public void testRandomAlphanumeric() {
    Assert.assertNotNull(RandomStringUtils.randomAlphanumeric(5));
  }
  
  @Test
  public void testRandomNumeric() {
    Assert.assertNotNull(RandomStringUtils.randomNumeric(5));
  }
  
  @Test
  public void testRandomWithLettersAndNumbers() {
    Assert.assertNotNull(RandomStringUtils.random(5, true, true));
  }
  
  @Test
  public void testRandomWithCustomChars() {
    Assert.assertNotNull(RandomStringUtils.random(5, 32, 127, false, false, null));
  }
  
  @Test
  public void testRandomWithCustomCharsAndRange() {
    Assert.assertNotNull(RandomStringUtils.random(5, 32, 127, true, true, new char[] {'a', 'b', 'c'}));
  }
  
  @Test
  public void testRandomWithCustomCharsAndRandom() {
    Assert.assertNotNull(RandomStringUtils.random(5, 32, 127, false, false, new char[] {'a', 'b', 'c'}, new Random()));
  }
  
  @Test
  public void testRandomWithInvalidLength() {
    try {
      RandomStringUtils.random(-1);
      Assert.fail("IllegalArgumentException expected");
    } catch (IllegalArgumentException e) {
      // pass
    }
  }
  
  @Test
  public void testRandomWithInvalidChars() {
    try {
      RandomStringUtils.random(5, new char[] {});
      Assert.fail("IllegalArgumentException expected");
    } catch (IllegalArgumentException e) {
      // pass
    }
  }
  
  @Test
  public void testRandomWithEmptyString() {
    try {
      RandomStringUtils.random(5, "");
      Assert.fail("IllegalArgumentException expected");
    } catch (IllegalArgumentException e) {
      // pass
    }
  }
  
  @Test
  public void testRandomWithNullString() {
    try {
      RandomStringUtils.random(5, (String) null);
      Assert.fail("IllegalArgumentException expected");
    } catch (IllegalArgumentException e) {
      // pass
    }
  }
  
  @Test
  public void testRandomWithNullChars() {
    try {
      RandomStringUtils.random(5, (char[]) null);
      Assert.fail("IllegalArgumentException expected");
    } catch (IllegalArgumentException e) {
      // pass
    }
  }
}