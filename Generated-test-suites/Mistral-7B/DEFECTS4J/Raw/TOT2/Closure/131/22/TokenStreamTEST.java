import static com.google.javascript.rhino.TokenStream.isKeyword;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TokenStreamTest {
  @Test
  public void testIsKeyword() {
    // Basic test case - valid keywords
    assertEquals(true, isKeyword("if"));
    assertEquals(true, isKeyword("else"));
    assertEquals(true, isKeyword("while"));
    assertEquals(true, isKeyword("for"));
    assertEquals(true, isKeyword("switch"));
    assertEquals(true, isKeyword("case"));
    assertEquals(true, isKeyword("break"));
    assertEquals(true, isKeyword("default"));
    assertEquals(true, isKeyword("try"));
    assertEquals(true, isKeyword("catch"));
    assertEquals(true, isKeyword("throw"));
    assertEquals(true, isKeyword("return"));
    assertEquals(true, isKeyword("class"));
    assertEquals(true, isKeyword("var"));
    assertEquals(true, isKeyword("function"));
    assertEquals(true, isKeyword("export"));

    // Test edge cases
    assertEquals(false, isKeyword(""));
    assertEquals(false, isKeyword(" "));
    assertEquals(false, isKeyword("1"));
    assertEquals(false, isKeyword("123"));
    assertEquals(false, isKeyword("_"));
    assertEquals(false, isKeyword("abc"));
    assertEquals(false, isKeyword("abcd"));
    assertEquals(false, isKeyword("ABC"));
    assertEquals(false, isKeyword("ABCD"));
    assertEquals(false, isKeyword("123abc"));
    assertEquals(false, isKeyword("_123"));

    // Test exception handling
    try {
      isKeyword(null);
    } catch (NullPointerException e) {
      // expected
    }
  }
}