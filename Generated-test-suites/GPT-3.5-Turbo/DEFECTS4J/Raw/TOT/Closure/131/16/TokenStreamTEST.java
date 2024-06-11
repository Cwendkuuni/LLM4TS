import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TokenStreamTest {

  private TokenStream tokenStream;

  @Before
  public void setUp() {
    tokenStream = new TokenStream();
  }

  @Test
  public void testIsKeyword_WithEmptyString_ReturnsFalse() {
    boolean result = tokenStream.isKeyword("");
    Assert.assertFalse(result);
  }

  @Test
  public void testIsKeyword_WithValidKeyword_ReturnsTrue() {
    boolean result = tokenStream.isKeyword("public");
    Assert.assertTrue(result);
  }

  @Test
  public void testIsKeyword_WithInvalidKeyword_ReturnsFalse() {
    boolean result = tokenStream.isKeyword("invalid");
    Assert.assertFalse(result);
  }

  @Test
  public void testIsJSIdentifier_WithEmptyString_ReturnsFalse() {
    boolean result = tokenStream.isJSIdentifier("");
    Assert.assertFalse(result);
  }

  @Test
  public void testIsJSIdentifier_WithValidIdentifier_ReturnsTrue() {
    boolean result = tokenStream.isJSIdentifier("test123");
    Assert.assertTrue(result);
  }

  @Test
  public void testIsJSIdentifier_WithInvalidIdentifier_ReturnsFalse() {
    boolean result = tokenStream.isJSIdentifier("1test");
    Assert.assertFalse(result);
  }
}