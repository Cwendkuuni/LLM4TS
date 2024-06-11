import org.junit.Assert;
import org.junit.Test;
import org.scribe.model.Verifier;

public class VerifierTest {

  @Test
  public void testGetValue() {
    Verifier verifier = new Verifier("test");
    Assert.assertEquals("test", verifier.getValue());
  }

  @Test(expected = NullPointerException.class)
  public void testNullValue() {
    Verifier verifier = new Verifier(null);
    verifier.getValue();
  }

  @Test
  public void testEmptyValue() {
    Verifier verifier = new Verifier("");
    Assert.assertEquals("", verifier.getValue());
  }
  
  @Test
  public void testLongValue() {
    String longValue = "ThisIsALongValueThatExceedsTheLimitOfFiftyCharacters";
    Verifier verifier = new Verifier(longValue);
    Assert.assertEquals(longValue, verifier.getValue());
  }

  @Test
  public void testSpecialCharactersValue() {
    String specialCharacters = "!@#$%^&*()_+-={}[]|\\:\";'<>?,./";
    Verifier verifier = new Verifier(specialCharacters);
    Assert.assertEquals(specialCharacters, verifier.getValue());
  }

  @Test
  public void testNumbersValue() {
    String numbers = "0123456789";
    Verifier verifier = new Verifier(numbers);
    Assert.assertEquals(numbers, verifier.getValue());
  }

}